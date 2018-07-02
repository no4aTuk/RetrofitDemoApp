package com.example.apple.retrofitdemoapp;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.apple.retrofitdemoapp.Enums.FileCategory;
import com.example.apple.retrofitdemoapp.Helpers.FileHelper;
import com.example.apple.retrofitdemoapp.Models.ErrorResult;
import com.example.apple.retrofitdemoapp.Models.Token;
import com.example.apple.retrofitdemoapp.Models.UserPermissions;
import com.example.apple.retrofitdemoapp.Retrofit.CompleteCallbacks.OnFileRequestComplete;
import com.example.apple.retrofitdemoapp.Retrofit.CompleteCallbacks.OnRequestComplete;
import com.example.apple.retrofitdemoapp.Retrofit.Configuration.CredentialsStorage;
import com.example.apple.retrofitdemoapp.Retrofit.DispatchGroup;
import com.example.apple.retrofitdemoapp.Retrofit.Services.AuthService.AuthService;
import com.example.apple.retrofitdemoapp.Retrofit.Services.FileService.FileService;

import java.io.File;
import java.io.IOException;

import okhttp3.ResponseBody;

public class MainActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;

    private Button button;
    private ImageView imageView;
    private ProgressBar progressBar;
    private ProgressBar progressBar2;
    private ProgressBar progressBar3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.imageView = findViewById(R.id.imageView);
        this.button = findViewById(R.id.button);
        this.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //openGallery();
                GroupRequest();
            }
        });
        this.progressBar = findViewById(R.id.progressBar);
        this.progressBar2 = findViewById(R.id.progressBar2);
        this.progressBar3 = findViewById(R.id.progressBar3);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults)
    {
        switch (requestCode) {
            case PICK_IMAGE_REQUEST:
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Intent galleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(galleryIntent, PICK_IMAGE_REQUEST);
                } else {
                    //do something like displaying a message that he didn`t allow the app to access gallery and you wont be able to let him select from gallery
                }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {

            Uri uri = data.getData();
            //File file = new File(getImagePath(uri));
            File file = FileHelper.generateFile(MainActivity.this, uri);
            if (file != null) {
                // add it into UI
                uploadFile(file);
            }

//            try {
//                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
//                // Log.d(TAG, String.valueOf(bitmap));
//
//                this.imageView.setImageBitmap(bitmap);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
        }
    }

    private String getImagePath(Uri uri) {
        String[] projection = { MediaStore.Images.Media.DATA };

        Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
        cursor.moveToFirst();

        int columnIndex = cursor.getColumnIndex(projection[0]);
        String picturePath = cursor.getString(columnIndex); // returns null
        cursor.close();

        return picturePath;
    }

    private void GroupRequest() {
        final DispatchGroup group = new DispatchGroup();


        group.enter();
        AuthService.userPermissions(new OnRequestComplete<UserPermissions>() {
            @Override
            public void onSuccess(UserPermissions result) {

                Log.d("DISPATCH", "REQUESTS 1 COMPLETE: ");
                group.leave();
            }

            @Override
            public void onFail(ErrorResult error) {

            }
        });

        group.enter();
        AuthService.userPermissions(new OnRequestComplete<UserPermissions>() {
            @Override
            public void onSuccess(UserPermissions result) {
                Log.d("DISPATCH", "REQUESTS 2 COMPLETE: ");
                group.leave();
            }

            @Override
            public void onFail(ErrorResult error) {

            }
        });

        group.enter();
        AuthService.userPermissions(new OnRequestComplete<UserPermissions>() {
            @Override
            public void onSuccess(UserPermissions result) {
                Log.d("DISPATCH", "REQUESTS 3 COMPLETE: ");
                group.leave();
            }

            @Override
            public void onFail(ErrorResult error) {

            }
        });

        group.notify(new Runnable() {
            @Override
            public void run() {

                try {
                    Thread.sleep(200);
                    Log.d("DISPATCH", "ALL REQUESTS COMPLETE: ");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
    }


    private void openGallery() {

        try {
            if (ActivityCompat.checkSelfPermission(MainActivity.this,
                    Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE}, PICK_IMAGE_REQUEST);
            } else {
                //Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                //startActivityForResult(galleryIntent, PICK_IMAGE_REQUEST);

                Intent intent = new Intent();
                // Show only images, no videos or anything else
                intent.setType("video/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                // Always show the chooser (if there are multiple options available)
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void uploadFile(File file) {

        String mimeType = FileHelper.getMimeTypeByExtension(MainActivity.this, file);
        FileCategory category = FileHelper.getFileCategory(mimeType);

        FileService.uploadFileWithProgress(MainActivity.this, file, category.toString(), new OnFileRequestComplete<ResponseBody>() {
            @Override
            public void onProgress(int percents) {
                Log.d("UPLOAD", "onSuccess: " + percents);
                progressBar.setProgress(percents);
            }

            @Override
            public void onSuccess(ResponseBody result) {

                progressBar.setProgress(100);
            }

            @Override
            public void onFail(ErrorResult error) {
                Toast.makeText(MainActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getToken() {
        AuthService.token("+79085111864", "Qwerty123", new OnRequestComplete<Token>() {
            @Override
            public void onSuccess(Token result) {
                int a = 0;
            }

            @Override
            public void onFail(ErrorResult error) {
                Toast.makeText(MainActivity.this, error.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void downloadFiles() {


        //String file1Id = "a80cd6da-e21e-494a-85a2-d2459a7d076c";
        //String fileId2 = "acd4f7e8-bf95-471d-875c-2efa21261159";
        String file1Id = "3f436350-6f3a-4223-98fd-03590a9236a4";
        String file2Id = "ef961c71-97e6-488f-be6d-ad3be535b810";
        String file3Id = "f15ddd42-59a8-4440-a136-d23a7a7d03d1";
        //Video1 "3f436350-6f3a-4223-98fd-03590a9236a4";
        //Video2 "ef961c71-97e6-488f-be6d-ad3be535b810";
        //Video3 "f15ddd42-59a8-4440-a136-d23a7a7d03d1";
        //String fileExt = "jpg";
        String fileExt = "mp4";
        FileService.downloadFileWithProgress(MainActivity.this, file1Id, fileExt, null, new OnFileRequestComplete<File>() {
            @Override
            public void onProgress(int percents) {
                //Log.d("INTERCEPTOR", "onProgress1: " + percents);
                progressBar.setProgress(percents);
                CredentialsStorage.getInstance().setToken("asdsd");
            }

            @Override
            public void onSuccess(File result) {
                Log.d("INTERCEPTOR", "onProgress1: complete");
                int a = 0;
                progressBar.setProgress(100);
            }

            @Override
            public void onFail(ErrorResult error) {
                Toast.makeText(MainActivity.this, error.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}
