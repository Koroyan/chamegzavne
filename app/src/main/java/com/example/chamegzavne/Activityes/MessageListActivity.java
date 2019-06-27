package com.example.chamegzavne.Activityes;



import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.chamegzavne.Adapters.MessageListRecyclerViewAdapter;
import com.example.chamegzavne.InfoClass.Chat;
import com.example.chamegzavne.InfoClass.ChatList;
import com.example.chamegzavne.R;
import com.example.chamegzavne.Adapters.MessageListRecyclerViewAdapter;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MessageListActivity extends AppCompatActivity implements MessageListRecyclerViewAdapter.OnChatItemClickListenr {
    private static final String INTENT_NAV_BAR_KEY="intent.navbar.key";
    private static final String INTENT_MES_KEY="intent.mes.key";
    private static final String TAGi="chato";
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myChatListstRef;
    DatabaseReference mypostsRef;
    DatabaseReference mypostmesages;


    BottomNavigationView bottomNavigationView;
    MessageListRecyclerViewAdapter adapter;
    RecyclerView chatRecyclerView;
    List<ChatList> chatLists=new ArrayList<>();




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_list);
        Log.d(TAGi, "onCreate: ");



        myChatListstRef=database.getReference("chatLists/"+MainActivity.userID);
        mypostsRef=database.getReference("posts");
        mypostmesages=database.getReference(MainActivity.userID+"/messages");


        //RecyclerView
        chatRecyclerView=findViewById(R.id.recycler_view_chat_lists);
        chatRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter=new MessageListRecyclerViewAdapter(this,chatLists);
        chatRecyclerView.setAdapter(adapter);
        adapter.setItemClickListener(this);


        //Bottom navigation bar
        bottomNavigationView=findViewById(R.id.nav_bar);
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

                        return true;
                }
                return false;
            }
        });


        myChatListstRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                ChatList chatList=dataSnapshot.getValue(ChatList.class);
                Log.d(TAGi, "onChilstwrite "+chatList.getChatListMessage());
                chatLists.add(chatList);
                adapter.setAdapterList(chatLists);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                ChatList chatList=dataSnapshot.getValue(ChatList.class);
                Log.d(TAGi, "onChilstwrite "+chatList.getChatID());
                chatLists.remove(chatList);
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


    @Override
    public void OnChatItemClick(View view, ChatList chatList) {
        try{
        Intent intent=new Intent(MessageListActivity.this,MessageActivity.class);
        intent.putExtra(INTENT_MES_KEY,chatList.getChatID()+chatList.getChatListMessage());
        startActivity(intent);}
        catch (Exception e){
            Toast.makeText(this,"deleded messagelist____",Toast.LENGTH_SHORT).show();
        }

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
                        mypostsRef.child(MainActivity.userID + chatList.getChatListMessage()).child("messages").setValue(new Chat(null, "chat deleded", null));

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
}
