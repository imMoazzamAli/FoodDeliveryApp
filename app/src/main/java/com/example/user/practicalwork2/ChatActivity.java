package com.example.user.practicalwork2;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.user.practicalwork2.Adapters.ChatAdapter;
import com.example.user.practicalwork2.Models.ModelChat;
import com.example.user.practicalwork2.Models.ModelUser;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class ChatActivity extends AppCompatActivity {

    EditText etMessage;
    Button btnSend;
    ListView lvChat;

    //ArrayList<String> listChat = new ArrayList<String>();
    ArrayList<ModelChat> listChat = new ArrayList<ModelChat>();
    ArrayAdapter adapter;

    String userName, userEmail = "";
    Boolean status = false;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference dbReferenceUser, dbReferenceMessages;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        actionBar.setCustomView(getLayoutInflater().inflate(R.layout.tool_bar, null),
                new ActionBar.LayoutParams(
                        ActionBar.LayoutParams.WRAP_CONTENT,
                        ActionBar.LayoutParams.MATCH_PARENT,
                        Gravity.CENTER
                )
        );
        actionBar.setDisplayHomeAsUpEnabled(true);

        actionBar.setBackgroundDrawable(getDrawable(R.color.colorPurple));
        getWindow().setStatusBarColor(getColor(R.color.colorPurple));

        firebaseDatabase = FirebaseDatabase.getInstance();
        dbReferenceUser = firebaseDatabase.getReference("USERS");
        dbReferenceMessages = firebaseDatabase.getReference("MESSAGES");

        etMessage = findViewById(R.id.etChatMessage);
        btnSend = findViewById(R.id.btnSend);
        lvChat = findViewById(R.id.lvChat);

        getUserName();
        //getUerEmail();

        //adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, listChat);
        adapter = new ChatAdapter(this, listChat);
        lvChat.setSelection(adapter.getCount() - 1);
        adapter.notifyDataSetChanged();
        lvChat.setAdapter(adapter);

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (TextUtils.isEmpty(etMessage.getText().toString().trim())) {
                    //Toast.makeText(this, "required data", Toast.LENGTH_SHORT).show();
                    etMessage.setError("Field required");

                } else {
                    Map<String, Object> map = new HashMap<String, Object>();

                    String id = dbReferenceMessages.push().getKey();

                    dbReferenceMessages.updateChildren(map);

                    DatabaseReference dbr = dbReferenceMessages.child(id);

                    Map<String, Object> map2 = new HashMap<String, Object>();
                    map2.put("userMsg", etMessage.getText().toString());
                    map2.put("userName", userName);
                    map2.put("userEmail", userEmail);
                    dbr.updateChildren(map2);

                    etMessage.setText("");
                    //showListData();
                    //lvChat.setSelection(adapter.getCount() - 1);
                }
            }
        });

        dbReferenceMessages.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                //updateConversation(dataSnapshot);
                //showListData();

                ModelChat modelChat = dataSnapshot.getValue(ModelChat.class);

                listChat.add(modelChat);
                lvChat.setSelection(adapter.getCount() - 1);
                //adapter.insert(listChat, adapter.getCount());
                adapter.notifyDataSetChanged();

//                adapter = new ChatAdapter(ChatActivity.this, listChat);
//                lvChat.setSelection(adapter.getCount() - 1);
//                adapter.notifyDataSetChanged();
//                lvChat.setAdapter(adapter);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    public void showListData() {
        dbReferenceMessages.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                ModelChat modelChat = null;
                for (DataSnapshot messageSnap : dataSnapshot.getChildren()) {

                    modelChat = messageSnap.getValue(ModelChat.class);
                    listChat.add(modelChat);
                    //adapter.insert(listChat, adapter.getCount());

                }
                //adapter = new ChatAdapter(ChatActivity.this, listChat, modelChat.getUserEmail());
                //adapter.insert(listChat, adapter.getCount());
                //adapter.notifyDataSetChanged();
                //lvChat.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void updateConversation(DataSnapshot dataSnapshot) {

        String msg, user, conversation;
        Iterator i = dataSnapshot.getChildren().iterator();
        while (i.hasNext()) {
            msg = (String) ((DataSnapshot) i.next()).getValue();
            user = (String) ((DataSnapshot) i.next()).getValue();

//            conversation = user + ": " + msg;
//            adapter.insert(conversation, adapter.getCount());

            adapter.notifyDataSetChanged();
        }
    }

    private void getUserName() {
        dbReferenceUser.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot userData : dataSnapshot.getChildren()) {
                    ModelUser modelUser = userData.getValue(ModelUser.class);

                    if (modelUser.getUSER_mEMAIL().equals(FirebaseAuth.getInstance().getCurrentUser().getEmail())) {
                        userName = modelUser.getUSER_NAME();
                        userEmail = modelUser.getUSER_mEMAIL();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }

    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }

}
