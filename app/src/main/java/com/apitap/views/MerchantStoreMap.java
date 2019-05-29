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
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
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
import android.view.MotionEvent;
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
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.apitap.R;
import com.apitap.controller.ModelManager;
import com.apitap.model.AddTabBar;
import com.apitap.model.Constants;
import com.apitap.model.CustomMapView;
import com.apitap.model.GpsLocation;
import com.apitap.model.Operations;
import com.apitap.model.TouchSupportMapFragment;
import com.apitap.model.Utils;
import com.apitap.model.bean.LocationListBean;
import com.apitap.model.bean.MerchantDetailBean;
import com.apitap.model.bean.RatingBean;
import com.apitap.model.customclasses.Event;
import com.apitap.model.preferences.ATPreferences;
import com.apitap.views.NavigationMenu.FragmentDrawer;
import com.apitap.views.adapters.LocationListAdpater;
import com.apitap.views.fragments.FragmentItems;
import com.apitap.views.fragments.FragmentMessages;
import com.apitap.views.fragments.FragmentScanner;
import com.apitap.views.fragments.FragmentSpecial;
import com.apitap.views.fragments.Fragment_Ads;
import com.github.rahatarmanahmed.cpv.CircularProgressView;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderApi;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import fr.castorflex.android.smoothprogressbar.SmoothProgressBar;

/**
 * Created by Shami on 7/10/2017.
 */

public class MerchantStoreMap extends AppCompatActivity implements View.OnClickListener, OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener, FragmentDrawer.FragmentDrawerListener, LocationListAdpater.AdapterClick {

    public static boolean mMapIsTouched = false;
    private TextView storeName, ratingNo;
    private ImageView inbox, favourite, share, img_main;
    private RecyclerView recyclerView;
    SmoothProgressBar smoothProgressBar;
    private FragmentDrawer drawerFragment;
    private Toolbar mToolbar;
    private RatingBar ratingBar;
    private String merchantId, image_str, merchantName, searchkey;
    private LinearLayout giveRating, ratingNumbers;
    CircularProgressView mPocketBar;
    List<LocationListBean.RESULT_> arrayList;
    private Activity mActivity;
    private String current_lat, current_long;
    private static int toolint = 0;
    FrameLayout frameLayout;
    CustomMapView mapView;
    LinearLayout scan_tool;
    LinearLayout msg_tool;
    LinearLayout search_tool;
    LinearLayout back_tool;
    private static final int MY_CAMERA_REQUEST_CODE = 100;
    LocationListAdpater locationListAdpater;
    Uri uri;
    ArrayList<String> latlist = new ArrayList<String>();
    ArrayList<String> longlist = new ArrayList<String>();
    private GoogleApiClient mGoogleApiClient;
    LinearLayout selectMap, selectSatelite;
    private GoogleMap mMap;
    int state = 0;
    private Location mLastLocation;
    private FusedLocationProviderApi fusedLocationProviderApi;
    private LocationRequest locationRequest;
    private ImageView backll;
    private LocationListAdpater.AdapterClick adapterClick;
    Button storeBrowse;
    CardView nolocationTxt, nolocationLinear;
    LinearLayout browse_store;
    ImageView naviagte_to;
    int position = 99;
    int clicked_pos = 99;
    public static TabLayout tabLayout;
    private TextView tabOne, tabTwo, tabThree, tabFour, tabFive, tabSix;
    LinearLayout tabConatiner;
    boolean isFavorite=false;

    @Override
    protected void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        setContentView(R.layout.store_review_map);

