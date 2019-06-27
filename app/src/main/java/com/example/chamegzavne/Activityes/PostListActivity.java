package com.example.chamegzavne.Activityes;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.chamegzavne.Adapters.MessageRecyclerViewAdapter;
import com.example.chamegzavne.Adapters.PostListRecyclerViewAdapter;
import com.example.chamegzavne.InfoClass.Post;
import com.example.chamegzavne.R;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

import static com.example.chamegzavne.Activityes.MainActivity.userName;

public class PostListActivity extends AppCompatActivity implements PostListRecyclerViewAdapter.onPostItemClickListener {
    private static final String INTENT_CHAT_KEY = "intent.chat.key";

    PostListRecyclerViewAdapter adapter;
    List<Post> posts;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myPostRef = database.getReference("posts");
    BottomNavigationView bottomNavigationViewListMap;
    BottomNavigationView bottomNavigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_list);


        bottomNavigationViewListMap = findViewById(R.id.list_map_nav_bar);

        bottomNavigationViewListMap.getMenu().findItem(R.id.navigation_list_map).setChecked(true);
        bottomNavigationViewListMap.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                Toast.makeText(PostListActivity.this, "" + menuItem.getMenuInfo(), Toast.LENGTH_SHORT).show();
                switch (menuItem.getItemId()) {
                    case R.id.navigation_map:
                        Intent intent = new Intent(PostListActivity.this, MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        startActivity(intent);

                        return true;
                }
                return false;
            }
        });

        bottomNavigationView = findViewById(R.id.nav_bar);

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

                        return true;
                }
                return false;
            }
        });

        posts=new ArrayList<>();

        final RecyclerView recyclerView = findViewById(R.id.post_list_recycler_view_id);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new PostListRecyclerViewAdapter(this, posts);

        adapter.setPostclickListener(this);

        recyclerView.setAdapter(adapter);

       firebasePostListener();

    }

    @Override
    public void onPostClick(View v, Post post) {
        Intent intent=new Intent(PostListActivity.this, DetailPostsActivity.class);
        ArrayList<String> puting = new ArrayList<>();
        puting.add(MainActivity.userID.toString()); //[0] id for chat
        puting.add(userName);                             //[1]username for client
        puting.add(post.getpName());                      //[2]username server
        puting.add(post.getpTitle());                     //[3]title
        puting.add(post.getpComment());                   //[4]comment
        puting.add(post.getpGel());                       //[5]gel
        puting.add(post.getpUserID() + post.getpTitle().toString());             //[6]Post user ID for post
        puting.add(post.getpUserID());                                         //[7]post user id

        //intent.putExtra(INTENT_CHAT_KEY,userID+marker.getId().toString());
        intent.putStringArrayListExtra(INTENT_CHAT_KEY, puting);
        startActivity(intent);
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
}
