package com.example.whatsappclone;

import android.Manifest;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import com.example.whatsappclone.chat.chatListAdapter;
import com.example.whatsappclone.chat.chatObject;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainPageActivity extends AppCompatActivity {

    private RecyclerView mChatList;
    private  RecyclerView.Adapter mChatListAdapter;
    private  RecyclerView.LayoutManager mChatListLayoutManager;

    ArrayList<chatObject> chatList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);
        try {
            Button mfindUsers=findViewById(R.id.findUser);
            mfindUsers.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(getApplicationContext(),findUsersActivity.class));
                }
            });
            getUserChatList();
            getPermission();

            initializeRecyclerView();

        }catch (Exception e){
            Log.e("chatObject", "error", e) ;
            return;
        }


    }

    private void getUserChatList(){
        DatabaseReference mUserChatDB= FirebaseDatabase.getInstance().getReference().child("user").child(FirebaseAuth.getInstance().getUid()).child("chat");
        mUserChatDB.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    for(DataSnapshot childSnapshot: dataSnapshot.getChildren()){
                        chatObject mChat=new chatObject(childSnapshot.getKey());
                        chatList.add(mChat);
                        mChatListAdapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
    private void getPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M  ) {
            requestPermissions(new String[]{Manifest.permission.WRITE_CONTACTS,Manifest.permission.READ_CONTACTS},1);
        }
    }
    private void initializeRecyclerView() {
        try {
            mChatList =findViewById(R.id.chatList);
            mChatList.setNestedScrollingEnabled(false);
            mChatList.setHasFixedSize(false);
            mChatListLayoutManager =new LinearLayoutManager(getApplicationContext(), LinearLayout.VERTICAL,false);
            mChatList.setLayoutManager( mChatListLayoutManager);
            mChatListAdapter=new chatListAdapter(chatList);
            mChatList.setAdapter(mChatListAdapter);
        }catch (Exception e){
            Log.e("chatObject", "error", e) ;
            return;
        }

    }


}
