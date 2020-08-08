package com.example.AmbersAR;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class ListAdapter extends ArrayAdapter<String> {
    private Context context;
    private String[] items;
    private String[] itemDescriptions;
    private int[] icons;

    public ListAdapter(@NonNull Context context, String[] items, String[] itemDescriptions, int[] icons) {
        super(context, R.layout.row_design, R.id.textView2, items);

        this.context = context;
        this.items = items;
        this.itemDescriptions = itemDescriptions;
        this.icons = icons;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @Nullable ViewGroup parent){
        LayoutInflater layoutInflater = (LayoutInflater) context.getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row = layoutInflater.inflate(R.layout.row_design, parent,false);

        ImageView icon = row.findViewById(R.id.imageView);
        TextView item = row.findViewById(R.id.item);
        TextView itemDescription = row.findViewById(R.id.subItem);

        icon.setImageResource(icons[position]);
        item.setText(items[position]);
        itemDescription.setText(itemDescriptions[position]);

        return row;
    }


}
