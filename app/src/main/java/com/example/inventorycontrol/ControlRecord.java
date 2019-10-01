package com.example.inventorycontrol;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class ControlRecord extends AppCompatActivity {

    TextView text_view;
    DbOpenHelper helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.record);

        helper = new DbOpenHelper(getApplicationContext());

        text_view = findViewById(R.id.text_View);
        Button show = findViewById(R.id.show);
        Button back = findViewById(R.id.back);

        show.setOnClickListener(new showDatabase());
        back.setOnClickListener(new pageBackClickListener());

    }

//    DBから履歴を全件取得し画面に表示する
    class showDatabase implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            SQLiteDatabase db = helper.getReadableDatabase();
            Cursor cursor = db.query(
                    "Recorddb",
                    new String[] { "day", "item", "actionRecord", "count"},
                    null,
                    null,
                    null,
                    null,
                    null
            );

            StringBuilder sbuilder = new StringBuilder();

            String count = "";
            while(cursor.moveToNext()) {
                sbuilder.append(cursor.getString(0));
                sbuilder.append("：\n");
                sbuilder.append(cursor.getString(1));
                sbuilder.append("：");

                switch(cursor.getString(2)){
                    case "新規":
                        count = String.format("%5d", cursor.getInt(3)) + "個\n";
                        break;
                    case "追加":
                        count = "+" + String.format("%5d", cursor.getInt(3)) + "個\n";
                        break;
                    case "減少":
                        count = "-" + String.format("%5d", cursor.getInt(3)) + "個\n";
                        break;
                    case "削除":
                        count = "――\n";
                        break;
                }

                sbuilder.append(cursor.getString(2));
                sbuilder.append("：");
                sbuilder.append(count);
                sbuilder.append("\n");
            }

            cursor.close();
            if (sbuilder.toString().isEmpty()) {
                text_view.setText("操作履歴がありません。");
            } else {
                text_view.setText(sbuilder.toString());
            }
        }
    }

    class pageBackClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) { finish(); }
    }
}
