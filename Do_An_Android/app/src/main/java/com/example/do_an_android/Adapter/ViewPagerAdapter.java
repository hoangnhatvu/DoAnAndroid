package com.example.do_an_android.Adapter;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.ArrayList;

public class ViewPagerAdapter extends FragmentPagerAdapter {
    ArrayList<Fragment> lstFragment;
    ArrayList<String> lstTitle;
    public ViewPagerAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
        lstFragment = new ArrayList<>();
        lstTitle = new ArrayList<>();
    }
    @NonNull
    @Override
    public Fragment getItem(int position) {
        return lstFragment.get(position);
    }
    public void addFragment(Fragment fm, String title) {
        lstFragment.add(fm);
        lstTitle.add(title);

    }
    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return lstTitle.get(position);
    }



    @Override
    public int getCount() {
        return lstTitle.size();
    }
}