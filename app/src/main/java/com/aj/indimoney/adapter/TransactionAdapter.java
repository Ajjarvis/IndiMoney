package com.aj.indimoney.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import android.database.Cursor;

import com.aj.indimoney.R;

import java.util.ArrayList;
import java.util.HashMap;

public class TransactionAdapter extends ArrayAdapter<HashMap<String, String>> {
    private final ArrayList data;
    private LayoutInflater mInflater;

    public TransactionAdapter(Context context, ArrayList<HashMap<String, String>> trxList) {
        super(context, 0, trxList);
        data = new ArrayList();
        data.addAll(trxList);
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        HashMap<String, String> tr = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_row_images, parent, false);
        }
        TextView title = (TextView) convertView.findViewById(R.id.title);
        TextView money = (TextView) convertView.findViewById(R.id.money);
        TextView tglTrx = (TextView) convertView.findViewById(R.id.tglTrx);
        TextView type = (TextView) convertView.findViewById(R.id.type_trx);

        int image = R.drawable.rupees_g;
        int image2 = R.drawable.rupees_r;
        if (tr.get("type").equals("Enter")) {
            ImageView imageView = (ImageView) convertView.findViewById(R.id.list_image);
            imageView.setImageResource(image);
            title.setTextColor(Color.parseColor("#388E3C"));
            money.setTextColor(Color.parseColor("#388E3C"));
            tglTrx.setTextColor(Color.parseColor("#388E3C"));
            type.setTextColor(Color.parseColor("#388E3C"));
        } else {
            ImageView imageView = (ImageView) convertView.findViewById(R.id.list_image);
            imageView.setImageResource(image2);
            title.setTextColor(Color.RED);
            money.setTextColor(Color.RED);
            tglTrx.setTextColor(Color.RED);
            type.setTextColor(Color.RED);
        }

        title.setText(tr.get("utilities").toString());
        Double am = Double.parseDouble(tr.get("money"));
        money.setText("Rs. " + String.format("%,.2f", am));
        tglTrx.setText(tr.get("date"));
        type.setText(tr.get("type"));

        return convertView;
    }
}
