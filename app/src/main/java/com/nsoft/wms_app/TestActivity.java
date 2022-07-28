package com.nsoft.wms_app;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ClipData;
import android.content.ClipDescription;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextWatcher;
import android.util.Log;
import android.view.InputEvent;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.m3.sdk.scannerlib.BarcodeListener;
import com.m3.sdk.scannerlib.BarcodeManager;

import org.w3c.dom.Document;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.regex.Pattern;

import com.m3.sdk.scannerlib.Barcode;
import com.m3.sdk.scannerlib.BarcodeListener;
import com.m3.sdk.scannerlib.BarcodeManager;
import com.m3.sdk.scannerlib.Barcode.Symbology;

import javax.xml.transform.Result;


public class TestActivity extends AppCompatActivity {

    private Barcode mBarcode = null;
    private BarcodeListener mListener = null;
    private BarcodeManager mManager = null;
    private com.m3.sdk.scannerlib.Barcode.Symbology mSymbology = null;

    private Button  button;
    private TextView TVbarcode;
    private TextView TVbarcode2;
    private String   Sbarcode;
    private String   mTvResult;

    Connection connect;              //database Connect
    String ConnectionResult = "";



    private int i;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_test);

        TVbarcode = findViewById(R.id.tv_barcode);
        TVbarcode2 = findViewById(R.id.tv_barcode2);
        button = findViewById(R.id.btnUrl);


        ArrayList<String> array_bar = new ArrayList<>();


        Intent intent = getIntent();
        mTvResult = (String) intent.getStringExtra("intentBarcode");




        //ETbarcode.setText(mTvResult);
        TVbarcode.setText(mTvResult);
        System.out.println(Sbarcode);
        Sbarcode = TVbarcode.getText().toString();



        barcodescan(TVbarcode);
        TVbarcode.setText(mTvResult);



        TVbarcode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                Log.e(TAG, "beforeTextChanged() - charSequence : " + charSequence);
                System.out.println("*************beforeTextChanged*************");
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                Log.e(TAG, "onTextChanged() - charSequence : " + charSequence);
                System.out.println("*************onTextChanged*************");
            }

            @Override
            public void afterTextChanged(Editable editable) {
                Log.e(TAG, "afterTextChanged() - editable : " + editable.toString());
                System.out.println("*************afterTextChanged*************");
                Sbarcode = TVbarcode.getText().toString();
                System.out.println("찍힌 바코드 : " + TVbarcode);
                System.out.println("찍힌 S바코드 : " + Sbarcode);
                System.out.println("찍힌 Rewult바코드 : " + mTvResult);


                try {
                    ConnectionHelper connectionHelper = new ConnectionHelper();
                    connect = connectionHelper.ConnectionClass();
                    if (connect != null) {
                        //String query = "INSERT INTO KKJ_EMP VALUES ('" + Email + "', '" + Pwd + "');";
                        //프로시저 돌리기
                        String query = "INSERT INTO BARCODE_TEST VALUES ('" + Sbarcode + "')select * from BARCODE_TEST";
                        Statement st = connect.createStatement();
                        ResultSet rs = st.executeQuery(query);

                        while (rs.next()) {
                            //Toast.makeText(getApplicationContext(), "데이터베이스에 값 넣기", Toast.LENGTH_SHORT).show();
                            if (rs.getString(1).isEmpty() != true) {
                                Toast.makeText(TestActivity.this, rs.getString(1) + rs.getString(2) + rs.getString(3), Toast.LENGTH_SHORT).show();
                                //array_bar.add(rs.getString(1));
                                //array_bar.add(rs.getString(2));
                                //array_bar.add(rs.getString(3));
                                System.out.println("55555555555555555555555");
                                //System.out.println(rs.getString(1) + rs.getString(2) + rs.getString(3));
                            } else {
                                ConnectionResult = "Check Connection";
                            }
                        }
                    }
                } catch (Exception e) {
                }

            }

                /*
                try{
                    ConnectionHelper connectionHelper = new ConnectionHelper();
                    connect = connectionHelper.ConnectionClass();
                    if(connect!=null){
                        //String query = "INSERT INTO KKJ_EMP VALUES ('" + Email + "', '" + Pwd + "');";
                        //프로시저 돌리기
                        String query = "select * from BARCODE_TEST";
                        Statement st = connect.createStatement();
                        ResultSet rs = st.executeQuery(query);

                        while (rs.next()){
                            if(rs.getString(1).isEmpty()!=true){
                                Toast.makeText(getApplicationContext(), rs.getString(1) + rs.getString(2) + rs.getString(3), Toast.LENGTH_SHORT).show();
                                array_bar.add(rs.getString(1));
                                array_bar.add(rs.getString(2));
                                array_bar.add(rs.getString(3));
                                System.out.println("55555555555555555555555");
                                System.out.println(rs.getString(1) + rs.getString(2) + rs.getString(3));
                            }else if(rs.getString(1).isEmpty()){
                                Toast.makeText(getApplicationContext(), "데이터베이스에 안들어갔습니다.", Toast.LENGTH_SHORT).show();

                            }
                        }

                    }else{
                        ConnectionResult="Check Connection";
                    }
                }catch(Exception ex){
                }
                */




        });


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    ConnectionHelper connectionHelper = new ConnectionHelper();
                    connect = connectionHelper.ConnectionClass();
                    if(connect!=null){

                        String query = "select * from BARCODE_TEST";
                        Statement st = connect.createStatement();
                        ResultSet rs = st.executeQuery(query);

                        while (rs.next()){
                            if(rs.getString(1).isEmpty()!=true){
                                //Toast.makeText(getApplicationContext(), rs.getString(1) + rs.getString(2) + rs.getString(3), Toast.LENGTH_SHORT).show();
                                //System.out.println("55555555555555555555555");
                                //System.out.println(rs.getString(1) + rs.getString(2) + rs.getString(3));
                                System.out.println(rs.getString(1));
                                array_bar.add(rs.getString(1));
                                System.out.println(array_bar);
                            }else if(rs.getString(1).isEmpty()){
                                Toast.makeText(getApplicationContext(), "빈값.", Toast.LENGTH_SHORT).show();

                            }
                        }

                    }else{
                        ConnectionResult="Check Connection";
                    }
                }catch(Exception ex){
                    Toast.makeText(getApplicationContext(), "데이터베이스에 안들어갔습니다.", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }


    public void barcodescan(TextView mTvResult) {
        // lib
        Barcode mBarcode = null;
        BarcodeListener mListener = null;
        BarcodeManager mManager = null;
        Barcode.Symbology mSymbology = null;

        //ui
        mBarcode = new Barcode(this);
        mManager = new BarcodeManager(this);
        mSymbology = mBarcode.getSymbologyInstance();

        mBarcode.scanStart();

        mListener = new BarcodeListener() {

            @Override
            public void onGetSymbology(int nSymbol, int nVal) {
                Log.i("ScannerTest", "onGetSymbology result=" + nSymbol + ", " + nVal);
            }
            @Override
            public void onBarcode(String strBarcode) {
                Log.i("ScannerTest1", "result=" + strBarcode);
                mTvResult.setText(strBarcode);
                //mTvResult.toString();
                //Intent intent = new Intent(TestActivity.this, TestActivity.class);
                Intent intent = new Intent(getApplicationContext(), TestActivity.class);
                intent.putExtra("intentBarcode", strBarcode);
                startActivity(intent);
            }
            @Override
            public void onBarcode(String barcode, String codeType) {
                Log.i("ScannerTest2", "result=" + barcode);
                //mTvResult.setText(barcode);
            }
        };
        mManager.addListener(mListener);
        mBarcode.scanDispose();
    }



}









