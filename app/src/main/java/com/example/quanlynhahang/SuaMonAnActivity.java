package com.example.quanlynhahang;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.model.NhaHang;

public class SuaMonAnActivity extends ThemMonAnActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_sua_mon_an);
        final Intent intent =getIntent();
        edName.setText(intent.getStringExtra("Name"));
        edAddress.setText(intent.getStringExtra("Address"));
        Integer price = intent.getIntExtra("Price",0);
        edPrice.setText(price.toString());
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Integer getId = intent.getIntExtra("Id",0);
                String id = getId.toString();
                String name = edName.getText().toString();
                String price = edPrice.getText().toString();
                String address = edAddress.getText().toString();
                String insertData = "UPDATE NhaHang SET TenMonAn = '"+name+"',GiaMonAn = '"+price+"',DiaDiem = '"+address+"' WHERE Id ="+id;
                DangNhapActivity.sqLiteDB.QueryData(insertData);
                finish();
            }
        });
    }
}
