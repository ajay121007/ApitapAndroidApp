package com.apitap.views.adapters;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.apitap.R;
import com.apitap.model.Constants;
import com.apitap.model.bean.ShoppingCartDetailBean;
import com.apitap.model.preferences.ATPreferences;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sahil on 1/9/16.
 */

/*
public class CartItemAdapter extends RecyclerView.Adapter<CartItemAdapter.ViewHolder> {

    private final Activity activity;
    private List<ShoppingCartDetailBean.RESULT.DetailData> response;
    private AdapterClick adapterClick;
    private String companyName;
    private float totalPrice;
    int selected_position = -1;
    ArrayList<CheckBox> mCheckBoxes = new ArrayList<CheckBox>();

    public CartItemAdapter(Activity activity, String companyName, List<ShoppingCartDetailBean.RESULT.DetailData> response) {
        this.response = response;
        this.activity = activity;
        this.companyName = companyName;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view=null;
        if (viewType != response.size()) {


            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.checkout_row_new, null); //view_recycler_fav

        }else{
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_total, null);
        }
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        if (position < response.size()) {
            holder.txt_name.setText(response.get(position).getDelviery_options());
          //  holder.txt_desc.setText(Utils.hexToASCII(response.get(position).getDescription()));
           // holder.txt_dealername.setText("By " + companyName);
            holder.txt_quantity.setText(*/
/*"Quantity: " + *//*
response.get(position).getQuantity());
            holder.txt_price.setText("$ " + response.get(position).getPrice());
            SpinnerAdapter spinnerAdapter = new SpinnerAdapter(activity, R.layout.row_spinner, response.get(position).getdE());
            //holder.spinner.setAdapter(spinnerAdapter);
            Picasso.with(activity).load(ATPreferences.readString(activity, Constants.KEY_IMAGE_URL) + response.get(position).getImage()).into(holder.img);

           // holder.checkbox.setChecked(position==selected_position);
            //holder.txt_addtocart.setVisibility(View.VISIBLE);
          //  holder.txt_remove.setVisibility(View.VISIBLE);
            //holder.txt_shippingfee.setText("Shipping fee : $" + response.get(position).get_122159());

        */
/*    holder.ll_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ModelManager.getInstance().getDeleteItemManager().deleteItem(activity, Operations.makeJsonDeleteItemFromCart(activity, response.get(position).get_12130()));
                }
            });
*//*


            mCheckBoxes.add(holder.checkbox);
            holder.checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if(isChecked)
                    {
                        for (int i = 0; i < mCheckBoxes.size(); i++) {
                            if (mCheckBoxes.get(i) == buttonView)
                                selected_position = i;
                            else
                                mCheckBoxes.get(i).setChecked(false);
                        }

                    }
                    else
                    {
                        selected_position=-1;
                    }

                }
            });

            totalPrice = totalPrice + Float.parseFloat(response.get(position).getPrice()) * Integer.parseInt(response.get(position).getQuantity());
        } else {
           // holder.txt_shippingfee.setText("Total Cost : " + totalPrice);
           // holder.ll_delete.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return response.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private final ImageView img;
        private final Spinner spinner;
        private final LinearLayout ll_delete;
        private final CheckBox checkbox;
        private final TextView txt_name, txt_dealername, txt_quantity, txt_price, txt_addtocart, txt_remove, txt_desc, txt_deliverydays, txt_shippingfee;

        public ViewHolder(View itemView) {
            super(itemView);
            txt_name = (TextView) itemView.findViewById(R.id.txt_name);
            txt_dealername = (TextView) itemView.findViewById(R.id.txt_dealername);
            txt_price = (TextView) itemView.findViewById(R.id.txt_price);
            txt_addtocart = (TextView) itemView.findViewById(R.id.txt_addtocart);
            txt_remove = (TextView) itemView.findViewById(R.id.txt_remove);
            txt_quantity = (TextView) itemView.findViewById(R.id.txt_quantity);
            txt_desc = (TextView) itemView.findViewById(R.id.txt_desc);
            txt_deliverydays = (TextView) itemView.findViewById(R.id.delivery_days);
            txt_shippingfee = (TextView) itemView.findViewById(R.id.shipping_fee);
            img = (ImageView) itemView.findViewById(R.id.img);
            ll_delete = (LinearLayout) itemView.findViewById(R.id.delete);
            spinner = (Spinner) itemView.findViewById(R.id.spinner);
            checkbox = (CheckBox) itemView.findViewById(R.id.checkimg);
        }
    }

    public void setOnRemoveClickListner(AdapterClick adapterClick) {
        this.adapterClick = adapterClick;
    }

    public interface AdapterClick {
        public void onRemoveClick(View v, int position);
    }

}
*/
