package com.example.chamegzavne.Adapters;

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

public class UnreadMessagesAdapter {
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference chatsRef;
    private IunreadMessagesCount iunreadMessagesCount;
    private List<String> ID;
    private Set<IsReaded> isReadedSet;
    private boolean isReadedStatus;
     public UnreadMessagesAdapter(){
         ID=new ArrayList<>();
         isReadedSet=new HashSet<>();

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


         DatabaseReference myChatListstRef = database.getReference("chatLists/" + MainActivity.userID);
         myChatListstRef.addChildEventListener(new ChildEventListener() {
             @Override
             public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                 ChatList chatList=dataSnapshot.getValue(ChatList.class);
                 ID.add(Objects.requireNonNull(chatList).getChatID());

                 Log.d("unread", "onChildAdded ChatList: "+chatList.getChatID());

                 chatsRef=database.getReference("posts/"+chatList.getChatID()+chatList.getChatListMessage()+"/messages");
                 chatsRef.addChildEventListener(new ChildEventListener() {
                     @Override
                     public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                         final Chat chat = dataSnapshot.getValue(Chat.class);
                         Log.d("unread", "onChildAdded Chat: "+chat.getUserID()+" "+chat.getUserMessage());
                         isReadedStatus=true;
                         if(iunreadMessagesCount!=null) {
                             Log.d("unreads", "isReadedSet Length: " + isReadedSet.size());
                             for (IsReaded isReadedset : isReadedSet) {
                                 if(chat.getUserID().equals(MainActivity.userID))  {
                                     Log.d("unreads", "it my message ");
                                     isReadedStatus=false;
                                     break;
                                 }
                                 if (!isReadedset.getID().equals(chat.getUserID() + chat.getUserMessage())) {
                                     Log.d("unreads", "chka: " + isReadedset.getID() + " == " + chat.getUserID() + chat.getUserMessage());

                                 }else{
                                                  Log.d("unreads", "ka: " + isReadedset.getID() + " == " + chat.getUserID() + chat.getUserMessage());
                                   isReadedStatus=false;
                                   break;
                                 }

                             }


                            if(isReadedStatus){iunreadMessagesCount.unreadMessagesCount();}


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

     public void setUnreadMessagesInterface(IunreadMessagesCount iunreadMessagesCount){
         this.iunreadMessagesCount=iunreadMessagesCount;
     }



     public interface IunreadMessagesCount {
         void unreadMessagesCount();

    }
}
