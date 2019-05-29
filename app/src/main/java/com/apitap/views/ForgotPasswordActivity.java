package com.apitap.views;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.apitap.R;
import com.apitap.controller.ModelManager;
import com.apitap.model.Operations;
import com.apitap.model.Utils;

/**
 * Created by apple on 10/08/16.
 */
public class ForgotPasswordActivity extends AppCompatActivity implements View.OnClickListener {

    EditText editEmail;
    RelativeLayout forgotpassword;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.forgotpassword_activity);
        initView();
    }

    private void initView() {

        editEmail = (EditText)findViewById(R.id.editEmail);
        forgotpassword = (RelativeLayout)findViewById(R.id.forgotpassword);
        forgotpassword.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.forgotpassword:
                String email = editEmail.getText().toString();
                if(Utils.isEmpty(email)){
                    Toast.makeText(ForgotPasswordActivity.this, "please fill email id", Toast.LENGTH_SHORT).show();
                }else{
                    ModelManager.getInstance().getforgotManager().getForgotPassword(ForgotPasswordActivity.this, Operations.makeJsonUserForgotPassword(ForgotPasswordActivity.this,email));
                }
                break;
        }
    }
}
