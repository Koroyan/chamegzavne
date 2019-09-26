package com.example.chamegzavne.Activityes;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chamegzavne.Adapters.AddPostPhotoRecyclerView;
import com.example.chamegzavne.InfoClass.Post;
import com.example.chamegzavne.R;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class AddPostsActivity extends AppCompatActivity implements AddPostPhotoRecyclerView.OnCancelClickListener {
    private static final int PICK_IMAGE_REQUEST =100 ;
    public static final String TAG="changwdlistener";
    private static final String adMobTAG="adMobTag";
    private static boolean bool = true;
    private List<Uri> filePaths=new ArrayList<>();
    List<Bitmap> ImageURI=new ArrayList<>();
    final List<String> posts=new ArrayList<>();
    //message token
    public static  String MESSAG_TOKEN;

    //RecyclerView Adapter
    AddPostPhotoRecyclerView adapter;

    //reklam banner
    AdView mAdView;

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
    ImageView send_btn;
    Toolbar toolbar;
    ImageView addimage;
    ImageView postImage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_posts);

        toolbar=findViewById(R.id.add_post_toolbar_id);
        setSupportActionBar(toolbar);


        RecyclerViewInit();
        initaddedPostNames();
        admobInit();



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
                if(bool)
                    try {
                        uploadImage(filePaths.get(0), title.getEditText().getText().toString(), 0);
                    }
                catch (Exception e) {
                    uploadImage(null, title.getEditText().getText().toString(), 0);

                }

                else{
                    title.setError("უკვე გაქვთ იგივენაირი ფოსტი");
                    Toast.makeText(AddPostsActivity.this,"change post name",Toast.LENGTH_LONG).show();
                }
            }
        });

        Objects.requireNonNull(title.getEditText()).addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                Log.d(TAG, "beforeTextChanged: ");

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Log.d(TAG, "onTextChanged: ");
            }

            @Override
            public void afterTextChanged(Editable s) {
                Log.d(TAG, "afterTextChanged: "+posts.size());
                for(String result:posts) {
                    Log.d(TAG, " " + result + " == " + s);
                    if (s.toString().equals(result)) {
                        title.setError("უკვე გაქვთ იგივენაირი ფოსტი");
                        bool=false;
                        break;
                    }
                    else {
                        title.setError(null);
                        bool=true;
                    }
                }
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

    public void uploadImage(final Uri fileImage, final String posttitle, int count) {


        Log.d("countess", "filepathsize: "+filePaths.size()+"  "+ count);

        if(filePaths.size() != 0) {



                final ProgressDialog progressDialog = new ProgressDialog(this);
                progressDialog.setTitle("Uploading image... "+(count+1)+"/"+filePaths.size());
                progressDialog.show();

                StorageReference ref = storageReference.child("images/" + MainActivity.userID + posttitle+count);
                count++;
            final int finalCount = count;
            ref.putFile(fileImage)
                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                if(finalCount<filePaths.size())
                                uploadImage(filePaths.get(finalCount),posttitle,finalCount);
                                else{

                                        ArrayList<String> putInfoArray = new ArrayList<>();
                                        // putInfoArray.add(gel.getText().toString());
                                        putInfoArray.add(title.getEditText().getText().toString());
                                        putInfoArray.add(comment.getEditText().getText().toString());
                                        putInfoArray.add(filePaths.get(0).toString());
                                        Intent intent = new Intent(AddPostsActivity.this, MainActivity.class);
                                        //intent.putStringArrayListExtra(INTENT_INFO_KEY,putInfoArray);
                                        intent.putExtra(INTENT_INFO_KEY, new String[]{filePaths.size()+"", title.getEditText().getText().toString(), comment.getEditText().getText().toString()});
                                        Log.d("add", "onClick: " + MainActivity.userID + " : " + MainActivity.userName + " : " + title.getEditText().getText());
                                        // addChatList=database.getReference("chatLists/"+MainActivity.userID);
                                        // addChatList.child(MainActivity.userID+title.getText()).setValue(new ChatList(MainActivity.userName,title.getText().toString(),MainActivity.userID,filePath.toString()));
                                        Log.d(TAGo, "Added chatList: " + title.getEditText().getText() + "in " + MainActivity.userID + "chat:" + MainActivity.userID);

                                        progressDialog.dismiss();
                                        setResult(100, intent);
                                        finish();
                                        Log.d("resultactivity", "onClick: resultchanged");
                                        Toast.makeText(AddPostsActivity.this, "Uploaded", Toast.LENGTH_SHORT).show();

                                }
                                progressDialog.dismiss();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                progressDialog.dismiss();
                                Toast.makeText(AddPostsActivity.this, "Failed " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        })
                        .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                                double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot
                                        .getTotalByteCount());
                                progressDialog.setMessage("Uploaded " + (int) progress + "%");
                            }
                        });

            }


        else{
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();
            ArrayList<String> putInfoArray=new ArrayList<>();
            // putInfoArray.add(gel.getText().toString());
            putInfoArray.add(title.getEditText().getText().toString());
            putInfoArray.add(comment.getEditText().getText().toString());
            putInfoArray.add(null);
            Intent intent =new Intent(AddPostsActivity.this,MainActivity.class);
            //intent.putStringArrayListExtra(INTENT_INFO_KEY,putInfoArray);
            intent.putExtra(INTENT_INFO_KEY,new String[]{"0",title.getEditText().getText().toString(),comment.getEditText().getText().toString()});


            progressDialog.dismiss();
            setResult(100,intent);
            finish();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Uri filePath;
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null )
        {
            filePath = data.getData();
            try {

                //jamanakavor mi nkari hamar
                filePaths.clear();
                ImageURI.clear();

                filePaths.add(filePath);
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                ImageURI.add(bitmap);
                adapter.setAdapter(ImageURI);

            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }
    private void RecyclerViewInit(){
        final RecyclerView recyclerView = findViewById(R.id.add_post_photo_recycler_view_id);
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemViewCacheSize(20);
        recyclerView.setDrawingCacheEnabled(true);
        recyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        adapter = new AddPostPhotoRecyclerView(this, ImageURI);
        recyclerView.setAdapter(adapter);
        adapter.setOnCancelClickListener(this);
    }

    @Override
    public void onCancelClick(View view, Bitmap bitmap,int position) {
        ImageURI.remove(bitmap);
        filePaths.remove(filePaths.get(position));
        adapter.setAdapter(ImageURI);
    }
    private void initaddedPostNames(){

        DatabaseReference myPostRef=FirebaseDatabase.getInstance().getReference("posts");
        myPostRef.keepSynced(true);
        myPostRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Post post=dataSnapshot.getValue(Post.class);
                assert post != null;
                if (post.getpUserID() == null) return;
                if(post.getpUserID().equals(MainActivity.userID))
               posts.add(post.getpTitle());

                // recyclerView.smoothScrollToPosition(0);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                Post post=dataSnapshot.getValue(Post.class);
                posts.remove(post.getpTitle());


            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    private void admobInit(){
        MobileAds.initialize(this,
                "ca-app-pub-1537892667165291~9892742328");


        mAdView = findViewById(R.id.ad_post_adView);
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
                Toast.makeText(AddPostsActivity.this, "adMob error: "+errorCode, Toast.LENGTH_SHORT).show();
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
    }
}