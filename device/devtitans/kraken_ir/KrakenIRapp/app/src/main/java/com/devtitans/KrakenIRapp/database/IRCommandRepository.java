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
