#include <Arduino.h>

// #include "PinDefinitionsAndMore.h" // Set IR_SEND_PIN for different CPU's

#include "TinyIRSender.hpp"
#define IR_SEND_PIN 23
#define LED_BUILTIN 2

void setup() {
    pinMode(LED_BUILTIN, OUTPUT);

    Serial.begin(115200);
    while (!Serial)
        ; // Wait for Serial to become available. Is optimized away for some cores.

    // Just to know which program is running on my Arduino
    Serial.println(F("START " __FILE__ " from " __DATE__ "\r\nUsing library version " VERSION_TINYIR));
    Serial.print(F("Send IR signals at pin "));
    Serial.println(IR_SEND_PIN);
}

void loop() {
  
  // Verifica se há dados disponíveis no monitor serial
  if (Serial.available()) {
    String hexCode = Serial.readStringUntil('\n');
    hexCode.trim();  // Remove espaços ou quebras de linha extras
    long hexValue = strtol(hexCode.c_str(), NULL, 16);

    Serial.print("Enviando código IR: ");
    Serial.println(hexValue);

    sendExtendedNEC(IR_SEND_PIN, 0x0, hexValue, 0);
  }
}
