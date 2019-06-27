package com.example.chamegzavne.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.chamegzavne.InfoClass.Chat;
import com.example.chamegzavne.R;
import com.google.firebase.auth.FirebaseAuth;

import java.util.List;



public class MessageRecyclerViewAdapter extends RecyclerView.Adapter<MessageRecyclerViewAdapter.ViewHolder> {
    private static final String TAG="item";
    private LayoutInflater mInflater;
    private String userID;
    List<Chat> chat;
    FirebaseAuth firebaseAuth;

    public MessageRecyclerViewAdapter(Context context, List<Chat> chat,String userID) {
        this.mInflater = LayoutInflater.from(context);
        this.chat=chat;
        this.userID=userID;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view;
        if(i==1){
            Log.d(TAG, "onCreateViewHolder: 1");
            view =  mInflater.inflate(R.layout.message_recyclerview_rows_right, viewGroup, false);
        }
        else{
            Log.d(TAG, "onCreateViewHolder: 2");
            view=mInflater.inflate(R.layout.message_recyclerview_rows,viewGroup,false);
        }
        Log.d(TAG, "onCreateViewHolder: returned");
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        if(chat.get(i).getUserID().equals(userID)){
            viewHolder.userMessageRight.setText(chat.get(i).getUserMessage());
        }
        else{
            viewHolder.userName.setText(chat.get(i).getUserName());
            viewHolder.userMessage.setText(chat.get(i).getUserMessage());
        }
    }


    public void setAdapter(List<Chat> chat){
        this.chat=chat;
        notifyDataSetChanged();

    }

    @Override
    public int getItemCount() {
        return chat.size();
    }

    @Override
    public int getItemViewType(int position) {

        Log.d("item", "getItemViewType: ");
        if(chat.get(position).getUserID().equals(userID)){
            Log.d("item", "getItemViewType: returned 1 ");
            return 1;

        }

        else{
            Log.d(TAG, "getItemViewType: "+chat.get(position).getUserID()+" : "+userID);
            Log.d("item", "getItemViewType: returned 2 ");
            return 2;
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView userName;
        TextView userMessage;
        TextView userMessageRight;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            userName=itemView.findViewById(R.id.user_name);
            userMessage=itemView.findViewById(R.id.user_message);
            userMessageRight=itemView.findViewById(R.id.user_message_right);
        }

    }




}
