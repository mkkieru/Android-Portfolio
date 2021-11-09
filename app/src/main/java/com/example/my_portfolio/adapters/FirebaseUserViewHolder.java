package com.example.my_portfolio.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.my_portfolio.R;
import com.example.my_portfolio.models.User;
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
    public void bindUser(User user) {
        ImageView userImageView = (ImageView) mView.findViewById(R.id.userImageView);
        TextView userNameTextView = (TextView) mView.findViewById(R.id.userNameTextView);
        TextView professionTextView = (TextView) mView.findViewById(R.id.professionTextView);

        TextView like = (TextView) mView.findViewById(R.id.likeIcon);
        TextView comment = (TextView) mView.findViewById(R.id.commentIcon);

        like.setOnClickListener(this);
        comment.setOnClickListener(this);


        Picasso.get().load(user.getImageUrl()).into(userImageView);

        userNameTextView.setText(user.getName());
        professionTextView.setText(user.getProfession());
    }

    @Override
    public void onClick(View v) {
        final ArrayList<User> users = new ArrayList<>();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("USER");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot shot : snapshot.getChildren()){
                    users.add(snapshot.getValue(User.class));
                }
                int itemPosition = getLayoutPosition();
                Intent intent = new Intent(mContext, UserDetailActivity.class);
                intent.putExtra("position", itemPosition+"");
                intent.putExtra("users", Parcels.wrap(users));
                mContext.startActivity(intent);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
