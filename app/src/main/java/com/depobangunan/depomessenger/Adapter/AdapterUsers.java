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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class AdapterUsers extends RecyclerView.Adapter<AdapterUsers.ViewHolder> {

    private Context context;
    private List<Users> listUser;
    private boolean isChat;

    String thelastMessage;

    public AdapterUsers(Context context, List<Users> listUser, boolean isChat) {
        this.context = context;
        this.listUser = listUser;
        this.isChat = isChat;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.adapter_users,parent,false);
        return new  AdapterUsers.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        final Users users = listUser.get(position);
        holder.textUser.setText(users.getUsername());
        if (users.getImageUrl().equals("default")){
            holder.imageprofile.setImageResource(R.drawable.profile_default);
        }else {
            Glide.with(context).load(users.getImageUrl()).into(holder.imageprofile);
        }

        if (isChat){
            lastMessage(users.getId(), holder.textLastMessage);
        }else {
            holder.textLastMessage.setVisibility(View.GONE);
        }

        if (isChat){
            if (users.getStatus().equals("Online")){
                holder.img_on.setVisibility(View.VISIBLE);
                holder.img_off.setVisibility(View.GONE);
            }else {
                holder.img_on.setVisibility(View.GONE);
                holder.img_off.setVisibility(View.VISIBLE);
            }
        }else {
            holder.img_on.setVisibility(View.GONE);
            holder.img_off.setVisibility(View.GONE);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, MessageActivity.class);
                intent.putExtra("userid", users.getId());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listUser.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView textUser;
        public ImageView imageprofile;

        private ImageView img_on, img_off;

        public TextView textLastMessage;

        public ViewHolder(View itemView) {
            super(itemView);

            textUser =itemView.findViewById(R.id.text_username);
            imageprofile = (ImageView) itemView.findViewById(R.id.profile_image);
            img_on = itemView.findViewById(R.id.img_on);
            img_off = itemView.findViewById(R.id.img_off);
            textLastMessage = itemView.findViewById(R.id.text_lastMessage);
        }
    }

    private void lastMessage(final String userId, final TextView lastmessage){
        thelastMessage = "default";
        final FirebaseUser fu = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Chats");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Chat chat = snapshot.getValue(Chat.class);
                    if (chat.getReceiver().equals(fu.getUid()) && chat.getSender().equals(userId)
                        || chat.getReceiver().equals(userId) && chat.getSender().equals(fu.getUid())){
                        thelastMessage = chat.getMessage();
                    }
                }

                switch (thelastMessage){
                    case "default":
                        lastmessage.setText("No Message");
                        break;
                    default:
                        lastmessage.setText(thelastMessage);
                        break;
                }
                thelastMessage = "default";
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
