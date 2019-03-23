package com.example.olivier.businessapp.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.olivier.businessapp.Objects.User;
import com.example.olivier.businessapp.R;

import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {

    public List<User> uList;
    private Context mContext;


    public UserAdapter( List<User> uList)

    {
        this.uList=uList;

    }



    @Override
    public UserAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.user_list,parent,false);

        view.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                // item clicked
            }
        });
        return new UserAdapter.ViewHolder(view);
    }

    @Override
    public int getItemCount() {
        return uList.size();
    }
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.uName.setText(uList.get(position).getDisplayName());
        holder.uLastM.setText(uList.get(position).getLast());


    }


    public class  ViewHolder extends RecyclerView.ViewHolder{

        View mView;
        public TextView uName;
        public TextView uLastM;
        public ViewHolder(View itemView) {
            super(itemView);
            mView=itemView;
            uName= (TextView) mView.findViewById(R.id.FName);
            uLastM= (TextView) mView.findViewById(R.id.Lname);
            mView.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    Context context = mView.getContext();
                    ////fill out on click
                }
            });


        }
    }
}