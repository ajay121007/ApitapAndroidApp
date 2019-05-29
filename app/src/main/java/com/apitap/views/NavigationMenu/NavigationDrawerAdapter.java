package com.apitap.views.NavigationMenu;

/**
 * Created by Ravi on 29/07/15.
 */

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.apitap.R;

import java.util.Collections;
import java.util.List;


public class NavigationDrawerAdapter extends RecyclerView.Adapter<NavigationDrawerAdapter.MyViewHolder> {
    List<NavDrawerItem> data = Collections.emptyList();
    private LayoutInflater inflater;
    private Context context;
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    public static String TAG1 = "pref";

    public NavigationDrawerAdapter(Context context, List<NavDrawerItem> data) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.data = data;
        pref = context.getSharedPreferences(TAG1, context.MODE_PRIVATE);
        editor = pref.edit();
    }

    public void delete(int position) {
        data.remove(position);
        notifyItemRemoved(position);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.nav_drawer_row, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        if (position == 6) {
            if (pref.getBoolean("login", false)) {
                NavDrawerItem current = data.get(position);
                holder.title.setText("Logout");
                holder.img.setImageResource(current.getImage());
            } else {
                NavDrawerItem current = data.get(position);
                holder.title.setText(current.getTitle());
                holder.img.setImageResource(current.getImage());
            }
        } else {
            NavDrawerItem current = data.get(position);
            holder.title.setText(current.getTitle());
            holder.img.setImageResource(current.getImage());
        }


    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        private final ImageView img;
        TextView title;

        public MyViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.title);
            img = (ImageView) itemView.findViewById(R.id.img);
        }
    }
}
