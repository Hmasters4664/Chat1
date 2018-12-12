package com.example.olivier.businessapp.Adapters;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.olivier.businessapp.Objects.Activities.BusinessInfo;
import com.example.olivier.businessapp.Objects.Business;
import com.example.olivier.businessapp.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.List;

public class BusinessAdapter extends RecyclerView.Adapter<BusinessAdapter.ViewHolder> {
    public List<Business> bList;
    private int pos;
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageRef;


    public BusinessAdapter( List<Business> bList)
    {
        this.bList=bList;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.business_card,parent,false);

        view.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                // item clicked
            }
        });
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {


        holder.bText.setText(bList.get(position).getName());
        holder.descText.setText(bList.get(position).getDescription());
        pos=position;
        storageRef = storage.getReferenceFromUrl("gs://business-5c307.appspot.com").child(bList.get(position).getPic());
        Bitmap bitmap;

        final long ONE_MEGABYTE = 1024 * 1024;
        storageRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                // Data for "images/island.jpg" is returns, use this as needed

                Bitmap  bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                holder.mImageView.setImageBitmap(bitmap);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
            }
        });




    }

    @Override
    public int getItemCount() {
        return bList.size();
    }

    public class  ViewHolder extends RecyclerView.ViewHolder{

        View mView;
        public TextView bText;
        public TextView descText;
        ImageView mImageView;
        public ViewHolder(View itemView) {
            super(itemView);
            mView=itemView;
            bText= (TextView) mView.findViewById(R.id.textView6);
            descText= (TextView) mView.findViewById(R.id.textView7);
            mImageView =(ImageView)itemView.findViewById(R.id.imageView);

           /* mView.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    int g=getAdapterPosition();
                    final String p=bList.get(g).getPic();
                    final String p2=bList.get(g).getPic2();
                    final String p3=bList.get(g).getPic3();
                    final String p4=bList.get(g).getPic4();
                    final String id=bList.get(g).getId();
                    Context context = mView.getContext();
                    Intent i = new Intent(context,BusinessInfo.class);
                    i.putExtra("pic",p);
                    i.putExtra("pic2",p2);
                    i.putExtra("pic3",p3);
                    i.putExtra("pic4",p4);
                    i.putExtra("id",id);
                    context.startActivity(i);
                }
            });*/


        }
    }
}
