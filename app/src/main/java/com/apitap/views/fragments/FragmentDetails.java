package com.apitap.views.fragments;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.apitap.R;
import com.apitap.controller.ModelManager;
import com.apitap.model.Constants;
import com.apitap.model.Operations;
import com.apitap.model.Utils;
import com.apitap.model.bean.CategoryDetailsBean;
import com.apitap.model.bean.DetailsBean;
import com.apitap.model.bean.ProductDetailsBean;
import com.apitap.model.bean.ProductOptions2Bean;
import com.apitap.model.bean.ProductOptionsBean;
import com.apitap.model.bean.RelatedDetailsBean;
import com.apitap.model.bean.SizeBean;
import com.apitap.model.bean.Sizedata;
import com.apitap.model.bean.SpecialItemBean;
import com.apitap.model.customclasses.Event;
import com.apitap.model.customclasses.NestedListView;
import com.apitap.model.preferences.ATPreferences;
import com.apitap.views.AdDetailActivity;
import com.apitap.views.ForgotPasswordActivity;
import com.apitap.views.HomeActivity;
import com.apitap.views.LoginActivity;
import com.apitap.views.MerchantStoreDetails;
import com.apitap.views.MessageDetailActivity;
import com.apitap.views.SearchItemActivity;
import com.apitap.views.ZoomImage;
import com.apitap.views.adapters.SubImagesAdapter;
import com.github.rahatarmanahmed.cpv.CircularProgressView;
import com.linearlistview.LinearListView;
import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ashok-kumar on 9/6/16.
 */

