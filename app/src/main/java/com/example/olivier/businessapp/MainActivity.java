package com.example.olivier.businessapp;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.olivier.businessapp.Adapters.MessageAdapterRecycler;
import com.example.olivier.businessapp.Objects.Activities.Base;
import com.example.olivier.businessapp.Objects.BaseMessage;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.annotations.Nullable;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
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
    int count=0;
    FirebaseStorage storage;
    private StorageReference storageRef;
    private StorageReference storageReference;

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
        mRecyclerview= (RecyclerView)findViewById(R.id.reyclerview_message_list);
        binfo= new ArrayList<>();
        mLayoutManager = new LinearLayoutManager(getBaseContext());
        MessA= new MessageAdapterRecycler(binfo);
        mRecyclerview.setHasFixedSize(true);
        mRecyclerview.setNestedScrollingEnabled(false);
        editText = (EditText) findViewById(R.id.msg_type);
        mRecyclerview.setLayoutManager(mLayoutManager);
        mRecyclerview.setAdapter(MessA);
        binfo.clear();
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();
        ImageView img = (ImageView)findViewById(R.id.send);
        img.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                send();
            }
        });

        ImageView att = (ImageView)findViewById(R.id.attach);

        att.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                chooseImage();
            }
        });

        if (mFirebaseAuth.getInstance().getCurrentUser() != null) {
            db.collection("Global")
                    .addSnapshotListener(new EventListener<QuerySnapshot>() {
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

        if(checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED )
        {
            return true;
        } else
        {
            return false;
        }

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
        String messageText = editText.getText().toString();

            /*if (editText.getText().toString().trim().equals("")) {
                Toast.makeText(getActivity(), "Please input some text...", Toast.LENGTH_SHORT).show();
            } else {*/
        //if (!messageText.equals("")) {
        //hasfile="f";
        String t= hasfile;
        boolean hf=false;
        if(hasfile.equals("t"))
        {
            uploadImage(0);
            hf=true;
            hasfile="f";
        }
        Long time = System.currentTimeMillis();
        String s = "T" + Objects.toString(time, null);
        String Sender = mFirebaseUser.getUid();
        //BaseMessage b =new BaseMessage(messageText,Sender,file_url,hasfile);
        Map<String, Object> map = new HashMap<>();
        map.put("file_url", file_url);
        map.put("messageText", messageText);
        map.put("sender", Sender);
        map.put("hasfile", hf);
        map.put("displayname",mFirebaseUser.getDisplayName());
        db.collection("Global")
                .add(map)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
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
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    private void uploadImage(int num) {


        if(files[num] != null)
        {
            // final ProgressDialog progressDialog = new ProgressDialog(this.getContext());
            //progressDialog.setTitle("Uploading...");
            // progressDialog.show();
            file_url = UUID.randomUUID().toString();
            StorageReference storageRef = storageReference.child("data/"+file_url);
            storageRef.putFile(files[num])
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            // progressDialog.dismiss();
                            Toast.makeText(getBaseContext(), "Uploaded", Toast.LENGTH_SHORT).show();

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            // progressDialog.dismiss();
                            Toast.makeText(getBaseContext(), "Failed "+e.getMessage(), Toast.LENGTH_SHORT).show();

                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0*taskSnapshot.getBytesTransferred()/taskSnapshot
                                    .getTotalByteCount());
                            //  progressDialog.setMessage("Uploaded "+(int)progress+"%");
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


