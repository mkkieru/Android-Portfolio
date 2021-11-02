package com.example.my_portfolio;

import android.content.Intent;
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
    @BindView(R.id.userNumber) TextView mUserNumber;
    @BindView(R.id.userLocation) TextView mUserLocation;
    @BindView(R.id.editProfile) ImageView mEditProfile;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //Picasso.get().load(mProperty.getImageUri()).into(mPropertyView);
        View view = inflater.inflate(R.layout.fragment_profile, container,false);

        ButterKnife.bind(this,view);

        mEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), Personal_Details.class);
                startActivity(intent);
            }
        });

        mUserNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String number = mUserNumber.getText().toString();
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + number));
                startActivity(intent);
            }
        });

        mUserLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String label = "Cinnamon & Toast";
                String uriBegin = "geo:12,34";
                String query = "12,34(" + label + ")";
                String encodedQuery = Uri.encode(query);
                String uriString = uriBegin + "?q=" + encodedQuery;
                Uri uri = Uri.parse(uriString);
                Intent intent = new Intent(android.content.Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });


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
