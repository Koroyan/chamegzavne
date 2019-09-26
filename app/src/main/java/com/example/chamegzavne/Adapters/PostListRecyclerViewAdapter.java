package com.example.chamegzavne.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Build;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;


import com.example.chamegzavne.Activityes.MainActivity;
import com.example.chamegzavne.Activityes.MessageListActivity;
import com.example.chamegzavne.Activityes.PostListActivity;
import com.example.chamegzavne.InfoClass.Chat;
import com.example.chamegzavne.InfoClass.Post;
import com.example.chamegzavne.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Cache;
import com.squareup.picasso.Callback;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Response;

public class PostListRecyclerViewAdapter extends RecyclerView.Adapter<PostListRecyclerViewAdapter.ViewHolder> {
    private LayoutInflater mInflater;
    private List<Post> posts;
    private onPostItemClickListener onPostItemClickListener;
    private onImageTouchListener onImageTouchListener;
    private OnProfileImageClickListener onProfileImageClickListener;
    private Context context;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myChatListstRef;
    DatabaseReference mypostsRef;
    DatabaseReference mypostmesages;

    private Uri imageUri=null;
    FirebaseStorage storage;
    StorageReference storageReference;

    public PostListRecyclerViewAdapter(Context context, List<Post> posts) {
        this.mInflater = LayoutInflater.from(context);
        this.posts=posts;
        this.context=context;
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        if(i==1){
            View view=mInflater.inflate(R.layout.post_list_recyclerview_rows_noimage,viewGroup,false);
            return new ViewHolder(view);
        }
        else {
            View view = mInflater.inflate(R.layout.post_list_recyclerview_rows, viewGroup, false);
            return new ViewHolder(view);
        }
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, final int i) {


       viewHolder.menuBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("sxmec", "onClick: ");
                PopupMenu popupMenu= null;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                    popupMenu = new PopupMenu(context,viewHolder.menuBtn);
                }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
                    popupMenu.inflate(R.menu.post_settings_menu);
                }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                    popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            Log.d("sxmec", "onMenuItemClick: ");
                            switch(item.getItemId()){
                                case R.id.menu_item_delete:
                                    deletePost(posts.get(i));
                                    return true;
                                default:
                                    return true;
                            }


                        }
                    });
                }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                    popupMenu.show();
                }
            }
        });

        final int position = i;
        try {
            String address = getAddress(posts.get(i).getpLatitude(), posts.get(i).getpLongitude()).get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
            String city = getAddress(posts.get(i).getpLatitude(), posts.get(i).getpLongitude()).get(0).getLocality();
            String state = getAddress(posts.get(i).getpLatitude(), posts.get(i).getpLongitude()).get(0).getAdminArea();
            String country = getAddress(posts.get(i).getpLatitude(), posts.get(i).getpLongitude()).get(0).getCountryName();
            String postalCode = getAddress(posts.get(i).getpLatitude(), posts.get(i).getpLongitude()).get(0).getPostalCode();
            String knownName = getAddress(posts.get(i).getpLatitude(), posts.get(i).getpLongitude()).get(0).getFeatureName(); // Only if available else return NULL
            city = city != null ? city : "";




        if (posts.get(i).getpPhotoURL() == null) {
            viewHolder.name.setText(posts.get(i).getpName());
            viewHolder.comment.setText(getComment(posts.get(i).getpComment().toString()));
            viewHolder.title.setText(posts.get(i).getpTitle());
            viewHolder.address.setText(country + " : " + state + " : " + city);
            Log.d("photouri", "photouri : " + posts.get(i).getpUserPhotoURL());
            Picasso.get().load(posts.get(i).getpUserPhotoURL()).networkPolicy(NetworkPolicy.OFFLINE).fit().centerCrop().into(viewHolder.profileImage, new Callback() {
                @Override
                public void onSuccess() {

                }

                @Override
                public void onError(Exception e) {
                    Log.d("Picassoerror", "onError: " + e);
                    Picasso.get().load(posts.get(position).getpUserPhotoURL()).fit().centerCrop().into(viewHolder.profileImage);

                }
            });

        } else {
            viewHolder.name.setText(posts.get(i).getpName());
            viewHolder.comment.setText(getComment(posts.get(i).getpComment()), TextView.BufferType.SPANNABLE);
            viewHolder.title.setText(posts.get(i).getpTitle());
            viewHolder.address.setText(country + " : " + state + " : " + city);
            Log.d("photouri", "photouri : " + posts.get(i).getpUserPhotoURL());
            Picasso.get().load(posts.get(i).getpUserPhotoURL()).networkPolicy(NetworkPolicy.OFFLINE).fit().centerCrop().into(viewHolder.profileImage, new Callback() {
                @Override
                public void onSuccess() {

                }

                @Override
                public void onError(Exception e) {
                    Log.d("Picassoerror", "onError: " + e);
                    Picasso.get().load(posts.get(position).getpUserPhotoURL()).fit().centerCrop().into(viewHolder.profileImage);

                }
            });
            Picasso.get().load(posts.get(i).getpPhotoURL()).networkPolicy(NetworkPolicy.OFFLINE).fit().centerCrop().into(viewHolder.image, new Callback() {
                @Override
                public void onSuccess() {

                }

                @Override
                public void onError(Exception e) {
                    Log.d("Picassoerror", "onError: " + e);
                    Picasso.get().load(posts.get(position).getpPhotoURL()).fit().centerCrop().into(viewHolder.image);
                }
            });

        }
        }
        catch (Exception e){
            posts.add(new Post());
            viewHolder.name.setText("internet Exception");
            viewHolder.comment.setText("გთხოვთ ჩაართეტ ინტერნეტი");
            return;
        }
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    @Override
    public int getItemViewType(int position) {

        Log.d("item", "getItemViewType: ");
        if(posts.get(position).getpPhotoURL()==null){
            return 1;

        }

        else{

            return 2;
        }
    }

    private void deletePost(Post post) {

        myChatListstRef = database.getReference("chatLists/" + MainActivity.userID);
        mypostsRef = database.getReference("posts");
        mypostmesages = database.getReference(MainActivity.userID + "/messages");

        if (post.getpUserID().equals(MainActivity.userID)){
            Log.d("sxmec", "its my list____________ ");

            myChatListstRef.child(post.getpUserID() + post.getpTitle()).removeValue();
            mypostsRef.child(post.getpUserID() + post.getpTitle()).removeValue();
            mypostmesages.removeValue();
            mypostsRef.child(post.getpUserID() + post.getpTitle()).removeValue();
            posts.remove(post);
            mypostsRef.child(MainActivity.userID + post.getpTitle()).child("messages").setValue(new Chat(null, "chat deleded", null, null, new Date().toString()));

        } else{
            Log.d("sxmec", "its other list_____________");

            myChatListstRef.child(post.getpUserID().toString() + post.getpTitle().toString()).removeValue();
            Toast.makeText(context,"სხვის ფოსტის წაშლა შეუძლებელია",Toast.LENGTH_LONG).show();
        }
    }

    public void setAdapter(List<Post> posts){
        this.posts=posts;
        notifyDataSetChanged();

    }

    private List<Address> getAddress(double latitude, double longitude){
        Geocoder geocoder;
        List<Address> addresses = null;
        geocoder = new Geocoder(context, Locale.getDefault());

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

    private SpannableStringBuilder getComment(String comment){
        if(comment.length()<40)return new SpannableStringBuilder(comment);
        comment=  comment.length()>40 ?
                comment.substring(0,40)+"..." :
                comment;

        SpannableStringBuilder commentBuilder = new SpannableStringBuilder(comment);
        commentBuilder.setSpan(new ForegroundColorSpan(ContextCompat.getColor(context, R.color.colorBlack)), 40, 43, 0);
        commentBuilder.setSpan(new RelativeSizeSpan(2f),40,43,0);

        return commentBuilder;
    }

    public void setPostclickListener(onPostItemClickListener onPostItemClickListener){
        this.onPostItemClickListener=onPostItemClickListener;
    }
    public void setImageTouchListener(onImageTouchListener onImageTouchListener){
        this.onImageTouchListener=onImageTouchListener;
    }
    public void setOnProfileImageClickListener(OnProfileImageClickListener onProfileImageClickListener){
        this.onProfileImageClickListener=onProfileImageClickListener;
    }



    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,View.OnTouchListener{
        ImageView profileImage;
        TextView name;
        TextView comment;
        TextView title;
        TextView address;
        ImageView image;
        ImageView acceptBtn;
        ImageView menuBtn;



        ViewHolder(@NonNull View itemView) {
            super(itemView);
            profileImage = itemView.findViewById(R.id.profile_image);
            name = itemView.findViewById(R.id.post_list_user_name_id);
            comment = itemView.findViewById(R.id.post_list_comment_id);
            title = itemView.findViewById(R.id.post_list_title_id);
            address = itemView.findViewById(R.id.post_address);
            image = itemView.findViewById(R.id.post_list_image);
            acceptBtn = itemView.findViewById(R.id.post_accept_btn);
            menuBtn=itemView.findViewById(R.id.post_list_menu);
            comment.setOnClickListener(this);
            acceptBtn.setOnClickListener(this);
            if(name!=null)
                name.setOnClickListener(this);
            if (image != null)
                image.setOnClickListener(this);
            if(profileImage!=null)
                profileImage.setOnClickListener(this);
            itemView.setOnClickListener(this);
            final double[] oldTouchValue = new double[1];

        }


        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.post_list_comment_id:
                    comment.setText(posts.get(getAdapterPosition()).getpComment());
                    break;
                case R.id.profile_image:
                case R.id.post_list_user_name_id:
                    if(onProfileImageClickListener!=null)
                        onProfileImageClickListener.onProfileImageClick(v,posts.get(getAdapterPosition()));
                    break;
                default:
                if (onPostItemClickListener != null) {
                    onPostItemClickListener.onPostClick(v, posts.get(getAdapterPosition()));

                }
                break;

            }
        }

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            return false;
        }
    }

   public interface onPostItemClickListener{
        void onPostClick(View v, Post post);
    }

    public interface onImageTouchListener{
        void onImageTouch(View v, Post post,MotionEvent event);
    }

    public interface OnProfileImageClickListener{
        void onProfileImageClick(View v,Post post);
    }

}
