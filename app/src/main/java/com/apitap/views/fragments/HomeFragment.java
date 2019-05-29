package com.apitap.views.fragments;

/**
 * Created by Ravi on 29/07/15.
 */

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.apitap.R;
import com.apitap.controller.ModelManager;
import com.apitap.model.Constants;
import com.apitap.model.Operations;
import com.apitap.model.customclasses.Event;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import fr.castorflex.android.smoothprogressbar.SmoothProgressBar;
import fr.castorflex.android.smoothprogressbar.SmoothProgressBarUtils;
import fr.castorflex.android.smoothprogressbar.SmoothProgressDrawable;

public class HomeFragment extends Fragment {

    TabLayout mTabs;
    ViewPager mViewPager;
    SmoothProgressBar mPocketBar;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews(view);
        mPocketBar.progressiveStart();
        //ModelManager.getInstance().getHomeManager().getHome(getContext(), Operations.makeJsonGetAds(getActivity()));
    }

    private void initViews(View v) {
        mTabs = (TabLayout) v.findViewById(R.id.custom_tabs);
        mViewPager = (ViewPager) v.findViewById(R.id.viewpager_home1);

        mPocketBar = (SmoothProgressBar)v. findViewById(R.id.pocket);

        mPocketBar.setSmoothProgressDrawableBackgroundDrawable(
                SmoothProgressBarUtils.generateDrawableWithColors(
                        getResources().getIntArray(R.array.pocket_background_colors),
                        ((SmoothProgressDrawable) mPocketBar.getIndeterminateDrawable()).getStrokeWidth()));
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe
    public void onEvent(Event event) {
        switch (event.getKey()) {
            case Constants.ALL_IMAGES_SUCCESS:
                setupViewPager();
                mPocketBar.progressiveStop();
                mPocketBar.setVisibility(View.INVISIBLE);
                break;
        }
    }

    private void setupViewPager() {
//        Adapter adapter = new Adapter(getFragmentManager());
//        adapter.addFragment(new FragmentSpecial(), "Category 1");
//        adapter.addFragment(new FragmentItems(), "Category 2");
//        adapter.addFragment(new FragmentSignup(), "Category 3");
//        mViewPager.setAdapter(adapter);
//        mTabs.setupWithViewPager(mViewPager);
//        mTabs.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
//            @Override
//            public void onTabSelected(TabLayout.Tab tab) {
//                mViewPager.setCurrentItem(tab.getPosition());
//            }
//
//            @Override
//            public void onTabUnselected(TabLayout.Tab tab) {
//            }
//
//            @Override
//            public void onTabReselected(TabLayout.Tab tab) {
//
//            }
//        });
//        setupTabs();
//        mViewPager.setCurrentItem(0);
    }

    private void setupTabs() {
        ArrayList<String> tabs = new ArrayList<>();
        tabs.add("SPECIAL");
        tabs.add("ITEMS");
        tabs.add("SAVED");
        for (int i = 0; i < tabs.size(); i++) {

            TextView tabOne = (TextView) LayoutInflater.from(getContext()).inflate(R.layout.custom_tab, null);
            tabOne.setTextColor(Color.BLACK);
            tabOne.setText(tabs.get(i));
            mTabs.getTabAt(i).setCustomView(tabOne);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
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
