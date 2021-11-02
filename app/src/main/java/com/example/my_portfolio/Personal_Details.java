package com.example.my_portfolio;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.ButtonBarLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import butterknife.BindView;
import butterknife.ButterKnife;

public class Personal_Details extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    @BindView(R.id.uploadDocumentButton) Button mUploadDocumentButton;
    @BindView(R.id.uploadDetailsButton) Button mUploadDetailsButton;

    @BindView(R.id.nameEditProfile) TextInputLayout mNameEditProfile;
    @BindView(R.id.emailEditProfile) TextInputLayout mEmailEditProfile;
    @BindView(R.id.PhoneNumberEditProfile) TextInputLayout mPhoneNumberEditProfile;
    @BindView(R.id.statusEditProfile) TextInputLayout mStatusEditProfile;
    @BindView(R.id.locationEditProfile) TextInputLayout mLocationEditProfile;
    @BindView(R.id.countryEditProfile) TextInputLayout mCuntryEditProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_details);
        ButterKnife.bind(this);

        mAuth = FirebaseAuth.getInstance();

        getSupportActionBar().setTitle("Edit Profile");


        mUploadDocumentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getSupportFragmentManager().beginTransaction()
                        .add(android.R.id.content, new ProfileFragment()).commit();

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
