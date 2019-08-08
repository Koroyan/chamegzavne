package com.example.chamegzavne.Activityes;

import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ImagePinchZoomActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String INTENT_CHAT_KEY="intent.chat.key";
    private static final String INTENT_MES_KEY="intent.mes.key";
    private static final String TAGo="chato";
    PhotoView detailImage;
    ImageView acceptBtn;
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

    //pinch zoom






    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_pinch_zoom);
        detailImage=findViewById(R.id.detail_image);
        acceptBtn=findViewById(R.id.accept_btn);
        acceptBtn.setOnClickListener(this);

        Intent intent=getIntent();
        result=intent.getStringArrayListExtra(INTENT_CHAT_KEY);

        postID=result.get(6);
        postUserId=result.get(7);
        photoURI=result.get(8);
        Picasso.get().load(photoURI).into(detailImage);









        visible_unVisibleButton();

        chatKey="chatLists";
        myChatListstRef=database.getReference("chatLists/"+MainActivity.userID);
        myPosts=database.getReference("posts");
        messageList=database.getReference("chatLists/"+MainActivity.userID);



    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.accept_btn:
                Log.d("postsiii", "" +postUserId+"  "+MainActivity.userID);
                //if(!postUserId.equals(MainActivity.userID)){
                Log.d("postsiii", "" +"!=");
                Intent intent=new Intent(ImagePinchZoomActivity.this,MessageActivity.class);
                myChatListstRef.child(postID).setValue(new ChatList(result.get(2),result.get(3),result.get(7),result.get(8)));
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




}

