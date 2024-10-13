package com.pushstartuphub.smartwrite.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.pushstartuphub.smartwrite.R;

public class LengthAdapter extends ArrayAdapter<String> {
    private final Context context;
    private final String[] lengths;

    public LengthAdapter(Context context, String[] lengths) {
        super(context, 0, lengths);
        this.context = context;
        this.lengths = lengths;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_length, parent, false);
        }
        TextView tvLength = convertView.findViewById(R.id.tvLength);
        tvLength.setText(lengths[position]);
        return convertView;
    }
}
