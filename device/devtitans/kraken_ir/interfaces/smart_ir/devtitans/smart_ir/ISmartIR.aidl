package devtitans.smart_ir;

interface ISmartIR {
  int transmit();

  int receive();

  boolean set_transmit(int irTransmiteValue);

  boolean set_receive(int irReceiveValue);
}
