package com.example.do_an_android.Activity;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.splashscreen.SplashScreen;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.example.do_an_android._Fragment.CartFragment;
import com.example.do_an_android._Fragment.HomeFragment;
import com.example.do_an_android._Fragment.LoginSignupFragment;
import com.example.do_an_android.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class MainActivity extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;
    Fragment fm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SplashScreen splashScreen = SplashScreen.installSplashScreen(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setControl();
        boolean checkBuyNow = getIntent().getBooleanExtra("checkBuyNow", false);
        setOnClickBottomView();
        if (checkBuyNow)
            fm = new CartFragment(this);
        else {
            boolean checkPayCart = getIntent().getBooleanExtra("checkPayCart", false);
            if (checkPayCart)
                fm = new LoginSignupFragment(this, checkPayCart);
            else {
                boolean checkChangpass = getIntent().getBooleanExtra("checkChangpass", false);
                if (checkChangpass)
                    fm = new LoginSignupFragment(this);
                else
                    fm = new HomeFragment(this);
            }
        }

        loadFragment(fm);

    }

    private void setOnClickBottomView() {
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                switch (id) {
                    case R.id.mn_home:
                        fm = new HomeFragment(getApplicationContext());
                        break;
                    case R.id.mn_cart:
                        fm = new CartFragment(getApplicationContext());
                        break;
                    case R.id.mn_account:
                        fm = new LoginSignupFragment(getApplicationContext());
                        break;
                    default:
                        fm = new CartFragment(getApplicationContext());
                }
                loadFragment(fm);
                return true;
            }
        });
    }

    public void loadFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.framehost, fragment);
        fragmentTransaction.commit();
    }

    private void setControl() {
        bottomNavigationView = findViewById(R.id.bottomnavigation);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_bottom, menu);
        return true;
    }
}