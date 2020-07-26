package com.aj.indimoney.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.aj.indimoney.DBHelper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class Transaction {
    public String utilities;
    public String date;
    public Integer money;
    private DBHelper dbHelper;
    public String type;
    Context contextApp;

    public  Transaction() {

    }

    public Transaction (String utilities, Integer money, String date, String type) {
        this.utilities = utilities;
        this.date = date;
        this.type = type;
        this.money = money;
    }

    public Transaction(Context context) {
        dbHelper = new DBHelper(context);
        contextApp = context;
    }

    public int insertTable(Transaction data) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String strDate = sdf.format(new Date());

        values.put("money", data.money);
        values.put("utilities", data.utilities);
        values.put("type", data.type);
        values.put("date", strDate);

        long transaction_id = db.insert("tb_indiMoney",null,values);
        db.close();
        return (int) transaction_id;
    }

    public ArrayList<HashMap<String, String>> getList() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String querySelect = "SELECT * From tb_indiMoney order by id desc";
        ArrayList<HashMap<String, String>> trxList = new ArrayList<HashMap<String, String>>();
        Cursor cursor = db.rawQuery(querySelect, null);
        if (cursor.moveToFirst()) {
            do {
                HashMap<String, String> trx = new HashMap<String, String>();
                trx.put("id", cursor.getString(cursor.getColumnIndex("id")));
                trx.put("utilities", cursor.getString(cursor.getColumnIndex("utilities")));
                trx.put("money", cursor.getString(cursor.getColumnIndex("money")));
                trx.put("type", cursor.getString(cursor.getColumnIndex("type")));
                trx.put("date", cursor.getString(cursor.getColumnIndex("date")));
                trxList.add(trx);
            }
            while(cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return trxList;
    }

    public ArrayList<HashMap<String, String>> getList2() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String querySelect = "SELECT * From tb_indiMoney where type LIKE '%Enter%'";
        ArrayList<HashMap<String, String>> trxList = new ArrayList<HashMap<String, String>>();
        Cursor cursor = db.rawQuery(querySelect, null);
        if (cursor.moveToFirst()) {
            do {
                HashMap<String, String> trx = new HashMap<String, String>();
                trx.put("id", cursor.getString(cursor.getColumnIndex("id")));
                trx.put("utilities", cursor.getString(cursor.getColumnIndex("utilities")));
                trx.put("money", cursor.getString(cursor.getColumnIndex("money")));
                trx.put("type", cursor.getString(cursor.getColumnIndex("type")));
                trx.put("date", cursor.getString(cursor.getColumnIndex("date")));
                trxList.add(trx);
            }
            while(cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return trxList;
    }

    public ArrayList<HashMap<String, String>> getList3() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String querySelect = "SELECT * From tb_indiMoney where type LIKE '%Exit%'";
        ArrayList<HashMap<String, String>> trxList = new ArrayList<HashMap<String, String>>();
        Cursor cursor = db.rawQuery(querySelect, null);
        if (cursor.moveToFirst()) {
            do {
                HashMap<String, String> trx = new HashMap<String, String>();
                trx.put("id", cursor.getString(cursor.getColumnIndex("id")));
                trx.put("utilities", cursor.getString(cursor.getColumnIndex("utilities")));
                trx.put("money", cursor.getString(cursor.getColumnIndex("money")));
                trx.put("type", cursor.getString(cursor.getColumnIndex("type")));
                trx.put("date", cursor.getString(cursor.getColumnIndex("date")));
                trxList.add(trx);
            }
            while(cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return trxList;
    }
}
