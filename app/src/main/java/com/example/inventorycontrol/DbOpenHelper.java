package com.example.inventorycontrol;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DbOpenHelper extends SQLiteOpenHelper {

    //    データベースのバージョン
    private static final int DATABASE_VERSION = 1;

    //    データベースの情報
    private static final String DATABASE_NAME = "ICDB.db";
    private static final String TABLE_NAME_IC = "Inventorydb";
    private static final String TABLE_NAME_CR = "Recorddb";
    private static final String _ID = "_id";
    private static final String COLUMN_NAME_ITEM_ICDB = "item";
    private static final String COLUMN_NAME_COUNT_ICDB = "stockcount";
    private static final String COLUMN_NAME_DAY_CRDB = "day";
    private static final String COLUMN_NAME_ITEM_CRDB = "item";
    private static final String COLUMN_NAME_ACTION_CRDB = "actionRecord";
    private static final String COLUMN_NAME_COUNT_CRDB = "count";

    //    データベースの作成
    private static final String SQL_CREATE_INVENTORY_CONTROL =
            "CREATE TABLE " + TABLE_NAME_IC + " (" +
                    _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_NAME_ITEM_ICDB + " TEXT NOT NULL, " +
                    COLUMN_NAME_COUNT_ICDB + " INTEGER);";

    private static final String SQL_CREATE_CONTROL_RECORD =
            "CREATE TABLE " + TABLE_NAME_CR + " (" +
                    _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_NAME_DAY_CRDB + " TEXT NOT NULL, " +
                    COLUMN_NAME_ITEM_CRDB + " TEXT NOT NULL, " +
                    COLUMN_NAME_ACTION_CRDB + " TEXT NOT NULL, " +
                    COLUMN_NAME_COUNT_CRDB + " INTEGER);";

    //    テーブルの削除
    private static final String SQL_DELETE_ICDB =
            "DROP TABLE IF EXISTS " + TABLE_NAME_IC;

    private static final String SQL_DELETE_CRDB =
            "DROP TABLE IF EXISTS " + TABLE_NAME_CR;

    DbOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

//        テーブル作成
//        SQLiteファイルが無ければSQLiteファイルが作成される
        db.execSQL(SQL_CREATE_INVENTORY_CONTROL);
        db.execSQL(SQL_CREATE_CONTROL_RECORD);
        Log.d("debug", "onCreate(SQLiteDatabase db)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
//        アップデートの判別
        db.execSQL(SQL_DELETE_ICDB);
        db.execSQL(SQL_DELETE_CRDB);
        onCreate(db);
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

//    品物の新規登録
    public void addData(SQLiteDatabase db, String item, int stockcount) {
        ContentValues values = new ContentValues();

        values.put(COLUMN_NAME_ITEM_ICDB, item);
        values.put(COLUMN_NAME_COUNT_ICDB, stockcount);

        newItem.isfinish = db.insert(TABLE_NAME_IC, null, values);
    }

//    品物の増減
    public void updateData(SQLiteDatabase db, int count, String field) {
        ContentValues values = new ContentValues();

        values.put(COLUMN_NAME_COUNT_ICDB, count);

        changeCount.isSuccess = db.update(TABLE_NAME_IC, values, "item=?", new String[]{field});
    }

//    品物の削除
    public void deleteData(SQLiteDatabase db, String field) {
        deleteItem.isSuccess = db.delete(TABLE_NAME_IC, "item=?", new String[]{field});
    }

//    履歴への登録
    public void addRecord(SQLiteDatabase db, String day, String item, String action, int count) {
        ContentValues values = new ContentValues();
        int insert_count = 0;

        values.put(COLUMN_NAME_DAY_CRDB, day);
        values.put(COLUMN_NAME_ITEM_CRDB, item);
        values.put(COLUMN_NAME_ACTION_CRDB, action);

        switch (action) {
            case "新規":
            case "追加":
            case "減少":
                insert_count = count;
                values.put(COLUMN_NAME_COUNT_CRDB, insert_count);
                break;
            case "削除":
                break;
        }

        db.insert(TABLE_NAME_CR, null, values);
    }

}
