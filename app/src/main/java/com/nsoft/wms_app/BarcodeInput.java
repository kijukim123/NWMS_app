package com.nsoft.wms_app;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.m3.sdk.scannerlib.Barcode;
import com.m3.sdk.scannerlib.BarcodeListener;
import com.m3.sdk.scannerlib.BarcodeManager;

import org.w3c.dom.Text;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class BarcodeInput extends AppCompatActivity {

    private Button btn_input;
    private Button btn_cancel;
    private Button ResetButton;
    private ImageButton back;
    private ImageButton logout;
    private List<String> LIST_Name_Input = new ArrayList<>();
    private List<String> LIST_Number_Input = new ArrayList<>();
    private List<String> LIST_Barcode_Input = new ArrayList<>();
    private List<String> LIST_Name_Cancel = new ArrayList<>();
    private List<String> LIST_Number_Cancel = new ArrayList<>();
    private List<String> LIST_Barcode_Cancel = new ArrayList<>();
    private ListView BarcodeInputListview;
    private ListView BarcodeCancelListview;
    private TextView LocationBarcodeInput;
    private EditText EtPreInput;
    private String SPreInput;
    private String SPreInputR;
    private String SPreInputA;
    private String RgBtn1Click;
    private String RgBtn2Click;
    private TextView Alert1;
    private TextView Alert2;
    private String UserId;
    private Context mContext;

    Connection connect;              //database Connect
    String ConnectionResult = "";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.barcode_input_main);

        Alert1 = findViewById(R.id.barcode_input_alert1);
        Alert2 = findViewById(R.id.barcode_input_alert2);

        btn_input = findViewById(R.id.rg_btn1);
        btn_cancel = findViewById(R.id.rg_btn2);
        ResetButton = findViewById(R.id.button);

        EtPreInput = findViewById(R.id.et_pre_input);
        LocationBarcodeInput = findViewById(R.id.tv_barcode_location);

        BarcodeInputListview = (ListView) findViewById(R.id.listview_barcode_input);
        BarcodeCancelListview = (ListView) findViewById(R.id.listview_barcode_cancel);

        //??????????????????????????? ???????????? ???????????? ????????? ?????????
        BarcodeInputListview.setVisibility(View.INVISIBLE);
        BarcodeCancelListview.setVisibility(View.INVISIBLE);

        //???????????? UserId ??? ????????????
        mContext = this;
        UserId = PreferenceManager.getString(mContext, "UID");
        System.out.println(UserId + "///////////////////////////////////////////////////////////////");


        ResetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LIST_Name_Input.clear();
                LIST_Number_Input.clear();
                LIST_Barcode_Input.clear();
                LIST_Name_Cancel.clear();
                LIST_Number_Cancel.clear();
                LIST_Barcode_Cancel.clear();
                CustomListBarcodeInput adapterInput = new CustomListBarcodeInput(BarcodeInput.this);
                BarcodeInputListview = (ListView) findViewById(R.id.listview_barcode_input);
                BarcodeInputListview.setAdapter(adapterInput);
                CustomListBarcodeCancel adapterCancel = new CustomListBarcodeCancel(BarcodeInput.this);
                BarcodeCancelListview = (ListView) findViewById(R.id.listview_barcode_cancel);
                BarcodeCancelListview.setAdapter(adapterCancel);
                LocationBarcodeInput.setText("");
                Alert1.setText("0 Scaneed");
                Alert2.setTextColor(Color.BLACK);
                Alert2.setText("??????????????? ???????????????.");

            }
        });


        EtPreInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(EtPreInput.getWindowToken(), 0);
            }
        });

        //editText (??????,??????,??????)??? ??????
        EtPreInput.setFilters(new InputFilter[]{new InputFilter() {
            @Override
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                Pattern ps = Pattern.compile("^[a-zA-Z0-9???-??????-??????-???\\u318D\\u119E\\u11A2\\u2022\\u2025a\\u00B7\\uFE55]+$");
                if (source.equals("") || ps.matcher(source).matches()) {
                    return source;
                }
                else if(LocationBarcodeInput != null && LocationBarcodeInput.length() >10){
                    //????????? ??????????????? ????????? ?????? ????????? EditText ?????????
                    EtPreInput.setText(null);
                    EtPreInput.setText("");
                    //SPreInputR.replace(SPreInputR, "");
                    SPreInputA.replace(SPreInputA, "");
                    System.out.println("EtPreInput:::::::" + EtPreInput);
                }
                else if(SPreInputA != null && SPreInputA.length() >10){
                    //????????? ??????????????? ????????? ?????? ????????? EditText ?????????
                    EtPreInput.setText(null);
                    EtPreInput.setText("");
                    System.out.println("EtPreInput:::::::" + EtPreInput);
                }
                return "";
            }
        },new InputFilter.LengthFilter(20)});






        //????????? ??????
        EtPreInput.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                switch(keyCode){
                    case KeyEvent.KEYCODE_ENTER:
                        SPreInput = EtPreInput.getText().toString();
                        System.out.println("!!!!!!!!!!!!!!!!!!!EnterKeyOn!!!!!!!!!!!!!!!!!!!!");
                        System.out.println(SPreInput);


                        if(SPreInput.startsWith("R")){
                            LocationBarcodeInput.setText(SPreInput);
                            SPreInputR = SPreInput;

                            try{  //???????????? ?????????????????? ??????
                                ConnectionHelper connectionHelper = new ConnectionHelper();
                                connect = connectionHelper.ConnectionClass();

                                if(connect!=null){
                                    //????????? ?????? ??????
                                    String query4 = "EXEC SP_PDA_COMMON_BARCODE_TYPE '" + SPreInputR + "','ko'";
                                    Statement st4 = connect.createStatement();
                                    ResultSet rs4 = st4.executeQuery(query4);
                                    System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!?????????????????? ????????? ?????? ??????!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
                                    while (rs4.next()){
                                        //????????? ????????? ?????? ??????
                                        if(rs4.getString(1).isEmpty()!=true) {
                                            LocationBarcodeInput.setText(rs4.getString(5));
                                            LIST_Name_Input.clear();
                                            LIST_Number_Input.clear();
                                            LIST_Barcode_Input.clear();
                                            System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!????????? ????????? ?????? ??????!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
                                            System.out.println(rs4.getString(5));
                                            EtPreInput.setText("");
                                        }
                                    }
                                }else{
                                    ConnectionResult="Check Connection";
                                }
                            }catch(Exception ex){
                                Toast.makeText(getApplicationContext(), "????????????????????? ?????????????????????.", Toast.LENGTH_SHORT).show();
                            }
                        }


                        else if(SPreInput.startsWith("A") && RgBtn1Click == "1"){
                            SPreInputA = SPreInput;
                            try{  //??????????????? ?????????????????? ??????
                                ConnectionHelper connectionHelper = new ConnectionHelper();
                                connect = connectionHelper.ConnectionClass();

                                System.out.println(SPreInput);
                                if(connect!=null){
                                    //???????????? ???????????? ?????????
                                    String query = "EXEC dbo.SP_PDA_WM00010_SAVE_VALIDATION '" + SPreInputR + "','" + SPreInputA + "','" + UserId +  "','ko'";
                                    System.out.println("EXEC dbo.SP_PDA_WM00010_SAVE_VALIDATION '" + SPreInputR + "','" + SPreInputA + "','" + UserId +  "','ko'");
                                    Statement st = connect.createStatement();
                                    ResultSet rs = st.executeQuery(query);

                                    while (rs.next()){
                                        System.out.println(rs.getString(2) + "/////////////////////////////////////////");
                                        //????????? ????????? ?????? ??????
                                        if(rs.getString(2).isEmpty()==true) {
                                            try{
                                                if(connect!=null){  //?????????????????? ?????? ???????????? ??????(TB_WM_BCOD)
                                                    String query3 = "EXEC dbo.SP_PDA_WM00010_SAVE '" + SPreInputR + "','" + SPreInputA + "','" + UserId +  "','ko'";
                                                    Statement st3 = connect.createStatement();
                                                    ResultSet rs3 = st3.executeQuery(query3);

                                                    while (rs3.next()){
                                                        System.out.println("????????? : " + rs3.getString(5).toString().length());
                                                        if(rs3.getString(1).isEmpty()!=true) {
                                                            LIST_Barcode_Input.add(rs3.getString(1));
                                                            LIST_Name_Input.add(rs3.getString(3));
                                                            LIST_Number_Input.add(rs3.getString(4));
                                                            Alert1.setText(LIST_Barcode_Input.size() + " Scanned");
                                                            Alert2.setTextColor(Color.BLACK);
                                                            Alert2.setText("?????? ????????? ?????????????????????.");
                                                            CustomListBarcodeInput adapterInput = new CustomListBarcodeInput(BarcodeInput.this);
                                                            BarcodeInputListview = (ListView) findViewById(R.id.listview_barcode_input);
                                                            BarcodeInputListview.setAdapter(adapterInput);
                                                        }

                                                    }
                                                }else{
                                                    ConnectionResult="Check Connection";
                                                }
                                            }catch(Exception e){
                                                //Toast.makeText(getApplicationContext(), "????????????????????? ?????????????????????.", Toast.LENGTH_SHORT).show();
                                            }

                                        }else if(rs.getString(2).length() == 15){
                                            System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!???????????? ?????? ??????????????????.!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
                                            Alert2.setTextColor(Color.RED);
                                            Alert2.setText("???????????? ?????? ??????????????????.");
                                        }
                                        else if(rs.getString(2).length() == 14){
                                            System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!?????? ????????? ??????????????????.!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
                                            Alert2.setTextColor(Color.RED);
                                            Alert2.setText("?????? ????????? ????????? ?????????.");
                                        }
                                    }
                                }else{
                                    ConnectionResult="Check Connection";
                                }
                            }catch(Exception ex){
                                //System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
                                Toast.makeText(getApplicationContext(), "????????????????????? ?????????????????????.", Toast.LENGTH_SHORT).show();
                            }
                        }


                        else if(SPreInput.startsWith("A") && RgBtn2Click == "1"){
                            SPreInputA = SPreInput;
                            try{  //??????????????? ?????????????????? ??????
                                ConnectionHelper connectionHelper = new ConnectionHelper();
                                connect = connectionHelper.ConnectionClass();
                                if(connect!=null){
                                    //?????? ?????? ???????????? ?????????
                                    String query = "EXEC dbo.SP_PDA_WM00010_CANCEL_BARCODE '" + SPreInputA + "','" + UserId +  "','ko'";
                                    Statement st = connect.createStatement();
                                    ResultSet rs = st.executeQuery(query);

                                    while (rs.next()){

                                        if(rs.getString(2).length() == 8) {
                                            LIST_Barcode_Cancel.add(rs.getString(3));
                                            LIST_Name_Cancel.add(rs.getString(5));
                                            LIST_Number_Cancel.add(rs.getString(6));
                                            Alert1.setText(LIST_Barcode_Cancel.size() + " Scanned");
                                            Alert2.setTextColor(Color.BLACK);
                                            Alert2.setText("?????? ????????? ?????????????????????.");
                                            CustomListBarcodeCancel adapterCancel = new CustomListBarcodeCancel(BarcodeInput.this);
                                            BarcodeCancelListview = (ListView) findViewById(R.id.listview_barcode_cancel);
                                            BarcodeCancelListview.setAdapter(adapterCancel);
                                        }
                                        else if(rs.getString(2).length() == 14){
                                            Alert2.setTextColor(Color.RED);
                                            Alert2.setText("????????? ???????????? ????????????.");
                                        }
                                        else{
                                            Alert2.setTextColor(Color.RED);
                                            Alert2.setText("???????????? ?????? ????????? ?????????.");
                                        }


                                    }
                                }else{
                                    ConnectionResult="Check Connection";
                                }
                            }catch(Exception ex){
                                //Toast.makeText(getApplicationContext(), "????????????????????? ?????????????????????.", Toast.LENGTH_SHORT).show();
                            }   //??????????????? ?????????????????? ?????????
                        }

                }
                return false;
            }
        });




















