package com.example.olivier.businessapp.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.olivier.businessapp.Objects.ChatS;
import com.example.olivier.businessapp.R;

import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ViewHolder> {

    public List<ChatS> cList;
    private Context mContext;

    public ChatAdapter(List<ChatS> uList) {
        this.cList = uList;
    }

    @Override
    public ChatAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_card, parent, false);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // item clicked
            }
        });
        return new ChatAdapter.ViewHolder(view);
    }

    @Override
    public int getItemCount() {
        return cList.size();
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.uName.setText(cList.get(position).getUser2());


    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView uName;
        public TextView uLastM;
        View mView;

        public ViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
            uName = mView.findViewById(R.id.displayname);
            mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Context context = mView.getContext();
                    ////fill out on click
                }
            });


        }
    }

}
