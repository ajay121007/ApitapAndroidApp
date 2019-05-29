package com.apitap.views.adapters;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.apitap.R;
import com.apitap.model.Constants;
import com.apitap.model.Utils;
import com.apitap.model.bean.HistoryInvoiceBean;
import com.apitap.model.bean.InvoiceDetailBean;
import com.apitap.model.preferences.ATPreferences;
import com.squareup.picasso.Picasso;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sahil on 1/9/16.
 */

public class History2Adapter extends RecyclerView.Adapter<History2Adapter.ViewHolder> {


    private AdapterClick adapterClick;
    List<InvoiceDetailBean.RESULT.DetailData> itemList;
    Activity activity;
    LinearLayout op1lay, op2lay;
    private HistoryInvoiceBean.RESULT.Invoicedata data;
    int selected_position = -1;
    ArrayList<CheckBox> mCheckBoxes = new ArrayList<CheckBox>();

    public History2Adapter(Activity activity, List<InvoiceDetailBean.RESULT.DetailData> list, HistoryInvoiceBean.RESULT.Invoicedata data) {
        this.itemList = list;
        this.data = data;
        this.activity = activity;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.invoice_row_new, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.txt_title.setText(Utils.hexToASCII(itemList.get(position).getProductName()));
        holder.txt_qty.setText(itemList.get(position).getProductQty());
        holder.txt_price.setText("$" + itemList.get(position).getRegularPrice());

        Picasso.with(activity).load(ATPreferences.readString(activity, Constants.KEY_IMAGE_URL) + itemList.get(position).getInvoiceImage()).into(holder.img);


        holder.more_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                moreInfoDialog();
            }
        });

        mCheckBoxes.add(holder.checkbox);

        holder.checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    ATPreferences.putBoolean(activity, "isCheckedH", true);
                    for (int i = 0; i < mCheckBoxes.size(); i++) {
                        if (mCheckBoxes.get(i) == buttonView)
                            selected_position = i;
                        else
                            mCheckBoxes.get(i).setChecked(false);
                    }
                } else {
                    selected_position = -1;
                    ATPreferences.putBoolean(activity, "isCheckedH", false);
                }

            }
        });

        if (itemList.get(position).getStatus().equals("102")) {
            holder.txt_title.setTextColor(Color.parseColor("#cc0000"));
            holder.txt_qty.setTextColor(Color.parseColor("#cc0000"));
            holder.txt_price.setTextColor(Color.parseColor("#cc0000"));
        } else {
            holder.txt_title.setTextColor(Color.parseColor("#707070"));
            holder.txt_qty.setTextColor(Color.parseColor("#707070"));
            holder.txt_price.setTextColor(Color.parseColor("#707070"));
        }

        if (itemList.get(position).getcH().size() != 0) {
            holder.op1lay.setVisibility(View.VISIBLE);
            holder.tvOption1.setText(itemList.get(position).getcH().get(0).getChoiceName());
            holder.option1Price.setText("$" + itemList.get(position).getcH().get(0).getChoicePrice());
            if (itemList.get(0).getcH().size() > 1) {
                holder.op2lay.setVisibility(View.VISIBLE);
                holder.tvOption2.setText(itemList.get(position).getcH().get(1).getChoiceName());
                holder.option2price.setText("$" + itemList.get(position).getcH().get(1).getChoicePrice());
            }
        }

    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView options_price, tvOption1, tvOption2, option1Price, option2price;
        private final TextView txt_title, txt_qty, txt_price;
        private final LinearLayout lay_choices, op1lay, op2lay;
        private final ImageView img, more_info;
        private final CheckBox checkbox;

        public ViewHolder(View itemView) {
            super(itemView);
            txt_title = (TextView) itemView.findViewById(R.id.txt_name);
            txt_qty = (TextView) itemView.findViewById(R.id.txt_quantity);
            txt_price = (TextView) itemView.findViewById(R.id.txt_price);
            lay_choices = (LinearLayout) itemView.findViewById(R.id.lay_choices);
            op1lay = (LinearLayout) itemView.findViewById(R.id.opy1layout);
            op2lay = (LinearLayout) itemView.findViewById(R.id.opy2layout);
            img = (ImageView) itemView.findViewById(R.id.img);
            checkbox = (CheckBox) itemView.findViewById(R.id.checkimg);
            tvOption1 = (TextView) itemView.findViewById(R.id.tv_option1);
            tvOption2 = (TextView) itemView.findViewById(R.id.tv_option2);
            option1Price = (TextView) itemView.findViewById(R.id.option1_price);
            option2price = (TextView) itemView.findViewById(R.id.option2_price);
            more_info = (ImageView) itemView.findViewById(R.id.more_info);

        }
    }

    public void setOnEditClickListner(AdapterClick adapterClick) {
        this.adapterClick = adapterClick;
    }

    public interface AdapterClick {
        public void onEditClick(CompoundButton compoundButton, boolean position);
    }

    private void moreInfoDialog() {
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        dialog.setContentView(R.layout.more_info_checkout);
        Button done = (Button) dialog.findViewById(R.id.close);
        TextView specialInstructions = (TextView) dialog.findViewById(R.id.specs);
        TextView specialInstructions_edt = (TextView) dialog.findViewById(R.id.specs_ed);

        specialInstructions.setVisibility(View.VISIBLE);
        specialInstructions_edt.setVisibility(View.GONE);
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        String strSpecial =data.getSpeicalInstruction();
        if (!strSpecial.isEmpty())
        specialInstructions.setText(Utils.hexToASCII(data.getSpeicalInstruction()));
        else
            specialInstructions.setText("No special Instructions");
        dialog.show();
        dialog.getWindow().setAttributes(lp);
    }


}
