 package com.example.whatsappclone;

import android.content.Context;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.LinearLayout;

import com.example.whatsappclone.R;
import com.example.whatsappclone.User.UserListAdapter;
import com.example.whatsappclone.User.UserObject;
import com.example.whatsappclone.Utils.CountryToPhonePrefix;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

 public class findUsersActivity extends AppCompatActivity {
    private RecyclerView mUserList;
    private  RecyclerView.Adapter muserListAdapter;
    private  RecyclerView.LayoutManager mUserListLayoutManager;

    String countryISOprefix;

    ArrayList<UserObject> userList,contactList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_users);

        contactList= new ArrayList<>();
        userList= new ArrayList<>();
        countryISOprefix=getCountryISO();
        initializeRecyclerView();
        getContactList();

    }
    private  void  getContactList(){
        try {
            Cursor phones = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                    null, null, null, null);
            while (phones.moveToNext()) {
                String name = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                String phoneNumber = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                phoneNumber.replace(" ","");
                phoneNumber.replace("-","");
                phoneNumber.replace(")","");
                phoneNumber.replace("(","");

                if(!String.valueOf(phoneNumber.charAt(0)).equals("+")){

                    phoneNumber=countryISOprefix+phoneNumber.substring(1);
                    Log.d("ISO",phoneNumber);
                }



                UserObject mContact = new UserObject(name, phoneNumber,"");
                contactList.add(mContact);
                getUserDetails(mContact);
            }
        }
        catch (Exception e){
                Log.e("contactPermission", "error", e) ;
                return;
            }


        }

     private void getUserDetails(UserObject mContact) {
         DatabaseReference mUserDB= FirebaseDatabase.getInstance().getReference().child("user");
         Query query=mUserDB.orderByChild("phone").equalTo(mContact.getPhone());
         query.addListenerForSingleValueEvent(new ValueEventListener() {
             @Override
             public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                 if(dataSnapshot.exists()){
                     String phone="",
                             name="";

                    for (DataSnapshot childsnapShot:dataSnapshot.getChildren()){
                        if(childsnapShot.child("phone").getValue() != null){
                            phone= childsnapShot.child("phone").getValue().toString();
                        }
                        if(childsnapShot.child("name").getValue() != null){
                            name= childsnapShot.child("name").getValue().toString();
                        }
                        UserObject mUser=new UserObject(childsnapShot.getKey(),name,phone);
                        if(name.equals(phone)){
                            for (UserObject mContactIterator :contactList){
                                if(mContactIterator.getPhone().equals(mUser.getPhone())){
                                    mUser.setName(mContactIterator.getName());
                                }

                            }

                        }


                        userList.add(mUser);
                        muserListAdapter.notifyDataSetChanged();
                        return;

                    }
                 }
             }

             @Override
             public void onCancelled(@NonNull DatabaseError databaseError) {

             }
         });
     }

     private String getCountryISO(){
        String iso  =null;
        try {
            TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
            Log.d("telepohony",telephonyManager.getNetworkCountryIso().toString());
//            String imei = telephonyManager.getDeviceId();
            if(telephonyManager.getNetworkCountryIso()!= null ){
                if(!telephonyManager.getNetworkCountryIso().equals("") ){
                    iso=telephonyManager.getNetworkCountryIso();
                }
            }
        } catch (Exception e){
            Log.e("errorCountry","msg",e);
            iso="LK";
        }

        return CountryToPhonePrefix.getPhone(iso);
    }
    private void initializeRecyclerView() {

        try {


        mUserList =findViewById(R.id.userList);
        mUserList.setNestedScrollingEnabled(false);
        mUserList.setHasFixedSize(false);
        mUserListLayoutManager =new LinearLayoutManager(getApplicationContext(), LinearLayout.VERTICAL,false);
        mUserList.setLayoutManager( mUserListLayoutManager);
        muserListAdapter=new UserListAdapter(userList);
        mUserList.setAdapter(muserListAdapter);
        }catch (Exception e){
            Log.e("List","msg",e);
        }
     }
 }
