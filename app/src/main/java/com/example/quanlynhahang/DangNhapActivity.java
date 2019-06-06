package com.example.quanlynhahang;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.service.SQLiteDB;

public class DangNhapActivity extends AppCompatActivity {

    public static SQLiteDB sqLiteDB;
    EditText edtUser, edtPassword;
    Button btnLogin;
    TextView txtError;
//    String userName, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dang_nhap);
        //Create database
        sqLiteDB = new SQLiteDB(this, "QuanLyNhaHang.db", null, 1);
        //Create table -- TenMonAn --- GiaMonAn -- DiaDiem
        String create_table = "CREATE TABLE IF NOT EXISTS NhaHang(Id INTEGER PRIMARY KEY AUTOINCREMENT , TenMonAn VARCHAR , GiaMonAn INTEGER , DiaDiem VARCHAR )";
        String create_table2 = "CREATE TABLE IF NOT EXISTS User(Id INTEGER PRIMARY KEY AUTOINCREMENT , UserName VARCHAR , Password VARCHAR );";
        sqLiteDB.QueryData(create_table);
        sqLiteDB.QueryData(create_table2);
        String addUserIfNotExistQuery = "INSERT OR IGNORE INTO User (UserName, Password) VALUES ('Admin', 'Admin')";
        sqLiteDB.QueryData(addUserIfNotExistQuery);

        //Add value
//        String insert = "INSERT INTO NhaHang VALUES(null, 'Com suon', 25000, 'Quan 10')";
//        String insert_1 = "INSERT INTO NhaHang VALUES(null, 'Hu tieu', 20000, 'Quan 1')";
//        String insert_2 = "INSERT INTO NhaHang VALUES(null, 'Banh canh', 20000, 'Quan 2')";
//        String insert_3 = "INSERT INTO NhaHang VALUES(null, 'Pha lau', 15000, 'Quan 3')";
//        String insert_4 = "INSERT INTO User VALUES(null, 'admin', 'admin');";
//        sqLiteDB.QueryData(insert_4);
//        sqLiteDB.QueryData(insert_1);
//        sqLiteDB.QueryData(insert_2);
//        sqLiteDB.QueryData(insert_3);
//        sqLiteDB.QueryData(insert);
        addControls();
        addEvents();
//        Cursor cursor = sqLiteDB.GetData("SELECT * FROM User");
//        while (cursor.moveToNext()) {
//            userName = cursor.getString(1);
//            password = cursor.getString(2);
//
//        }
    }

    private void addEvents() {
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (login()) {
                    txtError.setText("");
                    Intent intent = new Intent(DangNhapActivity.this, MainActivity.class);
                    startActivity(intent);
                    Toast.makeText(DangNhapActivity.this, "Login Success", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(DangNhapActivity.this, "Login Failed", Toast.LENGTH_SHORT).show();
                    txtError.setText("Incorrect user name or password.");
                }
            }
        });

        edtUser.addTextChangedListener(textWatcher);
        edtPassword.addTextChangedListener(textWatcher);
    }

    private TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            checkLogin();
        }
    };

    private void checkLogin() {
        String userName = edtUser.getText().toString();
        String password = edtPassword.getText().toString();
        if (("").equals(userName) || ("").equals(password)) {
            btnLogin.setEnabled(false);
            btnLogin.setBackgroundColor(Color.GRAY);
        } else if (!("").equals(userName) && !("").equals(password)) {
            btnLogin.setEnabled(true);
            btnLogin.setBackgroundColor(Color.rgb(34, 153, 222));
        }
    }

    private boolean login() {
        String userName = edtUser.getText().toString();
        String password = edtPassword.getText().toString();
        String query = "select * from user where username = '" + userName + "' and password = '" + password + "'";
        Cursor cursor = sqLiteDB.GetData(query);

        boolean result = cursor.moveToNext();
        cursor.close();

        return result;
    }

    private void addControls() {
        edtUser = findViewById(R.id.edtUserName);
        edtPassword = findViewById(R.id.edtPassword);
        btnLogin = findViewById(R.id.btnLogin);
        btnLogin.setBackgroundColor(Color.GRAY);
        txtError = findViewById(R.id.txtError);
    }

//    public void exitAction(View view) {
//        finish();
//    }


    @Override
    protected void onStop() {
        super.onStop();
        sqLiteDB.close();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        sqLiteDB = new SQLiteDB(this, "QuanLyNhaHang.db", null, 1);
    }
}
