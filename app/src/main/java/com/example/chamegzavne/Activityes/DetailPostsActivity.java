package com.example.chamegzavne.Activityes;

import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.chamegzavne.InfoClass.ChatList;
import com.example.chamegzavne.R;
import com.github.chrisbanes.photoview.PhotoView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class DetailPostsActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String INTENT_CHAT_KEY="intent.chat.key";
    private static final String INTENT_MES_KEY="intent.mes.key";
    private static final String TAGo="chato";

    ArrayList<String> result=new ArrayList<>();
    ArrayList<String> addedChatIDes=new ArrayList<>();
    public static Map<String,String> chatHashmap=new HashMap<>();
    String chatKey;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myChatListstRef;
    DatabaseReference myPosts;
    DatabaseReference messageList;

    private String postUserId;
    private String postID;
    private String photoURI;
    private String userPhotoURI;
    private String title;
    private String comment;
    private String userName;
    private String GEL;
    private String Latitude;
    private String Longitude;

    TextView pUserName;
    TextView pTitle;
    TextView pComment;
    TextView pAddress;
    TextView pGEL;
    ImageView pUserImage;
    PhotoView pImage;
    ImageView pAcceptBtn;



    //pinch zoom






    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_posts);


        Intent intent=getIntent();
        result=intent.getStringArrayListExtra(INTENT_CHAT_KEY);

        userName=result.get(2);
        title=result.get(3);
        comment=result.get(4);
        GEL=result.get(5);
        postID=result.get(6);
        postUserId=result.get(7);
        photoURI=result.get(8);
        userPhotoURI=result.get(9);
        Latitude=result.get(10);
        Longitude=result.get(11);

        pUserName=findViewById(R.id.detail_user_name_id);
        pTitle=findViewById(R.id.detail_title_id);
        pComment=findViewById(R.id.detail_comment_id);
        pAddress=findViewById(R.id.detail_address);
        pAcceptBtn=findViewById(R.id.detail_accept_btn);
        pUserImage=findViewById(R.id.detail_profile_image);
        pImage=findViewById(R.id.detail_image);

        pAcceptBtn.setOnClickListener(this);
        findViewById(R.id.constraint_id).setOnClickListener(this);

        //Addresses
        String address = getAddress(Double.parseDouble(Latitude),Double.parseDouble(Longitude)).get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
        String city = getAddress(Double.parseDouble(Latitude),Double.parseDouble(Longitude)).get(0).getLocality();
        String state = getAddress(Double.parseDouble(Latitude),Double.parseDouble(Longitude)).get(0).getAdminArea();
        String country = getAddress(Double.parseDouble(Latitude),Double.parseDouble(Longitude)).get(0).getCountryName();
        String postalCode = getAddress(Double.parseDouble(Latitude),Double.parseDouble(Longitude)).get(0).getPostalCode();
        String knownName = getAddress(Double.parseDouble(Latitude),Double.parseDouble(Longitude)).get(0).getFeatureName(); // Only if available else return NULL

        city=city!=null?city:"";

        pUserName.setText(userName);
        pTitle.setText(title);
        pComment.setText(getComment(comment));
        pAddress.setText(country+" : "+state+" : "+city);
        Picasso.get().load(userPhotoURI).into(pUserImage);
        Picasso.get().load(photoURI).into(pImage);










        visible_unVisibleButton();

        chatKey="chatLists";
        myChatListstRef=database.getReference("chatLists/"+MainActivity.userID);
        myPosts=database.getReference("posts");
        messageList=database.getReference("chatLists/"+MainActivity.userID);



    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.constraint_id:
                pComment.setText(comment);
                break;
            case R.id.detail_accept_btn:
                Log.d("postsiii", "" +postUserId+"  "+MainActivity.userID);
                //if(!postUserId.equals(MainActivity.userID)){
                    Log.d("postsiii", "" +"!=");
                Intent intent=new Intent(DetailPostsActivity.this,MessageActivity.class);
                myChatListstRef.child(postID).setValue(new ChatList(result.get(2),result.get(3),result.get(7),result.get(8),result.get(9)));
                Log.d(TAGo, "Added chatList: "+result.get(2)+"in "+MainActivity.userID+"chat:"+postUserId);
                intent.putExtra(INTENT_MES_KEY,postID);
                startActivity(intent);
                finish();
               // }
               // else{
                  // deletePost();
               // }
                break;
            default:
                break;
        }
    }

    public void visible_unVisibleButton(){
        Log.d("chato", postUserId+" : "+MainActivity.userID);
        if(postUserId.equals(MainActivity.userID)){
            Log.d("chato", "onCreate: ===============");
            //acceptBtn.setVisibility(View.INVISIBLE);



        }
        else{
            Log.d("chato", "visible_unVisibleButton: ");
            //acceptBtn.setVisibility(View.VISIBLE);

        }
    }




    private void deletePost(){
        myPosts.child(postID).removeValue();
        messageList.child(postID).removeValue();
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


    private String getComment(String comment){
        return  comment.length()>40 ?
                comment.substring(0,40)+"..." :
                comment;
    }


}
