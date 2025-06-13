package com.example.androidphotosapp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

//import com.google.gson.Gson;

import java.sql.Struct;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST_CODE = 1;
    private static final int REQUEST_GALLERY = 1;
    // Global things
    ArrayAdapter<String> albumsAdapter; // Adapter for albums ListView
    public static ArrayList<Album> albums = new ArrayList<Album>();
    //ListView albumsListView = findViewById(R.id.albumsListView);

    // only one user (device)
    //User Device = new User("Device", 101);

    Button searchByLocationButton;


    public static String selection = null;
    public static int imageIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        searchByLocationButton = (Button)findViewById(R.id.searchByLocationButton);
        searchByLocationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public  void onClick(View v) {
                startActivity(new Intent(MainActivity.this, searchByLoc.class));
            }
        });

        // Initialize ListView and its adapter
        ListView albumsListView = findViewById(R.id.albumsListView);
        albumsAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);
        albumsListView.setAdapter(albumsAdapter);
        albumsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Handle item click here
                selection = (String) parent.getItemAtPosition(position);
                //Toast.makeText(getApplicationContext(), "Item clicked: " + selection, Toast.LENGTH_SHORT).show();
                if (!albums.isEmpty()) {
                    Album current = getSelectedAlbum();
                    if (!current.getPhotos().isEmpty()) {
                        ImageView imageView = findViewById(R.id.imageView);
                        imageView.setImageURI(current.getPhotos().get(0).getImage());
                        imageIndex = 0;
                        TextView textView = findViewById(R.id.detailsheaderlabel);
                        StringBuilder allTags = new StringBuilder();
                        allTags.append("Photo Tags\n");
                        for (int i = 0; i < current.photos.get(imageIndex).tagNames.size(); i++) {
                            allTags.append(current.photos.get(imageIndex).getTagNameAt(i) + ": " + current.photos.get(imageIndex).getTagValueAt(i) + "\n");
                            textView.setText(allTags.toString());

                        } if (current.photos.get(imageIndex).tagNames.isEmpty()){
                            textView.setText("No Tags");
                        }
                        //imageView.notify();
                        displayMultipleImages();

                    }
                }
                Album current = getSelectedAlbum();
                if (current.photos.isEmpty()){
                    TextView textView = findViewById(R.id.detailsheaderlabel);
                    textView.setText("No Images");
                    ImageView imageView = findViewById(R.id.imageView);
                    imageView.setImageURI(null);
                    androidx.gridlayout.widget.GridLayout GL = findViewById(R.id.GLPhotos);
                    GL.removeAllViews();

                }

            }
        });
        //Toast.makeText(getApplicationContext(), "Albums: " + albums.size(), Toast.LENGTH_SHORT).show();
        for (int i = 0; i < albums.size(); i++){
            albumsAdapter.add(albums.get(i).getName());
            albumsAdapter.notifyDataSetChanged();

        }
        TextView textView = findViewById(R.id.detailsheaderlabel);
        textView.setText("No Albums");

    }

    @Override
    protected void onResume() {
        super.onResume();
        // Clear the existing data in the adapter
        albumsAdapter.clear();

        // Repopulate the adapter with the updated list of albums
        for (Album album : albums) {
            albumsAdapter.add(album.getName());
        }

        // Notify the adapter that the data set has changed
        albumsAdapter.notifyDataSetChanged();
    }

    public void createAlbum(View view) {
        // Create a new AlertDialog for user input
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Add Album");
        builder.setMessage("Enter Album Name:");

        // Create a new EditText for user input
        final EditText input = new EditText(MainActivity.this);
        builder.setView(input);

        // Add buttons to the dialog
        builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // If "Add" button is clicked, retrieve the input from the EditText
                String name = input.getText().toString().trim();
                // Add the album
                if (!name.isEmpty()) {
                    // Add the album name to the adapter
                    Album toAdd = new Album(name);
                    albums.add(toAdd);
                    albumsAdapter.add(toAdd.getName());
                    // Notify the adapter that the data set has changed
                    albumsAdapter.notifyDataSetChanged();
                    TextView textView = findViewById(R.id.detailsheaderlabel);
                    textView.setText("No Images");

                }
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        // Show the dialog
        builder.show();
    }

    public void removeAlbum(View view) {
        if (albums.isEmpty()){
            Toast.makeText(this, "No Albums", Toast.LENGTH_SHORT).show();
            return;
        }
        // Create a new AlertDialog for user input
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Delete Album");
        builder.setMessage("Enter Album Name:");

        // Create a new EditText for user input
        final EditText input = new EditText(MainActivity.this);
        builder.setView(input);

        // Add buttons to the dialog
        builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // If "Add" button is clicked, retrieve the input from the EditText
                String name = input.getText().toString().trim();
                // Add the album
                if (!name.isEmpty()) {
                    // Add the album name to the adapter
                    int indexToRemove = 0;
                    for (int i = 0; i < albums.size(); i++){
                        if (albums.get(i).getName() == name){
                            indexToRemove = i;
                        }
                    }
                    albums.remove(albums.get(indexToRemove));
                    albumsAdapter.remove(name);
                    // Notify the adapter that the data set has changed
                    albumsAdapter.notifyDataSetChanged();
                }
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        // Show the dialog
        builder.show();
    }

    public void renameAlbum(View view) {
        // Create a new AlertDialog for user input
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Rename Album");

// Inflate the custom layout
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.doubletextpopup, null);
        builder.setView(dialogView);

