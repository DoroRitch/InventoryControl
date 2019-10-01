package com.example.inventorycontrol;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private Intent intent = new Intent();
    private TextView text_view;
    private DbOpenHelper helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        helper = new DbOpenHelper(getApplicationContext());

        text_view = findViewById(R.id.text_View);
        Button show = findViewById(R.id.show);
        Button newItem = findViewById(R.id.newItem);
        Button changeCount = findViewById(R.id.changeCount);
        Button delete = findViewById(R.id.deleteItem);
        Button record = findViewById(R.id.record);

        show.setOnClickListener(new showDatabase());
        newItem.setOnClickListener(new pageSendClickListener());
        changeCount.setOnClickListener(new pageSendClickListener());
        delete.setOnClickListener(new pageSendClickListener());
        record.setOnClickListener(new pageSendClickListener());

    }

    /**
     * DBからデータを全件取得し画面に表示する
     */
    class showDatabase implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            SQLiteDatabase db = helper.getReadableDatabase();
            Cursor cursor = db.query(
                    "Inventorydb",
                    new String[] { "item", "stockcount"},
                    null,
                    null,
                    null,
                    null,
                    null
            );

            StringBuilder sbuilder = new StringBuilder();

            while(cursor.moveToNext()) {
                sbuilder.append(cursor.getString(0));
                sbuilder.append(": ");
                sbuilder.append(String.format("%6d", cursor.getInt(1)));
                sbuilder.append("個\n");
            }

            cursor.close();
            if (sbuilder.toString().isEmpty()) {
                text_view.setText("「品物追加」から品名と個数を指定して登録してください。");
            } else {
                text_view.setText(sbuilder.toString());
            }
        }
    }

    class pageSendClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            switch(v.getId()) {
                case R.id.newItem:
                    intent = new Intent(MainActivity.this, newItem.class);
                    break;
                case R.id.changeCount:
                    intent = new Intent(MainActivity.this, changeCount.class);
                    break;
                case R.id.deleteItem:
                    intent = new Intent(MainActivity.this, deleteItem.class);
                    break;
                case R.id.record:
                    intent = new Intent(MainActivity.this, ControlRecord.class);
                    break;
            }
            startActivity(intent);
        }
    }


}
