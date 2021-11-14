package com.example.my_portfolio.ui;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.my_portfolio.R;
import com.example.my_portfolio.models.model;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CreateAccount extends AppCompatActivity implements View.OnClickListener {
    @BindView(R.id.profileImage)
    ImageView mProfileImage;
    @BindView(R.id.createUserButton)
    Button mCreateUserButton;
    @BindView(R.id.loginTextView)
    TextView mLoginTextView;
    public static final String TAG = CreateAccount.class.getSimpleName();

    TextInputLayout mNameEditText, mEmailEditText, mPasswordEditText, mConfirmPasswordEditText;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseStorage storage;
    private StorageReference storageReference;
    private Uri imageUri;

    private FirebaseFirestore db;
    private CollectionReference ref;
    private DatabaseReference root = FirebaseDatabase.getInstance().getReference("Image");
    private StorageReference reference = FirebaseStorage.getInstance().getReference();

    private String mName;
    private String imagePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);
        ButterKnife.bind(this);

        //mLoginTextView.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);

        mNameEditText = findViewById(R.id.nameTextInputLayout);
        mEmailEditText = findViewById(R.id.emailTextInputLayout);
        mPasswordEditText = findViewById(R.id.PasswordTextInputLayout);
        mConfirmPasswordEditText = findViewById(R.id.confirmPasswordTextInputLayout);

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        createAuthStateListener();

        mAuth = FirebaseAuth.getInstance();

        mCreateUserButton.setOnClickListener(this);
        mLoginTextView.setOnClickListener(this);
        mProfileImage.setOnClickListener(this);

    }

    public void createNewUser() {
        mName = mNameEditText.getEditText().getText().toString().trim();
        final String name = mNameEditText.getEditText().getText().toString().trim();
        final String email = mEmailEditText.getEditText().getText().toString().trim();
        String password = mPasswordEditText.getEditText().getText().toString().trim();
        String confirmPassword = mConfirmPasswordEditText.getEditText().getText().toString().trim();

        boolean validEmail = isValidEmail(email);
        boolean validName = isValidName(name);
        boolean validmName = isValidName(mName);
        boolean validPassword = isValidPassword(password, confirmPassword);
        if (!validEmail || !validName || !validPassword) return;

        if (imageUri == null) {
            Toast.makeText(this, "Select Image", Toast.LENGTH_SHORT).show();
        } else {
            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, task -> {
                        if (task.isSuccessful()) {

                            createFirebaseUserProfile(Objects.requireNonNull(task.getResult().getUser()));

                            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                            FirebaseFirestore db = FirebaseFirestore.getInstance();

                            Map<String, Object> NewUser = new HashMap<>();
                            NewUser.put("Name", name);
                            NewUser.put("Email", email);
                            NewUser.put("Image Path", imagePath);

                            db.collection("users")
                                    .document(user.getUid())
                                    .set(NewUser);
                            Toast.makeText(this, "Account created", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(this, "Account creation Failed", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }

    public void createAuthStateListener() {
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                final FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    Intent intent = new Intent(CreateAccount.this, Personal_Details.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                }
            }
        };
    }

    @Override
    public void onClick(View v) {
        if (v == mCreateUserButton) {
            createNewUser();
        }
        if (v == mLoginTextView) {
            Intent intent = new Intent(CreateAccount.this, Login.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        }
        if (v == mProfileImage) {
            Intent galleryIntent = new Intent();
            galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
            galleryIntent.setType("image/*");
            startActivityForResult(galleryIntent, 1);
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            mProfileImage.setImageURI(imageUri);
            uploadPicture(imageUri);
        }
    }

    private void uploadPicture(Uri uri) {

        if (imageUri == null) {
            Toast.makeText(this, "Please select image", Toast.LENGTH_SHORT).show();
        } else {
            final ProgressDialog pd = new ProgressDialog(this);
            pd.setTitle("Uploading image... ");
            pd.show();

            StorageReference fileRef = reference.child((System.currentTimeMillis() + "." + getFileExtension(uri)));
            fileRef.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    pd.dismiss();
                    fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {

                            imagePath = uri.toString();
                            model model = new model(uri.toString());
                            String modelId = root.push().getKey();
                            root.child(modelId).setValue(model);

                            Toast.makeText(CreateAccount.this, "Uploaded successfully", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                    double progressPercentage = (100.00 * snapshot.getBytesTransferred() / snapshot.getTotalByteCount());
                    pd.setMessage("Percentage: " + (int)  progressPercentage + "%");
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                }
            });


        }

    }

    private String getFileExtension(Uri mUri) {
        ContentResolver cr = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cr.getType(mUri));
    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    private boolean isValidEmail(String email) {
        boolean isGoodEmail =
                (email != null && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches());
        if (!isGoodEmail) {
            mEmailEditText.setError("Please enter a valid email address");
            return false;
        }
        return isGoodEmail;
    }

    private boolean isValidName(String name) {
        if (name.equals("")) {
            mNameEditText.setError("Please enter your name");
            return false;
        }
        return true;
    }

    private boolean isValidPassword(String password, String confirmPassword) {
        if (password.length() < 6) {
            mPasswordEditText.setError("Please create a password containing at least 6 characters");
            return false;
        } else if (!password.equals(confirmPassword)) {
            mPasswordEditText.setError("Passwords do not match");
            return false;
        }
        return true;
    }

    private void createFirebaseUserProfile(final FirebaseUser user) {

        UserProfileChangeRequest addProfileName = new UserProfileChangeRequest.Builder()
                .setDisplayName(mName)
                .build();

        user.updateProfile(addProfileName)
                .addOnCompleteListener(new OnCompleteListener<Void>() {

                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            //Log.d(TAG, Objects.requireNonNull(user.getDisplayName()));
                            Toast.makeText(CreateAccount.this, "The display name has ben set", Toast.LENGTH_LONG).show();
                        }
                    }

                });
    }
}