package com.wax.irapp.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "ir_command_table")
data class IRCommand(
    @PrimaryKey(autoGenerate = true) val id: Int?,
    @ColumnInfo(name = "title") val title: String?,
    @ColumnInfo(name = "code") val code: String?,
    @ColumnInfo(name = "date") val date: String?
) : Serializable
