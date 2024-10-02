package devtitans.smartir;

@VintfStability
interface ISmartIR {

  int connect();

  int transmit();

  int receive();

  boolean set_transmit(int irTransmiteValue);

  boolean set_receive(int irReceiveValue);
}
