package com.wax.irapp.database

import androidx.lifecycle.LiveData
import com.wax.irapp.models.IRCommand

class IRCommandRepository(private val irCommandDAO: IRCommandDAO) {

    val allCommands: LiveData<List<IRCommand>> = irCommandDAO.getAllNotes()

    suspend fun insert(irCommand: IRCommand) {
        irCommandDAO.insert(irCommand)
    }

    suspend fun delete(irCommand: IRCommand) {
        irCommandDAO.delete(irCommand)
    }

    suspend fun update(irCommand: IRCommand) {
        irCommandDAO.update(irCommand.id, irCommand.title, irCommand.code)
    }
}