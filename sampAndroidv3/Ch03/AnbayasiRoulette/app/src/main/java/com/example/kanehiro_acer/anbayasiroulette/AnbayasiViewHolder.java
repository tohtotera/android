package com.example.kanehiro_acer.anbayasiroulette;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

public class AnbayasiViewHolder extends RecyclerView.ViewHolder {

    View base;
    TextView textViewNumber;
    TextView textViewComment;

    public AnbayasiViewHolder(View v) {
        super(v);
        this.base = v;
        this.textViewNumber = (TextView) v.findViewById(R.id.number);
        this.textViewComment = (TextView) v.findViewById(R.id.comment);
    }
}
