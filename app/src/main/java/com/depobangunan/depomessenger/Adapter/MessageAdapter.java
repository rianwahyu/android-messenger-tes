package com.depobangunan.depomessenger.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.depobangunan.depomessenger.MessageActivity;
import com.depobangunan.depomessenger.Model.Chat;
import com.depobangunan.depomessenger.Model.Users;
import com.depobangunan.depomessenger.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {

    public static final int MSG_TYPE_LEFT = 0;
    public static final int MSG_TYPE_RIGHT = 1;

    private Context context;
    private List<Chat> mChat;
    private String imageUrl;

    FirebaseUser firebaseUser;

    public MessageAdapter(Context context, List<Chat> mChat, String imageUrl) {
        this.context = context;
        this.mChat = mChat;
        this.imageUrl = imageUrl;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == MSG_TYPE_RIGHT){
            View view = LayoutInflater.from(context).inflate(R.layout.chat_item_right,parent,false);
            return new  MessageAdapter.ViewHolder(view);
        }else {
            View view = LayoutInflater.from(context).inflate(R.layout.chat_item_left,parent,false);
            return new  MessageAdapter.ViewHolder(view);
        }

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Chat chat = mChat.get(position);

        holder.showMessage.setText(chat.getMessage());

        if (imageUrl.equals("default")){
            holder.imageprofile.setImageResource(R.drawable.profile_default);
        }else {
            Glide.with(context).load(imageUrl).into(holder.imageprofile);
        }

        if (position == mChat.size()-1){
            if (chat.isIsseen()){
                holder.textSeen.setText("Read");
            }else {
                holder.textSeen.setText("Delivered");
            }
        }else {
            holder.textSeen.setVisibility(View.GONE);
        }

    }

    @Override
    public int getItemCount() {
        return mChat.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView showMessage;
        public ImageView imageprofile;

        public TextView textSeen;
        public ViewHolder(View itemView) {
            super(itemView);

            showMessage =itemView.findViewById(R.id.text_showMessage);
            imageprofile = (ImageView) itemView.findViewById(R.id.profile_image);
            textSeen =itemView.findViewById(R.id.textSeen);
        }
    }

    @Override
    public int getItemViewType(int position) {
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (mChat.get(position).getSender().equals(firebaseUser.getUid())){
            return MSG_TYPE_RIGHT;
        }else {
            return MSG_TYPE_LEFT;
        }
    }
}
