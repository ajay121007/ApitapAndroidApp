package com.apitap.views.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
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

public class FavStoreAdapter extends BaseAdapter /*RecyclerView.Adapter<StoreAdapter.ViewHolder>*/ implements BaseSliderView.OnSliderClickListener, ViewPagerEx.OnPageChangeListener {

    private final Activity activity;
    private List<MerchantStore.RESULT.RESULT_> response;
    LayoutInflater inflater;
    ArrayList<Favdetailsbean> favdetailsbeen = new ArrayList<>();
    private ArrayList<Favdetailsbean> favdetailsbeenlist = new ArrayList<Favdetailsbean>();
    private ArrayList<Storebean> arrayList = new ArrayList<Storebean>();
    HashMap<Integer, ArrayList<Storebean>> map;
    ArrayList<Storebean> allImages;
    TabLayout tabLayout;
    String selectedName;

    public FavStoreAdapter(Activity activity, HashMap<Integer, ArrayList<Storebean>> map,TabLayout tabLayout,String selectedName) {
        this.map = map;
        this.selectedName = selectedName;
        this.activity = activity;
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
        TextView mCategoryName, mViewAll;
        LinearListView mTwoWayView;
        TextView view_all;
        //  MyTwoWayAdapter adapter;
    }

    int currentIndex = 0;

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolders holder;
        // if (convertView == null) {
        holder = new ViewHolders();
        convertView = inflater.inflate(R.layout.vertical_row_fav, parent, false);
        holder.mTwoWayView = (LinearListView) convertView.findViewById(R.id.my_gallery);
        holder.mCategoryName = (TextView) convertView.findViewById(R.id.category_name);
        holder.view_all = (TextView) convertView.findViewById(R.id.view);
        holder.view_all.setVisibility(View.GONE);
        allImages = map.get(position);

        holder.mTwoWayView.setTag(position);
        holder.mCategoryName.setText(map.get(position).get(0).getCategoryName());
        if (map.get(position).get(0).getCategoryName().contains(selectedName)){
            holder.mTwoWayView.setAdapter(mAdapter);
        }else if (selectedName.equals("All")){
            holder.mTwoWayView.setAdapter(mAdapter);
        }else{
            holder.mCategoryName.setVisibility(View.GONE);
        }

        holder.mViewAll = (TextView) convertView.findViewById(R.id.view);


