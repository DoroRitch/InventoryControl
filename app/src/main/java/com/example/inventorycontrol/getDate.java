package com.example.inventorycontrol;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class getDate {

//    履歴に登録するための現在日時を取得するメソッド
    public String getNowDate() {
        Calendar cal = Calendar.getInstance();
        final DateFormat df = new SimpleDateFormat("yyyy/MM/dd E");
        return df.format(cal.getTime());
    }
}
