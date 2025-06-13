package com.example.androidphotosapp;
import java.io.Serializable;
import java.util.ArrayList;

public class User implements Serializable{


    public String name;
    public int id;
    public ArrayList<Album> albums = new ArrayList<Album>();


    public User(String name, int id){
        this.name = name;
        this.id = id;
    }

    public String getName(){
        return name;
    }
    public int getId(){
        return id;
    }
    public void setName(String name){
        this.name = name;
    }
    public void setId(int id){
        this.id = id;
    }


    public ArrayList<Album> getAlbums(){
        return albums;
    }

}
