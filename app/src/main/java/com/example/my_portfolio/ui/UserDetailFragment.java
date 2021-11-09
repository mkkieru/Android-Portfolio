package com.example.my_portfolio.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.my_portfolio.R;
import com.example.my_portfolio.models.User;
import com.squareup.picasso.Picasso;

import org.parceler.Parcels;

import butterknife.BindView;
import butterknife.ButterKnife;

public class UserDetailFragment extends Fragment implements View.OnClickListener{

    @BindView(R.id.userFragImage) ImageView mImageLabel;
    @BindView(R.id.userNameFrag) TextView mNameLabel;
    @BindView(R.id.websiteFrag) TextView mWebsiteLabel;
    @BindView(R.id.phoneFrag) TextView mPhoneLabel;
    @BindView(R.id.addressFrag) TextView mAddressLabel;

    private User mUser;

    public UserDetailFragment() {
        // Required empty public constructor
    }

    public static UserDetailFragment newInstance(User user) {
        UserDetailFragment fragment = new UserDetailFragment();
        Bundle args = new Bundle();
        args.putParcelable("user", Parcels.wrap(user));
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        assert getArguments() != null;
        mUser = Parcels.unwrap(getArguments().getParcelable("user"));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_user_detail, container, false);
        ButterKnife.bind(this, view);

        Picasso.get().load(mUser.getImageUrl()).into(mImageLabel);

        mNameLabel.setText(mUser.getName());
        mPhoneLabel.setText(mUser.getPhone());

        mWebsiteLabel.setOnClickListener(this);
        mPhoneLabel.setOnClickListener(this);
        mAddressLabel.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v) {
        if (v == mWebsiteLabel) {
            //Intent webIntent = new Intent (Intent.ACTION_VIEW, Uri.parse(mUser.getUrl()));
            //startActivity(webIntent);
        }
        if (v == mPhoneLabel) {
            Intent phoneIntent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + mUser.getPhone()));
            startActivity(phoneIntent);
        }
        if (v == mAddressLabel) {
            /*Intent mapIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("geo:" + mRestaurant.getCoordinates().getLatitude()
                    + "," + mRestaurant.getCoordinates().getLongitude()
                    + "?q=(" + mRestaurant.getName() + ")"));
            startActivity(mapIntent);*/
        }
    }
}