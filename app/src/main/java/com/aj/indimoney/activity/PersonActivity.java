package com.aj.indimoney.activity;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.aj.indimoney.DBHelper;
import com.aj.indimoney.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;

public class PersonActivity extends AppCompatActivity {

    DBHelper dbHelper;
    TextView txtname, txtemail, txtmoney, txtincome, txtexpense;
    CircleImageView circleImageView;

    protected Cursor cursor;
    public static final String TAG = PersonActivity.class.getSimpleName();
    final private int REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS = 124;
    public static final int PICK_IMAGE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person);

        dbHelper = new DBHelper(this);
        txtname = (TextView) findViewById(R.id.txtname);
        txtemail = (TextView) findViewById(R.id.txtemail);
        txtmoney = (TextView) findViewById(R.id.txtmoney);
        txtincome = (TextView) findViewById(R.id.txtincome);
        txtexpense = (TextView) findViewById(R.id.txtexpense);

        SQLiteDatabase db = dbHelper.getReadableDatabase();
        cursor = db.rawQuery("SELECT * FROM tb_user WHERE id = 1 ", null);
        cursor.moveToFirst();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window w = getWindow(); // in Activity's onCreate() for instance
            w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }

        DecimalFormat toRupiah = (DecimalFormat) DecimalFormat.getCurrencyInstance();
        DecimalFormatSymbols formatAngka = new DecimalFormatSymbols();
        formatAngka.setCurrencySymbol("Rs.. ");
        formatAngka.setDecimalSeparator(',');
        formatAngka.setGroupingSeparator('.');
        toRupiah.setDecimalFormatSymbols(formatAngka);
        if (cursor.getCount() > 0) {
            cursor.moveToPosition(0);
            txtname.setText(cursor.getString(1).toString());
            txtemail.setText(cursor.getString(2).toString());
            txtmoney.setText(toRupiah.format(Double.valueOf(cursor.getString(5).toString())));
            txtincome.setText(toRupiah.format(Double.valueOf(cursor.getString(3).toString())));
            txtexpense.setText(toRupiah.format(Double.valueOf(cursor.getString(4).toString())));
        }

        circleImageView = findViewById(R.id.fotokue);
        circleImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkSelfPermission(Manifest.permission.CAMERA)
                        != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(new String[]{Manifest.permission.CAMERA},
                            REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS);
                } else {
                    showPictureDialog();
                }
            }
        });
        showImageList();
    }

    private void showPictureDialog(){
        AlertDialog.Builder pictureDialog = new AlertDialog.Builder(this);
        pictureDialog.setTitle("Select Action");
        String[] pictureDialogItems = {
                "Select photo from gallery",
                "Capture photo from camera" };
        pictureDialog.setItems(pictureDialogItems,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                Intent intent = new Intent();
                                intent.setType("image/*");
                                intent.setAction(Intent.ACTION_GET_CONTENT);
                                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);
                                break;
                            case 1:
                                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                                startActivityForResult(cameraIntent, REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS);
                                break;
                        }
                    }
                });
        pictureDialog.show();
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS && resultCode == Activity.RESULT_OK) {
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            circleImageView.setImageBitmap(photo);
            saveImage(photo);
        }
        if (requestCode == PICK_IMAGE) {
            if (data != null) {
                Uri contentURI = data.getData();
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), contentURI);
                    circleImageView.setImageBitmap(bitmap);
                    saveImage(bitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(PersonActivity.this, "Failed!", Toast.LENGTH_SHORT).show();
                }
            }

        }
    }

    private void saveImage(Bitmap finalBitmap) {
        String iname, Image_path;
        int numberOfImages;

        String root = Environment.getExternalStorageDirectory().getPath() + "/indiMoney/";
        System.out.println(root +" Root value in saveImage Function");
        File myDir = new File(root + "/image/");
        if (!myDir.exists()) {
            myDir.mkdirs();
        }

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");
        iname = "image_" + sdf.format(new Date()) + ".jpg";
        File file = new File(myDir, iname);
        if (file.exists())
            file.delete();
        try {
            FileOutputStream out = new FileOutputStream(file);
            finalBitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
            out.flush();
            out.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        MediaScannerConnection.scanFile(this, new String[] { file.toString() }, null,
                new MediaScannerConnection.OnScanCompletedListener() {

                    public void onScanCompleted(String path, Uri uri) {
                        Log.i("ExternalStorage", "Scanned " + path + ":");
                        Log.i("ExternalStorage", "-> uri=" + uri);
                    }
                });

        Image_path = Environment.getExternalStorageDirectory().getPath() + "/indiMoney/image/" +iname;

        File[] files = myDir.listFiles();
        numberOfImages=files.length;
        System.out.println("Total images in Folder "+numberOfImages);
    }

    private void showImageList() {
        String media = "mounted";
        String diskState = Environment.getExternalStorageState();
        try {
            if( diskState.equals(media) ) {
                File file = new File(Environment.getExternalStorageDirectory().getPath() + "/indiMoney/image/");
                if (file.exists()) {
                    File[] files = file.listFiles();
                    Arrays.sort(files);
                    for (int i = 0; i < files.length; i++) {
                        Log.i(TAG, "FOTO PROFILE [ FILE ] : " + files[i]);
                    }
                    Log.i(TAG, "FOTO PROFILE [ GET FILE ] : " + files[files.length - 1].toString());
                    Bitmap b = BitmapFactory.decodeFile(files[files.length - 1].toString());
                    circleImageView.setImageBitmap(b);
                }
            } else {
                Toast.makeText(this, "The external disk is not mounted", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(PersonActivity.this, MainActivity.class)); finish();
    }
}
