package com.apitap.views;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.apitap.R;
import com.apitap.controller.ModelManager;
import com.apitap.model.AddTabBar;
import com.apitap.model.Constants;
import com.apitap.model.GPSService;
import com.apitap.model.GpsLocation;
import com.apitap.model.Operations;
import com.apitap.model.Utils;
import com.apitap.model.preferences.ATPreferences;
import com.apitap.views.NavigationMenu.FragmentDrawer;
import com.apitap.views.fragments.FragmentFavourite;
import com.apitap.views.fragments.FragmentHistory;
import com.apitap.views.fragments.FragmentHome;
import com.apitap.views.fragments.FragmentItems;
import com.apitap.views.fragments.FragmentMessages;
import com.apitap.views.fragments.FragmentScanner;
import com.apitap.views.fragments.FragmentSettings;
import com.apitap.views.fragments.FragmentShoppingAsst;
import com.apitap.views.fragments.FragmentSpecial;
import com.apitap.views.fragments.Fragment_Ads;
import com.apitap.views.fragments.Fragment_Store;
import com.apitap.views.fragments.ShoppingCartFragment;
import com.crashlytics.android.Crashlytics;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;

import java.util.ArrayList;

import io.fabric.sdk.android.Fabric;

import static com.apitap.model.Utils.MY_PERMISSIONS_REQUEST_LOCATION;

/**
 * Created by apple on 10/08/16.
 */
public class HomeActivity extends AppCompatActivity implements FragmentDrawer.FragmentDrawerListener, View.OnClickListener {


    private TextView items;
    private Toolbar mToolbar;
    private FragmentDrawer drawerFragment;
    private TextView mTxtHeading;
    private static int toolint = 0;
    private ImageView mlogo;

