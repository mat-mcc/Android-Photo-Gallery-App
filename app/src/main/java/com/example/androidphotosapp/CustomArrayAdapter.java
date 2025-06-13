package com.example.androidphotosapp;

import android.content.Context;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class CustomArrayAdapter extends ArrayAdapter<Photo> {
    private Context mContext;
    private int mResource;
    List<Photo> items;
    public CustomArrayAdapter(@NonNull Context context, int resource, @NonNull ArrayList<Photo> objects) {
        super(context, resource, objects);
        this.mContext = context;
        this.mResource = resource;
        this.items = objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        if (convertView == null)
        {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.list_results, null);
        }

        Photo photo = items.get(position);
        if (photo!=null) {
            ImageView iv = (ImageView) convertView.findViewById(R.id.searchResultImage);
            if (iv!=null)
            {
                iv.setImageURI(photo.getImage());
            }
        }

        return convertView;
    }
}

