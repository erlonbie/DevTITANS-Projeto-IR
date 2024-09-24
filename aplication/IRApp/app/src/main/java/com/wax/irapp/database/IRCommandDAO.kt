package com.wax.irapp.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.wax.irapp.models.IRCommand

@Dao
interface IRCommandDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(irCommand: IRCommand)

    @Delete
    suspend fun delete(irCommand: IRCommand)

    @Query("Select * from ir_command_table order by id ASC")
    fun getAllNotes(): LiveData<List<IRCommand>>

    @Query("UPDATE ir_command_table SET title = :title, code = :code WHERE id = :id")
    suspend fun update(id: Int?, title: String?, code: String?)

}