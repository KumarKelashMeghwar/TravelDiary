package com.traveldiary.models;

import javafx.scene.image.Image;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.time.LocalDate;

public class JournalEntry {
    private int SNo;
    private String title;
    private LocalDate date;
    private String location;
    private String description;
    private Image photos;

    private String expenses;
    private String ratings;


    public JournalEntry(int SNo, String title, LocalDate date, String photoPath, String location, String description, String expenses, String ratings) {
        this.title = title;
        this.date = date;
        this.location = location;
        this.description = description;
        try {
            this.photos = new Image(new FileInputStream(photoPath)); // Load image from file path
        } catch (FileNotFoundException e) {
            throw new Error(e);
        }
        this.SNo = SNo;
        this.ratings =ratings;
        this.expenses = expenses;
    }


    public String getTitle() {
        return title;
    }

    public LocalDate getDate() {
        return date;
    }

    public String getLocation() {
        return location;
    }

    public String getDescription() {
        return description;
    }

    public int getSNo(){return SNo;}

    public Image getPhotos(){return photos;}

    public String getExpenses(){return expenses;}
    public String getRatings(){return ratings;}
}
