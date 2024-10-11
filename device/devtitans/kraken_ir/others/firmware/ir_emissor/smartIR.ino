#include <Arduino.h>
#include <IRremote.hpp>
// #include "PinDefinitionsAndMore.h"

#include "TinyIRSender.hpp"
#define DECODE_NEC
#define IR_SEND_PIN 23
#define IR_RECEIVE_PIN 21
#define LED_BUILTIN 2

void setup() {
    pinMode(LED_BUILTIN, OUTPUT);

    Serial.begin(115200);
    while (!Serial)
        ;

    Serial.println(F("START " __FILE__ " from " __DATE__ "\r\nUsing library version " VERSION_TINYIR));
    Serial.print(F("Send IR signals at pin "));
    Serial.println(IR_SEND_PIN);
    IrReceiver.begin(IR_RECEIVE_PIN, ENABLE_LED_FEEDBACK);
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

    if (command.startsWith("SEND_CODE ")) {

        command = command.substring(10);
        
        // Dividir a mensagem usando o delimitador "_"
        int firstSeparatorIndex = command.indexOf('_');
        int secondSeparatorIndex = command.lastIndexOf('_');

        // Separar protocolo, endereço e comando
        String protocolo = command.substring(0, firstSeparatorIndex);
        String endereco = command.substring(firstSeparatorIndex + 1, secondSeparatorIndex);
        String comando = command.substring(secondSeparatorIndex + 1);

        // Exibir os resultados
        Serial.println("Protocolo: " + protocolo);
        Serial.println("Endereço: " + endereco);
        Serial.println("Comando: " + comando);

        long hexAddress = strtol(endereco.c_str(), NULL, 16);
        long hexCommand = strtol(comando.c_str(), NULL, 16);
        Serial.print("Enviando código IR: ");
        Serial.println(hexAddress);
        Serial.println(hexCommand);

        if (protocolo == "NEC") {
          sendExtendedNEC(IR_SEND_PIN, hexAddress, hexCommand, 2);
        } else {
          Serial.println("Protocolo desconhecido");
        }
    }

    else if (command == "GET_CODE") {
        IrReceiver.start();
        while(!IrReceiver.decode()) {

        }
        if (IrReceiver.decode()) {
            if (IrReceiver.decodedIRData.protocol == UNKNOWN) {
                Serial.println(F("Received noise or an unknown (or not yet enabled) protocol"));
                // We have an unknown protocol here, print extended info
                IrReceiver.printIRResultRawFormatted(&Serial, true);
                IrReceiver.resume(); // Do it here, to preserve raw data for printing with printIRResultRawFormatted()
            } else {
                IrReceiver.resume(); // Early enable receiving of the next IR frame
                IrReceiver.printIRResultShort(&Serial);
                IrReceiver.printIRSendUsage(&Serial);
            }
        Serial.println();
        }
    }
    
    else
      Serial.println("ERR Unknown command.");
      
}