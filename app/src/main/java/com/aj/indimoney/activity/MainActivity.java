package com.aj.indimoney.activity;

import android.os.Bundle;
import android.os.Handler;
import androidx.annotation.NonNull;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.aj.indimoney.adapter.ViewPagerAdapter;
import com.aj.indimoney.fragment.HomeFragment;
import com.aj.indimoney.fragment.IncomeFragment;
import com.aj.indimoney.fragment.ExpenseFragment;
import com.aj.indimoney.R;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    public static MainActivity main;
    public ViewPager viewPager;
    public BottomNavigationView navigation;
    boolean doubleBackToExitPressedOnce = false;

    MenuItem prevMenuItem;
    IncomeFragment chatFragment;
    HomeFragment homeFragment;
    ExpenseFragment contactsFragment;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(MainActivity.this);
        navigation.getMenu().findItem(R.id.navigation_home).setChecked(true);
        main = this;

        viewPager = (ViewPager) findViewById(R.id.fragment_container);
        setupViewPager(viewPager);
        viewPager.setCurrentItem(1);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (prevMenuItem != null) {
                    prevMenuItem.setChecked(false);
                } else {
                    navigation.getMenu().getItem(0).setChecked(false);
                }
                Log.d("page", "onPageSelected: " + position);
                navigation.getMenu().getItem(position).setChecked(true);
                prevMenuItem = navigation.getMenu().getItem(position);

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Fragment fragment = null;
        ViewPager viewPager = (ViewPager) findViewById(R.id.fragment_container);
        BottomNavigationView navigation = findViewById(R.id.navigation);

        switch (item.getItemId()) {
            case R.id.navigation_home:
                setupViewPager(viewPager);
                viewPager.setCurrentItem(1);
                navigation.getMenu().findItem(R.id.navigation_home).setChecked(true);
                break;

            case R.id.navigation_income:
                setupViewPager(viewPager);
                viewPager.setCurrentItem(0);
                navigation.getMenu().findItem(R.id.navigation_income).setChecked(true);
                break;

            case R.id.navigation_expense:
                setupViewPager(viewPager);
                viewPager.setCurrentItem(2);
                navigation.getMenu().findItem(R.id.navigation_expense).setChecked(true);
                break;
        }
        return loadFragment(fragment);
    }

    private boolean loadFragment(Fragment fragment) {
        if (fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .commit();
            return true;
        }
        return false;
    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }
        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Click again to exit the application", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }

    public void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        chatFragment = new IncomeFragment();
        homeFragment = new HomeFragment();
        contactsFragment = new ExpenseFragment();
        adapter.addFragment(chatFragment);
        adapter.addFragment(homeFragment);
        adapter.addFragment(contactsFragment);
        viewPager.setAdapter(adapter);
    }
}