        return convertView;
    }

    /*@Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.vertical_row, null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        allImages = map.get(position);

        holder.mTwoWayView.setAdapter(mAdapter);
        holder.mTwoWayView.setTag(position);
        holder.mCategoryName.setText(map.get(position).get(0).getCategoryName());

    }

    @Override
    public int getItemCount() {
        return map.size();
    }*/

  /*  public class ViewHolder extends RecyclerView.ViewHolder {

        TextView mCategoryName, mViewAll;
        LinearListView mTwoWayView;

        public ViewHolder(View itemView) {
            super(itemView);
            mTwoWayView = (LinearListView) itemView.findViewById(R.id.my_gallery);
            mCategoryName = (TextView) itemView.findViewById(R.id.category_name);

        }
    }*/


    /*   @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, final int position) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.vertical_row, null);





        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {


        allImages=  map.get(position);
        holder.mTwoWayView.setAdapter(mAdapter);
        holder.mTwoWayView.setTag(position);
        holder.mCategoryName.setText(favdetailsbeenlist.get(position).getCategoryName());


      *//*  holder.txt_name.setText(response.get(position).getName());
      //  holder.txt_dealername.setText("by " + response.get(posZition).getDealerName());

        if (response.get(position).getPrice().equals(response.get(position).get11498()) || response.get(position).get11498().equals("") || response.get(position).get11498().equals("0") || response.get(position).get11498().equals("0.00")) {
            holder.rlSinglePrice.setVisibility(View.VISIBLE);
            holder.rlTwoPrice.setVisibility(View.GONE);
            holder.txt_price.setText("$"+response.get(position).getPrice());
        } else {
            holder.rlSinglePrice.setVisibility(View.GONE);
            holder.rlTwoPrice.setVisibility(View.VISIBLE);
            holder.txt_price.setText("$"+response.get(position).getPrice());
            holder.txt_discountedprice.setText("$"+response.get(position).get11498());
            holder.txt_price.setPaintFlags(holder.txt_price.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        }
     //   holder.txt_price.setText("$ " + response.get(position).getPrice());
        if (response.get(position).getImages().size() > 0)
            Picasso.with(activity).load(ATPreferences.readString(activity, Constants.KEY_IMAGE_URL) + response.get(position).getImages().get(0).getPath()).into(holder.img);

*//**//*        holder.txt_remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapterClick.onRemoveClick(v, position);
            }
        });*//**//*

        holder.eye.setVisibility(View.GONE);

        holder.img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("Positions",position+"");
                String productId = Utils.lengtT(11,favdetailsbeenlist.get(position).getId());;
                Log.d("ProductsID",productId);
                String productType =favdetailsbeenlist.get(position).getProductType();
                Log.d("productTypes",productType);
                String ActualPrice =favdetailsbeenlist.get(position).getActualPrice();
                String PriceAfterDiscount =favdetailsbeenlist.get(position).getPriceAfterDiscount();
                Bundle bundle = new Bundle();

             *//**//*   bundle.putString("productType", productType);
                bundle.putString("isFavorite", map.get(pos).get(position).getIsFavorite());
                bundle.putString("actualprice", map.get(pos).get(position).getActualPrice());
                bundle.putString("discountprice", map.get(pos).get(position).getPriceAfterDiscount());*//**//*
                bundle.putString("productId", productId);
                bundle.putString("productType", productType);
                bundle.putString("actualprice", ActualPrice);
                bundle.putString("discountprice", PriceAfterDiscount);
                bundle.putString("isFavorite", "1");
                FragmentDetails fragment = new FragmentDetails();
                fragment.setArguments(bundle);
                ((HomeActivity) activity).displayView(fragment, Constants.TAG_DETAILSPAGE, bundle);
            }
        });*//*

    }

    @Override
    public int getItemCount() {
        return map.size();
    }
*/
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
            //      description.setText(/*Utils.hexToASCII*/(response.get(position).getSellerName()));

       /*     if (arrayList.get(position).getActualPrice().equals(arrayList.get(position).getPriceAfterDiscount()) || arrayList.get(position).getPriceAfterDiscount().equals("") || arrayList.get(position).getPriceAfterDiscount().equals("0") || arrayList.get(position).getPriceAfterDiscount().equals("0.00")) {
                rlSinglePrice.setVisibility(View.VISIBLE);
                rlTwoPrice.setVisibility(View.GONE);
                actualPrice.setText("$"+arrayList.get(position).getActualPrice());
            } else {
                rlSinglePrice.setVisibility(View.GONE);
                rlTwoPrice.setVisibility(View.VISIBLE);
                actualPrice.setText("$"+arrayList.get(position).getActualPrice());
                priceAfterDiscount.setText("$"+arrayList.get(position).getPriceAfterDiscount());
                actualPrice.setPaintFlags(actualPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            }
*/
        /*    if (allImages.get(position).getPriceAfterDiscount().equals("") || allImages.get(position).getPriceAfterDiscount().equals("0.00")) {
                priceAfterDiscount.setVisibility(View.GONE);
                actualPrice.setText("$" + actualPrice);
            } else if (priceAfterDiscount.equals(actualPrice)) {
                priceAfterDiscount.setVisibility(View.GONE);
                actualPrice.setText("$" + actualPrice);
                actualPrice.setTextColor(Color.RED);
            }else if (allImages.get(position).getActualPrice().equals("0.00")&&Double.parseDouble(allImages.get(position).getActualPrice())<Double.parseDouble(allImages.get(position).getPriceAfterDiscount())) {
                priceAfterDiscount.setTextColor(Color.RED);
                priceAfterDiscount.setText("$" + priceAfterDiscount);
                actualPrice.setVisibility(View.GONE);
            }
            else
            {
                actualPrice.setText("$" + actualPrice);
                actualPrice.setTextColor(Color.RED);
                priceAfterDiscount.setText("$" + priceAfterDiscount);
            }*/

            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int pos = Integer.parseInt(parent.getTag().toString());
                    String merchantID = map.get(pos).get(position).getId();
                    Bundle b = new Bundle();
                    b.putBoolean("header_store",true);
                    b.putString("merchant_store",merchantID);
                    ATPreferences.putBoolean(activity,"header_store",true);
                    ATPreferences.putString(activity,"merchant_store",merchantID);
                   //
//                    tabLayout.setScrollPosition(0, 0f, true);
                    ((HomeActivity) activity).displayView(new FragmentHome(), "Home", b);
                  /*
                    Intent intent = new Intent(activity, MerchantStoreDetails.class);
                    intent.putExtra("merchantId", merchantID);
                    activity.startActivity(intent);*/
                }
            });
            Picasso.with(activity).load(ATPreferences.readString(activity, Constants.KEY_IMAGE_URL) + allImages.get(position).getImageUrl()).into(imageView);
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