    private LinearLayout llScan, llMessage, llSearch;
    private static final int ZXING_CAMERA_PERMISSION = 1;
    private Class<?> mClss;
    private static final int MY_CAMERA_REQUEST_CODE = 100;
    private Context context;
    String searchkey = "";
    public static TabLayout tabLayout;
    public static TabLayout tabLayout2;
    public static LinearLayout tabContainer2;
    public static LinearLayout tabContainer1;
    private boolean isGuest = false;
    private TextView tabOne, tabTwo, tabThree, tabFour, tabFive, tabSix;
    String locationSearch = "";
    private boolean isDrawerItemSelected = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.homepage_activity);

        context = this;

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        findToolbarViews();

        drawerFragment = (FragmentDrawer)
                getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
        drawerFragment.setUp(R.id.fragment_navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout), mToolbar);
        drawerFragment.setDrawerListener(this);

        getSupportActionBar().setDisplayShowTitleEnabled(false);

        if (getIntent() != null) {
            if (getIntent().hasExtra("Guest")) {
                Log.d("itsTrue", "guest");
                ATPreferences.putBoolean(this, "guest", true);
            } else if (getIntent().hasExtra("Drawer")) {
                isDrawerItemSelected = true;
                int position = getIntent().getIntExtra("Drawer", 99);
                drawerItemSelected(position);
            } else if (getIntent().hasExtra("Tab")) {
                int tabposition = getIntent().getIntExtra("Tab", 99);
                setCurrentTabFragment(tabposition);
            } else {
                ATPreferences.putBoolean(this, "guest", false);
            }
            isGuest = ATPreferences.readBoolean(this, "guest");
        }
        //getloc();
        if (!isDrawerItemSelected)
            displayView(new FragmentHome(), Constants.TAG_HOMEPAGE, new Bundle());

    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    private void findToolbarViews() {
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout2 = (TabLayout) findViewById(R.id.tabs2);
        tabContainer2 = (LinearLayout) findViewById(R.id.tab_container2);
        tabContainer1 = (LinearLayout) findViewById(R.id.tab_container);

        llScan = (LinearLayout) mToolbar.findViewById(R.id.ll_scan);
        llMessage = (LinearLayout) mToolbar.findViewById(R.id.ll_message);
        llSearch = (LinearLayout) mToolbar.findViewById(R.id.ll_search);
        mTxtHeading = (TextView) mToolbar.findViewById(R.id.txt_heading);
        mlogo = (ImageView) mToolbar.findViewById(R.id.img_logo);
        llScan.setOnClickListener(this);
        llMessage.setOnClickListener(this);
        llSearch.setOnClickListener(this);

        setupViewPager();
        setupTabIcons();
        bindWidgetsWithAnEvent();

        AddTabBar.getmInstance().setupViewPager(tabLayout2);
        AddTabBar.getmInstance().setupTabIcons(tabLayout2, context, tabOne, tabTwo, tabThree, tabFour, tabFive, tabSix);
        AddTabBar.getmInstance().bindWidgetsWithAnEvent(tabContainer2, tabLayout2, HomeActivity.this, R.id.container_body);

        tabContainer1.setVisibility(View.VISIBLE);
        tabContainer2.setVisibility(View.GONE);

    }

    private void bindWidgetsWithAnEvent() {
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getPosition() == 0) {
                    ATPreferences.putBoolean(context, "header_store", false);
                }
                setCurrentTabFragment(tab.getPosition());


            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {


            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
    }

    private void setCurrentTabFragment(int tabPosition) {
        switch (tabPosition) {
            case 0:
                displayView(new FragmentHome(), "home", null);
                break;
            case 1:
                displayView(new Fragment_Store(), "Store", null);
                break;
            case 2:
                displayView(new Fragment_Ads(), "Ads", null);
                break;
            case 3:
                displayView(new FragmentSpecial(), "Specials", null);
                break;
            case 4:
                displayView(new FragmentItems(), "Items", null);
                break;
            case 5:
                if (isGuest)
                    showGuestDialog();
                else
                    displayView(new FragmentFavourite(), "Favorites", null);
                break;

        }
    }

    private void setupTabIcons() {

        tabOne = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
        tabOne.setText("  Home");
        tabOne.setTextColor(getResources().getColor(R.color.colorGrey1));
        tabOne.setTextSize(12);
        tabOne.setCompoundDrawablesWithIntrinsicBounds(R.drawable.home_ico_selctor, 0, 0, 0);
        tabLayout.getTabAt(0).setCustomView(tabOne);

        tabTwo = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
        tabTwo.setText("  Stores");
        tabTwo.setTextColor(getResources().getColor(R.color.colorGrey1));
        tabTwo.setTextSize(12);
        tabTwo.setCompoundDrawablesWithIntrinsicBounds(R.drawable.store_ico_selctor, 0, 0, 0);
        tabLayout.getTabAt(1).setCustomView(tabTwo);

        tabThree = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
        tabThree.setTextColor(getResources().getColor(R.color.colorGrey1));
        tabThree.setText("  Ads");
        tabThree.setTextSize(12);
        tabThree.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ads_selector, 0, 0, 0);
        tabLayout.getTabAt(2).setCustomView(tabThree);

        tabFour = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
        tabFour.setText("  Specials");
        tabFour.setTextColor(getResources().getColor(R.color.colorGrey1));
        tabFour.setTextSize(12);
        tabFour.setCompoundDrawablesWithIntrinsicBounds(R.drawable.special_selector, 0, 0, 0);
        tabLayout.getTabAt(3).setCustomView(tabFour);

        tabFive = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
        tabFive.setText("  Items");
        tabFive.setTextSize(12);
        tabFive.setTextColor(getResources().getColor(R.color.colorGrey1));
        tabFive.setCompoundDrawablesWithIntrinsicBounds(R.drawable.items_selector, 0, 0, 0);
        tabLayout.getTabAt(4).setCustomView(tabFive);

        tabSix = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
        tabSix.setText("  Favorites");
        tabSix.setTextSize(12);
        tabSix.setTextColor(getResources().getColor(R.color.colorGrey1));
        tabSix.setCompoundDrawablesWithIntrinsicBounds(R.drawable.favorite_selector, 0, 0, 0);
        tabLayout.getTabAt(5).setCustomView(tabSix);
    }

    private void setupViewPager() {

        tabLayout.addTab(tabLayout.newTab().setText("Home"));
        tabLayout.addTab(tabLayout.newTab().setText("Stores"));
        tabLayout.addTab(tabLayout.newTab().setText("Ads"));
        tabLayout.addTab(tabLayout.newTab().setText("Specials"));
        tabLayout.addTab(tabLayout.newTab().setText("Items"));
        tabLayout.addTab(tabLayout.newTab().setText("Favorites"));

    }

    @Override
    public void onBackPressed() {
        if (getFragmentManager().getBackStackEntryCount() > 0) {
            getFragmentManager().popBackStack();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onDrawerItemSelected(View view, int position) {
        drawerItemSelected(position);
    }

    private void drawerItemSelected(int position) {
        if (position == 0) {
            //mTxtHeading.setText("Apitap");
            mlogo.setVisibility(View.VISIBLE);
            mTxtHeading.setVisibility(View.GONE);
            displayView(new FragmentHome(), Constants.TAG_HOMEPAGE, new Bundle());
            ATPreferences.putBoolean(context, "header_store", false);
            tabLayout.setScrollPosition(0, 0f, true);
            TabLayout.Tab tab = tabLayout.getTabAt(0);
            tab.select();
        } else if (position == 2) {
            mTxtHeading.setText("Apitap");
            mlogo.setVisibility(View.VISIBLE);
            mTxtHeading.setVisibility(View.GONE);
            tabLayout.setScrollPosition(2, 0f, true);
            displayView(new Fragment_Ads(), Constants.TAG_ADS, new Bundle());
        } else if (position == 3) {
            mTxtHeading.setText("Apitap");
            mlogo.setVisibility(View.VISIBLE);
            mTxtHeading.setVisibility(View.GONE);
            displayView(new FragmentSpecial(), Constants.TAG_SPECIAL, new Bundle());
            tabLayout.setScrollPosition(3, 0f, true);
        } else if (position == 4) {
            mTxtHeading.setText("Apitap");
            mlogo.setVisibility(View.VISIBLE);
            mTxtHeading.setVisibility(View.GONE);
            displayView(new FragmentItems(), Constants.TAG_ITEMS, new Bundle());
            tabLayout.setScrollPosition(4, 0f, true);
        } else if (position == 5) {
            toolint = 0;
            if (checkSelfPermission(Manifest.permission.CAMERA)
                    != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.CAMERA},
                        MY_CAMERA_REQUEST_CODE);
            } else {
                mlogo.setVisibility(View.VISIBLE);
                // mlogo.setVisibility(View.GONE);
                mTxtHeading.setVisibility(View.GONE);
                // tabLayout.setVisibility(View.GONE);
                displayView(new FragmentScanner(), Constants.TAG_SCANNER, new Bundle());
            }
        } else if (position == 6) {
            if (isGuest) {
                showGuestDialog();
            } else {
                mTxtHeading.setText("Carts");
                mlogo.setVisibility(View.VISIBLE);
                mTxtHeading.setVisibility(View.GONE);
                tabLayout.setVisibility(View.GONE);
                displayView(new ShoppingCartFragment(), Constants.TAG_SHOPPING, new Bundle());
            }
        } else if (position == 7) {
            if (isGuest) {
                showGuestDialog();
            } else {
                mlogo.setVisibility(View.VISIBLE);
                mTxtHeading.setVisibility(View.GONE);
                mTxtHeading.setText("Messages");
                // tabLayout.setVisibility(View.GONE);
                displayView(new FragmentMessages(), Constants.TAG_MESSAGEPAGE, new Bundle());
            }
        } else if (position == 8) {
            mlogo.setVisibility(View.VISIBLE);
            mTxtHeading.setVisibility(View.GONE);
            mTxtHeading.setText("Apitap");
            tabLayout.setVisibility(View.GONE);
            displayView(new FragmentShoppingAsst(), Constants.TAG_SHOPPING_ASST, new Bundle());
        } else if (position == 9) {
            if (isGuest) {
                showGuestDialog();
            } else {
                mTxtHeading.setText("Apitap");
                //  mlogo.setVisibility(View.VISIBLE);
                //mTxtHeading.setVisibility(View.GONE);
                tabLayout.setScrollPosition(5, 0f, true);
                displayView(new FragmentFavourite(), Constants.TAG_FAVOURITEPAGE, new Bundle());

            }
        } else if (position == 10) {
            if (isGuest) {
                showGuestDialog();
            } else {
                mlogo.setVisibility(View.VISIBLE);
                mTxtHeading.setVisibility(View.GONE);
                mTxtHeading.setText("History");
                tabLayout.setVisibility(View.GONE);
                displayView(new FragmentHistory(), Constants.TAG_HISTORYPAGE, new Bundle());
            }
        } else if (position == 11) {
            if (isGuest) {
                showGuestDialog();
            } else {
                mlogo.setVisibility(View.VISIBLE);
                mTxtHeading.setVisibility(View.GONE);
                mTxtHeading.setText("Settings");
                tabLayout.setVisibility(View.GONE);
                displayView(new FragmentSettings(), Constants.TAG_SETTINGSPAGE, new Bundle());
            }
        } else if (position == 12) {
            mlogo.setVisibility(View.GONE);
            mTxtHeading.setVisibility(View.VISIBLE);
            mTxtHeading.setText("Logout");
            startActivity(new Intent(this, LoginActivity.class));
            //Toast.makeText(this, "Coming Soon..", Toast.LENGTH_SHORT).show();
            //displayView(new FragmentSettings(), Constants.TAG_SETTINGSPAGE, new Bundle());
        } else if (position == 1) {
            mlogo.setVisibility(View.VISIBLE);
            mTxtHeading.setVisibility(View.GONE);
            displayView(new Fragment_Store(), Constants.TAG_STORESPAGE, new Bundle());
            tabLayout.setScrollPosition(1, 0f, true);
        }

    }

    public void displayView(Fragment fragment, String tag, Bundle bundle) {

        //  if (fragment != null) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        if (bundle != null)
            fragment.setArguments(bundle);
        //  if (fragB == null) {
        fragmentTransaction.replace(R.id.container_body, fragment);
        if (fragment instanceof Fragment_Ads || fragment instanceof FragmentSpecial || fragment instanceof FragmentItems || fragment instanceof Fragment_Store || fragment instanceof FragmentFavourite) {

        } else
            fragmentTransaction.addToBackStack(tag);
        fragmentTransaction.commit();
//            } else
//                fragmentTransaction.show(fragB);
        //  getSupportActionBar().setTitle(tag);
        // }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_message:
                if (isGuest) {
                    showGuestDialog();
                } else {
                    // tabLayout.setVisibility(View.GONE);
                    displayView(new FragmentMessages(), Constants.TAG_MESSAGEPAGE, null);
                }
                break;
            case R.id.ll_scan:
                toolint = 1;
                if (checkSelfPermission(Manifest.permission.CAMERA)
                        != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(new String[]{Manifest.permission.CAMERA},
                            MY_CAMERA_REQUEST_CODE);
                } else {
                    //tabLayout.setVisibility(View.GONE);
                    displayView(new FragmentScanner(), Constants.TAG_SCANNER, null);
                }

                break;
            case R.id.ll_search:
                showSearchDialog();

                break;

        }
    }

    public void showSearchDialog() {
        final ArrayList<String> list = ModelManager.getInstance().getHomeManager().listAddresses;

        final Dialog dialog = new Dialog(HomeActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.CENTER;
        //  GpsLocation gps = new GpsLocation(context);

        dialog.setContentView(R.layout.quick_search_test);


        Button submit = (Button) dialog.findViewById(R.id.submit);
        Button cancel = (Button) dialog.findViewById(R.id.cancel);
        final EditText search = (EditText) dialog.findViewById(R.id.search);
        Spinner spinner = (Spinner) dialog.findViewById(R.id.spinner);
        ArrayAdapter arrayAdapter = new ArrayAdapter(getApplicationContext(), R.layout.spinner_item, list);
        spinner.setAdapter(arrayAdapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                locationSearch = list.get(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchkey = search.getText().toString();
                startActivity(new Intent(HomeActivity.this, SearchItemActivity.class).putExtra("key", searchkey).putExtra("location", locationSearch));
                dialog.dismiss();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });


        dialog.show();
        dialog.getWindow().setAttributes(lp);
//        if (gps.canGetLocation()) {
//            add = getLocations();
//            if (!add.isEmpty()&&!list.contains(add))
//                list.add(0,add);
//        } else {
//            gps.showSettingsAlert(dialog);
//        }
        //getDialogView(dialog);
        //viewsVisibility(dialog);

    }


    public void showGuestDialog() {

        final Dialog dialog = new Dialog(HomeActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.CENTER;
        dialog.setContentView(R.layout.guest_login);

        Button login = (Button) dialog.findViewById(R.id.continueshoping);
        Button cancel = (Button) dialog.findViewById(R.id.cancel);


        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomeActivity.this, LoginActivity.class));
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });


        dialog.show();
        dialog.getWindow().setAttributes(lp);
        //getDialogView(dialog);
        //viewsVisibility(dialog);

    }

    public void getDialogView(Dialog dialog) {
        final EditText editText = (EditText) dialog.findViewById(R.id.edit_msg);
        final ImageView search = (ImageView) dialog.findViewById(R.id.search);

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String address = editText.getText().toString();
                startActivity(new Intent(HomeActivity.this, SearchItemActivity.class).putExtra("key", address));
//                ModelManager.getInstance().getSearchManager().searchAddresses(HomeActivity.this,
//                        Operations.makeJsonSearchAddress(HomeActivity.this, address), Constants.GET_ADDRESS_SUCCESS);
            }
        });
    }

    public String getLocations() {
        String address = "";
        GPSService mGPSService = new GPSService(context);
        mGPSService.getLocation();
        boolean b = Utils.checkLocationPermission(HomeActivity.this);
        if (!b) {

            // Here you can ask the user to try again, using return; for that
            Toast.makeText(context, "Your location is not available, please try again.", Toast.LENGTH_SHORT).show();
            return "";

            // Or you can continue without getting the location, remove the return; above and uncomment the line given below
            // address = "Location not available";
        } else {

            // Getting location co-ordinates
            double latitude = mGPSService.getLatitude();
            double longitude = mGPSService.getLongitude();


            //Toast.makeText(context, "Latitude:" + latitude + " | Longitude: " + longitude, Toast.LENGTH_LONG).show();


            address = mGPSService.getLocationAddress();

        }

        //Toast.makeText(context, "Your address is: " + address, Toast.LENGTH_SHORT).show();

        // make sure you close the gps after using it. Save user's battery power
        mGPSService.closeGPS();
        return address;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == MY_CAMERA_REQUEST_CODE) {

            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (toolint == 0) {

                    //  Toast.makeText(this, "Camera permission granted", Toast.LENGTH_LONG).show();
                    mTxtHeading.setText("Scan");
                    mlogo.setVisibility(View.VISIBLE);
                    mTxtHeading.setVisibility(View.GONE);
                    // tabLayout.setVisibility(View.GONE);
                    displayView(new FragmentScanner(), Constants.TAG_SCANNER, new Bundle());

                } else {
                    displayView(new FragmentScanner(), Constants.TAG_SCANNER, null);
                }
            } else {

                Toast.makeText(this, "Camera permission denied", Toast.LENGTH_LONG).show();

            }

        } else if (requestCode == MY_PERMISSIONS_REQUEST_LOCATION) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                // permission was granted, yay! Do the
                // location-related task you need to do.
                String address = getLocations();
                //items.setText(address);
                if (ContextCompat.checkSelfPermission(this,
                        Manifest.permission.ACCESS_FINE_LOCATION)
                        == PackageManager.PERMISSION_GRANTED) {

                    //Request location updates:

                }

            } else {

                // permission denied, boo! Disable the
                // functionality that depends on this permission.

            }
            return;


        }
    }

    private boolean getloc() {
        boolean isReturn = false;
        GpsLocation gps = new GpsLocation(context);

        if (gps.canGetLocation()) {
            isReturn = true;
        } else {
            gps.showSettingsAlertLocation();
        }
        return isReturn;
    }

}