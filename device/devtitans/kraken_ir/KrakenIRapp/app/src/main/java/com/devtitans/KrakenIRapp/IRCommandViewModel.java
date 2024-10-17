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
        repository = new IRCommandRepository(database.getNoteDao());
        allCommands = repository.getAllCommands();

        // Initialize test commands
        commandsTest = new MutableLiveData<>();
        List<IRCommand> initialCommands = new ArrayList<>();
        initialCommands.add(new IRCommand(1, "Command 1", 1, ""));
        initialCommands.add(new IRCommand(2, "Command 2", 1, ""));
        initialCommands.add(new IRCommand(3, "Command 3", 1, ""));
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