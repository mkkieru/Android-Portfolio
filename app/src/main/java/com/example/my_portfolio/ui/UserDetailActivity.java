package com.example.my_portfolio.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import com.example.my_portfolio.R;
import com.example.my_portfolio.adapters.UserPagerAdapter;
import com.example.my_portfolio.models.User;

import org.parceler.Parcels;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class UserDetailActivity extends AppCompatActivity {

    @BindView(R.id.viewPager) ViewPager mViewPager;
    private UserPagerAdapter adapterViewPager;
    List<User> mUsers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_detail);
        ButterKnife.bind(this);

        mUsers = Parcels.unwrap(getIntent().getParcelableExtra("users"));
        int startingPosition = getIntent().getIntExtra("position", 0);

        adapterViewPager = new UserPagerAdapter(getSupportFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT, mUsers);
        mViewPager.setAdapter(adapterViewPager);
        mViewPager.setCurrentItem(startingPosition);
    }
}