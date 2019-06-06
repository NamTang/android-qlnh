package com.example.quanlynhahang;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
                    DangNhapActivity.sqLiteDB.QueryData(insertData);
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
}
