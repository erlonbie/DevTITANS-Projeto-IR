package com.devtitans.KrakenIRapp.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.devtitans.KrakenIRapp.models.IRCommand;

@Database(entities = {IRCommand.class}, version = 1, exportSchema = false)
public abstract class IRCommandDatabase extends RoomDatabase {

    public abstract IRCommandDAO getNoteDao();

    private static volatile IRCommandDatabase INSTANCE;

    public static IRCommandDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (IRCommandDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    IRCommandDatabase.class, "ir_command_database")
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
