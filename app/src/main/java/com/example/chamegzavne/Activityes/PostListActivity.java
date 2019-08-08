package com.example.chamegzavne.Activityes;

import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.chamegzavne.InfoClass.ChatList;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.Switch;
import android.widget.Toast;

import com.example.chamegzavne.Adapters.PostListRecyclerViewAdapter;
import com.example.chamegzavne.InfoClass.Post;
import com.example.chamegzavne.R;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.example.chamegzavne.Activityes.MainActivity.userName;

public class PostListActivity extends AppCompatActivity implements PostListRecyclerViewAdapter.onPostItemClickListener {
    private static final String INTENT_CHAT_KEY = "intent.chat.key";
    private static final String INTENT_MES_KEY="intent.mes.key";

    PostListRecyclerViewAdapter adapter;
    List<Post> posts;
    Task<Uri> postImage=null;

    FirebaseStorage storage;
    StorageReference storageReference;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myChatListstRef;
    DatabaseReference myPostRef = database.getReference("posts");
    BottomNavigationView bottomNavigationViewListMap;
    BottomNavigationView bottomNavigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_list);



        myChatListstRef=database.getReference("chatLists/"+MainActivity.userID);
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        StorageReference ref = storageReference.child("images/grigory");
        File localFile = null;
        try {
            localFile = File.createTempFile("images", "jpg");
        } catch (IOException e) {
            e.printStackTrace();
        }
        localFile.toURI().toString();




        bottomNavigationViewListMap = findViewById(R.id.nav_bar);
        bottomNavigationViewListMap.setItemIconTintList(null);
        bottomNavigationViewListMap.getMenu().findItem(R.id.navigation_messages).setChecked(true);
        bottomNavigationViewListMap.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                Toast.makeText(PostListActivity.this, "" + menuItem.getMenuInfo(), Toast.LENGTH_SHORT).show();
                switch (menuItem.getItemId()) {
                    case R.id.navigation_map:
                        Intent intent = new Intent(PostListActivity.this, MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        startActivity(intent);
                        menuItem.setChecked(true);
                        return true;
                }
                return false;
            }
        });

       /* bottomNavigationView = findViewById(R.id.nav_bar);
        bottomNavigationView.setItemIconTintList(null);
        bottomNavigationView.getMenu().findItem(R.id.navigation_map).setChecked(true);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                Toast.makeText(PostListActivity.this, "" + menuItem.getMenuInfo(), Toast.LENGTH_SHORT).show();
                switch (menuItem.getItemId()) {
                    case R.id.navigation_messages:
                        Intent intent = new Intent(PostListActivity.this, MessageListActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        startActivity(intent);
                        menuItem.setChecked(true);

                        return true;
                }
                return false;
            }
        });/*/



        posts=new ArrayList<>();

        final RecyclerView recyclerView = findViewById(R.id.post_list_recycler_view_id);
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemViewCacheSize(20);
        recyclerView.setDrawingCacheEnabled(true);
        recyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new PostListRecyclerViewAdapter(this, posts);

        adapter.setPostclickListener(this);

        recyclerView.setAdapter(adapter);

       firebasePostListener();

    }

    @Override
    public void onPostClick(View v, Post post) {
        switch(v.getId()) {
            case R.id.post_accept_btn:

                Intent intent=new Intent(PostListActivity.this,MessageActivity.class);
                myChatListstRef.child(post.getpUserID()+post.getpTitle()).setValue(new ChatList(post.getpName(),post.getpTitle(),post.getpUserID(),post.getpPhotoURL()));

                intent.putExtra(INTENT_MES_KEY,post.getpUserID()+post.getpTitle());
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
                finish();

                break;
            case R.id.constraint_id:

//                Intent intent1 = new Intent(PostListActivity.this, DetailPostsActivity.class);
//                ArrayList<String> puting = new ArrayList<>();
//
//                puting.add(MainActivity.userID.toString()); //[0] id for chat
//                puting.add(userName);                             //[1]username for client
//                puting.add(post.getpName());                      //[2]username server
//                puting.add(post.getpTitle());                     //[3]title
//                puting.add(post.getpComment());                   //[4]comment
//                puting.add(post.getpGel());                       //[5]gel
//                puting.add(post.getpUserID() + post.getpTitle().toString());             //[6]Post user ID for post
//                puting.add(post.getpUserID());                                    //[7]post user id
//                puting.add(post.getpPhotoURL());                  //[8] post photoURI
//                puting.add(post.getpUserPhotoURL());              //[9]user photoURI
//                puting.add(String.valueOf(post.getpLatitude()));                  //[10] latidute
//                puting.add(String.valueOf(post.getpLongitude()));                 //[11] longitude
//                //intent.putExtra(INTENT_CHAT_KEY,userID+marker.getId().toString());
//                intent1.putStringArrayListExtra(INTENT_CHAT_KEY, puting);
//                intent1.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
//                startActivity(intent1);

                break;
            default:
                Intent intent2 = new Intent(PostListActivity.this, ImagePinchZoomActivity.class);
                ArrayList<String> puting1 = new ArrayList<>();


                puting1.add(MainActivity.userID.toString()); //[0] id for chat
                puting1.add(userName);                             //[1]username for client
                puting1.add(post.getpName());                      //[2]username server
                puting1.add(post.getpTitle());                     //[3]title
                puting1.add(post.getpComment());                   //[4]comment
                puting1.add(post.getpGel());                       //[5]gel
                puting1.add(post.getpUserID() + post.getpTitle().toString());             //[6]Post user ID for post
                puting1.add(post.getpUserID());                      //[7]post user id
                puting1.add(post.getpPhotoURL());                    //[8]post photoURI
                puting1.add(post.getpUserPhotoURL());                //[9]userPhotoURI
                //intent.putExtra(INTENT_CHAT_KEY,userID+marker.getId().toString());

                intent2.putStringArrayListExtra(INTENT_CHAT_KEY, puting1);
                intent2.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent2);
                break;

        }
    }

    //FIREBASE POST LISTENER
    void firebasePostListener(){
        myPostRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Post post=dataSnapshot.getValue(Post.class);
                assert post != null;
                if (post.getpUserID() == null) return;
                posts.add(post);
                adapter.setAdapter(posts);
                // recyclerView.smoothScrollToPosition(0);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                Post post=dataSnapshot.getValue(Post.class);
                posts.remove(post);
                adapter.setAdapter(posts);

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
