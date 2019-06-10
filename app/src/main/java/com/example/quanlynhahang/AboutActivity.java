package com.example.quanlynhahang;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class AboutActivity extends AppCompatActivity {

    TextView txtPhone1, txtPhone2, txtPhone3, txtPhone4, txtPhone5, txtPhone6, txtPhone7;
    ImageView imgPhone1, imgPhone2, imgPhone3, imgPhone4, imgPhone5, imgPhone6, imgPhone7;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        addControls();
        addEvents();
    }

    private void addEvents() {
        imgPhone1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String num = txtPhone1.getText().toString();
                call(num);
            }
        });
        imgPhone2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String num = txtPhone2.getText().toString();
                call(num);
            }
        });
        imgPhone3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String num = txtPhone3.getText().toString();
                call(num);
            }
        });
        imgPhone4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String num = txtPhone4.getText().toString();
                call(num);
            }
        });
        imgPhone5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String num = txtPhone5.getText().toString();
                call(num);
            }
        });

        imgPhone6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String num = txtPhone6.getText().toString();
                call(num);
            }
        });
        imgPhone7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String num = txtPhone7.getText().toString();
                call(num);
            }
        });
    }

    private void call(String number) {

        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel: " + number));
        startActivity(callIntent);
    }

    private void addControls() {
        txtPhone1 = findViewById(R.id.txtPhone1);
        txtPhone2 = findViewById(R.id.txtPhone2);
        txtPhone3 = findViewById(R.id.txtPhone3);
        imgPhone1 = findViewById(R.id.imgPhone1);
        imgPhone2 = findViewById(R.id.imgPhone2);
        imgPhone3 = findViewById(R.id.imgPhone3);

        txtPhone4 = findViewById(R.id.txtPhone4);
        imgPhone4 = findViewById(R.id.imgPhone4);

        txtPhone5 = findViewById(R.id.txtPhone5);
        imgPhone5 = findViewById(R.id.imgPhone5);

        txtPhone6 = findViewById(R.id.txtPhone6);
        imgPhone6 = findViewById(R.id.imgPhone6);
        txtPhone7 = findViewById(R.id.txtPhone7);
        imgPhone7 = findViewById(R.id.imgPhone7);

    }
}
