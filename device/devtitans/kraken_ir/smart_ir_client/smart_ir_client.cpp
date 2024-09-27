#include "smart_ir_client.h"

using namespace std;                  // Permite usar o cout e endl diretamente ao invés de std::cout

namespace devtitans::smartir {      // Entra no pacote devtitans::hello

void SmartIrClient::start(int argc, char **argv) {
    cout << "Cliente SmartIr!" << endl;

    if (argc < 2) {
        cout << "Sintaxe: " << argv[0] << "  " << endl;
        cout << "    Comandos: transmit, receive, set_transmit, set_receive" << endl;
        exit(1);
    }

    SmartIr smartir;             // Classe da biblioteca SmartIr

    // Comandos receive e transmit
    if (!strcmp(argv[1], "receive")) {
        cout << "Valor recebido: " << smartir.receive() << endl;
    }
    else if (!strcmp(argv[1], "set_receive")) {
        int receiveValue = atoi(argv[2]);
        if (smartir.set_receive(receiveValue))
            cout << "Valor recebido alterado para " << receiveValue << endl;
        else
            cout << "Erro ao setar valor recebido para " << receiveValue << endl;
    }

    else if (!strcmp(argv[1], "transmit")) {
        cout << "Valor recebido: " << smartir.transmit() << endl;
    }
    else if (!strcmp(argv[1], "set_transmit")) {
        int transmitValue = atoi(argv[2]);
        if (smartir.set_transmit(transmitValue))
            cout << "Valor enviado alterado para " << transmitValue << endl;
        else
            cout << "Erro ao setar valor enviado para " << transmitValue << endl;
    }

    else {
        cout << "Comando inválido." << endl;
        exit(1);
    }
}

} // namespace

// MAIN

using namespace devtitans::smartir; // Permite usar HelloCpp diretamente ao invés de devtitans::hello::HelloCpp

int main(int argc, char **argv) {
    SmartIrClient client;               // Variável hello, da classe HelloCpp, do pacote devtitans::hello
    client.start(argc, argv);             // Executa o método printHello
    return 0;
}
