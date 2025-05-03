package com.example.t3mobile_h071231080;

import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class PostActivity extends AppCompatActivity {

    private static final int PICK_IMAGE = 100;
    private ImageView imagePreview;
    private EditText captionInput;
    private Uri selectedImageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        imagePreview = findViewById(R.id.imagePreview);
        captionInput = findViewById(R.id.captionInput);
        Button selectImageButton = findViewById(R.id.selectImageButton);
        Button postButton = findViewById(R.id.postButton);

        selectImageButton.setOnClickListener(v -> openGallery());

        postButton.setOnClickListener(v -> {
            String caption = captionInput.getText().toString();
            if (caption.isEmpty()) {
                Toast.makeText(this, "Please enter a caption", Toast.LENGTH_SHORT).show();
                return;
            }

            if (selectedImageUri == null) {
                Toast.makeText(this, "Please select an image", Toast.LENGTH_SHORT).show();
                return;
            }

            Uri persistentUri = saveImageToInternalStorage(selectedImageUri);
            if (persistentUri != null) {
                int newPostId = 200 + DataStorage.getNewPosts().size();
                Post newPost = new Post(
                        newPostId,
                        "fadillanstura",
                        R.drawable.profile,
                        persistentUri.toString(),
                        caption,
                        0,
                        0
                );

                DataStorage.addNewPost(newPost);

                Toast.makeText(this, "Post added successfully!", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(PostActivity.this, ProfileActivity.class);
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(this, "Failed to save image", Toast.LENGTH_SHORT).show();
            }
        });

        // Set up bottom navigation
        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setSelectedItemId(R.id.nav_add);
        bottomNav.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_home) {
                startActivity(new Intent(PostActivity.this, MainActivity.class));
                return true;
            } else if (id == R.id.nav_add) {
                return true;
            } else if (id == R.id.nav_profile) {
                startActivity(new Intent(PostActivity.this, ProfileActivity.class));
                return true;
            }
            return false;
        });
    }

    private void openGallery() {
        Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(gallery, PICK_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == PICK_IMAGE) {
            selectedImageUri = data.getData();
            try {
                final int takeFlags = Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION;
                getContentResolver().takePersistableUriPermission(selectedImageUri, takeFlags);
            } catch (SecurityException e) {
                // Log removed
            }
            imagePreview.setImageURI(selectedImageUri);
        }
    }

    private Uri saveImageToInternalStorage(Uri sourceUri) {
        try {
            File imagesDir = new File(getFilesDir(), "images");
            if (!imagesDir.exists()) {
                imagesDir.mkdirs();
            }

            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
            File destFile = new File(imagesDir, "IMG_" + timeStamp + ".jpg");

            Bitmap bitmap = getBitmapFromUri(sourceUri);
            if (bitmap != null) {
                FileOutputStream fos = new FileOutputStream(destFile);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 90, fos);
                fos.close();
                return Uri.fromFile(destFile);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private Bitmap getBitmapFromUri(Uri uri) throws IOException {
        ContentResolver resolver = getContentResolver();
        ParcelFileDescriptor parcelFileDescriptor = resolver.openFileDescriptor(uri, "r");
        if (parcelFileDescriptor != null) {
            FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
            Bitmap image = BitmapFactory.decodeFileDescriptor(fileDescriptor);
            parcelFileDescriptor.close();
            return image;
        }
        return null;
    }
}
