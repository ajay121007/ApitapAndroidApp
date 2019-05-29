package com.apitap.views.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.apitap.R;
import com.apitap.model.Constants;
import com.apitap.model.Utils;
import com.apitap.model.bean.FavBean;
import com.apitap.model.bean.Favdetailsbean;

import com.apitap.model.bean.ImagesBean;
import com.apitap.model.bean.MerchantStore;

import com.apitap.model.bean.Storebean;
import com.apitap.model.preferences.ATPreferences;
import com.apitap.views.HomeActivity;
import com.apitap.views.MerchantStoreDetails;
import com.apitap.views.fragments.FragmentDetails;
import com.apitap.views.fragments.FragmentHome;
import com.apitap.views.fragments.FragmentSpecial;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.Tricks.ViewPagerEx;
import com.linearlistview.LinearListView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Sahil on 1/9/16.
 */

public class StoreAdapter extends BaseAdapter {

    private final Activity activity;
    LayoutInflater inflater;
    HashMap<Integer, ArrayList<Storebean>> map;
    ArrayList<Storebean> allImages;
    TabLayout tabLayout;
    String selectedName;
    boolean viewAdded = false;
    ArrayList<Integer> add_positions = new ArrayList<>();
    ArrayList<String> all_headers = new ArrayList<>();

    public StoreAdapter(Activity activity, HashMap<Integer, ArrayList<Storebean>> map, TabLayout tabLayout, String selectedName, ArrayList<Integer> add_positions) {
        this.map = map;
        this.selectedName = selectedName;
        this.activity = activity;
        this.add_positions = add_positions;
        inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.tabLayout = tabLayout;
    }

    @Override
    public int getCount() {
        return map.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }


    @Override
    public long getItemId(int position) {
        return position;
    }

    class ViewHolders {
        TextView mCategoryName;
        LinearListView mTwoWayView;
        TextView view_all, title;
        LinearLayout view_ll;
        LinearLayout parent_ll;
        CardView card_ll;
        LinearLayout title_ll;
    }

    int currentIndex = 0;

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolders holder;
        // if (convertView == null) {
        holder = new ViewHolders();
        convertView = inflater.inflate(R.layout.vertical_row, parent, false);
        holder.mTwoWayView = (LinearListView) convertView.findViewById(R.id.my_gallery);
        holder.mCategoryName = (TextView) convertView.findViewById(R.id.category_name);
        holder.view_all = (TextView) convertView.findViewById(R.id.view);
        holder.view_ll = (LinearLayout) convertView.findViewById(R.id.view_ll);
        holder.title_ll = (LinearLayout) convertView.findViewById(R.id.title_ll);
        holder.title = (TextView) convertView.findViewById(R.id.title);
        holder.parent_ll = (LinearLayout) convertView.findViewById(R.id.parentll);
        holder.card_ll = (CardView) convertView.findViewById(R.id.cardView);
        holder.title_ll.setVisibility(View.GONE);
        holder.view_all.setVisibility(View.GONE);

        allImages = map.get(position);

        holder.mTwoWayView.setTag(position);
        holder.mCategoryName.setText(map.get(position).get(0).getCategoryName());
        if (map.get(position).get(0).getTitle().contains(selectedName)) {
            holder.mTwoWayView.setAdapter(mAdapter);
        } else if ((selectedName.equals("All"))) {
            if (add_positions.contains(position)) {
                holder.title.setVisibility(View.VISIBLE);
                holder.title_ll.setVisibility(View.VISIBLE);
                holder.title.setText(map.get(position).get(0).getTitle());
                holder.mTwoWayView.setAdapter(mAdapter);
            } else {
                holder.title.setVisibility(View.GONE);
                holder.title_ll.setVisibility(View.GONE);
                holder.mTwoWayView.setAdapter(mAdapter);
            }

//            if (!all_headers.contains(map.get(position).get(0).getTitle())){
//                all_headers.add(map.get(position).get(0).getTitle());
//                holder.title.setText(map.get(position).get(0).getTitle());
//                holder.title.setVisibility(View.VISIBLE);
//               // addExtraTextView(convertView,map.get(position).get(0).getTitle());
//                holder.mTwoWayView.setAdapter(mAdapter);
//            }
//            else {
//                holder.title.setVisibility(View.GONE);
//                holder.title_ll.setVisibility(View.GONE);
//                holder.mTwoWayView.setAdapter(mAdapter);
//            }
        } else {
            holder.parent_ll.setVisibility(View.GONE);
            holder.card_ll.setVisibility(View.GONE);
        }
        return convertView;
    }

    public void addExtraTextView(View b, String j) {
        final LinearLayout lm = (LinearLayout) b.findViewById(R.id.title_ll);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        LinearLayout ll = new LinearLayout(activity);
        ll.setOrientation(LinearLayout.VERTICAL);

        TextView product = new TextView(activity);
        product.setText(j + "");
        product.setLayoutParams(params);
        ll.addView(product);
        lm.addView(ll);

        //((ViewGroup)itemView).addView(extraTextView, layoutParams);
        viewAdded = true;
    }


    private BaseAdapter mAdapter = new BaseAdapter() {

        @Override
        public View getView(final int position, View convertView, final ViewGroup parent) {
            if (convertView == null) {
                convertView = activity.getLayoutInflater().inflate(R.layout.row_horizontal_test, parent, false);
            }

            ImageView imageView = (ImageView) convertView.findViewById(R.id.image);
            TextView description = (TextView) convertView.findViewById(R.id.description);
            ImageView eye = (ImageView) convertView.findViewById(R.id.eye);

            LinearLayout rlSinglePrice = (LinearLayout) convertView.findViewById(R.id.rel_single_price);
            LinearLayout rlTwoPrice = (LinearLayout) convertView.findViewById(R.id.rl_two_price);
            TextView price = (TextView) convertView.findViewById(R.id.price);
            TextView actualPrice = (TextView) convertView.findViewById(R.id.actual_price);
            TextView priceAfterDiscount = (TextView) convertView.findViewById(R.id.price_after_discount);

            eye.setVisibility(View.GONE);

            imageView.setTag(position);

            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int pos = Integer.parseInt(parent.getTag().toString());
                    String merchantID = map.get(pos).get(position).getId();
                    Bundle b = new Bundle();
                    b.putBoolean("header_store", true);
                    b.putString("merchant_store", merchantID);
                    ATPreferences.putBoolean(activity, "header_store", true);
                    ATPreferences.putString(activity, "merchant_store", merchantID);

                    ((HomeActivity) activity).displayView(new FragmentHome(), "Home", b);
                }
            });
            Picasso.with(activity).load(ATPreferences.readString(activity, Constants.KEY_IMAGE_URL) + allImages.get(position).getImageUrl()).placeholder(R.drawable.logo_new).error(R.drawable.logo).into(imageView);
            description.setText(allImages.get(position).getName());
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
}
