package com.example.inventorycontrol;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class newItem extends AppCompatActivity {

    private Intent intent = new Intent();
    EditText editItem, editCount;
    private DbOpenHelper helper;
    public static long isfinish = -1;
    private getDate getDatehelper = new getDate();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_item);

        editItem = findViewById(R.id.editItemName);
        editCount = findViewById(R.id.editCount);
        Button addItem = findViewById(R.id.addItem);
        Button back = findViewById(R.id.back);

        addItem.setOnClickListener(new addItemClickListener());
        back.setOnClickListener(new pageBackClickListener());
    }

    class addItemClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {

            try {
                helper = new DbOpenHelper(getApplicationContext());
                SQLiteDatabase db = helper.getWritableDatabase();
                String item = editItem.getText().toString();
                String count = editCount.getText().toString();

                helper.addData(db, item, Integer.parseInt(count));

                if(newItem.isfinish == -1) {
                    Toast.makeText(getBaseContext(), "Insert失敗", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getBaseContext(), "Insert成功", Toast.LENGTH_LONG).show();
                }

                helper.addRecord(db, getDatehelper.getNowDate(), item, "新規", Integer.parseInt(count));

            } catch (NullPointerException e) {
                Toast.makeText(getBaseContext(), "「品名」か「数量」は必須項目です。", Toast.LENGTH_SHORT).show();
            } catch (NumberFormatException er) {
                Toast.makeText(getBaseContext(), "入力値が不正です。", Toast.LENGTH_SHORT).show();
            }

            editItem.setText("");
            editCount.setText("");
        }

    }

    class pageBackClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) { finish(); }
    }

}
