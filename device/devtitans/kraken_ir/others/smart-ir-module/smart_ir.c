#include <linux/module.h>
#include <linux/usb.h>
#include <linux/slab.h>

MODULE_AUTHOR("DevTITANS <devtitans@icomp.ufam.edu.br>");
MODULE_DESCRIPTION("Driver for DevTitans IR Project (ESP32 Serial CP2102");
MODULE_LICENSE("GPL");

// Max response length
#define MAX_RECV_LINE 100

static int  usb_probe(struct usb_interface *ifce, const struct usb_device_id *id);  // Execs when device connects to USB
static void usb_disconnect(struct usb_interface *ifce);  // Execs when device disconnects from USB
static int  usb_send_cmd(char *cmd, int param);  // Sends USB command and waits/returns device response (int)
// Execs when /sys/kernel/smart_ir/{ir_transmit, ir_receive} is read (e.g., cat /sys/kernel/smart_ir/ir_transmit)
static ssize_t attr_show(struct kobject *sys_obj, struct kobj_attribute *attr, char *buff);
// Execs when /sys/kernel/smart_ir/{ir_transmit, ir_receive} is written (e.g., echo "100" | sudo tee -a /sys/kernel/smart_ir/ir_transmit)
static ssize_t attr_store(struct kobject *sys_obj, struct kobj_attribute *attr, const char *buff, size_t count);   

static char recv_line[MAX_RECV_LINE];  // Stores USB response until end of line
static struct usb_device *smart_ir_device;  // USB device reference
static uint usb_in, usb_out;  // USB I/O ports address.
static char *usb_in_buffer, *usb_out_buffer;  // USB I/O Buffer
static int usb_max_size;  // Max USB message size

// Var to create /sys/kernel/smartlamp/{ir_transmit, ir_receive} files
static struct kobj_attribute  ir_transmit_attribute = __ATTR(ir_transmit, S_IRUGO | S_IWUSR, attr_show, attr_store);
static struct kobj_attribute  ir_receive_attribute = __ATTR(ir_receive, S_IRUGO | S_IWUSR, attr_show, attr_store);

static struct attribute      *attrs[]       = { &ir_transmit_attribute.attr, &ir_receive_attribute.attr, NULL };
static struct attribute_group attr_group    = { .attrs = attrs };
static struct kobject        *sys_obj;

// Registers CP2102 (Chip USB-Serial for ESP32) in kernel's USB-Core
#define VENDOR_ID   0x10c4  // CP2102 VendorID
#define PRODUCT_ID  0xea60  // CP2102 ProductID
static const struct usb_device_id id_table[] = { { USB_DEVICE(VENDOR_ID, PRODUCT_ID) }, {} };
MODULE_DEVICE_TABLE(usb, id_table);
bool ignore = true;

// Creates and registers smart_ir driver in kernel
static struct usb_driver smart_ir_driver = {
    .name        = "smart_ir",
    .probe       = usb_probe,  // Executes when connected to USB
    .disconnect  = usb_disconnect,  // Executes when disconnected USB
    .id_table    = id_table,  // VendorID ProductID table
};
module_usb_driver(smart_ir_driver);

// Execs when device connects to USB
static int usb_probe(struct usb_interface *interface, const struct usb_device_id *id) {
    struct usb_endpoint_descriptor *usb_endpoint_in, *usb_endpoint_out;

    printk(KERN_INFO "[SmartIR] Device connected. ...\n");

    // Creates /sys/kernel/smart_ir/* files
    sys_obj = kobject_create_and_add("smart_ir", kernel_kobj);
    ignore = sysfs_create_group(sys_obj, &attr_group);

    // Detects ports and initiate USB I/O buffers
    smart_ir_device = interface_to_usbdev(interface);
    ignore =  usb_find_common_endpoints(interface->cur_altsetting, &usb_endpoint_in, &usb_endpoint_out, NULL, NULL);
    usb_max_size = usb_endpoint_maxp(usb_endpoint_in);
    usb_in = usb_endpoint_in->bEndpointAddress;
    usb_out = usb_endpoint_out->bEndpointAddress;
    usb_in_buffer = kmalloc(usb_max_size, GFP_KERNEL);
    usb_out_buffer = kmalloc(usb_max_size, GFP_KERNEL);

    return 0;
}

// Execs when device disconnects from USB
static void usb_disconnect(struct usb_interface *interface) {
    printk(KERN_INFO "[SmartIR] Device disconnected\n");
    if (sys_obj) kobject_put(sys_obj);      // Remove /sys/kernel/smart_ir/* files
    kfree(usb_in_buffer);                   // Unallocated buffers
    kfree(usb_out_buffer);
}

