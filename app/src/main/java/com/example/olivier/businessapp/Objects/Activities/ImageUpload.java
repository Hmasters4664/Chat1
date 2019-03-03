package com.example.olivier.businessapp.Objects.Activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.content.FileProvider;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.olivier.businessapp.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.mikhaellopez.circularimageview.CircularImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class ImageUpload extends AppCompatActivity {
    static final int REQUEST_IMAGE_CAPTURE = 1;
    static final int REQUEST_IMAGE_EDIT = 2;
    private static final String[] PERMISSION_CAMERA = new String[] {"android.permission.CAMERA"};

    private Button takePic;
    private DrawerLayout mdrawer;
    private Button acceptbutton;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;
    private CircularImageView circularImageView;
    private String mCurrentPhotoPath;
    private StorageReference storageRef;
    private StorageReference ProfilePicRef;
    private StorageReference UserFilesRef;
    private FirebaseStorage storage;
    private String Profilepicture;
    public FirebaseFirestore db;
    private Uri photoURI;
    File Dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
    File docDir = new File(Dir, "TinDocs");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_upload);
         circularImageView = (CircularImageView)findViewById(R.id.circularImageView);
         takePic =(Button)findViewById(R.id.takepic);
        acceptbutton= (Button)findViewById(R.id.accept);
        mdrawer = (DrawerLayout) findViewById(R.id.dre_drawer);
        NavigationView navigationView = findViewById(R.id.nav_view);
        mFirebaseAuth = FirebaseAuth.getInstance();

        if (mFirebaseAuth.getInstance().getCurrentUser() == null) {
            Intent i = new Intent(getBaseContext(), SignIn.class);
            startActivity(i);

        } else{
            mFirebaseUser = mFirebaseAuth.getCurrentUser();
            storage = FirebaseStorage.getInstance();
            storageRef = storage.getReference();
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            Profilepicture =mFirebaseUser.getUid()+"_"+timeStamp+".jpg";
            ProfilePicRef = storageRef.child(Profilepicture);
            UserFilesRef = storageRef.child("Profile"+"/"+Profilepicture);
            db = FirebaseFirestore.getInstance();
        }

        if(Build.VERSION.SDK_INT >=23 && !isPermissionGranted()){
            requestPermissions(PERMISSION_CAMERA,0);
        }




        takePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dispatchTakePictureIntent();
            }
        });

        acceptbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadPic();
            }
        });




    }

    private File createImageFile() throws IOException {
        // Create an image file name
       // String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = mFirebaseUser.getUid();
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }



    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {

            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File

            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                 photoURI = FileProvider.getUriForFile(this,
                        this.getApplicationContext().getPackageName()+".provider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }

        }
    }

    private boolean isPermissionGranted(){

        if(checkSelfPermission(Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED )
        {
            return true;
        } else
        {
            return false;
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            //Bundle extras = data.getExtras();
            //Bitmap imageBitmap = (Bitmap) extras.get("data");
            circularImageView.setImageURI(photoURI);
        }



    }

    private void uploadPic()
    {
        circularImageView.setDrawingCacheEnabled(true);
        circularImageView.buildDrawingCache();

        Bitmap bitmap = ((BitmapDrawable) circularImageView.getDrawable()).getBitmap();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 40, baos);
        byte[] data = baos.toByteArray();

        UploadTask uploadTask = UserFilesRef.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
                Toast.makeText(ImageUpload.this, "Failed", Toast.LENGTH_SHORT).show();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(ImageUpload.this, "Uploaded", Toast.LENGTH_SHORT).show();
                Map<String, Object> map = new HashMap<>();
                map.put("profile", Profilepicture);
                db.collection("Users").document(mFirebaseUser.getUid()).update(map);

            }
        })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                    double progress = (100.0*taskSnapshot.getBytesTransferred()/taskSnapshot
                            .getTotalByteCount());
                    //progressDialog.setMessage("Uploaded "+(int)progress+"%");
                }
        });

    }
}
