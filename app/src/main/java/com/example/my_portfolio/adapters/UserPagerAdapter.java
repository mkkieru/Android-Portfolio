package com.example.my_portfolio.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.my_portfolio.models.User;
import com.example.my_portfolio.fragments.UserDetailFragment;

import java.util.List;

public class UserPagerAdapter extends FragmentPagerAdapter {

    private List<User> mUsers;

    public UserPagerAdapter(@NonNull FragmentManager fm, int behavior, List<User> users) {
        super(fm, behavior);
        mUsers = users;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return UserDetailFragment.newInstance(mUsers.get(position));
    }

    @Override
    public int getCount() {
        return mUsers.size();
    }
    @Override
    public CharSequence getPageTitle(int position) {
        return mUsers.get(position).getName();
    }
}
