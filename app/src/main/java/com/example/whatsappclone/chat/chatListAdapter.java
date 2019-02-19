package com.example.whatsappclone.chat;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.whatsappclone.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class chatListAdapter extends RecyclerView.Adapter<chatListAdapter.chatListViewHolder> {
    ArrayList<chatObject> chatList;

    public chatListAdapter(ArrayList<chatObject> chatList){
        this.chatList  = chatList;
    }

    @NonNull
    @Override
    public chatListViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View layoutView= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_chat,null,false);
        RecyclerView.LayoutParams lp=new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutView.setLayoutParams(lp);

        chatListViewHolder rcv=new chatListViewHolder(layoutView);
        return rcv  ;
    }

    @Override
    public void onBindViewHolder(@NonNull chatListViewHolder chatListViewHolder, final int i) {
        chatListViewHolder.mTitle.setText(chatList.get(i).getChatID());
        chatListViewHolder.mLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });


    }

    @Override
    public int getItemCount() {
        try {
            return chatList.size();
        }catch (Exception e){
            Log.e("chatObject", "error", e) ;
            return 0;
        }
//        return 0;

    }

    public class chatListViewHolder extends RecyclerView.ViewHolder {
        public TextView mTitle;
        public LinearLayout mLinearLayout;

        public chatListViewHolder (View view){
            super(view);
            mTitle=view.findViewById(R.id.mtitle);
            mLinearLayout.findViewById(R.id.layoutChat);
        }
    }
}
