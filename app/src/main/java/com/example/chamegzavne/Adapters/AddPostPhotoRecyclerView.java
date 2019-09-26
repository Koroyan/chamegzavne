package com.example.chamegzavne.Adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chamegzavne.R;

import java.util.List;

public class AddPostPhotoRecyclerView extends RecyclerView.Adapter<AddPostPhotoRecyclerView.ViewHolder> {
    private LayoutInflater mInflater;
    List<Bitmap> URI;
    OnCancelClickListener onCancelClickListener;
    public AddPostPhotoRecyclerView(Context context, List<Bitmap> URI) {
        this.mInflater = LayoutInflater.from(context);
        this.URI=URI;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view=mInflater.inflate(R.layout.add_post_photos_recyclerview,viewGroup,false);
        return new AddPostPhotoRecyclerView.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.postImage.setImageBitmap(URI.get(position));

    }

    @Override
    public int getItemCount() {
        return URI.size();
    }

    public void setAdapter(List<Bitmap> URI){
        this.URI=URI;
        notifyDataSetChanged();

    }




    public void setOnCancelClickListener(OnCancelClickListener onCancelClickListener){this.onCancelClickListener=onCancelClickListener;}

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView postImage;
        ImageView postCancelBtn;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            postImage=itemView.findViewById(R.id.post_image);
            postCancelBtn=itemView.findViewById(R.id.post_cancel_btn);
            postCancelBtn.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (onCancelClickListener!=null){
                onCancelClickListener.onCancelClick(v,URI.get(getAdapterPosition()),getAdapterPosition());
            }
        }
    }

    public interface OnCancelClickListener{
      void onCancelClick(View view,Bitmap bitmap,int position);
    }



}
