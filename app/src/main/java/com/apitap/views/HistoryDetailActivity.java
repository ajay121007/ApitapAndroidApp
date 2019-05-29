package com.apitap.views;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.apitap.R;
import com.apitap.controller.ModelManager;
import com.apitap.model.AddTabBar;
import com.apitap.model.Client;
import com.apitap.model.Constants;
import com.apitap.model.Operations;
import com.apitap.model.Utils;
import com.apitap.model.bean.HistoryInvoiceBean;
import com.apitap.model.bean.InvoiceDetailBean;
import com.apitap.model.bean.InvoiceItemsBean;
import com.apitap.model.bean.MerchantDetailBean;
import com.apitap.model.customclasses.Event;
import com.apitap.model.preferences.ATPreferences;
import com.apitap.views.NavigationMenu.FragmentDrawer;
import com.apitap.views.adapters.History2Adapter;
import com.apitap.views.adapters.InvoiceItemAdapter;
import com.apitap.views.customviews.DividerItemDecoration;
import com.apitap.views.fragments.FragmentItems;
import com.apitap.views.fragments.FragmentMessages;
import com.apitap.views.fragments.FragmentScanner;
import com.apitap.views.fragments.FragmentSpecial;
import com.apitap.views.fragments.Fragment_Ads;
import com.github.rahatarmanahmed.cpv.CircularProgressView;
import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

public class HistoryDetailActivity extends AppCompatActivity implements View.OnClickListener, FragmentDrawer.FragmentDrawerListener {

