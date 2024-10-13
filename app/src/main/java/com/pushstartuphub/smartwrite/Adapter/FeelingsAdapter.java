package com.pushstartuphub.smartwrite.Adapter;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.pushstartuphub.smartwrite.R;

public class FeelingsAdapter extends ArrayAdapter<String> {
    private final Context context;
    private final String[] feelings;

    public FeelingsAdapter(Context context, String[] feelings) {
        super(context, 0, feelings);
        this.context = context;
        this.feelings = feelings;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_feeling, parent, false);
        }
        TextView tvFeeling = convertView.findViewById(R.id.tvFeeling);
        tvFeeling.setText(feelings[position]);
        return convertView;
    }
}
