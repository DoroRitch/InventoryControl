package com.example.inventorycontrol;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

/**
 * @param
 * @itemName = 増減させる品名
 * @itemCount = 増減させる数量
 * @isSuccess = DB操作が成功したかどうか
 */

public class changeCount extends AppCompatActivity {

    private DbOpenHelper helper;
    private EditText editCount;
    private String itemName;
    private int itemCount;
    public static int isSuccess = -1;
    private getDate getDatehelper = new getDate();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.change_count);

        editCount = findViewById(R.id.editCount);
        Button plus = findViewById(R.id.plus);
        Button minus = findViewById(R.id.minus);
        Button back = findViewById(R.id.back);

//        データベースの取得
        helper = new DbOpenHelper(getApplicationContext());
        SQLiteDatabase db = helper.getReadableDatabase();
        String sql = "SELECT * FROM Inventorydb";
        Cursor cursor = db.rawQuery(sql, null);

//        Adapterの設定
        String[] from = {"item", "stockcount"};
        int[] to = {R.id.text, R.id.description};
        SimpleCursorAdapter adapter =
                new SimpleCursorAdapter(this, R.layout.spinner, cursor, from, to, 0);
        adapter.setDropDownViewResource(R.layout.spinner_dropdown);

//        SpinnerにAdapterの内容をセット
        Spinner spinner = findViewById(R.id.spinner);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new itemSelectListener());

        plus.setOnClickListener(new updateClickListener());
        minus.setOnClickListener(new updateClickListener());
        back.setOnClickListener(new pageBackClickListener());

    }

    class itemSelectListener implements AdapterView.OnItemSelectedListener {
        @Override
        public void onItemSelected(AdapterView parent, View view, int position, long id) {
            Spinner spinner = (Spinner)parent;
            Cursor cursor = (Cursor)spinner.getSelectedItem();

            itemName = cursor.getString(1);
            itemCount = Integer.parseInt(cursor.getString(2));

        }

        public void onNothingSelected(AdapterView parent) {}
    }

    /**
     * @param
     * @newCount = 増減する数量
     * @result = 増減させた後の結果の数量
     * @action = "追加" or "減少"
     */
    class updateClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {

            try {
                SQLiteDatabase db = helper.getWritableDatabase();
                int newCount = Integer.parseInt(editCount.getText().toString());
                int result = 0;
                String action = "";

//                押したボタンを判別して結果を算出
                switch(v.getId()) {
                    case R.id.plus:
                        result = newCount + itemCount;
                        action = "追加";
                        break;
                    case R.id.minus:
                        result = Math.max(itemCount - newCount, 0);
                        action = "減少";
                        break;
                }

//                データベースのアップデート
                helper.updateData(db, result, itemName);

                if (isSuccess != -1) {
                    Toast.makeText(getBaseContext(), "update成功", Toast.LENGTH_SHORT).show();
                    isSuccess = -1;
                } else {
                    Toast.makeText(getBaseContext(), "update失敗", Toast.LENGTH_SHORT).show();
                }

//                履歴に操作を登録
                helper.addRecord(db, getDatehelper.getNowDate(), itemName, action, newCount);

            } catch (NumberFormatException er) {
                Toast.makeText(getBaseContext(), "入力値が不正です。", Toast.LENGTH_SHORT).show();
            } catch (NullPointerException e) {
                Toast.makeText(getBaseContext(), "「数量」を指定してください。", Toast.LENGTH_SHORT).show();
            }

            editCount.setText("");
        }
    }

    class pageBackClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) { finish(); }
    }
}
