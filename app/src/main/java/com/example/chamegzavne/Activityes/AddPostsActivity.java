package com.example.chamegzavne.Activityes;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.chamegzavne.InfoClass.ChatList;
import com.example.chamegzavne.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class AddPostsActivity extends AppCompatActivity {
    //message token
    public static  String MESSAG_TOKEN;

    private static final String INTENT_INFO_KEY="intent_info_key";
    final FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference addChatList;
    private static final String TAGo="chato";
    TextView title;
    TextView gel;
    TextView comment;
    TextView send_btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_posts);

        title=findViewById(R.id.title_id);
        gel=findViewById(R.id.gel_id);
        comment=findViewById(R.id.comment_id);
        send_btn=findViewById(R.id.send_btn);

        send_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("mylocation", "onClick: ");
                ArrayList<String> putInfoArray=new ArrayList<>();
                putInfoArray.add(gel.getText().toString());
                putInfoArray.add(title.getText().toString());
                putInfoArray.add(comment.getText().toString());
                Intent intent =new Intent(AddPostsActivity.this,MainActivity.class);
                //intent.putStringArrayListExtra(INTENT_INFO_KEY,putInfoArray);
                intent.putExtra(INTENT_INFO_KEY,new String[]{gel.getText().toString(),title.getText().toString(),comment.getText().toString()});

                Log.d("add", "onClick: "+MainActivity.userID+" : "+MainActivity.userName+" : "+title.getText());
                addChatList=database.getReference("chatLists/"+MainActivity.userID);
                addChatList.child(MainActivity.userID+title.getText()).setValue(new ChatList(MainActivity.userName,title.getText().toString(),MainActivity.userID));
                Log.d(TAGo, "Added chatList: "+title.getText()+"in "+MainActivity.userID+"chat:"+MainActivity.userID);

                setResult(100,intent);
                finish();
                Log.d("resultactivity", "onClick: resultchanged");
            }
        });

    }
}