/*
        //textWatcher ???????????? 1????????? ????????? ????????????
        EtPreInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                //Log.e(TAG, "beforeTextChanged() - charSequence : " + charSequence);


            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                //Log.e(TAG, "onTextChanged() - charSequence : " + charSequence);

            }

            @Override
            public void afterTextChanged(Editable editable) {
                Log.e(TAG, "afterTextChanged() - editable : " + editable.toString());
                System.out.println("*************afterTextChanged*************");
                System.out.println("????????? ?????? ?????? : " + EtPreInput);
                SPreInput = EtPreInput.getText().toString();


                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(EtPreInput.getWindowToken(), 0);


                try {
                    //EditText??? R??? ????????? ??????
                    if(SPreInput.startsWith("R") && SPreInput.length() == 12){   //???????????? R??? ????????? ??????
                        LocationBarcodeInput.setText(SPreInput);
                        SPreInputR = SPreInput;

                        try{  //???????????? ?????????????????? ??????
                            ConnectionHelper connectionHelper = new ConnectionHelper();
                            connect = connectionHelper.ConnectionClass();

                            if(connect!=null){
                                //????????? ?????? ??????
                                String query4 = "EXEC SP_PDA_COMMON_BARCODE_TYPE '" + SPreInputR + "','ko'";
                                Statement st4 = connect.createStatement();
                                ResultSet rs4 = st4.executeQuery(query4);
                                System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!?????????????????? ????????? ?????? ??????!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
                                while (rs4.next()){
                                    //????????? ????????? ?????? ??????
                                    if(rs4.getString(1).isEmpty()!=true) {
                                        LocationBarcodeInput.setText(rs4.getString(5));
                                        LIST_Name_Input.clear();
                                        LIST_Number_Input.clear();
                                        LIST_Barcode_Input.clear();
                                        System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!????????? ????????? ?????? ??????!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
                                        System.out.println(rs4.getString(5));
                                        EtPreInput.setText("");
                                    }
                                }
                            }else{
                                ConnectionResult="Check Connection";
                            }
                        }catch(Exception ex){
                            Toast.makeText(getApplicationContext(), "????????????????????? ?????????????????????.", Toast.LENGTH_SHORT).show();
                        }

                    }


                    //EditText??? A??? ????????????, ?????? ?????? ?????? ON ??????
                    else if(SPreInput.startsWith("A") && SPreInput.length() == 16 && RgBtn1Click == "1"){
                        //Toast.makeText(getApplicationContext(), "A scanned", Toast.LENGTH_SHORT).show();
                        SPreInputA = SPreInput;
                        try{  //??????????????? ?????????????????? ??????
                            ConnectionHelper connectionHelper = new ConnectionHelper();
                            connect = connectionHelper.ConnectionClass();

                            System.out.println(SPreInput);
                            if(connect!=null){
                                //???????????? ???????????? ?????????
                                String query = "EXEC dbo.SP_PDA_WM00010_SAVE_VALIDATION '" + SPreInputR + "','" + SPreInputA + "','" + UserId +  "','ko'";
                                System.out.println("EXEC dbo.SP_PDA_WM00010_SAVE_VALIDATION '" + SPreInputR + "','" + SPreInputA + "','" + UserId +  "','ko'");
                                Statement st = connect.createStatement();
                                ResultSet rs = st.executeQuery(query);

                                while (rs.next()){
                                    System.out.println(rs.getString(2) + "/////////////////////////////////////////");
                                    //????????? ????????? ?????? ??????
                                    if(rs.getString(2).isEmpty()==true) {
                                        try{
                                            if(connect!=null){  //?????????????????? ?????? ???????????? ??????(TB_WM_BCOD)
                                                String query3 = "EXEC dbo.SP_PDA_WM00010_SAVE '" + SPreInputR + "','" + SPreInputA + "','" + UserId +  "','ko'";
                                                Statement st3 = connect.createStatement();
                                                ResultSet rs3 = st3.executeQuery(query3);

                                                while (rs3.next()){
                                                    System.out.println("????????? : " + rs3.getString(5).toString().length());
                                                    if(rs3.getString(1).isEmpty()!=true) {
                                                        LIST_Barcode_Input.add(rs3.getString(1));
                                                        LIST_Name_Input.add(rs3.getString(3));
                                                        LIST_Number_Input.add(rs3.getString(4));
                                                        Alert1.setText(LIST_Barcode_Input.size() + " Scanned");
                                                        Alert2.setTextColor(Color.BLACK);
                                                        Alert2.setText("?????? ????????? ?????????????????????.");
                                                        CustomListBarcodeInput adapterInput = new CustomListBarcodeInput(BarcodeInput.this);
                                                        BarcodeInputListview = (ListView) findViewById(R.id.listview_barcode_input);
                                                        BarcodeInputListview.setAdapter(adapterInput);
                                                    }

                                                }
                                            }else{
                                                ConnectionResult="Check Connection";
                                            }
                                        }catch(Exception e){
                                            //Toast.makeText(getApplicationContext(), "????????????????????? ?????????????????????.", Toast.LENGTH_SHORT).show();
                                        }

                                    }else if(rs.getString(2).length() == 15){
                                        System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!???????????? ?????? ??????????????????.!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
                                        Alert2.setTextColor(Color.RED);
                                        Alert2.setText("???????????? ?????? ??????????????????.");
                                    }
                                    else if(rs.getString(2).length() == 14){
                                        System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!?????? ????????? ??????????????????.!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
                                        Alert2.setTextColor(Color.RED);
                                        Alert2.setText("?????? ????????? ????????? ?????????.");
                                    }
                                }
                            }else{
                                ConnectionResult="Check Connection";
                            }
                        }catch(Exception ex){
                            //System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
                            Toast.makeText(getApplicationContext(), "????????????????????? ?????????????????????.", Toast.LENGTH_SHORT).show();
                            }
                        }   //??????????????? ?????????????????? ?????????


                    //EditText??? A??? ????????????, ?????? ?????? ?????? ON ??????
                    else if(SPreInput.startsWith("A") && SPreInput.length() == 16 && RgBtn2Click == "1"){
                        //Toast.makeText(getApplicationContext(), "A scanned", Toast.LENGTH_SHORT).show();
                        SPreInputA = SPreInput;
                        try{  //??????????????? ?????????????????? ??????
                            ConnectionHelper connectionHelper = new ConnectionHelper();
                            connect = connectionHelper.ConnectionClass();
                            if(connect!=null){
                                //?????? ?????? ???????????? ?????????
                                String query = "EXEC dbo.SP_PDA_WM00010_CANCEL_BARCODE '" + SPreInputA + "','" + UserId +  "','ko'";
                                Statement st = connect.createStatement();
                                ResultSet rs = st.executeQuery(query);

                                while (rs.next()){

                                    if(rs.getString(2).length() == 8) {
                                        LIST_Barcode_Cancel.add(rs.getString(3));
                                        LIST_Name_Cancel.add(rs.getString(5));
                                        LIST_Number_Cancel.add(rs.getString(6));
                                        Alert1.setText(LIST_Barcode_Cancel.size() + " Scanned");
                                        Alert2.setTextColor(Color.BLACK);
                                        Alert2.setText("?????? ????????? ?????????????????????.");
                                        CustomListBarcodeCancel adapterCancel = new CustomListBarcodeCancel(BarcodeInput.this);
                                        BarcodeCancelListview = (ListView) findViewById(R.id.listview_barcode_cancel);
                                        BarcodeCancelListview.setAdapter(adapterCancel);
                                    }
                                    else if(rs.getString(2).length() == 14){
                                        Alert2.setTextColor(Color.RED);
                                        Alert2.setText("????????? ???????????? ????????????.");
                                    }
                                    else{
                                        Alert2.setTextColor(Color.RED);
                                        Alert2.setText("???????????? ?????? ????????? ?????????.");
                                    }


                                }
                            }else{
                                ConnectionResult="Check Connection";
                            }
                        }catch(Exception ex){
                            //Toast.makeText(getApplicationContext(), "????????????????????? ?????????????????????.", Toast.LENGTH_SHORT).show();
                        }   //??????????????? ?????????????????? ?????????
                    }
                    else if(LocationBarcodeInput == null){
                    }
                } catch (Exception e) {
                }

            }
        });



 */










        //LocationBarcodeInput ????????? ??? setFilters
        LocationBarcodeInput.setFilters(new InputFilter[]{new InputFilter() {
            @Override
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                if(LocationBarcodeInput != null && LocationBarcodeInput.length() > 1){
                    //String SPreInput??? ????????? (????????? ??????????????? ??????????????? ?????????)
                    SPreInput.replace(SPreInput, "");
                    System.out.println(SPreInput);
                }
              return source;
            }
        },new InputFilter.LengthFilter(20)});


        //????????? ?????? ??????
        btn_input.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                //Alert1 ?????????
                Alert1.setText("0 Scanned");
                //?????? ?????? ?????????????????? ?????? ?????????, ???????????? ????????????
                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    btn_input.setBackgroundColor(Color.LTGRAY);
                }else if(event.getAction() == MotionEvent.ACTION_UP) {
                    btn_input.setBackgroundColor(R.drawable.colors);
                    btn_cancel.setBackgroundColor(Color.LTGRAY);

                }
                return false;
            }
        });
        btn_input.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Alert2.setTextColor(Color.BLACK);
                Alert2.setText("??????????????? ???????????????");
                //????????? ????????? ????????? ????????? ??????(????????? ?????????????????? "1", ????????????????????? "0")
                RgBtn1Click = "1";
                RgBtn2Click = "0";
                //???????????? ???????????? ?????????
                LIST_Name_Cancel.clear();
                LIST_Number_Cancel.clear();
                LIST_Barcode_Cancel.clear();
                CustomListBarcodeCancel adapterCancel = new CustomListBarcodeCancel(BarcodeInput.this);
                BarcodeCancelListview = (ListView) findViewById(R.id.listview_barcode_cancel);
                BarcodeCancelListview.setAdapter(adapterCancel);

            }
        });




        //????????? ?????? ??????
        btn_cancel.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                //?????? ?????? ?????????????????? ?????? ?????????, ???????????? ????????????
                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    btn_cancel.setBackgroundColor(Color.LTGRAY);
                }else if(event.getAction() == MotionEvent.ACTION_UP) {
                    btn_cancel.setBackgroundColor(R.drawable.colors);
                    btn_input.setBackgroundColor(Color.LTGRAY);

                }
                return false;
            }
        });
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Alert2.setTextColor(Color.BLACK);
                Alert2.setText("????????? ?????? ???????????? ???????????????");
                //????????? ????????? ????????? ????????? ??????(????????? ?????????????????? "1", ????????????????????? "0")
                RgBtn1Click = "0";
                RgBtn2Click = "1";
                //???????????? ???????????? ?????????
                LIST_Name_Input.clear();
                LIST_Number_Input.clear();
                LIST_Barcode_Input.clear();
                LocationBarcodeInput.setText("");
                CustomListBarcodeInput adapterInput = new CustomListBarcodeInput(BarcodeInput.this);
                BarcodeInputListview = (ListView) findViewById(R.id.listview_barcode_input);
                BarcodeInputListview.setAdapter(adapterInput);
            }
        });



        back = findViewById(R.id.back);
        logout = findViewById(R.id.logout);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(BarcodeInput.this, MainActivity.class);
                startActivity(intent);
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(BarcodeInput.this, PopupActivity.class);
                startActivity(intent);
            }
        });

    }


    //??????????????? ?????? ?????????
    public class CustomListBarcodeInput extends ArrayAdapter<String> {
        private final Activity context;

        public CustomListBarcodeInput(Activity context) {
            super(context, R.layout.barcode_input_listview_input, LIST_Name_Input);
            this.context = context;
            //???????????? ?????????, ???????????? ??????
            BarcodeInputListview.setVisibility(View.VISIBLE);
            BarcodeCancelListview.setVisibility(View.INVISIBLE);
        }

        @Override
        public View getView(int position, View view, ViewGroup parent) {
            LayoutInflater inflater = context.getLayoutInflater();
            View rowView = inflater.inflate(R.layout.barcode_input_listview_input, null, true);
            TextView ItemName = (TextView) rowView.findViewById(R.id.input_item_name);
            TextView ItemNumber = (TextView) rowView.findViewById(R.id.input_item_number);
            TextView ItemBarcode = (TextView) rowView.findViewById(R.id.input_item_barcode);
            //Button approve = (Button) rowView.findViewById(R.id.approve_user);
            ItemName.setText(LIST_Name_Input.get(position).toString());
            ItemNumber.setText(LIST_Number_Input.get(position).toString());
            ItemBarcode.setText(LIST_Barcode_Input.get(position).toString());
            //approve.setText("??????");

            return rowView;
        }

    }


    //??????????????? ?????? ?????????
    public class CustomListBarcodeCancel extends ArrayAdapter<String> {
        private final Activity context;

        public CustomListBarcodeCancel(Activity context) {
            super(context, R.layout.barcode_input_listview_cancel, LIST_Name_Cancel);
            this.context = context;
            //???????????? ?????????, ???????????? ??????
            BarcodeInputListview.setVisibility(View.INVISIBLE);
            BarcodeCancelListview.setVisibility(View.VISIBLE);
        }

        @Override
        public View getView(int position, View view, ViewGroup parent) {
            LayoutInflater inflater = context.getLayoutInflater();
            View rowView = inflater.inflate(R.layout.barcode_input_listview_cancel, null, true);
            TextView ItemName = (TextView) rowView.findViewById(R.id.cancel_item_name);
            TextView ItemNumber = (TextView) rowView.findViewById(R.id.cancel_item_number);
            TextView ItemBarcode = (TextView) rowView.findViewById(R.id.cancel_item_barcode);
            //Button approve = (Button) rowView.findViewById(R.id.approve_user);
            ItemName.setText(LIST_Name_Cancel.get(position).toString());
            ItemNumber.setText(LIST_Number_Cancel.get(position).toString());
            ItemBarcode.setText(LIST_Barcode_Cancel.get(position).toString());
            //approve.setText("??????");

            return rowView;
        }

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
            }
            @Override
            public void onBarcode(String barcode, String codeType) {
                Log.i("ScannerTest2", "result=" + barcode);
                //mTvResult.setText(barcode);
            }
        };
        mManager.addListener(mListener);

    }


};