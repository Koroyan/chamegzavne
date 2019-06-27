package com.example.chamegzavne.Adapters;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.chamegzavne.InfoClass.Chat;
import com.example.chamegzavne.InfoClass.Post;
import com.example.chamegzavne.R;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.List;

public class PostListRecyclerViewAdapter extends RecyclerView.Adapter<PostListRecyclerViewAdapter.ViewHolder> {
    private LayoutInflater mInflater;
    List<Post> posts;
    onPostItemClickListener onPostItemClickListener;

    public PostListRecyclerViewAdapter(Context context, List<Post> posts) {
        this.mInflater = LayoutInflater.from(context);
        this.posts=posts;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view =  mInflater.inflate(R.layout.post_list_recyclerview_rows, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        Picasso.get().load(Uri.parse(posts.get(i).getpPhotoURL())).into(viewHolder.profileImage);
        viewHolder.name.setText(posts.get(i).getpName());
        viewHolder.comment.setText(posts.get(i).getpComment());
        viewHolder.title.setText(posts.get(i).getpTitle());

    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    public void setAdapter(List<Post> posts){
        this.posts=posts;
        notifyDataSetChanged();

    }

    public void setPostclickListener(onPostItemClickListener onPostItemClickListener){
        this.onPostItemClickListener=onPostItemClickListener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        ImageView profileImage;
        TextView name;
        TextView comment;
        TextView title;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            profileImage=itemView.findViewById(R.id.profile_image);
            name=itemView.findViewById(R.id.post_list_user_name_id);
            comment=itemView.findViewById(R.id.post_list_comment_id);
            title=itemView.findViewById(R.id.post_list_title_id);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if(onPostItemClickListener!=null){
                onPostItemClickListener.onPostClick(v,posts.get(getAdapterPosition()));

            }

        }
    }

   public interface onPostItemClickListener{
        public void onPostClick(View v, Post post);
    }

}
