package com.apitap.views;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.apitap.R;
import com.apitap.controller.ModelManager;
import com.apitap.model.AddTabBar;
import com.apitap.model.Constants;
import com.apitap.model.Operations;
import com.apitap.model.Utils;
import com.apitap.model.ViewPagerCustomDuration;
import com.apitap.model.bean.AdsBean;
import com.apitap.model.bean.AdsDetailWithMerchant;
import com.apitap.model.bean.AdsListBean;
import com.apitap.model.bean.SearchItemBean;
import com.apitap.model.bean.SearchSpecialBean;
import com.apitap.model.customclasses.Event;
import com.apitap.model.preferences.ATPreferences;
import com.apitap.views.NavigationMenu.FragmentDrawer;
import com.apitap.views.adapters.AdsListingAdapter;
import com.apitap.views.adapters.SamplePagerAdapter;
import com.apitap.views.adapters.SearchItemAdapter;
import com.apitap.views.customviews.HorizontalSpaceItemDecoration;
import com.apitap.views.fragments.FragmentDetails;
import com.apitap.views.fragments.FragmentItems;
import com.apitap.views.fragments.FragmentMessages;
import com.apitap.views.fragments.FragmentScanner;
import com.apitap.views.fragments.FragmentSpecial;
import com.apitap.views.fragments.Fragment_Ads;
import com.github.rahatarmanahmed.cpv.CircularProgressView;
import com.linearlistview.LinearListView;
import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import me.relex.circleindicator.CircleIndicator;

/**
 * Created by sourcefuse on 25/11/16.
 */

