package com.example.quanlynhahang;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.Toast;

import com.example.adapter.TableAdapter;
import com.example.model.Table;
import com.example.service.SQLiteDB;

public class TableActivity extends AppCompatActivity {
    public static SQLiteDB sqLiteDB;
    GridView gvTable;
    TableAdapter tableAdapter;
    int index = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_table);
        sqLiteDB = new SQLiteDB(this, "QuanLyNhaHang.db", null, 1);

        initActionBar();
        addControls();
        addEvents();
    }

    private void addEvents() {
        gvTable.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                index = position;
            }
        });

        gvTable.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                index = position;

                return false;
            }
        });
    }

    private void addControls() {
        gvTable = findViewById(R.id.gvTable);
        gvTable.setLongClickable(true);
        registerForContextMenu(gvTable);
        tableAdapter = new TableAdapter(this, R.layout.table_item);
        gvTable.setAdapter(tableAdapter);
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
        menu.putExtra("from", "table");
        startActivity(menu);
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

    public void getData() {
        tableAdapter.clear();
        Cursor cursor = sqLiteDB.getData("SELECT * FROM Ban");
        while (cursor.moveToNext()) {
            int id = cursor.getInt(0);
            String name = cursor.getString(1);
            int status = cursor.getInt(2);
            tableAdapter.add(new Table(id, status, name));
        }
        cursor.close();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_mon_an, menu);
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
        Intent intent = null;
        if (item.getItemId() == R.id.mnuAbout) {
            intent = new Intent(this, AboutActivity.class);
        } else if (item.getItemId() == R.id.mnuAdd) {
            intent = new Intent(TableActivity.this, AddTableActivity.class);
        } else if (item.getItemId() == R.id.mnuHelp) {
            intent = new Intent(TableActivity.this, GuideActivity.class);
        }

        startActivity(intent);

        return super.onOptionsItemSelected(item);
    }

    private void processSearch(String text) {
        tableAdapter.clear();
        Cursor cursor = sqLiteDB.query("Ban", "name like ?", new String[]{"%" + text + "%"});
        while (cursor.moveToNext()) {
            int id = cursor.getInt(0);
            String name = cursor.getString(1);
            int status = cursor.getInt(2);
            tableAdapter.add(new Table(id, status, name));
        }
        cursor.close();
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        getMenuInflater().inflate(R.menu.menu_table, menu);
    }


    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.mnuAddDish:
                break;
            case R.id.mnuCheckout:
                break;
            case R.id.mnuUpdateTable:
                break;
            case R.id.mnuDeleteTable:
                deleteTable();
                break;
        }

        return super.onContextItemSelected(item);
    }

    private void deleteTable() {
        if (index >= 0) {
            try {
                sqLiteDB.getWritableDatabase().delete("Ban", "id = ?", new String[]{"" + tableAdapter.getItem(index).getId()});
                getData();
                Toast.makeText(this, "Xóa thành công", Toast.LENGTH_SHORT).show();
            } catch (Exception ex) {
                AlertDialog.Builder builder = new AlertDialog.Builder(TableActivity.this);

                builder.setMessage("Có lỗi xảy ra khi xóa, xin thử lại...")
                        .setTitle("Lỗi");

                AlertDialog dialog = builder.create();
                dialog.show();
            }
        }
    }
}
