package com.aj.indimoney.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.aj.indimoney.DBHelper;
import com.aj.indimoney.R;
import com.aj.indimoney.activity.DeleteActivity;
import com.aj.indimoney.activity.MainActivity;
import com.aj.indimoney.activity.UpdateActivity;
import com.aj.indimoney.adapter.TransactionAdapter;
import com.aj.indimoney.model.Transaction;
import com.aj.indimoney.model.User;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class ExpenseFragment extends Fragment {

    String[] list;
    String[] list2;
    String[] list3;
    ListView ListView01;
    DBHelper dbcenter;
    Transaction transaction;

    protected Cursor cursor;

    public ExpenseFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_expense, container, false);

        TextView expense = (TextView) rootView.findViewById(R.id.expense);
        FloatingActionButton fab = (FloatingActionButton) rootView.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity.main.viewPager.setCurrentItem(1);
                Toast.makeText(getActivity(), "Refreshed",Toast.LENGTH_LONG).show();
            }
        });

        dbcenter = new DBHelper(getActivity());
        SQLiteDatabase db = dbcenter.getReadableDatabase();
        cursor = db.rawQuery("SELECT * From tb_indiMoney where type LIKE '%Exit%'", null);
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

        transaction = new Transaction(getActivity());
        ArrayList<HashMap<String, String>> trxList = transaction.getList3();
        TransactionAdapter adapter = new TransactionAdapter(getActivity(), trxList);
        ListView01 = (ListView) rootView.findViewById(R.id.list_transaction);
        ListView01.setAdapter(adapter);
        ListView01.setSelected(true);
        ListView01.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView arg0, View arg1, int arg2, long arg3) {
                final String selection = list2[arg2]; //.getItemAtPosition(arg2).toString();
                final CharSequence[] dialogitem = {" Update Data  ", " Delete Data  "};
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Selection");
                builder.setItems(dialogitem, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {
                        switch (item) {
                            case 0:
                                Intent i = new Intent(getActivity(), UpdateActivity.class);
                                i.putExtra("id", selection);
                                startActivity(i);
                                break;
                            case 1:
                                Intent in = new Intent(getActivity(), DeleteActivity.class);
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

        User dataUser = new User(this.getActivity());
        JSONObject user = dataUser.getUser();
        try {
            Double balance3 = Double.parseDouble(user.getString("expense"));
            expense.setText("Rs. " + String.format("%,.2f", balance3));

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return rootView;
    }
}
