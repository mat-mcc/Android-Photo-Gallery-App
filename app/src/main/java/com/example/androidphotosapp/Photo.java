package com.example.androidphotosapp;

import android.net.Uri;
import android.widget.ImageView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;



public class Photo implements Serializable {

    public int id;
    public String name;
    public Calendar date;
    // will be associated with file type eventually
    public transient Uri image;
    public User owner;

    public ArrayList<String> tagNames = new ArrayList<String>();
    public ArrayList<String> tagValues = new ArrayList<String>();
    public String caption;

    public Photo(int id, String name, Uri image){
        this.name = name;
        this.date = Calendar.getInstance();
        date.set(Calendar.MILLISECOND, 0);
        this.id = id;
        this.image = image;
        this.name = name;
    }
    public ArrayList<String> getTagList()
    {
        return tagNames;
    }
    public ArrayList<String> getValuesList()
    {
        return tagValues;
    }
    public int getId(){
        return id;
    }
    public String getName(){
        return name;
    }
    public Calendar getDate(){
        return date;
    }
    public Uri getImage(){
        return image;
    }
    public void setUser(User u){
        this.owner = u;
    }

    public void setTag(String tag, String value){
        this.tagNames.add(tag);
        this.tagValues.add(value);
    }
    public String getTagNameAt(int i){
        return this.tagNames.get(i);
    }
    public String getTagValueAt(int i){
        return this.tagValues.get(i);
    }

    public void removeTag(String tag, String value){
        int i = 0;
        for (String s : tagNames) {
            if (this.tagNames.get(i).equals(tag) && this.tagValues.get(i).equals(value))
            {
                tagNames.remove(i);
                tagValues.remove(i);
                return;
            }
            i = i + 1;
        }
        //System.out.println("Tag Not Found");
    }

    //temporary method
    public void printAllTags(){
        int i = 0;
        for (String s : tagNames) {
            //System.out.println("Tag: "+ this.tagNames.get(i) + ", Value: " + this.tagValues.get(i));
            i = i + 1;
        }
    }
    public void setCaption(String c){
        this.caption = c;
    }
    public String getCaption(){
        return caption;
    }
    public void setName(String n){
        this.name = n;
    }
}
