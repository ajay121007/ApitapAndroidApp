package com.apitap.views.fragments;


import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
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
public class FragmentAllSpecialData extends Fragment implements View.OnClickListener{

    private GridView mGrid;
    TextView mHeader;
    ArrayList<ImagesBean> allImages;
    int position;
    ArrayList<ImagesBean> array=new ArrayList<>();

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_all_specials, container, false);

        initViews(v);
        setListener(v);
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
        for (int key: map.keySet()) {
            array.addAll(map.get(key));
        }
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
            convertView = inflater.inflate(R.layout.row_horizontal, parent, false);
            ImageView imageView = (ImageView) convertView.findViewById(R.id.image);

            ImageView eye = (ImageView) convertView.findViewById(R.id.eye);
            String isSeen = array.get(position).getIsSeen();
            if (isSeen.equalsIgnoreCase("false")) {
                eye.setBackgroundResource(R.drawable.green_seen);
            } else {
                eye.setVisibility(View.GONE);
            }

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
