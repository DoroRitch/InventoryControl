package com.example.inventorycontrol;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;


public class deleteItem extends AppCompatActivity {

    private Intent intent = new Intent();
    private DbOpenHelper helper;
    private  String itemName;
    public static int isSuccess = -1;
    private getDate getDatehelper = new getDate();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.delete_item);

        Button delete = findViewById(R.id.delete);
        Button back = findViewById(R.id.back);

//        データベースの取得
        helper = new DbOpenHelper(getApplicationContext());
        SQLiteDatabase db = helper.getReadableDatabase();
        String sql = "SELECT * FROM Inventorydb";
        Cursor cursor = db.rawQuery(sql, null);

//        Adapterの設定
        String[] from = {"item","stockcount"};
        int[] to = {R.id.text,R.id.description};
        SimpleCursorAdapter adapter =
                new SimpleCursorAdapter(this,R.layout.spinner,cursor,from,to, 0);
        adapter.setDropDownViewResource(R.layout.spinner_dropdown);

//        SpinnerにAdapterの内容をセット
        Spinner spinner = findViewById(R.id.spinner2);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new itemSelectListener());

        delete.setOnClickListener(new itemDeleteClickListener());
        back.setOnClickListener(new pageBackClickListener());
    }

    class itemSelectListener implements AdapterView.OnItemSelectedListener {
        @Override
        public void onItemSelected(AdapterView parent, View view, int position, long id) {
            Spinner spinner = (Spinner)parent;
            Cursor cursor = (Cursor)spinner.getSelectedItem();
            itemName = cursor.getString(1);
        }
        public void onNothingSelected(AdapterView parent) {}
    }

    class itemDeleteClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            SQLiteDatabase db = helper.getWritableDatabase();

            helper.deleteData(db, itemName);

            if(isSuccess != -1) {
                Toast.makeText(getBaseContext(), "delete成功", Toast.LENGTH_SHORT).show();
                isSuccess = -1;
            } else {
                Toast.makeText(getBaseContext(), "delete失敗", Toast.LENGTH_SHORT).show();
            }

            helper.addRecord(db, getDatehelper.getNowDate(), itemName, "削除", 0);
        }

    }

    class pageBackClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) { finish(); }
    }

}
