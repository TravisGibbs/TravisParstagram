package com.example.travisparstagram.fragments;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.travisparstagram.MainActivity;
import com.example.travisparstagram.Post;
import com.example.travisparstagram.R;
import com.google.android.material.snackbar.Snackbar;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.File;
import java.util.List;

import static android.app.Activity.RESULT_OK;


public class composeFragment extends Fragment {

    EditText descriptionText;
    ImageView imageTaken;
    Button takePhoto;
    Button postPhoto;
    String Tag = "composeFragment";
    LinearLayout linearLayout;
    public static final int  CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 42;
    public String photoFileName = "photo.jpg";
    private File photoFile;
    ProgressBar pb;


    public composeFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_compose, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        descriptionText = view.findViewById(R.id.descriptionText);
        imageTaken = view.findViewById(R.id.imageTaken);
        takePhoto = view.findViewById(R.id.cameraButton);
        postPhoto = view.findViewById(R.id.postButton);
        imageTaken.setVisibility(View.GONE);
        linearLayout = view.findViewById(R.id.linearCompose);
        pb = (ProgressBar) view.findViewById(R.id.pbLoading);
        pb.setVisibility(ProgressBar.GONE);
        takePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchCamera();
            }
        });

        postPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pb.setVisibility(ProgressBar.VISIBLE);
                String description = descriptionText.getText().toString();
                if(description == ""){
                    Snackbar.make(linearLayout, "No description", Snackbar.LENGTH_SHORT).show();
                    pb.setVisibility(ProgressBar.GONE);

                    return;
                }
                if(photoFile == null || imageTaken.getDrawable()==null){
                    Snackbar.make(linearLayout, "No image", Snackbar.LENGTH_SHORT).show();
                    pb.setVisibility(ProgressBar.GONE);
                    return;
                }
                ParseUser parseUser = ParseUser.getCurrentUser();
                savePost(description,parseUser,photoFile);

            }
        });
    }

    private void launchCamera() {
        Log.i(Tag,"here");
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Create a File reference for future access
        photoFile = getPhotoFileUri(photoFileName);

        // wrap File object into a content provider
        // required for API >= 24
        // See https://guides.codepath.com/android/Sharing-Content-with-Intents#sharing-files-with-api-24-or-higher
        Uri fileProvider = FileProvider.getUriForFile(getContext(), "com.codepath.fileprovider", photoFile);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider);

        // If you call startActivityForResult() using an intent that no app can handle, your app will crash.
        // So as long as the result is not null, it's safe to use the intent.
        startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);

        if (intent.resolveActivity(getContext().getPackageManager()) != null) {
            // Start the image capture intent to take photo
            startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
        }
    }

    public File getPhotoFileUri(String fileName) {
        // Get safe storage directory for photos
        // Use `getExternalFilesDir` on Context to access package-specific directories.
        // This way, we don't need to request external read/write runtime permissions.
        File mediaStorageDir = new File(getContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES), Tag);

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists() && !mediaStorageDir.mkdirs()){
            Log.d(Tag, "failed to create directory");
        }

        // Return the file target for the photo based on filename
        File file = new File(mediaStorageDir.getPath() + File.separator + fileName);

        return file;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                // by this point we have the camera photo on disk
                Bitmap takenImage = BitmapFactory.decodeFile(photoFile.getAbsolutePath());
                // RESIZE BITMAP, see section below
                // Load the taken image into a preview
                imageTaken.setImageBitmap(takenImage);
                imageTaken.setVisibility(View.VISIBLE);
            } else { // Result was a failure
                Toast.makeText(getContext(), "Picture wasn't taken!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void savePost (String description, ParseUser parseUser, File photoFile){
        Post post = new Post();
        post.setKeyDescription(description);
        post.setUser(parseUser);
        post.setImage(new ParseFile(photoFile));
        post.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e != null) {
                    Log.e(Tag, "post failed", e);
                    pb.setVisibility(ProgressBar.GONE);
                }
                Log.i(Tag, "post succ");
                descriptionText.setText("");
                imageTaken.setImageResource(0);
                imageTaken.setVisibility(View.GONE);
                pb.setVisibility(ProgressBar.GONE);
            }
        });
    }


}