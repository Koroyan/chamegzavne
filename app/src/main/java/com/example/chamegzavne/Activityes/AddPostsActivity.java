package com.example.chamegzavne.Activityes;
import android.app.ProgressDialog;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;
import com.example.chamegzavne.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;
import java.util.UUID;

public class AddPostsActivity extends AppCompatActivity {
    private static final int PICK_IMAGE_REQUEST =100 ;
    private Uri filePath;
    //message token
    public static  String MESSAG_TOKEN;

    private static final String INTENT_INFO_KEY="intent_info_key";
    //FIREBASE
    FirebaseStorage storage;
    StorageReference storageReference;
    final FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference addChatList;
    private static final String TAGo="chato";
    TextInputLayout title;
    TextView gel;
    TextInputLayout comment;
    TextView send_btn;
    Toolbar toolbar;
    TextView addimage;
    ImageView postImage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_posts);

        toolbar=findViewById(R.id.add_post_toolbar_id);
        setSupportActionBar(toolbar);

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        //setActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        title=(TextInputLayout) findViewById(R.id.title_id);
       // gel=findViewById(R.id.gel_id);
        comment=(TextInputLayout)findViewById(R.id.comment_id);
        send_btn=findViewById(R.id.send_btn);
        addimage=findViewById(R.id.add_image);
        postImage=findViewById(R.id.post_image);
        addimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage();
            }
        });

        send_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("mylocation", "onClick: ");
                uploadImage(title.getEditText().getText().toString());

            }
        });

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == android.R.id.home) {
            Toast.makeText(this,"knopka nazad", Toast.LENGTH_LONG).show();
            finish();
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
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

    private void chooseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"),PICK_IMAGE_REQUEST);
    }

    private void uploadImage(String posttitle) {

        if(filePath != null)
        {
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();

            StorageReference ref = storageReference.child("images/"+MainActivity.userID+posttitle);
            ref.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            ArrayList<String> putInfoArray=new ArrayList<>();
                            putInfoArray.add(gel.getText().toString());
                            putInfoArray.add(title.getEditText().getText().toString());
                            putInfoArray.add(comment.getEditText().getText().toString());
                            putInfoArray.add(filePath.toString());
                            Intent intent =new Intent(AddPostsActivity.this,MainActivity.class);
                            //intent.putStringArrayListExtra(INTENT_INFO_KEY,putInfoArray);
                            intent.putExtra(INTENT_INFO_KEY,new String[]{gel.getText().toString(),title.getEditText().getText().toString(),comment.getEditText().getText().toString()});

                            Log.d("add", "onClick: "+MainActivity.userID+" : "+MainActivity.userName+" : "+title.getEditText().getText());
                            Log.d("fiol", "filepath : "+filePath.toString());
                           // addChatList=database.getReference("chatLists/"+MainActivity.userID);
                           // addChatList.child(MainActivity.userID+title.getText()).setValue(new ChatList(MainActivity.userName,title.getText().toString(),MainActivity.userID,filePath.toString()));
                            Log.d(TAGo, "Added chatList: "+title.getEditText().getText()+"in "+MainActivity.userID+"chat:"+MainActivity.userID);

                            progressDialog.dismiss();
                            setResult(100,intent);
                            finish();
                            Log.d("resultactivity", "onClick: resultchanged");
                            Toast.makeText(AddPostsActivity.this, "Uploaded", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(AddPostsActivity.this, "Failed "+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0*taskSnapshot.getBytesTransferred()/taskSnapshot
                                    .getTotalByteCount());
                            progressDialog.setMessage("Uploaded "+(int)progress+"%");
                        }
                    });
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null )
        {
            filePath = data.getData();
            try {

                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                postImage.setImageBitmap(bitmap);

            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }
}