package com.apitap.views.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.apitap.R;
import com.apitap.controller.ModelManager;
import com.apitap.model.Constants;
import com.apitap.model.Operations;
import com.apitap.model.bean.MessageListBean;
import com.apitap.model.customclasses.Event;
import com.apitap.views.HomeActivity;
import com.apitap.views.MessageDetailActivity;
import com.apitap.views.adapters.MessageListAdapter;
import com.apitap.views.customviews.DividerItemDecoration;
import com.github.rahatarmanahmed.cpv.CircularProgressView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentMessages extends Fragment {


    private ViewHolder holder;
    MessageListAdapter adp;
    List<MessageListBean.RESULT.MessageData> list;
    int state = 0;
    public static Button view_msg;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.activity_apitap_message, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        holder = new ViewHolder(view);
        view_msg = (Button) view.findViewById(R.id.view_msg);
        view_msg.setVisibility(View.GONE);
        tabContainer2Visible();
        holder.recycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        holder.recycler.addItemDecoration(new DividerItemDecoration(getActivity(), R.drawable.divider_grey));
        holder.mPocketBar.setVisibility(View.VISIBLE);

        ModelManager.getInstance().getMessageManager().getAllMessages(getActivity(),
                Operations.makeJsonAllMessages(getActivity()), Constants.ALL_MESSAGES_SUCCESS);

        holder.find.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                state = 1;
                if (holder.search_msg.getText().toString().length() == 0) {
                    Toast.makeText(getActivity(), "Please input something to search", Toast.LENGTH_SHORT).show();
                } else {
                    String query = holder.search_msg.getText().toString();
                    filter(query);
                }
            }
        });
//
//        getView().setFocusableInTouchMode(true);
//        getView().requestFocus();
//        getView().setOnKeyListener( new View.OnKeyListener()
//        {
//            @Override
//            public boolean onKey( View v, int keyCode, KeyEvent event )
//            {
//                if( keyCode == KeyEvent.KEYCODE_BACK )
//                {
//                    getFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
//                    return true;
//                }
//                return false;
//            }
//        } );


        holder.search_msg.addTextChangedListener(txwatcher);
    }

    private class ViewHolder {

        private final RecyclerView recycler;
        private final CircularProgressView mPocketBar;
        private final TextView no_messages;
        private final EditText search_msg;
        private final Button find;


        public ViewHolder(View view) {
            recycler = (RecyclerView) view.findViewById(R.id.recycler);
            mPocketBar = (CircularProgressView) view.findViewById(R.id.pocket);
            no_messages = (TextView) view.findViewById(R.id.nomsgs);
            search_msg = (EditText) view.findViewById(R.id.searchmsg);
            find = (Button) view.findViewById(R.id.find);


        }
    }

    void filter(String text) {
        List<MessageListBean.RESULT.MessageData> temp = new ArrayList();
        for (MessageListBean.RESULT.MessageData d : list) {
            //or use .equal(text) with you want equal match
            //use .toLowerCase() for better matches
            if (d.getSubject().contains(text)) {
                temp.add(d);
            }
        }
        //update recyclerview
        adp.updateList(temp);
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    final TextWatcher txwatcher = new TextWatcher() {
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        public void onTextChanged(CharSequence s, int start, int before, int count) {

            // sms_count.setText(String.valueOf(s.length()));
            if (state == 1 && holder.search_msg.length() == 0) {
                adp = new MessageListAdapter(getActivity(), list);
                holder.recycler.setAdapter(adp);
            }
        }

        public void afterTextChanged(Editable s) {
        }
    };

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe
    public void onEvent(final Event event) {
        switch (event.getKey()) {
            case Constants.ALL_MESSAGES_SUCCESS:

                list = ModelManager.getInstance().getMessageManager().messageListBean.getRESULT().get(0).getRESULT();
                adp = new MessageListAdapter(getActivity(), list);
                holder.recycler.setAdapter(adp);
                Log.d("ListSizess", list.size() + "");
                if (list.size() == 0) {
                    holder.no_messages.setVisibility(View.VISIBLE);
                    holder.recycler.setVisibility(View.GONE);


                }
                adp.setOnItemClickListner(new MessageListAdapter.AdapterClick() {
                    @Override
                    public void onItemClick(View v, int position) {

//                        ImageView selectedImage = (ImageView) v.findViewById(R.id.img_main);
  //                      selectedImage.setImageResource(R.drawable.ic_icon_radio_button);

                        Intent obji = new Intent(getActivity(), MessageDetailActivity.class);
                        obji.putExtra("data", list.get(position));
                        startActivity(obji);
                    }
                });
                holder.mPocketBar.setVisibility(View.GONE);
                break;
        }
    }
    public void tabContainer2Visible(){
        HomeActivity.tabContainer2.setVisibility(View.VISIBLE);
        HomeActivity.tabContainer1.setVisibility(View.GONE);
    }
    public void tabContainer1Visible(){
        HomeActivity.tabContainer2.setVisibility(View.GONE);
        HomeActivity.tabContainer1.setVisibility(View.VISIBLE);
    }
}
