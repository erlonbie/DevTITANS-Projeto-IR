package com.devtitans.KrakenIRapp.database;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.lifecycle.LiveData;

import com.devtitans.KrakenIRapp.models.IRCommand;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class IRCommandRepository {

    private static final String PREFS_NAME = "ir_command_prefs";
    private static final String PREFS_KEY_INITIAL_SETUP_DONE = "initial_setup_done";

    private final IRCommandDAO irCommandDAO;
    private final LiveData<List<IRCommand>> allCommands;

    private final ExecutorService executorService;
    private final SharedPreferences sharedPreferences;

    public IRCommandRepository(IRCommandDAO irCommandDAO, Context context) {
        this.irCommandDAO = irCommandDAO;
        this.allCommands = irCommandDAO.getAllNotes();
        this.executorService = Executors.newSingleThreadExecutor();
        this.sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);

        insertPredefinedCommands();
    }

    private void insertPredefinedCommands() {
        boolean initialSetupDone = sharedPreferences.getBoolean(PREFS_KEY_INITIAL_SETUP_DONE, false);

        if (!initialSetupDone) {
            IRCommand command1 = new IRCommand(null, "Ligar TV", 1, "2024-10-17");
            IRCommand command2 = new IRCommand(null, "Desligar TV", 2, "2024-10-17");
            IRCommand command3 = new IRCommand(null, "Aumentar Volume", 3, "2024-10-17");
            IRCommand command4 = new IRCommand(null, "LED Verde", 4, "2024-10-17");

            executorService.execute(() -> {
                irCommandDAO.insert(command1);
                irCommandDAO.insert(command2);
                irCommandDAO.insert(command3);
                irCommandDAO.insert(command4);
            });

            sharedPreferences.edit().putBoolean(PREFS_KEY_INITIAL_SETUP_DONE, true).apply();
        }

    }

    public LiveData<List<IRCommand>> getAllCommands() {
        return allCommands;
    }

    public void insert(IRCommand irCommand) {
        executorService.execute(() -> irCommandDAO.insert(irCommand));
    }

    public void delete(IRCommand irCommand) {
        executorService.execute(() -> irCommandDAO.delete(irCommand));
    }

    public void update(IRCommand irCommand) {
        executorService.execute(() -> irCommandDAO.update(irCommand.getId(), irCommand.getTitle(), irCommand.getCode()));
    }
}
