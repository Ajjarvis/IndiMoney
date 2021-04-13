package com.aj.indimoney.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Toast;

import com.aj.indimoney.DBHelper;
import com.aj.indimoney.R;
import com.aj.indimoney.model.Transaction;
import com.aj.indimoney.model.User;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONException;
import org.json.JSONObject;

public class img2transaction extends AppCompatActivity {

    TextInputEditText moneyText, utilitiesText;
    RadioButton radioincome, radioexpense;
    Transaction transaction;
    Transaction dataTransaction = new Transaction();
    TextInputLayout textInputLayoutUtilities, textInputLayoutMoney;
    ImageView capturedBitmapImage;

    String userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_img2transaction);

        capturedBitmapImage = findViewById(R.id.capturedImage_i2t);
        moneyText = (TextInputEditText) findViewById(R.id.editTextMoney_i2t);
        utilitiesText = (TextInputEditText) findViewById(R.id.editTextUtilities_i2t);
        radioincome = findViewById(R.id.radioincome_i2t);
        radioexpense = findViewById(R.id.radioexpense_i2t);
        transaction = new Transaction(this);

        View btn = findViewById(R.id.buttonSimpan_i2t);
        View cancel = findViewById(R.id.textViewCancel_i2t);

        User dataUser = new User(img2transaction.this);
        JSONObject user = dataUser.getUser();
        try {
            userName = user.getString("name");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Intent intent = getIntent();
        byte[] byteArray = getIntent().getByteArrayExtra("Bytearrayimage");
        Bitmap bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
        capturedBitmapImage.setImageBitmap(bitmap);
        String name_str = intent.getStringExtra("Recognized_str_name");
        utilitiesText.setText(name_str);
        String money = intent.getStringExtra("Recognized_str_money");
        moneyText.setText(money);

        System.out.println( userName.equals(name_str) );
        if (userName != null) {
            System.out.println(";;;;;;;;;;;;;;;;;;;;;;");
            System.out.println(userName);
            System.out.println(name_str);
            if ( userName.equals(name_str) ) {
                radioexpense.setChecked(true);
                radioincome.setChecked(false);
                System.out.println(";;;;;;;;;;;;;;;;;;;;;;-----------");
            } else {
                radioincome.setChecked(true);
                radioexpense.setChecked(false);
                System.out.println(";;;;;;;;;;;;;;;;;;;;;;+++++++++++");
            }
        }

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String utilities = utilitiesText.getText().toString();
                if (validate()) {
                    String type = "";
                    if (radioincome.isChecked()) {
                        type = "Income";
                    } else if (radioexpense.isChecked()) {
                        type = "Expense";
                    } else {
                        type = "type";
                    }

                    if (type.equals("type")){
                        Toast.makeText(img2transaction.this, "Income or Expense not Selected.",Toast.LENGTH_LONG).show();
                    } else {
                        Integer money = Integer.parseInt(moneyText.getText().toString());
                        dataTransaction.utilities = utilities;
                        dataTransaction.money = money;
                        dataTransaction.type = type;
                        transaction.insertTable(dataTransaction);

                        User dataUser = new User(img2transaction.this);
                        JSONObject user = dataUser.getUser();

                        Integer balance = 0;
                        Integer balance2 = 0;
                        Integer balance3 = 0;
                        try {
                            if (type.equals("Expense")) {
                                balance3 = money + user.getInt("expense");
                                balance2 = user.getInt("income");
                                balance = user.getInt("money") - money;
                            } else {
                                balance3 = user.getInt("expense");
                                balance2 = money + user.getInt("income");
                                balance = money + user.getInt("money");
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

                        moneyText.setText("");
                        utilitiesText.setText("");
                        moneyText.requestFocus();
                        Toast.makeText(img2transaction.this, "Your data has been successfully saved", Toast.LENGTH_SHORT).show();
                    }
                    onBackPressed();
                } else {
                    Toast.makeText(img2transaction.this, "Your data failed to save", Toast.LENGTH_SHORT).show();
                }
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(img2transaction.this, "Your data failed to save", Toast.LENGTH_SHORT).show();
                onBackPressed();
            }
        });
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(img2transaction.this, img2Text.class)); finish();
    }

    public boolean validate() {
        boolean valid = false;

        moneyText = (TextInputEditText) findViewById(R.id.editTextMoney_i2t);
        utilitiesText = (TextInputEditText) findViewById(R.id.editTextUtilities_i2t);

        textInputLayoutUtilities = (TextInputLayout) findViewById(R.id.textInputLayoutUtilities);
        textInputLayoutMoney = (TextInputLayout) findViewById(R.id.textInputLayoutMoney);

        String money = moneyText.getText().toString();
        String utilities = utilitiesText.getText().toString();
        if (money.isEmpty()) {
            valid = false;
            textInputLayoutMoney.setError("There is no money");
        } else {
            if (money.length() >= 1) {
                valid = true;
                textInputLayoutMoney.setError(null);
            } else {
                valid = false;
                textInputLayoutMoney.setError("Entered Money is not in Proper Manner");
            }
        }

        if (utilities.isEmpty()) {
            valid = false;
            textInputLayoutUtilities.setError("Utilities still empty");
        } else {
            if (utilities.length() >= 1) {
                valid = true;
                textInputLayoutUtilities.setError(null);
            } else {
                valid = false;
                textInputLayoutUtilities.setError("Your needs do not meet the requirements");
            }
        }
        return valid;
    }
}