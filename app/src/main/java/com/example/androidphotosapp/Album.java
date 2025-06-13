package com.example.androidphotosapp;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;

public class Album implements Serializable {

    public String name;
    public User owner;
    public File file;
    public Calendar created;
    ArrayList<Photo> photos = new ArrayList<Photo>();

    public Album(String name){
        this.name = name;
        this.created = Calendar.getInstance();
        created.set(Calendar.MILLISECOND, 0);
    }

    public String getName(){
        return name;
    }
    public void setName(String name){
        this.name = name;
    }


    public User getUser(){
        return owner;
    }
    /**
     *
     * @param u user
     * @return void
     */


    public void setUser(User u){
        this.owner = u;
    }


    public ArrayList<Photo> getPhotos(){
        return photos;
    }


}
