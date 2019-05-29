package com.apitap.views.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.apitap.R;
import com.apitap.controller.ModelManager;
import com.apitap.model.Constants;
import com.apitap.model.Operations;
import com.apitap.model.bean.Storebean;
import com.apitap.model.customclasses.Event;
import com.apitap.views.HomeActivity;
import com.apitap.views.adapters.StoreAdapter;
import com.github.rahatarmanahmed.cpv.CircularProgressView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.HashMap;

public class Fragment_Store extends Fragment {

    private String actionBar = "";
    private ViewHolder holder;
    private Spinner sortby;
    private TabLayout tabLayout;
    private String selected_sort = "";
    private ArrayList<String> arraylist;
    private TextView tvSearchItem;
    Bundle bundle = null;
    ArrayList<String> arrayListCat;
    ArrayList<String> spinnerArrayList = new ArrayList<>();
    ArrayList<Integer> add_Positions = new ArrayList<>();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        bundle = getArguments();
        return inflater.inflate(R.layout.fragment_store, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        holder = new ViewHolder(view);
        ModelManager.getInstance().getMerchantStoresManager().getMerchantStoreDetail(getActivity(),
                Operations.makeJsonSearchStore(getActivity()));

    }

    private class ViewHolder {

        TextView mTabAds, mTabSpecial, mTabItems, mTabSaved,mTitle;
        private final ListView recycler;
        private LinearLayout title_ll;
        private final CircularProgressView mPocketBar;


        public ViewHolder(View view) {
            tvSearchItem = (TextView) getActivity().findViewById(R.id.tv_search_item);
            tabLayout = (TabLayout) getActivity().findViewById(R.id.tabs);
            tabLayout.setVisibility(View.VISIBLE);
            recycler = (ListView) view.findViewById(R.id.recycler);
            mPocketBar = (CircularProgressView) view.findViewById(R.id.pocket);
            mTabAds = (TextView) view.findViewById(R.id.tab_ads);
            mTabSpecial = (TextView) view.findViewById(R.id.tab_special);
            mTabItems = (TextView) view.findViewById(R.id.tab_items);
            mTabSaved = (TextView) view.findViewById(R.id.tab_saved);
            mTitle = (TextView) view.findViewById(R.id.title);
            title_ll = (LinearLayout) view.findViewById(R.id.title_ll);
            sortby = (Spinner) view.findViewById(R.id.spinner_sort);
            spinnerArrayList.add("All");
            if (bundle != null) {
                actionBar = bundle.getString("asst");
                arrayListCat = bundle.getStringArrayList("list");
                tvSearchItem.setText(actionBar + "");
                if (arrayListCat != null && arrayListCat.size() > 0) {
                    for (int i = 0; i < arrayListCat.size(); i++) {
                        Log.d("Lisst", arrayListCat.get(i) + " ");
                    }
                }
            } else {
                tvSearchItem.setText("");
                tabLayout.setScrollPosition(1, 0f, true);
                tabLayout.getTabAt(1).select();
            }
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

    private void spinnerAdapter() {
        spinnerArrayList.add("All");
        ArrayAdapter<String> arrayadap = new ArrayAdapter<String>(getActivity(), R.layout.spinner_item_new, spinnerArrayList);
        sortby.setAdapter(arrayadap);

        sortby.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selected_sort = spinnerArrayList.get(i);

                if (selected_sort.equals("All"))
                    holder.title_ll.setVisibility(View.GONE);
                else {
                    holder.title_ll.setVisibility(View.VISIBLE);
                    holder.mTitle.setText(selected_sort);
                }
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
                clearfocus();
                setAdapter();
                spinnerAdapter();
                holder.mPocketBar.setVisibility(View.GONE);
                break;


        }
    }

    public void setAdapter() {
        HashMap<Integer, ArrayList<Storebean>> map = ModelManager.getInstance().getMerchantStoresManager().itemsData;

        if (map != null && map.size() > 0) {
            for (int i = 0; i < map.size(); i++) {
                if (!spinnerArrayList.contains(map.get(i).get(0).getTitle())) {
                    spinnerArrayList.add(map.get(i).get(0).getTitle());
                    add_Positions.add(i);
                }
            }
        }

        if (map != null && map.size() > 0) {
            StoreAdapter adp = new StoreAdapter(getActivity(), map, tabLayout, selected_sort,add_Positions);
            holder.recycler.setAdapter(adp);
        }
    }

    public void clearfocus() {
        getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }

}
