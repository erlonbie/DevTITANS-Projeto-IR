/*
 * Este código em Arduino (embarcado no ESP32) permite o controle de um dispositivo por meio de um transmissor infravermelho.
 * Ele utiliza a biblioteca IRSND para enviar sinais IR, que podem ser configurados via comandos
 * recebidos pelo monitor serial. O comando "SET_IR_TRANSMIT" seguido de um número (1 a 24)
 * define o sinal infravermelho a ser transmitido, com valores de comando e endereço pré-definidos.
 *
 * Mapa de comandos (número e valor em hexadecimal transmitido):
 * 
 * 1  | 0x0    | 2  | 0x1    | 3  | 0x2    | 4  | 0x3
 * 5  | 0x4    | 6  | 0x5    | 7  | 0x6    | 8  | 0x7
 * 9  | 0x8    | 10 | 0x9    | 11 | 0xA    | 12 | 0xB
 * 13 | 0xC    | 14 | 0xD    | 15 | 0xE    | 16 | 0xF
 * 17 | 0x10   | 18 | 0x11   | 19 | 0x12   | 20 | 0x13
 * 21 | 0x14   | 22 | 0x15   | 23 | 0x16   | 24 | 0x17
 *
 * Cada número representa um comando IR específico, e o valor hexadecimal correspondente é
 * enviado como parte da mensagem IR. A comunicação é feita via serial a uma taxa de 9600 baud.
 */

#include <Arduino.h>

// Definição da frequência de transmissão infravermelha (38 kHz, comum para controles remotos)
#define IRSND_IR_FREQUENCY 38000

// Definição do nome dos protocolos de transmissão (pode ser usado para debug)
#define IRSND_PROTOCOL_NAMES 1

// Pinos de entrada e saída para o infravermelho
#define IRSND_OUTPUT_PIN 23  // Pino de saída para o LED infravermelho
#define IRMP_INPUT_PIN 22    // Pino de entrada (não utilizado neste código, mas pode ser usado para recepção)
#define LED_BUILTIN 2        // LED embutido para feedback visual

// Inclusão das bibliotecas do IRMP e IRSND
#include <irsndSelectMain15Protocols.h>
#include <irsnd.hpp>

// Estrutura para armazenar os dados do sinal infravermelho a ser transmitido
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

        irsnd_data.protocol = IRMP_NEC_PROTOCOL;   // Define o protocolo de transmissão
        irsnd_data.address = 0xEF00; // Define o endereço de destino
        
        // Casos para valores de 1 até 24
        switch (valor) {
            case 1:
                irsnd_data.command = 0x0;
                break;
            case 2:
                irsnd_data.command = 0x1;
                break;
            case 3:
                irsnd_data.command = 0x2;
                break;
            case 4:
                irsnd_data.command = 0x3;
                break;
            case 5:
                irsnd_data.command = 0x4;
                break;
            case 6:
                irsnd_data.command = 0x5;
                break;
            case 7:
                irsnd_data.command = 0x6;
                break;
            case 8:
                irsnd_data.command = 0x7;
                break;
            case 9:
                irsnd_data.command = 0x8;
                break;
            case 10:
                irsnd_data.command = 0x9;
                break;
            case 11:
                irsnd_data.command = 0xA;
                break;
            case 12:
                irsnd_data.command = 0xB;
                break;
            case 13:
                irsnd_data.command = 0xC;
                break;
            case 14:
                irsnd_data.command = 0xD;
                break;
            case 15:
                irsnd_data.command = 0xE;
                break;
            case 16:
                irsnd_data.command = 0xF;
                break;
            case 17:
                irsnd_data.command = 0x10;
                break;
            case 18:
                irsnd_data.command = 0x11;
                break;
            case 19:
                irsnd_data.command = 0x12;
                break;
            case 20:
                irsnd_data.command = 0x13;
                break;
            case 21:
                irsnd_data.command = 0x14;
                break;
            case 22:
                irsnd_data.command = 0x15;
                break;
            case 23:
                irsnd_data.command = 0x16;
                break;
            case 24:
                irsnd_data.command = 0x17;
                break;
            default:
                Serial.println("RES SET_IR_TRANSMIT -1");
                return;
        }
        
        irsnd_data.flags = 2;  // Configuração de flags
        irsnd_send_data(&irsnd_data, true);
        Serial.println("RES SET_IR_TRANSMIT 1");

    } else {
        Serial.println("ERR Unknown command.");
    }
}
