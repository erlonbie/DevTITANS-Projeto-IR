#include <Arduino.h>

#define IRSND_IR_FREQUENCY 38000
#define IRSND_PROTOCOL_NAMES 1
#define IRSND_OUTPUT_PIN 23
#define IRMP_INPUT_PIN 22 
#define LED_BUILTIN 2

#include <irsndSelectMain15Protocols.h>
#include <irsnd.hpp>
IRMP_DATA irsnd_data;


void setup() {
    pinMode(LED_BUILTIN, OUTPUT);

    Serial.begin(115200);
    while (!Serial)
        ;

    Serial.println(F("START " __FILE__ " from " __DATE__ "\r\nUsing library version " VERSION_IRMP));

    irsnd_init();
    irmp_irsnd_LEDFeedback(true);

    Serial.println("Send IR signals at pin");
    Serial.println(IRSND_OUTPUT_PIN);
}

void loop() {

  String serialCommand;

  while (Serial.available() > 0) {
      char serialChar = Serial.read();
      serialCommand += serialChar; 

      if (serialChar == '\n') {
        processCommand(serialCommand);
        serialCommand = "";
      }
  }

  delay(100);
}

void processCommand(String command) {
    command.trim();
    command.toUpperCase();

    if (command.startsWith("SET_IR_TRANSMIT ")) {

        command = command.substring(10);
        
        // Dividir a mensagem usando o delimitador "_"
        int firstSeparatorIndex = command.indexOf('_');
        int secondSeparatorIndex = command.lastIndexOf('_');

        // Separar protocolo, endereço e comando
        String protocolo = command.substring(0, firstSeparatorIndex);
        String endereco = command.substring(firstSeparatorIndex + 1, secondSeparatorIndex);
        String comando = command.substring(secondSeparatorIndex + 1);

        // Exibir os resultados
        // Serial.println("Protocolo: " + protocolo);
        // Serial.println("Endereço: " + endereco);
        // Serial.println("Comando: " + comando);

        long hexAddress = strtol(endereco.c_str(), NULL, 16);
        long hexCommand = strtol(comando.c_str(), NULL, 16);
        uint8_t protocolCode = (uint8_t) strtoul(protocolo.c_str(), NULL, 16);
        // Serial.print("Enviando código IR: ");
        // Serial.println(hexAddress);
        // Serial.println(hexCommand);

        irsnd_data.protocol = protocolCode;
        irsnd_data.address = hexAddress;
        irsnd_data.command = hexCommand;
        irsnd_data.flags = 2;

        irsnd_send_data(&irsnd_data, true);
        irsnd_data_print(&Serial, &irsnd_data);
        
        Serial.println("RES SET_IR_TRANSMIT 1");
    }

    // else if (command == "GET_CODE") {
    //     IrReceiver.start();
    //     while(!IrReceiver.decode()) {

    //     }
    //     if (IrReceiver.decode()) {
    //         if (IrReceiver.decodedIRData.protocol == UNKNOWN) {
    //             Serial.println(F("Received noise or an unknown (or not yet enabled) protocol"));
    //             // We have an unknown protocol here, print extended info
    //             IrReceiver.printIRResultRawFormatted(&Serial, true);
    //             IrReceiver.resume(); // Do it here, to preserve raw data for printing with printIRResultRawFormatted()
    //         } else {
    //             IrReceiver.resume(); // Early enable receiving of the next IR frame
    //             IrReceiver.printIRResultShort(&Serial);
    //             IrReceiver.printIRSendUsage(&Serial);
    //         }
    //     Serial.println();
    //     }
    // }
    
    else
      Serial.println("ERR Unknown command.");
      
}