package com.devtitans.KrakenIRapp.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.devtitans.KrakenIRapp.models.IRCommand;

import java.util.List;

@Dao
public interface IRCommandDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(IRCommand irCommand);

    @Delete
    void delete(IRCommand irCommand);

    @Query("SELECT * FROM ir_command_table ORDER BY id ASC")
    LiveData<List<IRCommand>> getAllNotes();

    @Query("UPDATE ir_command_table SET title = :title, code = :code WHERE id = :id")
    void update(Integer id, String title, String code);
}
