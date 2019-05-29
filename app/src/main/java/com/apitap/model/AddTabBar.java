package com.apitap.model;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.apitap.R;
import com.apitap.model.preferences.ATPreferences;
import com.apitap.views.HomeActivity;
import com.apitap.views.NavigationMenu.FragmentDrawer;
import com.apitap.views.fragments.FragmentItems;
import com.apitap.views.fragments.FragmentSpecial;
import com.apitap.views.fragments.Fragment_Ads;

/**
 * Created by Shami on 20/2/2018.
 */

public class AddTabBar {

    private Toolbar mToolbar;
    private TabLayout tabLayout;
    private FragmentDrawer drawerFragment;
    private TextView tabOne, tabTwo, tabThree, tabFour, tabFive, tabSix;


    public static AddTabBar mInstance;

    public AddTabBar() {
        mInstance = this;
    }

    public static AddTabBar getmInstance() {
        return mInstance;
    }


    public void setUpDrawer(AppCompatActivity context, Toolbar mToolbar, FragmentDrawer drawerFragment) {
        this.drawerFragment = drawerFragment;
        this.mToolbar = mToolbar;
        context.getSupportActionBar().setDisplayShowTitleEnabled(false);

    }

    public void setupViewPager(TabLayout tabLayout) {

        tabLayout.addTab(tabLayout.newTab().setText("Home"));
        tabLayout.addTab(tabLayout.newTab().setText("Stores"));
        tabLayout.addTab(tabLayout.newTab().setText("Ads"));
        tabLayout.addTab(tabLayout.newTab().setText("Specials"));
        tabLayout.addTab(tabLayout.newTab().setText("Items"));
        tabLayout.addTab(tabLayout.newTab().setText("Favorites"));

    }

    public void setupTabIcons(TabLayout tabLayout, Context context, TextView tabOne, TextView tabTwo, TextView tabThree, TextView tabFour, TextView tabFive, TextView tabSix) {
        this.tabOne = tabOne;
        this.tabTwo = tabTwo;
        this.tabThree = tabThree;
        tabOne = (TextView) LayoutInflater.from(context).inflate(R.layout.custom_tab, null);
        tabOne.setText("  Home");
        tabOne.setTextColor(context.getResources().getColor(R.color.colorGrey1));
        tabOne.setTextSize(12);
        tabOne.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_icon_home, 0, 0, 0);
        tabLayout.getTabAt(0).setCustomView(tabOne);

        tabTwo = (TextView) LayoutInflater.from(context).inflate(R.layout.custom_tab, null);
        tabTwo.setText("  Stores");
        tabTwo.setTextColor(context.getResources().getColor(R.color.colorGrey1));
        tabTwo.setTextSize(12);
        tabTwo.setCompoundDrawablesWithIntrinsicBounds(R.drawable.store_ico_selctor, 0, 0, 0);
        tabLayout.getTabAt(1).setCustomView(tabTwo);

        tabThree = (TextView) LayoutInflater.from(context).inflate(R.layout.custom_tab, null);
        tabThree.setTextColor(context.getResources().getColor(R.color.colorGrey1));
        tabThree.setText("  Ads");
        tabThree.setTextSize(12);
        tabThree.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ads_selector, 0, 0, 0);
        tabLayout.getTabAt(2).setCustomView(tabThree);

        tabFour = (TextView) LayoutInflater.from(context).inflate(R.layout.custom_tab, null);
        tabFour.setText("  Specials");
        tabFour.setTextColor(context.getResources().getColor(R.color.colorGrey1));
        tabFour.setTextSize(12);
        tabFour.setCompoundDrawablesWithIntrinsicBounds(R.drawable.special_selector, 0, 0, 0);
        tabLayout.getTabAt(3).setCustomView(tabFour);

        tabFive = (TextView) LayoutInflater.from(context).inflate(R.layout.custom_tab, null);
        tabFive.setText("  Items");
        tabFive.setTextSize(12);
        tabFive.setTextColor(context.getResources().getColor(R.color.colorGrey1));
        tabFive.setCompoundDrawablesWithIntrinsicBounds(R.drawable.items_selector, 0, 0, 0);
        tabLayout.getTabAt(4).setCustomView(tabFive);

        tabSix = (TextView) LayoutInflater.from(context).inflate(R.layout.custom_tab, null);
        tabSix.setText("  Favorites");
        tabSix.setTextSize(12);
        tabSix.setTextColor(context.getResources().getColor(R.color.colorGrey1));
        tabSix.setCompoundDrawablesWithIntrinsicBounds(R.drawable.favorite_selector, 0, 0, 0);
        tabLayout.getTabAt(5).setCustomView(tabSix);
    }

    public void displayView(AppCompatActivity context, Fragment fragment, String tag, Bundle bundle, int id) {
        //  if (fragment != null) {
        FragmentManager fragmentManager = context.getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        if (bundle != null)
            fragment.setArguments(bundle);
        //  if (fragB == null) {
        fragmentTransaction.replace(id, fragment);
        if (fragment instanceof Fragment_Ads || fragment instanceof FragmentSpecial || fragment instanceof FragmentItems) {

        } else
            fragmentTransaction.addToBackStack(tag);
        fragmentTransaction.commit();
//            } else
//                fragmentTransaction.show(fragB);
        //  getSupportActionBar().setTitle(tag);
        // }
    }

    public void bindWidgetsWithAnEvent(final LinearLayout linearLayout, final TabLayout tabLayout, final AppCompatActivity activity, final int id) {
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                Log.d("tabSelect",tab.getPosition()+"  pos");

               // frameLayout.setVisibility(View.VISIBLE);
                setCurrentTabFragment(tabLayout, activity, tab.getPosition(), id);


            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                if (tab.getPosition()==0){
                    setCurrentTabFragment(tabLayout, activity, tab.getPosition(), id);
                    tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                        @Override
                        public void onTabSelected(TabLayout.Tab tab) {
                            setCurrentTabFragment(tabLayout, activity, tab.getPosition(), id);
                        }

                        @Override
                        public void onTabUnselected(TabLayout.Tab tab) {

                        }

                        @Override
                        public void onTabReselected(TabLayout.Tab tab) {

                        }
                    });
                }
            }
        });
    }

    public void setCurrentTabFragment(TabLayout tabLayout, AppCompatActivity activity, int tabPosition, int id) {
        switch (tabPosition) {

            case 0:
                ATPreferences.putBoolean(activity,"header_store",false);
                activity.startActivity(new Intent(activity, HomeActivity.class).putExtra("Tab", tabPosition));
                //displayView(activity, new FragmentHome(), "home", null, id);
                break;
            case 1:
                activity.startActivity(new Intent(activity, HomeActivity.class).putExtra("Tab", tabPosition));
               // displayView(activity, new Fragment_Store(), "Store", null, id);
                break;
            case 2:
                activity.startActivity(new Intent(activity, HomeActivity.class).putExtra("Tab", tabPosition));

                //displayView(activity, new Fragment_Ads(), "Ads", null, id);
                break;
            case 3:
                activity.startActivity(new Intent(activity, HomeActivity.class).putExtra("Tab", tabPosition));
                //displayView(activity, new FragmentSpecial(), "Specials", null, id);
                break;
            case 4:
                activity.startActivity(new Intent(activity, HomeActivity.class).putExtra("Tab", tabPosition));
                //displayView(activity, new FragmentItems(), "Items", null, id);
                break;
            case 5:
                activity.startActivity(new Intent(activity, HomeActivity.class).putExtra("Tab", tabPosition));
                // displayView(activity, new FragmentFavourite(), "Favorites", null, id);
                break;

        }
    }

}
