package com.example.my_portfolio;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ProfileFragment extends Fragment {

    @BindView(R.id.profileImage) ImageView mProfileImage;
    @BindView(R.id.userName) TextView mUserName;
    @BindView(R.id.userMail) TextView mUserMail;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //Picasso.get().load(mProperty.getImageUri()).into(mPropertyView);
        View view = inflater.inflate(R.layout.fragment_profile, container,false);

        ButterKnife.bind(this,view);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String userId = user.getUid();
        DocumentReference df = FirebaseFirestore.getInstance().collection("users").document(userId);
        df.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                String imagePath = documentSnapshot.getString("Image");
                String email = documentSnapshot.getString("Email");
                String name = documentSnapshot.getString("Name");

                mUserName.setText(name);
                mUserMail.setText(email);

                Picasso.get().load(imagePath).into(mProfileImage);
            }
        });

        return  view;
    }
}
