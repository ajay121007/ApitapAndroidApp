package com.apitap.views.adapters;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.apitap.R;
import com.apitap.model.bean.InvoiceItemsBean;
import com.apitap.model.bean.SizeBean;
import com.apitap.model.bean.Sizedata;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ashok-kumar on 16/6/16.
 */

public class InvoiceItemAdapter extends ArrayAdapter<String> {
    List<InvoiceItemsBean.RESULT.Invoicedata> listInvoiceItems;
    Context context;
    int quantity = 1;
    private String status;
    AdapterClick adapterClick;
    Dialog dialog;

    public InvoiceItemAdapter(Context context, int resource, List<InvoiceItemsBean.RESULT.Invoicedata> listInvoiceItems, String status, Dialog dialog) {
        super(context, resource);
        this.listInvoiceItems = listInvoiceItems;
        this.context = context;
        this.status=status;
        this.dialog=dialog;
    }

    @Override
    public int getCount() {
        if(listInvoiceItems==null)
            return 0;
        return listInvoiceItems.size();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.row_invoice_item, null);
        TextView label = (TextView) rowView.findViewById(R.id.name);
        TextView quantity = (TextView)rowView.findViewById(R.id.quantity);
        TextView price = (TextView)rowView.findViewById(R.id.price);
        final LinearLayout delete = (LinearLayout)rowView.findViewById(R.id.delete);

        label.setText(listInvoiceItems.get(position).getItemName());
        quantity.setText(listInvoiceItems.get(position).getQuantity());
        price.setText(listInvoiceItems.get(position).getActualPrice());

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listInvoiceItems.remove(position);
                notifyDataSetChanged();
                if(listInvoiceItems.size()==0){
                    dialog.dismiss();
                }
            }
        });
        if (status.equals("102")) {
            label.setTextColor(Color.parseColor("#cc0000"));
            quantity.setTextColor(Color.parseColor("#cc0000"));
            price.setTextColor(Color.parseColor("#cc0000"));
        } else {
            label.setTextColor(Color.parseColor("#707070"));
            quantity.setTextColor(Color.parseColor("#707070"));
            price.setTextColor(Color.parseColor("#707070"));
        }


        return rowView;
    }

    public void setOnTextClickListener(AdapterClick adapterClick) {
        this.adapterClick = adapterClick;
    }

    public interface AdapterClick {
        public void onTextClick(View v, String quantity);
    }
}