    public CircularProgressView mPocketBar;
    private RecyclerView recycler;
    private HistoryInvoiceBean.RESULT.Invoicedata data;
    private String status;
    private ImageView logo_merchant;
    private Button rate_merchant, reorder_btn;
    private List<InvoiceDetailBean.RESULT.DetailData> list;
    private RelativeLayout rlHedaerPrice;
    private ImageView iv_back;
    private TextView close;
    int selected_position = -1;
    ArrayList<CheckBox> mCheckBoxes = new ArrayList<CheckBox>();
    private List<InvoiceItemsBean.RESULT.Invoicedata> listInvoiceItems;
    private TextView txt_account, txt_time, txt_deleiveryMethod, txt_deleiveryAddress, txt_transNo, txt_tip2, txt_auth, txt_subTotal,
            txt_approval, txt_taxes, txt_total, txt_date, txt_no, tvStatus, tvTotal;
    private Activity mActivity;
    private TextView sendMessage;
    LinearLayout scan_tool;
    LinearLayout msg_tool;
    LinearLayout search_tool;
    RelativeLayout viewMain;
    FrameLayout frameLayout;
    private static int toolint = 0;
    String searchkey = "";
    ImageView storeImage;
    TextView storeName;
    private static final int MY_CAMERA_REQUEST_CODE = 100;
    private MerchantDetailBean.RESULT.DetailData merchant_data;
    private FragmentDrawer drawerFragment;
    private Toolbar mToolbar;
    public static TabLayout tabLayout;
    private TextView tabOne, tabTwo, tabThree, tabFour, tabFive, tabSix;
    LinearLayout tabConatiner;
    Button storeBrowse;
    public double sub_Total = 0.00;
    public double grand_Total = 0.00;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_detail2);
        status = getIntent().getExtras().getString("status");
        mActivity = this;
        data = (HistoryInvoiceBean.RESULT.Invoicedata) getIntent().getSerializableExtra("data");
        initViews();
        setListener();
    }

    @Override
    protected void onResume() {
        super.onResume();
        ModelManager.getInstance().getAddMerchantRating().getMerchantRating(HistoryDetailActivity.this,
                Operations.GetMerchantRating(mActivity, ATPreferences.readString(this, Constants.KEY_USERID), data.getMerchantId()));

    }

    private void initViews() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        drawerFragment = (FragmentDrawer)
                getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
        drawerFragment.setUp(R.id.fragment_navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout), mToolbar);
        drawerFragment.setDrawerListener(this);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        tabConatiner = (LinearLayout) findViewById(R.id.tab_container);
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        storeImage = (ImageView) findViewById(R.id.adstoreImg);
        storeName = (TextView) findViewById(R.id.storeName);
        tvTotal = (TextView) findViewById(R.id.tv_total);
        storeBrowse = (Button) findViewById(R.id.details_store);
        tvStatus = (TextView) findViewById(R.id.tv_status);
        txt_account = (TextView) findViewById(R.id.txt_account);
        sendMessage = (TextView) findViewById(R.id.send_message);
        iv_back = (ImageView) findViewById(R.id.tv_back);
        close = (TextView) findViewById(R.id.close);
        txt_deleiveryMethod = (TextView) findViewById(R.id.txt_deleiveryMethod);
        txt_time = (TextView) findViewById(R.id.time);
        txt_date = (TextView) findViewById(R.id.txt_date);

        txt_deleiveryAddress = (TextView) findViewById(R.id.txt_deleiveryAddress);
        //   txt_transNo = (TextView) findViewById(R.id.transaction);
        txt_tip2 = (TextView) findViewById(R.id.txt_tip2);
        txt_subTotal = (TextView) findViewById(R.id.txt_subTotal);
        txt_auth = (TextView) findViewById(R.id.tv_auth);
        txt_approval = (TextView) findViewById(R.id.approval);
        txt_taxes = (TextView) findViewById(R.id.txt_taxes);
        txt_total = (TextView) findViewById(R.id.txt_total);
        txt_no = (TextView) findViewById(R.id.txt_no);
        //   logo_merchant = (ImageView) findViewById(R.id.logoimg);
        rate_merchant = (Button) findViewById(R.id.add_rating);
        reorder_btn = (Button) findViewById(R.id.reorder);
        //  rlHedaerPrice = (RelativeLayout) findViewById(R.id.rl_hedaer_price);
        recycler = (RecyclerView) findViewById(R.id.recycler);
        mPocketBar = (CircularProgressView) findViewById(R.id.pocket);
        scan_tool = (LinearLayout) findViewById(R.id.ll_scan);
        viewMain = (RelativeLayout) findViewById(R.id.linear);
        frameLayout = (FrameLayout) findViewById(R.id.container_body);
        msg_tool = (LinearLayout) findViewById(R.id.ll_message);
        search_tool = (LinearLayout) findViewById(R.id.ll_search);

        mPocketBar.setVisibility(View.VISIBLE);


        recycler.setLayoutManager(new LinearLayoutManager(this));
        recycler.addItemDecoration(new DividerItemDecoration(this, R.drawable.divider_grey));

        setInitialData();

        ModelManager.getInstance().getHistoryManager().getInvoiceDetail(this,
                Operations.makeJsonGetInvoiceDetail(this, Utils.getElevenDigitId(data.getInvoiceId())),
                Constants.INVOICE_DETAIL_SUCCESS);
        ModelManager.getInstance().getMerchantManager().getMerchantDetail(this,
                Operations.makeJsonGetMerchantDetail(this, data.getMerchantId()), Constants.GET_MERCHANT_SUCCESS);

        ModelManager.getInstance().getInvoiceManager().getListOfInvoice(this, Operations.makeJsonGetInvoiceItems(this, Utils.getElevenDigitId(data.getInvoiceId())));

        AddTabBar.getmInstance().setupViewPager(tabLayout);
        AddTabBar.getmInstance().setupTabIcons(tabLayout, mActivity, tabOne, tabTwo, tabThree, tabFour, tabFive, tabSix);
        AddTabBar.getmInstance().bindWidgetsWithAnEvent(tabConatiner, tabLayout, HistoryDetailActivity.this, R.id.container_body2);
        ATPreferences.putBoolean(mActivity, "isCheckedH", false);

    }

    private void setListener() {
        findViewById(R.id.return_item).setOnClickListener(this);
        findViewById(R.id.add_rating).setOnClickListener(this);
        // findViewById(R.id.iv_back).setOnClickListener(this);
        findViewById(R.id.close).setOnClickListener(this);
        findViewById(R.id.send_message).setOnClickListener(this);
        findViewById(R.id.ll_message).setOnClickListener(this);
        findViewById(R.id.ll_search).setOnClickListener(this);
        findViewById(R.id.ll_scan).setOnClickListener(this);
        findViewById(R.id.reorder).setOnClickListener(this);
        findViewById(R.id.details_store).setOnClickListener(this);
        findViewById(R.id.tv_back).setOnClickListener(this);
    }

    private void setInitialData() {
        txt_no.setText("Invoice No.: " + data.getInvoiceNumber());
        if (!data.getApproval_number().isEmpty())
            txt_approval.setText("Approval No.: " + data.getApproval_Code());
        else
            txt_approval.setText("Approval No.: N/A");

        if (data.getStatus().equals("102")) {
            tvStatus.setText("Status: " + "Pending");
            txt_auth.setText("Auth: " + "Pending");

        } else {
            tvStatus.setText("Status: " + "Delivered");
            txt_auth.setText("Auth: " + "Approved");
        }

        // txt_transNo.setText("Transaction No.: " + data.getTransactionNo());
        try {
            txt_date.setText("Date: " + Utils.changeInvoiceDateFormat(data.getInvoiceDate()));
        } catch (ParseException e) {
            txt_date.setText("Date: " + data.getInvoiceDate());
            e.printStackTrace();
        }
        txt_time.setText("Time: " + Utils.getTimeFromInvoice(data.getInvoiceDate()));
        double totals = Float.parseFloat(data.getAmount()) + 10.00;
        Log.d("Total1", totals + "");
        Log.d("Total2", (new DecimalFormat("##.##").format(totals) + ""));
        txt_taxes.setText("Tax: $" + data.getInvoiceTax());
//      txt_tip1.setText("$" + data.getInvoiceTip());
        txt_tip2.setText("Tip: $" + data.getInvoiceTip());

        txt_deleiveryMethod.setText(data.getdE().get(0).getDeleiveryCompany());
        try {
            txt_deleiveryAddress.setText(data.getdE().get(0).getAddressLine());
        } catch (Exception e) {
            e.printStackTrace();
        }

        String card_typestr = data.getkT().get(0).getCardName();
        switch (card_typestr) {
            case "62":
                txt_account.setText("MasterCard");
                break;
            case "61":

                txt_account.setText("VISA");
                break;
            default:
                txt_account.setText("AMERICAN EXPRESS");
                break;
        }


        double subTotal = Double.parseDouble(data.getAmount()) - Double.parseDouble(data.getInvoiceTax()) - Double.parseDouble(data.getInvoiceTip());

        //txt_subTotal.setText("Sub Total: $" + String.format("%.2f", subTotal));
        txt_subTotal.setText("Delivery: $" + data.getdE().get(0).getDeleiveryPrice());
        //tvTotal.setText("$ " + data.getAmount());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
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
            case Constants.INVOICE_DETAIL_SUCCESS:
                double price = 0.0;
                int quantity = 0;
                list = ModelManager.getInstance().getHistoryManager().invoiceDetailBean.getRESULT().get(0).getRESULT();
                if (list == null || list.size() == 0)
                    rlHedaerPrice.setVisibility(View.GONE);
                else {
                    for (int i = 0; i < list.size(); i++) {
                        price = Double.parseDouble(list.get(i).getRegularPrice());
                        quantity = Integer.parseInt(list.get(i).getProductQty());
                        sub_Total = sub_Total + price * quantity;
                    }
                    tvTotal.setText("$" + String.format("%.2f", sub_Total));

                    grand_Total = sub_Total +
                            Double.parseDouble(data.getdE().get(0).getDeleiveryPrice()) +
                            Double.parseDouble(data.getInvoiceTax()) + Double.parseDouble(data.getInvoiceTip());

                    txt_total.setText("Total: $ " + String.format("%.2f", grand_Total));

                    History2Adapter adp = new History2Adapter(this, list, data);
                    recycler.setAdapter(adp);
                }


                mPocketBar.setVisibility(View.INVISIBLE);
                break;

            case Constants.INVOICE_ITEMS:
                listInvoiceItems = ModelManager.getInstance().getInvoiceManager().invoiceItemsBean.getRESULT().get(0).getRESULT();
                break;

            case Constants.RETURN_INVOICE_ITEMS:
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        mPocketBar.setVisibility(View.INVISIBLE);
                        Toast.makeText(HistoryDetailActivity.this, "Items submitted for return successfully", Toast.LENGTH_SHORT).show();
                    }
                });
                break;
            case Constants.GET_MERCHANT_SUCCESS:


                merchant_data = ModelManager.getInstance().getMerchantManager().merchantDetailBean.getRESULT().get(0).getRESULT().get(0);

                Picasso.with(mActivity).load(ATPreferences.readString(mActivity, Constants.KEY_IMAGE_URL) + merchant_data.getImage()).into(storeImage);
                storeName.setText(merchant_data.getName());

                break;
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.return_item:
                boolean isChecked = ATPreferences.readBoolean(mActivity, "isCheckedH");
                String status = "";
                String statusboolean = "";
           /*     if (listInvoiceItems != null && listInvoiceItems.size() > 0)
                    showDialog();
                else
                    Toast.makeText(this, "No items to return", Toast.LENGTH_SHORT).show();*/
                if (data.getStatus().equals("102")) {
                    status = "80002";
                    statusboolean = "pending";
                    if (isChecked)
                        showDialog();
                    else
                        Toast.makeText(this, "Please Select an Item", Toast.LENGTH_SHORT).show();
                } else {
                    status = "80001";
                    statusboolean = "success";
                    if (isChecked)
                        showDialog();
                    else
                        Toast.makeText(this, "Please Select an Item", Toast.LENGTH_SHORT).show();
                }
                //     ModelManager.getInstance().getReturnItemManager().returnItems(HistoryDetailActivity.this, Operations.makeJsonreturnOrder(mActivity, status, Utils.getElevenDigitId(data.getInvoiceId()), data.getStatus()));
                break;
            case R.id.add_rating:

                Intent i = new Intent(mActivity, RateMerchant.class);
                i.putExtra("merchantId", data.getMerchantId());
                i.putExtra("storeName", merchant_data.getName());
                i.putExtra("storeImage", merchant_data.getImage());
                startActivity(i);

                break;
            case R.id.details_store:
                ATPreferences.putBoolean(mActivity, "header_store", true);
                ATPreferences.putString(mActivity, "merchant_store", data.getMerchantId());
                startActivity(new Intent(mActivity, HomeActivity.class));
                break;

            case R.id.close:
                finish();
                break;
            case R.id.tv_back:
                backPressed();
                break;
            case R.id.reorder:
              /*  ModelManager.getInstance().getShoppingManager().getShoppingCart(HistoryDetailActivity.this, Operations.makeJsonPayShoppingCart
                        (HistoryDetailActivity.this, data.getMerchantId(), shoppingCompBean.getShoppingCartId(), total_Amount_Is, tip_is, shipping_id,
                                deliver_Id, card_token, total_Amount_Is, tax, shoppingCompBean, Choice_Id, Choice_Price));*/
                //   ModelManager.getInstance().getShoppingCartManager().addItemTOCart(mActivity, Operations.makeJsonAddToCartItems(mActivity, tv_quantityd.getText().toString(), productId, merchantID,option_Id));


                break;
            case R.id.send_message:
