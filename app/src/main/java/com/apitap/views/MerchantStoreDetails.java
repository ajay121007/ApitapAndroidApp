package com.apitap.views;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
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
import com.apitap.model.ViewPagerCustomDuration;
import com.apitap.model.bean.AdsDetailWithMerchant;
import com.apitap.model.bean.LocationListBean;
import com.apitap.model.bean.MerchantDetailBean;
import com.apitap.model.bean.RatingBean;
import com.apitap.model.customclasses.Event;
import com.apitap.model.preferences.ATPreferences;
import com.apitap.views.NavigationMenu.FragmentDrawer;
import com.apitap.views.adapters.LocationListAdpater;
import com.apitap.views.adapters.SamplePagerAdapter;
import com.apitap.views.fragments.FragmentItems;
import com.apitap.views.fragments.FragmentMessages;
import com.apitap.views.fragments.FragmentScanner;
import com.apitap.views.fragments.FragmentSpecial;
import com.apitap.views.fragments.Fragment_Ads;
import com.github.rahatarmanahmed.cpv.CircularProgressView;
import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import fr.castorflex.android.smoothprogressbar.SmoothProgressBar;
import me.relex.circleindicator.CircleIndicator;

import static com.apitap.model.Utils.MY_PERMISSIONS_REQUEST_LOCATION;

/**
 * Created by Shami on 20/9/2017.
 */

public class MerchantStoreDetails extends AppCompatActivity implements View.OnClickListener, FragmentDrawer.FragmentDrawerListener, LocationListAdpater.AdapterClick {

    private TextView storeName, storeDetails, showAll, ratingNo;
    private ImageView inbox, favourite, share, img_main, storeImg;
    private RecyclerView recyclerView;
    ViewPagerCustomDuration viewPager;
    CircleIndicator circleIndicator;
    private RatingBar ratingBar;
    private FragmentDrawer drawerFragment;
    private Toolbar mToolbar;
    private String merchantId, image_str, merchantName, searchkey;
    private MerchantDetailBean.RESULT.DetailData data;
    private LinearLayout giveRating, ratingNumbers;
    private Activity mActivity;
    private static int toolint = 0;
    FrameLayout frameLayout;
    SmoothProgressBar smoothProgressBar;
    Uri uri;
    CardView noAdsCard, adsCard;
    Button storeBrowse;
    LocationListAdpater.AdapterClick adapterClick;
    LinearLayout scan_tool;
    LinearLayout msg_tool;
    LinearLayout search_tool;
    LinearLayout back_tool;
    private static final int MY_CAMERA_REQUEST_CODE = 100;
    LocationListAdpater locationListAdpater;
    CircularProgressView mPocketBar;
    CardView nolocationTxt, nolocationLinear;
    LinearLayout browseStore, header;
    ImageView iv_back;
    ImageView noStoreLogo;
    int position = 0;
    String current_lat = "", current_long = "";
    public static TabLayout tabLayout;
    private TextView tabOne, tabTwo, tabThree, tabFour, tabFive, tabSix;
    LinearLayout tabConatiner;
    boolean isFavorite=false;


