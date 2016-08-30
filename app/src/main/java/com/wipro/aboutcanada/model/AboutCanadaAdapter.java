package com.wipro.aboutcanada.model;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.wipro.aboutcanada.R;

import java.util.List;

/**
 * Created by admin on 30-Aug-16.
 */
public class AboutCanadaAdapter extends RecyclerView.Adapter<AboutCanadaAdapter.MyViewHolder> {

    private List<AboutCanada> rowsList;
    private Context ctxt;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title, description;
        public ImageView iv_image;

        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.tv_title);
            description = (TextView) view.findViewById(R.id.tv_description);
            iv_image = (ImageView) view.findViewById(R.id.iv_image);
        }
    }


    public AboutCanadaAdapter(List<AboutCanada> rowsList,Context mContext) {
        this.ctxt = mContext;
        this.rowsList = rowsList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        AboutCanada aboutCanadaObj = rowsList.get(position);
        if(!aboutCanadaObj.getTitle().equals("null")) {
            holder.title.setText(aboutCanadaObj.getTitle());
            if(!aboutCanadaObj.getDescription().equals("null")) {
                holder.description.setText(aboutCanadaObj.getDescription());
            }else{
                holder.description.setText("");
            }

            if (!aboutCanadaObj.getImage().equals("null")) {
                Picasso.with(ctxt)
                        .load(aboutCanadaObj.getImage())
                        .placeholder(R.drawable.ic_launcher)
                        .error(R.drawable.ic_launcher)
                        .resize(200, 200)
                        .into(holder.iv_image);
            } else {
                Picasso.with(ctxt)
                        .load(R.drawable.ic_launcher)
                        .resize(200, 200)
                        .into(holder.iv_image);
            }
        }
    }

    @Override
    public int getItemCount() {
        return rowsList.size();
    }
}
