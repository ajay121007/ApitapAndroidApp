package com.apitap.views.fragments;

/**
 * Created by Ravi on 29/07/15.
 */

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ListView;

import com.apitap.R;
import com.apitap.controller.ModelManager;
import com.apitap.model.Constants;
import com.apitap.model.Operations;
import com.apitap.model.customclasses.Event;
import com.apitap.views.HomeActivity;
import com.apitap.views.adapters.ShoppingAdapter;
import com.github.rahatarmanahmed.cpv.CircularProgressView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

public class ShoppingCartFragment extends Fragment {

    ListView mList;
    ViewPager mViewPager;
    CircularProgressView mPocketBar;

    public ShoppingCartFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_shopping_cart, container, false);

        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews(view);
        mPocketBar.setVisibility(View.VISIBLE);
        getfocus();
        tabContainer2Visible();
        ModelManager.getInstance().getShoppingCartManager().getShoppingCarts(getActivity(), Operations.makeJsonGetShoppingCart(getActivity()));
    }

    private void initViews(View v) {
        mList = (ListView) v.findViewById(R.id.list);
        mPocketBar = (CircularProgressView) v.findViewById(R.id.pocket);
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
            case Constants.SHOPPING_SUCCESS:
                clearfocus();
                mPocketBar.setVisibility(View.GONE);
                mList.setAdapter(new ShoppingAdapter(mPocketBar,getActivity(),0,ModelManager.getInstance().getShoppingCartManager().shoopingArray));
                break;

            case Constants.ITEM_DELETED:
                mPocketBar.setVisibility(View.VISIBLE);
                ModelManager.getInstance().getShoppingCartManager().getShoppingCarts(getActivity(), Operations.makeJsonGetShoppingCart(getActivity()));
                break;
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    public void tabContainer2Visible(){
        HomeActivity.tabContainer2.setVisibility(View.VISIBLE);
        HomeActivity.tabContainer1.setVisibility(View.GONE);
    }
    public void clearfocus() {
        getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }

    public void getfocus() {
        getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }
}
