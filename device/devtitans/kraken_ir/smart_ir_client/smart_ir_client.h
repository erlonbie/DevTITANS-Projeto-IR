#pragma once                      // Inclui esse cabeçalho apenas uma vez

#include <iostream>               // std::cout (char-out) e std::endl (end-line)
#include <string.h>               // Função strcmp
#include <stdlib.h>               // Função atoi

#include "smart_ir_lib.h"        // Classe SmartIr

namespace devtitans::smartir {  // Pacote que a classe abaixo pertence

class SmartIrClient {           // Classe

    public:
        void start(int argc, char **argv);

};

} // namespace
