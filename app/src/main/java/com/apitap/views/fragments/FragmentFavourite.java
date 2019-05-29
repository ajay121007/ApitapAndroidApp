package com.apitap.views.fragments;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.apitap.R;
import com.apitap.controller.ModelManager;
import com.apitap.model.Constants;
import com.apitap.model.Operations;
import com.apitap.model.Utils;
import com.apitap.model.ViewPagerCustomDuration;
import com.apitap.model.bean.AdsDetailWithMerchant;
import com.apitap.model.bean.FavBeanOld;
import com.apitap.model.bean.FavBeanSpecial;
import com.apitap.model.bean.FavMerchantBean;
import com.apitap.model.bean.Favdetailsbean;
import com.apitap.model.bean.Favspecialbean;
import com.apitap.model.customclasses.Event;
import com.apitap.model.preferences.ATPreferences;
import com.apitap.views.HomeActivity;
import com.apitap.views.adapters.FavouriteAdapter;
import com.apitap.views.adapters.FavouriteMerchantAdapter;
import com.apitap.views.adapters.FavouriteSpecialAdapter;
import com.apitap.views.adapters.SamplePagerAdapter;
import com.github.rahatarmanahmed.cpv.CircularProgressView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import me.relex.circleindicator.CircleIndicator;

public class FragmentFavourite extends Fragment {

    private ViewHolder holder;
    FavouriteAdapter adp;
    FavouriteSpecialAdapter adp2;
    FavouriteMerchantAdapter fav_adp;
    int state_Ads = 0, state_Items = 0, state_Store = 0,state_Special = 0;
    ArrayList<String> spinnerArrayList;
    private String selected_sort = "";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_fragment_favourite, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        holder = new ViewHolder(view);

