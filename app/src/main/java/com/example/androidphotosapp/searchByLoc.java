package com.example.androidphotosapp;

import android.app.ListActivity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class searchByLoc extends AppCompatActivity {


    AutoCompleteTextView textFieldOne;
    AutoCompleteTextView textFieldTwo;

    ListView searchResults;

    Spinner conjunctionSpinner;
    Spinner tagName1;
    Spinner tagName2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Context context = getApplicationContext();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_by_loc);

        textFieldOne = (AutoCompleteTextView) findViewById(R.id.enterFirstLocation);
        textFieldTwo = (AutoCompleteTextView) findViewById(R.id.enterSecondLocation);

        //Back button functionality
        Button backButton = (Button) findViewById(R.id.backButton);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //Set the dropdown menus for conjunction/disjunction and Tag Name
        conjunctionSpinner = (Spinner)findViewById(R.id.spinner2);
        tagName1 = (Spinner)findViewById(R.id.TagName1);
        tagName2 = (Spinner)findViewById(R.id.TagName2);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this,
                R.array.conjunctions,
                android.R.layout.simple_spinner_item
        );

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        conjunctionSpinner.setAdapter(adapter);

        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(
                this,
                R.array.tagNameChoices,
                android.R.layout.simple_spinner_item
        );

        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        tagName1.setAdapter(adapter2);
        tagName2.setAdapter(adapter2);

        //search button functionality (
        Button searchButton = (Button) findViewById(R.id.searchButton);

        searchResults = findViewById(R.id.searchResults);

        ArrayList<Photo> listOfMatches = new ArrayList<Photo>();

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listOfMatches.removeAll(listOfMatches);
                String location1 = textFieldOne.getText().toString();
                String location2 = textFieldTwo.getText().toString();

                String firstTagName = tagName1.getSelectedItem().toString();
                String secondTagName = tagName2.getSelectedItem().toString();
                String conOrDis = conjunctionSpinner.getSelectedItem().toString();

                Log.d("myTag", location1 + " "+ conOrDis + " " + location2);

                if (conOrDis.equals("OR")) {
                    for (Album a : MainActivity.albums) {
                        for (Photo p : a.photos) {
                            boolean foundMatch = false;
                            for (int i = 0; i < p.tagValues.size(); i++) {
                                if (foundMatch == false && ((p.tagNames.get(i).equalsIgnoreCase(firstTagName) && p.tagValues.get(i).equalsIgnoreCase(location1)) || (p.tagNames.get(i).equalsIgnoreCase(secondTagName) && p.tagValues.get(i).equalsIgnoreCase(location2)))) {
                                        Log.d("Match found", p.tagValues.get(i));
                                        listOfMatches.add(p);
                                        foundMatch = true;
                                }
                            }
                        }
                    }
                }
                if (conOrDis.equals("AND")) {
                    for (Album a : MainActivity.albums) {
                        for (Photo p : a.photos) {
                            boolean tag1Found = false;
                            boolean tag2Found = false;
                            for (int i = 0; i < p.tagValues.size(); i++) {
                                if ((p.tagNames.get(i).equalsIgnoreCase(firstTagName) && p.tagValues.get(i).equalsIgnoreCase(location1))) {
                                    tag1Found = true;
                                }
                                if ((p.tagNames.get(i).equalsIgnoreCase(secondTagName) && p.tagValues.get(i).equalsIgnoreCase(location2))) {
                                    tag2Found = true;
                                }
                            }
                            if (tag1Found && tag2Found)
                            {
                                Log.d("Match found", p.toString());
                                listOfMatches.add(p);
                            }
                        }
                    }
                }
                if (conOrDis.equals("No Conjunction/Disjunction")) {
                    for (Album a : MainActivity.albums) {
                        for (Photo p : a.photos) {
                            for (int i = 0; i < p.tagValues.size(); i++) {
                                if ((p.tagNames.get(i).equalsIgnoreCase(firstTagName) && p.tagValues.get(i).equalsIgnoreCase(location1))) {
                                    Log.d("Match found", p.toString());
                                    listOfMatches.add(p);
                                }
                            }
                        }
                    }
                }
                CustomArrayAdapter customArrayAdapter = new CustomArrayAdapter(context, R.layout.list_results, listOfMatches);
                searchResults.setAdapter(customArrayAdapter);
            }
        });


        //Load location and person tags into the auto complete menues

            //Store locaiton tag values into a string array
        ArrayList<String> allLocations = new ArrayList<String>();

        for (Album a: MainActivity.albums) {
            for (Photo p: a.photos) {
                for (int i = 0 ; i < p.tagValues.size() ;i++)
                {
                    if (p.tagNames.get(i).equals("Location") || p.tagNames.get(i).equals("location"))
                    {
                        allLocations.add(p.tagValues.get(i));
                    }
                    if (p.tagNames.get(i).equals("Person") || p.tagNames.get(i).equals("person"))
                    {
                        allLocations.add(p.tagValues.get(i));
                    }
                }
            }
        }

        String[] locationsList = new String[allLocations.size()];
        locationsList = allLocations.toArray(locationsList);

        ArrayAdapter dataSet = new ArrayAdapter(this, android.R.layout.select_dialog_item, locationsList);

        textFieldOne.setThreshold(1);
        textFieldOne.setAdapter(dataSet);
        textFieldTwo.setThreshold(1);
        textFieldTwo.setAdapter(dataSet);

        //Get selected tag on search press





    }

}
