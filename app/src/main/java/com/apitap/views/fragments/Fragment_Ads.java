package com.apitap.views.fragments;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.apitap.R;
import com.apitap.controller.ModelManager;
import com.apitap.model.Constants;
import com.apitap.model.Operations;
import com.apitap.model.Utils;
import com.apitap.model.ViewPagerCustomDuration;
import com.apitap.model.bean.AdsBean;
import com.apitap.model.bean.AdsDetailWithMerchant;
import com.apitap.model.bean.AdsListBean;
import com.apitap.model.bean.NewAdBean;
import com.apitap.model.bean.NewStorebean;
import com.apitap.model.customclasses.Event;
import com.apitap.model.preferences.ATPreferences;
import com.apitap.views.AdDetailActivity;
import com.apitap.views.HomeActivity;
import com.apitap.views.adapters.AdsListingAdapter;
import com.apitap.views.adapters.SamplePagerAdapter;
import com.apitap.views.adapters.StoreAdapter;
import com.apitap.views.customviews.VerticalSpaceItemDecoration;
import com.github.rahatarmanahmed.cpv.CircularProgressView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import me.relex.circleindicator.CircleIndicator;

public class Fragment_Ads extends Fragment implements View.OnClickListener {


    private ViewHolder holder;
    TextView mTabAds, mTabSpecial, mTabItems, mTabSaved;
    ViewPagerCustomDuration viewPager;
    CircleIndicator circleIndicator;
    Dialog dialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment__ads, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        holder = new ViewHolder(view);


        holder.mPocketBar.setVisibility(View.VISIBLE);

        TabLayout tabLayout = (TabLayout) getActivity().findViewById(R.id.tabs);
        tabLayout.setVisibility(View.VISIBLE);

