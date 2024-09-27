package com.wax.irapp.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.wax.irapp.models.IRCommand

@Database(entities = arrayOf(IRCommand::class), version = 1, exportSchema = false)
abstract class IRCommandDatabase : RoomDatabase() {

    abstract fun getNoteDao() : IRCommandDAO

    companion object{

        @Volatile
        private var INSTANCE : IRCommandDatabase? = null

        fun getDatabase(context : Context) : IRCommandDatabase{
            return INSTANCE ?: synchronized(this){

                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    IRCommandDatabase::class.java,
                    "ir_command_database"
                ).build()

                INSTANCE = instance

                instance
            }
        }
    }

}