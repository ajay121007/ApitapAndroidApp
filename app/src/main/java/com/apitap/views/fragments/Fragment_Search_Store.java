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
import android.widget.Toast;

import com.apitap.R;
import com.apitap.controller.ModelManager;
import com.apitap.model.Constants;
import com.apitap.model.Operations;
import com.apitap.model.Utils;
import com.apitap.model.bean.SearchItemBean;
import com.apitap.model.bean.ShopAsstSearchBean;
import com.apitap.model.bean.Storebean;
import com.apitap.model.customclasses.Event;
import com.apitap.views.HomeActivity;
import com.apitap.views.SearchItemActivity;
import com.apitap.views.adapters.FavoriteStoreAdapter;
import com.apitap.views.adapters.SearchItemAdapter;
import com.apitap.views.adapters.StoreAdapter;
import com.github.rahatarmanahmed.cpv.CircularProgressView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Fragment_Search_Store extends Fragment implements View.OnClickListener {

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
    private String isFavorite = "false";
    ArrayList<String> mernchantfavlist = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        bundle = getArguments();
        return inflater.inflate(R.layout.fragment_fav_store, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        holder = new ViewHolder(view);
        holder.tv_back.setOnClickListener(this);
//        ModelManager.getInstance().getMerchantStoresManager().getMerchantStoreDetail(getActivity(),
//                Operations.makeJsonSearchStore(getActivity()));

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                ((HomeActivity) getActivity()).displayView(new FragmentShoppingAsst(), Constants.TAG_SHOPPING_ASST, new Bundle());

                break;
        }
    }

    private class ViewHolder {

        TextView mTabAds, mTabSpecial, mTabItems, mTabSaved, mTitle, tv_back;
        private final ListView recycler;
        private LinearLayout title_ll;
        private final CircularProgressView mPocketBar;


        public ViewHolder(View view) {
            tvSearchItem = (TextView) getActivity().findViewById(R.id.tv_search_item);
            tv_back = (TextView) getActivity().findViewById(R.id.back);
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
                isFavorite = bundle.getString("isFav");
                // arrayListCat = bundle.getStringArrayList("list");
                if (isFavorite != null && isFavorite.equals("true")) {
                    ModelManager.getInstance().getMerchantFavouriteManager().getFavourites(getActivity(),
                            Operations.makeJsonGetMerchantFavourite(getActivity()));
                } else {
                    ModelManager.getInstance().getSearchItemsManager().getSearchShoppingAsst(getActivity(), Operations.makeJsonSearchForShopAsst(getActivity(), actionBar));
                }
                // ModelManager.getInstance().getSearchItemsManager().getAllSearchProduct(getActivity(), Operations.makeJsonSearchProduct(getActivity(), actionBar));

                tvSearchItem.setText(actionBar + "");
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
        ArrayAdapter<String> arrayadap = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, spinnerArrayList);
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
                //setAdapter();
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

            case Constants.SEARCH_ITEM_SUCCESS:
                boolean setAdap = false;
                holder.mPocketBar.setVisibility(View.GONE);
                final List<ShopAsstSearchBean.RESULT_> arrayList = ModelManager.getInstance().getSearchItemsManager().asstSearchBean.getRESULT().get(0).getRESULT();

                if (isFavorite.equals("true")) {
                    for (int i = 0; i < arrayList.size(); i++) {
                        if (mernchantfavlist.contains(arrayList.get(i).getPC().get(0).get11470())) {
                            setAdap = true;
                        }
                    }
                }
                if (!isFavorite.equals("true"))
                    setAdapter(arrayList);


                if (setAdap) {
                    setAdapter(arrayList);
                } else if (isFavorite.equals("true") && !setAdap)
                    Toast.makeText(getActivity(), "No item Found", Toast.LENGTH_SHORT).show();


                break;

            case Constants.GET_MERCHANT_FAVORITES:
                //    if (isFavorite.equals("1"))
                mernchantfavlist = ModelManager.getInstance().getMerchantFavouriteManager().mernchantfavlist;
                ModelManager.getInstance().getSearchItemsManager().getSearchShoppingAsst(getActivity(), Operations.makeJsonSearchForShopAsst(getActivity(), actionBar));
                break;

            case Constants.SEARCH_ITEM_SUCCESS_Empty:

                holder.mPocketBar.setVisibility(View.GONE);
                Toast.makeText(getActivity(), "No item Found", Toast.LENGTH_SHORT).show();
                break;

            case Constants.STORES_SUCCESS:
                clearfocus();
                //   setAdapter();
                //spinnerAdapter();
                holder.mPocketBar.setVisibility(View.GONE);
                break;


        }
    }

    public void setAdapter(final List<ShopAsstSearchBean.RESULT_> arrayList) {
        FavoriteStoreAdapter favoriteStoreAdapter = new FavoriteStoreAdapter(getActivity(), arrayList, tabLayout, mernchantfavlist);
        holder.recycler.setAdapter(favoriteStoreAdapter);
        holder.recycler.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String productId = Utils.lengtT(11, arrayList.get(i).getPC().get(0).get114144());
                String productType = arrayList.get(i).getPC().get(0).get114112();
                Bundle bundle = new Bundle();
                bundle.putString("productId", productId);
                bundle.putString("productType", productType);
                FragmentDetails fragment = new FragmentDetails();
                fragment.setArguments(bundle);
                ((HomeActivity) getActivity()).displayView(fragment, Constants.TAG_DETAILSPAGE, bundle);
            }
        });
    }

    public void clearfocus() {
        getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }

}
