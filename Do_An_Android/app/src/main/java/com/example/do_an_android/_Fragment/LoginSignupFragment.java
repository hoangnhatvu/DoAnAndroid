package com.example.do_an_android._Fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.do_an_android.Activity.MainActivity;
import com.example.do_an_android.Adapter.ViewPagerAdapter;
import com.example.do_an_android.R;
import com.google.android.material.tabs.TabLayout;


public class LoginSignupFragment extends Fragment {
    Context context;
    TabLayout tabLayout;
    ViewPager viewPager;
    ViewPagerAdapter viewpagerAdapter;
    SharedPreferences sharedPreferencesUser;
    TextView titleLoginSignup;
    Boolean checkPayOrder=false;
    ImageView backLoginSignup;
    public LoginSignupFragment(Context context) {
        this.context = context;
    }
    public LoginSignupFragment(Context context,Boolean checkPayOrder) {
        this.context = context;
        this.checkPayOrder = checkPayOrder;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return  inflater.inflate(R.layout.fragment_login_signup, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setControl(view);
        sharedPreferencesUser = context.getSharedPreferences("user", Context.MODE_PRIVATE);
       setAdapterViewPager();
        setClick();

    }

    private void setClick() {
        backLoginSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(context, MainActivity.class));
            }
        });
    }

    private void setAdapterViewPager() {
        viewpagerAdapter = new ViewPagerAdapter(getChildFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        tabLayout.setupWithViewPager(viewPager);
        if(sharedPreferencesUser.getString("username","fail").equals("fail")) {
            if(!checkPayOrder)
                viewpagerAdapter.addFragment(new LoginTabFragment(context), "Đăng nhập");
            else
                viewpagerAdapter.addFragment(new LoginTabFragment(context,checkPayOrder), "Đăng nhập");
            viewpagerAdapter.addFragment(new SignupTabFragment(getContext()), "Đăng kí");
        }
        else
        {
            titleLoginSignup.setText("Thông tin tài khoản");
            viewpagerAdapter.addFragment(new UpdateCustomerFragment(getContext()), "Tài khoản");
            viewpagerAdapter.addFragment(new OrderOfCustomerFragment(getContext()), "Đơn hàng");
        }
        viewPager.setAdapter(viewpagerAdapter);
    }

    private void setControl(View view) {
        viewPager = view.findViewById(R.id.view_pager);
        tabLayout = view.findViewById(R.id.tab_layout);
        titleLoginSignup = view.findViewById(R.id.titleLoginSignup);
        backLoginSignup = view.findViewById(R.id.backLoginSignup);
    }
}