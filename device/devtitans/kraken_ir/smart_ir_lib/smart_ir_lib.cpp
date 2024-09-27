#include "smart_ir_lib.h"

using namespace std; // Permite usar string diretamente ao invés de std::string
using namespace android::base; // Permite usar GetBoolProperty ao invés de
                               // android::base::GetBoolProperty

namespace devtitans::smartir { // Pacote semartir

int SmartIr::connect() {
  char dirPath[] = "/sys/kernel/smart_ir";
  struct stat dirStat;
  if (stat(dirPath, &dirStat) == 0)
    if (S_ISDIR(dirStat.st_mode))
      return 1; // Se o diretório existir, retorna 1

  // Diretório não existe, vamos verificar a propriedade
  bool allowSimulated = GetBoolProperty("devtitans.smart_ir.allow_simulated", true);
  if (!allowSimulated)
    return 0; // Dispositivo não encontrado
  else
    return 2; // Usando valores simulados
}

int SmartIr::readFileValue(string file) {
  int connected = this->connect();

  if (connected == 2) { // Usando valores simulados
    if (file == "ir_transmit")
      return this->irTransmiteValue;
    else if (file == "ir_receive")
      return this->irReceiveValue;
    else {
      // "ldr" (luminosity): Gera um número aleatório entre 0 e 100
      random_device dev;
      mt19937 rng(dev());
      uniform_int_distribution<mt19937::result_type> dist100(0, 100);
      return (int)dist100(rng);
    }
  }

  else if (connected == 1) { // Conectado. Vamos solicitar o valor ao dispositivo
    int value;
    string filename = string("/sys/kernel/smart_ir/") + file;
    ifstream file(filename); // Abre o arquivo do módulo do kernel

    if (file.is_open()) { // Verifica se o arquivo foi aberto com sucesso
      file >> value;      // Lê um hexadecimal do arquivo
      file.close();
      return value;
    }
  }

  // Se chegou aqui, não foi possível conectar ou se comunicar com o dispositivo
  return -1;
}

bool SmartIr::writeFileValue(string file, int value) {
  int connected = this->connect();

  if (connected == 2) { // Usando valores simulados
    if (file == "ir_transmit")
      return this->irTransmiteValue;
    else if (file == "ir_receive")
      return this->irReceiveValue;
  }

  else if (connected == 1) { // Conectado. Vamos solicitar o valor ao dispositivo
    string filename = string("/sys/kernel/smart_ir/") + file;
    ofstream file(filename,
                  ios::trunc); // Abre o arquivo limpando o seu conteúdo

    if (file.is_open()) { // Verifica se o arquivo foi aberto com sucesso
      file << value;      // Escreve o irReceiveValue no arquivo
      file.close();
      return true;
    }
  }

  // Se chegou aqui, não foi possível conectar ou se comunicar com o dispositivo
  return false;
}

int SmartIr::transmit() { return this->readFileValue("ir_transmit"); }

int SmartIr::receive() { return this->readFileValue("ir_receive"); }

bool SmartIr::set_transmit(int value) {
  return this->writeFileValue("ir_transmit", value);
}

bool SmartIr::set_receive(int value) {
  return this->writeFileValue("ir_receive", value);
}

} // namespace devtitans::smartir
