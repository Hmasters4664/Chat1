package com.example.olivier.businessapp;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.olivier.businessapp.Adapters.MessageAdapterRecycler;
import com.example.olivier.businessapp.Objects.Activities.Base;
import com.example.olivier.businessapp.Objects.BaseMessage;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.annotations.Nullable;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class MainActivity extends Base {

    private static MainActivity INSTANCE = null;
    private String city="Bus";
    private static final String TAG="FireLog";
    private RecyclerView mRecyclerview;
    private RecyclerView.LayoutManager mLayoutManager;
    private FirebaseAuth mFirebaseAuth;
    private MessageAdapterRecycler MessA;

    private FirebaseUser mFirebaseUser;
    private DatabaseReference mDatabase;
    private EditText editText;
    private List<BaseMessage> binfo;
    private TextView header;
    boolean myMessage = true;
    private String file_url="";
    String hasfile="f";
    Uri[] files = new Uri[4];
    private String filetype;
    int count=0;
    FirebaseStorage storage;
    private StorageReference storageRef;
    private StorageReference storageReference;
    private ImageView img;
    private ImageView att;
    private ImageView pic;


    private Uri filePath;
    private final int PICK_IMAGE_REQUEST = 71;

    //private List<ChatBubble> ChatBubbles;
   // private ArrayAdapter<ChatBubble> adapter;
    private FirebaseDatabase database= FirebaseDatabase.getInstance();
    private ViewPager vp;
    TabLayout tabLayout;
    private static final String[] PERMISSION_STORE = new String[] {"android.permission.WRITE_EXTERNAL_STORAGE"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(Build.VERSION.SDK_INT >=23 && !isPermissionGranted()){
            requestPermissions(PERMISSION_STORE,0);
        }


        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        mRecyclerview = findViewById(R.id.reyclerview_message_list);
        binfo= new ArrayList<>();
        mLayoutManager = new LinearLayoutManager(getBaseContext());
        MessA= new MessageAdapterRecycler(binfo);
        mRecyclerview.setHasFixedSize(true);
        mRecyclerview.setNestedScrollingEnabled(false);
        editText = findViewById(R.id.msg_type);
        mRecyclerview.setLayoutManager(mLayoutManager);
        mRecyclerview.setAdapter(MessA);
        binfo.clear();
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();
        img = findViewById(R.id.send);
        img.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                send();
            }
        });

        att = findViewById(R.id.attach);

        pic = findViewById(R.id.imagesend);

        pic.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                chooseImage();
            }
        });

        att.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseFile();

            }
        });

        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            db.collection("Global").orderBy("time",Query.Direction.ASCENDING)
                   .limit(150).addSnapshotListener(new EventListener<QuerySnapshot>() {
                        @Override
                        public void onEvent(@Nullable QuerySnapshot snapshots,
                                            @Nullable FirebaseFirestoreException e) {
                            if (e != null) {
                                Log.w(TAG, "listen:error", e);
                                return;
                            }

                            for (DocumentChange dc : snapshots.getDocumentChanges()) {
                                if (dc.getType() == DocumentChange.Type.ADDED) {
                                    BaseMessage bus = dc.getDocument().toObject(BaseMessage.class);

                                    if (bus.getSender().equals(mFirebaseUser.getUid())) {
                                        bus.setMyMessage(true);

                                    } else {
                                        bus.setMyMessage(false);
                                    }


                                    binfo.add(bus);

                                    MessA.notifyDataSetChanged();
                                    // MessA.notifyItemInserted(MessA.getItemCount()+1);
                                    mRecyclerview.smoothScrollToPosition(MessA.getItemCount()-1);
                                    editText.setText("");

                                }
                            }

                        }
                    });


        }




    }

    private boolean isPermissionGranted(){

        return checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED;

    }



    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    public static MainActivity getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new MainActivity();
        }
        return(INSTANCE);
    }

    public void send() {
        //Implement image click function

        String t= hasfile;

        if(hasfile.equals("t"))
        {
            uploadImage(0);
            hasfile="f";
        } else{
            plainsend(false);
        }

    }

    public void plainsend(Boolean hf){
        String messageText = editText.getText().toString();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy-hh-mm-ss");
        String format = simpleDateFormat.format(new Date());
        long time= System.currentTimeMillis();
        String Sender = mFirebaseUser.getUid();
        Map<String, Object> map = new HashMap<>();
        map.put("file_url", file_url);
        map.put("messageText", messageText);
        map.put("fileType",filetype);
        map.put("sender", Sender);
        map.put("hasfile", hf);
        map.put("displayname",mFirebaseUser.getDisplayName());
        map.put("date",format);
        map.put("time",time);
        db.collection("Global").document(mFirebaseUser.getUid()+format)
                .set(map)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot successfully written!");
                        file_url = "";
                        filetype = "";
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding document", e);
                    }
                });



    }


    private void chooseImage() {
        Intent intent = new Intent();
        filetype = "IMG";
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Image"), PICK_IMAGE_REQUEST);
    }

    private void chooseFile() {
        Intent intent = new Intent();
        filetype = "PDF";
        intent.setType("application/pdf");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select File"), PICK_IMAGE_REQUEST);
    }

    private void uploadImage(int num) {
        String messageText = editText.getText().toString();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy-hh-mm-ss");
        String format = simpleDateFormat.format(new Date());
        long time= System.currentTimeMillis();

        if(files[num] != null)
        {
            //todo save the file URL after uploading the file
            // final ProgressDialog progressDialog = new ProgressDialog(this.getContext());
            //progressDialog.setTitle("Uploading...");
            // progressDialog.show();
            final String s= UUID.randomUUID().toString();
            if(filetype.equals("PDF"))
            {
                file_url=s+".pdf";
            } else {
                file_url = s;
            }


            final StorageReference storageRef = storageReference.child("data/"+file_url);
            UploadTask uploadTask=storageRef.putFile(files[num]);
                Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        if (!task.isSuccessful()) {
                            throw task.getException();
                        }

                        // Continue with the task to get the download URL
                        return storageRef.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if (task.isSuccessful()) {
                            Uri downloadUri = task.getResult();
                            plainsend(true);

                        } else {
                            // Handle failures
                            // ...
                        }
                    }
                });

            }


    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null )
        {

            files[0] = data.getData();
            hasfile="t";
            //count++;

        }

    }

}