        ModelManager.getInstance().getFavouriteManager().getFavourites(getActivity(),
                Operations.makeJsonGetFavourite(getActivity(), ""), Constants.GET_FAVOURITE_SUCCESS);

    }

    private class ViewHolder implements View.OnClickListener {

        TextView mTabAds, mTabSpecial, mTabItems, mTabSaved;
        private final RecyclerView recycler, recycler_Store,recycler_Special;
        private final CircularProgressView mPocketBar;
        private ViewPagerCustomDuration viewPager;
        private CircleIndicator circleIndicator;
        private LinearLayout linearLayout_Ads, linearLayout_stores, linearLayout_items,linearLayout_special;
        private LinearLayout loader_ll, main_ll;
        ImageView img_Ads, img_Store, img_Items,img_Specials;
        CardView ads_Card;
        Spinner spinner_sort;
        private TextView tvBusinessTitle;


        public ViewHolder(View view) {
            TabLayout tabLayout = (TabLayout) getActivity().findViewById(R.id.tabs);
            tabLayout.setVisibility(View.VISIBLE);
            tabContainer1Visible();
            LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
            LinearLayoutManager layoutManager1 = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
            recycler = (RecyclerView) view.findViewById(R.id.recycler);
            recycler_Store = (RecyclerView) view.findViewById(R.id.recyclerStore);
            recycler_Special = (RecyclerView) view.findViewById(R.id.recyclerSpecial);
            mPocketBar = (CircularProgressView) view.findViewById(R.id.pocket);
            mTabAds = (TextView) view.findViewById(R.id.tab_ads);
            tvBusinessTitle = (TextView) view.findViewById(R.id.title);
            mTabSpecial = (TextView) view.findViewById(R.id.tab_special);
            mTabItems = (TextView) view.findViewById(R.id.tab_items);
            mTabSaved = (TextView) view.findViewById(R.id.tab_saved);
            circleIndicator = (CircleIndicator) view.findViewById(R.id.indicator_default);
            viewPager = (ViewPagerCustomDuration) view.findViewById(R.id.viewpager);
            linearLayout_Ads = (LinearLayout) view.findViewById(R.id.lin_Ads);
            linearLayout_items = (LinearLayout) view.findViewById(R.id.lin_Items);
            linearLayout_stores = (LinearLayout) view.findViewById(R.id.title_lin_merchant);
            linearLayout_special = (LinearLayout) view.findViewById(R.id.lin_special);
            loader_ll = (LinearLayout) view.findViewById(R.id.load_ll);
            main_ll = (LinearLayout) view.findViewById(R.id.linear_main);
            img_Ads = (ImageView) view.findViewById(R.id.img_Ads);
            img_Store = (ImageView) view.findViewById(R.id.img_Stores);
            img_Specials = (ImageView) view.findViewById(R.id.img_Special);
            img_Items = (ImageView) view.findViewById(R.id.img_Items);
            ads_Card = (CardView) view.findViewById(R.id.ll_ads);
            spinner_sort = (Spinner) view.findViewById(R.id.spinner_sort);

            linearLayout_Ads.setOnClickListener(this);
            linearLayout_items.setOnClickListener(this);
            linearLayout_stores.setOnClickListener(this);
            linearLayout_special.setOnClickListener(this);
            tabLayout.setScrollPosition(5, 0f, true);
            tabLayout.getTabAt(5).select();
            getfocus();
            recycler.setNestedScrollingEnabled(false);
            recycler.setLayoutManager(layoutManager);

            recycler_Special.setNestedScrollingEnabled(false);
            recycler_Special.setLayoutManager(layoutManager1);
        }

        @Override
        public void onClick(View view) {
            switch (view.getId()) {

                case R.id.lin_Ads:

                    if (state_Ads == 0) {
                        state_Ads = 1;
                        img_Ads.setImageResource(R.drawable.ic_icon_downarrow);
                        ads_Card.setVisibility(View.GONE);
                    } else if (state_Ads == 1) {
                        state_Ads = 0;
                        img_Ads.setImageResource(R.drawable.ic_icon_uparrow);
                        ads_Card.setVisibility(View.VISIBLE);
                    }
                    break;
                case R.id.lin_Items:
                    if (state_Items == 0) {
                        state_Items = 1;
                        recycler.setVisibility(View.GONE);
                        img_Items.setImageResource(R.drawable.ic_icon_downarrow);
                    } else if (state_Items == 1) {
                        state_Items = 0;
                        recycler.setVisibility(View.VISIBLE);
                        img_Items.setImageResource(R.drawable.ic_icon_uparrow);
                    }
                    break;
                case R.id.lin_special:
                    if (state_Special == 0) {
                        state_Special = 1;
                        recycler_Special.setVisibility(View.GONE);
                        img_Specials.setImageResource(R.drawable.ic_icon_downarrow);
                    } else if (state_Special == 1) {
                        state_Special = 0;
                        recycler_Special.setVisibility(View.VISIBLE);
                        img_Specials.setImageResource(R.drawable.ic_icon_uparrow);
                    }
                    break;
                case R.id.title_lin_merchant:
                    if (state_Store == 0) {
                        state_Store = 1;
                        recycler_Store.setVisibility(View.GONE);
                        img_Store.setImageResource(R.drawable.ic_icon_downarrow);
                    } else if (state_Store == 1) {
                        state_Store = 0;
                        recycler_Store.setVisibility(View.VISIBLE);
                        img_Store.setImageResource(R.drawable.ic_icon_uparrow);
                    }
                    break;
            }
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

    @Subscribe
    public void onEvent(final Event event) {
        switch (event.getKey()) {

            case Constants.GET_FAVOURITE_SUCCESS:
                HashMap<Integer, ArrayList<Favdetailsbean>> map = ModelManager.getInstance().getFavouriteManager().itemsData;
//Gson Data which is set On Server
                spinnerArrayList = new ArrayList<>();
                for (int i = 0; i < map.size(); i++) {
                    if (!spinnerArrayList.contains(map.get(i).get(0).getBusiness_type())) {
                        spinnerArrayList.add(map.get(i).get(0).getBusiness_type());
                    }
                }

                holder.mPocketBar.setVisibility(View.GONE);
                holder.loader_ll.setVisibility(View.GONE);
                holder.main_ll.setVisibility(View.VISIBLE);
                clearfocus();
                break;
            case Constants.REMOVE_FAVOURITE_SUCCESS:

                Utils.showToast(getActivity(), "Removed");
                ModelManager.getInstance().getFavouriteManager().getFavourites(getActivity(),
                        Operations.makeJsonGetFavourite(getActivity(), ""), Constants.GET_FAVOURITE_SUCCESS);

                break;
            case Constants.GET_FAVOURITE_MERCHNAT_SUCCESS:
                //    if (isFavorite.equals("1"))

                List<FavMerchantBean.RESULT> result = ModelManager.getInstance().getFavouriteManager().favMerchantBean.getRESULT();
                for (int i = 0; i < result.size(); i++) {
                    for (int j = 0; j < result.get(i).getCU().size(); j++) {
                        if (!spinnerArrayList.contains(result.get(i).getCU().get(j).get_12083())) {
                            spinnerArrayList.add(result.get(i).getCU().get(j).get_12083());
                        }
                    }
                }
                setSpinnerAdapter();
               // setSpecialFavoriteAdapter();
                //setItemsFavoriteAdapter();
               // setMerchantFavoriteAdapter();
                break;
            case Constants.ADS_LISTING_SUCCESS:
                Log.d("Success", "success");
                showAdsWithAnimation();

                break;

        }

    }

    private void setItemsFavoriteAdapter() {
        List<FavBeanOld.RESULT> response = ModelManager.getInstance().getFavouriteManager().favBean.getRESULT();
        HashMap<Integer, ArrayList<Favdetailsbean>> map = ModelManager.getInstance().getFavouriteManager().itemsData;
//Gson Data which is set On Server

        if (map != null && map.size() > 0) {
            adp = new FavouriteAdapter(getActivity(), map, response,selected_sort);
            holder.recycler.setAdapter(adp);
        }
    }

    private void setSpecialFavoriteAdapter() {
        List<FavBeanSpecial.RESULT> response_special = ModelManager.getInstance().getFavouriteManager().favBeanSpecial.getRESULT();
        HashMap<Integer, ArrayList<Favspecialbean>> map_special = ModelManager.getInstance().getFavouriteManager().itemsDataSpecial;
//Gson Data which is set On Server

        if (map_special != null && map_special.size() > 0) {
            adp2 = new FavouriteSpecialAdapter(getActivity(), map_special, response_special,selected_sort);
            holder.recycler_Special.setAdapter(adp2);
        }
    }


    private void setMerchantFavoriteAdapter() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        holder.recycler_Store.setLayoutManager(layoutManager);
        List<FavMerchantBean.RESULT> result = ModelManager.getInstance().getFavouriteManager().favMerchantBean.getRESULT();

        for (int i =0;i<result.size();i++){

        }
        if (result.size() > 0) {
            fav_adp = new FavouriteMerchantAdapter(getActivity(), result,selected_sort);
            holder.recycler_Store.setAdapter(fav_adp);
        }
    }

    private void setSpinnerAdapter() {
        ArrayAdapter<String> arrayadap = new ArrayAdapter<String>(getActivity(), R.layout.spinner_item_new, spinnerArrayList);
        holder.spinner_sort.setAdapter(arrayadap);

        holder.spinner_sort.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selected_sort = spinnerArrayList.get(i);
                holder.tvBusinessTitle.setText(selected_sort);
                setMerchantFavoriteAdapter();
                setItemsFavoriteAdapter();
                setSpecialFavoriteAdapter();
                showAdsWithAnimation();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    public void clearfocus() {
        getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }

    public void getfocus() {
        getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }


    public void tabContainer1Visible() {
        HomeActivity.tabContainer2.setVisibility(View.GONE);
        HomeActivity.tabContainer1.setVisibility(View.VISIBLE);
    }

    Runnable r;
    Handler h;
    int count;

    private void showAdsWithAnimation() {

        if (ModelManager.getInstance().getFavouriteManager().ads != null)
            if (ModelManager.getInstance().getFavouriteManager().ads.size() > 0) {

                count = 0;
                final ArrayList<AdsDetailWithMerchant> adsDetailWithMerchants = ModelManager.getInstance().getFavouriteManager().url_maps1;
                holder.viewPager.setAdapter(new SamplePagerAdapter(getActivity(), ModelManager.getInstance().getFavouriteManager().ads, adsDetailWithMerchants, false, "fav",selected_sort));
                holder.circleIndicator.setViewPager(holder.viewPager);
                holder.viewPager.setCurrentItem(count);
                // viewPager.setPageTransformer(true, new ZoomOutPageTransformer());
                h = new Handler();
                r = new Runnable() {
                    @Override
                    public void run() {
                        h.removeMessages(0);
                        ++count;
                        if ((count + 1) > ModelManager.getInstance().getFavouriteManager().ads.size())
                            count = 0;

                        holder.viewPager.setCurrentItem(count);
                        h.postDelayed(r, 5000);
                    }
                };

                holder.viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
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
}