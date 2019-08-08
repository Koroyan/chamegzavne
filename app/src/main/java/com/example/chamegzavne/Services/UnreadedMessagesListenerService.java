package com.example.chamegzavne.Services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.util.Log;

import com.example.chamegzavne.Activityes.MainActivity;
import com.example.chamegzavne.InfoClass.Chat;
import com.example.chamegzavne.InfoClass.ChatList;
import com.example.chamegzavne.InfoClass.IsReaded;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public class UnreadedMessagesListenerService extends Service {
    private IunreadMessagesCount iunreadMessagesCount;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference chatsRef;
    private List<String> ID;
    private Set<IsReaded> isReadedSet;
    private boolean isReadedStatus;

    public void UnreadedMessagesListenerService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        ID=new ArrayList<>();
        isReadedSet=new HashSet<>();
        iunreadMessagesCount= (IunreadMessagesCount) MainActivity.getAppContext();

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        UnreadMessagesAdapter();
        return Service.START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public void UnreadMessagesAdapter(){

        Log.d("unread", "UnreadMessagesAdapter: Service");
        DatabaseReference isReaded = database.getReference("isReaded/" + MainActivity.userID);
        isReaded.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                isReadedSet.add(dataSnapshot.getValue(IsReaded.class));
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
        })   ;


        final DatabaseReference myChatListstRef = database.getReference("chatLists/" + MainActivity.userID);
        myChatListstRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                final ChatList chatList=dataSnapshot.getValue(ChatList.class);
                ID.add(Objects.requireNonNull(chatList).getChatID());

                Log.d("unread", "onChildAdded ChatList: "+chatList.getChatID());

                chatsRef=database.getReference("posts/"+chatList.getChatID()+chatList.getChatListMessage()+"/messages");
                chatsRef.addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                       try {

                           final Chat chat = dataSnapshot.getValue(Chat.class);

                           Log.d("unread", "onChildAdded Chat: " + chat.getUserID() + " " + chat.getUserMessage());
                           isReadedStatus = true;
                           Log.d("unreads", "iiiiiiiiiiiiiiiiiiiiiiiiiiiii " + iunreadMessagesCount);
                           if (iunreadMessagesCount != null) {
                               Log.d("unreads", "isReadedSet Length: " + isReadedSet.size());
                               for (IsReaded isReadedset : isReadedSet) {
                                   if (chat.getUserID().equals(MainActivity.userID)) {
                                       Log.d("unreads", "it my message ");
                                       isReadedStatus = false;
                                       break;
                                   }
                                   if (!(isReadedset.getID() + isReadedset.getMessageTime()).equals(chat.getUserID() + chat.getUserMessage() + chat.getSendTime())) {
                                       Log.d("unreads", "chka: " + isReadedset.getID() + " == " + chat.getUserID() + chat.getUserMessage());

                                   } else {
                                       Log.d("unreads", "ka: " + isReadedset.getID() + " == " + chat.getUserID() + chat.getUserMessage());
                                       isReadedStatus = false;
                                       break;
                                   }

                               }


                               if (isReadedStatus) {
                                   Log.d("unreads", "sarqaceeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeee ");

                                   //iunreadChatList.unreadChatList(chatList);
                                   if (!chat.getUserID().equals(MainActivity.userID)) {
                                       iunreadMessagesCount.unreadMessagesCount();
                                       Log.d("unreads", "onChildAdded: hassssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssss");
                                       chatList.setHasUnread("true");
                                       myChatListstRef.child(chatList.getChatID() + chatList.getChatListMessage()).setValue(chatList);
                                   }
                               }


                           }
                       }
                       catch (Exception e){

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
        Log.d("unread", "ID Count: "+ID.size());

    }

    public interface IunreadMessagesCount {
        void unreadMessagesCount();


    }



}
