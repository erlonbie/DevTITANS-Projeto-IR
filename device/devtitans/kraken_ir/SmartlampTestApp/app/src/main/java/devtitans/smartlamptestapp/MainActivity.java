package devtitans.smartlamptestapp;

import androidx.appcompat.app.AppCompatActivity;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.*;
import android.os.RemoteException;

import devtitans.smartirmanager.SmartIRManager; // Biblioteca do Manager

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "DevTITANS.SmartlampApp";

    private TextView textStatus, textLuminosity;
    private EditText editLed;
    private SmartIRManager manager; // Atributo para o manager

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main); 

        textStatus =     findViewById(R.id.textStatus);                      // Acessa os componentes da tela
        textLuminosity = findViewById(R.id.textLuminosity);
        editLed =        findViewById(R.id.editLed);

        manager = SmartIRManager.getInstance();

        updateAll(null);
    }

    public void updateAll(View view) {
        Log.d(TAG, "Atualizando dados do dispositivo ...");

        textStatus.setText("Atualizando ...");
        textStatus.setTextColor(Color.parseColor("#c47e00"));

        try {
            int luminosity = manager.transmit();                    // Executa o método getLuminosity via IPC
            textLuminosity.setText(String.valueOf(luminosity));

            int led = manager.receive();                                  // Executa o método getLed via IPC
            editLed.setText(String.valueOf(led));

            int status = manager.connect();                              // Executa o método connect via IPC
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

    // Executado ao clicar no botão "SET" do Led.
    public void updateLed(View view) {
        try {
            int newLed = Integer.parseInt(editLed.getText().toString());     // Executa o método getLed via IPC
            manager.set_receive(newLed);
        } catch (android.os.RemoteException e) {
            Toast.makeText(this, "Erro ao setar led!", Toast.LENGTH_LONG).show();
        }
    }
}
