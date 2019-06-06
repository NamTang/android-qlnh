package com.example.quanlynhahang;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import com.example.model.NhaHang;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    ArrayList<NhaHang> arrayRes;
    NhaHangAdapter nhaHangAdapter;
    Button btnAddEating;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        btnAddEating = findViewById(R.id.btnAddEating);
        recyclerView = findViewById(R.id.recycle_view);
        arrayRes = new ArrayList<>();
        nhaHangAdapter = new NhaHangAdapter(MainActivity.this,arrayRes);

        GridLayoutManager layoutManager = new GridLayoutManager(MainActivity.this,2);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(nhaHangAdapter);


        //Get value
        GetData();

        btnAddEating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,ThemMonAnActivity.class);
                startActivity(intent);
            }
        });
        registerForContextMenu(recyclerView);
    }
    public void GetData()
    {
        arrayRes.clear();
        Cursor cursor = DangNhapActivity.sqLiteDB.GetData("SELECT * FROM NhaHang");
        while (cursor.moveToNext())
        {
            int id = cursor.getInt(0);
            String ten_mon_an = cursor.getString(1);
            int gia_mon_an = cursor.getInt(2);
            String dia_diem = cursor.getString(3);
            arrayRes.add(new NhaHang(id,ten_mon_an,gia_mon_an,dia_diem));
        }
        nhaHangAdapter.notifyDataSetChanged();
    }
    @Override
    protected void onRestart() {
        GetData();
        super.onRestart();
    }

}
