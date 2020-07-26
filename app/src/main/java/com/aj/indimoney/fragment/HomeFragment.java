package com.aj.indimoney.fragment;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.aj.indimoney.activity.img2Text;
import com.ajts.androidmads.library.SQLiteToExcel;
import com.aj.indimoney.R;
import com.aj.indimoney.activity.indiMoneyActivity;
import com.aj.indimoney.activity.MainActivity;
import com.aj.indimoney.activity.PersonActivity;
import com.aj.indimoney.activity.addTransactionActivity;
import com.aj.indimoney.activity.AboutActivity;
import com.aj.indimoney.export.Utils;
import com.aj.indimoney.model.Transaction;
import com.aj.indimoney.model.User;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

public class HomeFragment extends Fragment {

    public String username, email;
    public ImageButton expense2, income;

    Transaction transaction;
    com.aj.indimoney.DBHelper DBHelper;
    String directory_path = Environment.getExternalStorageDirectory().getPath() + "/indiMoney/export/";
    SQLiteToExcel sqliteToExcel;

    public HomeFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_home, container, false);

        User user = new User(getActivity());
        JSONObject dataUser = user.getUser();
        transaction = new Transaction(getActivity());
        TextView moneyWalletText = (TextView) rootView.findViewById(R.id.rupees);
        try {
            Double balance = Double.parseDouble(dataUser.getString("money"));
            moneyWalletText.setText("Rs. " + String.format("%,.2f", balance));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        ImageButton indiMoney = (ImageButton) rootView.findViewById(R.id.indiMoney);
        ImageButton about = (ImageButton) rootView.findViewById(R.id.about);
        ImageButton addTransaction = (ImageButton) rootView.findViewById(R.id.addTransaction);
        income = (ImageButton) rootView.findViewById(R.id.income);
        expense2 = (ImageButton) rootView.findViewById(R.id.expense2);
        ImageButton person = (ImageButton) rootView.findViewById(R.id.person);
        ImageButton exit = (ImageButton) rootView.findViewById(R.id.exit);
        ImageButton export = (ImageButton) rootView.findViewById(R.id.export);
        ImageButton addbills = (ImageButton) rootView.findViewById(R.id.addbills);

        addbills.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), img2Text.class);
                startActivity(intent);
                getActivity().finish();
            }
        });

        export.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                File myDir = new File(directory_path);
                if (!myDir.exists()) {
                    myDir.mkdirs();
                }
                sqliteToExcel = new SQLiteToExcel(getActivity(), DBHelper.DATABASE_NAME, directory_path);
                sqliteToExcel.exportAllTables("export.xls", new SQLiteToExcel.ExportListener() {
                    @Override
                    public void onStart() {

                    }

                    @Override
                    public void onCompleted(String filePath) {
                        Utils.showSnackBar(v, "Export is Successful. " + directory_path);
                    }

                    @Override
                    public void onError(Exception e) {
                        Utils.showSnackBar(v, e.getMessage());
                    }
                });
            }
        });

        indiMoney.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), indiMoneyActivity.class);
                startActivity(intent);
                getActivity().finish();
            }
        });

        about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AboutActivity.class);
                startActivity(intent);
            }
        });

        addTransaction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), addTransactionActivity.class);
                startActivity(intent);
                getActivity().finish();
            }
        });
        income.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            MainActivity.main.viewPager.setCurrentItem(0);
            }
        });

        expense2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.main.viewPager.setCurrentItem(2);
            }
        });

        person.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), PersonActivity.class);
                startActivity(intent);
                getActivity().finish();
            }
        });

        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setCancelable(true);
                builder.setMessage("Want to Exit the Application?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        getActivity().finish();
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
        return rootView;
    }

    private void closefragment() {
        getActivity().finish();
    }
}
