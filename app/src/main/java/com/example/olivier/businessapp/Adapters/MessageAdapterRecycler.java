package com.example.olivier.businessapp.Adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Registry;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.module.AppGlideModule;
import com.example.olivier.businessapp.Objects.BaseMessage;
import com.example.olivier.businessapp.Objects.Business;
import com.example.olivier.businessapp.R;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import java.io.InputStream;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class MessageAdapterRecycler extends RecyclerView.Adapter {

    public List<BaseMessage> bList;
    private String mImageFile;
    private int pos;
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageRef;
    StorageReference ImagesRef;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;

    private static final int VIEW_TYPE_MESSAGE_SENT = 1;
    private static final int VIEW_TYPE_MESSAGE_RECEIVED = 2;
    StorageReference islandRef = storage.getReference();
    File Dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
    File docDir = new File(Dir, "TinDocs");
    File image;
     String LOG_TAG="Directory";
    private Context context;


    public MessageAdapterRecycler( List<BaseMessage> bList)
    {
        this.bList=bList;
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();
        if(!docDir.mkdirs())
        {
            Log.e(LOG_TAG, "Directory not created");
        }

       /* Collections.sort(this.bList, new Comparator<BaseMessage>() {
            @Override
            public int compare(BaseMessage z1, BaseMessage z2) {
                if (z1.getTime() > z2.getTime())
                    return 1;
                if (z1.getTime() < z2.getTime())
                    return -1;
                return 0;
            }
        });*/
    }

    @Override
    public int getItemCount() {
        return bList.size();
    }



   /* @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {


        holder.bText.setText(bList.get(position).getDisplayname());
        holder.descText.setText(bList.get(position).getMessageText());
        pos=position;

        if(bList.get(position).getHasfile()) {
            storageRef = storage.getReferenceFromUrl("gs://business-5c307.appspot.com").child(bList.get(position).getPic());
            Bitmap bitmap;

            final long ONE_MEGABYTE = 1024 * 1024;
            storageRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                @Override
                public void onSuccess(byte[] bytes) {
                    // Data for "images/island.jpg" is returns, use this as needed

                    Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                    holder.mImageView.setImageBitmap(bitmap);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // Handle any errors
                }
            });
        }


    }*/
////////////////////////////////////////////////////////////////////////////////////////////////////
    public class  SentMessageHolder  extends RecyclerView.ViewHolder{

    TextView messageText, nameText, dateText;
        ImageView mImageView;
       Context context;
        public SentMessageHolder (View itemView) {
            super(itemView);
           // mView=itemView;
            nameText= (TextView) itemView.findViewById(R.id.text_message_name);
            messageText= (TextView) itemView.findViewById(R.id.text_message_body);
            mImageView =(ImageView)itemView.findViewById(R.id.img);
            dateText = (TextView)itemView.findViewById(R.id.text_message_time);
            context=itemView.getContext();

        }

    void bind(BaseMessage message) {
        messageText.setText(message.getMessageText());
        nameText.setText(message.getDisplayname());
        dateText.setText(message.getDate());
        // Format the stored timestamp into a readable String using method.
        //timeText.setText(Utils.formatDateTime(message.getCreatedAt()));
        ///

        boolean fileexists;
        if(message.getHasfile()) {
            mImageView.setVisibility(View.VISIBLE);
            StorageReference storageRef = storage.getReference();
            StorageReference imagesRef = storageRef.child("data");
            StorageReference spaceRef = storageRef.child("data/"+message.getFile_url());
            GlideApp.with(context)
                    .load(spaceRef)
                    .into(mImageView);


            String Done="here";
        }
        else{
            mImageView.setImageResource(0);
            mImageView.setVisibility(View.GONE);
        }

    }
    }

////////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;

        if (viewType == VIEW_TYPE_MESSAGE_SENT) {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.message_sent, parent, false);
            return new SentMessageHolder(view);
        } else if (viewType == VIEW_TYPE_MESSAGE_RECEIVED) {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.messege_recieved, parent, false);
            return new SentMessageHolder(view);
        }

        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        boolean ownner = bList.get(position).isMyMessage();

        switch (holder.getItemViewType()) {
            case VIEW_TYPE_MESSAGE_SENT:
                ((SentMessageHolder) holder).bind(bList.get(position));
                break;
            case VIEW_TYPE_MESSAGE_RECEIVED:
                ((SentMessageHolder) holder).bind(bList.get(position));
        }
    }

    @Override
    public int getItemViewType(int position) {
        BaseMessage message = (BaseMessage) bList.get(position);

        if (!message.getSender().equals(mFirebaseUser.getUid())) {
            // If the current user is the sender of the message
            return VIEW_TYPE_MESSAGE_SENT;
        } else {
            // If some other user sent the message
            return VIEW_TYPE_MESSAGE_RECEIVED;
        }
    }





}
