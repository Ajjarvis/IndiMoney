package com.aj.indimoney.activity;

import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.google.android.material.textfield.TextInputEditText;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.aj.indimoney.DBHelper;
import com.aj.indimoney.R;
import com.aj.indimoney.model.User;

import org.json.JSONException;
import org.json.JSONObject;

public class DeleteActivity extends AppCompatActivity {

    TextInputEditText moneyText, moneyText2, utilitiesText, dateText, IDText, typeText;
    RadioButton radioincome, radioexpense;
    RadioGroup radioGroup;
    TextView cancel;
    protected Cursor cursor;
    DBHelper dbHelper;
    Button hapus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete);

        dbHelper = new DBHelper(this);
        typeText = (TextInputEditText) findViewById(R.id.editTextType2);
        moneyText = (TextInputEditText) findViewById(R.id.editTextMoney);
        utilitiesText = (TextInputEditText) findViewById(R.id.editTextUtilities);
        dateText = (TextInputEditText) findViewById(R.id.editTextDate);
        IDText = (TextInputEditText) findViewById(R.id.editTextID);
        moneyText2 = (TextInputEditText) findViewById(R.id.editTextMoney2);
        radioincome = (RadioButton) findViewById(R.id.radioincome2);
        radioexpense = (RadioButton) findViewById(R.id.radioexpense2);
        radioGroup = (RadioGroup) findViewById(R.id.radiogroup);
        this.setTitle("Delete Data ");

        IDText.setEnabled(false);
        dateText.setEnabled(false);
        moneyText.setEnabled(false);
        utilitiesText.setEnabled(false);
        radioincome.setEnabled(false);
        radioexpense.setEnabled(false);
        typeText.setVisibility(View.GONE);
        moneyText2.setVisibility(View.GONE);

        SQLiteDatabase db = dbHelper.getReadableDatabase();
        cursor = db.rawQuery("SELECT * FROM tb_indiMoney WHERE id= '" +
                getIntent().getStringExtra("id") + "'", null);
        cursor.moveToFirst();
        if (cursor.getCount() > 0) {
            cursor.moveToPosition(0);
            IDText.setText(cursor.getString(0).toString());
            utilitiesText.setText(cursor.getString(1).toString());
            moneyText.setText(cursor.getString(2).toString());
            moneyText2.setText(cursor.getString(2).toString());
            typeText.setText(cursor.getString(3).toString());
            dateText.setText(cursor.getString(4).toString());
        }

        if (cursor.getString(3).toString().equals("Enter")) {
            radioincome.setChecked(true);
        } else {
            radioexpense.setChecked(true);
        }

        hapus = (Button) findViewById(R.id.buttonHapus);
        cancel = (TextView) findViewById(R.id.textViewCancel);
        hapus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                AlertDialog.Builder builder = new AlertDialog.Builder(DeleteActivity.this);
                builder.setCancelable(true);
                builder.setMessage("Yakin ingin menghapus data ?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String idku = IDText.getText().toString();
                        SQLiteDatabase db = dbHelper.getWritableDatabase();
                        db.execSQL("delete from tb_indiMoney where id = '" + idku + "'");
                        User dataUser = new User(DeleteActivity.this);
                        JSONObject user = dataUser.getUser();

                        Integer balance = 0;
                        Integer balance2 = 0;
                        Integer balance3 = 0;

                        String type = "";
                        if (radioincome.isChecked()) {
                            type = "Enter";
                        } else if (radioexpense.isChecked()) {
                            type = "Exit";
                        } else {
                            type = "type";
                        }

                        String typeku = typeText.getText().toString();
                        Integer moneyku = Integer.parseInt(moneyText2.getText().toString());
                        try {
                            if (type.equals("Exit") && typeku.equals("Exit")) {
                                balance3 = user.getInt("expense") - moneyku;
                                balance2 = user.getInt("income");
                                balance = moneyku + user.getInt("money");
                            } else {
                                balance3 = user.getInt("expense");
                                balance2 = user.getInt("income") - moneyku;
                                balance = user.getInt("money") - moneyku;
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        Log.d("Balance2", balance2.toString());
                        Log.d("Balance3", balance3.toString());
                        Log.d("Balance", balance.toString());
                        dataUser.updateBalance2(balance2);
                        dataUser.updateBalance3(balance3);
                        dataUser.updateBalance(balance);
                        finish();

                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                AlertDialog alert = builder.create();
                alert.show();
            }


        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(DeleteActivity.this);
                builder.setCancelable(true);
                builder.setMessage("Yakin ingin kembali ?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                AlertDialog alert = builder.create();
                alert.show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setMessage("Yakin ingin kembali ?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }
}
