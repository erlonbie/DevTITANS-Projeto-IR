package com.devtitans.KrakenIRapp.models;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "ir_command_table")
public class IRCommand implements Serializable {

    @PrimaryKey(autoGenerate = true)
    private Integer id;

    @ColumnInfo(name = "title")
    private String title;

    @ColumnInfo(name = "code")
    private String code;

    @ColumnInfo(name = "date")
    private String date;

    // Construtor
    public IRCommand(Integer id, String title, String code, String date) {
        this.id = id;
        this.title = title;
        this.code = code;
        this.date = date;
    }

    // Getters e Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