//                frameLayout.setVisibility(View.VISIBLE);
//                viewMain.setVisibility(View.GONE);
//                displayView(new FragmentMessages(), Constants.TAG_MESSAGEPAGE, new Bundle());

                // Toast.makeText(this, "Currently there is no messages for this invoice", Toast.LENGTH_SHORT).show();
                Intent obji = new Intent(this, MessageDetailActivity.class);
                obji.putExtra("invoice", data.getInvoiceId());
                startActivity(obji);
               /* Intent intent = new Intent(mActivity, SendMessage.class);

                intent.putExtra("merchantId", data.getMerchantId());
                intent.putExtra("storeName", merchant_data.getName());
                intent.putExtra("invoiceId", data.getInvoiceId());

                startActivity(intent);*/
                break;
            case R.id.ll_message:
                frameLayout.setVisibility(View.VISIBLE);
                viewMain.setVisibility(View.GONE);
                displayView(new FragmentMessages(), Constants.TAG_MESSAGEPAGE, new Bundle());
                break;

            case R.id.ll_scan:
                frameLayout.setVisibility(View.VISIBLE);
                viewMain.setVisibility(View.GONE);
                if (checkSelfPermission(Manifest.permission.CAMERA)
                        != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(new String[]{Manifest.permission.CAMERA},
                            MY_CAMERA_REQUEST_CODE);
                } else {
                    //mTxtHeading.setText("Scan");
                    //mlogo.setVisibility(View.GONE);
                    //mTxtHeading.setVisibility(View.VISIBLE);
                    displayView(new FragmentScanner(), Constants.TAG_SCANNER, new Bundle());
                }

                break;
            case R.id.ll_search:
                Utils.showSearchDialog(this);
                break;
        }
    }

    public void showSearchDialog() {
        final ArrayList<String> list = ModelManager.getInstance().getSearchManager().listAddresses;
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.CENTER;
        dialog.setContentView(R.layout.quick_search_test);

        Button submit = (Button) dialog.findViewById(R.id.submit);
        Button cancel = (Button) dialog.findViewById(R.id.cancel);
        final EditText search = (EditText) dialog.findViewById(R.id.search);
        Spinner spinner = (Spinner) dialog.findViewById(R.id.spinner);
        ArrayAdapter arrayAdapter = new ArrayAdapter(getApplicationContext(), R.layout.spinner_item, list);
        spinner.setAdapter(arrayAdapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchkey = search.getText().toString();
                startActivity(new Intent(mActivity, SearchItemActivity.class).putExtra("key", searchkey));
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


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == MY_CAMERA_REQUEST_CODE) {

            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (toolint == 0) {

                    //  Toast.makeText(this, "Camera permission granted", Toast.LENGTH_LONG).show();
                    // mTxtHeading.setText("Scan");
                    //mlogo.setVisibility(View.GONE);
                    //mTxtHeading.setVisibility(View.VISIBLE);
                    displayView(new FragmentScanner(), Constants.TAG_SCANNER, new Bundle());

                } else {
                    displayView(new FragmentScanner(), Constants.TAG_SCANNER, null);
                }
            } else {

                Toast.makeText(this, "Camera permission denied", Toast.LENGTH_LONG).show();

            }

        }
    }

    public void displayView(Fragment fragment, String tag, Bundle bundle) {
        //  if (fragment != null) {
        FragmentManager fragmentManager = getSupportFragmentManager();
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        backPressed();
    }

    public void backPressed() {
        if (viewMain.getVisibility() == View.VISIBLE) {
            finish();
        } else {
            viewMain.setVisibility(View.VISIBLE);
            frameLayout.setVisibility(View.GONE);
        }
    }

    private void showDialog() {
        // custom dialog
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        dialog.setContentView(R.layout.dialog_invoice_item);
        dialog.setTitle("Title...");

//        // set the custom dialog components - text, image and button
//        TextView text = (TextView) dialog.findViewById(R.id.text);
        final String[] arraySpinner = new String[]{"Choose a reason",
                "Bought by mistake", "Better price available", "Product damaged, but shipping box OK", "Item arrived too late", "Missing parts or accessories", "Product and shipping box both damaged", "Wrong item was sent", "Item defective or doesn't work", "Received extra item I didn't buy(no refund needed)", "No longer needed", "Didn't approve purchase", "Inaccurate website description"
        };
        Spinner s = (Spinner) dialog.findViewById(R.id.spinner);
        ListView listView = (ListView) dialog.findViewById(R.id.list);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                R.layout.spinner_item, arraySpinner);
        adapter
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        s.setAdapter(adapter);

        InvoiceItemAdapter invoiceItemAdapter = new InvoiceItemAdapter(this, 0, listInvoiceItems, status, dialog);
        listView.setAdapter(invoiceItemAdapter);
