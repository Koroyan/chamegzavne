package com.example.chamegzavne.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.chamegzavne.InfoClass.ChatList;
import com.example.chamegzavne.R;

import java.util.List;

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
    }

    @Override
    public int getItemCount() {
        return chatLists.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,View.OnLongClickListener{
        TextView chatListName;
        TextView chatListMessage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            chatListName=itemView.findViewById(R.id.chat_list_row_name_txt);
            chatListMessage=itemView.findViewById(R.id.chat_list_row_message_id);
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
