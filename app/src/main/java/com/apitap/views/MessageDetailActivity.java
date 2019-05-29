package com.apitap.views;

import android.Manifest;
import android.app.Activity;
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
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.apitap.R;
import com.apitap.controller.ModelManager;
import com.apitap.model.AddTabBar;
import com.apitap.model.Constants;
import com.apitap.model.Operations;
import com.apitap.model.Utils;
import com.apitap.model.bean.MessageListBean;
import com.apitap.model.customclasses.Event;
import com.apitap.model.preferences.ATPreferences;

import com.apitap.views.NavigationMenu.FragmentDrawer;
import com.apitap.views.adapters.MessageListAdapter;
import com.apitap.views.fragments.FragmentItems;
import com.apitap.views.fragments.FragmentMessages;
import com.apitap.views.fragments.FragmentScanner;
import com.apitap.views.fragments.FragmentSearch;
import com.apitap.views.fragments.FragmentSpecial;
import com.apitap.views.fragments.Fragment_Ads;
import com.github.rahatarmanahmed.cpv.CircularProgressView;
import com.squareup.picasso.Picasso;

import org.apache.commons.codec.DecoderException;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import fr.castorflex.android.smoothprogressbar.SmoothProgressBar;
import fr.castorflex.android.smoothprogressbar.SmoothProgressBarUtils;
import fr.castorflex.android.smoothprogressbar.SmoothProgressDrawable;

public class MessageDetailActivity extends AppCompatActivity implements FragmentDrawer.FragmentDrawerListener, View.OnClickListener {

    private RecyclerView recycler;
    public static EditText edit_msg;
    public static String mesgStr;
    private TextView invoice, date, time, status;
    private RelativeLayout top_header;
    private static Button btn_send;
    public static LinearLayout iv_back;
    public static MessageListBean.RESULT.MessageData data;
    private static CircularProgressView mPocketBar;
    List<MessageListBean.RESULT.MessageData> list;
    public static Activity mActivity;
    int scrollDist = 0;
    LinearLayout reply_invoice;
    boolean isVisible = true;
    static final float MINIMUM = 25;
    private FragmentDrawer drawerFragment;
    private Toolbar mToolbar;
    public static TabLayout tabLayout;
    private TextView tabOne, tabTwo, tabThree, tabFour, tabFive, tabSix, title;
    LinearLayout tabConatiner;
    LinearLayout search_tool;
    LinearLayout scan_tool;
    LinearLayout msg_tool;
    LinearLayout ll_back;
    RelativeLayout viewMain;
    FrameLayout frameLayout;
    private static int toolint = 0;
    private static String invoiceStr = "";
    private static String productId = "";
    private static final int MY_CAMERA_REQUEST_CODE = 100;
    TextView tv_noMsg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_screen_new);
        mActivity = this;

        if (getIntent().hasExtra("productId"))
            productId = getIntent().getStringExtra("productId");
        else if (getIntent().hasExtra("invoice"))
            invoiceStr = getIntent().getStringExtra("invoice");
        else
            data = (MessageListBean.RESULT.MessageData) getIntent().getSerializableExtra("data");

        initViews();
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
        mPocketBar = (CircularProgressView) findViewById(R.id.pocket);
        recycler = (RecyclerView) findViewById(R.id.recycler);
        edit_msg = (EditText) findViewById(R.id.edit_msg);
        iv_back = (LinearLayout) findViewById(R.id.iv_back);
        reply_invoice = (LinearLayout) findViewById(R.id.reply_invoice);
        top_header = (RelativeLayout) findViewById(R.id.top_header);
        btn_send = (Button) findViewById(R.id.btn_send);
        invoice = (TextView) findViewById(R.id.invoice);
        tv_noMsg = (TextView) findViewById(R.id.no_msgTxt);
        date = (TextView) findViewById(R.id.datetxt);
        title = (TextView) findViewById(R.id.title_reply);
        time = (TextView) findViewById(R.id.timetxt);
        viewMain = (RelativeLayout) findViewById(R.id.linear);
        frameLayout = (FrameLayout) findViewById(R.id.container_body);
        msg_tool = (LinearLayout) findViewById(R.id.ll_message);
        ll_back = (LinearLayout) findViewById(R.id.back_ll);
        scan_tool = (LinearLayout) findViewById(R.id.ll_scan);
        search_tool = (LinearLayout) findViewById(R.id.ll_search);
        status = (TextView) findViewById(R.id.status);
        tabConatiner = (LinearLayout) findViewById(R.id.tab_container);
        tabLayout = (TabLayout) findViewById(R.id.tabs);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        layoutManager.setStackFromEnd(true);
        recycler.setLayoutManager(layoutManager);

        search_tool.setOnClickListener(this);
        scan_tool.setOnClickListener(this);
        msg_tool.setOnClickListener(this);
        ll_back.setOnClickListener(this);

