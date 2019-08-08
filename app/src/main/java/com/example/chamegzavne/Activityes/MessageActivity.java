package com.example.chamegzavne.Activityes;

import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;

import com.example.chamegzavne.InfoClass.Chat;
import com.example.chamegzavne.Adapters.MessageRecyclerViewAdapter;
import com.example.chamegzavne.InfoClass.IsReaded;
import com.example.chamegzavne.R;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MessageActivity extends AppCompatActivity {
    private static final String TAG="chat";
    private static final String INTENT_MES_KEY="intent.mes.key";

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myPostRef;
    DatabaseReference mychatListRef;
    DatabaseReference isReaded;

    EditText message;
    Button sendMessage;

    Toolbar toolbar;


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

        toolbar=findViewById(R.id.message_toolbar_id);
        setSupportActionBar(toolbar);

        //setActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent=getIntent();
        final String chatID=intent.getStringExtra(INTENT_MES_KEY);


        myPostRef=database.getReference("posts/"+chatID+"/messages");

        mychatListRef=database.getReference("chatLists/"+MainActivity.userID);

        isReaded=database.getReference("isReaded/"+MainActivity.userID);




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
                Chat chat=new Chat(MainActivity.userName,message.getText().toString(),MainActivity.userID,MainActivity.userProfilePhoto.toString(),new Date().toString());
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
                isReaded.push().setValue(new IsReaded(chat.getUserID()+chat.getUserMessage(),MainActivity.userID,chat.getSendTime()));
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == android.R.id.home) {
            Toast.makeText(this,"knopka nazad",Toast.LENGTH_LONG).show();
            finish();
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            finish();
            onBackPressed();
            Toast.makeText(this,"knopka nazad",Toast.LENGTH_LONG).show();
            return true;

        }
        return super.onKeyDown(keyCode, event);
    }




}
