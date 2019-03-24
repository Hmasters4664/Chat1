package com.example.olivier.businessapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.example.olivier.businessapp.Adapters.ChatAdapter;
import com.example.olivier.businessapp.Objects.Activities.Base;
import com.example.olivier.businessapp.Objects.ChatS;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class ChatList extends Base {
    //Chats/UserID/OneToOne/ChatID

    private static final String TAG = "FireLog";
    private RecyclerView mRecyclerview;
    private ChatAdapter chatA;
    private List<ChatS> Cinfo;
    private RecyclerView.LayoutManager mLayoutManager;
    private FirebaseFirestore mFire;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_chat_list);

        mRecyclerview = findViewById(R.id.reyclerview_chat_list);
        Cinfo = new ArrayList<>();
        chatA = new ChatAdapter(Cinfo);
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();

        mRecyclerview.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerview.setLayoutManager(mLayoutManager);
        mRecyclerview.setAdapter(chatA);
        mFire = FirebaseFirestore.getInstance();
        Cinfo.clear();

        mFire.collection("Chats").document(mFirebaseUser.getUid()).collection("OneToOne").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(QuerySnapshot queryDocumentSnapshots, FirebaseFirestoreException e) {
                if (e != null) {
                    Log.d(TAG, "Error: " + e.getMessage());
                }
                for (DocumentChange doc : queryDocumentSnapshots.getDocumentChanges()) {
                    if (doc.getType() == DocumentChange.Type.ADDED) {
                        ChatS bus = doc.getDocument().toObject(ChatS.class);
                        Cinfo.add(bus);
                        chatA.notifyDataSetChanged();
                    }
                }
            }
        });


    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_chat_list;
    }
}
