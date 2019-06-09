package com.example.quanlynhahang;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.model.Table;
import com.example.service.SQLiteDB;

public class AddTableActivity extends AppCompatActivity {
    public static SQLiteDB sqLiteDB;
    TextView txtAutoGenerate, txtManual;
    LinearLayout llAddTable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_table);
        sqLiteDB = new SQLiteDB(this, "QuanLyNhaHang.db", null, 1);

        initActionBar();
        addControls();
        addEvents();
    }

    private void addEvents() {
        txtManual.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                renderSaveLayout(true);
            }
        });

        txtAutoGenerate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                renderSaveLayout(false);
            }
        });
    }

    private void renderSaveLayout(final boolean isManual) {
        llAddTable.removeAllViews();
        llAddTable.setPadding(0, 0, 0, 0);
        // input ll
        LinearLayout linearLayout = new LinearLayout(this);
        linearLayout.setOrientation(LinearLayout.HORIZONTAL);
        TextView textView = new TextView(this);
        textView.setText(isManual ? R.string.table_name : R.string.quantity);
        final EditText editText = new EditText(this);
        editText.setWidth(llAddTable.getWidth());
        if (!isManual) {
            editText.setInputType(InputType.TYPE_CLASS_NUMBER);
            editText.setHint("Số cần nhập phải lớn hơn 0");
        }
        editText.setText("");

        // buttons ll
        LinearLayout llButton = new LinearLayout(this);
        llButton.setOrientation(LinearLayout.HORIZONTAL);
        llButton.setGravity(View.TEXT_ALIGNMENT_CENTER);
        final Button btnSave = new Button(this);
        btnSave.setEnabled(false);
        btnSave.setText(R.string.save);
        Button btnRefresh = new Button(this);
        btnRefresh.setText(R.string.back);

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String text = editText.getText().toString();
                if (!text.isEmpty()) {
                    if (!isManual) {
                        try {
                            int quantity = Integer.parseInt(editText.getText().toString());
                            if (quantity <= 0) {
                                btnSave.setEnabled(false);
                            } else {
                                btnSave.setEnabled(true);
                            }
                        } catch (NumberFormatException ex) {
                            btnSave.setEnabled(false);
                            AlertDialog.Builder builder = new AlertDialog.Builder(AddTableActivity.this);

                            builder.setMessage("Số quá lớn")
                                    .setTitle("Lỗi");

                            AlertDialog dialog = builder.create();
                            dialog.show();
                        }
                    } else if (Integer.parseInt(editText.getText().toString()) <= 0) {
                        btnSave.setEnabled(false);
                    } else {
                        btnSave.setEnabled(true);
                    }
                } else {
                    btnSave.setEnabled(false);
                }
            }
        });
        btnRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                refreshActivity();
            }
        });
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isManual) {
                    String name = editText.getText().toString();
                    if (saveTable(name)) {
                        Toast.makeText(AddTableActivity.this, "Thành công", Toast.LENGTH_SHORT).show();
                        editText.setText("");
                    } else {
                        Toast.makeText(AddTableActivity.this, "Thất bại", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    int quantity = Integer.parseInt(editText.getText().toString());
                    if (autoGenerateTable(quantity)) {
                        Toast.makeText(AddTableActivity.this, "Tạo thành công " + quantity + " bàn", Toast.LENGTH_SHORT).show();
                        editText.setText("");
                    } else {
                        Toast.makeText(AddTableActivity.this, "Thất bại", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        linearLayout.addView(textView);
        linearLayout.addView(editText);
        llButton.addView(btnSave);
        llButton.addView(btnRefresh);
        llAddTable.addView(linearLayout);
        llAddTable.addView(llButton);
    }

    private boolean autoGenerateTable(int quantity) {
        boolean result = false;
        if (quantity > 0) {
            Cursor cursor = sqLiteDB.getData("SELECT * FROM Ban");
            int index = cursor.getCount() + 1;
            cursor.close();
            for (int i = 0; i < quantity; i++) {
                ContentValues contentValues = new ContentValues();
                contentValues.put("name", "Ban " + index++);
                contentValues.put("tstatus", Table.FREE);
                try {
                    sqLiteDB.getWritableDatabase().insert("Ban", null, contentValues);
                } catch (Exception ex) {
                    return result;
                }
            }
            result = true;
        }

        return result;
    }

    private boolean saveTable(String name) {
        boolean result = false;
        if (!name.isEmpty()) {
//            String insertData = "INSERT INTO Ban (name, tstatus) VALUES ('" + name + "', " + Table.FREE + ")";
            ContentValues contentValues = new ContentValues();
            contentValues.put("name", name);
            contentValues.put("tstatus", Table.FREE);
            try {
                sqLiteDB.getWritableDatabase().insert("Ban", null, contentValues);
                result = true;
            } catch (Exception ex) {
                return result;
            }
        }

        return result;
    }

    private void refreshActivity() {
        finish();
        startActivity(getIntent());
    }

    private void addControls() {
        txtAutoGenerate = findViewById(R.id.txtAutoGenerateTable);
        txtManual = findViewById(R.id.txtManual);
        llAddTable = findViewById(R.id.llAddTable);
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
        menu.putExtra("from", "table");
        startActivity(menu);
    }

    @Override
    protected void onRestart() {
        sqLiteDB = new SQLiteDB(this, "QuanLyNhaHang.db", null, 1);
        super.onRestart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        sqLiteDB.close();
    }
}
