package devtitans.smartirmanager;

import android.os.IBinder;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.util.Log;

import devtitans.smartir.ISmartIR; // Criado pelo AIDL

public class SmartIRManager {

  private static final String TAG = "DevTITANS.SmartirApp";
  private IBinder binder;
  private ISmartIR service;

  private static SmartIRManager instance;

  private SmartIRManager() {
    Log.d(TAG, "Nova (única) instância do SmartIRManager ...");

    binder = ServiceManager.getService("devtitans.smartir.ISmartIR/default");
    if (binder != null) {
      service = ISmartIR.Stub.asInterface(binder);
      if (service != null)
        Log.d(TAG, "Serviço SmartIR acessado com sucesso.");
      else
        Log.e(TAG, "Erro ao acessar o serviço SmartIR!");
    } else
      Log.e(TAG, "Erro ao acessar o Binder!");
    }
    // Acessa a (única) instância dessa classe. Se ela não existir ainda, cria.
    // Note o "static" no método. Podemos executá-lo sem precisar instanciar um objeto.
    public static SmartIRManager getInstance() {
        if (instance == null)
            instance = new SmartIRManager();

        return instance;
    }
    public int connect() throws RemoteException {
        Log.d(TAG, "Executando método connect() ...");
        return service.connect();
    }

    public int transmit() throws RemoteException {
        Log.d(TAG, "Executando método transmit() ...");
        return service.transmit();
    }

    public int receive() throws RemoteException {
        Log.d(TAG, "Executando método receive() ...");
        return service.receive();
    }

    public boolean set_transmit(int irTransitValue) throws RemoteException {
        Log.d(TAG, "Executando método set_transmit(" + irTransitValue + ") ...");
        return service.set_transmit(irTransitValue);
    }

    public boolean set_receive(int irReceiveValue) throws RemoteException {
        Log.d(TAG, "Executando método set_receive(" + irReceiveValue + ") ...");
        return service.set_receive(irReceiveValue);
    }
}
