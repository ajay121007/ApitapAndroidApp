package com.apitap.views.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.apitap.R;
import com.apitap.controller.ModelManager;
import com.apitap.model.Constants;
import com.apitap.model.Operations;
import com.apitap.model.Utils;
import com.apitap.model.customclasses.Event;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ashok-kumar on 9/6/16.
 */

public class FragmentSignup extends Fragment implements View.OnClickListener, AdapterView.OnItemSelectedListener {
    Activity mActivity;
    Spinner mSpinner;
    EditText mFirstName, mLastName, mEmail;
    String gender = "51";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_signup, container, false);
        mActivity = getActivity();
        initViews(v);
        setListeners(v);
        setGenderSpinner();
        return v;
    }

    private void initViews(View v) {
        mFirstName = (EditText) v.findViewById(R.id.first_name);
        mLastName = (EditText) v.findViewById(R.id.second_name);
        mEmail = (EditText) v.findViewById(R.id.email);
        mSpinner = (Spinner) v.findViewById(R.id.spinner);
    }

    private void setListeners(View v) {
        v.findViewById(R.id.create_account).setOnClickListener(this);
        mSpinner.setOnItemSelectedListener(this);
    }

    private void setGenderSpinner() {
        List<String> categories = new ArrayList<String>();
        categories.add("Male");
        categories.add("Female");

        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(mActivity, android.R.layout.simple_spinner_item, categories);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinner.setAdapter(dataAdapter);
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
            case Constants.ACCOUNT_ALREADY_REGISTERED:
                Toast.makeText(mActivity, event.getResponse(), Toast.LENGTH_SHORT).show();
                break;

            case Constants.ACCOUNT_NOT_REGISTERED:
                String email = mEmail.getText().toString();
                String firstName = mFirstName.getText().toString();
                String lastName = mLastName.getText().toString();
                ModelManager.getInstance().getSignUpManager().postUserDetails(mActivity, Operations.makeJsonUserSignup(mActivity, email, firstName, lastName, gender), false);
                break;
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.create_account:
                String email = mEmail.getText().toString();
                String firstName = mFirstName.getText().toString();
                String lastName = mLastName.getText().toString();
                if (Utils.isEmpty(email) || Utils.isEmpty(firstName) || Utils.isEmpty(lastName))
                    Toast.makeText(mActivity, "Please fill all details", Toast.LENGTH_SHORT).show();
                else
                    ModelManager.getInstance().getSignUpManager().postUserDetails(mActivity, Operations.makeJsonValidateUser(mActivity, email), true);
                break;
        }
    }


    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        gender = i == 0 ? "51" : "52";
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
