package com.depobangunan.depomessenger;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.depobangunan.depomessenger.Adapter.MessageGroupAdapter;
import com.depobangunan.depomessenger.Model.ChatGroup;
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
import java.util.List;

public class GroupActivity extends AppCompatActivity {

    String currentGroupName, currentUserId, currentUserName;
    FirebaseAuth firebaseAuth;
    DatabaseReference userRef, groupRef, groupMessageKeyRef;
    EditText et_message;
    ImageButton btnSend;

    RecyclerView recyclerView;

    List<ChatGroup> cGroup;
    MessageGroupAdapter mGA;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group);

        currentGroupName = getIntent().getStringExtra("groupName").toString();
        //Toast.makeText(getApplicationContext(),currentGroupName,Toast.LENGTH_LONG).show();
        et_message = findViewById(R.id.et_message);
        btnSend = findViewById(R.id.btnSend);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(currentGroupName);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //finish();
                startActivity(new Intent(GroupActivity.this,HomeActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
            }
        });

        recyclerView = findViewById(R.id.recylce_view);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);

        firebaseAuth = FirebaseAuth.getInstance();
        currentUserId = firebaseAuth.getCurrentUser().getUid();
        userRef = FirebaseDatabase.getInstance().getReference().child("Users");
        groupRef = FirebaseDatabase.getInstance().getReference().child("Groups").child(currentGroupName);

        userRef.child(currentUserId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    currentUserName = dataSnapshot.child("username").getValue().toString();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage();
                et_message.setText("");
            }
        });



    }

    private void sendMessage() {
        String message = et_message.getText().toString();
        String messageKey = groupRef.push().getKey();
        if (TextUtils.isEmpty(message)){
            Toast.makeText(getApplicationContext(),"Please Write", Toast.LENGTH_LONG).show();
        }else {
            HashMap<String,Object> groupMessageKey = new HashMap<>();
            groupRef.updateChildren(groupMessageKey);

            groupMessageKeyRef = groupRef.child(messageKey);

            HashMap<String,Object> messageInfo = new HashMap<>();
            messageInfo.put("username", currentUserName);
            messageInfo.put("message", message);
            messageInfo.put("sender", currentUserId);
            groupMessageKeyRef.updateChildren(messageInfo);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        groupRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                if (dataSnapshot.exists()){
                    readMessage(dataSnapshot);
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                if (dataSnapshot.exists()){
                    readMessage(dataSnapshot);
                }
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

    private void readMessage(DataSnapshot dataSnapshot) {
        cGroup = new ArrayList<>();

        Iterator iterator = dataSnapshot.getChildren().iterator();
        cGroup.clear();
        while (iterator.hasNext()){
            String message = (String) ((DataSnapshot)iterator.next()).getValue();
            String sender = (String) ((DataSnapshot)iterator.next()).getValue();
            String usrnm = (String) ((DataSnapshot)iterator.next()).getValue();

            ChatGroup chatGroup = new ChatGroup(sender, message,usrnm);
            cGroup.add(chatGroup);
        }
        mGA = new MessageGroupAdapter(GroupActivity.this, cGroup);
        recyclerView.setAdapter(mGA);
    }
}
