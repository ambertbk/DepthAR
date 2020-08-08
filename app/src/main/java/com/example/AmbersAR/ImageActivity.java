package com.example.AmbersAR;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;

public class ImageActivity extends AppCompatActivity {
    private ImageView image;

    private SeekBar seekBar;
    private TextView seekBarValue;

    private Drawable drawable;

    private Drawable initialImage;

    private int quality = 100;
    String imagePath = null;
    Uri imageURI = null;

    private static final int REQUEST_GALLERY = 1;
    private static final int REQUEST_CAMERA = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);

        image = findViewById(R.id.imageView);

        seekBar = findViewById(R.id.seekBar);
        seekBarValue = findViewById(R.id.textView2);

        drawable = getResources().getDrawable(R.drawable.ic_action_upload_image);

        initialImage = image.getDrawable();

        System.out.println("************************** initialImage: " + initialImage);

//        SeekbarFragment seekbarFragment = new SeekbarFragment();
//        seekbarFragment.initView();

        setSeekBar();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void onClick(View v) throws UnknownHostException {
        switch (v.getId()) {
            case R.id.imageView: // On clicking on the imageView, open the gallery and display the image on the imageView
                final CharSequence[] options = {"Camera", "Gallery", "Cancel"};

                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Select Image from");
                builder.setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (options[which].equals("Camera")) {
                            // EVOKE CAMERA
                            dispatchTakePictureIntent();
                        } else if (options[which].equals("Gallery")) {
                            // EVOKE GALLERY
                            Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                            startActivityForResult(galleryIntent, REQUEST_GALLERY);
                        } else if (options[which].equals("Cancel")) {
                            dialog.dismiss();
                        }
                    }
                });
                builder.show();
                break;
            case R.id.imageButton:
                if (image.getDrawable() != drawable) {
                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                    Bitmap bitmap = ((BitmapDrawable) image.getDrawable()).getBitmap();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, quality, byteArrayOutputStream);
                    byte[] array = byteArrayOutputStream.toByteArray();
                    String firstEncoded = encodeData(array);
                    String encodedImage = "0001" + encodeData(array);

                    System.out.println("******************************** the Encoded Image is: "+encodedImage);
                    System.out.println("******************************** the first version of Encoded Image is: "+firstEncoded);

//                    byte[] testbytes = new byte[]{1,2,3};
//                    String test = Base64.getEncoder().encodeToString(testbytes);
//
//                    System.out.println("******************************** JAVA Base64 encodes:[1,2,3]: " + test);

                    Client imageClient = new Client();
                    imageClient.execute(encodedImage);


//                    imageClient.execute(Arrays.toString(array));

//                    ImageClient imageClient = new ImageClient();
//                    imageClient.execute(array);

                    informUser("image sent!");
                } else {
                    informUser("Please add image!");
                }
        }
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
//                System.out.println("Succeed create photoFile!");
            } catch (IOException ex) {
                // Error occurred while creating the File
                ex.printStackTrace();
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                imageURI = FileProvider.getUriForFile(this,
                        "com.example.AmbersAR.fileprovider",
                        photoFile);
                System.out.println("Succeed save photoURI!");
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageURI);
                startActivityForResult(takePictureIntent, REQUEST_CAMERA);
            }
        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        @SuppressLint("SimpleDateFormat") SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss");
        String timeStamp = simpleDateFormat.format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(imageFileName, ".jpg", storageDir);

        // Save a file: path for use with ACTION_VIEW intents
        imagePath = image.getAbsolutePath();
        return image;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (resultCode == RESULT_OK){
            if (requestCode == REQUEST_CAMERA){
                if (intent != null){
                    setPic();
                }
            }
            else if(requestCode == REQUEST_GALLERY){
                if (intent != null){
                    imageURI = intent.getData();
                    image.setImageURI(imageURI);
                }
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private static String encodeData(byte[] byteArray){
        return Base64.getEncoder().encodeToString(byteArray);
    }

    private void informUser(String text){
//        image.setImageDrawable(drawable);
        Toast message = Toast.makeText(ImageActivity.this, text, Toast.LENGTH_SHORT);
        message.setGravity(Gravity.CENTER, 0, 0);
        message.show();
    }

    private void displayCompressImage(int quality) throws IOException {
        ByteArrayOutputStream byteArray = new ByteArrayOutputStream();
        Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageURI);
        bitmap.compress(Bitmap.CompressFormat.JPEG, quality, byteArray);
        Bitmap display = BitmapFactory.decodeStream(new ByteArrayInputStream(byteArray.toByteArray()));

        image.setImageBitmap(display);
    }

    private void setPic() {
        // Get the dimensions of the View
        int targetW = image.getWidth();
        int targetH = image.getHeight();

        // Get the dimensions of the bitmap
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;

        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

        // Determine how much to scale down the image
        int scaleFactor = Math.min(photoW/targetW, photoH/targetH);

        // Decode the image file into a Bitmap sized to fill the View
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;
        bmOptions.inPurgeable = true;

        Bitmap bitmap = BitmapFactory.decodeFile(imagePath, bmOptions);
        image.setImageBitmap(bitmap);
    }

    private void setSeekBar(){
        String message = "image quality: 100% (High)";
        seekBarValue.setText(message);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                quality = seekBar.getProgress();
                String qualityLevel = findQualityLevel(quality);
                String message = "image quality: " + quality + "%" + qualityLevel;
                seekBarValue.setText(message);
                if (image.getDrawable() != initialImage){
                    try {
                        displayCompressImage(quality);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                quality = seekBar.getProgress();
                String qualityLevel = findQualityLevel(quality);
                String message = "image quality: " + quality + "%" + qualityLevel;
                seekBarValue.setText(message);
                if (image.getDrawable() != initialImage){
                    try {
                        displayCompressImage(quality);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                quality = seekBar.getProgress();
                String qualityLevel = findQualityLevel(quality);
                String message = "image quality: " + quality + "%" + qualityLevel;
                seekBarValue.setText(message);
                if (image.getDrawable() != initialImage){
                    try {
                        displayCompressImage(quality);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    private String findQualityLevel(int quality){
        String qualityLevel;
        if (quality >= 65){
            qualityLevel = " (High)";
        } else if (quality >= 30){
            qualityLevel = " (Medium)";
        } else {
            qualityLevel = " (Low)";
        }
        return qualityLevel;
    }
}