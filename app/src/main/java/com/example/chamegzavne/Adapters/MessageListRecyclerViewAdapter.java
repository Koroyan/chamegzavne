package com.example.chamegzavne.Adapters;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.chamegzavne.InfoClass.ChatList;
import com.example.chamegzavne.R;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MessageListRecyclerViewAdapter extends RecyclerView.Adapter<MessageListRecyclerViewAdapter.ViewHolder> {
    private LayoutInflater mInflater;
    List<ChatList> chatLists;
    OnChatItemClickListenr onChatItemClickListenr;

    public MessageListRecyclerViewAdapter(Context context,List<ChatList> chatLists){
        this.mInflater=LayoutInflater.from(context);
        this.chatLists=chatLists;
    }

    public void setAdapterList(List<ChatList> chatLists){
        this.chatLists=chatLists;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = mInflater.inflate(R.layout.message_list_recyclerview_rows,viewGroup,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        viewHolder.chatListName.setText(chatLists.get(i).getChatListName());
        viewHolder.chatListMessage.setText(chatLists.get(i).getChatListMessage());
        Picasso.get().load(chatLists.get(i).getChatListPhotoUri()).fit().centerCrop().into(viewHolder.messageListImage);
        Log.d("photos", "ID: "+chatLists.get(i).getChatListPhotoUri().toString());
        if(chatLists.get(i).getHasUnread().equals("true")){
            viewHolder.hasUnread.setText("    ");
        }
        else{
            viewHolder.hasUnread.setText("");
        }
    }

    @Override
    public int getItemCount() {
        return chatLists.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,View.OnLongClickListener{
        TextView chatListName;
        TextView chatListMessage;
        CircleImageView messageListImage;
        TextView hasUnread;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            chatListName=itemView.findViewById(R.id.chat_list_row_name_txt);
            chatListMessage=itemView.findViewById(R.id.chat_list_row_message_id);
            messageListImage=itemView.findViewById(R.id.chat_list_profile_image);
            hasUnread=itemView.findViewById(R.id.has_unread_id);
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if(onChatItemClickListenr!=null){
                onChatItemClickListenr.OnChatItemClick(v,chatLists.get(getAdapterPosition()));
            }


        }

        @Override
        public boolean onLongClick(View v) {

            if(onChatItemClickListenr!=null){
                onChatItemClickListenr.OnchatLongclick(v,chatLists.get(getAdapterPosition()));

            }
            return false;
        }
    }

    public interface OnChatItemClickListenr{
        public void OnChatItemClick(View view,ChatList chatlist);
        public void OnchatLongclick(View view,ChatList chatList);
    }

    public void setItemClickListener(OnChatItemClickListenr onChatItemClickListenr){
        this.onChatItemClickListenr=onChatItemClickListenr;
    }

}