        mActivity = this;
        adapterClick = this;
        if (getIntent() != null) {
            merchantId = getIntent().getStringExtra("merchantId");
            position = getIntent().getIntExtra("position", 99);
            Log.d("MerchantID", merchantId + "   s");
        }
        getloc();
        initViews(savedInstance);
        checkLocationPermission();
        initGoogleAPIClient();

    }

    private void initGoogleAPIClient() {
        locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(5000);
        fusedLocationProviderApi = LocationServices.FusedLocationApi;
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }
    }

    private void initViews(Bundle savedInstance) {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        drawerFragment = (FragmentDrawer)
                getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
        drawerFragment.setUp(R.id.fragment_navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout), mToolbar);
        drawerFragment.setDrawerListener(this);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

   /*     SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
*/
        mapView = (CustomMapView)findViewById(R.id.map);
        mapView.onCreate(savedInstance);
        mapView.onResume();
        mapView.getMapAsync(this);

        frameLayout = (FrameLayout) findViewById(R.id.container_body);
        frameLayout.setVisibility(View.GONE);
        storeName = (TextView) findViewById(R.id.tv_view_store);

        selectMap = (LinearLayout) findViewById(R.id.maptxt);
        selectSatelite = (LinearLayout) findViewById(R.id.sattxt);
        tabConatiner = (LinearLayout) findViewById(R.id.tab_container);
        tabLayout = (TabLayout) findViewById(R.id.tabs);

        ratingNo = (TextView) findViewById(R.id.ratingNo);
        inbox = (ImageView) findViewById(R.id.messages);
        favourite = (ImageView) findViewById(R.id.favourite);
        img_main = (ImageView) findViewById(R.id.img_main);
        share = (ImageView) findViewById(R.id.share);
        recyclerView = (RecyclerView) findViewById(R.id.recycler);
        ratingBar = (RatingBar) findViewById(R.id.ratingBar);
        giveRating = (LinearLayout) findViewById(R.id.giveRating);
        ratingNumbers = (LinearLayout) findViewById(R.id.ratingnumbers);
        smoothProgressBar = (SmoothProgressBar) findViewById(R.id.pocketa);
        mPocketBar = (CircularProgressView) findViewById(R.id.pocket);
        scan_tool = (LinearLayout) findViewById(R.id.ll_scan);
        msg_tool = (LinearLayout) findViewById(R.id.ll_message);
        search_tool = (LinearLayout) findViewById(R.id.ll_search);
        back_tool = (LinearLayout) findViewById(R.id.iv_back);
        nolocationTxt = (CardView) findViewById(R.id.nolocation);
        storeBrowse = (Button) findViewById(R.id.details_store);
        backll = (ImageView) findViewById(R.id.tv_back);
        nolocationLinear = (CardView) findViewById(R.id.locationlinear);
        browse_store = (LinearLayout) findViewById(R.id.browse_store);
        naviagte_to = (ImageView) findViewById(R.id.naviagte_to);

        giveRating.setOnClickListener(this);

        backll.setOnClickListener(this);
        naviagte_to.setOnClickListener(this);
        favourite.setOnClickListener(this);
        inbox.setOnClickListener(this);
        share.setOnClickListener(this);
        scan_tool.setOnClickListener(this);
        msg_tool.setOnClickListener(this);
        search_tool.setOnClickListener(this);
        storeBrowse.setOnClickListener(this);
        selectMap.setOnClickListener(this);
        selectSatelite.setOnClickListener(this);
        browse_store.setOnClickListener(this);

        mPocketBar.setVisibility(View.VISIBLE);
        List<RatingBean.RESULT_> ratingBeen = ModelManager.getInstance().getAddMerchantRating().ratingBean.getRESULT().get(0).getRESULT();
        ratingNo.setText("(" +ratingBeen.size()+ ")");
        if (ratingBeen.size()==0){
            ratingNo.setText("No Ratings");
        }

        ModelManager.getInstance().getMerchantManager().getMerchantDetail(this,
                Operations.makeJsonGetMerchantDetail(this, merchantId), Constants.GET_MERCHANT_SUCCESS);

        ModelManager.getInstance().getMerchantFavouriteManager().getFavourites(mActivity,
                Operations.makeJsonGetMerchantFavourite(mActivity));

        ModelManager.getInstance().getMerchantManager().getMerchantDistance(this,
                Operations.makeJsonGetMerchantDistance(this, merchantId, current_lat, current_long), Constants.GET_MERCHANT_DISTANCE_SUCCESS);

        AddTabBar.getmInstance().setupViewPager(tabLayout);
        AddTabBar.getmInstance().setupTabIcons(tabLayout, mActivity, tabOne, tabTwo, tabThree, tabFour, tabFive, tabSix);
        AddTabBar.getmInstance().bindWidgetsWithAnEvent(tabConatiner, tabLayout, MerchantStoreMap.this, R.id.container_body);
    }

    @Override
    public void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        mGoogleApiClient.disconnect();
        EventBus.getDefault().unregister(this);
    }


    @Subscribe
    public void onEvent(Event event) {
        switch (event.getKey()) {
            case Constants.GET_MERCHANT_SUCCESS:

                if (ModelManager.getInstance().getMerchantManager().merchantDetailBean.getRESULT().get(0).getRESULT().size() > 0) {
                    MerchantDetailBean.RESULT.DetailData data = ModelManager.getInstance().getMerchantManager().merchantDetailBean.getRESULT().get(0).getRESULT().get(0);
                    storeName.setText(data.getName());
                    Log.d("aboutss", data.getAbout());
                    merchantName = data.getName();
                    //   txt_address.setText(data.getAddress());
                    //   txt_call.setText("Call " + data.getPhone());
                    //   txt_call.setTag(data.getPhone());
                    //  txt_msg.setTag(data.getPhone());
                    // txt_reviews.setText(data.getReview_count() + " Reviews");
                    LayerDrawable stars = (LayerDrawable) ratingBar.getProgressDrawable();


                    if (data.getRating().equals("0")) {
                        stars.getDrawable(0).setColorFilter(Color.parseColor("#fcb74d"), PorterDuff.Mode.SRC_ATOP);

                        ratingBar.setProgress((int) Double.parseDouble(data.getRating()));
                    } else {
                        stars.getDrawable(2).setColorFilter(Color.parseColor("#fcb74d"), PorterDuff.Mode.SRC_ATOP);
                        stars.getDrawable(0).setColorFilter(Color.parseColor("#fcb74d"), PorterDuff.Mode.SRC_ATOP);
                        stars.getDrawable(1).setColorFilter(Color.parseColor("#fcb74d"), PorterDuff.Mode.SRC_ATOP);
                        ratingBar.setProgress((int) Double.parseDouble(data.getRating()));
                        //ratingNo.setText("(" +data.getReview_count()+ ")");
                    }
                    try {
                        //if (!data.getAbout().equals(""))
                        //  storeDetails.setText(Utils.getStringHexaDecimal(data.getAbout()));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    Picasso.with(this).load(ATPreferences.readString(this, Constants.KEY_IMAGE_URL) + data.getImage()).into(img_main);
                    image_str = data.getImage();
                    uri = getLocalBitmapUri(img_main);
                }

                break;
            case Constants.REMOVE_MERCHANT_FAVORITES:

                smoothProgressBar.setVisibility(View.GONE);
                smoothProgressBar.progressiveStop();
                favourite.setBackgroundResource(R.drawable.ic_icon_fav_gray);
                break;

            case Constants.ADD_MERCHANT_FAVORITE_SUCCESS:
                //    if (isFavorite.equals("1"))
                smoothProgressBar.setVisibility(View.GONE);
                smoothProgressBar.progressiveStop();
                favourite.setBackgroundResource(R.drawable.ic_icon_fav);
                break;
            case Constants.GET_MERCHANT_DISTANCE_SUCCESS:
                mPocketBar.setVisibility(View.GONE);
                arrayList = ModelManager.getInstance().
                        getMerchantManager().locationListBean.getRESULT().get(0).getRESULT();
                locationListAdpater = new LocationListAdpater(adapterClick, mActivity, arrayList, naviagte_to);
                recyclerView.setHasFixedSize(true);
                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(locationListAdpater);
                updateLocation();
                currentLocation();
                double latus = 36.2423876;
                double lngus = -113.7481853;

                LatLng markerLoc = new LatLng(latus, lngus);

//                final CameraPosition cameraPosition = new CameraPosition.Builder()
//                        .target(markerLoc)      // Sets the center of the map to Mountain View
//                        .zoom(3)                   // Sets the zoom
//                        .build();
//                mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
             //   CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(markerLoc, 16);
           //     mMap.animateCamera(cameraUpdate);
                for (int i = 0; i < arrayList.size(); i++) {
                    if (!arrayList.get(i).getAD().get12038().isEmpty()) {
                        double lat = Double.parseDouble(arrayList.get(i).getAD().get12038());
                        double lng = Double.parseDouble(arrayList.get(i).getAD().get12039());

                      //  LatLng location = new LatLng(lat, lng);
                        LatLng location = getLocationFromAddress(arrayList.get(i).getAD().get11412());
                        mMap.addMarker(new MarkerOptions().position(location).title(arrayList.get(i).get11470()));
                    }
                }


                if (arrayList.size() == 0) {
                    nolocationTxt.setVisibility(View.VISIBLE);
                    nolocationLinear.setVisibility(View.GONE);
                }
                if (position != 99) {
                    navigateToMap(position);
                    locationListAdpater.updateNotify(arrayList, position);
                }

                naviagte_to.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int selctPos;
                        if (clicked_pos == 99) {
                            selctPos = 0;
                        } else {
                            selctPos = clicked_pos;
                        }
                        boolean isReturn = getloc();
                        if (!isReturn) {
                            current_lat = "41.881832";
                            current_long = "-87.623177";
                        }
                        Log.d("positionLOcation", selctPos + "  lop");
                        Log.d("latlongs", arrayList.get(selctPos).getAD().getZP().get12038() + "   " + arrayList.get(selctPos).getAD().getZP().get12039());
//                     Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://maps.google.com/maps?" + "saddr=" + current_long + "," + current_lat + "&daddr=" + list.getAD().getZP().get12038() + "," + list.getAD().getZP().get12039()));
//                    intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");
//                    context.startActivity(intent);
                        Uri gmmIntentUri = Uri.parse("google.navigation:q=" + arrayList.get(selctPos).getAD().getZP().get12038() + "," + arrayList.get(selctPos).getAD().getZP().get12039());
                        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                        mapIntent.setPackage("com.google.android.apps.maps");
                        startActivity(mapIntent);
                    }

                });
                View locationButton = ((View)mapView.findViewById(Integer.parseInt("1")).getParent()).findViewById(Integer.parseInt("2"));
                RelativeLayout.LayoutParams rlp =(RelativeLayout.LayoutParams) locationButton.getLayoutParams();
                rlp.addRule(RelativeLayout.ALIGN_PARENT_TOP,0);
                rlp.addRule(RelativeLayout.ALIGN_PARENT_TOP,RelativeLayout.TRUE);
                rlp.setMargins(0,90,180,0);
                break;
            case Constants.GET_MERCHANT_FAVORITES:
                ArrayList<String>  mername = ModelManager.getInstance().getMerchantFavouriteManager().mernchantfavlist;
                Log.d("MernMae", mername + "  " + merchantName);
                try {
                    if (mername.contains(merchantName)) {
                        favourite.setBackgroundResource(R.drawable.ic_icon_fav);
                        isFavorite=true;
                    }
                } catch (Exception e) {
                }
                break;

        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.giveRating:

                Intent i = new Intent(this, RateMerchant.class);
                i.putExtra("merchantId", merchantId);
                i.putExtra("storeName", storeName.getText().toString());
                i.putExtra("storeImage", image_str);
                i.putExtra("rateProgress", ratingBar.getProgress());
                startActivity(i);
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
                Utils.showSearchDialog(this);
                break;

            case R.id.showall:
                Intent intent = new Intent(mActivity, MerchantDetailActivity.class);
                intent.putExtra("merchantId", merchantId);
                startActivity(intent);
                break;

            case R.id.iv_back:

                if (frameLayout.getVisibility() == View.VISIBLE)
                    frameLayout.setVisibility(View.GONE);
                else
                    finish();

                break;

            case R.id.sattxt:
                mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                break;

            case R.id.maptxt:
                mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (frameLayout.getVisibility() == View.VISIBLE)
            frameLayout.setVisibility(View.GONE);
        else
            finish();
    }

    private void shareImage() {
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_TEXT, "Nice item on ApiTap\n" + merchantName);
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

                    //  Toast.makeText(this, "Camera permission granted", Toast.LENGTH_LONG).show();
                    // mTxtHeading.setText("Scan");
                    //mlogo.setVisibility(View.GONE);
                    //mTxtHeading.setVisibility(View.VISIBLE);
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
                if (ContextCompat.checkSelfPermission(this,
                        Manifest.permission.ACCESS_FINE_LOCATION)
                        == PackageManager.PERMISSION_GRANTED) {

                    //Request location updates:
                    updateLocation();
                }

            } else {

                // permission denied, boo! Disable the
                // functionality that depends on this permission.

            }
            return;
        }

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        fusedLocationProviderApi.requestLocationUpdates(mGoogleApiClient, locationRequest, this);
    }


    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {
        updateLocation();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
    }

    private void updateLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);
    }

    public void currentLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mMap.setMyLocationEnabled(true);
    }


    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;

    public boolean checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                new AlertDialog.Builder(this)
                        .setTitle("Location Permissions")
                        .setMessage("Please enable location to get you current location.")
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Prompt the user once explanation has been shown
                                ActivityCompat.requestPermissions(mActivity,
                                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                        MY_PERMISSIONS_REQUEST_LOCATION);
                            }
                        })
                        .create()
                        .show();


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
            return false;
        } else {
            return true;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onDrawerItemSelected(View view, int position) {
        startActivity(new Intent(this, HomeActivity.class).putExtra("Drawer", position));

    }

    private boolean getloc() {
        boolean isReturn = false;
        GpsLocation gps = new GpsLocation(mActivity);

        if (gps.canGetLocation()) {
            current_lat = String.valueOf(gps.getLatitude());
            current_long = String.valueOf(gps.getLongitude());
            isReturn = true;
        } else {
            current_lat = "41.881832";
            current_long = "-87.623177";
            gps.showSettingsAlertLocation();
        }
        return isReturn;
    }

    @Override
    public void onItemClick(View v, int i) {
        clicked_pos = i;
        navigateToMap(i);
    }

    public void navigateToMap(int i) {

        if (!arrayList.get(i).getAD().get12038().isEmpty()) {
            naviagte_to.setVisibility(View.VISIBLE);
            double lat = Double.parseDouble(arrayList.get(i).getAD().get12038());
            double lng = Double.parseDouble(arrayList.get(i).getAD().get12039());
            Log.e("LAT  " + lat, "LONG  "+ lng+"");
            LatLng markerLoc = new LatLng(lat, lng);
            final CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(markerLoc)      // Sets the center of the map to Mountain View
                    .zoom(6)                   // Sets the zoom
                    .bearing(90)                // Sets the orientation of the camera to east
                    .tilt(30)                   // Sets the tilt of the camera to 30 degrees
                    .build();
            LatLng location = getLocationFromAddress(arrayList.get(i).getAD().get11412());

            mMap.addMarker(new MarkerOptions().position(location).title(arrayList.get(i).get11470()).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
         //   mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(markerLoc, 14);
            mMap.animateCamera(cameraUpdate);
        } else {
            Toast.makeText(mActivity, "No Location Found", Toast.LENGTH_SHORT).show();
        }
    }


    public LatLng getLocationFromAddress(String strAddress) {

        Geocoder coder = new Geocoder(this);
        List<Address> address;
        LatLng p1 = null;

        try {
            // May throw an IOException
            address = coder.getFromLocationName(strAddress, 5);
            if (address == null) {
                return null;
            }

            Address location = address.get(0);
            p1 = new LatLng(location.getLatitude(), location.getLongitude() );

        } catch (IOException ex) {

            ex.printStackTrace();
        }

        return p1;
    }

}




