package com.apitap.views.adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.apitap.R;
import com.apitap.controller.ModelManager;
import com.apitap.model.Constants;
import com.apitap.model.Operations;
import com.apitap.model.bean.ShoppingCompBean;
import com.apitap.model.bean.SizeBean;
import com.apitap.model.bean.Sizedata;
import com.apitap.model.preferences.ATPreferences;
import com.apitap.views.HomeActivity;
import com.apitap.views.ShoppingCartDetailActivity;
import com.apitap.views.fragments.ShoppingCartDetailsFragment;
import com.github.rahatarmanahmed.cpv.CircularProgressView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by ashok-kumar on 16/6/16.
 */

public class ShoppingAdapter extends ArrayAdapter<String> {
    ArrayList<ShoppingCompBean> array;
    CircularProgressView progressView;
    Context context;
    String url;
    ShoppingCompBean bean;
    public ShoppingAdapter(CircularProgressView progressView,Context context, int resource, ArrayList<ShoppingCompBean> array) {
        super(context, resource);
        this.array = array;
        this.context = context;
        this.progressView=progressView;
        this.url = ATPreferences.readString(context, Constants.KEY_IMAGE_URL);
    }

    @Override
    public int getCount() {
        return array.size();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final LayoutInflater inflater = (LayoutInflater) context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.row_shopping, null);
        TextView companyName = (TextView) rowView.findViewById(R.id.company_name);
        TextView counter = (TextView) rowView.findViewById(R.id.counter);
        TextView LastAdded = (TextView) rowView.findViewById(R.id.lastadded);
        LinearLayout delete = (LinearLayout) rowView.findViewById(R.id.delete);
        ImageView image = (ImageView) rowView.findViewById(R.id.image);
        Button submit = (Button) rowView.findViewById(R.id.checkout);
        LinearLayout layoutCompanyName = (LinearLayout) rowView.findViewById(R.id.layout_name);
         bean = array.get(position);
        companyName.setText("Expiring Items: "+bean.getExpiring());
        counter.setText("No. of Items: "+bean.getItemCounter());
        LastAdded.setText("Last Added: "+bean.getLastDate().substring(0,10));
        Log.d("Carts_itemsPrice",url + array.get(position).getTotalAmount());
        Picasso.with(context).load(url + bean.getCompanyImage()).into(image);
        Log.d("hellopic",url + bean.getCompanyImage());

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Intent intent=new Intent(context, ShoppingCartDetailActivity.class);
                intent.putExtra("companyName",array.get(position).getCompanyName());
                intent.putExtra("shoppingCart",array.get(position));
                intent.putExtra("totalprice",array.get(position).getTotalAmount());
                intent.putExtra("compnyimg",array.get(position).getCompanyImage());
                intent.putExtra("compnyID",array.get(position).getMerchantId());
                intent.putExtra("dateTime",array.get(position).getLastDate());
                intent.putExtra("cartId",array.get(position).getShoppingCartId());
                context.startActivity(intent);
            }
        });

        return rowView;
    }
}
