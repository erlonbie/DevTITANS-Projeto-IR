package com.devtitans.KrakenIRapp.database;

import androidx.lifecycle.LiveData;

import com.devtitans.KrakenIRapp.models.IRCommand;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class IRCommandRepository {

    private final IRCommandDAO irCommandDAO;
    private final LiveData<List<IRCommand>> allCommands;

    private final ExecutorService executorService;

    public IRCommandRepository(IRCommandDAO irCommandDAO) {
        this.irCommandDAO = irCommandDAO;
        this.allCommands = irCommandDAO.getAllNotes();
        this.executorService = Executors.newSingleThreadExecutor();

        insertPredefinedCommands();
    }

    private void insertPredefinedCommands() {
        IRCommand command1 = new IRCommand(null, "Ligar TV", 1, "2024-10-17");
        IRCommand command2 = new IRCommand(null, "Desligar TV", 2, "2024-10-17");
        IRCommand command3 = new IRCommand(null, "Aumentar Volume", 3, "2024-10-17");

        executorService.execute(() -> {
            irCommandDAO.insert(command1);
            irCommandDAO.insert(command2);
            irCommandDAO.insert(command3);
        });
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
