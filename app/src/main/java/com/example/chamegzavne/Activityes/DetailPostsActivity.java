package com.example.chamegzavne.Activityes;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.chamegzavne.InfoClass.ChatList;
import com.example.chamegzavne.R;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DetailPostsActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String INTENT_CHAT_KEY="intent.chat.key";
    private static final String INTENT_MES_KEY="intent.mes.key";
    private static final String TAGo="chato";
    TextView pTitle;
    TextView pGel;
    TextView pName;
    TextView pComment;
    TextView acceptBtn;
    String idForChat;
    String nameChatClient;
    String postUserId;
    ArrayList<String> result=new ArrayList<>();
    ArrayList<String> addedChatIDes=new ArrayList<>();
    public static Map<String,String> chatHashmap=new HashMap<>();
    String chatKey;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myChatListstRef;
    DatabaseReference myPosts;
    DatabaseReference messageList;





    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_posts);
        pTitle=findViewById(R.id.post_detail_title);
        pName=findViewById(R.id.info_detail_user_name);
        pGel=findViewById(R.id.gel_detail_post);
        pComment=findViewById(R.id.post_detail_comment);
        acceptBtn=findViewById(R.id.post_detail_accept_btn);
        acceptBtn.setOnClickListener(this);

        Intent intent=getIntent();
        result=intent.getStringArrayListExtra(INTENT_CHAT_KEY);
        idForChat=result.get(0);
        nameChatClient=result.get(1);
        postUserId=result.get(6);
        pName.setText(result.get(2));
        pTitle.setText(result.get(3));
        pComment.setText(result.get(4));
        pGel.setText("Gel"+result.get(5));

        visible_unVisibleButton();

        chatKey="chatLists";
        myChatListstRef=database.getReference("chatLists/"+MainActivity.userID);
        myPosts=database.getReference("posts");
        messageList=database.getReference("chatLists/"+MainActivity.userID);



    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.post_detail_accept_btn:
                if(!postUserId.equals(MainActivity.userID)){
                Intent intent=new Intent(DetailPostsActivity.this,MessageActivity.class);
                myChatListstRef.child(postUserId).setValue(new ChatList(result.get(2),result.get(3),result.get(7)));
                Log.d(TAGo, "Added chatList: "+result.get(2)+"in "+MainActivity.userID+"chat:"+postUserId);
                intent.putExtra(INTENT_MES_KEY,postUserId);
                startActivity(intent);}
                else{
                    myPosts.child(postUserId).removeValue();
                    messageList.child(postUserId).removeValue();


                }
                break;
            default:
                break;
        }
    }

    public void visible_unVisibleButton(){
        Log.d("chato", postUserId+" : "+MainActivity.userID);
        if(postUserId.equals(MainActivity.userID+result.get(3))){
            Log.d("chato", "onCreate: ===============");
            //acceptBtn.setVisibility(View.INVISIBLE);
            acceptBtn.setText("clear");

        }
        else{
            Log.d("chato", "visible_unVisibleButton: ");
            //acceptBtn.setVisibility(View.VISIBLE);
            acceptBtn.setText("accept");
        }
    }





}
