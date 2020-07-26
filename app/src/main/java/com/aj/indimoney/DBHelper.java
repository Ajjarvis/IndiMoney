package com.aj.indimoney;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "db_indiMoney";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.w("Version", "Current Version is " + db.getVersion());
        String queryBuatTable = "CREATE TABLE IF NOT EXISTS tb_indiMoney (id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "utilities text," +
                "money int," +
                "type text," +
                "date text )";
        String queryBuatTableWallet = "CREATE TABLE IF NOT EXISTS tb_user (id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "name text," +
                "email text," +
                "income int," +
                "expense int," +
                "money int )";
        db.execSQL(queryBuatTable);
        db.execSQL(queryBuatTableWallet);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w("Update DB", "DB IS UPDATE TO " + db.getVersion());
        db.execSQL("DROP TABLE IF EXISTS tb_indiMoney");
        db.execSQL("DROP TABLE IF EXISTS tb_user");
        onCreate(db);
    }
}
