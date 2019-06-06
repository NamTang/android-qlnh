package com.example.quanlynhahang;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.model.NhaHang;

public class DangNhapActivity extends AppCompatActivity {

    EditText edtUser, edtPassword;
    Button btnLogin;
    static SQLiteDB sqLiteDB;
    String userName, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dang_nhap);
        //Create database
        sqLiteDB = new SQLiteDB(this,"QuanLyNhaHang.sql",null,1);
        //Create table -- TenMonAn --- GiaMonAn -- DiaDiem
        String create_table = "CREATE TABLE IF NOT EXISTS NhaHang(Id INTEGER PRIMARY KEY AUTOINCREMENT , TenMonAn VARCHAR , GiaMonAn INTEGER , DiaDiem VARCHAR )";
        String create_table2 = "CREATE TABLE IF NOT EXISTS User(Id INTEGER PRIMARY KEY AUTOINCREMENT , UserName VARCHAR , Password VARCHAR );";
        sqLiteDB.QueryData(create_table);
        sqLiteDB.QueryData(create_table2);

        //Add value
        String insert = "INSERT INTO NhaHang VALUES(null, 'Com suon', 25000, 'Quan 10')";
        String insert_1 = "INSERT INTO NhaHang VALUES(null, 'Hu tieu', 20000, 'Quan 1')";
        String insert_2 = "INSERT INTO NhaHang VALUES(null, 'Banh canh', 20000, 'Quan 2')";
        String insert_3 = "INSERT INTO NhaHang VALUES(null, 'Pha lau', 15000, 'Quan 3')";
        String insert_4 = "INSERT INTO User VALUES(null, 'admin', 'admin');";
//        sqLiteDB.QueryData(insert_4);
//        sqLiteDB.QueryData(insert_1);
//        sqLiteDB.QueryData(insert_2);
//        sqLiteDB.QueryData(insert_3);
//        sqLiteDB.QueryData(insert);
        addControls();
        addEvents();
        Cursor cursor = sqLiteDB.GetData("SELECT * FROM User");
        while (cursor.moveToNext()) {
            userName = cursor.getString(1);
            password = cursor.getString(2);

        }
    }

    private void addEvents() {
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edtUser.getText().toString().equals(userName) && edtPassword.getText().toString().equals(password)){
                    Intent intent = new Intent(DangNhapActivity.this, MainActivity.class);
                    startActivity(intent);
                    Toast.makeText(DangNhapActivity.this, "Login Success", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(DangNhapActivity.this, "Login Failed", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void addControls() {
        edtUser = findViewById(R.id.edtUser);
        edtPassword = findViewById(R.id.edtPassword);
        btnLogin = findViewById(R.id.btnLogin);
    }

    public void exitAction(View view) {
        finish();
    }
}
