package com.wax.irapp

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.wax.irapp.database.IRCommandDatabase
import com.wax.irapp.database.IRCommandRepository
import com.wax.irapp.models.IRCommand
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class IRCommandViewModel(application: Application) : AndroidViewModel(application) {

    private val repository : IRCommandRepository

    val allCommands : LiveData<List<IRCommand>>

    val commandsTest = MutableLiveData<List<IRCommand>>().apply {
        value = listOf(
            IRCommand(1, "Command 1", "Code 1", ""),
            IRCommand(2, "Command 2", "Code 2", ""),
            IRCommand(3, "Command 3", "Code 3", "")
        )
    }

    init {
        val dao = IRCommandDatabase.getDatabase(application).getNoteDao()
        repository = IRCommandRepository(dao)
        allCommands = repository.allCommands
    }
    fun deleteCommand(irCommand: IRCommand) = viewModelScope.launch(Dispatchers.IO) {
        repository.delete(irCommand)
    }

    fun insertCommand(irCommand: IRCommand) = viewModelScope.launch(Dispatchers.IO) {
        repository.insert(irCommand)
    }

    fun updateCommand(irCommand: IRCommand) = viewModelScope.launch(Dispatchers.IO) {
        repository.update(irCommand)
    }

}