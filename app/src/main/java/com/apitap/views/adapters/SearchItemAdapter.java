package com.apitap.views.adapters;

import android.content.Context;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.apitap.R;
import com.apitap.model.Constants;
import com.apitap.model.Utils;
import com.apitap.model.bean.SearchItemBean;
import com.apitap.model.preferences.ATPreferences;
import com.apitap.views.HomeActivity;
import com.apitap.views.fragments.FragmentDetails;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ashok-kumar on 16/6/16.
 */

public class SearchItemAdapter extends RecyclerView.Adapter<SearchItemAdapter.ViewHolder> {
    List<SearchItemBean.Result.ResultData> array;
    Context context;
    private AdapterClick adapterClick;
    public SearchItemAdapter(Context context,  List<SearchItemBean.Result.ResultData> array) {
        this.array = array;
        this.context = context;
    }



    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_horizontal_test, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {



        holder.description.setText(Utils.hexToASCII(array.get(position).getName()));
        String isSeen = array.get(position).getSeen();
        if (isSeen.equalsIgnoreCase("false")) {
            holder.eye.setBackgroundResource(R.drawable.green_seen);
        } else {
            holder.eye.setVisibility(View.GONE);
        }

        if (array.get(position).getActualPrice().equals(array.get(position).getPriceAfterDiscount()) || array.get(position).getPriceAfterDiscount().equals("") || array.get(position).getPriceAfterDiscount().equals("0") || array.get(position).getPriceAfterDiscount().equals("0.00")) {
            holder.rlSinglePrice.setVisibility(View.VISIBLE);
            holder.rlTwoPrice.setVisibility(View.GONE);
            holder.actualPrice.setText("$"+array.get(position).getActualPrice());
        } else {
            holder.rlSinglePrice.setVisibility(View.GONE);
            holder.rlTwoPrice.setVisibility(View.VISIBLE);
            holder.actualPrice.setText("$"+array.get(position).getActualPrice());
            holder.priceAfterDiscount.setText("$"+array.get(position).getPriceAfterDiscount());
            holder.actualPrice.setPaintFlags(holder.actualPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        }

        //description.setText(Utils.hexToASCII(array.get(position).getDescription()));

        Picasso.with(context).load(ATPreferences.readString(context, Constants.KEY_IMAGE_URL)+array.get(position).getImage()).into(holder.imageView);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapterClick.onItemClick(v, position);
            }
        });

    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public int getItemCount() {
        return array.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        private  ImageView imageView,eye;
        LinearLayout rlSinglePrice,rlTwoPrice;
        TextView description,price,priceAfterDiscount,actualPrice;
        public ViewHolder(View itemView) {
            super(itemView);

             imageView = (ImageView) itemView.findViewById(R.id.image);
             actualPrice = (TextView) itemView.findViewById(R.id.actual_price);
             priceAfterDiscount = (TextView) itemView.findViewById(R.id.price_after_discount);
             price = (TextView) itemView.findViewById(R.id.price);

             eye = (ImageView) itemView.findViewById(R.id.eye);
             rlSinglePrice = (LinearLayout) itemView.findViewById(R.id.rel_single_price);
             rlTwoPrice = (LinearLayout) itemView.findViewById(R.id.rl_two_price);
             description = (TextView) itemView.findViewById(R.id.description);
        }
    }
    public void setOnItemClickListner(AdapterClick adapterClick) {
        this.adapterClick = adapterClick;
    }

    public interface AdapterClick {
        public void onItemClick(View v, int position);
    }
}
