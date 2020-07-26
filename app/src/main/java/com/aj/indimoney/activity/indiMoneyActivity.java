package com.aj.indimoney.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.aj.indimoney.DBHelper;
import com.aj.indimoney.R;
import com.aj.indimoney.adapter.TransactionAdapter;
import com.aj.indimoney.model.Transaction;
import com.aj.indimoney.model.User;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class indiMoneyActivity extends AppCompatActivity {
    String[] list;
    String[] list2;
    String[] list3;
    ListView ListView01;
    DBHelper dbcenter;
    Transaction transaction;

    protected Cursor cursor;
    public static indiMoneyActivity ma;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_indimoney);

        ma = this;
        dbcenter = new DBHelper(this);
        RefreshList();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window w = getWindow();
            w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(indiMoneyActivity.this, addTransactionActivity.class)); finish();
            }
        });

        FloatingActionButton fab2 = (FloatingActionButton) findViewById(R.id.fab2);
        fab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(indiMoneyActivity.this, indiMoneyActivity.class)); finish();
                Toast.makeText(getApplication(), "Refreshed", Toast.LENGTH_LONG).show();
            }
        });

        TextView moneyWalletText = (TextView) findViewById(R.id.money_wallet);
        User dataUser = new User(this);
        JSONObject user = dataUser.getUser();
        try {
            Double balance = Double.parseDouble(user.getString("money"));
            moneyWalletText.setText("Rs. " + String.format("%,.2f", balance));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(indiMoneyActivity.this, MainActivity.class));
        finish();
    }

    public void RefreshList() {
        SQLiteDatabase db = dbcenter.getReadableDatabase();
        cursor = db.rawQuery("SELECT * FROM tb_indiMoney order by id desc", null);
        list = new String[cursor.getCount()];
        list2 = new String[cursor.getCount()];
        list3 = new String[cursor.getCount()];
        cursor.moveToFirst();
        for (int cc = 0; cc < cursor.getCount(); cc++) {
            cursor.moveToPosition(cc);
            list3[cc] = cursor.getString(3).toString();
            list2[cc] = cursor.getString(0).toString();
            list[cc] = cursor.getString(1).toString();
        }

        transaction = new Transaction(this);
        ArrayList<HashMap<String, String>> trxList = transaction.getList();
        TransactionAdapter adapter = new TransactionAdapter(this, trxList);

        ListView01 = (ListView) findViewById(R.id.list_transaction);
        ListView01.setAdapter(adapter);
        ListView01.setSelected(true);
        ListView01.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView arg0, View arg1, int arg2, long arg3) {
                final String selection = list2[arg2];
                final CharSequence[] dialogitem = {" Update Data  ", " Delete Data  "};
                AlertDialog.Builder builder = new AlertDialog.Builder(indiMoneyActivity.this);
                builder.setTitle("Selecton");
                builder.setItems(dialogitem, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {
                        switch (item) {
                            case 0:
                                Intent i = new Intent(getApplicationContext(), UpdateActivity.class);
                                i.putExtra("id", selection);
                                startActivity(i);
                                break;
                            case 1:
                                Intent in = new Intent(getApplicationContext(), DeleteActivity.class);
                                in.putExtra("id", selection);
                                startActivity(in);
                                break;
                        }
                    }
                });
                builder.create().show();
            }
        });
        ((ArrayAdapter) ListView01.getAdapter()).notifyDataSetInvalidated();
    }
}
