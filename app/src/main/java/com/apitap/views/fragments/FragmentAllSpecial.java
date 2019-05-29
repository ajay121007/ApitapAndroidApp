package com.apitap.views.fragments;


import android.content.Context;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.apitap.R;
import com.apitap.controller.ModelManager;
import com.apitap.model.Constants;
import com.apitap.model.Utils;
import com.apitap.model.bean.ImagesBean;
import com.apitap.views.HomeActivity;
import com.linearlistview.LinearListView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by apple on 11/08/16.
 */
public class FragmentAllSpecial extends Fragment implements View.OnClickListener{

    private GridView mGrid;
    TextView mHeader;
    ArrayList<ImagesBean> allImages;
    int position;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_all_specials, container, false);

        initViews(v);
        setListener(v);
        position=getArguments().getInt("position");
        mHeader.setText(getArguments().getString("header"));

        setAdapter();
        return v;
    }

    private void initViews(View v) {
        mGrid = (GridView) v.findViewById(R.id.gridView);
        mHeader=(TextView)v.findViewById(R.id.header);
    }
    private void setListener(View v) {
    }


    private void setAdapter() {
        HashMap<Integer, ArrayList<ImagesBean>> map = ModelManager.getInstance().getHomeManager().specialData;

        ArrayList<ImagesBean> array=map.get(position);
        if (array != null && array.size() > 0) {
            MyAdapter adapter = new MyAdapter(array);
            mGrid.setAdapter(adapter);
        }
    }

    @Override
    public void onClick(View view) {
    }

    class MyAdapter extends BaseAdapter {
        LayoutInflater inflater;
        ArrayList<ImagesBean> array;

        public MyAdapter(ArrayList<ImagesBean> array) {
            inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            this.array = array;
        }

        @Override
        public int getCount() {
            return array.size();
        }

        @Override
        public Object getItem(int position) {
            return array.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        class ViewHolder {
            TextView mCategoryName;
            LinearListView mTwoWayView;
            //  MyTwoWayAdapter adapter;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            ViewHolder holder;
            // if (convertView == null) {

            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.row_horizontal_test, parent, false);
            ImageView imageView = (ImageView) convertView.findViewById(R.id.image);
            TextView actualPrice = (TextView) convertView.findViewById(R.id.actual_price);
            TextView priceAfterDiscount = (TextView) convertView.findViewById(R.id.price_after_discount);
            TextView price = (TextView) convertView.findViewById(R.id.price);

            ImageView eye = (ImageView) convertView.findViewById(R.id.eye);
            LinearLayout rlSinglePrice = (LinearLayout) convertView.findViewById(R.id.rel_single_price);
            LinearLayout rlTwoPrice = (LinearLayout) convertView.findViewById(R.id.rl_two_price);
            TextView description = (TextView) convertView.findViewById(R.id.description);
            String isSeen = array.get(position).getIsSeen();
            if (isSeen.equalsIgnoreCase("false")) {
                eye.setBackgroundResource(R.drawable.green_seen);
            } else {
                eye.setVisibility(View.GONE);
            }

            if (array.get(position).getActualPrice().equals(array.get(position).getPriceAfterDiscount()) || array.get(position).getPriceAfterDiscount().equals("") || array.get(position).getPriceAfterDiscount().equals("0") || array.get(position).getPriceAfterDiscount().equals("0.00")) {
                rlSinglePrice.setVisibility(View.VISIBLE);
                rlTwoPrice.setVisibility(View.GONE);
                actualPrice.setText(array.get(position).getActualPrice());
            } else {
                rlSinglePrice.setVisibility(View.GONE);
                rlTwoPrice.setVisibility(View.VISIBLE);
                actualPrice.setText(array.get(position).getActualPrice());
                priceAfterDiscount.setText(array.get(position).getPriceAfterDiscount());
                actualPrice.setPaintFlags(actualPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            }

            description.setText(/*Utils.hexToASCII*/(array.get(position).getSellerName()));

            Picasso.with(getActivity()).load(array.get(position).getImageUrls()).into(imageView);
            convertView.setTag(holder);

            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String productId = Utils.lengtT(11, array.get(position).getProductId());
                    String productType = array.get(position).getProdcutType();
                    Bundle bundle = new Bundle();
                    bundle.putString("productId", productId);
                    bundle.putString("productType", productType);
                    bundle.putString("isFavorite", array.get(position).getIsFavorite());
                    bundle.putString("actualprice", array.get(position).getActualPrice());
                    bundle.putString("discountprice", array.get(position).getPriceAfterDiscount());
                    FragmentDetails fragment = new FragmentDetails();
                    fragment.setArguments(bundle);
                    ((HomeActivity) getActivity()).displayView(fragment, Constants.TAG_DETAILSPAGE, bundle);
                }
            });

            return convertView;
        }

        private BaseAdapter mAdapter = new BaseAdapter() {

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                if (convertView == null) {
                    convertView = getActivity().getLayoutInflater().inflate(R.layout.row_horizontal, parent, false);
                }

                ImageView imageView = (ImageView) convertView.findViewById(R.id.image);

                ImageView eye = (ImageView) convertView.findViewById(R.id.eye);
                String isSeen = allImages.get(position).getIsSeen();
                if (isSeen.equalsIgnoreCase("false")) {
                    eye.setBackgroundResource(R.drawable.green_seen);
                } else {
                    eye.setVisibility(View.GONE);
                }

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

    }

}