// Sends USB command, awaits and returns response
// Command example:  SET_FREQ 455
// Response example: RES SET_FREQ 1
static int usb_send_cmd(char *cmd, int param) {
    int recv_size = 0;                      // Char length in recv_line
    int ret, actual_size, i;
    int retries = 10;                       // Max tries for sending command
    char resp_expected[MAX_RECV_LINE];      // Expected response for given command
    char *resp_pos;                         // Response value index in response line
    long resp_number = -1;                  // Frequency value returned by device

    printk(KERN_INFO "[SmartIR] Command sent: %s\n", cmd);

    if (param >= 0) sprintf(usb_out_buffer, "%s %d\n", cmd, param); // If param >=0, o comando has a parameter (int)
    else sprintf(usb_out_buffer, "%s\n", cmd);                      // Else, its command only

    // Sends command (usb_out_buffer) to USB
    ret = usb_bulk_msg(smart_ir_device, usb_sndbulkpipe(smart_ir_device, usb_out), usb_out_buffer, strlen(usb_out_buffer), &actual_size, 1000*HZ);
    if (ret) {
        printk(KERN_ERR "[SmartIR] Error while sending command. Exit code: %d\n", ret);
        return -1;
    }

    sprintf(resp_expected, "RES %s", cmd);  // Expected response format. Keeps reading until expected response is received

    // Awaits for correct response (Gives up after <retries variable> times)
    while (retries > 0) {
        // Read data from USB
        ret = usb_bulk_msg(smart_ir_device, usb_rcvbulkpipe(smart_ir_device, usb_in), usb_in_buffer, min(usb_max_size, MAX_RECV_LINE), &actual_size, HZ*1000);
        if (ret) {
            printk(KERN_ERR "[SmartIR] Error while reading from USB (attempt %d). Exit code: %d\n", retries--, ret);
            continue;
        }

        // For received char ...
        for (i=0; i<actual_size; i++) {

            if (usb_in_buffer[i] == '\n') {  // Complete line
                recv_line[recv_size] = '\0';
                printk(KERN_INFO "[SmartIR] Line received: '%s'\n", recv_line);

                // Verifies if start of received line matches expected response from sent command
                if (!strncmp(recv_line, resp_expected, strlen(resp_expected))) {
                    printk(KERN_INFO "[SmartIR] Line is response for %s! Processing ...\n", cmd);

                    // Access response value and converts to integer
                    resp_pos = &recv_line[strlen(resp_expected) + 1];
                    ignore = kstrtol(resp_pos, 10, &resp_number);

                    return resp_number;
                }
                else { // Not an expected line, retrieving next line
                    printk(KERN_INFO "[SmartIR] Not an expected response for %s! (Attempt %d) Next line...\n", cmd, retries--);
                    recv_size = 0; // Clears read line (recv_line)
                }
            }
            else { // Common character (not new line), add it to recv_line and get next char
                recv_line[recv_size] = usb_in_buffer[i];
                recv_size++;
            }
        }
    }
    return -1; // Did not receive expected response from device
}

// Execs when /sys/kernel/smart_ir/{ir_transmit, ir_receive} is read (e.g., cat /sys/kernel/smart_ir/ir_transmit)
static ssize_t attr_show(struct kobject *sys_obj, struct kobj_attribute *attr, char *buff) {
    int value;
    const char *attr_name = attr->attr.name;

    printk(KERN_INFO "[SmartIR] Reading %s ...\n", attr_name);

    if (!strcmp(attr_name, "ir_transmit"))
        value = usb_send_cmd("GET_IR_TRANSMIT", -1);
    else (!strcmp(attr_name, "ir_receive"))
        value = usb_send_cmd("GET_IR_RECEIVE", -1);
    else
        printk(KERN_ALERT "[SmartIR] The only readable attributes are 'ir_transmit' and 'ir_receive'\n");

    sprintf(buff, "%d\n", value);  // Creates message with ir_transmit or ir_receive values
    return strlen(buff);
}

// Execs when /sys/kernel/smart_ir/{ir_transmit, ir_receive} is written (e.g., echo "100" | sudo tee -a /sys/kernel/smart_ir/ir_transmit)
static ssize_t attr_store(struct kobject *sys_obj, struct kobj_attribute *attr, const char *buff, size_t count) {
    long ret, value;
    const char *attr_name = attr->attr.name;

    ret = kstrtol(buff, 10, &value);
    if (ret) {
        printk(KERN_ALERT "[SmartIR] Value %s invalid.\n", attr_name);
        return -EACCES;
    }

    printk(KERN_INFO "[SmartIR] Setting %s to %ld ...\n", attr_name, value);

    if (!strcmp(attr_name, "ir_transmit"))
        ret = usb_send_cmd("SET_IR_TRANSMIT", value);
    else if (!strcmp(attr_name, "ir_receive"))
        ret = usb_send_cmd("SET_IR_RECEIVE", value);
    else {
        printk(KERN_ALERT "[SmartIR] The only writable attributes are 'ir_transmit' and 'ir_receive'\n");
        return -EACCES;
    }
    if (ret < 0) {
        printk(KERN_ALERT "[SmartIR] Error while writing value to %s.\n", attr_name);
        return -EACCES;
    }

    return strlen(buff);
}
