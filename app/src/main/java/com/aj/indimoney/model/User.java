package com.aj.indimoney.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.aj.indimoney.DBHelper;

import org.json.JSONException;
import org.json.JSONObject;

public class User {
    public String name;
    public String email;
    public Integer money;
    public Integer income;
    public Integer expense;
    private DBHelper dbHelper;

    public User() {

    }

    public User(String name, String email, Integer money, Integer expense, Integer income) {
        this.name = name;
        this.email = email;
        this.money = money;
        this.income = income;
        this.expense = expense;
    }

    public User(Context context) {
        dbHelper = new DBHelper(context);
    }

    public int insertUser(User data) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", data.name);
        values.put("email", data.email);
        values.put("money", data.money);
        values.put("income", data.income);
        values.put("expense", data.expense);
        long user_id = db.insert("tb_user", null, values);
        db.close();
        return (int) user_id;
    }

    public int updateBalance(Integer balance) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues cValues = new ContentValues();
        cValues.put("money", balance);
        String selection = "id=?";
        String[] selectionArg = {String.valueOf(1)};
        long id = db.update("tb_user", cValues, selection, selectionArg);
        return (int) id;
    }

    public int updateBalance2(Integer balance2) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues cValues = new ContentValues();
        cValues.put("income", balance2);
        String selection = "id=?";
        String[] selectionArg = {String.valueOf(1)};
        long id = db.update("tb_user", cValues, selection, selectionArg);
        return (int) id;
    }

    public int updateBalance3(Integer balance3) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues cValues = new ContentValues();
        cValues.put("expense", balance3);
        String selection = "id=?";
        String[] selectionArg = {String.valueOf(1)};
        long id = db.update("tb_user", cValues, selection, selectionArg);
        return (int) id;
    }

    public JSONObject getUser() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("Select * from tb_user where id=1", null);
        JSONObject usernya = new JSONObject();
        if (cursor.moveToFirst()) {
            try {
                usernya.put("name", cursor.getString(cursor.getColumnIndex("name")));
                usernya.put("email", cursor.getString(cursor.getColumnIndex("email")));
                usernya.put("money", cursor.getString(cursor.getColumnIndex("money")));
                usernya.put("income", cursor.getString(cursor.getColumnIndex("income")));
                usernya.put("expense", cursor.getString(cursor.getColumnIndex("expense")));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        cursor.close();
        db.close();
        return usernya;
    }
}
