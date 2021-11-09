package com.example.my_portfolio.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.my_portfolio.R;
import com.example.my_portfolio.models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class Personal_Details extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    @BindView(R.id.uploadDocumentButton) Button mUploadDocumentButton;
    @BindView(R.id.uploadDetailsButton) Button mUploadDetailsButton;

    @BindView(R.id.profileNameTextInputLayout) TextInputLayout mNameEditProfile;
    @BindView(R.id.profileEmailTextInputLayout) TextInputLayout mEmailEditProfile;
    @BindView(R.id.profilePhoneNumberTextInputLayout) TextInputLayout mPhoneNumberEditProfile;
    @BindView(R.id.profileStatusTextInputLayout) TextInputLayout mStatusEditProfile;
    @BindView(R.id.profileLocationTextInputLayout) TextInputLayout mLocationEditProfile;
    @BindView(R.id.profileCountryTextInputLayout) TextInputLayout mCountryEditProfile;

    private String imagePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_details);
        ButterKnife.bind(this);

        mAuth = FirebaseAuth.getInstance();

        getSupportActionBar().setTitle("Edit Profile");

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseFirestore db = FirebaseFirestore.getInstance();


        db.collection("users").document(user.getUid()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                imagePath = documentSnapshot.getString("Image Path");
            }
        });


        mUploadDocumentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getSupportFragmentManager().beginTransaction()
                        .add(android.R.id.content, new ProfileFragment()).commit();

            }
        });

        mUploadDetailsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String name = mNameEditProfile.getEditText().getText().toString().trim();
                final String email = mEmailEditProfile.getEditText().getText().toString().trim();
                String PhoneNumber = mPhoneNumberEditProfile.getEditText().getText().toString().trim();
                String Status = mStatusEditProfile.getEditText().getText().toString().trim();
                String Location = mLocationEditProfile.getEditText().getText().toString().trim();
                String Country = mCountryEditProfile.getEditText().getText().toString().trim();

                db.collection("users").document(user.getUid()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        imagePath = documentSnapshot.getString("Image Path");
                    }
                });

                Map<String, Object> NewUser = new HashMap<>();

                NewUser.put("Name", name);
                NewUser.put("Email", email);
                NewUser.put("PhoneNumber", PhoneNumber);
                NewUser.put("Status", Status);
                NewUser.put("Location", Location);
                NewUser.put("Country", Country);
                NewUser.put("Image Path", imagePath);

                db.collection("users")
                        .document(user.getUid())
                        .set(NewUser);

                User user1 = new User(name,imagePath,"Not Selected",PhoneNumber);

                DatabaseReference restaurantRef = FirebaseDatabase
                        .getInstance()
                        .getReference("USER");
                restaurantRef.push().setValue(user1);

                getSupportFragmentManager().beginTransaction()
                        .add(android.R.id.content, new ProfileFragment()).commit();


                UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                        .setDisplayName(name)
                        .build();
                user.updateProfile(profileUpdates).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(Personal_Details.this,"Name updated successfully",Toast.LENGTH_LONG).show();
                        }else
                            Toast.makeText(Personal_Details.this,"Name update Failed",Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_logout) {
            logout();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void logout() {
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(this, Login.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}
