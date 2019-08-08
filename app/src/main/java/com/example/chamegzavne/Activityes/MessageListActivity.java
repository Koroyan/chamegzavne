package com.example.chamegzavne.Activityes;



import android.content.Context;
import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;

import com.example.chamegzavne.Adapters.MessageListRecyclerViewAdapter;
import com.example.chamegzavne.InfoClass.Chat;
import com.example.chamegzavne.InfoClass.ChatList;
import com.example.chamegzavne.R;
import com.example.chamegzavne.Services.UnreadedMessagesListenerService;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class MessageListActivity extends AppCompatActivity implements MessageListRecyclerViewAdapter.OnChatItemClickListenr  {
    private static final String INTENT_MES_KEY="intent.mes.key";
    private static final String TAGi="chato";
    private static Context context;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myChatListstRef;
    DatabaseReference mypostsRef;
    DatabaseReference mypostmesages;


    MessageListRecyclerViewAdapter adapter;
    RecyclerView chatRecyclerView;
    List<ChatList> chatLists=new ArrayList<>();




    Toolbar toolbar;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_list);
        Log.d(TAGi, "onCreate: ");

        context=MessageListActivity.this;

        myChatListstRef=database.getReference("chatLists/"+MainActivity.userID);
        mypostsRef=database.getReference("posts");
        mypostmesages=database.getReference(MainActivity.userID+"/messages");


        toolbar=findViewById(R.id.messages_list_toolbar_id);
        setSupportActionBar(toolbar);

        //setActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
       // getSupportActionBar().setHomeButtonEnabled(true);




        //RecyclerView
        chatRecyclerView=findViewById(R.id.recycler_view_chat_lists);
        chatRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter=new MessageListRecyclerViewAdapter(this,chatLists);
        chatRecyclerView.setAdapter(adapter);
        adapter.setItemClickListener(this);
        firebaseListener();


        startService(new Intent(MessageListActivity.this,UnreadedMessagesListenerService.class));
        //Bottom navigation bar
      /*  bottomNavigationView=findViewById(R.id.nav_bar);
        bottomNavigationView.setItemIconTintList(null);
        bottomNavigationView.getMenu().findItem(R.id.navigation_messages).setChecked(true);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                Intent intent;
                switch(menuItem.getItemId()){
                    case R.id.navigation_map:

                        intent=new Intent(MessageListActivity.this, MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        startActivity(intent);
                        menuItem.setChecked(true);
                        return true;
                }
                return false;
            }
        });*/



    }


    @Override
    public void OnChatItemClick(View view, ChatList chatList) {
        ChatList chatList1=chatList;
        chatList1.setHasUnread("false");
        try{
            myChatListstRef.child(chatList.getChatID()+chatList.getChatListMessage()).setValue(chatList1);
        Intent intent=new Intent(MessageListActivity.this,MessageActivity.class);
        intent.putExtra(INTENT_MES_KEY,chatList.getChatID()+chatList.getChatListMessage());
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);}
        catch (Exception e){
            Toast.makeText(this,"deleded messagelist____",Toast.LENGTH_SHORT).show();
        }

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
            onBackPressed();
            Toast.makeText(this,"knopka nazad",Toast.LENGTH_LONG).show();
            return true;

        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void OnchatLongclick(View view, final ChatList chatList) {

        PopupMenu popupMenu=new PopupMenu(MessageListActivity.this,view);
        popupMenu.getMenuInflater().inflate(R.menu.post_settings_menu,popupMenu.getMenu());
        Toast.makeText(MessageListActivity.this,"popup menu created",Toast.LENGTH_SHORT).show();
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                switch(menuItem.getItemId()) {
                    case R.id.menu_item_delete:
                    if (chatList.getChatID().equals(MainActivity.userID)) {
                        Log.d(TAGi, "its my list____________ ");

                        myChatListstRef.child(chatList.getChatID() + chatList.getChatListMessage()).removeValue();
                        mypostsRef.child(chatList.getChatID() + chatList.getChatListMessage()).removeValue();
                        mypostmesages.removeValue();
                        mypostsRef.child(chatList.getChatID() + chatList.getChatListMessage()).removeValue();
                        chatLists.remove(chatList);
                        mypostsRef.child(MainActivity.userID + chatList.getChatListMessage()).child("messages").setValue(new Chat(null, "chat deleded", null,null,new Date().toString()));

                    } else {
                        Log.d(TAGi, "its other list_____________");
                        Toast.makeText(MessageListActivity.this
                                , "" + chatList.getChatID().toString() + chatList.getChatListMessage().toString()
                                , Toast.LENGTH_SHORT).show();
                        myChatListstRef.child(chatList.getChatID().toString() + chatList.getChatListMessage().toString()).removeValue();
                        chatLists.remove(chatList);

                    }
                    break;
                    default:
                        break;
                }

                adapter.setAdapterList(chatLists);
                return true;

            }
        });
        popupMenu.setGravity(Gravity.CENTER);
        popupMenu.show();

    }
    public static Context getAppContext() {
        return MessageListActivity.context;
    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    void firebaseListener(){
     myChatListstRef.addChildEventListener(new ChildEventListener() {
         @Override
         public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
             ChatList chatList=dataSnapshot.getValue(ChatList.class);
             Log.d(TAGi, "onChilstwrite "+chatList.getChatListMessage());
             chatLists.add(chatList);
             Collections.sort(chatLists, new Comparator<ChatList>() {
                 @Override
                 public int compare(ChatList o1, ChatList o2) {
                     return o2.getHasUnread().compareTo(o1.getHasUnread());
                 }
             });
             adapter.setAdapterList(chatLists);
         }

         @Override
         public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
             ChatList chatList=dataSnapshot.getValue(ChatList.class);
             Log.d(TAGi, "onChilstwrite "+chatList.getChatListMessage());
             Collections.sort(chatLists, new Comparator<ChatList>() {
                 @Override
                 public int compare(ChatList o1, ChatList o2) {
                     return o2.getHasUnread().compareTo(o1.getHasUnread());
                 }
             });
             adapter.setAdapterList(chatLists);
         }

         @Override
         public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
             ChatList chatList=dataSnapshot.getValue(ChatList.class);
             Log.d(TAGi, "onChiltdeleded "+chatList.getChatID());
             chatLists.remove(chatList);
             adapter.setAdapterList(chatLists);
             adapter.setAdapterList(chatLists);
         }

         @Override
         public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

         }

         @Override
         public void onCancelled(@NonNull DatabaseError databaseError) {
             Log.d(TAGi, "onCancelled: ");
         }
     });

 }


}
