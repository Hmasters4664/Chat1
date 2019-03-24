package com.example.olivier.businessapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.example.olivier.businessapp.Adapters.UserAdapter;
import com.example.olivier.businessapp.Objects.Activities.Base;
import com.example.olivier.businessapp.Objects.User;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class Userlist extends Base {

    private RecyclerView mRecyclerview;
    private UserAdapter userA;
    private List<User> Uinfo;
    private RecyclerView.LayoutManager mLayoutManager;
    private FirebaseFirestore mFire;
    private static final String TAG="FireLog";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_userlist);
        mRecyclerview = findViewById(R.id.reyclerview_user_list);
        Uinfo= new ArrayList<>();
        userA= new UserAdapter(Uinfo);

        mRecyclerview.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerview.setLayoutManager(mLayoutManager);
        mRecyclerview.setAdapter(userA);
        mFire= FirebaseFirestore.getInstance();
        Uinfo.clear();



        mFire.collection("Users").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(QuerySnapshot queryDocumentSnapshots, FirebaseFirestoreException e) {
                if(e != null){
                    Log.d(TAG, "Error: "+ e.getMessage());
                }
                for(DocumentChange doc: queryDocumentSnapshots.getDocumentChanges())
                {
                    if(doc.getType()== DocumentChange.Type.ADDED)
                    {
                        User bus = doc.getDocument().toObject(User.class);
                        Uinfo.add(bus);
                        userA.notifyDataSetChanged();
                    }
                }
            }
        });
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_userlist;
    }
}