// Find the EditText fields in the custom layout
        final EditText input1 = dialogView.findViewById(R.id.editText1);
        final EditText input2 = dialogView.findViewById(R.id.editText2);

// Set up the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Retrieve the text from the EditText fields
                String text1 = input1.getText().toString().trim();
                String text2 = input2.getText().toString().trim();

                // Handle the input data as needed
                int index = -1;
                if (!albums.isEmpty()){

                    for (int i = 0; i < albums.size(); i++) {
                        if (albums.get(i).getName().compareTo(text1) == 0) {

                            index = i;
                            break;

                        }
                    }

                }

                if (index != -1) {
                    albums.get(index).setName(text2);
                    albumsAdapter.remove(text1);
                    albumsAdapter.insert(text2, index);
                }
                if (index == -1){
                    Toast.makeText(MainActivity.this, "Album doesnt exist", Toast.LENGTH_SHORT).show();

                    return;
                }
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

// Show the AlertDialog
        builder.show();
    }


public void addImage(View view) {

    Album selectedAlbum = null;
    if (albums.isEmpty()){
        Toast.makeText(this, "No Albums", Toast.LENGTH_SHORT).show();

        return;
    }
    for (int i = 0; i < albums.size(); i++) {
        if (albums.get(i).getName() == selection) {
            selectedAlbum = albums.get(i);
        }
    }
    openGallery();
}

    // Method to open the image gallery
    private void openGallery() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, REQUEST_GALLERY);
    }

    // Override onActivityResult to handle the result
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_GALLERY) {
                // Handle gallery selection
                Uri selectedImageUri = data.getData();
                // Do something with the selected image URI
                Toast.makeText(this, "Gallery Image Selected", Toast.LENGTH_SHORT).show();
                Album a = getSelectedAlbum();
                Photo toAdd = new Photo(0, "no name", selectedImageUri);
                a.photos.add(toAdd);
                toAdd.getTagList().clear();
                toAdd.getValuesList().clear();
                ImageView imageView = findViewById(R.id.imageView);
                imageView.setImageURI(toAdd.getImage());
                setDetails();
                displayMultipleImages();

            }
        }
    }


public void deleteImage(View view){
    if (albums.isEmpty()){
        Toast.makeText(this, "No Albums", Toast.LENGTH_SHORT).show();

        return;
    }
    Album selectedAlbum = getSelectedAlbum();

    if (selectedAlbum.photos.isEmpty()){
        Toast.makeText(this, "Select NON-empty album", Toast.LENGTH_SHORT).show();
        return;


    }
    if (selectedAlbum != null) {
        if (!selectedAlbum.photos.isEmpty()){
        selectedAlbum.photos.remove(imageIndex);
        if (imageIndex != 0) {
            imageIndex--;
            ImageView imageView = findViewById(R.id.imageView);
            imageView.setImageURI(selectedAlbum.photos.get(imageIndex).getImage());

        }
        if (selectedAlbum.photos.size() > 1) {
            ImageView imageView = findViewById(R.id.imageView);
            imageView.setImageURI(selectedAlbum.photos.get(imageIndex).getImage());

        }
            if (!selectedAlbum.photos.isEmpty()) {
                ImageView imageView = findViewById(R.id.imageView);
                setDetails();
            }
            if (selectedAlbum.photos.isEmpty()){
                ImageView imageView = findViewById(R.id.imageView);
                imageView.setImageURI(null);
                TextView textView = findViewById(R.id.detailsheaderlabel);
                textView.setText("No Images");

            }
            displayMultipleImages();

        }}
}


