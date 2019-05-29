package com.apitap.views;

import android.Manifest;
import android.app.Activity;
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
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.apitap.R;
import com.apitap.controller.ModelManager;
import com.apitap.model.AddTabBar;
import com.apitap.model.Constants;
import com.apitap.model.Utils;
import com.apitap.model.bean.ProductDetailsBean;
import com.apitap.model.preferences.ATPreferences;
import com.apitap.views.NavigationMenu.FragmentDrawer;
import com.apitap.views.fragments.FragmentDetails;
import com.apitap.views.fragments.FragmentItems;
import com.apitap.views.fragments.FragmentMessages;
import com.apitap.views.fragments.FragmentScanner;
import com.apitap.views.fragments.FragmentSpecial;
import com.apitap.views.fragments.Fragment_Ads;
import com.jsibbold.zoomage.AutoResetMode;
import com.jsibbold.zoomage.ZoomageView;
import com.linearlistview.LinearListView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


/**
 * Created by sourcefuse on 15/9/16.
 */

public class ZoomImage extends AppCompatActivity implements View.OnClickListener, FragmentDrawer.FragmentDrawerListener {
    ZoomageView touch;
    private FragmentDrawer drawerFragment;
    private Toolbar mToolbar;
    TextView productName, cross;
    HorizontalScrollView scrollView;
    ArrayList<ProductDetailsBean> detailsArray;
    LinearLayout scan_tool;
    LinearLayout msg_tool;
    LinearLayout search_tool;
    ImageView resize;
    LinearLayout viewMain;
    FrameLayout frameLayout;
    private static int toolint = 0;
    String searchkey = "";
    String imageUrl = "";
    int rel_position = 99;
    ImageView iv_back;
    public static TabLayout tabLayout;
    private TextView tabOne, tabTwo, tabThree, tabFour, tabFive, tabSix;
    LinearLayout tabConatiner;
    private static final int MY_CAMERA_REQUEST_CODE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zoom_image);

        initViews();

        imageUrl = getIntent().getExtras().getString("image");
        rel_position = getIntent().getExtras().getInt("position");
        String name = getIntent().getExtras().getString("name");
        detailsArray = (ArrayList<ProductDetailsBean>) getIntent().getSerializableExtra("detailsArray");

        productName.setText(name);
        Picasso.with(this).load(imageUrl).into(touch);
        if (rel_position!=99) {
            Picasso.with(ZoomImage.this).load(ATPreferences.readString(ZoomImage.this, Constants.KEY_IMAGE_URL) +
                    detailsArray.get(rel_position).getProductImage()).skipMemoryCache().into(touch);
            imageUrl = ATPreferences.readString(ZoomImage.this,Constants.KEY_IMAGE_URL)+detailsArray.get(rel_position).getProductImage();
        }


        findViewById(R.id.ll_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        //  cross.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                finish();
//            }
//        });

        setImages(detailsArray);

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

        // cross=(TextView)findViewById(R.id.cross);
        touch = (ZoomageView) findViewById(R.id.touch);
        productName = (TextView) findViewById(R.id.product_name);
        iv_back = (ImageView) findViewById(R.id.back);
        scrollView = (HorizontalScrollView) findViewById(R.id.scroll_view);
        scan_tool = (LinearLayout) findViewById(R.id.ll_scan);
        viewMain = (LinearLayout) findViewById(R.id.linear);
        frameLayout = (FrameLayout) findViewById(R.id.container_body);
        resize = (ImageView) findViewById(R.id.resize);
        msg_tool = (LinearLayout) findViewById(R.id.ll_message);
        search_tool = (LinearLayout) findViewById(R.id.ll_search);
        tabConatiner = (LinearLayout) findViewById(R.id.tab_container);
        tabLayout = (TabLayout) findViewById(R.id.tabs);


        iv_back.setOnClickListener(this);
        scan_tool.setOnClickListener(this);
        resize.setOnClickListener(this);
        msg_tool.setOnClickListener(this);
        search_tool.setOnClickListener(this);

        AddTabBar.getmInstance().setupViewPager(tabLayout);
        AddTabBar.getmInstance().setupTabIcons(tabLayout, ZoomImage.this, tabOne, tabTwo, tabThree, tabFour, tabFive, tabSix);
        AddTabBar.getmInstance().bindWidgetsWithAnEvent(tabConatiner, tabLayout, ZoomImage.this, R.id.container_body2);

    }

    private void setImages(ArrayList<ProductDetailsBean> detailsArray) {
        final LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        LinearListView mTwoWayView = (LinearListView) findViewById(R.id.my_gallery);
        mTwoWayView.setAdapter(mAdapter);
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

    private BaseAdapter mAdapter = new BaseAdapter() {

        @Override
        public View getView(final int position, View convertView, final ViewGroup parent) {
            if (convertView == null) {
                convertView = getLayoutInflater().inflate(R.layout.row_zoom, parent, false);
            }

            ImageView imageView = (ImageView) convertView.findViewById(R.id.image);

            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    touch.setScaleType(ImageView.ScaleType.FIT_CENTER);
                    Picasso.with(ZoomImage.this).load(ATPreferences.readString(ZoomImage.this, Constants.KEY_IMAGE_URL) +
                            detailsArray.get(position).getProductImage()).skipMemoryCache().into(touch);

                    imageUrl = ATPreferences.readString(ZoomImage.this, Constants.KEY_IMAGE_URL) + detailsArray.get(position).getProductImage();

                    Log.d("imageUrla",imageUrl+"  "+ATPreferences.readString(ZoomImage.this, Constants.KEY_IMAGE_URL) + detailsArray.get(position).getProductImage());
                }
            });

            Picasso.with(ZoomImage.this).load(ATPreferences.readString(ZoomImage.this, Constants.KEY_IMAGE_URL) + detailsArray.get(position).getProductImage()).into(imageView);
            return convertView;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public int getCount() {
            return detailsArray.size();
        }
    };

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.back:
                finish();
                break;
            case R.id.ll_message:
                frameLayout.setVisibility(View.VISIBLE);
                viewMain.setVisibility(View.GONE);
                displayView(new FragmentMessages(), Constants.TAG_MESSAGEPAGE, new Bundle());
                break;

            case R.id.ll_scan:
                frameLayout.setVisibility(View.VISIBLE);
                viewMain.setVisibility(View.GONE);
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
            case R.id.resize:
                Intent intent = new Intent(this, FullScreenImage.class);

                Bundle extras = new Bundle();
                extras.putString("imagebitmap", imageUrl);
                extras.putString("video", "");
                intent.putExtras(extras);
                startActivity(intent);
                break;
        }
    }

    public void showSearchDialog() {
        final ArrayList<String> list = ModelManager.getInstance().getSearchManager().listAddresses;
        final Dialog dialog = new Dialog(this);
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
                startActivity(new Intent(ZoomImage.this, SearchItemActivity.class).putExtra("key", searchkey));
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
    public void onDrawerItemSelected(View view, int position) {
        startActivity(new Intent(this, HomeActivity.class).putExtra("Drawer",position));
    }
}
