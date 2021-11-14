package com.example.my_portfolio.fragments;

import static android.app.Activity.RESULT_OK;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.my_portfolio.R;
import com.example.my_portfolio.models.Posts;
import com.example.my_portfolio.models.model;
import com.example.my_portfolio.ui.CreateAccount;
import com.example.my_portfolio.ui.Personal_Details;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PostFragment extends Fragment {

    @BindView(R.id.userImageView) ImageView mProfileImage;
    @BindView(R.id.userNameTextView) TextView mUserName;

    @BindView(R.id.postCaption) EditText mPostCation;
    @BindView(R.id.postImageView) ImageView mPostImageView;
    @BindView(R.id.uploadPost) Button mUploadPost;

    private DatabaseReference root = FirebaseDatabase.getInstance().getReference("Image");
    private StorageReference reference = FirebaseStorage.getInstance().getReference();


    private String profileImagePath;
    private String postImagePath;
    private String name;
    private Uri imageUri;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_post, container,false);

        ButterKnife.bind(this,view);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        String userId = user.getUid();

        db.collection("users").document(user.getUid()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                profileImagePath = documentSnapshot.getString("Image Path");
                name = documentSnapshot.getString("Name");
            }
        });
        Picasso.get().load(profileImagePath).into(mProfileImage);
        mUserName.setText(name);

        mUploadPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String postCaption = mPostCation.getText().toString();
                Posts newPost = new Posts(userId,name,postCaption,profileImagePath,postImagePath);

                DatabaseReference ref = FirebaseDatabase
                        .getInstance()
                        .getReference("POSTS");
                ref.push().setValue(newPost);


                getFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new HomeFragment()).commit();
            }
        });

        mPostImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryIntent = new Intent();
                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent, 1);
            }
        });
        return  view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            mPostImageView.setImageURI(imageUri);
            uploadPicture(imageUri);
        }
    }
    private void uploadPicture(Uri uri) {

        if (imageUri == null) {
            Toast.makeText(getContext(), "Please select image", Toast.LENGTH_SHORT).show();
        } else {
            final ProgressDialog pd = new ProgressDialog(getContext());
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
                            postImagePath = uri.toString();
                            model model = new model(uri.toString());
                            String modelId = root.push().getKey();
                            root.child(modelId).setValue(model);
                            Toast.makeText(getContext(), "Uploaded successfully", Toast.LENGTH_SHORT).show();
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
        ContentResolver cr = getActivity().getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cr.getType(mUri));
    }
}