public Album getSelectedAlbum(){
    Album selectedAlbum = null;
    for (int i = 0; i < albums.size(); i++) {
        if (albums.get(i).getName() == selection) {
            selectedAlbum = albums.get(i);
        }
    }
    return selectedAlbum;
}


public void next(View view){
    if (albums.isEmpty()){
        Toast.makeText(this, "No album selected", Toast.LENGTH_SHORT).show();
        return;
    }
        Album selectedAlbum = getSelectedAlbum();
    if (selectedAlbum.photos.isEmpty()){
        Toast.makeText(this, "No Images", Toast.LENGTH_SHORT).show();
        return;
    }
    if ((imageIndex + 1) < selectedAlbum.photos.size()){
        imageIndex++;
        ImageView imageView = findViewById(R.id.imageView);
        imageView.setImageURI(selectedAlbum.photos.get(imageIndex).getImage());

    }
    setDetails();

}

public void prev(View view){
    if (albums.isEmpty()){
        Toast.makeText(this, "No album selected", Toast.LENGTH_SHORT).show();
        return;
    }
    Album selectedAlbum = getSelectedAlbum();
    if (selectedAlbum.photos.isEmpty()){
        Toast.makeText(this, "No Images", Toast.LENGTH_SHORT).show();
        return;
    }
    if ((imageIndex - 1) >= 0){
        imageIndex--;
        ImageView imageView = findViewById(R.id.imageView);
        imageView.setImageURI(selectedAlbum.photos.get(imageIndex).getImage());


    }
    setDetails();

}


public void Tag(View view){
    if (albums.isEmpty()){
        Toast.makeText(this, "No Albums", Toast.LENGTH_SHORT).show();

        return;
    }
    // Create a new AlertDialog for user input
    //Toast.makeText(this, "Tag Add", Toast.LENGTH_SHORT).show();
    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
    builder.setTitle("Add Tag");

// Inflate the custom layout
    LayoutInflater inflater = getLayoutInflater();
    View dialogView = inflater.inflate(R.layout.doubletexttag, null);
    builder.setView(dialogView);

// Find the EditText fields in the custom layout
    final Spinner input1 = (Spinner)dialogView.findViewById(R.id.LocOrPer);
    final EditText input2 = dialogView.findViewById(R.id.editText2);

    ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
            this,
            R.array.tagNameChoices,
            android.R.layout.simple_spinner_item
    );

    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    input1.setAdapter(adapter);


// Set up the buttons
    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            // Retrieve the text from the EditText fields
            Album current = getSelectedAlbum();
            if (current != null) {
                if (!current.photos.isEmpty()) {
                    String tag = input1.getSelectedItem().toString();
                    String value = input2.getText().toString().trim();
                    Album selectedAlbum = getSelectedAlbum();
                    selectedAlbum.photos.get(imageIndex).setTag(tag, value);
                    setDetails();
                }
                if (current.photos.isEmpty() || current == null){
                    Toast.makeText(MainActivity.this, "No image to Tag", Toast.LENGTH_SHORT).show();

                }


            }
        }
    });
builder.show();




}

public void DeTag(View view){
    if (albums.isEmpty()){
        Toast.makeText(this, "No Albums", Toast.LENGTH_SHORT).show();
        return;
    }
    // Create a new AlertDialog for user input
    //Toast.makeText(this, "Tag Removal", Toast.LENGTH_SHORT).show();
    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
    builder.setTitle("Remove Tag");

// Inflate the custom layout
    LayoutInflater inflater = getLayoutInflater();
    View dialogView = inflater.inflate(R.layout.doubletexttag, null);
    builder.setView(dialogView);

// Find the EditText fields in the custom layout
    final Spinner input1 = (Spinner)dialogView.findViewById(R.id.LocOrPer);
    final EditText input2 = dialogView.findViewById(R.id.editText2);

    ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
            this,
            R.array.tagNameChoices,
            android.R.layout.simple_spinner_item
    );

    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    input1.setAdapter(adapter);

// Set up the buttons
    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            // Retrieve the text from the EditText fields
            Album current = getSelectedAlbum();
            if (current != null) {
                if (!current.photos.isEmpty()) {
                    String tag = input1.getSelectedItem().toString();
                    String value = input2.getText().toString().trim();
                    Album selectedAlbum = getSelectedAlbum();
                    selectedAlbum.photos.get(imageIndex).removeTag(tag, value);
                    setDetails();
                }
                if (current.photos.isEmpty() || current == null){
                    Toast.makeText(MainActivity.this, "No image to DeTag", Toast.LENGTH_SHORT).show();

                }
            }
        }
    });
    builder.show();


}


