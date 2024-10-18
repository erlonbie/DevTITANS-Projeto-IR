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

    Serial.begin(9600);
    delay(500);

    irsnd_init();
    irmp_irsnd_LEDFeedback(true);

}

void loop() {

  if (Serial.available() > 0) {
    String str = Serial.readString();
    str.trim();
    processCommand(str);
  }

  delay(100);
}

void processCommand(String command) {
    command.trim();
    command.toUpperCase();

    if (command.startsWith("SET_IR_TRANSMIT")) {

        int valor = command.substring(16).toInt();
        // Serial.println(valor);

        if (valor == 1) {
            // Comando LIGAR TV
            irsnd_data.protocol = 0xA;   // Define o protocolo de transmissão
            irsnd_data.address = 0x707;  // Define o endereço de destino
            irsnd_data.command = 0x19E6; // Define o comando a ser transmitido
            irsnd_data.flags = 2;        // Configuração de flags
            irsnd_send_data(&irsnd_data, true);
            Serial.println("RES SET_IR_TRANSMIT");
        } 
        else if (valor == 2) {
            // Comando CH+ (Canal para cima)
            irsnd_data.protocol = 0x2;   // Define o protocolo de transmissão
            irsnd_data.address = 0x5E87; // Define o endereço de destino
            irsnd_data.command = 0x10;   // Define o comando a ser transmitido
            irsnd_data.flags = 2;        // Configuração de flags
            irsnd_send_data(&irsnd_data, true);
            Serial.println("RES SET_IR_TRANSMIT 1");
        } 
        else if (valor == 3) {
            // Comando CH- (Canal para baixo)
            irsnd_data.protocol = 0x2;   // Define o protocolo de transmissão
            irsnd_data.address = 0x5E87; // Define o endereço de destino
            irsnd_data.command = 0x1A;   // Define o comando a ser transmitido
            irsnd_data.flags = 2;        // Configuração de flags
            irsnd_send_data(&irsnd_data, true);
            Serial.println("RES SET_IR_TRANSMIT 1");
        } 
         else if (valor == 4) {
            // LED
            irsnd_data.protocol = IRMP_NEC_PROTOCOL;   // Define o protocolo de transmissão
            irsnd_data.address = 0xEF00; // Define o endereço de destino
            irsnd_data.command = 0x0D;   // Define o comando a ser transmitido
            irsnd_data.flags = 2;        // Configuração de flags
            irsnd_send_data(&irsnd_data, true);
            Serial.println("RES SET_IR_TRANSMIT 1");
        } 
        else {
            // Comando desconhecido
            Serial.println("RES SET_IR_TRANSMIT -1");
        }
    }

    else
      Serial.println("ERR Unknown command.");
      
}
