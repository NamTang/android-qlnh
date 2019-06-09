package com.example.quanlynhahang;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class ThemMonAnActivity extends AppCompatActivity {

    EditText edName, edPrice, edAddress;
    Button btnAdd;
    TextView txtAddTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_them_mon_an);
        initActionBar();
        addControls();
        addEvents();
    }

    private void addEvents() {
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = edName.getText().toString();
                String price = edPrice.getText().toString();
                String address = edAddress.getText().toString();

                if (!name.equals("") && !price.equals("") && !address.equals("")) {
                    String insertData = "INSERT INTO NhaHang VALUES (null,'" + name + "','" + price + "','" + address + "')";
                    DangNhapActivity.sqLiteDB.queryData(insertData);
                } else {
                    Toast.makeText(ThemMonAnActivity.this, "Vui long nhap day du thong tin cua mon an!", Toast.LENGTH_SHORT).show();
                }
                finish();
            }
        });
    }

    private void addControls() {
        edName = findViewById(R.id.edName);
        edPrice = findViewById(R.id.edPrice);
        edAddress = findViewById(R.id.edAddress);
        btnAdd = findViewById(R.id.btnAdd);
        txtAddTitle = findViewById(R.id.txtTitleAdd);
    }

    private void initActionBar() {
        this.getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setCustomView(R.layout.custom_action_bar);
        View view = getSupportActionBar().getCustomView();
        ImageView imgBack = view.findViewById(R.id.imgBack);
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // menu button
        ImageView imgMenu = view.findViewById(R.id.imgMenu);
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
}