public void setDetails() {
    //Toast.makeText(this, "Setting Tags", Toast.LENGTH_SHORT).show();

    Album current = getSelectedAlbum();

    TextView textView = findViewById(R.id.detailsheaderlabel);
    StringBuilder allTags = new StringBuilder();
    allTags.append("Photo Tags\n");
    int numTags = current.photos.get(imageIndex).tagNames.size();
    //Toast.makeText(this, "# Tags: " + numTags, Toast.LENGTH_SHORT).show();

    if (numTags == 0){
        textView.setText("No Tags");
        return;
    }

    for (int i = 0; i < numTags; i++) {
        if (current.photos.get(imageIndex).tagNames != null) {
            allTags.append(current.photos.get(imageIndex).getTagNameAt(i) + ": " + current.photos.get(imageIndex).getTagValueAt(i) + "\n");
            textView.setText(allTags.toString());
        }

    }
}


public void moveImage(View view){

        Album current = getSelectedAlbum();
        if (current == null){
            Toast.makeText(MainActivity.this, "Select an Album", Toast.LENGTH_SHORT).show();

            return;
        }

        if (current.photos.isEmpty()){
            Toast.makeText(MainActivity.this, "No photos to move", Toast.LENGTH_SHORT).show();

            return;
        }
        Photo currentPhoto = current.photos.get(imageIndex);

    // Create a new AlertDialog for user input
    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
    builder.setTitle("Move Image");
    builder.setMessage("Enter album to move to:");

    // Create a new EditText for user input
    final EditText input = new EditText(MainActivity.this);
    builder.setView(input);

    // Add buttons to the dialog
    builder.setPositiveButton("Move", new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            // Retrieve the input from the EditText
            String name = input.getText().toString().trim();
            if (!name.isEmpty()) {
                int index = -1; // Initialize to -1 to indicate not found
                for (int i = 0; i < albums.size(); i++) {
                    if (albums.get(i).getName().equals(name)) {
                        index = i;
                        break; // No need to continue searching
                    }
                }
                if (index != -1) { // Check if the album exists
                    albums.get(index).photos.add(currentPhoto);
                    current.photos.remove(currentPhoto);
                    // Update adapters for both albums
                    albumsAdapter.notifyDataSetChanged();
                    // Update adapter for the current album
                    // Assuming you have a reference to the adapter for the current album
                    // currentAlbumAdapter.notifyDataSetChanged();
                    TextView textView = findViewById(R.id.detailsheaderlabel);
                    textView.setText("Photo Moved");
                    displayMultipleImages();
                    if (!current.photos.isEmpty()) {
                        imageIndex--;
                        setDetails();
                        ImageView imageView = findViewById(R.id.imageView);
                        imageView.setImageURI(current.photos.get(imageIndex).getImage());
                    } else if (current.photos.isEmpty()){
                        ImageView imageView = findViewById(R.id.imageView);
                        imageView.setImageURI(null);

                    }

                } else {
                    // Handle case where album doesn't exist
                    Toast.makeText(MainActivity.this, "Album not found", Toast.LENGTH_SHORT).show();
                }
            }
        }
    });


    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            dialog.cancel();
        }
    });

    // Show the dialog
    builder.show();

}

    public void displayMultipleImages() {
        Album current = getSelectedAlbum();

        // Clear existing images
        androidx.gridlayout.widget.GridLayout GL = findViewById(R.id.GLPhotos);

        GL.removeAllViews();
        //GL.setColumnCount(2);

        // Load images from the selected album
        ArrayList<Uri> imageList = new ArrayList<>();
        for (Photo photo : current.getPhotos()) {
            imageList.add(photo.getImage());
        }

        // Loop through image Bitmaps and create ImageView objects
        int check = 0;
        for (Uri Uris : imageList) {
            check++;
            ImageView imageView = new ImageView(this);
            imageView.setImageURI(Uris);
            imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
            imageView.setLayoutParams(new ViewGroup.LayoutParams(
                    300, // Adjust width to wrap content
                    200)); // Adjust height to wrap content
            GL.addView(imageView); // Add ImageView to GridLayout
        }
    }




    }






