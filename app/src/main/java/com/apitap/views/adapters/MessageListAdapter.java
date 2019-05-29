package com.apitap.views.adapters;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.apitap.R;
import com.apitap.controller.ModelManager;
import com.apitap.model.Constants;
import com.apitap.model.Operations;
import com.apitap.model.Utils;
import com.apitap.model.bean.HistoryInvoiceBean;
import com.apitap.model.bean.MessageListBean;
import com.apitap.model.preferences.ATPreferences;
import com.apitap.views.MessageDetailActivity;
import com.apitap.views.fragments.FragmentMessages;
import com.squareup.picasso.Picasso;

import org.apache.commons.codec.DecoderException;

import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by appzorro on 1/9/16.
 */

public class MessageListAdapter extends RecyclerView.Adapter<MessageListAdapter.ViewHolder> {

    private AdapterClick adapterClick;
    List<MessageListBean.RESULT.MessageData> list;
    Activity activity;
    int selected_position = -1;
    ArrayList<CheckBox> mCheckBoxes = new ArrayList<CheckBox>();

    public MessageListAdapter(Activity activity, List<MessageListBean.RESULT.MessageData> list) {
        this.activity = activity;
        this.list = list;
    }
    public void updateList(List<MessageListBean.RESULT.MessageData> itemlist){
        list = itemlist;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.messages_row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

      //  holder.txt_title.setText(list.get(position).getName());
        holder.txt_store.setText(list.get(position).getMerchantName());
        SimpleDateFormat sdf_old = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        try {
            Date old = sdf_old.parse(list.get(position).getDate());
            holder.txt_date.setText(Utils.getDateFromMsg(list.get(position).getDate()));
        } catch (ParseException e) {
            e.printStackTrace();
        }

            holder.txt_msg.setText(list.get(position).getSubject());

        if (list.get(position).getStatus().equals("1501"))
            holder.txt_status.setText("Open");
        else
            holder.txt_status.setText("Close");

        if (list.get(position).getReplied().equals("false")) {
            holder.txt_replied.setText("N");
            //holder.lin_main.setVisibility(View.GONE);
        }
        else if (list.get(position).getReplied().equals("true"))
            holder.txt_replied.setText("Y");
        else
            holder.txt_replied.setText("-");

        FragmentMessages.view_msg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent obji = new Intent(activity, MessageDetailActivity.class);
                obji.putExtra("data", list.get(position));
                activity.startActivity(obji);
            }
        });
        mCheckBoxes.add(holder.img_main);
/*
        holder.img_main.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {

                    for (int i = 0; i < mCheckBoxes.size(); i++) {
                        if (mCheckBoxes.get(i) == buttonView)
                            selected_position = i;
                        else
                            mCheckBoxes.get(i).setChecked(false);
                    }

                } else {
                    selected_position = -1;
                }

            }
        });
*/



        // Picasso.with(activity).load(ATPreferences.readString(activity, Constants.KEY_IMAGE_URL) +
         //       list.get(position).getLogoImage()).into(holder.img_main);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapterClick.onItemClick(v, position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView txt_status, txt_date, txt_msg,txt_store,txt_replied;
        private final CheckBox img_main;
        private final LinearLayout lin_main;


        public ViewHolder(View itemView) {
            super(itemView);
            txt_date = (TextView) itemView.findViewById(R.id.txt_date);
            txt_msg = (TextView) itemView.findViewById(R.id.txt_msg);
            img_main = (CheckBox) itemView.findViewById(R.id.img_main);
            txt_store = (TextView) itemView.findViewById(R.id.txtstore);
            txt_status = (TextView) itemView.findViewById(R.id.txtStatus);
            txt_replied = (TextView) itemView.findViewById(R.id.replied);
            lin_main = (LinearLayout) itemView.findViewById(R.id.mainlayout);


        }
    }

    public void setOnItemClickListner(AdapterClick adapterClick) {
        this.adapterClick = adapterClick;
    }

    public interface AdapterClick {
        public void onItemClick(View v, int position);
    }

}
