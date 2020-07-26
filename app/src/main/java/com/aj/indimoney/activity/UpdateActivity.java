package com.aj.indimoney.activity;

import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.aj.indimoney.DBHelper;
import com.aj.indimoney.R;
import com.aj.indimoney.model.User;

import org.json.JSONException;
import org.json.JSONObject;

public class UpdateActivity extends AppCompatActivity {

    protected Cursor cursor;

    TextInputEditText moneyText, moneyText2, utilitiesText, dateText, IDText, typeText;
    TextInputLayout textInputLayoutUtilities, textInputLayoutMoney;
    RadioButton radioincome, radioexpense;
    RadioGroup radioGroup;
    TextView cancel;
    DBHelper dbHelper;
    Button saveit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);

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

        this.setTitle("Update Data ");

        IDText.setEnabled(false);
        dateText.setEnabled(false);
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

        cancel = (TextView) findViewById(R.id.textViewCancel);
        saveit = (Button) findViewById(R.id.buttonSimpan);
        saveit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                AlertDialog.Builder builder = new AlertDialog.Builder(UpdateActivity.this);
                builder.setCancelable(true);
                builder.setMessage("Are you sure you want to update the data?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (validate()) {
                            String type = "";
                            if (radioincome.isChecked()) {
                                type = "Enter";
                            } else if (radioexpense.isChecked()) {
                                type = "Exit";
                            } else {
                                type = "type";
                            }

                            SQLiteDatabase db = dbHelper.getWritableDatabase();
                            db.execSQL("update tb_indiMoney set utilities='" +
                                    utilitiesText.getText().toString() + "', money='" +
                                    moneyText.getText().toString() + "', type='" +
                                    type + "', date='" +
                                    dateText.getText().toString() + "' where id='" +
                                    IDText.getText().toString() + "'");

                            User dataUser = new User(UpdateActivity.this);
                            JSONObject user = dataUser.getUser();
                            Integer balance = 0;
                            Integer balance2 = 0;
                            Integer balance3 = 0;
                            String typeku = typeText.getText().toString();
                            Integer moneyku = Integer.parseInt(moneyText.getText().toString());
                            Integer moneyku2 = Integer.parseInt(moneyText2.getText().toString());
                            try {
                                if (type.equals("Exit") && typeku.equals("Exit")) {
                                    balance3 = (user.getInt("expense") - moneyku2) + moneyku;
                                    balance2 = user.getInt("income");
                                    balance = (user.getInt("money") + moneyku2) - moneyku;
                                } else if (type.equals("Enter") && typeku.equals("Enter")) {
                                    balance3 = user.getInt("expense");
                                    balance2 = (user.getInt("income") - moneyku2) + moneyku;
                                    balance = (user.getInt("money") - moneyku2) + moneyku;
                                } else if (type.equals("Exit") && typeku.equals("Enter")) {
                                    balance3 = user.getInt("expense") + moneyku;
                                    balance2 = (user.getInt("income") - moneyku2);
                                    balance = (user.getInt("money") - moneyku2) - moneyku;
                                } else {
                                    balance3 = (user.getInt("expense") - moneyku2);
                                    balance2 = user.getInt("income") + moneyku;
                                    balance = (user.getInt("money") + moneyku2) + moneyku;
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
                            Toast.makeText(getApplicationContext(), "Your data has been successfully saved", Toast.LENGTH_LONG).show();
                            indiMoneyActivity.ma.RefreshList();
                            finish();
                        } else {
                            Toast.makeText(UpdateActivity.this, "Your data failed to save", Toast.LENGTH_LONG).show();
                        }
                    }
                });
                builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
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
                AlertDialog.Builder builder = new AlertDialog.Builder(UpdateActivity.this);
                builder.setCancelable(true);
                builder.setMessage("Are you sure you want to go back?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });
                builder.setNegativeButton("Put a little money", new DialogInterface.OnClickListener() {
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
        builder.setMessage("Are you sure you want to go back?");
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

    public boolean validate() {
        boolean valid = false;

        moneyText = (TextInputEditText) findViewById(R.id.editTextMoney);
        utilitiesText = (TextInputEditText) findViewById(R.id.editTextUtilities);

        textInputLayoutUtilities = (TextInputLayout) findViewById(R.id.textInputLayoutUtilities);
        textInputLayoutMoney = (TextInputLayout) findViewById(R.id.textInputLayoutMoney);

        String money = moneyText.getText().toString();
        String utilities = utilitiesText.getText().toString();
        if (money.isEmpty()) {
            valid = false;
            textInputLayoutMoney.setError("Put a little money");
        } else {
            if (money.length() >= 1) {
                valid = true;
                textInputLayoutMoney.setError(null);
            } else {
                valid = false;
                textInputLayoutMoney.setError("Money is not according to regulations");
            }
        }

        if (utilities.isEmpty()) {
            valid = false;
            textInputLayoutUtilities.setError("Utilities are still empty");
        } else {
            if (utilities.length() >= 1) {
                valid = true;
                textInputLayoutUtilities.setError(null);
            } else {
                valid = false;
                textInputLayoutUtilities.setError("Your needs do not match the conditions");
            }
        }
        return valid;
    }
}
