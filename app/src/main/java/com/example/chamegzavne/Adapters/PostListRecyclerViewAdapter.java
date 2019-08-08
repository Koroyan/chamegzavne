package com.example.chamegzavne.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.example.chamegzavne.InfoClass.Post;
import com.example.chamegzavne.R;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class PostListRecyclerViewAdapter extends RecyclerView.Adapter<PostListRecyclerViewAdapter.ViewHolder> {
    private LayoutInflater mInflater;
    private List<Post> posts;
    private onPostItemClickListener onPostItemClickListener;
    private Context context;

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
        View view =  mInflater.inflate(R.layout.post_list_recyclerview_rows, viewGroup, false);
        return new ViewHolder(view);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, int i) {



         String address = getAddress(posts.get(i).getpLatitude(),posts.get(i).getpLongitude()).get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
         String city = getAddress(posts.get(i).getpLatitude(),posts.get(i).getpLongitude()).get(0).getLocality();
         String state = getAddress(posts.get(i).getpLatitude(),posts.get(i).getpLongitude()).get(0).getAdminArea();
         String country = getAddress(posts.get(i).getpLatitude(),posts.get(i).getpLongitude()).get(0).getCountryName();
         String postalCode = getAddress(posts.get(i).getpLatitude(),posts.get(i).getpLongitude()).get(0).getPostalCode();
         String knownName = getAddress(posts.get(i).getpLatitude(),posts.get(i).getpLongitude()).get(0).getFeatureName(); // Only if available else return NULL

        city=city!=null?city:"";


        TextView textView=new TextView(context);
        textView.setText("...");
        textView.setTextColor(R.color.colorBlack);



        viewHolder.name.setText(posts.get(i).getpName());
        viewHolder.comment.setText(getComment(posts.get(i).getpComment().toString()));
        viewHolder.title.setText(posts.get(i).getpTitle());
        viewHolder.address.setText(country+" : "+state+" : "+city);
        Picasso.get().load(posts.get(i).getpUserPhotoURL()).into(viewHolder.profileImage);
        Picasso.get().load(posts.get(i).getpPhotoURL()).fit().centerCrop().into(viewHolder.image);


    }

    @Override
    public int getItemCount() {
        return posts.size();
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

    private String getComment(String comment){
        return  comment.length()>40 ?
                comment.substring(0,40)+"..." :
                comment;
    }

    public void setPostclickListener(onPostItemClickListener onPostItemClickListener){
        this.onPostItemClickListener=onPostItemClickListener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        ImageView profileImage;
        TextView name;
        TextView comment;
        TextView title;
        TextView address;
        ImageView image;
        ImageView acceptBtn;
        View constraint;


        ViewHolder(@NonNull View itemView) {
            super(itemView);
            profileImage=itemView.findViewById(R.id.profile_image);
            name=itemView.findViewById(R.id.post_list_user_name_id);
            comment=itemView.findViewById(R.id.post_list_comment_id);
            title=itemView.findViewById(R.id.post_list_title_id);
            address=itemView.findViewById(R.id.post_address);
            image=itemView.findViewById(R.id.post_list_image);
            acceptBtn=itemView.findViewById(R.id.post_accept_btn);
            constraint=itemView.findViewById(R.id.constraint_id);
            constraint.setOnClickListener(this);
            acceptBtn.setOnClickListener(this);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.constraint_id:
                    comment.setText(posts.get(getAdapterPosition()).getpComment());
                    break;

                    default:
                if (onPostItemClickListener != null) {
                    onPostItemClickListener.onPostClick(v, posts.get(getAdapterPosition()));

                }
                break;

            }
        }
    }

   public interface onPostItemClickListener{
        void onPostClick(View v, Post post);
    }

}
