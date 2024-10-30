#pragma once                           // Inclui esse cabeçalho apenas uma vez

#include <fstream>                     // Classe ifstream
#include <android-base/properties.h>   // Função GetBoolProperty
#include <sys/stat.h>                  // Função e struct stat
#include <random>                      // Geração de números aleatórios (valores simulados)

using namespace std;                   // Permite usar string diretamente ao invés de std::string

namespace devtitans::smartir {       // Pacote SmartIr

class SmartIr {
    public:
      /**
       * Verifica se o diretório /sys/kernel/smart_ir existe. Se existir
       * o dispositivo SmartIr está conectado via USB. Caso contrário,
       * verifica a propriedade devtitans.smart_ir.allow_simulated
       * para ver se valores simulados podem ser usados.
       *
       * Retorna:
       *      0: dispositivo não encontrado
       *      1: sucesso
       *      2: simulado (disp. não encontrado, mas usando valores simulados)
       */
      int connect();

      /**
       * Transmite um sinal.
       * 
       * Esta função transmite um sinal e retorna um valor booleano indicando o sucesso da transmissão.
       * 
       * Parâmetros:
       *      Nenhum.
       * 
       * Retorna:
       *      int: O valor transmitido
       */
       int transmit();

       /**
       * Recebe um sinal.
       * 
       * Esta função recebe um sinal e retorna o valor recebido.
       * 
       * Parâmetros:
       *      Nenhum.
       * 
       * Retorna:
       *      int: O valor do sinal recebido.
       */
      int receive();

      /**
       * Transmite um sinal.
       * 
       * Esta função seta o valor a ser transmitido.
       * 
       * Parâmetros:
       *      int: O valor a ser transmitido
       * 
       * Retorna:
       *      bool: Se o valor vou gravado com sucesso
       */
       bool set_transmit(int value);

       /**
       * Recebe um sinal.
       * 
       * Esta função seta o valor recebido.
       * 
       * Parâmetros:
       *      int: O valor recebido
       * 
       * Retorna:
       *      bool: Se o valor vou gravado com sucesso
       */
      bool set_receive(int value);

    private:
      /**
       * Métodos para ler e escrever valores nos arquivos "led",
       * "ldr" ou "threshold" do diretório /sys/kernel/smart_ir.
       */
      int readFileValue(string file);
      bool writeFileValue(string file, int value);

      /**
       * Armazena valores simulados para o caso do dispositivo não estar
       * conectado, mas a propriedade devtitans.smart_ir.allow_simulated
       * for true.
       */
       int irTransmiteValue = 40;
       int irReceiveValue = 40;
};
} // namespace
