package com.apitap.views.fragments;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
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
import com.apitap.views.WebViewActivity;
import com.github.rahatarmanahmed.cpv.CircularProgressView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

/**
 * Created by ashok-kumar on 9/6/16.
 */

public class FragmentLogin extends Fragment implements View.OnClickListener {
    EditText editEmail, editPassword;
    Activity mActivity;
    TextView forgot_password,policy;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_login, container, false);
        mActivity = getActivity();
        initViews(v);
        setListeners(v);
        return v;
    }

    private void setListeners(View v) {
        v.findViewById(R.id.login).setOnClickListener(this);
        v.findViewById(R.id.txtForgotPassword).setOnClickListener(this);
        v.findViewById(R.id.create_password).setOnClickListener(this);
    }

    private void initViews(View v) {
        editEmail = (EditText) v.findViewById(R.id.editEmail);
        editPassword = (EditText) v.findViewById(R.id.editPassword);
        forgot_password = (TextView) v.findViewById(R.id.create_password);
        policy = (TextView) v.findViewById(R.id.policy);
        setSpan();
    }

    private void setSpan() {
        SpannableString ss = new SpannableString(getContext().getResources().getString(R.string.privacy));
        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(View textView) {
                startActivity(new Intent(getContext(), WebViewActivity.class));
            }
            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setUnderlineText(false);
                ds.setColor(getContext().getResources().getColor(R.color.colorBlue));
            }
        };
        ss.setSpan(clickableSpan, 31, 53, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);


        policy.setText(ss);
        policy.setMovementMethod(LinkMovementMethod.getInstance());
        policy.setHighlightColor(Color.TRANSPARENT);
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
            case Constants.LOGIN_SUCCESS:
                ModelManager.getInstance().getLoginManager().registerFCM(getActivity(), Operations.makeJsonGetFCM(getActivity()));
                CircularProgressView pocket=((LoginActivity)mActivity).mPocketBar;
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
                    CircularProgressView pocket=((LoginActivity)mActivity).mPocketBar;
                    pocket.setVisibility(View.VISIBLE);
                    ModelManager.getInstance().getLoginManager().doLogin(mActivity, Operations.makeJsonUserLogin(mActivity, email, password));
                }
                break;
            case R.id.txtForgotPassword:
                Intent i = new Intent(getContext(), ForgotPasswordActivity.class);
                startActivity(i);
                break;
            case R.id.create_password:
              startActivity(new Intent(getContext(), WebViewActivity.class));
                break;
        }
    }
}
