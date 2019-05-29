package com.apitap.views.fragments;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.apitap.R;
import com.apitap.controller.ModelManager;
import com.apitap.model.Constants;
import com.apitap.model.Operations;
import com.apitap.model.Utils;
import com.apitap.model.bean.FavBean;
import com.apitap.model.bean.Favdetailsbean;
import com.apitap.model.bean.ImagesBean;
import com.apitap.model.bean.MerchantStore;
import com.apitap.model.bean.Storebean;
import com.apitap.model.customclasses.Event;
import com.apitap.views.HomeActivity;
import com.apitap.views.adapters.FavStoreAdapter;
import com.apitap.views.adapters.FavouriteAdapter;
import com.apitap.views.adapters.StoreAdapter;
import com.apitap.views.customviews.VerticalSpaceItemDecoration;
import com.github.rahatarmanahmed.cpv.CircularProgressView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import fr.castorflex.android.smoothprogressbar.SmoothProgressBar;
import fr.castorflex.android.smoothprogressbar.SmoothProgressBarUtils;
import fr.castorflex.android.smoothprogressbar.SmoothProgressDrawable;

public class Favorite_Fragment_Store extends Fragment {

    private ViewHolder holder;
    private Spinner sortby;
    private TabLayout tabLayout;
    private String selected_sort = "";
    private ArrayList<String> arraylist;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fav_fragment_store, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        holder = new ViewHolder(view);

//        ModelManager.getInstance().getMerchantStoresManager().getMerchantStoreDetail(getActivity(),
  //              Operations.makeJsonSearchStore(getActivity()));

    }

    private class ViewHolder {

        TextView mTabAds, mTabSpecial, mTabItems, mTabSaved;
        private final ListView recycler;
        private final CircularProgressView mPocketBar;


        public ViewHolder(View view) {
            tabLayout = (TabLayout) getActivity().findViewById(R.id.tabs);
            tabLayout.setVisibility(View.VISIBLE);
            recycler = (ListView) view.findViewById(R.id.recycler);
            mPocketBar = (CircularProgressView) view.findViewById(R.id.pocket);
            mTabAds = (TextView) view.findViewById(R.id.tab_ads);
            mTabSpecial = (TextView) view.findViewById(R.id.tab_special);
            mTabItems = (TextView) view.findViewById(R.id.tab_items);
            mTabSaved = (TextView) view.findViewById(R.id.tab_saved);
            sortby = (Spinner) view.findViewById(R.id.spinner_sort);
            sort_dialog();
            // tabLayout.setScrollPosition(1, 0f, true);
            mTabAds.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ((HomeActivity) getActivity()).displayView(new Fragment_Ads(), "Ads", new Bundle());

                }
            });
            mTabSpecial.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ((HomeActivity) getActivity()).displayView(new FragmentSpecial(), "Special", new Bundle());
                }
            });
            mTabItems.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ((HomeActivity) getActivity()).displayView(new FragmentItems(), "Items", new Bundle());
                }
            });
            mTabSaved.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ((HomeActivity) getActivity()).displayView(new FragmentFavourite(), Constants.TAG_FAVOURITEPAGE, new Bundle());

                }
            });
        }


    }

    private void sort_dialog() {
        arraylist = new ArrayList<>();


        arraylist.add("All");
        arraylist.add("Retail");
        arraylist.add("Restaurant");
        arraylist.add("Hospitality");
        arraylist.add("Services");

        ArrayAdapter<String> arrayadap = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, arraylist);
        sortby.setAdapter(arrayadap);

        sortby.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selected_sort = arraylist.get(i);
                setAdapter();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


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

            case Constants.STORES_SUCCESS:
                setAdapter();
                holder.mPocketBar.setVisibility(View.GONE);
                break;


        }
    }

    public void setAdapter() {
        HashMap<Integer, ArrayList<Storebean>> map = ModelManager.getInstance().getMerchantStoresManager().itemsData;
        if (map != null && map.size() > 0) {
            FavStoreAdapter adp = new FavStoreAdapter(getActivity(), map, tabLayout, selected_sort);
            holder.recycler.setAdapter(adp);
        }
    }


}
