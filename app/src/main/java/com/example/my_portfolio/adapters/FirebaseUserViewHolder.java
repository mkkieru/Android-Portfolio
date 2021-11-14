package com.example.my_portfolio.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.my_portfolio.R;
import com.example.my_portfolio.models.Posts;
import com.example.my_portfolio.models.User;
import com.example.my_portfolio.ui.MessageActivity;
import com.example.my_portfolio.ui.UserDetailActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.parceler.Parcels;

import java.util.ArrayList;

public class FirebaseUserViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    View mView;
    Context mContext;

    public FirebaseUserViewHolder (View itemView){
        super(itemView);
        mView = itemView;
        mContext = itemView.getContext();
        itemView.setOnClickListener(this);
    }
    public void bindUser(Posts user) {

        ImageView userImageView = (ImageView) mView.findViewById(R.id.userImageView);
        TextView userNameTextView = (TextView) mView.findViewById(R.id.userNameTextView);

        ImageView postImage = (ImageView) mView.findViewById(R.id.postImage);
        TextView caption = (TextView) mView.findViewById(R.id.postCaption);


        Picasso.get().load(user.getProfileImageUrl()).into(userImageView);
        Picasso.get().load(user.getPostImageUrl()).into(postImage);

        caption.setText(user.getCaption());
        userNameTextView.setText(user.getName());
    }

    public void bindChatUser(User user) {

        ImageView chatImageView = (ImageView) mView.findViewById(R.id.chatImageView);
        TextView chatUserName = (TextView) mView.findViewById(R.id.chatUserName);
        ImageView img_on = (ImageView) mView.findViewById(R.id.log_on);
        ImageView img_off = (ImageView) mView.findViewById(R.id.log_off);

        if (user.getStatus().equals("online")){
            img_on.setVisibility(View.VISIBLE);;
            img_off.setVisibility(View.GONE);;
        }
        if (user.getStatus().equals("offline")){
            img_on.setVisibility(View.GONE);;
            img_off.setVisibility(View.VISIBLE);;
        }

        Picasso.get().load(user.getImageUrl()).into(chatImageView);
        chatUserName.setText(user.getName());

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, MessageActivity.class);
                intent.putExtra("userid", user.getId());
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public void onClick(View v) {
        if (v == itemView){
            final ArrayList<User> users = new ArrayList<>();
            DatabaseReference ref = FirebaseDatabase.getInstance().getReference("USER");
            ref.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot shot : snapshot.getChildren()){
                        users.add(shot.getValue(User.class));
                    }
                    int itemPosition = getLayoutPosition();
                    Intent intent = new Intent(mContext, UserDetailActivity.class);
                    intent.putExtra("position", itemPosition);
                    intent.putExtra("users", Parcels.wrap(users));
                    mContext.startActivity(intent);

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }
}
