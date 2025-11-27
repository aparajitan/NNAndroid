package com.app_neighbrsnook.demo;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.app_neighbrsnook.R;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.text.Text;
import com.google.mlkit.vision.text.TextRecognition;
import com.google.mlkit.vision.text.TextRecognizer;
import com.google.mlkit.vision.text.latin.TextRecognizerOptions;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AdharBlurActivity extends AppCompatActivity {

    private ImageView imageView;
    private Uri imageUri;
    private static final Pattern AADHAAR_PATTERN = Pattern.compile("\\b\\d{4}\\s\\d{4}\\s\\d{4}\\b"); // Aadhaar format

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adhar_blur);

        imageView = findViewById(R.id.imageView);
        Button btnSelectImage = findViewById(R.id.btnSelectImage);

        btnSelectImage.setOnClickListener(v -> selectImageFromGallery());
    }

    private void selectImageFromGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        pickImageLauncher.launch(intent);
    }

    private final ActivityResultLauncher<Intent> pickImageLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    imageUri = result.getData().getData();
                    processAadhaarImage(imageUri);
                }
            });

    private void processAadhaarImage(Uri uri) {
        try {
            InputImage image = InputImage.fromFilePath(this, uri);
            TextRecognizer recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS);

            recognizer.process(image)
                    .addOnSuccessListener(visionText -> {
                        Bitmap modifiedBitmap = blurAadhaarNumber(uri, visionText);
                        if (modifiedBitmap != null) {
                            imageView.setImageBitmap(modifiedBitmap);
                        } else {
                            Toast.makeText(AdharBlurActivity.this, "Failed to process image", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(AdharBlurActivity.this, "Text recognition failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    });

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Bitmap blurAadhaarNumber(Uri imageUri, Text visionText) {
        try {
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
            Bitmap mutableBitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true);
            Canvas canvas = new Canvas(mutableBitmap);
            Paint paint = new Paint();
            paint.setColor(Color.BLACK);
            paint.setStyle(Paint.Style.FILL);

            for (Text.TextBlock block : visionText.getTextBlocks()) {
                String text = block.getText();
                Matcher matcher = AADHAAR_PATTERN.matcher(text);

                if (matcher.find()) {  // Aadhaar found in the block
                    for (Text.Line line : block.getLines()) { // Use getLines() instead of getElements()
                        String lineText = line.getText();
                        if (AADHAAR_PATTERN.matcher(lineText).matches()) {
                            Rect boundingBox = line.getBoundingBox(); // Get bounding box
                            if (boundingBox != null) {
                                canvas.drawRect(boundingBox, paint); // Draw a black box to hide the number
                            }
                        }
                    }
                }
            }

            return mutableBitmap;

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
