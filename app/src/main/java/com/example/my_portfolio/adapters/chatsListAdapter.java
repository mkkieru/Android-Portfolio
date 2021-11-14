package com.example.my_portfolio.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.my_portfolio.R;
import com.example.my_portfolio.models.User;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class chatsListAdapter extends RecyclerView.Adapter<chatsListAdapter.userViewHolder> {

    private List<User> users;
    private Context context;

    public chatsListAdapter(List<User> users, Context context) {
        this.users = users;
        this.context = context;
    }

    @NonNull
    @Override
    public chatsListAdapter.userViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_user_list_item,parent,false);
        userViewHolder viewHolder = new userViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull chatsListAdapter.userViewHolder holder, int position) {
        holder.bindUser(users.get(position));
        final User user = users.get(position);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Intent intent = new Intent(context, MessageActivity.class);
                intent.putExtra("userid", user.getId());
                context.startActivity(intent);*/

            }
        });
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public class userViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.userImageView) ImageView mUserImageView;
        @BindView(R.id.userNameTextView) TextView mUserNameTextView;


        public Context mContext;

        public userViewHolder(View itemView){
            super(itemView);
            ButterKnife.bind(this, itemView);
            mContext = itemView.getContext();
        }
        public void bindUser(User user){
        }

    }
}