        viewPager = (ViewPagerCustomDuration) view.findViewById(R.id.viewpager);
        circleIndicator = (CircleIndicator) view.findViewById(R.id.indicator_default);
        mTabAds = (TextView) view.findViewById(R.id.tab_ads);
        mTabSpecial = (TextView) view.findViewById(R.id.tab_special);
        mTabItems = (TextView) view.findViewById(R.id.tab_items);
        mTabSaved = (TextView) view.findViewById(R.id.tab_saved);
        setListener(view);
        tabLayout.setScrollPosition(2, 0f, true);
        tabLayout.getTabAt(2).select();
        getfocus();
        ModelManager.getInstance().getAdsManager().getBusinessAds(getActivity(),
                Operations.makeJsonAdsByBusiness(getActivity()), Constants.ADS_LISTING_SUCCESS);

    }

    private void setListener(View v) {
        mTabAds.setOnClickListener(this);
        mTabSpecial.setOnClickListener(this);
        mTabItems.setOnClickListener(this);
        mTabSaved.setOnClickListener(this);
    }

    private class ViewHolder {

        private final ExpandableListView recycler;
        private final CircularProgressView mPocketBar;

        public ViewHolder(View view) {
            recycler = (ExpandableListView) view.findViewById(R.id.recyclerView);
            mPocketBar = (CircularProgressView) view.findViewById(R.id.pocket);
        }

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
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tab_ads:
                ((HomeActivity) getActivity()).displayView(new Fragment_Ads(), "Ads", new Bundle());

                break;
            case R.id.tab_special:
                ((HomeActivity) getActivity()).displayView(new FragmentSpecial(), "Special", new Bundle());
                break;
            case R.id.tab_items:
                ((HomeActivity) getActivity()).displayView(new FragmentItems(), "Items", new Bundle());
                break;
            case R.id.tab_saved:
                ((HomeActivity) getActivity()).displayView(new FragmentFavourite(), Constants.TAG_FAVOURITEPAGE, new Bundle());

                break;
        }
    }

    @Subscribe
    public void onEvent(final Event event) {
        switch (event.getKey()) {
            case Constants.ADS_LISTING_SUCCESS:
                clearfocus();
                showAdsWithAnimation();

                ArrayList<String> stringArrayList = new ArrayList<>();

                //  final HashMap<Integer, AdsBean> ads = ModelManager.getInstance().getHomeManager().ads;
                final ArrayList<AdsDetailWithMerchant> adsDetailWithMerchants = ModelManager.getInstance().getAdsManager().url_maps;

                final AdsListBean.RESULT list = ModelManager.getInstance().getAdsManager().adsListBean.getRESULT().get(0);

                NewAdBean newAdBean = new NewAdBean();
                List<NewAdBean> childBeen = new ArrayList<>();
                List<NewAdBean.ChildBean> childBean = new ArrayList<>();


                if (adsDetailWithMerchants != null && adsDetailWithMerchants.size() > 0) {

                    for (int i = 0; i < adsDetailWithMerchants.size(); i++) {
                        if (!stringArrayList.contains(adsDetailWithMerchants.get(i).getBusiness_type())) {
                            NewAdBean newAdBean1 = new NewAdBean();
                            newAdBean1.setTitle(adsDetailWithMerchants.get(i).getBusiness_type());
                            stringArrayList.add(adsDetailWithMerchants.get(i).getBusiness_type());
//                            Log.d("titleName",adsDetailWithMerchants.get(i).getBusiness_type());
                        }

                    }

                    for (int j = 0; j < stringArrayList.size(); j++) {
                        newAdBean = new NewAdBean();
                        childBean = new ArrayList<>();
                        for (int i = 0; i < adsDetailWithMerchants.size(); i++) {
                            if (stringArrayList.get(j).equals(adsDetailWithMerchants.get(i).getBusiness_type())) {
                                Log.d("titleNameloop", adsDetailWithMerchants.get(i).getBusiness_type() + "  " + stringArrayList.get(j));
                                NewAdBean.ChildBean childBean1 = new NewAdBean.ChildBean();
                                childBean1.setId(adsDetailWithMerchants.get(i).getId());
                                childBean1.setMerchantId(adsDetailWithMerchants.get(i).getMerchantId());
                                childBean1.setName(adsDetailWithMerchants.get(i).getName());
                                Log.d("NameinLoop", adsDetailWithMerchants.get(i).getName() + "  " + i);
                                childBean1.setMerchantname(adsDetailWithMerchants.get(i).getMerchantname());
                                childBean1.setIsSeen(adsDetailWithMerchants.get(i).getIsSeen());
                                childBean1.setImageUrl(ATPreferences.readString(getActivity(), Constants.KEY_IMAGE_URL) + adsDetailWithMerchants.get(i).getImageUrl());
                                childBean1.setVideoUrl(adsDetailWithMerchants.get(i).getVideo());
                                childBean1.setDesc(adsDetailWithMerchants.get(i).getDesc());
                                childBean.add(childBean1);
                            }
                        }
                        //  childBean1.setMeList(meArrayList);

                        newAdBean.setChildBeen(childBean);
                        childBeen.add(newAdBean);

                    }
                    AdsListingAdapter adp = new AdsListingAdapter(getActivity(), childBeen, stringArrayList, list.getRESULT(), adsDetailWithMerchants, holder.recycler);
                    holder.recycler.setAdapter(adp);
                    holder.recycler.expandGroup(0);

                }

//                adp.setOnItemClickListner(new AdsListingAdapter.AdapterClick() {
//                    @Override
//                    public void onItemClick(View v, int position) {
//                        Log.d("adsDetailWithMerchantss", adsDetailWithMerchants.get(position).getMerchantname());
//                        startActivity(new Intent(getActivity(), AdDetailActivity.class)
//                                .putExtra("videoUrl", ads   .get(position).getVideoUrl())
//                                .putExtra("image", ads.get(position).getImageUrl())
//                                .putExtra("merchant", adsDetailWithMerchants.get(position).getMerchantname())
//                                .putExtra("adName", adsDetailWithMerchants.get(position).getName())
//                                .putExtra("desc", adsDetailWithMerchants.get(position).getDesc())
//                                .putExtra("id", adsDetailWithMerchants.get(position).getId())
//                                .putExtra("merchantid", adsDetailWithMerchants.get(position).getMerchantId())
//                                .putExtra("adpos", position)
//                        );
//                    }
//                });

                holder.mPocketBar.setVisibility(View.GONE);

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
        TextView textView_yes = (TextView) dialog.findViewById(R.id.txtok);
        TextView textView_no = (TextView) dialog.findViewById(R.id.txtcancel);

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

    public void reloadFragment() {
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.detach(this).attach(this).commit();
    }

    Runnable r;
    Handler h;
    int count;

    private void showAdsWithAnimation() {

        if (ModelManager.getInstance().getHomeManager().ads != null)
            if (ModelManager.getInstance().getHomeManager().ads.size() > 0) {
                count = 0;
                final ArrayList<AdsDetailWithMerchant> adsDetailWithMerchants = ModelManager.getInstance().getAdsManager().url_maps;
                viewPager.setAdapter(new SamplePagerAdapter(getActivity(), ModelManager.getInstance().getHomeManager().ads, adsDetailWithMerchants, false, ""));
                circleIndicator.setViewPager(viewPager);
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
            }
    }

    public void onResume() {
        super.onResume();
        showAdsWithAnimation();
    }

    public void clearfocus() {
        getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }

    public void getfocus() {
        getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }

}