public class FragmentDetails extends Fragment implements View.OnClickListener, AdapterView.OnItemClickListener {
    EditText editEmail, editPassword;
    TextView actual_price, price_afterdiscount, storeName, more_details;
    Activity mActivity;
    LinearLayout llAddToCart;
    ScrollView scrollView;
    ImageView icImage, ivFav;
    NestedListView listSubInages;
    GridView list;
    RelativeLayout layout;
    TextView sellerName, txtRelatedItems, txtCategorySpecialItems, txtCategoryItems, productDesc, txtTitle, desc, minus, plus, tv_quantity;
    LinearLayout layoutFavorite, llQuantity;
    ImageView back_tv;
    CircularProgressView mPocketBar;
    String isFavorite, productImage, productId, productName, productType;
    String merchantID = "";
    LinearListView listRelatedItems, listCategoryItems;
    LinearListView listSpecialRelatedItems;
    CardView layAttributes, layRelatedItems, layCategoryItems, layCategoryspecialItems;
    List<RelatedDetailsBean.RESULT_> relatedArray;
    List<SpecialItemBean.RESULT> specialArrayRelated;
    List<CategoryDetailsBean.RESULT> catArray;
    Spinner spinnerSize, spinnerColor;
    Uri uri;
    LinearLayout llListImages;
    TextView thumbNa;
    String actualPrice, priceAfterDiscount;
    ArrayList<ProductOptionsBean> productOptionsArrayList = new ArrayList<ProductOptionsBean>();
    ArrayList<ProductOptions2Bean> productOptionsArrayList2 = new ArrayList<ProductOptions2Bean>();
    ArrayList<String> productOptionsArrayList2Str = new ArrayList<String>();
    ArrayList<String> productOptionsArrayList3 = new ArrayList<String>();
    GetOption1 adp1;
    GetOption2 adp2;
    ArrayList<String> option_Ids = new ArrayList<String>();
    Spinner option1, option2;
    Button detailStore;
    ImageView inbox;
    LinearListView listView, listViewItems, listViewSaved;
    String option_Id, option_Id2;
    String str_option_Id = "", str_option_Id2 = "";
    int state = 0;
    boolean isFavouriteboolean = false;
    private boolean isGuest = false;
    private int related_pos = 99;
    private String barcode;
    private String specs;
    private String sku;
    private String model;
    private String age21;
    private String age18;
    private String limit_quantity;
    private String flag = "";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_details, container, false);
        return v;
    }

    @Override
    public void onViewCreated(View v, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(v, savedInstanceState);
        mActivity = getActivity();
        initViews(v);
        setListeners(v);


        Bundle b = getArguments();
        if (b != null) {
            isGuest = ATPreferences.readBoolean(getActivity(), "guest");
            productId = b.getString("productId");
            productType = b.getString("productType");
            flag = b.getString("flag");


            mPocketBar.setVisibility(View.VISIBLE);
            getfocus();

            //  ModelManager.getInstance().setProductSeen().setProductSeen(getActivity(), Operations.makeProductSeen(getActivity(), productId));
            //   ModelManager.getInstance().setProductSeen().setCounter(getActivity(), Operations.makeJsonCounter(getActivity(), productId));
            //   ModelManager.getInstance().getProductOptions().getOption1(getActivity(), Operations.makeJsonGetOptions(getActivity(), productId));
            //    ModelManager.getInstance().getDetailsManager().getItemsByCategory(getActivity(), Operations.makeJsonGetItemsByCategoryViews(getActivity(), productId));
            //    ModelManager.getInstance().getFavouriteManager().getFavourites(getActivity(),
            //         Operations.makeJsonGetFavourite(getActivity(), ""), Constants.GET_FAVOURITE_SUCCESS);


            ModelManager.getInstance().getDetailsManager().getDetails(getActivity(), Operations.makeJsonGetItems(getActivity(), productId, ATPreferences.readString(mActivity, Constants.KEY_USERID),productType), false);

            //21 for items
            if (productType.equals("21")) {
                ModelManager.getInstance().getDetailsManager().getDetails(getActivity(), Operations.makeJsonGetRelatedItems(getActivity(), productId), true);
                txtCategorySpecialItems.setVisibility(View.GONE);
                layCategoryspecialItems.setVisibility(View.GONE);
                more_details.setVisibility(View.VISIBLE);
                llAddToCart.setVisibility(View.VISIBLE);
            } else {
                txtRelatedItems.setVisibility(View.GONE);
                layCategoryItems.setVisibility(View.GONE);
                more_details.setVisibility(View.INVISIBLE);
                llAddToCart.setVisibility(View.INVISIBLE);
                ModelManager.getInstance().getDetailsManager().getSpecialDetails(getActivity(), Operations.makeJsonSpecialItemrequired(getActivity(), productId));
            }
        }
    }

    private void setListeners(View v) {
        inbox.setOnClickListener(this);
        layoutFavorite.setOnClickListener(this);
        back_tv.setOnClickListener(this);
        detailStore.setOnClickListener(this);
        icImage.setOnClickListener(this);
        storeName.setOnClickListener(this);
        more_details.setOnClickListener(this);
        listSubInages.setOnItemClickListener(this);
        llAddToCart.setOnClickListener(this);
        minus.setOnClickListener(this);
        plus.setOnClickListener(this);


        // v.findViewById(R.id.ll_back).setOnClickListener(this);
        v.findViewById(R.id.ic_back).setOnClickListener(this);
        v.findViewById(R.id.ll_add_to_cart).setOnClickListener(this);
        v.findViewById(R.id.store_name).setOnClickListener(this);
    }

    private void initViews(View v) {
        TabLayout tabLayout = (TabLayout) getActivity().findViewById(R.id.tabs);
        if (tabLayout != null)
            tabLayout.setVisibility(View.VISIBLE);

        llListImages = (LinearLayout) v.findViewById(R.id.ll_list_images);
        thumbNa = (TextView) v.findViewById(R.id.tvthumb);
        spinnerSize = (Spinner) v.findViewById(R.id.spinner);
        actual_price = (TextView) v.findViewById(R.id.actual_price);
        price_afterdiscount = (TextView) v.findViewById(R.id.price_after_discount);
        storeName = (TextView) v.findViewById(R.id.storeName);
        more_details = (TextView) v.findViewById(R.id.moreDetails);
        spinnerColor = (Spinner) v.findViewById(R.id.spinner_color);
        icImage = (ImageView) v.findViewById(R.id.ic_image);
        ivFav = (ImageView) v.findViewById(R.id.iv_fav);
        detailStore = (Button) v.findViewById(R.id.details_stor);
        sellerName = (TextView) v.findViewById(R.id.seller_name);
        listRelatedItems = (LinearListView) v.findViewById(R.id.list_related_items);
        listSpecialRelatedItems = (LinearListView) v.findViewById(R.id.list_special_related_items);
        listCategoryItems = (LinearListView) v.findViewById(R.id.list_category_items);
        txtRelatedItems = (TextView) v.findViewById(R.id.txt_related);
        txtCategoryItems = (TextView) v.findViewById(R.id.txt_category);
        txtCategorySpecialItems = (TextView) v.findViewById(R.id.txt_category_Special);
        productDesc = (TextView) v.findViewById(R.id.product_desc);
        back_tv = (ImageView) v.findViewById(R.id.ic_back);
        desc = (TextView) v.findViewById(R.id.desc);
        inbox = (ImageView) v.findViewById(R.id.iv_inbox);
        txtTitle = (TextView) v.findViewById(R.id.title);
        layAttributes = (CardView) v.findViewById(R.id.lay_attributes);
        layRelatedItems = (CardView) v.findViewById(R.id.lay_related_items);
        layCategoryItems = (CardView) v.findViewById(R.id.lay_cat_items);
        layCategoryspecialItems = (CardView) v.findViewById(R.id.lay_specialitems);
        llAddToCart = (LinearLayout) v.findViewById(R.id.ll_add_to_cart);
        //  icProductImage = (ImageView) v.findViewById(R.id.product_image_first);
        list = (GridView) v.findViewById(R.id.list);
        listSubInages = (NestedListView) v.findViewById(R.id.list_subimages);
        mPocketBar = (CircularProgressView) v.findViewById(R.id.pocket);
        layoutFavorite = (LinearLayout) v.findViewById(R.id.layout_favorite);
        scrollView = (ScrollView) v.findViewById(R.id.scrollView);
        llQuantity = (LinearLayout) v.findViewById(R.id.ll_quantity);
        minus = (TextView) v.findViewById(R.id.tv_minus);
        plus = (TextView) v.findViewById(R.id.tv_plus);
        tv_quantity = (TextView) v.findViewById(R.id.tv_quantity);
        v.findViewById(R.id.iv_share).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shareImage();
            }
        });
        setListViewScrollable(listSubInages);

    }

    private void shareImage() {
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_TEXT, "Nice item on ApiTap\n" + sellerName.getText().toString() + "\n" + productDesc.getText().toString());
        shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
        shareIntent.setType("image/*");
        shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        startActivity(Intent.createChooser(shareIntent, "send"));
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

    // Returns the URI path to the Bitmap displayed in specified ImageView
    public Uri getLocalBitmapUri(ImageView imageView) {
        // Extract Bitmap from ImageView drawable
        Drawable drawable = imageView.getDrawable();
        Bitmap bmp = null;
        if (drawable instanceof BitmapDrawable) {
            bmp = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
        } else {
            return null;
        }
        // Store image to default external storage directory
        Uri bmpUri = null;
        try {
            File file = new File(Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_DOWNLOADS), "share_image_" + System.currentTimeMillis() + ".png");
            file.getParentFile().mkdirs();
            FileOutputStream out = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.PNG, 90, out);
            out.close();
            bmpUri = Uri.fromFile(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bmpUri;
    }

    @Subscribe
    public void onEvent(Event event) {
        switch (event.getKey()) {
            case Constants.DETAILS_SUCCESS:
                ArrayList<DetailsBean> arrayDetails = ModelManager.getInstance().getDetailsManager().arrayDetails;
                Picasso.with(getActivity()).load(ATPreferences.readString(getActivity(), Constants.KEY_IMAGE_URL) + arrayDetails.get(0).getImage()).fit().centerInside().into(icImage);

                barcode = arrayDetails.get(0).getBarcode();
                specs = arrayDetails.get(0).getSpecs();
                sku = arrayDetails.get(0).getSku();
                model = arrayDetails.get(0).getModel();
                age21 = arrayDetails.get(0).getAge21();
                age18 = arrayDetails.get(0).getAge18();
                limit_quantity = arrayDetails.get(0).getQuantity();
                uri = getLocalBitmapUri(icImage);
                productImage = ATPreferences.readString(getActivity(), Constants.KEY_IMAGE_URL) + arrayDetails.get(0).getArrayProductDetails().get(0).getProductImage();
                productName = arrayDetails.get(0).getName();
                storeName.setText(arrayDetails.get(0).getSellerName());
                merchantID = arrayDetails.get(0).getMerchantID();
                txtTitle.setText(/*"By " +*/ arrayDetails.get(0).getName());

                priceAfterDiscount = arrayDetails.get(0).getPrice_AfterDiscount();
                actualPrice = arrayDetails.get(0).getPrice();
                String special_price = arrayDetails.get(0).getSpecial_price();

                Log.d("PriceDetail",priceAfterDiscount+"  "+actualPrice);

                if (!special_price.isEmpty()) {
                    actual_price.setText(special_price);
                    actual_price.setTextColor(Color.RED);
                    price_afterdiscount.setVisibility(View.GONE);
                } /*else if (priceAfterDiscount.equals("") || priceAfterDiscount.equals("0.00")) {
                    price_afterdiscount.setVisibility(View.GONE);
                    actual_price.setText("$" + actualPrice);
                } else if (priceAfterDiscount.equals(actualPrice)) {
                    price_afterdiscount.setVisibility(View.GONE);
                    actual_price.setText("$" + actualPrice);
                    actual_price.setTextColor(Color.RED);
                } else if (actualPrice.equals("0.00") && Double.parseDouble(actualPrice) < Double.parseDouble(priceAfterDiscount)) {
                    price_afterdiscount.setTextColor(Color.RED);
                    price_afterdiscount.setText("$" + priceAfterDiscount);
                    actual_price.setVisibility(View.GONE);
                } else {
                    actual_price.setText("$" + actualPrice);
                    actual_price.setTextColor(Color.RED);
                    price_afterdiscount.setText("$" + priceAfterDiscount);
                }*/

              else if (Double.parseDouble(actualPrice)==Double.parseDouble(priceAfterDiscount) || priceAfterDiscount.equals("0") || priceAfterDiscount.equals("0.00")||Double.parseDouble(priceAfterDiscount)>Double.parseDouble(actualPrice)) {
                    actual_price.setText("$" + actualPrice);
                }else if (actualPrice.equals("0") || actualPrice.equals("0.00")){
                    price_afterdiscount.setText("$" + priceAfterDiscount);
                    actual_price.setVisibility(View.GONE);
                }
                else if (Double.parseDouble(actualPrice) > Double.parseDouble(priceAfterDiscount)) {
                    price_afterdiscount.setText("$" + (String.format("%.2f", Double.parseDouble(priceAfterDiscount))));
//                    priceAfterDiscount.setGravity(Gravity.END);
//                    actualPrice.setGravity(Gravity.START);
                    actual_price.setText("$" + actualPrice);
                    actual_price.setTextColor(getResources().getColor(R.color.colorRed));
                    actual_price.setPaintFlags(actual_price.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                }

/*
              else  if (Double.parseDouble(actualPrice)==Double.parseDouble(priceAfterDiscount) || priceAfterDiscount.equals("0") || priceAfterDiscount.equals("0.00")) {
                    price_afterdiscount.setVisibility(View.GONE);
                    actual_price.setText("$" + actualPrice);
                }else if (actualPrice.equals("0") || actualPrice.equals("0.00")){
                    price_afterdiscount.setText("$" + priceAfterDiscount);
                    actual_price.setVisibility(View.GONE);
                }
                else if (Double.parseDouble(actualPrice) > Double.parseDouble(priceAfterDiscount)) {
                    price_afterdiscount.setText("$" + priceAfterDiscount);
                    price_afterdiscount.setTextColor(Color.RED);
                    actual_price.setText("$" + actualPrice);
                    actual_price.setTextColor(getResources().getColor(R.color.colorGrey1));
                    actual_price.setPaintFlags(actual_price.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                }
*/

                productDesc.setText(Utils.hexToASCII(arrayDetails.get(0).getProductDesc()));
                desc.setText(Utils.hexToASCII(arrayDetails.get(0).getProductDesc()));
                sellerName.setText(arrayDetails.get(0).getTitle());

                //Picasso.with(getActivity()).load(productImage).into(icProductImage);
                ArrayList<Sizedata> arraySize = arrayDetails.get(0).getSizedata();
                if (arraySize != null && arraySize.size() > 0) {
                    llQuantity.setVisibility(View.VISIBLE);
//                    DetailsAdapter adapter = new DetailsAdapter(getActivity(), 0, arraySize);
//                    list.setAdapter(adapter);
//                    adapter.setOnTextClickListener(new DetailsAdapter.AdapterClick() {
//                        @Override
//                        public void onTextClick(View v, String quantity) {
//                            productQuantity = quantity;
//                        }
//                    });

                    for (int i = 0; i < arraySize.size(); i++) {
                        ArrayList<SizeBean> arrData = arraySize.get(i).getSizeArray();
                        ArrayList<String> nameArray = new ArrayList<>();
                        int size = arrData.size();
                        for (int j = 0; j < size; j++) {
                            nameArray.add(arrData.get(j).getSize());
                        }

                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, nameArray);
                        if (i == 0) {
                            spinnerColor.setAdapter(adapter); // this will set list of values to spinner
                        } else if (i == 1) {
                            spinnerSize.setAdapter(adapter); // this will set list of values to spinner
                        }
                    }

                } else {
                    spinnerSize.setVisibility(View.GONE);
                    spinnerColor.setVisibility(View.GONE);
                    llQuantity.setVisibility(View.GONE);
                    layAttributes.setVisibility(View.GONE);
                }
                ArrayList<ProductDetailsBean> arrayProducts = arrayDetails.get(0).getArrayProductDetails();
                if (arrayProducts != null && arrayProducts.size() > 0) {
                    if (arrayProducts.size() == 1) {
                        thumbNa.setVisibility(View.VISIBLE);
                        listSubInages.setVisibility(View.GONE);
                    } else {
                        SubImagesAdapter adapterSubImages = new SubImagesAdapter(getActivity(), 0, arrayDetails.get(0).getArrayProductDetails());
                        listSubInages.setAdapter(adapterSubImages);
                        listSubInages.setVisibility(View.VISIBLE);
                        thumbNa.setVisibility(View.GONE);
                    }
                }

                break;

            case Constants.RELATED_DETAILS:

                relatedArray = ModelManager.getInstance().getDetailsManager().relatedDetailsBean.getRESULT().get(0).getRESULT();
                Log.d("relatedArraySize",relatedArray.size()+"");

                if (relatedArray != null && relatedArray.size() > 0) {
                    listRelatedItems.setAdapter(mAdapter);
                      txtRelatedItems.setVisibility(View.VISIBLE);
                    layRelatedItems.setVisibility(View.VISIBLE);
                } else {
                    // layRelatedItems.setVisibility(View.GONE);
                  //  txtRelatedItems.setText("People Also View");
                    //txtCategorySpecialItems.setText("People Also View");
                  //  txtCategoryItems.setText("People Also View");

                    layRelatedItems.setVisibility(View.GONE);
                }

                break;

            case Constants.RELATED_SPECIAL_DETAILS:

                specialArrayRelated = ModelManager.getInstance().getDetailsManager().specialItemBean.getRESULT();
                if (specialArrayRelated != null && specialArrayRelated.size() > 0) {
                    listSpecialRelatedItems.setAdapter(mAdapterSpecialRealted);
                    //  txtRelatedItems.setVisibility(View.VISIBLE);
                    txtCategorySpecialItems.setVisibility(View.VISIBLE);
                    layCategoryspecialItems.setVisibility(View.VISIBLE);

                    layRelatedItems.setVisibility(View.GONE);
                } else {
                    // layRelatedItems.setVisibility(View.GONE);
                    // layRelatedItems.setVisibility(View.GONE);
                    // layCategoryspecialItems.setVisibility(View.GONE);
                }

                break;

            case Constants.CATEGORY_DETAILS:
                clearfocus();
                mPocketBar.setVisibility(View.GONE);
                catArray = ModelManager.getInstance().getDetailsManager().categoryDetailsBean.getRESULT();
                if (catArray != null && catArray.size() > 0) {
                    listCategoryItems.setAdapter(mCatAdapter);
                    //txtCategoryItems.setVisibility(View.VISIBLE);
                    txtCategoryItems.setVisibility(View.VISIBLE);

                } else
                    //  layCategoryItems.setVisibility(View.GONE);
                    layCategoryItems.setVisibility(View.GONE);
                break;

            case Constants.ADD_TO_FAVORITE_SUCCESS:
                //    if (isFavorite.equals("1"))
                clearfocus();
                mPocketBar.setVisibility(View.GONE);
                isFavouriteboolean = true;
                ivFav.setBackgroundResource(R.drawable.ic_icon_fav);
//                else
//                    ivFav.setBackgroundResource(R.drawable.ic_icon_fav_gray);
                break;


            case Constants.SHOPPING_SUCCESS:
                clearfocus();
                mPocketBar.setVisibility(View.GONE);
                // Toast.makeText(getActivity(), "Item added to cart", Toast.LENGTH_SHORT).show();
                showSuccessdialog();
                break;
            case -1:
                clearfocus();
                mPocketBar.setVisibility(View.GONE);
                Toast.makeText(getActivity(), "Something went wrong, Please try again.", Toast.LENGTH_SHORT).show();
                break;

            case Constants.GET_FAVOURITE_SUCCESS:

                final ArrayList<String> favdetailsbeanArrayList = ModelManager.getInstance().getDetailsManager().hexIds;

                if (favdetailsbeanArrayList.contains(productId)) {
                    ivFav.setBackgroundResource(R.drawable.ic_icon_fav);
                    isFavouriteboolean = true;
                } else {
                    isFavouriteboolean = false;
                    ivFav.setBackgroundResource(R.drawable.ic_icon_fav_gray);
                }
                break;
            case Constants.GET_OPTIONS1_SUCCESS:
                productOptionsArrayList = ModelManager.getInstance().getDetailsManager().arrayOptions1;
                adp1 = new GetOption1(mActivity, productOptionsArrayList);
                break;

            case Constants.GET_OPTIONS2_SUCCESS:
                state++;
                productOptionsArrayList2 = ModelManager.getInstance().getProductOptions().arrayOptions2;
                productOptionsArrayList2Str = ModelManager.getInstance().getProductOptions().arrayOptionsStr;
                adp2 = new GetOption2(mActivity, productOptionsArrayList2);
                if (productOptionsArrayList.size() == 1) {
                    option1.setAdapter(adp2);
                }
                if (layout.getVisibility() == View.VISIBLE && state == 1) {
                    ModelManager.getInstance().getProductOptions().getOption2(getActivity(), Operations.makeJsonGetOptions2(getActivity(), option_Id2), 1);
                }
                if (state > 1) {
                    option2.setAdapter(adp2);
                    productOptionsArrayList3 = ModelManager.getInstance().getProductOptions().arrayOptionsStr2;
                } else
                    option1.setAdapter(adp2);

                break;
            case Constants.REMOVE_FAVOURITE_SUCCESS:
                ivFav.setBackgroundResource(R.drawable.ic_icon_fav_gray);
                isFavouriteboolean = false;
              //  ModelManager.getInstance().getDetailsManager().getDetails(getActivity(), Operations.makeJsonGetItems(getActivity(), productId, ATPreferences.readString(mActivity, Constants.KEY_USERID)), false);

                break;

        }
    }

    private void setListViewScrollable(final ListView list) {
        list.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                v.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.login:
                String email = editEmail.getText().toString();
                String password = editPassword.getText().toString();
                if (Utils.isEmpty(email) || Utils.isEmpty(password)) {
                    Toast.makeText(mActivity, "Please fill all details", Toast.LENGTH_SHORT).show();
                } else {
                    ModelManager.getInstance().getLoginManager().getLogin(mActivity, Operations.makeJsonUserLogin(mActivity, email, password));
                }
                break;
            case R.id.iv_inbox:
                if (isGuest) {
                    showGuestDialog();
                } else {
//                    displayView(new FragmentMessages(), Constants.TAG_MESSAGEPAGE, new Bundle());
                    Intent obji = new Intent(getActivity(), MessageDetailActivity.class);
                    obji.putExtra("productId", productId);
                    startActivity(obji);
                }
                break;

            case R.id.moreDetails:
             /*   ATPreferences.putBoolean(mActivity, "header_store", true);
                ATPreferences.putString(mActivity, "merchant_store", merchantID);
                Bundle bundle = new Bundle();
                bundle.putString("merchantId", merchantID);
                startActivity(new Intent(mActivity, MerchantStoreDetails.class).putExtras(bundle));*/
                //storeName.performClick();
                moreDetailsdialog();
                break;
            case R.id.txtForgotPassword:
                Intent i = new Intent(getActivity(), ForgotPasswordActivity.class);
                startActivity(i);
                break;

            case R.id.layout_favorite:
                if (isGuest) {
                    showGuestDialog();
                } else if (!isFavouriteboolean) {
                    mPocketBar.setVisibility(View.VISIBLE);
                    getfocus();
                    ModelManager.getInstance().getAddToFavoriteManager().addToFavorite(mActivity, Operations.makeJsonAddToFavorite(mActivity, productId));
                } else {
                    ModelManager.getInstance().getFavouriteManager().removeFavourites(mActivity, Operations.makeJsonRemoveFavourite(mActivity, productId), Constants.REMOVE_FAVOURITE_SUCCESS);
                }
                break;

            case R.id.ic_image:
                if (productImage != null) {
                    Intent intent = new Intent(getActivity(), ZoomImage.class);
                    intent.putExtra("image", productImage);
                    intent.putExtra("position", related_pos);
                    intent.putExtra("name", txtTitle.getText().toString());
                    intent.putExtra("detailsArray", ModelManager.getInstance().getDetailsManager().arrayDetails.get(0).getArrayProductDetails());
                    startActivity(intent);
                }
                break;

            case R.id.storeName:
                ATPreferences.putBoolean(mActivity, "header_store", true);
                ATPreferences.putString(mActivity, "merchant_store", merchantID);
                startActivity(new Intent(mActivity, HomeActivity.class));
//                Bundle bundle = new Bundle();
//                bundle.putBoolean("header", true);
//                displayView(new FragmentHome(), "Home", bundle);

                break;
            case R.id.details_stor:
                ATPreferences.putBoolean(mActivity, "header_store", true);
                ATPreferences.putString(mActivity, "merchant_store", merchantID);
                startActivity(new Intent(mActivity, HomeActivity.class));
//                Bundle bundle = new Bundle();
//                bundle.putBoolean("header", true);
//                displayView(new FragmentHome(), "Home", bundle);

                break;


            case R.id.store_name:
                Intent intents = new Intent(getActivity(), MerchantStoreDetails.class);
                intents.putExtra("merchantId", merchantID);
                startActivity(intents);
                break;


//            case R.id.ic_back:
//                Log.d("fragmentcount", getFragmentManager().getBackStackEntryCount() + "");
//                if (getFragmentManager().getBackStackEntryCount() > 0) {
//                    getFragmentManager().popBackStack();
//                } else {
//                    getActivity().onBackPressed();
//                }
//
//                break;

            case R.id.ic_back:
                //((HomeActivity) getActivity()).onBackPressed();
                Log.d("fragmentcount", getFragmentManager().getBackStackEntryCount() + "");
                if (getFragmentManager().getBackStackEntryCount() > 0) {
                    getFragmentManager().popBackStack();
                } else {
                    getActivity().onBackPressed();
                }
                if (getFragmentManager().getBackStackEntryCount() == 1) {
                    if (flag.equals("adDetail"))
                        AdDetailActivity.viewFrame();
                    else if (flag.equals("search"))
                        SearchItemActivity.viewFrame();

                }
                break;

            case R.id.tv_minus:
                int quantity = Integer.parseInt(tv_quantity.getText().toString());
                if (quantity > 1)
                    quantity--;

                tv_quantity.setText(String.valueOf(quantity));
                break;

            case R.id.tv_plus:
                int quan = Integer.parseInt(tv_quantity.getText().toString());
                quan++;
                tv_quantity.setText(String.valueOf(quan));
                break;

            case R.id.ll_add_to_cart:
//                if (isGuest) {
//                    showGuestDialog();
//                } else {
                    showdialog();
                //}
                // mPocketBar.setVisibility(View.VISIBLE);
                //ModelManager.getInstance().getShoppingCartManager().addItemTOCart(mActivity, Operations.makeJsonAddToCartItems(mActivity, productQuantity, productId));

                break;
        }
    }

    public void showdialog() {
        final Dialog dialog = new Dialog(mActivity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        dialog.setContentView(R.layout.customdialog_checkout);
        TextView minusd = (TextView) dialog.findViewById(R.id.tv_minus);
        TextView plusd = (TextView) dialog.findViewById(R.id.tv_plus);
        final TextView tv_quantityd = (TextView) dialog.findViewById(R.id.tv_quantity);
        final TextView tv_option = (TextView) dialog.findViewById(R.id.option);
        final TextView tv_option1 = (TextView) dialog.findViewById(R.id.option1);
        final Button submit = (Button) dialog.findViewById(R.id.submitdailog);
        final Button cancel = (Button) dialog.findViewById(R.id.cancel);
        layout = (RelativeLayout) dialog.findViewById(R.id.realativesecond);
        option1 = (Spinner) dialog.findViewById(R.id.editoption);
        option2 = (Spinner) dialog.findViewById(R.id.editoption2);


        option1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String optionId = productOptionsArrayList2Str.get(i);
                String options_Id = Utils.getElevenDigitId(optionId);
                str_option_Id = options_Id;

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        option2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (layout.getVisibility() == View.VISIBLE) {
                    String optionId = productOptionsArrayList3.get(i);
                    String options_Id = Utils.getElevenDigitId(optionId);
                    str_option_Id2 = options_Id;
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        //option1.setAdapter(adp1);
        Log.e("LIst", productOptionsArrayList.size() + "");
        if (productOptionsArrayList.size() == 1) {
            tv_option.setText(productOptionsArrayList.get(0).getName_option().toString() + "");

            String optionId = productOptionsArrayList.get(0).getOption_id();
            String options_Id = Utils.getElevenDigitId(optionId);
            option_Id = options_Id;
            ModelManager.getInstance().getProductOptions().getOption2(getActivity(), Operations.makeJsonGetOptions2(getActivity(), options_Id), 0);


        } else if (productOptionsArrayList.size() == 2) {
            layout.setVisibility(View.VISIBLE);
            tv_option.setText(productOptionsArrayList.get(0).getName_option().toString() + "");
            String optionId = productOptionsArrayList.get(0).getOption_id();
            String options_Id = Utils.getElevenDigitId(optionId);
            option_Id = options_Id;
            ModelManager.getInstance().getProductOptions().getOption2(getActivity(), Operations.makeJsonGetOptions2(getActivity(), options_Id), 0);


            tv_option1.setText(productOptionsArrayList.get(1).getName_option().toString() + "");
            String optionId2 = productOptionsArrayList.get(1).getOption_id();
            String options_Id2 = Utils.getElevenDigitId(optionId2);
            option_Id2 = options_Id2;
        }

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                state = 0;
                dialog.dismiss();
            }
        });

        minusd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int quan = Integer.parseInt(tv_quantityd.getText().toString());
                if (quan > 1)
                    quan--;
                tv_quantityd.setText(String.valueOf(quan));
            }
        });
        plusd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int quan = Integer.parseInt(tv_quantityd.getText().toString());
                quan++;
                tv_quantityd.setText(String.valueOf(quan));
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPocketBar.setVisibility(View.VISIBLE);
                getfocus();
                DetailsBean detailsBean = new DetailsBean();
                Log.d("UserDrais", detailsBean.getMerchantID() + "");
                ModelManager.getInstance().getShoppingCartManager().addItemTOCart(mActivity, Operations.makeJsonAddToCartItems(mActivity, tv_quantityd.getText().toString(), productId, merchantID, str_option_Id, str_option_Id2));
                dialog.dismiss();
            }
        });

        dialog.show();
        dialog.getWindow().setAttributes(lp);
    }

    public void showSuccessdialog() {
        final Dialog dialog = new Dialog(mActivity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        dialog.setContentView(R.layout.customdialogcart);
        Button btncontinue = (Button) dialog.findViewById(R.id.continueshoping);
        Button btncheckout = (Button) dialog.findViewById(R.id.checkout);

        btncontinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        btncheckout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                displayView(new ShoppingCartFragment(), Constants.TAG_SHOPPING, new Bundle());
                dialog.dismiss();

            }
        });

        dialog.show();
        dialog.getWindow().setAttributes(lp);
    }

    public void moreDetailsdialog() {
        final Dialog dialog = new Dialog(mActivity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.verticalMargin = 10;
        dialog.setContentView(R.layout.more_details_dialog);
        Button done = (Button) dialog.findViewById(R.id.close);
        TextView modelNumber = (TextView) dialog.findViewById(R.id.model_number);
        TextView skuId = (TextView) dialog.findViewById(R.id.pos_sku);
        TextView barCode = (TextView) dialog.findViewById(R.id.bar_code_number);
        TextView quantity = (TextView) dialog.findViewById(R.id.quantity);
        TextView specification = (TextView) dialog.findViewById(R.id.specs);
        CheckBox checkBox18 = (CheckBox) dialog.findViewById(R.id.eightyears);
        CheckBox checkBox21 = (CheckBox) dialog.findViewById(R.id.twoyears);



        checkBox21.setClickable(false);
        checkBox18.setClickable(false);

        if (limit_quantity.equals("0"))
            quantity.setText("No limit");
        else
            quantity.setText(limit_quantity);

        if (sku.isEmpty())
            skuId.setText("N/A");
        else
            skuId.setText(sku);

        if (barcode.isEmpty())
            barCode.setText("N/A");
        else
            barCode.setText(barcode);

        if (specs.isEmpty())
            specification.setText("N/A");
        else
            specification.setText(specs);

        if (model.isEmpty())
            modelNumber.setText("N/A");
        else
            modelNumber.setText(model);

        if (age18.equals("true"))
            checkBox18.setChecked(true);
        else
            checkBox18.setVisibility(View.GONE);
        if (age21.equals("true"))
            checkBox21.setChecked(true);
        else
            checkBox21.setVisibility(View.GONE);
        //  checkBox21.setText("The product is not available to people over 21 years of age");

        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        dialog.show();
        dialog.getWindow().setAttributes(lp);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        ArrayList<ProductDetailsBean> arrayDetails = ModelManager.getInstance().getDetailsManager().arrayDetails.get(0).getArrayProductDetails();
        if (arrayDetails.size() > 0) {
            thumbNa.setVisibility(View.GONE);
        }
        String image = ATPreferences.readString(getActivity(), Constants.KEY_IMAGE_URL) + arrayDetails.get(i).getProductImage();
        String id = arrayDetails.get(i).getProductId();
        related_pos = i;
        // ModelManager.getInstance().getProductOptions().getOption1(getActivity(), Operations.makeJsonGetOptions(getActivity(), id));

        Picasso.with(getActivity()).load(image).into(icImage);

    }

    public void displayView(Fragment fragment, String tag, Bundle bundle) {
        //  if (fragment != null) {
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        Fragment fragB = fragmentManager.findFragmentByTag(tag);
        if (bundle != null)
            fragment.setArguments(bundle);
        //  if (fragB == null) {
        fragmentTransaction.replace(R.id.container_body, fragment);
        if (fragment instanceof Fragment_Ads || fragment instanceof FragmentSpecial || fragment instanceof FragmentItems) {

        } else
            fragmentTransaction.addToBackStack(tag);
        fragmentTransaction.commit();
//            } else
//                fragmentTransaction.show(fragB);
        //  getSupportActionBar().setTitle(tag);
        // }
    }

    public void clearfocus() {
        getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }

    public void getfocus() {
        getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }

    public void showGuestDialog() {

        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.CENTER;
        dialog.setContentView(R.layout.guest_login);

        Button login = (Button) dialog.findViewById(R.id.continueshoping);
        Button cancel = (Button) dialog.findViewById(R.id.cancel);


        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), LoginActivity.class));
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });


        dialog.show();
        dialog.getWindow().setAttributes(lp);
        //getDialogView(dialog);
        //viewsVisibility(dialog);

    }

    public class GetOption1 extends BaseAdapter {
        private List<ProductOptionsBean> response;
        private LayoutInflater inflater = null;
        private Activity activity;

        public GetOption1(Activity a, List<ProductOptionsBean> response) {
            this.response = response;
            activity = a;
            inflater = (LayoutInflater) activity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            return response.size();
        }

        @Override
        public Object getItem(int i) {
            return i;
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int position, View view, ViewGroup viewGroup) {
            View vi = view;
            vi = inflater.inflate(R.layout.spinner_row, null);
            TextView txt_type = (TextView) vi.findViewById(R.id.txt_type);
            option_Ids.add(response.get(position).getOption_id());
            Log.d("options_Ids", response.get(position).getOption_id());
            txt_type.setText(response.get(position).getName_option());
            return vi;
        }
    }

    public class GetOption2 extends BaseAdapter {
        private List<ProductOptions2Bean> response;
        private LayoutInflater inflater = null;
        private Activity activity;

        public GetOption2(Activity a, List<ProductOptions2Bean> response) {
            this.response = response;
            activity = a;
            inflater = (LayoutInflater) activity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            return response.size();
        }

        @Override
        public Object getItem(int i) {
            return i;
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int position, View view, ViewGroup viewGroup) {
            View vi = view;
            vi = inflater.inflate(R.layout.spinner_row, null);
            TextView txt_type = (TextView) vi.findViewById(R.id.txt_type);
            TextView txt_price = (TextView) vi.findViewById(R.id.txt_price);
            txt_price.setVisibility(View.VISIBLE);
            Log.d("responseas", response.get(position).getChoice_price() + "wdk");
            double price=Double.parseDouble(response.get(position).getChoice_price());
            txt_price.setText("$" + (String.format("%.2f", price)));


            txt_type.setText(response.get(position).getChoice_name());
            return vi;
        }

    }

    public void DisplayFragment(String flag, Bundle bundle) {
        if (flag == null) {
            flag = "home";
        }
        switch (flag) {
            case "search":
                ((SearchItemActivity) getActivity()).displayView(new FragmentDetails(), Constants.TAG_DETAILSPAGE, bundle);
                break;
            case "adDetail":
                ((AdDetailActivity) getActivity()).displayView(new FragmentDetails(), Constants.TAG_DETAILSPAGE, bundle);
                break;
            default:
                ((HomeActivity) getActivity()).displayView(new FragmentDetails(), Constants.TAG_DETAILSPAGE, bundle);
                break;
        }
    }

    private BaseAdapter mCatAdapter = new BaseAdapter() {

        @Override
        public View getView(final int position, View convertView, final ViewGroup parent) {
            if (convertView == null) {
                convertView = getActivity().getLayoutInflater().inflate(R.layout.row_horizontal_test, parent, false);
            }

            ImageView imageView = (ImageView) convertView.findViewById(R.id.image);
            TextView description = (TextView) convertView.findViewById(R.id.description);
            ImageView eye = (ImageView) convertView.findViewById(R.id.eye);
            String isSeen = catArray.get(position).get1149();
            LinearLayout rlSinglePrice = (LinearLayout) convertView.findViewById(R.id.rel_single_price);
            LinearLayout rlTwoPrice = (LinearLayout) convertView.findViewById(R.id.rl_two_price);
            TextView price = (TextView) convertView.findViewById(R.id.price);
            TextView actualPrice = (TextView) convertView.findViewById(R.id.actual_price);
            TextView priceAfterDiscount = (TextView) convertView.findViewById(R.id.price_after_discount);

            if (isSeen!=null&&isSeen.equalsIgnoreCase("false")) {
                eye.setBackgroundResource(R.drawable.green_seen);
            } else {
                eye.setVisibility(View.GONE);
            }
            try {
                description.setText(Utils.hexToASCII(catArray.get(position).get12083()));
            } catch (Exception e) {
                e.printStackTrace();
            }
            String ActualPrice=catArray.get(position).get11498();
            String DiscountPrice=catArray.get(position).get122158();

            if (Double.parseDouble(ActualPrice)==Double.parseDouble(DiscountPrice) || DiscountPrice.equals("0") || DiscountPrice.equals("0.00")) {
                rlSinglePrice.setVisibility(View.VISIBLE);
                rlTwoPrice.setVisibility(View.GONE);
                actualPrice.setText("$" + ActualPrice);
            }else if (ActualPrice.equals("0") || ActualPrice.equals("0.00")){
                rlTwoPrice.setVisibility(View.VISIBLE);
                rlSinglePrice.setVisibility(View.GONE);
                priceAfterDiscount.setText("$" + DiscountPrice);
                actualPrice.setVisibility(View.GONE);
            }
            else if (Double.parseDouble(ActualPrice) > Double.parseDouble(DiscountPrice)) {
                rlTwoPrice.setVisibility(View.VISIBLE);
                rlSinglePrice.setVisibility(View.VISIBLE);
                priceAfterDiscount.setText("$" + (String.format("%.2f", Double.parseDouble(DiscountPrice))));
                priceAfterDiscount.setGravity(Gravity.END);
                actualPrice.setGravity(Gravity.START);
                actualPrice.setText("$" + ActualPrice);
                actualPrice.setTextColor(getResources().getColor(R.color.colorRed));
                actualPrice.setPaintFlags(actualPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            }
            Log.d("acyial", catArray.get(position).get11498() + "Dsax" + catArray.get(position).get122158());
//            if (catArray.get(position).get11498() != null && catArray.get(position).get122158().equals(catArray.get(position).get122158()) || catArray.get(position).get122158().equals("") || catArray.get(position).get122158().equals("0") || catArray.get(position).get122158().equals("0.00")) {
//                rlSinglePrice.setVisibility(View.VISIBLE);
//                rlTwoPrice.setVisibility(View.GONE);
//                actualPrice.setText("$" + catArray.get(position).get11498());
//            } else if (catArray.get(position).get11498() != null && catArray.get(position).get11498().equals("0.00") && Double.parseDouble(catArray.get(position).get11498()) < Double.parseDouble(catArray.get(position).get122158())) {
//                rlTwoPrice.setVisibility(View.VISIBLE);
//                rlSinglePrice.setVisibility(View.GONE);
//                priceAfterDiscount.setText("$ " + catArray.get(position).get122158());
//                actual_price.setVisibility(View.GONE);
//            } else {
//                rlSinglePrice.setVisibility(View.GONE);
//                rlTwoPrice.setVisibility(View.VISIBLE);
//                if (catArray.get(position).get11498() != null)
//                    actualPrice.setText(catArray.get(position).get11498());
//                priceAfterDiscount.setText("$" + catArray.get(position).get122158());
//            }

            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    getfocus();
                    productId = Utils.lengtT(11, catArray.get(position).get114144());
                    ModelManager.getInstance().setProductSeen().setProductSeen(getActivity(), Operations.makeProductSeen(getActivity(), productId));
                    mPocketBar.setVisibility(View.VISIBLE);

                    String productType = catArray.get(position).get114112();
                    // ModelManager.getInstance().getDetailsManager().getDetails(getActivity(), Operations.makeJsonGetItems(getActivity(), productId, ATPreferences.readString(mActivity, Constants.KEY_USERID)), false);
//                    ModelManager.getInstance().getProductOptions().getOption1(getActivity(), Operations.makeJsonGetOptions(getActivity(), productId));
//                    ModelManager.getInstance().setProductSeen().setProductSeen(getActivity(), Operations.makeProductSeen(getActivity(), productId));
//                    ModelManager.getInstance().setProductSeen().setCounter(getActivity(), Operations.makeJsonCounter(getActivity(), productId));
//                    ModelManager.getInstance().getDetailsManager().getItemsByCategory(getActivity(), Operations.makeJsonGetItemsByCategoryViews(getActivity(), productId));
//                    ModelManager.getInstance().getFavouriteManager().getFavourites(getActivity(),
//                            Operations.makeJsonGetFavourite(getActivity(), ""), Constants.GET_FAVOURITE_SUCCESS);
//                    if (productType.equals("21")) {
//                        ModelManager.getInstance().getDetailsManager().getDetails(getActivity(), Operations.makeJsonGetRelatedItems(getActivity(), productId), true);
//                        txtCategorySpecialItems.setVisibility(View.GONE);
//                        layCategoryspecialItems.setVisibility(View.GONE);
//                        more_details.setVisibility(View.VISIBLE);
//                        llAddToCart.setVisibility(View.VISIBLE);
//                    } else {
//                        ModelManager.getInstance().getDetailsManager().getSpecialDetails(getActivity(), Operations.makeJsonSpecialItemrequired(getActivity(), productId));
//                        txtRelatedItems.setVisibility(View.GONE);
//                        layCategoryItems.setVisibility(View.GONE);
//                        more_details.setVisibility(View.INVISIBLE);
//                        llAddToCart.setVisibility(View.INVISIBLE);
//                    }
                    Bundle bundle = new Bundle();
                    bundle.putString("productId", productId);
                    bundle.putString("productType", productType);
                    bundle.putString("flag", flag);
                    DisplayFragment(flag, bundle);
                }
            });

            Picasso.with(getActivity()).load(ATPreferences.readString(getActivity(), Constants.KEY_IMAGE_URL) + catArray.get(position).get121170()).into(imageView);
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
            return catArray.size();
        }
    };
    private BaseAdapter mAdapter = new BaseAdapter() {

        @Override
        public View getView(final int position, View convertView, final ViewGroup parent) {
            if (convertView == null) {
                convertView = getActivity().getLayoutInflater().inflate(R.layout.row_horizontal_test, parent, false);
            }

            ImageView imageView = (ImageView) convertView.findViewById(R.id.image);
            TextView description = (TextView) convertView.findViewById(R.id.description);
            ImageView eye = (ImageView) convertView.findViewById(R.id.eye);
            String isSeen = relatedArray.get(position).get_1149();
            LinearLayout rlSinglePrice = (LinearLayout) convertView.findViewById(R.id.rel_single_price);
            LinearLayout rlTwoPrice = (LinearLayout) convertView.findViewById(R.id.rl_two_price);
            TextView price = (TextView) convertView.findViewById(R.id.price);
            TextView actualPrice = (TextView) convertView.findViewById(R.id.actual_price);
            TextView priceAfterDiscount = (TextView) convertView.findViewById(R.id.price_after_discount);

            if (isSeen.equalsIgnoreCase("false")) {
                eye.setBackgroundResource(R.drawable.green_seen);
            } else {
                eye.setVisibility(View.GONE);
            }
            try {
                description.setText(Utils.hexToASCII(relatedArray.get(position).get12083()));
            } catch (Exception e) {
                e.printStackTrace();
            }
            String ActualPrice=relatedArray.get(position).get11498();
            String DiscountPrice=relatedArray.get(position).get122158();


            Log.d("acyial2", ActualPrice + "Dsax" + DiscountPrice);

            if (Double.parseDouble(ActualPrice)==Double.parseDouble(DiscountPrice) || DiscountPrice.equals("0") || DiscountPrice.equals("0.00")) {
                rlSinglePrice.setVisibility(View.VISIBLE);
                rlTwoPrice.setVisibility(View.GONE);
                actualPrice.setText("$" + ActualPrice);
            }else if (ActualPrice.equals("0") || ActualPrice.equals("0.00")){
                rlTwoPrice.setVisibility(View.VISIBLE);
                rlSinglePrice.setVisibility(View.GONE);
                priceAfterDiscount.setText("$" + DiscountPrice);
                actualPrice.setVisibility(View.GONE);
            }
            else if (Double.parseDouble(ActualPrice) > Double.parseDouble(DiscountPrice)) {
                rlTwoPrice.setVisibility(View.VISIBLE);
                rlSinglePrice.setVisibility(View.VISIBLE);
                priceAfterDiscount.setText("$" + (String.format("%.2f", Double.parseDouble(DiscountPrice))));
                priceAfterDiscount.setGravity(Gravity.END);
                actualPrice.setGravity(Gravity.START);
                actualPrice.setText("$" + ActualPrice);
                actualPrice.setTextColor(getResources().getColor(R.color.colorRed));
                actualPrice.setPaintFlags(actualPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            }/* else {
                rlSinglePrice.setVisibility(View.GONE);
                rlTwoPrice.setVisibility(View.VISIBLE);
                actualPrice.setText(ActualPrice);
                priceAfterDiscount.setText("$" + DiscountPrice);
            }*/

            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mPocketBar.setVisibility(View.VISIBLE);
                    productId = Utils.lengtT(11, relatedArray.get(position).get114144());
                    String productType =  relatedArray.get(position).get114112();
                    ModelManager.getInstance().setProductSeen().setProductSeen(getActivity(), Operations.makeProductSeen(getActivity(), productId));

                    // ModelManager.getInstance().getDetailsManager().getDetails(getActivity(), Operations.makeJsonGetItems(getActivity(), productId, ATPreferences.readString(mActivity, Constants.KEY_USERID)), false);
//                    if (productType.equals("21")) {
//                        ModelManager.getInstance().getDetailsManager().getDetails(getActivity(), Operations.makeJsonGetRelatedItems(getActivity(), productId), true);
//                        txtCategorySpecialItems.setVisibility(View.GONE);
//                        layCategoryspecialItems.setVisibility(View.GONE);
//                        more_details.setVisibility(View.VISIBLE);
//                        llAddToCart.setVisibility(View.VISIBLE);
//                    } else {
//                        ModelManager.getInstance().getDetailsManager().getSpecialDetails(getActivity(), Operations.makeJsonSpecialItemrequired(getActivity(), productId));
//                        txtRelatedItems.setVisibility(View.GONE);
//                        layCategoryItems.setVisibility(View.GONE);
//                        more_details.setVisibility(View.INVISIBLE);
//                        llAddToCart.setVisibility(View.INVISIBLE);
//                    }
                    Bundle bundle = new Bundle();
                    bundle.putString("productId", productId);
                    bundle.putString("productType", productType);
                    bundle.putString("flag", flag);

                    DisplayFragment(flag, bundle);
                }
            });

            Picasso.with(getActivity()).load(ATPreferences.readString(getActivity(), Constants.KEY_IMAGE_URL) + relatedArray.get(position).get121170()).into(imageView);
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
            return relatedArray.size();
        }
    };
    private BaseAdapter mAdapterSpecialRealted = new BaseAdapter() {

        @Override
        public View getView(final int position, View convertView, final ViewGroup parent) {
            if (convertView == null) {
                convertView = getActivity().getLayoutInflater().inflate(R.layout.row_horizontal_test, parent, false);
            }

            ImageView imageView = (ImageView) convertView.findViewById(R.id.image);
            TextView description = (TextView) convertView.findViewById(R.id.description);
            ImageView eye = (ImageView) convertView.findViewById(R.id.eye);
            String isSeen = specialArrayRelated.get(0).getRESULT().get(0).getIA().get(position).get_114_9();
            LinearLayout rlSinglePrice = (LinearLayout) convertView.findViewById(R.id.rel_single_price);
            LinearLayout rlTwoPrice = (LinearLayout) convertView.findViewById(R.id.rl_two_price);
            TextView price = (TextView) convertView.findViewById(R.id.price);
            TextView actualPrice = (TextView) convertView.findViewById(R.id.actual_price);
            TextView priceAfterDiscount = (TextView) convertView.findViewById(R.id.price_after_discount);

            if (isSeen.equalsIgnoreCase("false")) {
                eye.setBackgroundResource(R.drawable.green_seen);
            } else {
                eye.setVisibility(View.GONE);
            }
            try {
                description.setText(Utils.hexToASCII(specialArrayRelated.get(0).getRESULT().get(0).getIA().get(position).get12083()));
            } catch (Exception e) {
                e.printStackTrace();
            }

            String ActualPrice=specialArrayRelated.get(0).getRESULT().get(0).getIA().get(position).get11498();
            String DiscountPrice=specialArrayRelated.get(0).getRESULT().get(0).getIA().get(position).get122158();
            if (Double.parseDouble(ActualPrice)==Double.parseDouble(DiscountPrice) || DiscountPrice.equals("0") || DiscountPrice.equals("0.00")) {
                rlSinglePrice.setVisibility(View.VISIBLE);
                rlTwoPrice.setVisibility(View.GONE);
                actualPrice.setText("$" + ActualPrice);
            }else if (ActualPrice.equals("0") || ActualPrice.equals("0.00")){
                rlTwoPrice.setVisibility(View.VISIBLE);
                rlSinglePrice.setVisibility(View.GONE);
                priceAfterDiscount.setText("$" + DiscountPrice);
                actualPrice.setVisibility(View.GONE);
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


//            if (relatedArray.get(position).getActualPrice().equals(relatedArray.get(position).getPriceAfterDiscount()) || relatedArray.get(position).getPriceAfterDiscount().equals("") || relatedArray.get(position).getPriceAfterDiscount().equals("0") || relatedArray.get(position).getPriceAfterDiscount().equals("0.00")) {
//                rlSinglePrice.setVisibility(View.VISIBLE);
//                rlTwoPrice.setVisibility(View.GONE);
//                actualPrice.setText("$" + relatedArray.get(position).getActualPrice());
//            } else if (catArray.get(position).get122158().equals("0.00") && Double.parseDouble(catArray.get(position).get11498()) < Double.parseDouble(catArray.get(position).get122158())) {
//                rlTwoPrice.setVisibility(View.VISIBLE);
//                rlSinglePrice.setVisibility(View.GONE);
//                priceAfterDiscount.setText("$" + catArray.get(position).get122158());
//                actual_price.setVisibility(View.GONE);
//            } else {
//                rlSinglePrice.setVisibility(View.GONE);
//                rlTwoPrice.setVisibility(View.VISIBLE);
//                actualPrice.setText(relatedArray.get(position).getActualPrice());
//                priceAfterDiscount.setText("$" + relatedArray.get(position).getPriceAfterDiscount());
//            }
//            rlSinglePrice.setVisibility(View.VISIBLE);
//            rlTwoPrice.setVisibility(View.GONE);
//            actualPrice.setText("$" + specialArrayRelated.get(position).get114112());


            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mPocketBar.setVisibility(View.VISIBLE);
                    productId = Utils.lengtT(11, specialArrayRelated.get(0).getRESULT().get(0).getIA().get(position).get114144());
                    ModelManager.getInstance().setProductSeen().setProductSeen(getActivity(), Operations.makeProductSeen(getActivity(), productId));
                    String productType = specialArrayRelated.get(0).getRESULT().get(0).getIA().get(position).get114112();
                    //  ModelManager.getInstance().getDetailsManager().getDetails(getActivity(), Operations.makeJsonGetItems(getActivity(), productId, ATPreferences.readString(mActivity, Constants.KEY_USERID)), false);
//                    if (productType.equals("21")) {
//                        ModelManager.getInstance().getDetailsManager().getDetails(getActivity(), Operations.makeJsonGetRelatedItems(getActivity(), productId), true);
//                        txtCategorySpecialItems.setVisibility(View.GONE);
//                        layCategoryspecialItems.setVisibility(View.GONE);
//                        more_details.setVisibility(View.VISIBLE);
//                        llAddToCart.setVisibility(View.VISIBLE);
//                    } else {
//                        ModelManager.getInstance().getDetailsManager().getSpecialDetails(getActivity(), Operations.makeJsonSpecialItemrequired(getActivity(), productId));
//                        txtRelatedItems.setVisibility(View.GONE);
//                        layCategoryItems.setVisibility(View.GONE);
//                        more_details.setVisibility(View.INVISIBLE);
//                        llAddToCart.setVisibility(View.INVISIBLE);
//                    }

                    Bundle bundle = new Bundle();
                    bundle.putString("productId", productId);
                    bundle.putString("productType", productType);
                    bundle.putString("flag", flag);

                    DisplayFragment(flag, bundle);

                }
            });

            Picasso.with(getActivity()).load(ATPreferences.readString(getActivity(),
                    Constants.KEY_IMAGE_URL) + specialArrayRelated.get(0).getRESULT().get(0).getIA().get(position).getIM().get(0).get4742()).into(imageView);
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
            return specialArrayRelated.get(0).getRESULT().get(0).getIA().size();
        }
    };


}
