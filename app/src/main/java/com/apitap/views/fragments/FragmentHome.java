package com.apitap.views.fragments;

/*
 * Created by Sourcefuse on 29/07/15.
 */

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.apitap.R;
import com.apitap.controller.ModelManager;
import com.apitap.model.Constants;
import com.apitap.model.Operations;
import com.apitap.model.Utils;
import com.apitap.model.ViewPagerCustomDuration;
import com.apitap.model.bean.AdsBean;
import com.apitap.model.bean.AdsDetailWithMerchant;
import com.apitap.model.bean.AdsListBean;
import com.apitap.model.bean.ImagesBean;
import com.apitap.model.bean.MerchantDetailBean;
import com.apitap.model.customclasses.Event;
import com.apitap.model.preferences.ATPreferences;
import com.apitap.views.HomeActivity;
import com.apitap.views.MerchantStoreDetails;
import com.apitap.views.adapters.SamplePagerAdapter;
import com.bumptech.glide.Glide;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.Tricks.ViewPagerEx;
import com.github.rahatarmanahmed.cpv.CircularProgressView;
import com.linearlistview.LinearListView;
import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import me.relex.circleindicator.CircleIndicator;

public class FragmentHome extends Fragment implements View.OnClickListener, BaseSliderView.OnSliderClickListener, ViewPagerEx.OnPageChangeListener {

