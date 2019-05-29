package com.apitap.views;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.apitap.R;
import com.apitap.controller.ModelManager;
import com.apitap.model.Constants;
import com.apitap.model.Operations;
import com.apitap.model.Utils;
import com.apitap.model.customclasses.Event;
import com.apitap.model.customclasses.Event_Add_Address;
import com.apitap.views.NavigationMenu.FragmentDrawer;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;

import fr.castorflex.android.smoothprogressbar.SmoothProgressBar;
import fr.castorflex.android.smoothprogressbar.SmoothProgressBarUtils;
import fr.castorflex.android.smoothprogressbar.SmoothProgressDrawable;

/**
 * Created by Shami on 6/10/2017.
 */

public class SendMessage extends AppCompatActivity implements View.OnClickListener, FragmentDrawer.FragmentDrawerListener {

    ArrayList<String> type_list = new ArrayList<String>();
    EditText subject;
    String merchantId = "";
    EditText message;
    Spinner spinner;
    TextView merchantName;
    String merchantNamestr;
    String InvoiceIdstr;

    String type;
    Button sendMessage;
    private SmoothProgressBar mPocketBar;
    private FragmentDrawer drawerFragment;
    private Toolbar mToolbar;

    @Override

    protected void onCreate(Bundle saved) {
        super.onCreate(saved);
        setContentView(R.layout.send_message);
        if (getIntent() != null) {
            merchantId = getIntent().getStringExtra("merchantId");
            merchantNamestr = getIntent().getStringExtra("storeName");
            InvoiceIdstr = getIntent().getStringExtra("invoiceId");

        }
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

        subject = (EditText) findViewById(R.id.editTextSubject2);
        sendMessage = (Button) findViewById(R.id.buttonSend);
        message = (EditText) findViewById(R.id.editTextMessage);
        merchantName = (TextView) findViewById(R.id.editTextTo);
        merchantName.setText(merchantNamestr);
        spinner = (Spinner) findViewById(R.id.editTextSubject);
        mPocketBar = (SmoothProgressBar) findViewById(R.id.pocket);


        mPocketBar.setSmoothProgressDrawableBackgroundDrawable(
                SmoothProgressBarUtils.generateDrawableWithColors(
                        getResources().getIntArray(R.array.pocket_background_colors),
                        ((SmoothProgressDrawable) mPocketBar.getIndeterminateDrawable()).getStrokeWidth()));

        type_list.add("Contact Form");
        type_list.add("Message");
        type_list.add("Help Ticket");
        type_list.add("Note");
        type_list.add("Share offer");
        type_list.add("Consultation");
        type_list.add("Report Abuse");
        type_list.add("Notification");

        ArrayAdapter<String> arrayadpter = new ArrayAdapter<String>(this, R.layout.address_row2, R.id.txt_type, type_list);
        spinner.setAdapter(arrayadpter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 0)
                    type = "91";
                else if (i == 1)
                    type = "92";
                else if (i == 2)
                    type = "93";
                else if (i == 3)
                    type = "94";
                else if (i == 4)
                    type = "95";
                else if (i == 5)
                    type = "96";
                else if (i == 6)
                    type = "97";
                else if (i == 7)
                    type = "98";

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        sendMessage.setOnClickListener(this);


    }

    @Subscribe
    public void onEvent(Event event) {

        //      Utils.stopDialog();
        switch (event.getKey()) {
            case Constants.MESSAGE_SEND_SUCCESS:
                mPocketBar.setVisibility(View.INVISIBLE);
                mPocketBar.progressiveStop();
                Toast.makeText(getApplicationContext(), "Message Sent", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.buttonSend:
                mPocketBar.setVisibility(View.VISIBLE);
                mPocketBar.progressiveStart();

                String subject_str = subject.getText().toString();
                String message_str = Utils.convertStringToHex(message.getText().toString());


                ModelManager.getInstance().getMessageManager().sendMessage(this,
                        Operations.sendMessage(this, merchantId, Utils.getElevenDigitId(type), subject_str, message_str, Utils.getElevenDigitId(InvoiceIdstr)), Constants.MESSAGE_SEND_SUCCESS);

                break;

        }
    }

    @Override
    public void onDrawerItemSelected(View view, int position) {
        startActivity(new Intent(this, HomeActivity.class).putExtra("Drawer",position));
    }
}