//        text.setText("Android custom dialog example!");
        LinearLayout cancel = (LinearLayout) dialog.findViewById(R.id.cancel);
        LinearLayout submit = (LinearLayout) dialog.findViewById(R.id.submit);
//        image.setImageResource(R.drawable.ic_launcher);
//
//        Button dialogButton = (Button) dialog.findViewById(R.id.dialogButtonOK);
//        // if button is clicked, close the custom dialog
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
//                try {
//                    JSONObject jsonObject = new JSONObject();
//                    jsonObject.put("11", Client.getTimeStamp());
//                    jsonObject.put("57", Utils.getDeviceId(HistoryDetailActivity.this));
//                    jsonObject.put("192", ATPreferences.readString(HistoryDetailActivity.this, Constants.KEY_USER_DEFAULT));
//                    jsonObject.put("122.45", "en");
//                    jsonObject.put("120.38", "0.0");
//                    jsonObject.put("120.39", "0.0");
//
//                    JSONArray jsonArray = new JSONArray();
//                    JSONObject obj = new JSONObject();
//                    obj.put("101", "020300332");
//
//                    JSONObject obj_param = new JSONObject();
//                    obj_param.put("53", data.getMerchantId());
//                    obj_param.put("121.75", Utils.getElevenDigitId(data.getInvoiceId()));
//                    JSONArray array = new JSONArray();
//                    for (int i = 0; i < listInvoiceItems.size(); i++) {
//                        JSONObject jsonData = new JSONObject();
//                        jsonData.put("114.144", Utils.lengtT(11, listInvoiceItems.get(i).getInvoiceNumber()));
//                        jsonData.put("114.121", listInvoiceItems.get(i).getQuantity());
//                        jsonData.put("114.98", listInvoiceItems.get(i).getActualPrice());
//                        jsonData.put("120.157", arraySpinner[0]);
//                        array.put(jsonData);
//                    }
//                    obj_param.put("PC", array);
//                    obj.put("PARAM", obj_param);
//                    jsonArray.put(obj);
//                    jsonObject.put("OPTLST", jsonArray);
//                    mPocketBar.setVisibility(View.INVISIBLE);
//                    // ModelManager.getInstance().getReturnItemManager().returnItems(HistoryDetailActivity.this, jsonObject.toString());
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
                ModelManager.getInstance().getReturnItemManager().returnItems(HistoryDetailActivity.this, Operations.makeJsonreturnOrder(mActivity, status, Utils.getElevenDigitId(data.getInvoiceId()), data.getStatus()));


            }
        });


        dialog.show();
        dialog.getWindow().setAttributes(lp);
    }

    @Override
    public void onDrawerItemSelected(View view, int position) {
        startActivity(new Intent(this, HomeActivity.class).putExtra("Drawer", position));
    }

}
