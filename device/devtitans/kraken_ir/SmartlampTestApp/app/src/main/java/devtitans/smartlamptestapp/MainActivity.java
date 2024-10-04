package devtitans.smartlamptestapp;

import androidx.appcompat.app.AppCompatActivity;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.*;

import android.os.ServiceManager;
import android.os.IBinder;
import android.os.RemoteException;

import devtitans.smartir.ISmartIR;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "DevTITANS.SmartlampApp";

    private TextView textStatus, textLuminosity;
    private EditText editLed;
    private IBinder binder;
    private ISmartIR service;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textStatus =     findViewById(R.id.textStatus);                      // Acessa os componentes da tela
        textLuminosity = findViewById(R.id.textLuminosity);
        editLed =        findViewById(R.id.editLed);

        binder = ServiceManager.getService("devtitans.smartir.IsmartIR/default"); // Acessa e consulta o binder
        if (binder != null) {
            service = ISmartIR.Stub.asInterface(binder);                   // Acessa o serviço smartir
            if (service != null)
                Toast.makeText(this, "Serviço smartir acessado com sucesso.", Toast.LENGTH_LONG).show();
            else
                Toast.makeText(this, "Erro ao acessar o serviço smartir!", Toast.LENGTH_LONG).show();
        }
        else
            Toast.makeText(this, "Erro ao acessar o Binder!", Toast.LENGTH_LONG).show();

        updateAll(null);
    }

    public void updateAll(View view) {
        Log.d(TAG, "Atualizando dados do dispositivo ...");

        if (binder == null) {
            textStatus.setText("Erro no Binder");
            textStatus.setTextColor(Color.parseColor("#73312f"));
        }
        else if (service == null) {
            textStatus.setText("Erro no Serviço");
            textStatus.setTextColor(Color.parseColor("#73312f"));
        }
        else {
            textStatus.setText("Atualizando ...");
            textStatus.setTextColor(Color.parseColor("#c47e00"));

            try {
                int luminosity = service.transmit();                    // Executa o método getLuminosity via IPC
                textLuminosity.setText(String.valueOf(luminosity));

                int led = service.receive();                                  // Executa o método getLed via IPC
                editLed.setText(String.valueOf(led));

                int status = service.connect();                              // Executa o método connect via IPC
                if (status == 0) {
                    textStatus.setText("Desconectado");
                    textStatus.setTextColor(Color.parseColor("#73312f"));
                }
                else if (status == 1) {
                    textStatus.setText("Conectado");
                    textStatus.setTextColor(Color.parseColor("#6d790c"));
                }
                else {
                    textStatus.setText("Simulado");
                    textStatus.setTextColor(Color.parseColor("#207fb5"));
                }

            } catch (android.os.RemoteException e) {
                Toast.makeText(this, "Erro ao acessar o Binder!", Toast.LENGTH_LONG).show();
                Log.e(TAG, "Erro atualizando dados:", e);
            }

        }

    }

    // Executado ao clicar no botão "SET" do Led.
    public void updateLed(View view) {
        try {
            int newLed = Integer.parseInt(editLed.getText().toString());     // Executa o método getLed via IPC
            service.set_receive(newLed);
        } catch (android.os.RemoteException e) {
            Toast.makeText(this, "Erro ao setar led!", Toast.LENGTH_LONG).show();
        }
    }
}
