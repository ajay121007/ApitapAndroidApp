package com.apitap.views.fragments;


import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
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
import com.apitap.model.bean.ImagesBean;
import com.apitap.model.customclasses.Event;
import com.apitap.model.preferences.ATPreferences;
import com.apitap.views.HomeActivity;
import com.apitap.views.adapters.SortAdapter;
import com.github.rahatarmanahmed.cpv.CircularProgressView;
import com.linearlistview.LinearListView;
import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by shami on 11/08/16.
 */
public class FragmentSpecial extends Fragment implements View.OnClickListener {

    private ListView mList;
    TextView mTabAds, mTabSpecial, mTabItems, mTabSaved;
    ArrayList<ImagesBean> allImages;
    LinearLayout sortby;
    private String sort_by = "",sortPostion="";
    private CircularProgressView mPocketBar;
    ArrayList<String> arraylist ;
    private TextView tvSortType;
    private Spinner sortby_spinner;
    TextView tvBusinessTitle;
    ArrayList<String> spinnerArrayList;
    private String selected_sort="";
    Dialog dialog;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_special, container, false);

        initViews(v);
        setListener();
        setAdapter();
        setSpinnerAdapter();
        return v;
    }

    private void initViews(View v) {
        TabLayout tabLayout = (TabLayout) getActivity().findViewById(R.id.tabs);
        tabLayout.setVisibility(View.VISIBLE);
        tabContainer1Visible();
        tvSortType=(TextView)v.findViewById(R.id.tv_sort_type);
        mList = (ListView) v.findViewById(R.id.list);
        mTabAds = (TextView) v.findViewById(R.id.tab_ads);
        mPocketBar = (CircularProgressView) v.findViewById(R.id.pocket);
        mTabSpecial = (TextView) v.findViewById(R.id.tab_special);
        mTabItems = (TextView) v.findViewById(R.id.tab_items);
        mTabSaved = (TextView) v.findViewById(R.id.tab_saved);
        sortby = (LinearLayout) v.findViewById(R.id.sortby);
        tvBusinessTitle = (TextView) v.findViewById(R.id.title);
        sortby_spinner = (Spinner) v.findViewById(R.id.spinner_sort);
        tabLayout = (TabLayout) getActivity().findViewById(R.id.tabs);
        tabLayout.setScrollPosition(3, 0f, true);
        tabLayout.getTabAt(3).select();

        arraylist = new ArrayList<>();
        arraylist.add("Newest Arrivals");
        arraylist.add("Expiring Soon");
        arraylist.add("Most Popular");
        arraylist.add("Sort A-Z");
        arraylist.add("Sort Z-A");

        if (!ATPreferences.getString(getActivity(),Constants.SPECIAL_SORT,"").isEmpty()){
            sortPostion=ATPreferences.getString(getActivity(),Constants.SPECIAL_SORT,"");
            tvSortType.setText(arraylist.get(Integer.parseInt(sortPostion)));
        }
        else {
            tvSortType.setText("");
        }
    }

    private void setListener() {
        sortby.setOnClickListener(this);
    }

    private void setSpinnerAdapter() {
        ArrayAdapter<String> arrayadap = new ArrayAdapter<String>(getActivity(), R.layout.spinner_item_new, spinnerArrayList);
        sortby_spinner.setAdapter(arrayadap);

        sortby_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selected_sort = spinnerArrayList.get(i);
                tvBusinessTitle.setText(selected_sort);
                setAdapter();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }


    private void setAdapter() {
        HashMap<Integer, ArrayList<ImagesBean>> map = ModelManager.getInstance().getHomeManager().specialData;
        spinnerArrayList =  new ArrayList<>();
        for (int i =0;i<map.size();i++) {
            if (!spinnerArrayList.contains(map.get(i).get(0).getBusiness_type())){
                spinnerArrayList.add(map.get(i).get(0).getBusiness_type());
            }
        }

        if (map != null && map.size() > 0) {
            MyAdapter adapter = new MyAdapter(map);
            mList.setAdapter(adapter);
        }


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tab_ads:
                ((HomeActivity) getActivity()).displayView(new Fragment_Ads(), "Ads", new Bundle());
                break;

            case R.id.tab_special:
                ((HomeActivity) getActivity()).displayView(new FragmentSpecial(), "Special", new Bundle());
                break;
            case R.id.tab_items:
                ((HomeActivity) getActivity()).displayView(new FragmentItems(), "Items", new Bundle());
                break;
            case R.id.tab_saved:
                ((HomeActivity) getActivity()).displayView(new FragmentFavourite(), Constants.TAG_FAVOURITEPAGE, new Bundle());

                break;
            case R.id.sortby:
                sort_dialog();
                break;
        }
    }

    private void sort_dialog() {

        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        dialog.setContentView(R.layout.dialog_list);
        final ListView listview = (ListView) dialog.findViewById(R.id.list);


        SortAdapter sortAdapter = new SortAdapter(getActivity(),arraylist,1);
        listview.setAdapter(sortAdapter);

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                LinearLayout item_view = (LinearLayout) view;
                item_view.setBackgroundColor(getResources().getColor(R.color.colorGrey));
                final CheckBox itemcheck = (CheckBox) item_view.findViewById(R.id.checkimg);

                 if (position == 0)
                    sort_by = Constants.NewToOld;
                else if (position == 1)
                    sort_by = Constants.EXPIRING_SOON;
                 else if (position == 2)
                     sort_by = Constants.MostPopular;
                 else if (position == 3)
                     sort_by = Constants.Alphabetical;
                 else if (position == 4)
                     sort_by = Constants.REVERSE_Alphabetical;

                if (itemcheck.isChecked()) {
                    itemcheck.setChecked(true);
                } else {
                    itemcheck.setChecked(false);
                }


                tvSortType.setText(arraylist.get(position));
                ATPreferences.putString(getActivity(),Constants.SPECIAL_SORT,position+"");
                ModelManager.getInstance().getHomeManager().getHome(getActivity(), Operations.makeJsonGetAds(getActivity(), ""
                        , sort_by));
                mPocketBar.setVisibility(View.VISIBLE);
                getfocus();
                dialog.dismiss();


            }
        });
        dialog.show();

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
            case Constants.ALL_IMAGES_SUCCESS:
                clearfocus();
                mPocketBar.setVisibility(View.GONE);
                setAdapter();
                break;

            case Constants.GET_SERVER_ERROR:
                clearfocus();
                mPocketBar.setVisibility(View.GONE);
                Toast.makeText(getActivity(), "Server Error, Please try again.", Toast.LENGTH_SHORT).show();
                break;
            case -1:
                clearfocus();
                mPocketBar.setVisibility(View.GONE);
                Toast.makeText(getActivity(), "Problem occurred, please try again.", Toast.LENGTH_SHORT).show();
                break;
            case Constants.NOTIFICATION_ARRIVED:
                dialog = Utils.showReloadDialog(getActivity());
                reloadDialog();
                break;
            case Constants.FCM_NOTIFICATION:
                ModelManager.getInstance().getHomeManager().getHome(getActivity(),
                        Operations.makeJsonGetAds(getActivity(), "", Constants.Alphabetical));

                ModelManager.getInstance().getAdsManager().getAllAds(getActivity(),
                        Operations.makeJsonGetAdsListing(getActivity(), ""), Constants.NOTIFICATION_ARRIVED);
                break;
        }
    }

    private void reloadDialog() {
        TextView textView_yes = (TextView)dialog.findViewById(R.id.txtok);
        TextView textView_no = (TextView)dialog.findViewById(R.id.txtcancel);

        textView_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reloadFragment();
                dialog.dismiss();
            }
        });

        textView_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        dialog.show();

    }

    public void reloadFragment(){
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.detach(this).attach(this).commit();
    }



    class MyAdapter extends BaseAdapter {
        LayoutInflater inflater;
        HashMap<Integer, ArrayList<ImagesBean>> map;

        public MyAdapter(HashMap<Integer, ArrayList<ImagesBean>> map) {
            inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            this.map = map;
        }

        @Override
        public int getCount() {
            return map.size();
        }

        @Override
        public Object getItem(int position) {
            return map.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        class ViewHolder {
            TextView mCategoryName;
            TextView mViewAll;
            LinearListView mTwoWayView;
            LinearLayout title_ll;
            LinearLayout parent_ll;
            CardView card_ll;
            //  MyTwoWayAdapter adapter;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            final ViewHolder holder;
            // if (convertView == null) {

            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.vertical_row, parent, false);
            holder.mTwoWayView = (LinearListView) convertView.findViewById(R.id.my_gallery);
            holder.mCategoryName = (TextView) convertView.findViewById(R.id.category_name);
            holder.title_ll = (LinearLayout) convertView.findViewById(R.id.title_ll);
            holder.mViewAll = (TextView) convertView.findViewById(R.id.view);
            holder.parent_ll = (LinearLayout) convertView.findViewById(R.id.parentll);
            holder.card_ll = (CardView) convertView.findViewById(R.id.cardView);
            allImages = map.get(position);


            holder.mTwoWayView.setAdapter(mAdapter);

            if (map.get(position).get(0).getBusiness_type().contains(selected_sort)) {
                holder.mTwoWayView.setAdapter(mAdapter);
            }  else {
                holder.parent_ll.setVisibility(View.GONE);
                holder.card_ll.setVisibility(View.GONE);
            }

            holder.mTwoWayView.setTag(position);
            holder.mCategoryName.setText(map.get(position).get(0).getCategoryName());
            holder.title_ll.setVisibility(View.GONE);
            holder.mViewAll.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Bundle bundle = new Bundle();
                    bundle.putInt("position", position);
                    bundle.putString("header", holder.mCategoryName.getText().toString());
                    ((HomeActivity) getActivity()).displayView(new FragmentAllSpecial(), Constants.TAG_VIEW_ALL_SPECIALS, bundle);
                }
            });



            convertView.setTag(holder);

            return convertView;
        }

        private BaseAdapter mAdapter = new BaseAdapter() {

            @Override
            public View getView(final int position, View convertView, final ViewGroup parent) {
                if (convertView == null) {
                    convertView = getActivity().getLayoutInflater().inflate(R.layout.row_horizontal_test, parent, false);
                }

                ImageView imageView = (ImageView) convertView.findViewById(R.id.image);

                TextView actualPrice = (TextView) convertView.findViewById(R.id.actual_price);
                TextView priceAfterDiscount = (TextView) convertView.findViewById(R.id.price_after_discount);
                TextView price = (TextView) convertView.findViewById(R.id.price);

                ImageView eye = (ImageView) convertView.findViewById(R.id.eye);
                LinearLayout rlSinglePrice = (LinearLayout) convertView.findViewById(R.id.rel_single_price);
                LinearLayout rlTwoPrice = (LinearLayout) convertView.findViewById(R.id.rl_two_price);
                TextView description = (TextView) convertView.findViewById(R.id.description);
                String isSeen = allImages.get(position).getIsSeen();
                if (isSeen.equalsIgnoreCase("false")) {
                    eye.setBackgroundResource(R.drawable.green_seen);
                } else {
                    eye.setVisibility(View.GONE);
                    //eye.setBackgroundResource(R.drawable.green_seen);
                }

                if (allImages.get(position).getActualPrice().equals(allImages.get(position).getPriceAfterDiscount()) || allImages.get(position).getPriceAfterDiscount().equals("") || allImages.get(position).getPriceAfterDiscount().equals("0") || allImages.get(position).getPriceAfterDiscount().equals("0.00")) {
                    rlSinglePrice.setVisibility(View.VISIBLE);
                    rlTwoPrice.setVisibility(View.GONE);
                    actualPrice.setText(/*"$"+*/allImages.get(position).getActualPrice());
                } else {
                    rlSinglePrice.setVisibility(View.GONE);
                    rlTwoPrice.setVisibility(View.VISIBLE);
                    actualPrice.setTextColor(Color.GRAY);
                    actualPrice.setText(/*"$" + */allImages.get(position).getActualPrice());
                    priceAfterDiscount.setText(/*"$" +*/ allImages.get(position).getPriceAfterDiscount());
                    actualPrice.setPaintFlags(actualPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

                }


                description.setText(Utils.hexToASCII(allImages.get(position).getSellerName()));
                imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int pos = Integer.parseInt(parent.getTag().toString());
                        String productId = Utils.lengtT(11, map.get(pos).get(position).getProductId());
                        String productType = map.get(pos).get(position).getProdcutType();
                        Bundle bundle = new Bundle();
                        bundle.putString("productId", productId);
                        bundle.putString("productType", productType);
                        bundle.putString("isFavorite", map.get(pos).get(position).getIsFavorite());
                        bundle.putString("actualprice", map.get(pos).get(position).getActualPrice());
                        bundle.putString("discountprice", map.get(pos).get(position).getPriceAfterDiscount());
                        FragmentDetails fragment = new FragmentDetails();
                        fragment.setArguments(bundle);
                        ((HomeActivity) getActivity()).displayView(fragment, Constants.TAG_DETAILSPAGE, bundle);
                        tabContainer2Visible();
                    }
                });
                Log.d("hellospecials", "hELLO" + allImages.get(position).getImageUrls());
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

    public void clearfocus() {
        getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }

    public void getfocus() {
        getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }

    public void tabContainer2Visible(){
        HomeActivity.tabContainer2.setVisibility(View.VISIBLE);
        HomeActivity.tabContainer1.setVisibility(View.GONE);
    }
    public void tabContainer1Visible(){
        HomeActivity.tabContainer2.setVisibility(View.GONE);
        HomeActivity.tabContainer1.setVisibility(View.VISIBLE);
    }
}
