package com.devtitans.KrakenIRapp;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.devtitans.KrakenIRapp.database.IRCommandDatabase;
import com.devtitans.KrakenIRapp.database.IRCommandRepository;
import com.devtitans.KrakenIRapp.models.IRCommand;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class IRCommandViewModel extends AndroidViewModel {

    private IRCommandRepository repository;
    private LiveData<List<IRCommand>> allCommands;

    private MutableLiveData<List<IRCommand>> commandsTest;

    public IRCommandViewModel(Application application) {
        super(application);
        IRCommandDatabase database = IRCommandDatabase.getDatabase(application);
        repository = new IRCommandRepository(database.getNoteDao(), application);
        allCommands = repository.getAllCommands();

        // Initialize test commands
        commandsTest = new MutableLiveData<>();
        List<IRCommand> initialCommands = new ArrayList<>();
        initialCommands.add(new IRCommand(1, "Command 1", 1, ""));
        initialCommands.add(new IRCommand(2, "Command 2", 2, ""));
        initialCommands.add(new IRCommand(3, "Command 3", 3, ""));
        initialCommands.add(new IRCommand(4, "Command 4", 4, ""));
        initialCommands.add(new IRCommand(5, "Command 5", 5, ""));
        initialCommands.add(new IRCommand(6, "Command 6", 6, ""));
        initialCommands.add(new IRCommand(7, "Command 7", 7, ""));
        initialCommands.add(new IRCommand(8, "Command 8", 8, ""));
        initialCommands.add(new IRCommand(9, "Command 9", 9, ""));
        initialCommands.add(new IRCommand(10, "Command 10", 10, ""));
        initialCommands.add(new IRCommand(11, "Command 11", 11, ""));
        initialCommands.add(new IRCommand(12, "Command 12", 12, ""));
        initialCommands.add(new IRCommand(13, "Command 13", 13, ""));
        initialCommands.add(new IRCommand(14, "Command 14", 14, ""));
        initialCommands.add(new IRCommand(15, "Command 15", 15, ""));
        initialCommands.add(new IRCommand(16, "Command 16", 16, ""));
        initialCommands.add(new IRCommand(17, "Command 17", 17, ""));
        initialCommands.add(new IRCommand(18, "Command 18", 18, ""));
        initialCommands.add(new IRCommand(19, "Command 19", 19, ""));
        initialCommands.add(new IRCommand(20, "Command 20", 20, ""));
        initialCommands.add(new IRCommand(21, "Command 21", 21, ""));
        initialCommands.add(new IRCommand(22, "Command 22", 22, ""));
        initialCommands.add(new IRCommand(23, "Command 23", 23, ""));
        initialCommands.add(new IRCommand(23, "Command 24", 24, ""));
        commandsTest.setValue(initialCommands);
    }

    public LiveData<List<IRCommand>> getAllCommands() {
        return allCommands;
    }

    public void deleteCommand(IRCommand irCommand) {
        new Thread(() -> repository.delete(irCommand)).start();
    }

    public void insertCommand(IRCommand irCommand) {
        new Thread(() -> repository.insert(irCommand)).start();
    }

    public void updateCommand(IRCommand irCommand) {
        new Thread(() -> repository.update(irCommand)).start();
    }

    public LiveData<List<IRCommand>> getCommandsTest() {
        return commandsTest;
    }
}