    @Override
    protected void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        setContentView(R.layout.store_review_screen);
        mActivity = this;
        adapterClick = this;
        if (getIntent() != null) {
            merchantId = getIntent().getStringExtra("merchantId");
            Log.d("MerchantID", merchantId + "   s");
        }
        getloc();
        initViews();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (frameLayout.getVisibility() == View.VISIBLE)
            frameLayout.setVisibility(View.GONE);
        else
            finish();
    }

    private void initViews() {

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        drawerFragment = (FragmentDrawer)
                getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
        drawerFragment.setUp(R.id.fragment_navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout), mToolbar);
        drawerFragment.setDrawerListener(this);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        tabConatiner = (LinearLayout) findViewById(R.id.tab_container);
        noAdsCard = (CardView) findViewById(R.id.noAdsll);
        adsCard = (CardView) findViewById(R.id.adsCard);
        storeBrowse = (Button) findViewById(R.id.details_store);
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        viewPager = (ViewPagerCustomDuration) findViewById(R.id.viewpager);
        iv_back = (ImageView) findViewById(R.id.tv_back);
        circleIndicator = (CircleIndicator) findViewById(R.id.indicator_default);
        frameLayout = (FrameLayout) findViewById(R.id.container_body);
        frameLayout.setVisibility(View.GONE);
        storeName = (TextView) findViewById(R.id.tv_view_store);
        storeDetails = (TextView) findViewById(R.id.storeDetails);
        showAll = (TextView) findViewById(R.id.showall);
        ratingNo = (TextView) findViewById(R.id.ratingNo);
        inbox = (ImageView) findViewById(R.id.messages);
        favourite = (ImageView) findViewById(R.id.favourite);
        img_main = (ImageView) findViewById(R.id.img_main);
        storeImg = (ImageView) findViewById(R.id.store_img);
        noStoreLogo = (ImageView) findViewById(R.id.noStoreLogo);
        share = (ImageView) findViewById(R.id.share);
        recyclerView = (RecyclerView) findViewById(R.id.recycler);
        ratingBar = (RatingBar) findViewById(R.id.ratingBar);
        giveRating = (LinearLayout) findViewById(R.id.giveRating);
        ratingNumbers = (LinearLayout) findViewById(R.id.ratingnumbers);
        smoothProgressBar = (SmoothProgressBar) findViewById(R.id.pocketa);

        scan_tool = (LinearLayout) findViewById(R.id.ll_scan);
        browseStore = (LinearLayout) findViewById(R.id.browse_store);
        header = (LinearLayout) findViewById(R.id.header);
        msg_tool = (LinearLayout) findViewById(R.id.ll_message);
        search_tool = (LinearLayout) findViewById(R.id.ll_search);
        back_tool = (LinearLayout) findViewById(R.id.iv_back);
        mPocketBar = (CircularProgressView) findViewById(R.id.pocket);
        nolocationTxt = (CardView) findViewById(R.id.nolocation);
        nolocationLinear = (CardView) findViewById(R.id.locationlinear);


        giveRating.setOnClickListener(this);
        iv_back.setOnClickListener(this);
        favourite.setOnClickListener(this);
        storeBrowse.setOnClickListener(this);
        inbox.setOnClickListener(this);
        share.setOnClickListener(this);
        scan_tool.setOnClickListener(this);
        msg_tool.setOnClickListener(this);
        search_tool.setOnClickListener(this);
        browseStore.setOnClickListener(this);
        showAll.setOnClickListener(this);
        getfocus();
        mPocketBar.setVisibility(View.VISIBLE);
        ModelManager.getInstance().getMerchantManager().getMerchantDetail(this,
                Operations.makeJsonGetMerchantDetail(this, merchantId), Constants.GET_MERCHANT_SUCCESS);

        ModelManager.getInstance().getMerchantFavouriteManager().getFavourites(mActivity,
                Operations.makeJsonGetMerchantFavourite(mActivity));



        ModelManager.getInstance().getAdsManager().getAllAds(this,
                Operations.makeJsonGetAdsListing(this, merchantId), Constants.ADS_LISTING_SUCCESS);

        ModelManager.getInstance().getAddMerchantRating().getMerchantRating(MerchantStoreDetails.this,
                Operations.GetMerchantRating(mActivity, ATPreferences.readString(this, Constants.KEY_USERID), merchantId));


        AddTabBar.getmInstance().setupViewPager(tabLayout);
        AddTabBar.getmInstance().setupTabIcons(tabLayout, mActivity, tabOne, tabTwo, tabThree, tabFour, tabFive, tabSix);
        AddTabBar.getmInstance().bindWidgetsWithAnEvent(tabConatiner, tabLayout, MerchantStoreDetails.this, R.id.container_body);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
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
            case Constants.GET_MERCHANT_SUCCESS:
                if (ModelManager.getInstance().getMerchantManager().merchantDetailBean.getRESULT().get(0).getRESULT().size() > 0) {
                    data = ModelManager.getInstance().getMerchantManager().merchantDetailBean.getRESULT().get(0).getRESULT().get(0);
                    storeName.setText(data.getName());
                    Log.d("aboutss", data.getAbout());
                    merchantName = data.getName();
                    LayerDrawable stars = (LayerDrawable) ratingBar.getProgressDrawable();

                    Log.d("ratingIs", data.getRating());
                    if (data.getRating().equals("0")) {
                        ratingNo.setText("No Ratings");
                        stars.getDrawable(0).setColorFilter(Color.parseColor("#fcb74d"), PorterDuff.Mode.SRC_ATOP);

                        ratingBar.setProgress((int) Double.parseDouble(data.getRating()));
                    } else {
                        stars.getDrawable(2).setColorFilter(Color.parseColor("#fcb74d"), PorterDuff.Mode.SRC_ATOP);
                        stars.getDrawable(0).setColorFilter(Color.parseColor("#fcb74d"), PorterDuff.Mode.SRC_ATOP);
                        stars.getDrawable(1).setColorFilter(Color.parseColor("#fcb74d"), PorterDuff.Mode.SRC_ATOP);
                        ratingBar.setProgress((int) Double.parseDouble(data.getRating()));
                        Log.d("ratingIs2", (int) Double.parseDouble(data.getRating()) + "");

                    }
                    try {
                        Log.d("GetAbout", data.getAbout() + "  l");
                        if (!data.getAbout().isEmpty())
                            storeDetails.setText(Utils.getStringHexaDecimal(data.getAbout()));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    Picasso.with(this).load(ATPreferences.readString(this, Constants.KEY_IMAGE_URL) + data.getImage()).into(img_main);
                    Picasso.with(this).load(ATPreferences.readString(this, Constants.KEY_IMAGE_URL) + data.getImage()).into(storeImg);
                    Picasso.with(this).load(ATPreferences.readString(this, Constants.KEY_IMAGE_URL) + data.getImage()).into(noStoreLogo);
                    image_str = data.getImage();
                    uri = getLocalBitmapUri(img_main);
                }
                //Ok but sabiya in this we have to send Product Id to get Quantity but when we have multiple product list showing in listview we cant send multiple product Ids to get quantity
                break;
            case Constants.ADS_LISTING_SUCCESS:

                showAdsWithAnimation();
                break;
            case Constants.REMOVE_MERCHANT_FAVORITES:

                smoothProgressBar.setVisibility(View.GONE);
                smoothProgressBar.progressiveStop();
                favourite.setBackgroundResource(R.drawable.ic_icon_fav_gray);
                break;

            case Constants.GET_RATING_SUCCESS:
                clearfocus();
                mPocketBar.setVisibility(View.GONE);
                List<RatingBean.RESULT_> ratingBeen = ModelManager.getInstance().getAddMerchantRating().ratingBean.getRESULT().get(0).getRESULT();
                ratingNo.setText("(" + ratingBeen.size() + ")");
                if (ratingBeen.size() == 0) {
                    ratingNo.setText("No Ratings");
                }

                break;

            case Constants.GET_MERCHANT_DISTANCE_SUCCESS:
                List<LocationListBean.RESULT_> arrayList = new ArrayList<LocationListBean.RESULT_>();
                arrayList = ModelManager.getInstance().getMerchantManager().locationListBean.getRESULT().get(0).getRESULT();
                Log.d("listContains", arrayList.get(0).get478() + "");
                if (arrayList.get(0).get478() == null) {
                    nolocationTxt.setVisibility(View.VISIBLE);
                    nolocationLinear.setVisibility(View.GONE);
                } else {
                    locationListAdpater = new LocationListAdpater(adapterClick, mActivity, arrayList, inbox);
                    recyclerView.setHasFixedSize(true);
                    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
                    recyclerView.setLayoutManager(layoutManager);
                    recyclerView.setAdapter(locationListAdpater);
                }

                break;

            case Constants.ADD_MERCHANT_FAVORITE_SUCCESS:
                //    if (isFavorite.equals("1"))
                smoothProgressBar.setVisibility(View.GONE);
                smoothProgressBar.progressiveStop();
                favourite.setBackgroundResource(R.drawable.ic_icon_fav);
                break;
            case Constants.GET_MERCHANT_LOCATION_SUCCESS:
            /*    clearfocus();
                mPocketBar.setVisibility(View.GONE);
                ArrayList<LocationBean> arrayList = new ArrayList<LocationBean>();
                arrayList = ModelManager.getInstance().getMerchantManager().beanlis;
                locationListAdpater = new LocationListAdpater(mActivity, arrayList);
                recyclerView.setHasFixedSize(true);
                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(locationListAdpater);
                if (arrayList.size() == 0) {
                    nolocationTxt.setVisibility(View.VISIBLE);
                    nolocationLinear.setVisibility(View.GONE);
                }*/
                break;
            case Constants.GET_MERCHANT_FAVORITES:
                //    if (isFavorite.equals("1"))
                ArrayList<String>  mername = ModelManager.getInstance().getMerchantFavouriteManager().mernchantfavlist;
                Log.d("MernMae", mername + "  " + merchantName);
                try {
                    if (mername.contains(merchantName)) {
                        isFavorite=true;
                        favourite.setBackgroundResource(R.drawable.ic_icon_fav);
                    }

                } catch (Exception e) {
                }
                break;
        }
    }

    Runnable r;
    Handler h;
    int count;

    private void showAdsWithAnimation() {

        if (ModelManager.getInstance().getHomeManager().ads != null)
            if (ModelManager.getInstance().getHomeManager().ads.size() > 0) {
                count = 0;
                final ArrayList<AdsDetailWithMerchant> adsDetailWithMerchants = ModelManager.getInstance().getAdsManager().url_maps;
                viewPager.setAdapter(new SamplePagerAdapter(mActivity, ModelManager.getInstance().getHomeManager().ads, adsDetailWithMerchants, false,""));
                circleIndicator.setViewPager(viewPager);
                viewPager.setCurrentItem(count);
                h = new Handler();
                r = new Runnable() {
                    @Override
                    public void run() {
                        h.removeMessages(0);
                        ++count;
                        if ((count + 1) > ModelManager.getInstance().getHomeManager().ads.size())
                            count = 0;

                        viewPager.setCurrentItem(count);
                        h.postDelayed(r, 5000);
                    }
                };

                viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                    @Override
                    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                    }

                    @Override
                    public void onPageSelected(int position) {
                        count = position;
                    }

                    @Override
                    public void onPageScrollStateChanged(int state) {

                    }
                });
                h.postDelayed(r, 1000);
            } else {
                noAdsCard.setVisibility(View.VISIBLE);
                adsCard.setVisibility(View.GONE);
            }
    }

    public void onResume() {
        super.onResume();
        showAdsWithAnimation();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.giveRating:

                Intent i = new Intent(this, RateMerchant.class);
                i.putExtra("merchantId", merchantId);
                i.putExtra("storeName", storeName.getText().toString());
                i.putExtra("storeImage", image_str);
                i.putExtra("storeRateString", ratingNo.getText().toString());
                i.putExtra("rateProgress", (data.getRating()));
                startActivity(i);
                break;
            case R.id.tv_back:
                finish();
                break;
            case R.id.favourite:
                smoothProgressBar.setVisibility(View.VISIBLE);
                smoothProgressBar.progressiveStart();
                if (!isFavorite) {
                    ModelManager.getInstance().getAddMerchantFavorite().addMerchantToFavorite(mActivity, Operations.makeJsonMerchantAddToFavorite(mActivity, merchantId));
                    isFavorite=true;
                }
                else
                    ModelManager.getInstance().getMerchantFavouriteManager().removeFavourite(mActivity,
                            Operations.makeJsonRemoveMerchantFavourite(mActivity,merchantId));
                break;
            case R.id.messages:
                frameLayout.setVisibility(View.VISIBLE);
                displayView(new FragmentMessages(), Constants.TAG_MESSAGEPAGE, new Bundle());
                break;
            case R.id.share:
                shareImage();
                break;
            case R.id.ll_message:
                frameLayout.setVisibility(View.VISIBLE);
                displayView(new FragmentMessages(), Constants.TAG_MESSAGEPAGE, new Bundle());
                break;
            case R.id.ll_scan:
                frameLayout.setVisibility(View.VISIBLE);
                if (checkSelfPermission(Manifest.permission.CAMERA)
                        != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(new String[]{Manifest.permission.CAMERA},
                            MY_CAMERA_REQUEST_CODE);
                } else {
                    //mTxtHeading.setText("Scan");
                    //mlogo.setVisibility(View.GONE);
                    //mTxtHeading.setVisibility(View.VISIBLE);
                    displayView(new FragmentScanner(), Constants.TAG_SCANNER, new Bundle());
                }

                break;
            case R.id.ll_search:
                Utils.showSearchDialog(mActivity);
                break;
            case R.id.browse_store:
                ATPreferences.putBoolean(mActivity, "header_store", true);
                ATPreferences.putString(mActivity, "merchant_store", merchantId);
                startActivity(new Intent(mActivity, HomeActivity.class));
                break;
            case R.id.details_store:
                ATPreferences.putBoolean(mActivity, "header_store", true);
                ATPreferences.putString(mActivity, "merchant_store", merchantId);
                startActivity(new Intent(mActivity, HomeActivity.class));
                break;


            case R.id.showall:
                Intent intent = new Intent(mActivity, MerchantStoreMap.class);
                intent.putExtra("merchantId", merchantId);
                intent.putExtra("position", 99);
                startActivity(intent);
                break;

            case R.id.iv_back:

                if (frameLayout.getVisibility() == View.VISIBLE)
                    frameLayout.setVisibility(View.GONE);
                else
                    finish();

                break;
        }
    }

    public void displayView(Fragment fragment, String tag, Bundle bundle) {
        //  if (fragment != null) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        Fragment fragB = fragmentManager.findFragmentByTag(tag);
        if (bundle != null)
            fragment.setArguments(bundle);
        //  if (fragB == null) {
        fragmentTransaction.replace(R.id.container_body, fragment);
        if (fragment instanceof Fragment_Ads || fragment instanceof FragmentSpecial || fragment instanceof FragmentItems) {

        } else
            fragmentTransaction.addToBackStack(tag);
        fragmentTransaction.commit();
//            } else
//                fragmentTransaction.show(fragB);
        //  getSupportActionBar().setTitle(tag);
        // }
    }

    private void shareImage() {
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_TEXT, "Nice item on ApiTap\n" + merchantName + "\n" + storeDetails.getText().toString());
        shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
        shareIntent.setType("image/*");
        shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        startActivity(Intent.createChooser(shareIntent, "send"));
    }

    public Uri getLocalBitmapUri(ImageView imageView) {
        // Extract Bitmap from ImageView drawable
        Drawable drawable = imageView.getDrawable();
        Bitmap bmp = null;
        if (drawable instanceof BitmapDrawable) {
            bmp = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
        } else {
            return null;
        }
        // Store image to default external storage directory
        Uri bmpUri = null;
        try {
            File file = new File(Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_DOWNLOADS), "share_image_" + System.currentTimeMillis() + ".png");
            file.getParentFile().mkdirs();
            FileOutputStream out = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.PNG, 90, out);
            out.close();
            bmpUri = Uri.fromFile(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bmpUri;
    }

    public void showSearchDialog() {
        final ArrayList<String> list = ModelManager.getInstance().getSearchManager().listAddresses;
        final Dialog dialog = new Dialog(mActivity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.CENTER;
        dialog.setContentView(R.layout.quick_search_test);

        Button submit = (Button) dialog.findViewById(R.id.submit);
        Button cancel = (Button) dialog.findViewById(R.id.cancel);
        final EditText search = (EditText) dialog.findViewById(R.id.search);
        Spinner spinner = (Spinner) dialog.findViewById(R.id.spinner);
        ArrayAdapter arrayAdapter = new ArrayAdapter(getApplicationContext(), R.layout.spinner_item, list);
        spinner.setAdapter(arrayAdapter);


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchkey = search.getText().toString();
                startActivity(new Intent(mActivity, SearchItemActivity.class).putExtra("key", searchkey));
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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == MY_CAMERA_REQUEST_CODE) {

            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (toolint == 0) {

                    displayView(new FragmentScanner(), Constants.TAG_SCANNER, new Bundle());

                } else {
                    displayView(new FragmentScanner(), Constants.TAG_SCANNER, null);
                }
            } else {
                Toast.makeText(this, "Camera permission denied", Toast.LENGTH_LONG).show();
            }

        }
        if (requestCode == MY_PERMISSIONS_REQUEST_LOCATION) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                // permission was granted, yay! Do the
                // location-related task you need to do.
                getloc();
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

    public void clearfocus() {
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }

    public void getfocus() {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }

    @Override
    public void onDrawerItemSelected(View view, int position) {
        startActivity(new Intent(this, HomeActivity.class).putExtra("Drawer", position));
    }

    private boolean getloc() {
        boolean isReturn = false;
        GPSService mGPSService = new GPSService(getApplicationContext());
        mGPSService.getLocation();


        boolean b = Utils.checkLocationPermission(MerchantStoreDetails.this);
        if (!b) {
            Toast.makeText(getApplicationContext(), "Your location is not available, please try again.", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            current_lat = String.valueOf(mGPSService.getLatitude());
            current_long = String.valueOf(mGPSService.getLongitude());
            ModelManager.getInstance().getMerchantManager().getMerchantDistance(this,
                    Operations.makeJsonGetMerchantDistance(this, merchantId, current_lat, current_long), Constants.GET_MERCHANT_DISTANCE_SUCCESS);

        }

       /* if (gps.canGetLocation()) {
            current_lat = String.valueOf(gps.getLatitude());
            current_long = String.valueOf(gps.getLongitude());
            isReturn = true;
        } else {
            gps.showSettingsAlertLocation();
            current_lat = "41.881832";
            current_long = "-87.623177";

        }*/
        return isReturn;
    }

    @Override
    public void onItemClick(View v, int position) {
        Intent intent = new Intent(mActivity, MerchantStoreMap.class);
        intent.putExtra("merchantId", merchantId);
        intent.putExtra("position", position);
        startActivity(intent);
    }


}
