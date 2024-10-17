package com.devtitans.KrakenIRapp;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.devtitans.KrakenIRapp.models.IRCommand;

import java.text.SimpleDateFormat;
import java.util.Date;

public class AddIRCommandActivity extends AppCompatActivity {

    private static final String TAG = "KrakenIR.AddIRCommandActivity";

    private IRCommand iRCommand;
    private IRCommand oldIRCommand;
    private boolean isUpdate = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate called, initializing UI.");
        setContentView(R.layout.activity_add_ir_command);

        ProgressBar loadingSpinner = findViewById(R.id.loading_spinner);
        loadingSpinner.setVisibility(View.VISIBLE);
        Log.d(TAG, "Loading spinner set to visible.");

        TextView tvCommandRegistered = findViewById(R.id.tv_command_registered);
        tvCommandRegistered.setVisibility(View.GONE);
        Log.d(TAG, "Text view for command registration hidden.");

        TextView etTitle = findViewById(R.id.et_tilte);
        try {
            oldIRCommand = (IRCommand) getIntent().getSerializableExtra("current_command");
            if (oldIRCommand != null) {
                etTitle.setText(oldIRCommand.getTitle());
                Log.d(TAG, "Updating existing command: " + oldIRCommand.getTitle());
                loadingSpinner.setVisibility(View.GONE);
                tvCommandRegistered.setVisibility(View.VISIBLE);
                isUpdate = true;
            }
        } catch (Exception e) {
            Log.e(TAG, "No existing command found. Exception: " + e.getMessage());
            e.printStackTrace();
        }

        ImageView imgCheck = findViewById(R.id.img_check);
        imgCheck.setOnClickListener(v -> {
            String title = etTitle.getText().toString();
            Log.d(TAG, "Check button clicked, title: " + title);

            if (!title.isEmpty()) {
                @SuppressLint("SimpleDateFormat") SimpleDateFormat formatter = new SimpleDateFormat("EEE, d MMM yyyy HH:mm a");
                String formattedDate = formatter.format(new Date());

                if (isUpdate) {
                    iRCommand = new IRCommand(oldIRCommand.getId(), title, 1, formattedDate);
                    Log.d(TAG, "Command updated: " + title + ", date: " + formattedDate);
                } else {
                    iRCommand = new IRCommand(null, title, 1, formattedDate);
                    Log.d(TAG, "New command created: " + title + ", date: " + formattedDate);
                }

                Intent intent = new Intent();
                intent.putExtra("command", iRCommand);
                setResult(Activity.RESULT_OK, intent);
                Log.d(TAG, "Command set in result intent, finishing activity.");
                finish();
            } else {
                Log.d(TAG, "Title is empty, showing Toast message.");
                Toast.makeText(this, "Please enter some data", Toast.LENGTH_SHORT).show();
            }
        });

        ImageView imgBackArrow = findViewById(R.id.img_back_arrow);
        imgBackArrow.setOnClickListener(v -> {
            Log.d(TAG, "Back arrow clicked, calling onBackPressed.");
            onBackPressed();
        });
    }
}
