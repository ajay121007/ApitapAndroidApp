package com.apitap.views.fragments;


import android.app.Dialog;
import android.content.Context;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.Gravity;
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
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.Tricks.ViewPagerEx;
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
public class FragmentItems extends Fragment implements BaseSliderView.OnSliderClickListener, ViewPagerEx.OnPageChangeListener, View.OnClickListener {

    private ListView mList;
    LinearLayout sortby;
    TextView mTabAds, mTabSpecial, mTabItems, mTabSaved;
    private String sort_by,sortPostion="";
    private CircularProgressView mPocketBar;
    SortAdapter sortAdapter;
    private Spinner sortby_spinner;
    TextView tvBusinessTitle;
    ArrayList<String> spinnerArrayList;
    private String selected_sort="";
    TextView tvSortType;
    Dialog dialog;
    ArrayList<String> arraylist ;



    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_items, container, false);

        initViews(v);
        setListener(v);
        setAdapter();
        setSpinnerAdapter();
        return v;
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

    private void initViews(View v) {
        TabLayout tabLayout = (TabLayout) getActivity().findViewById(R.id.tabs);
        tabLayout.setVisibility(View.VISIBLE);
        tabContainer1Visible();
        tvSortType=(TextView)v.findViewById(R.id.tv_sort_type);
        mList = (ListView) v.findViewById(R.id.list);
        mTabAds = (TextView) v.findViewById(R.id.tab_ads);
        mTabSpecial = (TextView) v.findViewById(R.id.tab_special);
        mTabItems = (TextView) v.findViewById(R.id.tab_items);
        mTabSaved = (TextView) v.findViewById(R.id.tab_saved);
        sortby = (LinearLayout) v.findViewById(R.id.sortby);
        tvBusinessTitle = (TextView) v.findViewById(R.id.title);
        sortby_spinner = (Spinner) v.findViewById(R.id.spinner_sort);
        mPocketBar = (CircularProgressView) v.findViewById(R.id.pocket);
        tabLayout.setScrollPosition(4, 0f, true);
        tabLayout.getTabAt(4).select();

        arraylist  = new ArrayList<>();
        arraylist.add("Low to High");
        arraylist.add("High to Low");
        arraylist.add("Newest Arrivals");
        arraylist.add("Most Popular");
        //arraylist.add("Expiring Soon");
        arraylist.add("Sort A-Z");
        arraylist.add("Sort Z-A");

        if (!ATPreferences.getString(getActivity(),Constants.SAVED_SORT,"").isEmpty()){
            sortPostion=ATPreferences.getString(getActivity(),Constants.SAVED_SORT,"");
            tvSortType.setText(arraylist.get(Integer.parseInt(sortPostion)));
        }
        else {
            tvSortType.setText("");
        }
    }

    private void setListener(View v) {
        mTabAds.setOnClickListener(this);
        mTabSpecial.setOnClickListener(this);
        mTabItems.setOnClickListener(this);
        mTabSaved.setOnClickListener(this);
        sortby.setOnClickListener(this);
    }

    ArrayList<ImagesBean> allImages;

    private void setAdapter() {
        HashMap<Integer, ArrayList<ImagesBean>> map = ModelManager.getInstance().getHomeManager().itemsData;
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
        sortAdapter= new SortAdapter(getActivity(),arraylist,0);
        listview.setAdapter(sortAdapter);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                LinearLayout item_view = (LinearLayout) view.findViewById(R.id.root_view);
                item_view.setBackgroundColor(getResources().getColor(R.color.colorGrey));
                final CheckBox itemcheck = (CheckBox) view.findViewById(R.id.checkimg);
                sortBy(position);
                tvSortType.setText(arraylist.get(position));
                ATPreferences.putString(getActivity(),Constants.SAVED_SORT,position+"");
                ModelManager.getInstance().getHomeManager().getHome(getActivity(), Operations.makeJsonGetAds(getActivity(), /*ATPreferences.readString(getActivity(), "merchant_store")*/""
                        , sort_by));
                mPocketBar.setVisibility(View.VISIBLE);
                getfocus();
                dialog.dismiss();

            }
        });
        dialog.show();


    }

    public void sortBy(int position) {
        if (position == 0)
            sort_by = Constants.LowtoPriceHigh;
        else if (position == 1)
            sort_by = Constants.PriceHightoLow;
        else if (position == 2)
            sort_by = Constants.NewToOld;
        else if (position == 3)
            sort_by = Constants.MostPopular;
               /* else if (position == 4)
                    sort_by = Constants.EXPIRING_SOON;*/
        else if (position == 4)
            sort_by = Constants.Alphabetical;
        else if (position == 5)
            sort_by = Constants.REVERSE_Alphabetical;
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
            TextView mCategoryName, mViewAll;
            LinearListView mTwoWayView;
            LinearLayout title_ll;
            LinearLayout view_ll;
            LinearLayout parent_ll;
            CardView card_ll;
            //  MyTwoWayAdapter adapter;
        }

        int currentIndex = 0;

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            final ViewHolder holder;
            // if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.vertical_row, parent, false);
            holder.mTwoWayView = (LinearListView) convertView.findViewById(R.id.my_gallery);
            holder.mCategoryName = (TextView) convertView.findViewById(R.id.category_name);
            holder.mViewAll = (TextView) convertView.findViewById(R.id.view);
            holder.title_ll = (LinearLayout) convertView.findViewById(R.id.title_ll);
            holder.view_ll = (LinearLayout) convertView.findViewById(R.id.view_ll);
            holder.parent_ll = (LinearLayout) convertView.findViewById(R.id.parentll);
            holder.card_ll = (CardView) convertView.findViewById(R.id.cardView);
            allImages = map.get(position);


            if (map.get(position).get(0).getBusiness_type().contains(selected_sort)) {
                holder.mTwoWayView.setAdapter(mAdapter);
            }  else {
                holder.parent_ll.setVisibility(View.GONE);
                holder.card_ll.setVisibility(View.GONE);
            }


            holder.mTwoWayView.setTag(position);
            holder.mCategoryName.setText(map.get(position).get(0).getCategoryName());
            Log.d("catsnames", map.get(position).get(0).getCategoryName());


            holder.title_ll.setVisibility(View.GONE);
            holder.mViewAll.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Bundle bundle = new Bundle();
                    bundle.putInt("position", position);
                    bundle.putString("header", holder.mCategoryName.getText().toString());
                    ((HomeActivity) getActivity()).displayView(new FragmentAllItems(), Constants.TAG_VIEW_ALL_ITEMS, bundle);

                }
            });


            return convertView;
        }


        private BaseAdapter mAdapter = new BaseAdapter() {

            @Override
            public View getView(final int position, View convertView, final ViewGroup parent) {
                if (convertView == null) {
                    convertView = getActivity().getLayoutInflater().inflate(R.layout.row_horizontal_test, parent, false);
                }

                ImageView imageView = (ImageView) convertView.findViewById(R.id.image);
                TextView description = (TextView) convertView.findViewById(R.id.description);
                ImageView eye = (ImageView) convertView.findViewById(R.id.eye);
                String isSeen = allImages.get(position).getIsSeen();
                LinearLayout rlSinglePrice = (LinearLayout) convertView.findViewById(R.id.rel_single_price);
                LinearLayout rlTwoPrice = (LinearLayout) convertView.findViewById(R.id.rl_two_price);
                TextView actualPrice = (TextView) convertView.findViewById(R.id.actual_price);
                TextView priceAfterDiscount = (TextView) convertView.findViewById(R.id.price_after_discount);
                if (isSeen.equalsIgnoreCase("false")) {
                    eye.setBackgroundResource(R.drawable.green_seen);
                } else {
                    eye.setVisibility(View.GONE);
                }
                description.setText(Utils.hexToASCII(allImages.get(position).getSellerName()));

                String ActualPrice=allImages.get(position).getActualPrice();
                String DiscountPrice=allImages.get(position).getPriceAfterDiscount();

                if (Double.parseDouble(ActualPrice)==Double.parseDouble(DiscountPrice) || DiscountPrice.equals("0") || DiscountPrice.equals("0.00")||Double.parseDouble(DiscountPrice)>Double.parseDouble(ActualPrice)) {
                    rlSinglePrice.setVisibility(View.VISIBLE);
                    rlTwoPrice.setVisibility(View.GONE);
                    actualPrice.setText("$" + ActualPrice);

                }else if (ActualPrice.equals("0") || ActualPrice.equals("0.00")){
                    rlTwoPrice.setVisibility(View.VISIBLE);
                    rlSinglePrice.setVisibility(View.GONE);
                    priceAfterDiscount.setText("$" + DiscountPrice);
                    actualPrice.setVisibility(View.GONE);
                    Log.v("priceName",ActualPrice+"  "+DiscountPrice+"  "+Utils.hexToASCII(allImages.get(position).getSellerName()));

                }
                else if (Double.parseDouble(ActualPrice) > Double.parseDouble(DiscountPrice)) {
                    rlTwoPrice.setVisibility(View.VISIBLE);
                    rlSinglePrice.setVisibility(View.VISIBLE);
                    priceAfterDiscount.setText("$" + (String.format("%.2f", Double.parseDouble(DiscountPrice))));
                    actualPrice.setText("$" + ActualPrice);
                    priceAfterDiscount.setGravity(Gravity.END);
                    actualPrice.setGravity(Gravity.START);
                    actualPrice.setTextColor(getResources().getColor(R.color.colorRed));
                    actualPrice.setPaintFlags(actualPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                }


                imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int pos = Integer.parseInt(parent.getTag().toString());

                        String productId = Utils.lengtT(11, map.get(pos).get(position).getProductId());

                        ModelManager.getInstance().setProductSeen().setProductSeen(getActivity(), Operations.makeProductSeen(getActivity(), productId));


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

                Log.d("itemsImages", allImages.get(position).getImageUrls());
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

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onSliderClick(BaseSliderView slider) {

    }

    public void clearfocus() {
        getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }

    public void getfocus() {
        getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }

    public void tabContainer2Visible() {
        HomeActivity.tabContainer2.setVisibility(View.VISIBLE);
        HomeActivity.tabContainer1.setVisibility(View.GONE);
    }

    public void tabContainer1Visible() {
        HomeActivity.tabContainer2.setVisibility(View.GONE);
        HomeActivity.tabContainer1.setVisibility(View.VISIBLE);
    }
}
