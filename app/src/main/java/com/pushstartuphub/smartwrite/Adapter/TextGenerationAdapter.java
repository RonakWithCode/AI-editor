package com.pushstartuphub.smartwrite.Adapter;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.pushstartuphub.smartwrite.R;

public class TextGenerationAdapter extends ArrayAdapter<String> {
    private final Context context;
    private final String[] generationTypes;

    public TextGenerationAdapter(Context context, String[] generationTypes) {
        super(context, 0, generationTypes);
        this.context = context;
        this.generationTypes = generationTypes;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_generation_type, parent, false);
        }
        TextView tvGenerationType = convertView.findViewById(R.id.tvGenerationType);
        tvGenerationType.setText(generationTypes[position]);
        return convertView;
    }
}