public class SearchItemActivity extends AppCompatActivity implements View.OnClickListener, FragmentDrawer.FragmentDrawerListener {
    RecyclerView listView;
    LinearListView listView2;
    private static int toolint = 0;
    CircularProgressView circularProgressView;
    ViewPagerCustomDuration viewPager;
    CircleIndicator circleIndicator;
    LinearLayout listlayout1, listlayout2, nomsgLayout,adLayout;
    public static LinearLayout viewMain;
    public static FrameLayout frameLayout;
    List<SearchSpecialBean.RESULT> allImagesItems;
    private static final int MY_CAMERA_REQUEST_CODE = 100;
    static Activity mActivity;
    List<SearchSpecialBean.PC> pcList;
    LinearLayout scan_tool;
    LinearLayout msg_tool;
    LinearLayout search_tool;
    TextView searchedtv;
    TextView location_tv;
    LinearLayout back_tool;
    Button searchAgain, cancel_search;
    private FragmentDrawer drawerFragment;
    private Toolbar mToolbar;
    public static TabLayout tabLayout;
    private TextView tabOne, tabTwo, tabThree, tabFour, tabFive, tabSix;
    LinearLayout tabConatiner;
    FrameLayout ff_back;
    ScrollView scrollView;
    LinearLayout buttons_ll;
    TextView tv_back;
    SamplePagerAdapter SamplePagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_item);
        mActivity = this;
        viewPager = (ViewPagerCustomDuration) findViewById(R.id.viewpager);
        circleIndicator = (CircleIndicator) findViewById(R.id.indicator_default);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        drawerFragment = (FragmentDrawer)
                getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
        drawerFragment.setUp(R.id.fragment_navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout), mToolbar);
        drawerFragment.setDrawerListener(this);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        listlayout1 = (LinearLayout) findViewById(R.id.lin1);
        listlayout2 = (LinearLayout) findViewById(R.id.lin2);
        adLayout = (LinearLayout) findViewById(R.id.ad_ll);
        tv_back = (TextView) findViewById(R.id.ic_back);
        viewMain = (LinearLayout) findViewById(R.id.mainview);
        frameLayout = (FrameLayout) findViewById(R.id.container_body2);
        tabConatiner = (LinearLayout) findViewById(R.id.tab_container);
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        scan_tool = (LinearLayout) findViewById(R.id.ll_scan);
        msg_tool = (LinearLayout) findViewById(R.id.ll_message);
        search_tool = (LinearLayout) findViewById(R.id.ll_search);
        searchedtv = (TextView) findViewById(R.id.searched_key);
        location_tv = (TextView) findViewById(R.id.location_key);
        back_tool = (LinearLayout) findViewById(R.id.iv_back);
        ff_back = (FrameLayout) findViewById(R.id.backff);
        scrollView = (ScrollView) findViewById(R.id.scroll_view);
        buttons_ll = (LinearLayout) findViewById(R.id.button_ll);

        searchAgain = (Button) findViewById(R.id.searchAgain);
        cancel_search = (Button) findViewById(R.id.cancelbtn);

        nomsgLayout = (LinearLayout) findViewById(R.id.noResult);
        nomsgLayout.setVisibility(View.GONE);

        searchAgain.setOnClickListener(this);
        tv_back.setOnClickListener(this);
        cancel_search.setOnClickListener(this);
        scan_tool.setOnClickListener(this);
        msg_tool.setOnClickListener(this);
        search_tool.setOnClickListener(this);
        //back_tool.setOnClickListener(this);

        String key = getIntent().getExtras().getString("key");
        String location = getIntent().getExtras().getString("location");
        searchedtv.setText("Searched For: " + key);
        location_tv.setText("Location: " + location);


        circularProgressView = (CircularProgressView) findViewById(R.id.pocket);
        circularProgressView.setVisibility(View.VISIBLE);
        ModelManager.getInstance().getSearchItemsManager().getAllSearchItems(this, Operations.makeJsonSearchItem(SearchItemActivity.this, key));
        ModelManager.getInstance().getSearchItemsManager().getAllSearchProduct(this, Operations.makeJsonSearchProduct(SearchItemActivity.this, key));

        listView = (RecyclerView) findViewById(R.id.list);
        listView2 = (LinearListView) findViewById(R.id.list2);
        listView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        listView.addItemDecoration(new HorizontalSpaceItemDecoration(20));

        AddTabBar.getmInstance().setupViewPager(tabLayout);
        AddTabBar.getmInstance().setupTabIcons(tabLayout, mActivity, tabOne, tabTwo, tabThree, tabFour, tabFive, tabSix);
        AddTabBar.getmInstance().bindWidgetsWithAnEvent(tabConatiner, tabLayout, SearchItemActivity.this, R.id.container_body2);

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

    @Subscribe
    public void onEvent(Event event) {
        switch (event.getKey()) {
            case Constants.SEARCH_ITEM_SUCCESS:
                final List<SearchItemBean.Result.ResultData> arrayList = ModelManager.getInstance().getSearchItemsManager().messageListBean.getResult().get(0).getResult();
                SearchItemAdapter searchItemAdapter = new SearchItemAdapter(this, arrayList);
                Log.d("SizeIs", arrayList.size() + "");


                listView.setAdapter(searchItemAdapter);

                searchItemAdapter.setOnItemClickListner(new SearchItemAdapter.AdapterClick() {
                    @Override
                    public void onItemClick(View v, int position) {
                        String productId = Utils.lengtT(11, arrayList.get(0).getId());
                        String productType = arrayList.get(0).getProduct_type();
                        Bundle bundle = new Bundle();
                        bundle.putString("productId", productId);
                        bundle.putString("productType", productType);
                        bundle.putString("flag", "search");
                        clearBackstack();
                        displayView(new FragmentDetails(), Constants.TAG_DETAILSPAGE, bundle);
                        frameLayout.setVisibility(View.VISIBLE);
                        viewMain.setVisibility(View.GONE);

                        ;
                    }
                });


                //circularProgressView.setVisibility(View.GONE);
                nomsgLayout.setVisibility(View.GONE);
                listlayout2.setVisibility(View.VISIBLE);
                listlayout1.setVisibility(View.VISIBLE);
                break;

            case Constants.SEARCH_ITEM_SUCCESS_Empty:

                circularProgressView.setVisibility(View.GONE);
                nomsgLayout.setVisibility(View.VISIBLE);
                listlayout2.setVisibility(View.GONE);
                listlayout1.setVisibility(View.GONE);
                break;

            case Constants.SEARCH_ITEM_SUCCESS_List2:
             //   circularProgressView.setVisibility(View.GONE);
                setDataItems();
                break;
            case Constants.SEARCH_ITEM_SUCCESS_List2_Empty:
             //   circularProgressView.setVisibility(View.GONE);
                listlayout2.setVisibility(View.GONE);
                break;

            case Constants.ADS_LISTING_SUCCESS:
                circularProgressView.setVisibility(View.GONE);
                scrollView.setVisibility(View.VISIBLE);
                buttons_ll.setVisibility(View.VISIBLE);
                showAdsWithAnimation();
                break;
            case Constants.ADS_LISTING_EMPTY:
                circularProgressView.setVisibility(View.GONE);
                scrollView.setVisibility(View.VISIBLE);
                buttons_ll.setVisibility(View.VISIBLE);
                adLayout.setVisibility(View.GONE);
                break;
            case -1:
                circularProgressView.setVisibility(View.GONE);
                Toast.makeText(getApplicationContext(), "Problem occurred, please try again.", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    private void clearBackstack() {
        FragmentManager fm = getSupportFragmentManager();
        for (int i = 0; i < fm.getBackStackEntryCount(); ++i) {
            fm.popBackStack();
        }
    }

    Runnable r;
    Handler h;
    int count;

    private void showAdsWithAnimation() {

        if (ModelManager.getInstance().getSearchItemsManager().ads != null)
            if (ModelManager.getInstance().getSearchItemsManager().ads.size() > 0) {

                count = 0;
                final ArrayList<AdsDetailWithMerchant> adsDetailWithMerchants = ModelManager.getInstance().getSearchItemsManager().url_maps1;

                SamplePagerAdapter = new SamplePagerAdapter(this, ModelManager.getInstance().getSearchItemsManager().ads, adsDetailWithMerchants, false,"");
                viewPager.setAdapter(SamplePagerAdapter);
                circleIndicator.setViewPager(viewPager);
                viewPager.setCurrentItem(count);
                // viewPager.setPageTransformer(true, new ZoomOutPageTransformer());
                h = new Handler();
                r = new Runnable() {
                    @Override
                    public void run() {
                        h.removeMessages(0);
                        ++count;
                        if ((count + 1) > ModelManager.getInstance().getSearchItemsManager().ads.size())
                            count = 0;

                        if(SamplePagerAdapter != null) {
                            SamplePagerAdapter.notifyDataSetChanged();
                        }
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

                //  mLlAds.setVisibility(View.GONE);
                //noAdsll.setVisibility(View.VISIBLE);
            }
    }

    private void setDataItems() {

        List<SearchSpecialBean.RESULT> specialBeanArrayList = ModelManager.getInstance().getSearchItemsManager().searchSpecialBean.getRESULT();
        this.allImagesItems = specialBeanArrayList;
        Log.d("SizeSpecial", specialBeanArrayList.size() + "");
        if (allImagesItems.size() > 0)
            listView2.setAdapter(mAdapterItems);
        else
            listlayout2.setVisibility(View.GONE);

    }

    @Override
    protected void onPause() {
        if(SamplePagerAdapter != null) {
            SamplePagerAdapter.notifyDataSetChanged();
        }
        super.onPause();
    }

    private BaseAdapter mAdapterItems = new BaseAdapter() {

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = getLayoutInflater().inflate(R.layout.row_horizontal_test, parent, false);
            }
            pcList = allImagesItems.get(position).getPC();

            ImageView imageView = (ImageView) convertView.findViewById(R.id.image);
            TextView description = (TextView) convertView.findViewById(R.id.description);
            ImageView eye = (ImageView) convertView.findViewById(R.id.eye);
            String isSeen = pcList.get(0).get1149();
            Log.d("isSeenSpecial", isSeen);
            LinearLayout rlSinglePrice = (LinearLayout) convertView.findViewById(R.id.rel_single_price);
            LinearLayout rlTwoPrice = (LinearLayout) convertView.findViewById(R.id.rl_two_price);
            TextView price = (TextView) convertView.findViewById(R.id.price);
            TextView actualPrice = (TextView) convertView.findViewById(R.id.actual_price);
            TextView priceAfterDiscount = (TextView) convertView.findViewById(R.id.price_after_discount);
            if (isSeen.equalsIgnoreCase("false")) {
                eye.setBackgroundResource(R.drawable.green_seen);
            } else {
                eye.setVisibility(View.GONE);
            }
            description.setText(Utils.hexToASCII(pcList.get(0).get120157()));


            rlSinglePrice.setVisibility(View.VISIBLE);
            rlTwoPrice.setVisibility(View.GONE);
            actualPrice.setText(pcList.get(0).get122162());
            //actualPrice.setPaintFlags(actualPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            // priceAfterDiscount.setText("$" + allImagesItems.get(position).getPriceAfterDiscount());


            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    String productId = Utils.lengtT(11, allImagesItems.get(position).getPC().get(0).get114144());
                    String productType = allImagesItems.get(position).getPC().get(0).get114112();
                    Bundle bundle = new Bundle();
                    bundle.putString("productId", productId);
                    bundle.putString("productType", productType);
                    bundle.putString("flag", "search");

                    clearBackstack();
                    displayView(new FragmentDetails(), Constants.TAG_DETAILSPAGE, bundle);
                    frameLayout.setVisibility(View.VISIBLE);
                    viewMain.setVisibility(View.GONE);
                }
            });

            Picasso.with(SearchItemActivity.this).load(ATPreferences.readString(mActivity, Constants.KEY_IMAGE_URL)
                    + pcList.get(0).get121170()).into(imageView);
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
            return allImagesItems.size();
        }
    };

    public void displayView(Fragment fragment, String tag, Bundle bundle) {
        //  if (fragment != null) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        Fragment fragB = fragmentManager.findFragmentByTag(tag);
        if (bundle != null)
            fragment.setArguments(bundle);
        //  if (fragB == null) {
        fragmentTransaction.replace(R.id.container_body2, fragment);
        if (fragment instanceof Fragment_Ads || fragment instanceof FragmentSpecial || fragment instanceof FragmentItems) {

        } else
            fragmentTransaction.addToBackStack(tag);
        fragmentTransaction.commit();
//            } else
//                fragmentTransaction.show(fragB);
        //  getSupportActionBar().setTitle(tag);
        // }
    }


    public void onResume() {
        super.onResume();
        showAdsWithAnimation();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        //backPress();
        if (frameLayout.getVisibility() == View.VISIBLE) {
            frameLayout.setVisibility(View.GONE);
            viewMain.setVisibility(View.VISIBLE);
        } else
            mActivity.finish();

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_message:
                frameLayout.setVisibility(View.VISIBLE);
                viewMain.setVisibility(View.GONE);
                displayView(new FragmentMessages(), Constants.TAG_MESSAGEPAGE, new Bundle());
                break;
            case R.id.ic_back:
                    finish();
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

            case R.id.cancelbtn:
                finish();
                break;

            case R.id.searchAgain:
                Utils.showSearchDialog(this);
                break;

            case R.id.iv_back:

                if (frameLayout.getVisibility() == View.VISIBLE) {
                    frameLayout.setVisibility(View.GONE);
                    viewMain.setVisibility(View.VISIBLE);
                } else
                    finish();

                break;
        }
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


    @Override
    public void onDrawerItemSelected(View view, int position) {
        startActivity(new Intent(this, HomeActivity.class).putExtra("Drawer", position));

    }

    public static void viewFrame() {
        viewMain.setVisibility(View.VISIBLE);
        frameLayout.setVisibility(View.GONE);
    }

}