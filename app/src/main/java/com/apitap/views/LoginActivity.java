package com.apitap.views;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.apitap.R;
import com.apitap.controller.ModelManager;
import com.apitap.model.Operations;
import com.apitap.model.preferences.ATPreferences;
import com.apitap.views.fragments.FragmentLogin;
import com.apitap.views.fragments.FragmentSignup;
import com.github.rahatarmanahmed.cpv.CircularProgressView;
import com.google.firebase.FirebaseApp;

import java.util.ArrayList;
import java.util.List;

import fr.castorflex.android.smoothprogressbar.SmoothProgressBar;
import fr.castorflex.android.smoothprogressbar.SmoothProgressBarUtils;
import fr.castorflex.android.smoothprogressbar.SmoothProgressDrawable;

public class LoginActivity extends FragmentActivity implements View.OnClickListener {
    TabLayout mTabs;
    ViewPager mViewPager;
    Button btnGuest;
    public CircularProgressView mPocketBar;
    private static final String TAG = LoginActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initViews();
        ModelManager.getInstance().getLoginManager().getLogin(this, Operations.makeJsonDefaultParams(LoginActivity.this));
        setupViewPager();
    }

    private void initViews() {
        mTabs = (TabLayout) findViewById(R.id.custom_tabs);
        mViewPager = (ViewPager) findViewById(R.id.viewpager_home);
        btnGuest = (Button) findViewById(R.id.btnGuest);
        btnGuest.setOnClickListener(this);
        mPocketBar = (CircularProgressView) findViewById(R.id.pocket);
        ATPreferences.putBoolean(this,"header_store",false);

    }

    private void setupViewPager() {
        Adapter adapter = new Adapter(getSupportFragmentManager());
        adapter.addFragment(new FragmentLogin(), "Category 1");
        adapter.addFragment(new FragmentSignup(), "Category 2");
        mViewPager.setAdapter(adapter);
        mTabs.setupWithViewPager(mViewPager);
        mTabs.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                mViewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        setupTabs();
        mViewPager.setCurrentItem(0);

    }

    private void setupTabs() {
        ArrayList<String> tabs = new ArrayList<>();
        tabs.add("Log In");
        tabs.add("Sign Up");
        for (int i = 0; i < tabs.size(); i++) {

            TextView tabOne = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
            tabOne.setText(tabs.get(i));
            mTabs.getTabAt(i).setCustomView(tabOne);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnGuest:
                mPocketBar.setVisibility(View.VISIBLE);
                ModelManager.getInstance().getLoginManager().getLogin(this, Operations.makeJsonUserGuestLogin(LoginActivity.this));
                startActivity(new Intent(this, HomeActivity.class).putExtra("Guest","Login"));
                mPocketBar.setVisibility(View.GONE);

                break;
            default:
                break;
        }
    }


    static class Adapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragments = new ArrayList<>();
        private final List<String> mFragmentTitles = new ArrayList<>();

        public Adapter(android.support.v4.app.FragmentManager fm) {
            super(fm);
        }

        public void addFragment(Fragment fragment, String title) {
            mFragments.add(fragment);
            mFragmentTitles.add(title);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitles.get(position);
        }
    }
}
