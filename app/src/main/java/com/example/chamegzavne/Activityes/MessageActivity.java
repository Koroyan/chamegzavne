package com.example.chamegzavne.Activityes;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.chamegzavne.InfoClass.Chat;
import com.example.chamegzavne.Adapters.MessageRecyclerViewAdapter;
import com.example.chamegzavne.R;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class MessageActivity extends AppCompatActivity {
    private static final String TAG="chat";
    private static final String INTENT_MES_KEY="intent.mes.key";

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myPostRef;
    DatabaseReference mychatListRef;

    EditText message;
    Button sendMessage;



    MessageRecyclerViewAdapter adapter;
    List<Chat> chats;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        Log.d(TAG, "onCreate: skizb ");
        chats=new ArrayList<>();
        message=findViewById(R.id.message_edit_text);
        sendMessage=findViewById(R.id.message_send_btn);



        Intent intent=getIntent();
        final String chatID=intent.getStringExtra(INTENT_MES_KEY);


        myPostRef=database.getReference("posts/"+chatID+"/messages");

        mychatListRef=database.getReference("chatLists/"+MainActivity.userID);


        // chatIDs.add(new ChatList(chatID,"my Message"));



        final RecyclerView recyclerView = findViewById(R.id.chat_recycler_view_id);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new MessageRecyclerViewAdapter(this, chats,MainActivity.userID);

        recyclerView.setAdapter(adapter);




        sendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(message.getText().toString().equals("")){
                    Toast.makeText(MessageActivity.this, "please write message!", Toast.LENGTH_SHORT).show();
                    return;
                }
                Chat chat=new Chat(MainActivity.userName,message.getText().toString(),MainActivity.userID);
                message.setText("");
                myPostRef.push().setValue(chat);

            }
        });

        myPostRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Log.d(TAG, "onChildAdded: skizb");
                try{
                Chat chat = dataSnapshot.getValue(Chat.class);
                chats.add(chat);
                adapter.setAdapter(chats);
                recyclerView.smoothScrollToPosition(chats.size());
                Log.d(TAG, "onChildAdded: verj");}
                catch (Exception e){
                    Log.d("chato", "error");
                    Toast.makeText(MessageActivity.this,"messagelist deleded",Toast.LENGTH_SHORT).show();
                    mychatListRef.child(chatID).removeValue();
                    finish();

                }

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
        Log.d(TAG, "onCreate: verj");
    }




}
