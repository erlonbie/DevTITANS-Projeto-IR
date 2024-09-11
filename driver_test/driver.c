#include <linux/module.h>
#include <linux/usb.h>
#include <linux/slab.h>

MODULE_AUTHOR("DevTITANS <devtitans@icomp.ufam.edu.br>");
MODULE_DESCRIPTION("Driver for DevTitans IR Project (ESP32 Serial CP2102");
MODULE_LICENSE("GPL");


#define MAX_RECV_LINE 100  // Max response length
#define VENDOR_ID   0x10c4
#define PRODUCT_ID  0xea60

static char recv_line[MAX_RECV_LINE];  // Stores USB response until end of line
static struct usb_device *infrared_device;  // USB device reference
static uint usb_in, usb_out;  // USB I/O ports address.
static char *usb_in_buffer, *usb_out_buffer;  // USB I/O Buffer
static int usb_max_size;  // Max USB message size

static const struct usb_device_id id_table[] = { { USB_DEVICE(VENDOR_ID, PRODUCT_ID) }, {} };

static int  usb_probe(struct usb_interface *ifce, const struct usb_device_id *id);  // Execs when device connects to USB
static void usb_disconnect(struct usb_interface *ifce);  // Execs when device disconnects from USB
static int  usb_read_serial(void);

// Execs when /sys/kernel/infrared/{freq,data} is read (e.g., cat /sys/kernel/infrared/freq)
static ssize_t attr_show(struct kobject *sys_obj, struct kobj_attribute *attr, char *buff);
// Execs when /sys/kernel/infrared/{freq, data} is written (e.g., echo "100" | sudo tee -a /sys/kernel/infrared/freq)
static ssize_t attr_store(struct kobject *sys_obj, struct kobj_attribute *attr, const char *buff, size_t count);   
// Var to create /sys/kernel/infrared/freq
static struct kobj_attribute  freq_attribute = __ATTR(freq, S_IRUGO | S_IWUSR, attr_show, attr_store);
static struct kobj_attribute  data_attribute = __ATTR(data, S_IRUGO | S_IWUSR, attr_show, attr_store);
static struct attribute      *attrs[]       = { &freq_attribute.attr, &data_attribute.attr, NULL };
static struct attribute_group attr_group    = { .attrs = attrs };
static struct kobject        *sys_obj;

MODULE_DEVICE_TABLE(usb, id_table);

bool ignore = true;
int freq_value = 0;


static struct usb_driver infrared_driver = {
    .name        = "infrared",
    .probe       = usb_probe,
    .disconnect  = usb_disconnect,
    .id_table    = id_table,  // VendorID ProductID table
};


module_usb_driver(device_driver);


// Execs when device connects to USB
static int usb_probe(struct usb_interface *interface, const struct usb_device_id *id) {
    struct usb_endpoint_descriptor *usb_endpoint_in, *usb_endpoint_out;
    printk(KERN_INFO "[InfraRed] Device connected. ...\n");

    // Creates /sys/kernel/infrared/freq file
    sys_obj = kobject_create_and_add("infrared", kernel_kobj);
    ignore = sysfs_create_group(sys_obj, &attr_group); // AQUI

    // Detects ports and initiate USB I/O buffers
    infrared_device = interface_to_usbdev(interface);
    ignore =  usb_find_common_endpoints(interface->cur_altsetting, &usb_endpoint_in, &usb_endpoint_out, NULL, NULL);
    usb_max_size = usb_endpoint_maxp(usb_endpoint_in);
    usb_in = usb_endpoint_in->bEndpointAddress;
    usb_out = usb_endpoint_out->bEndpointAddress;
    usb_in_buffer = kmalloc(usb_max_size, GFP_KERNEL);
    usb_out_buffer = kmalloc(usb_max_size, GFP_KERNEL);

    freq_value = usb_read_serial();

    printk("InfraRed Frequency Value: %d\n", freq_value);
    return 0;
}


// Execs when device disconnects from USB
static void usb_disconnect(struct usb_interface *interface) {
    printk(KERN_INFO "[InfraRed] Device disconnected\n");
    if (sys_obj) kobject_put(sys_obj);      // Remove os arquivos em /sys/kernel/smartlamp
    kfree(usb_in_buffer);                   // Desaloca buffers
    kfree(usb_out_buffer);
}


// Sends USB command, awaits and returns response
// Command example:  SET_FREQ 455
// Response example: RES SET_FREQ 1
// Function call example for usb_send_cmd with command SET_FREQ: usb_send_cmd("SET_FREQ", 455);
static int usb_send_cmd(char *cmd, int param) {
    int recv_size = 0;                      // Char length in recv_line
    int ret, actual_size, i;
    int retries = 10;                       // Max tries for sending command
    char resp_expected[MAX_RECV_LINE];      // Expected response for given command
    char *resp_pos;                         // Response value index in response line
    long resp_number = -1;                  // Frequency value returned by device

    printk(KERN_INFO "[InfraRed] Command sent: %s\n", cmd);


    // Sends command (usb_out_buffer) to USB
    ret = usb_bulk_msg(infrared_device, usb_sndbulkpipe(infrared_device, usb_out), BUFFER, ?, &actual_size, 1000);
    if (ret) {
        printk(KERN_ERR "[InfraRed] Error while sending command. Exit code: %d\n", ret);
        return -1;
    }

    sprintf(resp_expected, "RES %s", cmd);  // Expected response format. Keeps reading until expected response is received

    // Awaits for correct response (Gives up after <retries variable> times)
    while (retries > 0) {
        // Read data from USB
        ret = usb_bulk_msg(infrared_device, usb_rcvbulkpipe(infrared_device, usb_in), usb_in_buffer, min(usb_max_size, MAX_RECV_LINE), &actual_size, 1000);
        if (ret) {
            printk(KERN_ERR "[InfraRed] Error while reading from USB (attempt %d). Exit code: %d\n", ret, retries--);
            continue;
        }

        char *start = strstr(usb_in_buffer, "RES "); // Returns first ocurrence of "RES_FREQ " in usb_in_buffer string
        if (!start) {
            printk(KERN_ERR "[InfraRed] Invalid message\n");
            continue;
        }

        // Gets value after RES_FREQ
        start += strlen("RES "); // Moves pointer to value position
        int value = atoi(start);

        return value;
    }
    return -1; // Unexpected response
}


static ssize_t attr_show(struct kobject *sys_obj, struct kobj_attribute *attr, char *buff) {
    int value;  // Represents infrared frequency value
    const char *attr_name = attr->attr.name;  // attr_name represents file name

    // Indicates which file is being read
    printk(KERN_INFO "[InfraRed] Reading from %s file\n", attr_name);

    sprintf(buff, "%d\n", value);  // Creates message with frequency value
    return strlen(buff);
}


static ssize_t attr_store(struct kobject *sys_obj, struct kobj_attribute *attr, const char *buff, size_t count) {
    long ret, value;
    const char *attr_name = attr->attr.name;

    // Converts value to long int
    ret = kstrtol(buff, 10, &value);
    if (ret) {
        printk(KERN_ALERT "[InfraRed] Value %s is invalid\n", attr_name);
        return -EACCES;
    }

    printk(KERN_INFO "[InfraRed] Writing %s with %ld ...\n", attr_name, value);

    if (ret < 0) {
        printk(KERN_ALERT "[InfraRed] Error while writing to %s.\n", attr_name);
        return -EACCES;
    }

    return strlen(buff);
}