//        recycler.setOnScrollListener(new MyRecyclerScroll() {
//            @Override
//            public void show() {
//                top_header.setVisibility(View.VISIBLE);
//            }
//
//            @Override
//            public void hide() {
//                top_header.setVisibility(View.GONE);
//            }
//        });


        recycler.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0 && top_header.getVisibility() == View.VISIBLE) {
                    top_header.setVisibility(View.GONE);
                } else if (dy < 0 && top_header.getVisibility() != View.VISIBLE) {
                    // top_header.setVisibility(View.VISIBLE);
                } else {
                    top_header.setVisibility(View.VISIBLE);
                }
            }
        });

        if (invoiceStr != null && !invoiceStr.isEmpty()) {
            ModelManager.getInstance().getMessageManager().getMessageDetail(this,
                    Operations.makeJsonGetMessagesInvoice(this, invoiceStr), Constants.MESSAGE_DETAIL_SUCCESS);
            title.setText("Re. Invoice:");
        } else if (productId != null && !productId.isEmpty()) {
            ModelManager.getInstance().getMessageManager().getMessageDetail(this,
                    Operations.getMessageProduct(this, productId), Constants.MESSAGE_DETAIL_SUCCESS);
            title.setText("Re. Product:");
        } else {
            title.setText("Re. Message:");
            ModelManager.getInstance().getMessageManager().getMessageDetail(this,
                    Operations.makeJsonGetMessagesDetail(this, data.getParentId()), Constants.MESSAGE_DETAIL_SUCCESS);
        }
        AddTabBar.getmInstance().setupViewPager(tabLayout);
        AddTabBar.getmInstance().setupTabIcons(tabLayout, mActivity, tabOne, tabTwo, tabThree, tabFour, tabFive, tabSix);
        AddTabBar.getmInstance().bindWidgetsWithAnEvent(tabConatiner, tabLayout, MessageDetailActivity.this, R.id.container_body2);

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
            case Constants.MESSAGE_DETAIL_SUCCESS:
                mPocketBar.setVisibility(View.GONE);
                list = ModelManager.getInstance().getMessageManager().messageDetailBean.getRESULT().get(0).getRESULT();
                Log.d("listMessage", list.size() + " l");
                if (list.get(0).getSubject() != null) {
                    MessageDetailAdapter adp = new MessageDetailAdapter(this, list);
                    recycler.setAdapter(adp);
                    invoice.setText(list.get(0).getInvoiceId());
                    date.setText("Date: " + Utils.getDateFromMsg(list.get(0).getDate()));
                    time.setText("Time: " + Utils.getTimeFromInvoice(list.get(0).getDate()));
                    if (list.get(0).getStatus().equals("1501"))
                        status.setText("Status: " + "Open");
                    else
                        status.setText("Status: " + "Close");

                }
                else{
                    date.setText("Date: " + "N/A");
                    time.setText("Time: " + "N/A");
                    status.setText("Status: " + "N/A");
                    reply_invoice.setVisibility(View.GONE);
                    tv_noMsg.setVisibility(View.VISIBLE);
                    recycler.setVisibility(View.GONE);
                    edit_msg.setEnabled(false);

                   /* btn_send.setVisibility(View.GONE);
                    edit_msg.setVisibility(View.GONE);
                    Toast.makeText(mActivity, "Message Retrieving Failed", Toast.LENGTH_LONG).show();*/
                }
                //  mPocketBar.progressiveStop();
                // mPocketBar.setVisibility(View.INVISIBLE);

                break;

            case Constants.MESSAGE_SEND_SUCCESS:
                mPocketBar.setVisibility(View.GONE);
                if (invoiceStr != null && !invoiceStr.isEmpty())
                    ModelManager.getInstance().getMessageManager().getMessageDetail(this,
                            Operations.makeJsonGetMessagesInvoice(this, invoiceStr), Constants.MESSAGE_DETAIL_SUCCESS);

                else if (productId != null && !productId.isEmpty())
                    ModelManager.getInstance().getMessageManager().getMessageDetail(this,
                            Operations.getMessageProduct(this, productId), Constants.MESSAGE_DETAIL_SUCCESS);
                else
                    ModelManager.getInstance().getMessageManager().getMessageDetail(this,
                            Operations.makeJsonGetMessagesDetail(this, data.getParentId()), Constants.MESSAGE_DETAIL_SUCCESS);

                Log.d("Message Success", "Messages");
                break;
            case -1:
                top_header.setVisibility(View.GONE);
                mPocketBar.setVisibility(View.GONE);
                date.setText("Date: " + "N/A");
                time.setText("Time: " + "N/A");
                status.setText("Status: " + "N/A");
                reply_invoice.setVisibility(View.GONE);
                tv_noMsg.setVisibility(View.VISIBLE);
                recycler.setVisibility(View.GONE);
                edit_msg.setEnabled(false);
               // Toast.makeText(mActivity, "Message Retrieving Failed", Toast.LENGTH_LONG).show();
                break;

        }
    }

    @Override
    public void onDrawerItemSelected(View view, int position) {
        startActivity(new Intent(this, HomeActivity.class).putExtra("Drawer", position));
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_search:
                Utils.showSearchDialog(this);
                break;
            case R.id.back_ll:
                if (viewMain.getVisibility() == View.VISIBLE) {
                    finish();
                } else {
                    viewMain.setVisibility(View.VISIBLE);
                    frameLayout.setVisibility(View.GONE);
                }
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
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (viewMain.getVisibility() == View.VISIBLE) {
            finish();
        } else {
            viewMain.setVisibility(View.VISIBLE);
            frameLayout.setVisibility(View.GONE);
        }

    }


    public static class MessageDetailAdapter extends RecyclerView.Adapter<MessageDetailAdapter.ViewHolder> {

        private AdapterClick adapterClick;
        List<MessageListBean.RESULT.MessageData> list;
        Activity activity;

        public MessageDetailAdapter(Activity activity, List<MessageListBean.RESULT.MessageData> list) {
            this.activity = activity;
            this.list = list;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_row, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, final int position) {

            //  holder.txt_title.setText(list.get(position).getName());
            SimpleDateFormat sdf_old = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            try {
                Date old = sdf_old.parse(list.get(position).getDate());
                holder.txt_date.setText(Utils.getTimeAgo(old.getTime(), activity));

            } catch (ParseException e) {
                e.printStackTrace();
            }
            try {
                holder.txt_msg.setText(Utils.getStringHexaDecimal(list.get(position).getContextData()));
            } catch (DecoderException e) {
                e.printStackTrace();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            holder.txt_time.setText(Utils.getTimeFromInvoice(list.get(position).getDate()));
            Picasso.with(activity).load(ATPreferences.readString(activity, Constants.KEY_IMAGE_URL) +
                    list.get(position).getLogoImage()).into(holder.img_main);

            btn_send.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    String message_str = edit_msg.getText().toString();
                    String hexmsg = Utils.convertStringToHex(message_str);
                    if (!message_str.isEmpty()) {
                        mPocketBar.setVisibility(View.VISIBLE);


                        if (invoiceStr != null && !invoiceStr.isEmpty())
                            ModelManager.getInstance().getMessageManager().sendMessage(mActivity,
                                    Operations.sendMessageReply(mActivity, list.get(0).getParentId(),
                                            "1", list.get(0).getMerchantReceiver(),
                                            Utils.getElevenDigitId(list.get(0).getType()), list.get(0).getSubject(), hexmsg, Utils.getElevenDigitId(invoiceStr), list.get(0).getId(), ""), Constants.MESSAGE_SEND_SUCCESS);

                        else if (productId != null && !productId.isEmpty())
                            ModelManager.getInstance().getMessageManager().sendMessage(mActivity,
                                    Operations.sendMessageReply(mActivity, list.get(0).getParentId(),
                                            "2", list.get(0).getMerchantReceiver(),
                                            Utils.getElevenDigitId(list.get(0).getType()), list.get(0).getSubject(), hexmsg, "", list.get(0).getId(), productId), Constants.MESSAGE_SEND_SUCCESS);
                        else
                            ModelManager.getInstance().getMessageManager().sendMessage(mActivity,
                                    Operations.sendMessageReply(mActivity, data.getParentId(),
                                            "3", data.getMerchantReceiver(),
                                            Utils.getElevenDigitId(data.getType()), data.getSubject(), hexmsg, Utils.getElevenDigitId(list.get(0).getInvoiceId()), list.get(0).getId(), ""), Constants.MESSAGE_SEND_SUCCESS);
                        edit_msg.setText("");
                    }
                }
            });

//            holder.itemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    adapterClick.onItemClick(v, position);
//                }
//            });
        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {

            private final TextView txt_title, txt_date, txt_msg, txt_time, txt_sending;
            private final ImageView img_main;
            private final LinearLayout replySent;

            public ViewHolder(View itemView) {
                super(itemView);
                txt_title = (TextView) itemView.findViewById(R.id.txt_title);
                txt_date = (TextView) itemView.findViewById(R.id.txt_date);
                txt_time = (TextView) itemView.findViewById(R.id.txt_time);
                txt_sending = (TextView) itemView.findViewById(R.id.sending);
                txt_msg = (TextView) itemView.findViewById(R.id.txt_msg);
                img_main = (ImageView) itemView.findViewById(R.id.img_main);
                replySent = (LinearLayout) itemView.findViewById(R.id.replysent);

            }
        }

        public void setOnItemClickListner(AdapterClick adapterClick) {
            this.adapterClick = adapterClick;
        }

        public interface AdapterClick {
            public void onItemClick(View v, int position);
        }

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

}
