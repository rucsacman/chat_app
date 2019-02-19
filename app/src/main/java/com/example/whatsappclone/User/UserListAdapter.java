package com.example.whatsappclone.User;

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

public class UserListAdapter extends RecyclerView.Adapter<UserListAdapter.UserListViewHolder> {

    ArrayList<UserObject> userList;

    public UserListAdapter(ArrayList<UserObject> userList){
        this.userList  = userList;
    }

    @NonNull
    @Override
    public UserListViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        try{
            View layoutView= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_user,null,false);
            RecyclerView.LayoutParams lp=new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutView.setLayoutParams(lp);

            UserListViewHolder rcv=new UserListViewHolder(layoutView);
            return rcv  ;
        }catch (Exception e){
            Log.e("chatObject", "error", e) ;
            return null;
        }

    }

    @Override
    public void onBindViewHolder(@NonNull UserListViewHolder userListViewHolder, final int i) {
        userListViewHolder.mName.setText(userList.get(i).getName());
        userListViewHolder.mPhone.setText(userList.get(i).getPhone());

        userListViewHolder.mLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String key= FirebaseDatabase.getInstance().getReference().child("chat").push().getKey();
                FirebaseDatabase.getInstance().getReference().child("user").child(FirebaseAuth.getInstance().getUid()).child("chat").child(key).setValue(true);
                FirebaseDatabase.getInstance().getReference().child("user").child(userList.get(i).getUid()).child("chat").child(key).setValue(true);
            }
        });


    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public class UserListViewHolder extends RecyclerView.ViewHolder {

        public TextView mName,mPhone;
        public LinearLayout mLinearLayout;

        public UserListViewHolder (View view){
            super(view);
            try {
                mName=view.findViewById(R.id.cname);
                mPhone=view.findViewById(R.id.phone);
                mLinearLayout.findViewById(R.id.layoutContacts);
            }catch (Exception e){
                Log.e("chatObject", "error", e) ;
            }
        }
    }
}
