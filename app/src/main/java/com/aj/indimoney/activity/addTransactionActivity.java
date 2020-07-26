package com.aj.indimoney.activity;

import android.content.Intent;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RadioButton;
import android.widget.Toast;

import com.aj.indimoney.R;
import com.aj.indimoney.model.Transaction;
import com.aj.indimoney.model.User;

import org.json.JSONException;
import org.json.JSONObject;

public class addTransactionActivity extends AppCompatActivity {

    TextInputEditText moneyText, utilitiesText;
    RadioButton radioincome, radioexpense;
    Transaction transaction;
    Transaction dataTransaction = new Transaction();
    TextInputLayout textInputLayoutUtilities, textInputLayoutMoney;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addtransaction);

        moneyText = (TextInputEditText) findViewById(R.id.editTextMoney);
        utilitiesText = (TextInputEditText) findViewById(R.id.editTextUtilities);
        radioincome = findViewById(R.id.radioincome);
        radioexpense = findViewById(R.id.radioexpense);
        transaction = new Transaction(this);

        View btn = findViewById(R.id.buttonSimpan);
        View cancel = findViewById(R.id.textViewCancel);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String utilities = utilitiesText.getText().toString();
                if (validate()) {
                    String type = "";
                    if (radioincome.isChecked()) {
                        type = "Enter";
                    } else if (radioexpense.isChecked()) {
                        type = "Exit";
                    } else {
                        type = "type";
                    }

                    if (type.equals("type")){
                        Toast.makeText(addTransactionActivity.this, "Income or Expence not Seleceted.",Toast.LENGTH_LONG).show();
                    } else {
                        Integer money = Integer.parseInt(moneyText.getText().toString());
                        dataTransaction.utilities = utilities;
                        dataTransaction.money = money;
                        dataTransaction.type = type;
                        transaction.insertTable(dataTransaction);

                        User dataUser = new User(addTransactionActivity.this);
                        JSONObject user = dataUser.getUser();

                        Integer balance = 0;
                        Integer balance2 = 0;
                        Integer balance3 = 0;
                        try {
                            if (type.equals("Exit")) {
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
                        Toast.makeText(addTransactionActivity.this, "Your data has been successfully saved", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(addTransactionActivity.this, "Your data failed to save", Toast.LENGTH_SHORT).show();
                }
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(addTransactionActivity.this, com.aj.indimoney.activity.indiMoneyActivity.class)); finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(addTransactionActivity.this, com.aj.indimoney.activity.indiMoneyActivity.class)); finish();
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
            textInputLayoutMoney.setError("There is no money");
        } else {
            if (money.length() >= 1) {
                valid = true;
                textInputLayoutMoney.setError(null);
            } else {
                valid = false;
                textInputLayoutMoney.setError("Money is not according to rules");
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
