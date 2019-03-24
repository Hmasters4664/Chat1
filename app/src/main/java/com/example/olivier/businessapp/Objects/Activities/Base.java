package com.example.olivier.businessapp.Objects.Activities;

import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.olivier.businessapp.Adapters.GlideApp;
import com.example.olivier.businessapp.ChatList;
import com.example.olivier.businessapp.R;
import com.example.olivier.businessapp.Userlist;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.mikhaellopez.circularimageview.CircularImageView;

public abstract class Base extends AppCompatActivity {

    Toolbar toolbar;
    private DrawerLayout mdrawer;
    private FirebaseDatabase database;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;
    private DatabaseReference mDatabase;
    private TextView header;
    private NavigationView navigationView;
    private ActionBar actionbar;
    public FirebaseFirestore db;
    private ImageView profilepic;
    FirebaseStorage storage;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());


            init();

            firbase();

            displayToolbar();
            displayDrawer();
            SetPic();





    }
    void init()
    {

        toolbar = findViewById(R.id.toolbar);

        navigationView = findViewById(R.id.nav_view);
        mdrawer = findViewById(R.id.dre_drawer);
        //mFirebaseUser = mFirebaseAuth.getCurrentUser();
    }

    @Override
    public void onStart(){
        super.onStart();

    }

    void displayToolbar() {

        setSupportActionBar(toolbar);
        header = findViewById(R.id.headertext);


    }

    // Renders the app navigation drawer and the toolbar
    void displayDrawer() {
        //toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);

        actionbar.setHomeAsUpIndicator(R.drawable.ic_menu);

        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        // set item as selected to persist highlight
                        menuItem.setChecked(true);
                        // close drawer when item is tapped
                        mdrawer.closeDrawers();

                        String choice = menuItem.getTitle().toString();

                        switch (choice) {
                            case "Register":
                                Intent i = new Intent(getBaseContext(), SignIn.class);
                                startActivity(i);
                                break;
                            case "Log In":
                                Intent p = new Intent(getBaseContext(), LoginActivity.class);
                                startActivity(p);
                                break;

                            case "Profile Picture":
                                Intent pic = new Intent(getBaseContext(), ImageUpload.class);
                                startActivity(pic);
                                break;

                            case "Users":
                                Intent users = new Intent(getBaseContext(), Userlist.class);
                                startActivity(users);
                                break;

                            case "Chats":
                                Intent chats = new Intent(getBaseContext(), ChatList.class);
                                startActivity(chats);
                                break;
                        }

                        // Add code here to update the UI based on the item selected
                        // For example, swap UI fragments here

                        return true;
                    }
                });
    }

    void firbase()
    {


        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            Intent i = new Intent(getBaseContext(), LoginActivity.class);
            startActivity(i);

        } else{
            mFirebaseAuth = FirebaseAuth.getInstance();
            mFirebaseUser = mFirebaseAuth.getCurrentUser();
            db = FirebaseFirestore.getInstance();
        }



    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mdrawer.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    protected int getLayoutId() {
        return R.layout.activity_base;
    }


    @Override
    protected void onResume() {
        super.onResume();
        String x="here";
        SetPic();
        //do something
    }

    @Override
    protected void onDestroy() {

        super.onDestroy();


}

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }
////////////todo fix the set pic
    void SetPic()
    {
        //db.collection("Users").document(mFirebaseUser.getUid()).get()
        profilepic = (CircularImageView) navigationView.getHeaderView(0).findViewById(R.id.imageView3);
        storage =FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();
        StorageReference imagesRef = storageRef.child("Profile");
        //db.collection("Users").document(mFirebaseUser.getUid())
        StorageReference spaceRef = storageRef.child("Profile/"+mFirebaseUser.getUid()+".jpg");
        GlideApp.with(this)
                .load(spaceRef)
                .into(profilepic);

    }


}