    CardView mLlSpecial, mLlItems, mLlAds;
    TextView mTabAds, mTabSpecial, mTabItems, mTabSaved;
    CircularProgressView mPocketBar;
    LinearListView listView, listViewItems;
    //ScrollView scrollView;
    ViewPagerCustomDuration viewPager;
    CircleIndicator circleIndicator;
    TabLayout tabLayout;
    boolean header = false;
    private String merchantId = "";
    View rootView;
    boolean isADSrunning = false;
    private HomeActivity homeActivity;
    LinearLayout llheader;
    private MerchantDetailBean.RESULT.DetailData data;
    LinearLayout ll_txtSpecial;
    LinearLayout ll_txtItems;
    TextView tvStoreName;
    ImageView ivStoreImage;
    ImageView iv_NoStoreLogo;
    TextView tvstoreDetails;
    ScrollView scrollView;
    boolean isPause = false;
    HorizontalScrollView horizontalScrollView;
    CardView noSpecialsll, noItemsll, noAdsll;
    private boolean isViewVisible =false;
    Dialog dialog;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_home, container, false);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        homeActivity = new HomeActivity();

        initViews(view);
        setListener(view);

        mPocketBar.setVisibility(View.VISIBLE);
        getfocus();
        //testfragment();
    }

    @Override
    public void onPause() {
        super.onPause();
        isPause = true;
    }

    private void initViews(View v) {
        tabLayout = homeActivity.tabLayout;
        tabLayout.setVisibility(View.VISIBLE);
        tabContainer1Visible();

        horizontalScrollView = (HorizontalScrollView) v.findViewById(R.id.horizontal_scrollview);
        mLlSpecial = (CardView) v.findViewById(R.id.ll_specials);
        mLlAds = (CardView) v.findViewById(R.id.ll_ads);
        noSpecialsll = (CardView) v.findViewById(R.id.noSpecials);
        noItemsll = (CardView) v.findViewById(R.id.noItems);
        noAdsll = (CardView) v.findViewById(R.id.noAdsll);
        mLlItems = (CardView) v.findViewById(R.id.ll_items);
        llheader = (LinearLayout) v.findViewById(R.id.header);
        ll_txtSpecial = (LinearLayout) v.findViewById(R.id.txt_speciall);
        ll_txtItems = (LinearLayout) v.findViewById(R.id.txt_itemsll);
//        mParentSpecialItem = (LinearLayout) v.findViewById(R.id.parent_special_item);
        mPocketBar = (CircularProgressView) v.findViewById(R.id.pocket);
        //scrollView = (ScrollView) v.findViewById(R.id.scrollVIew);
        listView = (LinearListView) v.findViewById(R.id.list);
        listViewItems = (LinearListView) v.findViewById(R.id.list_items);
        //listViewSaved = (LinearListView) v.findViewById(R.id.list_saved);
        mTabAds = (TextView) v.findViewById(R.id.tab_ads);
        mTabSpecial = (TextView) v.findViewById(R.id.tab_special);
        mTabItems = (TextView) v.findViewById(R.id.tab_items);
        mTabSaved = (TextView) v.findViewById(R.id.tab_saved);
        //  mDemoSlider = (SliderLayout) v.findViewById(R.id.slider);

        ivStoreImage = (ImageView) v.findViewById(R.id.adstoreImg);
        iv_NoStoreLogo = (ImageView) v.findViewById(R.id.noStoreLogo);
        tvstoreDetails = (TextView) v.findViewById(R.id.details_store);
        tvStoreName = (TextView) v.findViewById(R.id.storeName);
        scrollView = (ScrollView) v.findViewById(R.id.mainscroll);
        circleIndicator = (CircleIndicator) v.findViewById(R.id.indicator_default);
        viewPager = (ViewPagerCustomDuration) v.findViewById(R.id.viewpager);
//        tabLayout.setScrollPosition(0, 0f, true);
//        TabLayout.Tab tab1 = tabLayout.getTabAt(0);
//        tab1.select();

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            header = bundle.getBoolean("header_store", false);
            merchantId = bundle.getString("merchant_store", "");
            if (header) {

//                tabLayout.setScrollPosition(0, 0f, true);
//                TabLayout.Tab tab = tabLayout.getTabAt(0);
//                tab.select();

            }
        }
        header = ATPreferences.readBoolean(getActivity(), "header_store");
        if (header) {
            merchantId = ATPreferences.readString(getActivity(), "merchant_store");
            tabContainer2Visible();
        } else {
            tabContainer1Visible();
        }
        ModelManager.getInstance().getHomeManager().getHome(getActivity(), Operations.makeJsonGetAds(getActivity(), merchantId, Constants.Alphabetical));

        ModelManager.getInstance().getAdsManager().getAllAds(getActivity(),
                Operations.makeJsonGetAdsListing(getActivity(), merchantId), Constants.ADS_LISTING_SUCCESS);


    }

    private void setListener(View v) {
        v.findViewById(R.id.view_specials).setOnClickListener(this);
        v.findViewById(R.id.view_items).setOnClickListener(this);
        v.findViewById(R.id.details_store).setOnClickListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (isPause)
            showAdsWithAnimation();
    }

    Runnable r;
    Handler h;
    int count;

    private void showAdsWithAnimation() {

        if (ModelManager.getInstance().getHomeManager().ads != null)
            if (ModelManager.getInstance().getHomeManager().ads.size() > 0) {

                count = 0;
                final ArrayList<AdsDetailWithMerchant> adsDetailWithMerchants = ModelManager.getInstance().getAdsManager().url_maps;
                viewPager.setAdapter(new SamplePagerAdapter(getActivity(), ModelManager.getInstance().getHomeManager().ads, adsDetailWithMerchants, header,""));
                circleIndicator.setViewPager(viewPager);
                tvStoreName.setText(ModelManager.getInstance().getHomeManager().ads.get(0).getMerchantName());
                Glide.with(getActivity()).load(ATPreferences.readString(getActivity(), Constants.KEY_IMAGE_URL) + ModelManager.getInstance().getHomeManager().ads.get(0).getMerchantLogo()).placeholder(R.drawable.loading).into(ivStoreImage);
                Glide.with(getActivity()).load(ATPreferences.readString(getActivity(), Constants.KEY_IMAGE_URL) + ModelManager.getInstance().getHomeManager().ads.get(0).getMerchantLogo()).placeholder(R.drawable.loading).into(ivStoreImage);

                if (header) {
                    llheader.setVisibility(View.VISIBLE);

                }
                // viewPager.setScrollDurationFactor(10);
                viewPager.setCurrentItem(count);
                // viewPager.setPageTransformer(true, new ZoomOutPageTransformer());
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
                if (header) {
                    ModelManager.getInstance().getMerchantManager().getMerchantDetail(getActivity(),
                            Operations.makeJsonGetMerchantDetail(getActivity(), merchantId), Constants.GET_MERCHANT_SUCCESS);
                }

                mLlAds.setVisibility(View.GONE);
                noAdsll.setVisibility(View.VISIBLE);
            }
    }

    private BaseAdapter mAdapter = new BaseAdapter() {

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = getActivity().getLayoutInflater().inflate(R.layout.row_horizontal_test, parent, false);
            }

            ImageView imageView = (ImageView) convertView.findViewById(R.id.image);
            TextView actualPrice = (TextView) convertView.findViewById(R.id.actual_price);
            TextView priceAfterDiscount = (TextView) convertView.findViewById(R.id.price_after_discount);
            TextView price = (TextView) convertView.findViewById(R.id.price);
            ImageView eye = (ImageView) convertView.findViewById(R.id.eye);
            LinearLayout rlSinglePrice = (LinearLayout) convertView.findViewById(R.id.rel_single_price);
            LinearLayout rlTwoPrice = (LinearLayout) convertView.findViewById(R.id.rl_two_price);
            TextView description = (TextView) convertView.findViewById(R.id.description);
            String isSeen = allImages.get(position).getIsSeen();

            if (isSeen.equalsIgnoreCase("false")) {
                eye.setBackgroundResource(R.drawable.green_seen);
            } else {
                eye.setVisibility(View.GONE);
            }

            String ActualPrice = allImages.get(position).getActualPrice();
            String DiscountPrice = allImages.get(position).getPriceAfterDiscount();
            Log.d("ActualPriceActualPrice",ActualPrice+"  "+DiscountPrice);

            if (ActualPrice.equals(DiscountPrice) || DiscountPrice.equals("0") || DiscountPrice.equals("0.00")) {
                rlSinglePrice.setVisibility(View.VISIBLE);
                rlTwoPrice.setVisibility(View.GONE);
                actualPrice.setText(ActualPrice);
            } else if (ActualPrice.equals("0") || ActualPrice.equals("0.00")) {
                rlTwoPrice.setVisibility(View.VISIBLE);
                rlSinglePrice.setVisibility(View.GONE);
                priceAfterDiscount.setText(DiscountPrice);
                actualPrice.setVisibility(View.GONE);
            } else if (Double.parseDouble(ActualPrice) > Double.parseDouble(DiscountPrice)) {
                rlTwoPrice.setVisibility(View.VISIBLE);
                rlSinglePrice.setVisibility(View.VISIBLE);
                priceAfterDiscount.setText((String.format("%.2f", Double.parseDouble(DiscountPrice))));
                priceAfterDiscount.setGravity(Gravity.END);
                actualPrice.setGravity(Gravity.START);
                actualPrice.setText(ActualPrice);
                actualPrice.setTextColor(getResources().getColor(R.color.colorRed));
                actualPrice.setPaintFlags(actualPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            }

            description.setText(Utils.hexToASCII(allImages.get(position).getSellerName()));
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String productId = Utils.lengtT(11, allImages.get(position).getProductId());
                    String productType = allImages.get(position).getProdcutType();
                    Bundle bundle = new Bundle();
                    bundle.putString("productId", productId);
                    bundle.putString("productType", productType);
                    bundle.putString("flag", "home");

                    ((HomeActivity) getActivity()).displayView(new FragmentDetails(), Constants.TAG_DETAILSPAGE, bundle);
                    tabContainer2Visible();
                }
            });

            Picasso.with(getActivity()).load(allImages.get(position).getImageUrls()).into(imageView);
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
            return allImages.size();
        }
    };

    private BaseAdapter mAdapterItems = new BaseAdapter() {

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = getActivity().getLayoutInflater().inflate(R.layout.row_horizontal_test, parent, false);
            }

            ImageView imageView = (ImageView) convertView.findViewById(R.id.image);
            TextView description = (TextView) convertView.findViewById(R.id.description);
            ImageView eye = (ImageView) convertView.findViewById(R.id.eye);
            String isSeen = allImagesItems.get(position).getIsSeen();
            LinearLayout rlSinglePrice = (LinearLayout) convertView.findViewById(R.id.rel_single_price);
            LinearLayout rlTwoPrice = (LinearLayout) convertView.findViewById(R.id.rl_two_price);
            TextView actualPrice = (TextView) convertView.findViewById(R.id.actual_price);
            TextView priceAfterDiscount = (TextView) convertView.findViewById(R.id.price_after_discount);
            if (isSeen.equalsIgnoreCase("false")) {
                eye.setBackgroundResource(R.drawable.green_seen);
            } else {
                eye.setVisibility(View.GONE);
            }
            description.setText(Utils.hexToASCII(allImagesItems.get(position).getSellerName()));

            String ActualPrice = allImagesItems.get(position).getActualPrice();
            String DiscountPrice = allImagesItems.get(position).getPriceAfterDiscount();
            Log.d("itemnameprice",Utils.hexToASCII(allImagesItems.get(position).getSellerName())+" "+ActualPrice+" "+DiscountPrice);

            if (Double.parseDouble(ActualPrice)==Double.parseDouble(DiscountPrice) || DiscountPrice.equals("0") || DiscountPrice.equals("0.00")||Double.parseDouble(DiscountPrice)>Double.parseDouble(ActualPrice)) {
                rlSinglePrice.setVisibility(View.VISIBLE);
                rlTwoPrice.setVisibility(View.GONE);
                actualPrice.setText("$" + ActualPrice);
            } else if (ActualPrice.equals("0") || ActualPrice.equals("0.00")) {
                rlTwoPrice.setVisibility(View.VISIBLE);
                rlSinglePrice.setVisibility(View.GONE);
                priceAfterDiscount.setText("$" + DiscountPrice);
                actualPrice.setVisibility(View.GONE);
            } else if (Double.parseDouble(ActualPrice) > Double.parseDouble(DiscountPrice)) {
                rlTwoPrice.setVisibility(View.VISIBLE);
                rlSinglePrice.setVisibility(View.VISIBLE);
                priceAfterDiscount.setText("$" + (String.format("%.2f", Double.parseDouble(DiscountPrice))));
                priceAfterDiscount.setGravity(Gravity.END);
                actualPrice.setGravity(Gravity.START);
                actualPrice.setText("$" + ActualPrice);
                actualPrice.setTextColor(getResources().getColor(R.color.colorRed));
                actualPrice.setPaintFlags(actualPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            }

//            if (allImagesItems.get(position).getActualPrice().equals(allImagesItems.get(position).getPriceAfterDiscount()) || allImagesItems.get(position).getPriceAfterDiscount().equals("") || allImagesItems.get(position).getPriceAfterDiscount().equals("0") || allImagesItems.get(position).getPriceAfterDiscount().equals("0.00")) {
//                rlSinglePrice.setVisibility(View.VISIBLE);
//                rlTwoPrice.setVisibility(View.GONE);
//                actualPrice.setText("$" + allImagesItems.get(position).getActualPrice());
//            } else {
//                rlSinglePrice.setVisibility(View.GONE);
//                rlTwoPrice.setVisibility(View.VISIBLE);
//                actualPrice.setText("$" + allImagesItems.get(position).getActualPrice());
//                priceAfterDiscount.setText("$" + allImagesItems.get(position).getPriceAfterDiscount());
//            }

            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String productId = Utils.lengtT(11, allImagesItems.get(position).getProductId());
                    String productType = allImagesItems.get(position).getProdcutType();
                    Bundle bundle = new Bundle();
                    bundle.putString("productId", productId);
                    bundle.putString("productType", productType);
                    bundle.putString("flag", "home");
                    FragmentDetails fragment = new FragmentDetails();
                    fragment.setArguments(bundle);
                    ((HomeActivity) getActivity()).displayView(fragment, Constants.TAG_DETAILSPAGE, bundle);
                    tabContainer2Visible();
                }
            });

            // Picasso.with(getActivity()).load(allImagesItems.get(position).getImageUrls()).fit().into(imageView);
            Picasso.with(getActivity()).load(allImagesItems.get(position).getImageUrls()).into(imageView);
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

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (r != null && h != null) {
            h.removeCallbacksAndMessages(null);
        }

        EventBus.getDefault().unregister(this);
    }

    @Subscribe
    public void onEvent(Event event) {
        switch (event.getKey()) {
            case Constants.ALL_IMAGES_SUCCESS:
                // setAds();
                setDataSpecial();
                setDataItems();
                mLlSpecial.setVisibility(View.VISIBLE);
//                mParentSpecialItem.setVisibility(View.VISIBLE);
                break;
            case Constants.GET_MERCHANT_SUCCESS:
                if (ModelManager.getInstance().getMerchantManager().merchantDetailBean.getRESULT().get(0).getRESULT().size() > 0) {
                    data = ModelManager.getInstance().getMerchantManager().merchantDetailBean.getRESULT().get(0).getRESULT().get(0);
                    llheader.setVisibility(View.VISIBLE);
                    tvStoreName.setText(data.getName());
                    Glide.with(getActivity()).load(ATPreferences.readString(getActivity(), Constants.KEY_IMAGE_URL) + data.getImage()).placeholder(R.drawable.loading).into(ivStoreImage);
                    Glide.with(getActivity()).load(ATPreferences.readString(getActivity(), Constants.KEY_IMAGE_URL) + data.getImage()).placeholder(R.drawable.loading).into(iv_NoStoreLogo);
                }
                break;
            case Constants.ADS_LISTING_SUCCESS:
                scrollView.setVisibility(View.VISIBLE);
                mPocketBar.setVisibility(View.GONE);
                clearfocus();
                showAdsWithAnimation();
                break;
            case Constants.ADDRESS_SUCCESS:
                Log.v("Address","Success");
                break;
            case Constants.GET_SERVER_ERROR:
                clearfocus();
                mPocketBar.setVisibility(View.GONE);
                Toast.makeText(getActivity(), "Server Error, Please try again.", Toast.LENGTH_SHORT).show();
                break;
            case -1:
                clearfocus();
                mPocketBar.setVisibility(View.GONE);
                Toast.makeText(getActivity(), "Problem occurred, please try again.", Toast.LENGTH_SHORT).show();
                break;
            case Constants.NOTIFICATION_ARRIVED:
                dialog = Utils.showReloadDialog(getActivity());
                reloadDialog();
                break;
            case Constants.FCM_NOTIFICATION:
                ModelManager.getInstance().getHomeManager().getHome(getActivity(),
                        Operations.makeJsonGetAds(getActivity(), "", Constants.Alphabetical));

                ModelManager.getInstance().getAdsManager().getAllAds(getActivity(),
                        Operations.makeJsonGetAdsListing(getActivity(), ""), Constants.NOTIFICATION_ARRIVED);
                break;

        }
    }

    private void reloadDialog() {
        TextView textView_yes = (TextView)dialog.findViewById(R.id.txtok);
        TextView textView_no = (TextView)dialog.findViewById(R.id.txtcancel);

        textView_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reloadFragment();
                dialog.dismiss();
            }
        });

        textView_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        dialog.show();

    }

    public void reloadFragment(){
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.detach(this).attach(this).commit();
    }


    ArrayList<ImagesBean> allImages;
    ArrayList<ImagesBean> allImagesItems;

    private void setDataSpecial() {


        final HashMap<Integer, ArrayList<ImagesBean>> map = ModelManager.getInstance().getHomeManager().specialData;

        final ArrayList<ImagesBean> allImages = new ArrayList<>();
        Iterator it = map.entrySet().iterator();
        ArrayList<String> matchList = new ArrayList<>();

        for (Map.Entry<Integer, ArrayList<ImagesBean>> entry : map.entrySet()) {
            ArrayList<ImagesBean> imagesArray = entry.getValue();

            for (int i = 0; i < imagesArray.size(); i++) {
                if (!matchList.contains(imagesArray.get(i).getDescription())) {
                    matchList.add(imagesArray.get(i).getDescription());
                    allImages.add(imagesArray.get(i));
                }
            }
        }
        if (allImages.size() > 0) {
            this.allImages = allImages;
            listView.setAdapter(mAdapter);
        } else {
            horizontalScrollView.setVisibility(View.GONE);
            mLlSpecial.setVisibility(View.GONE);
            ll_txtSpecial.setVisibility(View.GONE);
            noSpecialsll.setVisibility(View.VISIBLE);
        }
    }

    private void setDataItems() {
        final HashMap<Integer, ArrayList<ImagesBean>> map = ModelManager.getInstance().getHomeManager().itemsData;

        final ArrayList<ImagesBean> allImages = new ArrayList<>();
        for (Map.Entry<Integer, ArrayList<ImagesBean>> entry : map.entrySet()) {
            ArrayList<ImagesBean> imagesArray = entry.getValue();
            for (int i = 0; i < imagesArray.size(); i++) {
                allImages.add(imagesArray.get(i));
            }
        }
        if (allImages.size() > 0) {
            this.allImagesItems = allImages;
            listViewItems.setAdapter(mAdapterItems);
            mLlItems.setVisibility(View.VISIBLE);
        } else {
            mLlItems.setVisibility(View.GONE);
            ll_txtItems.setVisibility(View.GONE);
            noItemsll.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tab_ads:
                mTabAds.setBackgroundColor(Color.parseColor("#909090"));
                mTabSpecial.setBackgroundColor(Color.parseColor("#ffffff"));
                mTabItems.setBackgroundColor(Color.parseColor("#ffffff"));
                mTabSaved.setBackgroundColor(Color.parseColor("#ffffff"));

                ((HomeActivity) getActivity()).displayView(new Fragment_Ads(), "Ads", new Bundle());

                break;
            case R.id.tab_special:
                mTabSpecial.setBackgroundColor(Color.parseColor("#909090"));
                mTabAds.setBackgroundColor(Color.parseColor("#ffffff"));
                mTabItems.setBackgroundColor(Color.parseColor("#ffffff"));
                mTabSaved.setBackgroundColor(Color.parseColor("#ffffff"));

                ((HomeActivity) getActivity()).displayView(new FragmentSpecial(), "Special", new Bundle());
                break;
            case R.id.tab_items:
                mTabItems.setBackgroundColor(Color.parseColor("#909090"));
                mTabSpecial.setBackgroundColor(Color.parseColor("#ffffff"));
                mTabAds.setBackgroundColor(Color.parseColor("#ffffff"));
                mTabSaved.setBackgroundColor(Color.parseColor("#ffffff"));
                ((HomeActivity) getActivity()).displayView(new FragmentItems(), "Items", new Bundle());
                break;
            case R.id.details_store:
                startActivity(new Intent(getActivity(), MerchantStoreDetails.class)
                        .putExtra("merchantId", merchantId));
                break;

            case R.id.tab_saved:
                mTabSaved.setBackgroundColor(Color.parseColor("#909090"));
                mTabItems.setBackgroundColor(Color.parseColor("#ffffff"));
                mTabSpecial.setBackgroundColor(Color.parseColor("#ffffff"));
                mTabAds.setBackgroundColor(Color.parseColor("#ffffff"));
                ((HomeActivity) getActivity()).displayView(new FragmentFavourite(), Constants.TAG_FAVOURITEPAGE, new Bundle());
                break;

            case R.id.view_specials:
                tabLayout.setScrollPosition(3, 0f, true);
                tabLayout.getTabAt(3).select();

                ((HomeActivity) getActivity()).displayView(new FragmentSpecial(), "Special", new Bundle());
                break;

            case R.id.view_items:
                tabLayout.setScrollPosition(4, 0f, true);
                tabLayout.getTabAt(4).select();
                ((HomeActivity) getActivity()).displayView(new FragmentItems(), "Items", new Bundle());
                break;
        }
    }

    @Override
    public void onSliderClick(BaseSliderView slider) {

    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    public void clearfocus() {
        getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }

    public void getfocus() {
        getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }

    public void tabContainer2Visible() {
        homeActivity.tabContainer2.setVisibility(View.VISIBLE);
        homeActivity.tabContainer1.setVisibility(View.GONE);
    }

    public void tabContainer1Visible() {
        homeActivity.tabContainer2.setVisibility(View.GONE);
        homeActivity.tabContainer1.setVisibility(View.VISIBLE);
    }

    public void testfragment(){
        Fragment currentFragment = getFragmentManager().findFragmentById(R.id.container_body);
        Fragment_Ads test1 = (Fragment_Ads) getFragmentManager().findFragmentByTag("Ads");
        if (currentFragment instanceof FragmentHome) {
            Log.v("homevisile", "your Fragment is Visible");
        }
        else {
            //Whatever
            Log.d("homevisile","no");
        }

        if (currentFragment instanceof Fragment_Ads) {
            Log.v("adsvisile", "your Fragment is Visible");
        }
        else {
            //Whatever
            Log.d("adsvisile","no");
        }

        // retrieve from `ViewPager`'s adapter.
    }

}
