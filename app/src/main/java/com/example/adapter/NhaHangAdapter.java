package com.example.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.model.NhaHang;
import com.example.quanlynhahang.DangNhapActivity;
import com.example.quanlynhahang.R;
import com.example.quanlynhahang.SuaMonAnActivity;

import java.util.ArrayList;

public class NhaHangAdapter extends RecyclerView.Adapter<NhaHangAdapter.ViewHolder> {
    Context context;
    ArrayList<NhaHang> arrayRes;

    public NhaHangAdapter(Context context, ArrayList<NhaHang> arrayRes) {
        this.context = context;
        this.arrayRes = arrayRes;
    }

    @NonNull
    @Override

    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.dong_item_nhahang, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, int i) {
        NhaHang nhaHang = arrayRes.get(i);
        viewHolder.txtTen.setText(nhaHang.getTenmonan());
        viewHolder.txtGia.setText(nhaHang.getGiamonan() + " Dong");
        viewHolder.txtDiaChi.setText(nhaHang.getDiadiem());
    }

    @Override
    public int getItemCount() {
        return arrayRes.size();
    }

    @Override
    public void onViewRecycled(@NonNull ViewHolder holder) {
        holder.itemView.setOnLongClickListener(null);
        super.onViewRecycled(holder);
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {

        TextView txtTen, txtGia, txtDiaChi;


        public ViewHolder(@NonNull final View itemView) {
            super(itemView);
            txtTen = itemView.findViewById(R.id.txtTen);
            txtGia = itemView.findViewById(R.id.txtGia);
            txtDiaChi = itemView.findViewById(R.id.txtDiaChi);
            itemView.setOnCreateContextMenuListener(this);
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
//            menu.setHeaderTitle("Select Action");
            MenuItem edit = menu.add(Menu.NONE, 1, 1, "Edit");
            MenuItem delete = menu.add(Menu.NONE, 2, 2, "Delete");


            edit.setOnMenuItemClickListener(onChange);
            delete.setOnMenuItemClickListener(onChange);
        }


        private final MenuItem.OnMenuItemClickListener onChange = new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case 1:
                        Intent intent = new Intent(context, SuaMonAnActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK); //add this line
                        intent.putExtra("Id",arrayRes.get(getPosition()).getId());
                        intent.putExtra("Name",arrayRes.get(getPosition()).getTenmonan());
                        intent.putExtra("Address",arrayRes.get(getPosition()).getDiadiem());
                        intent.putExtra("Price",arrayRes.get(getPosition()).getGiamonan());
                        context.startActivity(intent);
                        notifyDataSetChanged();
                        return true;
                    case 2:
                        String delete = "DELETE FROM NhaHang Where Id = '" + arrayRes.get(getPosition()).getId() + "'";
                        DangNhapActivity.sqLiteDB.queryData(delete);
                        arrayRes.remove(arrayRes.get(getPosition()));
                        notifyDataSetChanged();
                        Toast.makeText(context, "Delete Success", Toast.LENGTH_LONG).show();
                        return true;
                }
                return false;
            }
        };

    }


}
