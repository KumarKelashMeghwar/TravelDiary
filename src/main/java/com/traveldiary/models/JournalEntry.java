package com.traveldiary.models;

import javafx.scene.image.Image;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.time.LocalDate;

public class JournalEntry {

    public  String username;
    private String Id;
    private  String title;
    private  LocalDate date;
    private  String location;
    private  String description;
    private  Image photos;
    private  String expenses;
    private  String ratings;

    public JournalEntry(){

    }



    public JournalEntry(String Id, String username, String title, LocalDate date, String photoPath, String location, String description, String expenses, String ratings) {
        this.title = title;
        this.date = date;
        this.location = location;
        this.description = description;
        this.photos = new Image(new File(photoPath).toURI().toString());
        this.Id = Id;
        this.ratings = ratings;
        this.expenses = expenses;
        this.username = username;
    }


    public void setUsername(String username) {
        this.username = username;
    }

    public void setId(String id) {
        Id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setPhotos(Image photos) {
        this.photos = photos;
    }

    public void setExpenses(String expenses) {
        this.expenses = expenses;
    }

    public void setRatings(String ratings) {
        this.ratings = ratings;
    }

    public String getUsername() {
        return username;
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

    public String getId() {
        return Id;
    }

    public Image getPhotos() {
        return photos;
    }

    public String getExpenses() {
        return expenses;
    }

    public String getRatings() {
        return ratings;
    }

    @Override
    public String toString() {
        return "JournalEntry{" +
                "username='" + username + '\'' +
                ", Id='" + Id + '\'' +
                ", title='" + title + '\'' +
                ", date=" + date +
                ", location='" + location + '\'' +
                ", description='" + description + '\'' +
                ", photos=" + photos +
                ", expenses='" + expenses + '\'' +
                ", ratings='" + ratings + '\'' +
                '}';
    }
}
