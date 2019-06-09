package com.example.quanlynhahang;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

public class MenuActivity extends AppCompatActivity {
    LinearLayout llFood, llTable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        addControls();
        addEvents();
        Intent intent = getIntent();
        String from = intent.getStringExtra("from");
        if (("main").equals(from)) {
            llFood.setBackgroundColor(Color.GRAY);
        } else if (("table").equals(from)) {
            llTable.setBackgroundColor(Color.GRAY);
        }

    }

    private void addEvents() {
        llFood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goMain();
            }
        });

        llTable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goTable();
            }
        });
    }

    private void goTable() {
        Intent myIntent = new Intent(this, TableActivity.class);
        startActivity(myIntent);
    }

    private void goMain() {
        Intent myIntent = new Intent(this, MainActivity.class);
        startActivity(myIntent);
    }

    private void addControls() {
        llFood = findViewById(R.id.llFood);
        llTable = findViewById(R.id.llTable);
    }

    public void back(View view) {
        finish();
    }
}
