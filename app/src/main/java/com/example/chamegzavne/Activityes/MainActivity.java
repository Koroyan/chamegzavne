package com.example.chamegzavne.Activityes;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.chamegzavne.Adapters.UnreadMessagesAdapter;
import com.example.chamegzavne.InfoClass.ChatList;
import com.example.chamegzavne.InfoClass.Post;
import com.example.chamegzavne.InfoClass.User;
import com.example.chamegzavne.R;
import com.example.chamegzavne.Services.UnreadedMessagesListenerService;
import com.example.chamegzavne.push_notification.MySingleton;
import com.facebook.login.LoginManager;
import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

//test Admob Manifest  ca-app-pub-3940256099942544~3347511713
//autounid  ca-app-pub-3940256099942544/6300978111

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback, View.OnClickListener, UnreadedMessagesListenerService.IunreadMessagesCount {
    private static final String TAG = "mylocation";
    private static final String adMobTAG="adMobTag";
    //Intent Keys
    private static final String INTENT_INFO_KEY = "intent_info_key";
    private static final String INTENT_CHAT_KEY = "intent.chat.key";
    public static final String INTENT_HAS_INFO_KEY ="intent_has_info_key";
    public static final String INTENT_PROFILE_KEY="intent.profile.key";



    //push_Notification keys
    final private String FCM_API = "https://fcm.googleapis.com/fcm/send";
    final private String serverKey = "key=" + "AAAAflzg4vs:APA91bHpZOVe8GfSb-WoStbXaO3wbC_J8R8Ogumy2KBCt2flI1v8Xidpbg5ybg5wg6vHSPb3fiSB1h4QPhz8LGSyBSypTKF4vZn8fs5-UtfKDcU620B8V-m6yElCpPfPnbF06_WdCbDp";
    final private String contentType = "application/json";


    //context
    private static Context context;


    String NOTIFICATION_TITLE;
    String NOTIFICATION_MESSAGE;
    String TOPIC;

    //messages
    public static int COUNT_UNREAD_MESSAGES;

    //Firebase initialize.....




    FirebaseStorage storage;
    StorageReference storageReference;


    //Map for mapMarker
    private final Map<String, MarkerOptions> mMarkers = new ConcurrentHashMap<String, MarkerOptions>();
    private Map<String, Post> markerpost = new HashMap<>();

    //Location initialize.....
    private LocationManager locationManager;
    private static final int MINIMUM_TIME = 1000 * 0;
    private static final int MINIMUM_DISTANCE = 10;

    //myLocation
    public static Location mylocation;

    //USER
    private static List<User> users = new ArrayList<>();
    User user;
    public static String userName=null;
    public static String userID=null;
    public static Uri userProfilePhoto=null;
    static boolean isHasProfileInfo=false;
    // DatabaseReference myChatID=database.getReference(userID);
    //post
    Post post;
    private String pGel;
    private String pTitle;
    private String pComment;

    //Buttons
    FloatingActionButton add_marker;
    Button mes_btn;
    private static TextView unread_messages;
    TextView tollbartitle;


    String key = null;

    static FirebaseAuth firebaseAuth;
    private GoogleMap mMap;

    public Context mContext = (Context) getBaseContext();
    //
    public static boolean NOTIFICATION_SHOW_STATUS = true;


    BottomNavigationView bottomNavigationView;
    BottomNavigationView bottomNavigationViewListMap;
    Toolbar toolbar;

    Uri postImage;


    //unreadmessages
    UnreadMessagesAdapter unreadMessagesAdapter;


    AdView mAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //startActivity(new Intent(MainActivity.this,MapDirectionActivity.class))
        try {
            FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        }
        catch (Exception e){
            Log.d("firebaseexception", "Error : "+e);
        }

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();




        context=MainActivity.this;

        MobileAds.initialize(this,
                "ca-app-pub-1537892667165291~9892742328");

        mAdView = findViewById(R.id.adView);

        AdRequest adRequest = new AdRequest.Builder().addTestDevice("9D90BF50426EBE7A00CB247ED854F2DE").build();
        mAdView.loadAd(adRequest);
        mAdView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                Log.d(adMobTAG, "onAdLoaded: ");
                // Code to be executed when an ad finishes loading.
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                Log.d(adMobTAG, "onAdFailedToLoad: "+errorCode);
                Toast.makeText(MainActivity.this, "adMob error: "+errorCode, Toast.LENGTH_SHORT).show();
                // Code to be executed when an ad request fails.
            }

            @Override
            public void onAdOpened() {
                Log.d(adMobTAG, "onAdOpened: ");
                // Code to be executed when an ad opens an overlay that
                // covers the screen.
            }

            @Override
            public void onAdClicked() {
                Log.d(adMobTAG, "onAdClicked: ");
                // Code to be executed when the user clicks on an ad.
            }

            @Override
            public void onAdLeftApplication() {
                Log.d(adMobTAG, "onAdLeftApplication: ");
                // Code to be executed when the user has left the app.
            }

            @Override
            public void onAdClosed() {
                Log.d(adMobTAG, "onAdClosed: ");
                // Code to be executed when the user is about to return
                // to the app after tapping on an ad.
            }
        });

