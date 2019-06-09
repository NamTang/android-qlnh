package com.example.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.model.Table;
import com.example.quanlynhahang.R;

public class TableAdapter extends ArrayAdapter<Table> {
    Activity context;
    private int resource;

    public TableAdapter(Activity context, int resource) {
        super(context, resource);
        this.context = context;
        this.resource = resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
//        return super.getView(position, convertView, parent);
        View custom = context.getLayoutInflater().inflate(resource, null);
        ImageView imgTable = custom.findViewById(R.id.imgTable);
        TextView txtTableName = custom.findViewById(R.id.txtTableName);

        final Table table = getItem(position);

        txtTableName.setText(table.getName());
        if (table.getStatus() == Table.FREE) {
            imgTable.setImageResource(R.drawable.dining_table_64);
        } else {
            imgTable.setImageResource(R.drawable.dining_64);
        }

        return custom;
    }
}
