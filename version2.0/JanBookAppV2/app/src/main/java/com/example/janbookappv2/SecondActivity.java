package com.example.janbookappv2;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
@SuppressWarnings("deprecation")
public class SecondActivity extends AppCompatActivity {

    private static final String TAG = "SecondActivity";
    private TextView textViewTitle, textViewAuthor, textViewYear;
    private ImageView imageViewPhoto;
    private Button btnTakePhoto, btnNext;
    private Book book;
    private Bitmap photoBitmap;
    private boolean hasCustomPhoto = false;

    private static final String KEY_PHOTO = "photo";
    private static final String KEY_HAS_PHOTO = "hasPhoto";

    // Modern way to handle camera permission
    private ActivityResultLauncher<String> requestPermissionLauncher;

    // Modern way to handle camera result
    private ActivityResultLauncher<Intent> takePictureLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        Log.d(TAG, "SecondActivity onCreate called");

        // Initialize views
        textViewTitle = findViewById(R.id.textViewTitle);
        textViewAuthor = findViewById(R.id.textViewAuthor);
        textViewYear = findViewById(R.id.textViewYear);
        imageViewPhoto = findViewById(R.id.imageViewPhoto);
        btnTakePhoto = findViewById(R.id.btnTakePhoto);
        btnNext = findViewById(R.id.btnNext);

        // Check if views are found
        if (textViewTitle == null || textViewAuthor == null || textViewYear == null) {
            Log.e(TAG, "ERROR: TextViews not found in layout!");
            Toast.makeText(this, "Error: Views not found", Toast.LENGTH_LONG).show();
            return;
        }

        Log.d(TAG, "All views initialized successfully");

        // Initialize modern ActivityResultLaunchers
        setupActivityResultLaunchers();

        // Get Book object from intent
        Intent intent = getIntent();
        if (intent != null) {
            Log.d(TAG, "Intent received");

            book = intent.getParcelableExtra("BOOK_DATA");
            if (book == null) {
                Log.w(TAG, "Book not found with key 'BOOK_DATA', trying 'book'");
                book = intent.getParcelableExtra("book");
            }

            if (book != null) {
                Log.d(TAG, "Book object received successfully!");
                Log.d(TAG, "Title: " + book.getTitle());
                Log.d(TAG, "Author: " + book.getAuthor());
                Log.d(TAG, "Year: " + book.getYear());

                textViewTitle.setText(book.getTitle());
                textViewAuthor.setText(book.getAuthor());
                textViewYear.setText(book.getYear());

                Log.d(TAG, "Book details displayed");
            } else {
                Log.e(TAG, "ERROR: Book object is NULL!");
                Toast.makeText(this, "Error: No book data received", Toast.LENGTH_LONG).show();

                textViewTitle.setText("No Title Received");
                textViewAuthor.setText("No Author Received");
                textViewYear.setText("No Year Received");
            }
        } else {
            Log.e(TAG, "ERROR: Intent is NULL!");
            Toast.makeText(this, "Error: No intent received", Toast.LENGTH_LONG).show();
        }

        // Restore photo if exists
        if (savedInstanceState != null) {
            photoBitmap = savedInstanceState.getParcelable(KEY_PHOTO);
            hasCustomPhoto = savedInstanceState.getBoolean(KEY_HAS_PHOTO, false);
            if (photoBitmap != null && hasCustomPhoto) {
                imageViewPhoto.setImageBitmap(photoBitmap);
            }
        }

        // Take Photo button
        btnTakePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "Take Photo button clicked");
                checkCameraPermissionAndOpen();
            }
        });

        // Next Activity button
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "Next button clicked");
                Intent intent = new Intent(SecondActivity.this, ThirdActivity.class);
                startActivity(intent);
            }
        });
    }

    // Setup modern ActivityResultLaunchers (no deprecation warnings!)
    private void setupActivityResultLaunchers() {
        // Camera permission launcher
        requestPermissionLauncher = registerForActivityResult(
                new ActivityResultContracts.RequestPermission(),
                new ActivityResultCallback<Boolean>() {
                    @Override
                    public void onActivityResult(Boolean isGranted) {
                        if (isGranted) {
                            Log.d(TAG, "Camera permission granted");
                            openCamera();
                        } else {
                            Log.w(TAG, "Camera permission denied");
                            Toast.makeText(SecondActivity.this,
                                    "Camera permission denied", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
        );

        // Camera result launcher
        takePictureLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == RESULT_OK) {
                            Intent data = result.getData();
                            if (data != null) {
                                Log.d(TAG, "Photo captured successfully");
                                Bundle extras = data.getExtras();
                                photoBitmap = (Bitmap) extras.get("data");
                                imageViewPhoto.setImageBitmap(photoBitmap);
                                hasCustomPhoto = true;
                            }
                        }
                    }
                }
        );
    }

    private void checkCameraPermissionAndOpen() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED) {
            Log.d(TAG, "Camera permission already granted");
            openCamera();
        } else {
            Log.d(TAG, "Requesting camera permission");
            requestPermissionLauncher.launch(Manifest.permission.CAMERA);
        }
    }

    private void openCamera() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            Log.d(TAG, "Opening camera");
            takePictureLauncher.launch(takePictureIntent);
        } else {
            Log.e(TAG, "No camera app found");
            Toast.makeText(this, "No camera app found", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        if (photoBitmap != null) {
            outState.putParcelable(KEY_PHOTO, photoBitmap);
            outState.putBoolean(KEY_HAS_PHOTO, hasCustomPhoto);
        }
    }
}