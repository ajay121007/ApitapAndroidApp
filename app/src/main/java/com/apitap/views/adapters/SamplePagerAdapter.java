package com.apitap.views.adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.apitap.R;
import com.apitap.controller.ModelManager;
import com.apitap.model.Constants;
import com.apitap.model.bean.AdsBean;
import com.apitap.model.bean.AdsDetailWithMerchant;
import com.apitap.model.customclasses.CustomImageView;
import com.apitap.model.preferences.ATPreferences;
import com.apitap.views.AdDetailActivity;
import com.apitap.views.HomeActivity;
import com.apitap.views.MerchantStoreDetails;
import com.apitap.views.fragments.Fragment_Ads;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;


public class SamplePagerAdapter extends PagerAdapter {

    private final Random random = new Random();
    private int mSize;
    ArrayList<String> business_list= new ArrayList<>();
    private Context context;
    private HashMap<Integer, AdsBean> ads;
    private LayoutInflater inflater;
    private boolean doNotifyDataSetChangedOnce = false;
    private boolean visible_header = true;
    private String isFav;
    private String selected_sort = "no";
    private ArrayList<AdsDetailWithMerchant> adsDetailWithMerchants;

    public SamplePagerAdapter() {
    }

    public SamplePagerAdapter(Context context, HashMap<Integer, AdsBean> ads, ArrayList<AdsDetailWithMerchant> adsDetailWithMerchants, boolean visible_header, String isFav) {
        this.ads = ads;
        this.adsDetailWithMerchants = adsDetailWithMerchants;
        this.context = context;
        this.visible_header = visible_header;
        this.isFav = isFav;
        inflater = LayoutInflater.from(context);


    }

    public SamplePagerAdapter(Context context, HashMap<Integer, AdsBean> ads, ArrayList<AdsDetailWithMerchant> adsDetailWithMerchants, boolean visible_header, String isFav, String selected_Sort) {
        this.ads = ads;
        this.adsDetailWithMerchants = adsDetailWithMerchants;
        this.context = context;
        this.visible_header = visible_header;
        this.isFav = isFav;
        this.selected_sort = selected_Sort;
        inflater = LayoutInflater.from(context);


    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        if (doNotifyDataSetChangedOnce) {
            doNotifyDataSetChangedOnce = false;
            notifyDataSetChanged();
        }
        return ads.size();
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        View image = inflater.inflate(R.layout.row_customize_image, container, false);
        assert image != null;
        final CustomImageView imageView = (CustomImageView) image.findViewById(R.id.image);
        final ImageView storeImage = (ImageView) image.findViewById(R.id.adstoreImg);
        final TextView storeName = (TextView) image.findViewById(R.id.storeName);
        final TextView storeTitle = (TextView) image.findViewById(R.id.storeTitle);
        final ImageView eye = (ImageView) image.findViewById(R.id.seen);
        final ImageView play = (ImageView) image.findViewById(R.id.play);
        final TextView showAll = (TextView) image.findViewById(R.id.showallads);
        final LinearLayout mainLayout = (LinearLayout) image.findViewById(R.id.mainlayout);

        final TextView storeDetails = (TextView) image.findViewById(R.id.details_store);
        final LinearLayout header = (LinearLayout) image.findViewById(R.id.header);
        Glide.with(context).load(ads.get(position).getImageUrl()).placeholder(R.drawable.ic_icon_loading).into(imageView);
        if (visible_header)
            //  header.setVisibility(View.VISIBLE);
            storeName.setText(ads.get(position).getMerchantName());

        if (isFav.equals("fav"))
            storeTitle.setText(ads.get(position).getMerchantName());
        //storeName.setText(adsDetailWithMerchants.get(position).getImageUrl());
        Glide.with(context).load(ATPreferences.readString(context, Constants.KEY_IMAGE_URL) + ads.get(position).getMerchantLogo()).diskCacheStrategy(DiskCacheStrategy.NONE).placeholder(R.drawable.loading).into(storeImage);
        String isSeen = ads.get(position).getSeen();
        Log.d("isSeenss", ads.get(position).getSeen() + "");
        if (isSeen.equalsIgnoreCase("false")) {
            eye.setBackgroundResource(R.drawable.green_seen);
        } else {
            eye.setVisibility(View.GONE);
        }
        if (ads.get(position).getVideoUrl().contains("mp4") || ads.get(position).getVideoUrl().contains("avi")) {
            play.setVisibility(View.VISIBLE);
        }

        showAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((HomeActivity) context).displayView(new Fragment_Ads(), "Ads", new Bundle());
            }
        });

        if (!selected_sort.equals("no")){
            business_list=ModelManager.getInstance().getFavouriteManager().business_typeList;
        }


        if (!selected_sort.equals("no") && business_list.contains(selected_sort)) {
            mainLayout.setVisibility(View.VISIBLE);
        } else {
            if (!selected_sort.equals("no")) {
                mainLayout.setVisibility(View.GONE);
            }
        }
        storeDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                context.startActivity(new Intent(context, MerchantStoreDetails.class)
                        .putExtra("merchantId", adsDetailWithMerchants.get(position).getMerchantId()));
            }
        });

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                context.startActivity(new Intent(context, AdDetailActivity.class)
                        .putExtra("videoUrl", ads.get(position).getVideoUrl())
                        .putExtra("image", ads.get(position).getImageUrl())
                        .putExtra("merchant", adsDetailWithMerchants.get(position).getMerchantname())
                        .putExtra("adName", adsDetailWithMerchants.get(position).getName())
                        .putExtra("desc", adsDetailWithMerchants.get(position).getDesc())
                        .putExtra("id", adsDetailWithMerchants.get(position).getId())
                        .putExtra("merchantid", adsDetailWithMerchants.get(position).getMerchantId())
                        .putExtra("adpos", position)
                );
            }
        });

        container.addView(image, 0);
        return image;
    }


    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view.equals(object);
    }

    @Override
    public void restoreState(Parcelable state, ClassLoader loader) {
    }

    @Override
    public Parcelable saveState() {
        return null;
    }
}