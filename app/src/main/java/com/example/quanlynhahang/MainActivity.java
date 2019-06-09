package com.example.quanlynhahang;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SearchView;

import com.example.adapter.NhaHangAdapter;
import com.example.model.NhaHang;
import com.example.service.SQLiteDB;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    public static SQLiteDB sqLiteDB;
    RecyclerView recyclerView;
    ArrayList<NhaHang> arrayRes;
    NhaHangAdapter nhaHangAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initActionBar();
        sqLiteDB = new SQLiteDB(this, "QuanLyNhaHang.db", null, 1);

        recyclerView = findViewById(R.id.recycle_view);
        arrayRes = new ArrayList<>();
        nhaHangAdapter = new NhaHangAdapter(MainActivity.this,arrayRes);

        GridLayoutManager layoutManager = new GridLayoutManager(MainActivity.this,2);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(nhaHangAdapter);


        //Get value
        getData();

        registerForContextMenu(recyclerView);
    }

    private void initActionBar() {
        this.getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setCustomView(R.layout.custom_action_bar);
        View customActionBar = getSupportActionBar().getCustomView();

        // back button
        // hide and disable it cuz this activity don't need
        ImageView imgBack = customActionBar.findViewById(R.id.imgBack);
        imgBack.setVisibility(View.INVISIBLE);
        imgBack.setEnabled(false);

        // menu button
        ImageView imgMenu = customActionBar.findViewById(R.id.imgMenu);
        imgMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goMenu();
            }
        });
    }

    private void goMenu() {
        Intent menu = new Intent(this, MenuActivity.class);
        menu.putExtra("from", "main");
        startActivity(menu);
    }

    public void getData()
    {
        arrayRes.clear();
        Cursor cursor = sqLiteDB.getData("SELECT * FROM NhaHang");
        while (cursor.moveToNext())
        {
            int id = cursor.getInt(0);
            String ten_mon_an = cursor.getString(1);
            int gia_mon_an = cursor.getInt(2);
            String dia_diem = cursor.getString(3);
            arrayRes.add(new NhaHang(id,ten_mon_an,gia_mon_an,dia_diem));
        }
        nhaHangAdapter.notifyDataSetChanged();
        cursor.close();
    }

    @Override
    protected void onRestart() {
        sqLiteDB = new SQLiteDB(this, "QuanLyNhaHang.db", null, 1);
        super.onRestart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getData();
    }

    @Override
    protected void onStop() {
        super.onStop();
        sqLiteDB.close();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_mon_an, menu);
        MenuItem mnuHelp = menu.findItem(R.id.mnuHelp);
        mnuHelp.setVisible(false);
        MenuItem menuSearch = menu.findItem(R.id.mnuSearch);
        SearchView searchView = (SearchView) menuSearch.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                processSearch(newText);
                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.mnuAbout) {
            Intent intent = new Intent(this, AboutActivity.class);
            startActivity(intent);
        } else if (item.getItemId() == R.id.mnuAdd) {
            Intent intent = new Intent(MainActivity.this,ThemMonAnActivity.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    private void processSearch(String text) {
        arrayRes.clear();
        Cursor cursor = sqLiteDB.query("NhaHang", "TenMonAn like ? or GiaMonAn like ? or DiaDiem like ?",new String[]{"%" + text + "%", "%" + text + "%", "%" + text + "%"});
        while (cursor.moveToNext()) {
            int id = cursor.getInt(0);
            String name = cursor.getString(1);
            int price = cursor.getInt(2);
            String address = cursor.getString(3);
            NhaHang nhaHang = new NhaHang(id, name , price, address);
            arrayRes.add(nhaHang);
        }
        cursor.close();
        nhaHangAdapter.notifyDataSetChanged();
    }

}