// TODO: Add adView to your view hierarchy.


      //  unreadMessagesAdapter=new UnreadMessagesAdapter();
      //  unreadMessagesAdapter.setUnreadMessagesInterface(this);

        toolbar=findViewById(R.id.main_toolbar_id);
        setSupportActionBar(toolbar);




        add_marker = findViewById(R.id.add_marker);
        mes_btn=findViewById(R.id.messages_btn_id);
        unread_messages=findViewById(R.id.unread_messages);
        firebaseAuth = FirebaseAuth.getInstance();
        add_marker.setOnClickListener(this);
        mes_btn.setOnClickListener(this);






        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        assert mapFragment != null;
        mapFragment.getMapAsync(this);
        Log.d(TAG, "onCreate: verj");

    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        Log.d(TAG, "onMapReady: skizb");
        mMap = googleMap;
        mMap.isMyLocationEnabled();
        mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
            @Override
            public View getInfoWindow(Marker marker) {
                return null;
            }

            @Override
            public View getInfoContents(final Marker marker) {
                Post post=markerpost.get(marker.getId());
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View v =  inflater.inflate(R.layout.map_info_window,null,false);
                TextView info =  v.findViewById(R.id.map_window_info);
                ImageView image=v.findViewById(R.id.map_window_profile_image);
                // TextView tvLng = (TextView) v.findViewById(R.id.tv_lng);
                info.setText(post.getpTitle());
               Picasso.get().load(post.getpUserPhotoURL()).into(image);
                return v;
            }
        });
        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                Intent intent = new Intent(MainActivity.this, DetailPostsActivity.class);
                ArrayList<String> puting = new ArrayList<>();
                Post post = markerpost.get(marker.getId());
                assert post != null;

                Log.d("mypost", "onInfoWindowClick: " + post.getpUserID() + " : " + userID);

                puting.add(userID.toString()); //[0] id for chat
                puting.add(userName);                             //[1]username for client
                puting.add(post.getpName());                      //[2]username server
                puting.add(post.getpTitle());                     //[3]title
                puting.add(post.getpComment());                   //[4]comment
                puting.add(post.getpGel());                       //[5]gel
                puting.add(post.getpUserID() + post.getpTitle().toString());             //[6]Post user ID for post
                puting.add(post.getpUserID());                                    //[7]post user id
                puting.add(post.getpPhotoURL());                  //[8] post photoURI
                puting.add(post.getpUserPhotoURL());              //[9]user photoURI
                puting.add(String.valueOf(post.getpLatitude()));                  //[10] latidute
                puting.add(String.valueOf(post.getpLongitude()));                 //[11] longitude
                //intent.putExtra(INTENT_CHAT_KEY,userID+marker.getId().toString());
                intent.putStringArrayListExtra(INTENT_CHAT_KEY, puting);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);

            }
        });

        Log.d(TAG, "onMapReady: verj");
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.add_marker:
                Log.d(TAG, "onClick: button");
                statusCheck();
                if (mylocation == null) {
                    Toast.makeText(this, "GPS status is null", Toast.LENGTH_SHORT).show();
                    return;
                }

                Intent intent = new Intent(MainActivity.this, AddPostsActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivityForResult(intent, 100);
                return;
            case R.id.messages_btn_id:
                COUNT_UNREAD_MESSAGES=0;
                Intent intent1=new Intent(MainActivity.this,MessageListActivity.class);
                intent1.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent1);
                return;



        }

        return;
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d("resultactivity", "onActivityResult: resultcode" + resultCode);
        if (RESULT_CANCELED == resultCode) {
            return;
        }
        if (requestCode == 100) {
            final String[] result = data.getStringArrayExtra(INTENT_INFO_KEY);
            int imagesCount=Integer.parseInt(result[0]);
            // ArrayList<String> result = data.getStringArrayListExtra(INTENT_INFO_KEY);
            // Log.d("putextra", "onActivityResult:" + result.get(0) + result.get(1) + result.get(2));
            Log.d("username", "onActivityResult: " + userName);

            storage = FirebaseStorage.getInstance();
            storageReference = storage.getReference();
            final String[] imageURL = new String[1];
            StorageReference ref = storageReference.child("images/"+
                    userID +
                    result[1]+0);
            Log.d("photoURI", "URI useridtitle: "+userID+result[1]);

            ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    // Got the download URL for 'users/me/profile.png'
                    // Pass it to Picasso to download, show in ImageView and caching
                    Log.d("photoURI", "Uri: " + uri.toString());
                    imageURL[0] = uri.toString();

                    DatabaseReference myPostRef = FirebaseDatabase.getInstance().getReference("posts");

                    post = new Post(userName, result[0], result[1], result[2], userProfilePhoto.toString(), imageURL[0], userID, mylocation.getLatitude(), mylocation.getLongitude());
                    myPostRef.child(userID + result[1]).setValue(post);

                    TOPIC = "messages"; //topic has to match what the receiver subscribed to
                    NOTIFICATION_TITLE = post.getpTitle();
                    NOTIFICATION_MESSAGE = post.getpComment();


                    DatabaseReference addChatList;
                    addChatList=FirebaseDatabase.getInstance().getReference("chatLists/"+MainActivity.userID);
                    addChatList.child(MainActivity.userID+result[1]).setValue(new ChatList(MainActivity.userName,result[1].toString(),MainActivity.userID,imageURL[0],userProfilePhoto.toString()));
                    JSONObject notification = new JSONObject();
                    JSONObject notifcationBody = new JSONObject();
                    try {
                        notifcationBody.put("title", NOTIFICATION_TITLE);
                        notifcationBody.put("message", NOTIFICATION_MESSAGE);
                        notifcationBody.put("ID", userID);


                        notification.put("to", FirebaseInstanceId.getInstance().getToken());
                        notification.put("data", notifcationBody);

                    } catch (JSONException e) {
                        Log.e(TAG, "onCreate: " + e.getMessage());
                    }
                    sendNotification(notification);

                }



            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    //no image
                    Log.d("photoURI", "onFailure: "+exception.getMessage());
                    DatabaseReference myPostRef = FirebaseDatabase.getInstance().getReference("posts");

                    post = new Post(userName, result[0], result[1], result[2], userProfilePhoto.toString(), null, userID, mylocation.getLatitude(), mylocation.getLongitude());
                    myPostRef.child(userID + result[1]).setValue(post);

                    TOPIC = "messages"; //topic has to match what the receiver subscribed to
                    NOTIFICATION_TITLE = post.getpTitle();
                    NOTIFICATION_MESSAGE = post.getpComment();


                    DatabaseReference addChatList;
                    addChatList=FirebaseDatabase.getInstance().getReference("chatLists/"+MainActivity.userID);
                    addChatList.child(MainActivity.userID+result[1]).setValue(new ChatList(MainActivity.userName,result[1].toString(),MainActivity.userID,imageURL[0],userProfilePhoto.toString()));
                    JSONObject notification = new JSONObject();
                    JSONObject notifcationBody = new JSONObject();
                    try {
                        notifcationBody.put("title", NOTIFICATION_TITLE);
                        notifcationBody.put("message", NOTIFICATION_MESSAGE);
                        notifcationBody.put("ID", userID);


                        notification.put("to", FirebaseInstanceId.getInstance().getToken());
                        notification.put("data", notifcationBody);

                    } catch (JSONException e) {
                        Log.e(TAG, "onCreate: " + e.getMessage());
                    }
                    sendNotification(notification);
                }
            });

          //  post = new Post(userName, result[0], result[1], result[2],userProfilePhoto.toString(), imageURL[0], userID, mylocation.getLatitude(), mylocation.getLongitude());
          //  myPostRef.child(userID + result[1]).setValue(post);




        } else {

            Toast.makeText(this, "error", Toast.LENGTH_SHORT).show();
        }


    }


    @Override
    public void onStart() {
        super.onStart();
        COUNT_UNREAD_MESSAGES=0;
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user==null){
            Intent intent=new Intent(MainActivity.this,LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivity(intent);
            finish();
            return;
        }
        Log.d("startinggg", "onStart: ");
        startService(new Intent(MainActivity.this,UnreadedMessagesListenerService.class));
        // find the Facebook profile and get the user's id
        try {
            for (UserInfo profile : user.getProviderData()) {

                // check if the provider id matches "facebook.com"
                if (FacebookAuthProvider.PROVIDER_ID.equals(profile.getProviderId())) {
                    userID = profile.getUid();
                    userName = profile.getDisplayName();
                    if (profile.getPhotoUrl() != null) {
                        userProfilePhoto = profile.getPhotoUrl();
                    } else {
                        userProfilePhoto = null;
                    }
                }
            }

                DatabaseReference users = FirebaseDatabase.getInstance().getReference("users");
            users.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot) {
                    if (!snapshot.hasChild(userID+"/"+userName)) {
                        startActivity(new Intent(MainActivity.this, AddUserInfoActivity.class).putExtra(INTENT_HAS_INFO_KEY,false));
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });



        }

        catch(Exception e){
            Log.d("mylocation", "onStart: Error"+e);
            Toast.makeText(MainActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
        }
        Log.d(TAG, "onStart: verj");
        }






    //locationManagerinit
    public void locationInitializer() {
        Log.d(TAG, "locationInitializer: skizb");
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        Log.d(TAG, "locationInitializer: verj");
    }

    public void firebaseListener() {
        Log.d(TAG, "firebaseListener: skizb");

        DatabaseReference myPostRef=FirebaseDatabase.getInstance().getReference("posts");
        myPostRef.keepSynced(true);
        myPostRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Log.d("my", "onChildAdded: ");
                Post post = dataSnapshot.getValue(Post.class);


                if (post.getpUserID() == null) return;

                try {
                    String key = add(post.getpName() + " : " + post.getpTitle(), new LatLng(post.getpLatitude(), post.getpLongitude()), Uri.parse(post.getpUserPhotoURL()));
                    markerpost.put(key, post);
                }
                catch (Exception e){
                    Log.d("mylocation", "onChildAdded: Error"+e);
                    Toast.makeText(MainActivity.this,"onChildAdded Erroe: "+e,Toast.LENGTH_LONG).show();

                }


                if ((post.getpUserID().equals(userID))) {
                    //  showNotification(MainActivity.this,post.getpTitle(),post.getpComment(),new Intent(MainActivity.this,MainActivity.class   ));
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                remove(dataSnapshot.getValue(Post.class).getpName() + " : " +
                        dataSnapshot.getValue(Post.class).getpTitle());
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                Log.d("fireerror", "onCancelled: "+databaseError);
            }
        });
        Log.d(TAG, "firebaseListener: verj");

    }

    public void setMyLocation(Location location) {
        Log.d(TAG, "setMyLocation: skizb");
        if(location!=null) {
            mylocation = location;
            DatabaseReference ref = FirebaseDatabase.getInstance().getReference("locations/geofire" );
            GeoFire geoFire = new GeoFire(ref);
            geoFire.setLocation(userID, new GeoLocation(location.getLatitude(), location.getLongitude()), new GeoFire.CompletionListener() {
                @Override
                public void onComplete(String key, DatabaseError error) {
                    Log.d("geoerror", "error: "+error);
                }
            });
        }
        Log.d(TAG, "setMyLocation: verj");

    }


    //add Marker method
    private String add(final String name, final LatLng ll,Uri photoUri){
        final String[] markerID = new String[1];
        Log.d(TAG, "add: skizb");
        Log.d("photo", "add: " + userProfilePhoto.toString());




         Target target = new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                Log.d("my", "onBitmapLoaded: ");
                final MarkerOptions marker = new MarkerOptions().position(ll).title(name).icon(BitmapDescriptorFactory.fromBitmap(getCircledBitmap(bitmap)));
                mMarkers.put(name, marker);
                mMap.moveCamera(CameraUpdateFactory.newLatLng(ll));
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        markerID[0] = mMap.addMarker(marker).getId();



                    }
                });
            }

            @Override
            public void onBitmapFailed(Exception e, Drawable errorDrawable) {
                Log.d("my", "onBitmapFailed: "+e);
            }


            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {
            }
        };



        Picasso.get().load(photoUri).into(target);
      //  if(markerID[0]==null)
       //     add(name, ll, photoUri);
        Log.d(TAG, "add: verj");
        return markerID[0];

    }


    //remove Marker method
    private void remove(String name) {
        Log.d(TAG, "remove: skizb");
        mMarkers.remove(name);

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mMap.clear();

                for (MarkerOptions item : mMarkers.values()) {
                    mMap.addMarker(item);
                }
            }
        });
        Log.d(TAG, "remove: verj");
    }

    @Override
    protected void onStop() {
        super.onStop();
       // COUNT_UNREAD_MESSAGES=0;
        stopService(new Intent(MainActivity.this,UnreadedMessagesListenerService.class));
    }

    @Override
    protected void onResume() {
      super.onResume();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user==null){
            Intent intent=new Intent(MainActivity.this,LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivity(intent);
            finish();
        }

        if(false)
            startActivity(new Intent(MainActivity.this,AddUserInfoActivity.class));

        firebaseListener();
        locationInitializer();
        bottomNavigationInit();

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Log.d(TAG, "onResume: manifest error");
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
            return;
        }

        locationManager.requestLocationUpdates(
                LocationManager.GPS_PROVIDER, MINIMUM_TIME, MINIMUM_DISTANCE,
                locationListener);

        locationManager.requestLocationUpdates(
                LocationManager.NETWORK_PROVIDER, MINIMUM_TIME, MINIMUM_DISTANCE,
                locationListener);
        Log.d(TAG, "onResume: verj");
    }

    @Override
    protected void onPause() {
        Log.d(TAG, "onPause: skizb");
        super.onPause();
        locationManager.removeUpdates(locationListener);
        COUNT_UNREAD_MESSAGES=0;
        Log.d(TAG, "onPause: verj");
    }

    //LocationListener :)
    private LocationListener locationListener = new LocationListener() {


        @Override
        public void onLocationChanged(Location location) {
            Log.d(TAG, "onLocationChanged: skizb");
            if (location == null) {
                Toast.makeText(MainActivity.this, "location is null", Toast.LENGTH_SHORT).show();
                return;
            }
            setMyLocation(location);
            Log.d(TAG, "onLocationChanged: verj");
        }

        @Override
        public void onProviderDisabled(String provider) {
            Log.d(TAG, "onProviderDisabled: ");
            Toast.makeText(MainActivity.this, "provider disabled", Toast.LENGTH_SHORT).show();
            if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1);

                return;
            }
            setMyLocation(locationManager.getLastKnownLocation(provider));
        }

        @Override
        public void onProviderEnabled(String provider) {
            Log.d(TAG, "onProviderEnabled: skizb");
            if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
                return;
            }
            if (locationManager.getLastKnownLocation(provider) == null) {
                return;
            }
            setMyLocation(locationManager.getLastKnownLocation(provider));

            Log.d(TAG, "onProviderEnabled: verj");
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            Log.d(TAG, "onStatusChanged: ");

            Toast.makeText(MainActivity.this, "StatusChanged:" + status, Toast.LENGTH_SHORT).show();

        }
    };


    //show notification method
    public void showNotification(Context context, String title, String body, Intent intent) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        int notificationId = (int) System.currentTimeMillis();
        String channelId = "channel-01";
        String channelName = "Channel Name";
        int importance = 0;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            importance = NotificationManager.IMPORTANCE_HIGH;
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel mChannel = new NotificationChannel(
                    channelId, channelName, importance);
            notificationManager.createNotificationChannel(mChannel);
        }

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context, channelId)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(title)
                .setContentText(body)
                .setAutoCancel(true);

        TaskStackBuilder stackBuilder = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            stackBuilder = TaskStackBuilder.create(context);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            stackBuilder.addNextIntent(intent);
        }
        PendingIntent resultPendingIntent = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN) {
            resultPendingIntent = stackBuilder.getPendingIntent(
                    0,
                    PendingIntent.FLAG_UPDATE_CURRENT
            );
        }
        mBuilder.setContentIntent(resultPendingIntent);

        notificationManager.notify(notificationId, mBuilder.build());
    }

    //send push-Notification method
    private void sendNotification(JSONObject notification) {


        // RequestQueue requestQueue = Volley.newRequestQueue(this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, FCM_API, notification,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {


                        Log.i(TAG, "onResponse: " + response);


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MainActivity.this, "Request error", Toast.LENGTH_LONG).show();
                        Log.i(TAG, "onErrorResponse: Didn't work");
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> params = new HashMap<>();
                params.put("Authorization", serverKey);
                params.put("Content-Type", contentType);
                return params;
            }

        };
        MySingleton.getInstance(getApplicationContext()).addToRequestQueue(jsonObjectRequest);
        //requestQueue.add(jsonObjectRequest);

    }

       //getCircleBitmap
    public static Bitmap getCircledBitmap(Bitmap bitmap) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        canvas.drawCircle(bitmap.getWidth()/2f, bitmap.getHeight()/2f, bitmap.getWidth()/2f, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);

        return output;
    }


    @Override
    public void unreadMessagesCount() {

        COUNT_UNREAD_MESSAGES++;
        String count=COUNT_UNREAD_MESSAGES+"";
        unread_messages.setText("  ");
        Log.d("unreadedd", "unreadMessagesCount: "+COUNT_UNREAD_MESSAGES);
        Toast.makeText(this,""+COUNT_UNREAD_MESSAGES,Toast.LENGTH_LONG).show();
    }



    public static Context getAppContext() {
        return MainActivity.context;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            Toast.makeText(this,"knopka nazad",Toast.LENGTH_LONG).show();
            onBackPressed();
            moveTaskToBack(true);
            android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(0);

        }
        return super.onKeyDown(keyCode, event);
    }
    private boolean isCircleContains(Circle circle, LatLng point) {
        double r = circle.getRadius();
        LatLng center = circle.getCenter();
        double cX = center.latitude;
        double cY = center.longitude;
        double pX = point.latitude;
        double pY = point.longitude;

        float[] results = new float[1];

        Location.distanceBetween(cX, cY, pX, pY, results);

        if(results[0] < r) {
            Log.d("containsloc", "yes");
            return true;
        } else {
            Log.d("containsloc", "no");
            return false;
        }
    }




    //location checcked
    public void statusCheck() {
        final LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            buildAlertMessageNoGps();

        }
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

    public static void signOut(){
        firebaseAuth.signOut();;
    }
    private void bottomNavigationInit(){
        bottomNavigationView = findViewById(R.id.nav_bar);
        bottomNavigationView.setItemIconTintList(null);
        bottomNavigationView.getMenu().findItem(R.id.navigation_map).setChecked(true);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                switch (menuItem.getItemId()) {
                    case R.id.navigation_messages:
                        Intent intent = new Intent(MainActivity.this, PostListActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);

                        startActivity(intent);

                        menuItem.setChecked(true);
                        return true;
                    case R.id.navigation_profile:
                        Intent intent1=new Intent(MainActivity.this,UserProfileActivity.class);
                        intent1.putExtra(INTENT_PROFILE_KEY,userID);
                        intent1.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        startActivity(intent1);
                        menuItem.setChecked(true);

                        return true;
                }
                return false;
            }
        });
    }

}