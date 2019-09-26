package com.example.chamegzavne.Activityes;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.chamegzavne.InfoClass.ChatList;
import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.LocationCallback;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.location.Address;
import android.location.Geocoder;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.MotionEvent;
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
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Delayed;

import okio.Timeout;

import static com.example.chamegzavne.Activityes.MainActivity.mylocation;
import static com.example.chamegzavne.Activityes.MainActivity.userName;

public class PostListActivity extends AppCompatActivity implements PostListRecyclerViewAdapter.onPostItemClickListener, PostListRecyclerViewAdapter.onImageTouchListener,PostListRecyclerViewAdapter.OnProfileImageClickListener{
    private static final String INTENT_CHAT_KEY = "intent.chat.key";
    private static final String INTENT_MES_KEY="intent.mes.key";

    PostListRecyclerViewAdapter adapter;
    List<Post> posts;
    Task<Uri> postImage=null;

    LatLng myLocation;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_list);


//myPostRef=FirebaseDatabase.getInstance().getReference("posts");

        myLocationInit();
        FirebaseStorage storage;
        StorageReference storageReference;



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
        adapter.setImageTouchListener(this);
        adapter.setOnProfileImageClickListener(this);
        recyclerView.setAdapter(adapter);

      //  firebasePostListener();

        bottomNavigationViewInit();
    }

    @Override
    protected void onResume(){
        super.onResume();
        myLocationInit();
        firebasePostListener();

    }
    @Override
    public void onPostClick(View v, Post post) {
        switch(v.getId()) {
            case R.id.post_accept_btn:

                DatabaseReference myChatListstRef;
                myChatListstRef=FirebaseDatabase.getInstance().getReference("chatLists/"+MainActivity.userID);


                Intent intent=new Intent(PostListActivity.this,MessageActivity.class);
                myChatListstRef.child(post.getpUserID()+post.getpTitle()).setValue(new ChatList(post.getpName(),post.getpTitle(),post.getpUserID(),post.getpPhotoURL(),post.getpUserPhotoURL()));

                intent.putExtra(INTENT_MES_KEY,post.getpUserID()+post.getpTitle());
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
                finish();

                break;


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


            case R.id.post_list_image:
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


        DatabaseReference myPostRef=FirebaseDatabase.getInstance().getReference("posts");
        myPostRef.keepSynced(true);
        myPostRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Post post=dataSnapshot.getValue(Post.class);
                assert post != null;
                if (post.getpUserID() == null) return;
                if(!conectionStatus()){
                    Toast.makeText(PostListActivity.this,"გთხოვთ ჩაართეთ ინტერნეტი",Toast.LENGTH_LONG).show();
                    //return;
                }
                if(myLocation!=null) {
                    try{
                        if (isEqualAdresses(
                                new LatLng(myLocation.latitude, myLocation.longitude)
                                , new LatLng(post.getpLatitude(), post.getpLongitude()))) {
                            Log.d("imerkir", "havasar en");
                            posts.add(post);
                            adapter.setAdapter(posts);
                        }
                    }catch (Exception e){
                        Toast.makeText(PostListActivity.this,"გთხოვთ ჩაართეთ ინტერნეტი",Toast.LENGTH_LONG).show();
                        firebasePostListener();
                    }
                }
                    else {
                        posts.add(post);
                        adapter.setAdapter(posts);
                    }


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

    @Override
    public void onImageTouch(View v, Post post, MotionEvent event) {
        Log.d("touchimage", "onImageTouch: ");

    }


    @Override
    public void onProfileImageClick(View v, Post post) {
        startActivity(new Intent(PostListActivity.this,UserProfileActivity.class).putExtra(MainActivity.INTENT_PROFILE_KEY,post.getpUserID()));
    }
    private void myLocationInit(){

            DatabaseReference ref = FirebaseDatabase.getInstance().getReference("locations/geofire");
            ref.keepSynced(true);
            GeoFire geoFire = new GeoFire(ref);
            geoFire.getLocation(MainActivity.userID, new LocationCallback() {
                @Override
                public void onLocationResult(String key, GeoLocation location) {
                    try {
                        myLocation = new LatLng(location.latitude, location.longitude);
                    }
                    catch (Exception e){
                        Toast.makeText(PostListActivity.this,"გთხოვთ ჩაართეთ GPS-ი",Toast.LENGTH_SHORT).show();
                        new Timer().schedule(new TimerTask() {
                            @Override
                            public void run() {
                              //  startActivity(new Intent(PostListActivity.this,MainActivity.class));
                            }
                        },500);


                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });


    }

    private boolean isEqualAdresses(LatLng mylocation,LatLng postLocation){
        Log.d("locoloto", "ml"+mylocation.latitude+" ml"+mylocation.longitude+"   pl"+postLocation.latitude+" pl"+postLocation.longitude);

        try{
            Log.d("locoloto", "ml"+mylocation.latitude+" pl"+postLocation.latitude+"   "+getAddress(mylocation.latitude,mylocation.longitude).get(0));
            return getAddress(mylocation.latitude,mylocation.longitude).get(0).getCountryName()
.equals(getAddress(postLocation.latitude,postLocation.longitude).get(0).getCountryName());}
        catch (Exception e){
            Log.d("locoloto", "Error "+e);
            return false;
        }
    }
    private List<Address> getAddress(double latitude, double longitude){
        Geocoder geocoder;
        List<Address> addresses = null;
        geocoder = new Geocoder(this, Locale.getDefault());

        try {
            addresses = geocoder.getFromLocation(latitude, longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
        } catch (IOException e) {
            e.printStackTrace();
        }


        // String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
        // String city = addresses.get(0).getLocality();
        // String state = addresses.get(0).getAdminArea();
        // String country = addresses.get(0).getCountryName();
        // String postalCode = addresses.get(0).getPostalCode();
        // String knownName = addresses.get(0).getFeatureName(); // Only if available else return NULL
        return addresses;
    }
    private void bottomNavigationViewInit(){
        BottomNavigationView bottomNavigationViewListMap;
        bottomNavigationViewListMap = findViewById(R.id.nav_bar);
        bottomNavigationViewListMap.setItemIconTintList(null);
        bottomNavigationViewListMap.getMenu().findItem(R.id.navigation_messages).setChecked(true);
        bottomNavigationViewListMap.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.navigation_map:
                        Intent intent = new Intent(PostListActivity.this, MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        startActivity(intent);
                        menuItem.setChecked(true);
                        return true;
                    case R.id.navigation_profile:
                        Intent intent1=new Intent(PostListActivity.this,UserProfileActivity.class);
                        intent1.putExtra(MainActivity.INTENT_PROFILE_KEY,MainActivity.userID);
                        intent1.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        startActivity(intent1);
                        menuItem.setChecked(true);

                        return true;
                }
                return false;
            }
        });
    }
   boolean conectionStatus(){
        ConnectivityManager cm = (ConnectivityManager)getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo nInfo = cm.getActiveNetworkInfo();
        return nInfo != null && nInfo.isAvailable() && nInfo.isConnected();
    }
    private void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Your GPS seems to be disabled, do you want to enable it?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }
}
