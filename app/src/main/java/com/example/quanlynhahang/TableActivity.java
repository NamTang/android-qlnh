package com.example.quanlynhahang;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.Toast;

import com.example.adapter.TableAdapter;
import com.example.model.NhaHang;
import com.example.model.Table;
import com.example.service.SQLiteDB;

import java.util.ArrayList;
import java.util.List;

public class TableActivity extends AppCompatActivity {
    public static SQLiteDB sqLiteDB;
    GridView gvTable;
    TableAdapter tableAdapter;
    int index = -1;
    List selectedItems;
    List<NhaHang> dishes = new ArrayList<NhaHang>();

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
                showDialogAddDishToTable();
                break;
            case R.id.mnuCheckout:
                break;
            case R.id.mnuUpdateTable:
                processUpdateTable();
                break;
            case R.id.mnuDeleteTable:
                deleteTable();
                break;
        }

        return super.onContextItemSelected(item);
    }

    private void showDialogAddDishToTable() {
        selectedItems = new ArrayList();  // Where we track the selected items
        AlertDialog.Builder builder = new AlertDialog.Builder(TableActivity.this);
        // Set the dialog title
        // Specify the list array, the items to be selected by default (null for none),
        // and the listener through which to receive callbacks when items are selected
        builder.setMultiChoiceItems(getListDish(), null,
                new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which,
                                        boolean isChecked) {
                        if (isChecked) {
                            // If the user checked the item, add it to the selected items
                            selectedItems.add(which);
                        } else if (selectedItems.contains(which)) {
                            // Else, if the item is already in the array, remove it
                            selectedItems.remove(Integer.valueOf(which));
                        }
                    }
                })
                // Set the action buttons
                .setPositiveButton(R.string.save, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        // User clicked OK, so save the selectedItems results somewhere
                        // or return them to the component that opened the dialog
                        Toast.makeText(TableActivity.this, "" + selectedItems.size(), Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        builder.create().show();
    }

    private String[] getListDish() {
        Cursor cursor = sqLiteDB.getData("SELECT * from NhaHang");
        String[] result = new String[cursor.getCount()];
        int i = 0;
        dishes.clear();
        while (cursor.moveToNext()) {
            int id = cursor.getInt(0);
            String ten_mon_an = cursor.getString(1);
            int gia_mon_an = cursor.getInt(2);
            String dia_diem = cursor.getString(3);
            dishes.add(new NhaHang(id, ten_mon_an, gia_mon_an, dia_diem));
            result[i++] = ten_mon_an;
        }
        cursor.close();

        return result;
    }

    private void processUpdateTable() {
        AlertDialog.Builder builder = new AlertDialog.Builder(TableActivity.this);
        builder.setTitle(R.string.update)
                .setItems(R.array.table_action_array, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // The 'which' argument contains the index position
                        // of the selected item
                        if (which == 0) {
                            showDialogUpdateTableName();
                        } else if (which == 1) {
                            showDialogUpdateTableStatus();
                        }
                    }
                });
        builder.create().show();
    }

    private void showDialogUpdateTableStatus() {
        AlertDialog.Builder builder = new AlertDialog.Builder(TableActivity.this);
        builder.setItems(tableAdapter.getItem(index).getStatus() == Table.BUSY ? R.array.table_status_free : R.array.table_status_busy, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // The 'which' argument contains the index position
                // of the selected item
                updateTableStatus(tableAdapter.getItem(index).getStatus() == Table.BUSY ? Table.FREE : Table.BUSY);
            }
        });
        builder.create().show();
    }

    private void updateTableStatus(int which) {
        if (index >= 0) {
            try {
                ContentValues contentValues = new ContentValues();
                contentValues.put("tstatus", which);
                sqLiteDB.getWritableDatabase().update("Ban", contentValues, "id = ?", new String[]{"" + tableAdapter.getItem(index).getId()});
                getData();
                Toast.makeText(this, "Cập nhật thành công", Toast.LENGTH_SHORT).show();
            } catch (Exception ex) {
                AlertDialog.Builder builder = new AlertDialog.Builder(TableActivity.this);

                builder.setMessage("Có lỗi xảy ra khi cập nhật trạng thái, xin thử lại...")
                        .setTitle("Lỗi");

                builder.create().show();
            }
        }
    }

    private void showDialogUpdateTableName() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(TableActivity.this);
        // Get the layout inflater
        LayoutInflater inflater = getLayoutInflater();
        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        View dialogView = inflater.inflate(R.layout.dialog_update_table_name, null);
        final EditText edtTableName = dialogView.findViewById(R.id.edtTableName);
        edtTableName.setText(tableAdapter.getItem(index).getName());

        builder.setView(dialogView)
                // Add action buttons
                .setPositiveButton(R.string.save, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        // sign in the user ...
                        updateTableName(edtTableName.getText().toString());
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        builder.create().show();
    }

    private void updateTableName(String name) {
        if (index >= 0) {
            try {
                ContentValues contentValues = new ContentValues();
                contentValues.put("name", name);
                sqLiteDB.getWritableDatabase().update("Ban", contentValues, "id = ?", new String[]{"" + tableAdapter.getItem(index).getId()});
                getData();
                Toast.makeText(this, "Cập nhật thành công", Toast.LENGTH_SHORT).show();
            } catch (Exception ex) {
                AlertDialog.Builder builder = new AlertDialog.Builder(TableActivity.this);

                builder.setMessage("Có lỗi xảy ra khi cập nhật tên, xin thử lại...")
                        .setTitle("Lỗi");

                builder.create().show();
            }
        }
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

                builder.create().show();
            }
        }
    }
}
