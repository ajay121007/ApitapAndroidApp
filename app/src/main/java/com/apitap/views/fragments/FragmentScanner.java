package com.apitap.views.fragments;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.apitap.R;
import com.apitap.controller.ModelManager;
import com.apitap.model.Constants;
import com.apitap.model.Operations;
import com.apitap.model.Utils;
import com.apitap.model.customclasses.Event;
import com.apitap.views.ForgotPasswordActivity;
import com.apitap.views.HomeActivity;
import com.apitap.views.LoginActivity;
import com.github.rahatarmanahmed.cpv.CircularProgressView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import fr.castorflex.android.smoothprogressbar.SmoothProgressBar;
import me.dm7.barcodescanner.zbar.Result;
import me.dm7.barcodescanner.zbar.ZBarScannerView;

/**
 * Created by ashok-kumar on 9/6/16.
 */

public class FragmentScanner extends Fragment implements View.OnClickListener,  ZBarScannerView.ResultHandler {
    EditText editEmail, editPassword;
    Activity mActivity;
    ZBarScannerView mScannerView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_scanner, container, false);
        mActivity = getActivity();
//        initViews(v);
//        setListeners(v);
        return v;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews(view);
    }

    @Override
    public void onResume() {
        super.onResume();
        mScannerView.setResultHandler(this);
        mScannerView.startCamera();
    }

    @Override
    public void onPause() {
        super.onPause();
        mScannerView.stopCamera();           // Stop camera on pause
    }

    private void setListeners(View v) {
        v.findViewById(R.id.login).setOnClickListener(this);
        v.findViewById(R.id.txtForgotPassword).setOnClickListener(this);
    }

    private void initViews(View v) {
        mScannerView = (ZBarScannerView) v.findViewById(R.id.scanner);
        tabContainer2Visible();
    }

    @Override
    public void onStart() {
        super.onStart();
      //  EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
       // EventBus.getDefault().unregister(this);
    }

    @Subscribe
    public void onEvent(Event event) {
        switch (event.getKey()) {
            case Constants.LOGIN_SUCCESS:
                CircularProgressView pocket = ((LoginActivity) mActivity).mPocketBar;
                pocket.setVisibility(View.GONE);
                startActivity(new Intent(getActivity(), HomeActivity.class));
                break;

            case Constants.ACCOUNT_NOT_REGISTERED:
                break;
        }
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
                    CircularProgressView pocket = ((LoginActivity) mActivity).mPocketBar;
                    pocket.setVisibility(View.VISIBLE);
                    ModelManager.getInstance().getLoginManager().doLogin(mActivity, Operations.makeJsonUserLogin(mActivity, email, password));
                }
                break;
            case R.id.txtForgotPassword:
                Intent i = new Intent(getActivity(), ForgotPasswordActivity.class);
                startActivity(i);
                break;
        }
    }

    @Override
    public void handleResult(Result result) {
        Log.v("qwq", result.getContents()); // Prints scan results
        Log.v("122", result.getBarcodeFormat().getName());
        showDialog(result.getContents());
        Toast.makeText(getActivity(),"Scan Complete",Toast.LENGTH_SHORT).show();
    }
    public void showDialog(final String Result){
        String topass =Result.replaceFirst("^0+(?!$)", "");
        String substring = Result.substring(Math.max(Result.length() - 11, 0));
        Bundle bundle = new Bundle();
        bundle.putString("productId", Result);
        FragmentDetails fragment = new FragmentDetails();
        fragment.setArguments(bundle);
        ((HomeActivity) getActivity()).displayView(fragment, Constants.TAG_DETAILSPAGE, bundle);
        Toast.makeText(getActivity(),Result+"   l",Toast.LENGTH_SHORT).show();
      /*  AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(getActivity());
        } else {
            builder = new AlertDialog.Builder(getActivity());
        }
        builder.setTitle("Scan Results")
                .setMessage("\n"+Result)
                .setPositiveButton("Copy", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // continue with delete
                        ClipboardManager clipboard = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
                        ClipData clip = ClipData.newPlainText("QRCode", Result);
                        clipboard.setPrimaryClip(clip);
                    }
                })
                .setNegativeButton("New Scan", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // do nothing
                        FragmentTransaction ft = getFragmentManager().beginTransaction();
                        ft.detach(FragmentScanner.this).attach(FragmentScanner.this).commit();
                    }
                })
                .show();*/
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
    public void tabContainer2Visible(){
        HomeActivity.tabContainer2.setVisibility(View.VISIBLE);
        HomeActivity.tabContainer1.setVisibility(View.GONE);
    }
    public void tabContainer1Visible(){
        HomeActivity.tabContainer2.setVisibility(View.GONE);
        HomeActivity.tabContainer1.setVisibility(View.VISIBLE);
    }
}
