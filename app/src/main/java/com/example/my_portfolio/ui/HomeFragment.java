package com.example.my_portfolio.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.my_portfolio.R;
import com.example.my_portfolio.adapters.FirebaseUserViewHolder;
import com.example.my_portfolio.adapters.userListAdapter;
import com.example.my_portfolio.models.User;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HomeFragment extends Fragment {

    private DatabaseReference mUserReference;
    private FirebaseRecyclerAdapter<User, FirebaseUserViewHolder> mFirebaseAdapter;
    @BindView(R.id.recyclerView) RecyclerView mRecyclerView;
    private userListAdapter mAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home, container,false);
        ButterKnife.bind(this,view);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference();

        Toast.makeText(getContext(), "Update profile to be visible to other users", Toast.LENGTH_SHORT).show();

        mUserReference = FirebaseDatabase.getInstance().getReference("USER");
        setUpFirebaseAdapter();
        return view;
    }

    private void setUpFirebaseAdapter(){
        FirebaseRecyclerOptions<User> options =
                new FirebaseRecyclerOptions.Builder<User>()
                        .setQuery(mUserReference, User.class)
                        .build();

        mFirebaseAdapter = new FirebaseRecyclerAdapter<User, FirebaseUserViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull FirebaseUserViewHolder firebaseRestaurantViewHolder, int position, @NonNull User user) {
                firebaseRestaurantViewHolder.bindUser(user);
            }

            @NonNull
            @Override
            public FirebaseUserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_list_item, parent, false);
                return new FirebaseUserViewHolder(view);
            }
        };

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setAdapter(mFirebaseAdapter);
    }

    @Override
    public void onStart() {
        super.onStart();
        mFirebaseAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        if(mFirebaseAdapter!= null) {
            mFirebaseAdapter.stopListening();
        }
    }
}
