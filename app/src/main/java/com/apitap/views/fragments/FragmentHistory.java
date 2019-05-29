package com.apitap.views.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.apitap.R;
import com.apitap.controller.ModelManager;
import com.apitap.model.Constants;
import com.apitap.model.Operations;
import com.apitap.model.bean.HistoryInvoiceBean;
import com.apitap.model.customclasses.Event;
import com.apitap.views.HistoryDetailActivity;
import com.apitap.views.HomeActivity;
import com.apitap.views.adapters.HistoryAdapter;
import com.apitap.views.customviews.DividerItemDecoration;
import com.github.rahatarmanahmed.cpv.CircularProgressView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import fr.castorflex.android.smoothprogressbar.SmoothProgressBar;
import fr.castorflex.android.smoothprogressbar.SmoothProgressBarUtils;
import fr.castorflex.android.smoothprogressbar.SmoothProgressDrawable;

public class FragmentHistory extends Fragment {

    List<HistoryInvoiceBean.RESULT.Invoicedata> list;
    HistoryAdapter adp;
    private ViewHolder holder;
    int state = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_apitap_orders, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        holder = new ViewHolder(view);
        tabContainer2Visible();
        getfocus();
        holder.mPocketBar.setVisibility(View.VISIBLE);
        holder.recycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        holder.recycler.addItemDecoration(new DividerItemDecoration(getActivity(), R.drawable.divider_grey));

        ModelManager.getInstance().getHistoryManager().getInvoiceHistory(getActivity(), Operations.makeJsonGetInvoiceHistory(getActivity()), Constants.HISTORY_INVOICE_SUCCESS);

    }


    private class ViewHolder {

        private final RecyclerView recycler;
        private final CircularProgressView mPocketBar;
        private final TextView no_orders;
        private final Button find;
        private final EditText search;

        public ViewHolder(View view) {
            recycler = (RecyclerView) view.findViewById(R.id.recycler);
            mPocketBar = (CircularProgressView) view.findViewById(R.id.pocket);
            no_orders = (TextView) view.findViewById(R.id.nomsgs);
            find = (Button) view.findViewById(R.id.find);
            search = (EditText) view.findViewById(R.id.searchtext);


        }

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
    public void onEvent(final Event event) {
        switch (event.getKey()) {
            case Constants.HISTORY_INVOICE_SUCCESS:
                clearfocus();
                list = ModelManager.getInstance().getHistoryManager().historyInvoiceBean.getRESULT().get(0).getRESULT();
                adp = new HistoryAdapter(list);
                holder.recycler.setAdapter(adp);
                adp.setOnItemClickListner(new HistoryAdapter.AdapterClick() {
                    @Override
                    public void onClick(View v, int position) {
                        Intent obji = new Intent(getActivity(), HistoryDetailActivity.class);
                        obji.putExtra("data", list.get(position));
                        obji.putExtra("status", list.get(position).getStatus());
                        startActivity(obji);
                    }
                });
                if (list.size() == 0) {
                    holder.no_orders.setVisibility(View.VISIBLE);
                    holder.recycler.setVisibility(View.GONE);
                }
                holder.mPocketBar.setVisibility(View.GONE);


                holder.find.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        state = 1;
                        if (holder.search.getText().toString().length() == 0) {
                            Toast.makeText(getActivity(), "Please input something to search", Toast.LENGTH_SHORT).show();
                        } else {
                            String newValue = holder.search.getText().toString();
                            filter(newValue);
                        }
                    }
                });
                holder.search.addTextChangedListener(txwatcher);
                break;
        }
    }

    void filter(String text) {
        List<HistoryInvoiceBean.RESULT.Invoicedata> temp = new ArrayList();
        for (HistoryInvoiceBean.RESULT.Invoicedata d : list) {
            //or use .equal(text) with you want equal match
            //use .toLowerCase() for better matches
            if (d.getInvoiceNumber().contains(text)) {
                temp.add(d);
            }
        }
        //update recyclerview
        adp.updateList(temp);
    }

    final TextWatcher txwatcher = new TextWatcher() {
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        public void onTextChanged(CharSequence s, int start, int before, int count) {

            // sms_count.setText(String.valueOf(s.length()));
            if (state == 1 && holder.search.length() == 0) {
                adp = new HistoryAdapter(list);
                holder.recycler.setAdapter(adp);
            }
        }

        public void afterTextChanged(Editable s) {
        }
    };

    public void tabContainer2Visible() {
        HomeActivity.tabContainer2.setVisibility(View.VISIBLE);
        HomeActivity.tabContainer1.setVisibility(View.GONE);
    }

    public void clearfocus() {
        getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }

    public void getfocus() {
        getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }

}
