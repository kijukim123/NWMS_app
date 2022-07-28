package com.nsoft.wms_app;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
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

    private Button rg_btn1;
    private Button rg_btn2;
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
    private String SPreInputR;
    private String SPreInputA;
    private String RgBtn1Click;
    private String RgBtn2Click;
    private TextView Alert1;
    private TextView Alert2;

    Connection connect;              //database Connect
    String ConnectionResult = "";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.barcode_input_main);

        Alert1 = findViewById(R.id.barcode_input_alert1);
        Alert2 = findViewById(R.id.barcode_input_alert2);

        rg_btn1 = findViewById(R.id.rg_btn1);
        rg_btn2 = findViewById(R.id.rg_btn2);
        ResetButton = findViewById(R.id.button);

        EtPreInput = findViewById(R.id.et_pre_input);
        LocationBarcodeInput = findViewById(R.id.tv_barcode_location);

        BarcodeInputListview = (ListView) findViewById(R.id.listview_barcode_input);
        BarcodeCancelListview = (ListView) findViewById(R.id.listview_barcode_cancel);

        //프레임레이아웃에서 리스트뷰 비활성화 상태로 초기화
        BarcodeInputListview.setVisibility(View.INVISIBLE);
        BarcodeCancelListview.setVisibility(View.INVISIBLE);

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

            }
        });



        //editText (한글,영어,숫자)만 받기
        EtPreInput.setFilters(new InputFilter[]{new InputFilter() {
            @Override
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                Pattern ps = Pattern.compile("^[a-zA-Z0-9가-힣ㄱ-ㅎㅏ-ㅣ\\u318D\\u119E\\u11A2\\u2022\\u2025a\\u00B7\\uFE55]+$");
                if (source.equals("") || ps.matcher(source).matches()) {
                    return source;
                }
                else if(LocationBarcodeInput != null && LocationBarcodeInput.length() >10){
                    //랙위치 텍스트뷰에 유효한 값이 있으면 EditText 초기화
                    EtPreInput.setText(null);
                    EtPreInput.setText("");
                    //SPreInputR.replace(SPreInputR, "");
                    SPreInputA.replace(SPreInputA, "");
                    System.out.println("EtPreInput:::::::" + EtPreInput);
                }
                else if(SPreInputA != null && SPreInputA.length() >10){
                    //랙위치 텍스트뷰에 유효한 값이 있으면 EditText 초기화
                    EtPreInput.setText(null);
                    EtPreInput.setText("");
                    System.out.println("EtPreInput:::::::" + EtPreInput);
                }
                return "";
            }
        },new InputFilter.LengthFilter(20)});

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
                System.out.println("바코드 입력 감지 : " + EtPreInput);
                SPreInputR = EtPreInput.getText().toString();
                SPreInputA = EtPreInput.getText().toString();


                try {  //EditText가 R로 시작할 경우
                    if(SPreInputR.startsWith("R") && SPreInputR.length() == 12){   //문자열이 R로 시작할 경우

                        LocationBarcodeInput.setText(SPreInputR);


                    }
                    else if(SPreInputR.startsWith("A") && SPreInputA.length() == 16 && RgBtn1Click == "1"){  //EditText가 A로 시작하고, 입고 취소 버튼 ON 일때
                        //Toast.makeText(getApplicationContext(), "A scanned", Toast.LENGTH_SHORT).show();
                        SPreInputR = LocationBarcodeInput.getText().toString();
                        try{  //상품바코드 데이터베이스 추가
                            ConnectionHelper connectionHelper = new ConnectionHelper();
                            connect = connectionHelper.ConnectionClass();
                            System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
                            System.out.println(SPreInputR);
                            System.out.println(SPreInputA);
                            if(connect!=null){
                                //입고처리 프로시저 돌리기
                                String query = "EXEC dbo.SP_PDA_WM00010_SAVE '" + SPreInputR + "','" + SPreInputA + "','kjkim','ko'";
                                Statement st = connect.createStatement();
                                ResultSet rs = st.executeQuery(query);

                                while (rs.next()){
                                    //입고가 제대로 됐을 경우
                                    if(rs.getString(1).isEmpty()!=true) {
                                        LIST_Barcode_Input.add(rs.getString(1));
                                        LIST_Name_Input.add(rs.getString(3));
                                        LIST_Number_Input.add(rs.getString(4));
                                        Alert1.setText(LIST_Barcode_Input.size() + " Scanned");
                                        CustomListBarcodeInput adapterInput = new CustomListBarcodeInput(BarcodeInput.this);
                                        BarcodeInputListview = (ListView) findViewById(R.id.listview_barcode_input);
                                        BarcodeInputListview.setAdapter(adapterInput);
                                    }
                                    /*
                                    //입고가 제대로 안됐을 경우
                                    else if(rs.getString(5) != "완료되었습니다."){
                                        //데이터베이스 탐색(TB_BM_BCOD)
                                        if(){   //BM바코드에 있을 경우
                                            //데이터베이스 탐색(TB_WM_BCOD)후 값이 이미 있다면 이미 있는 값이라고 알림
                                        }else if(){   //BM바코드에 없을 경우
                                            //Alert2에 등록되지 않은 바코드라고 알림
                                        }
                                    }*/
                                }
                            }else{
                                ConnectionResult="Check Connection";
                            }
                        }catch(Exception ex){
                            Toast.makeText(getApplicationContext(), "데이터베이스에 안들어갔습니다.", Toast.LENGTH_SHORT).show();
                        }   //상품바코드 데이터베이스 추가끝
                    }
                    else if(SPreInputR.startsWith("A") && SPreInputA.length() == 16 && RgBtn2Click == "1"){  //EditText가 A로 시작하고, 입고 취소 버튼 ON 일때
                        //Toast.makeText(getApplicationContext(), "A scanned", Toast.LENGTH_SHORT).show();
                        SPreInputR = LocationBarcodeInput.getText().toString();
                        try{  //상품바코드 데이터베이스 추가
                            ConnectionHelper connectionHelper = new ConnectionHelper();
                            connect = connectionHelper.ConnectionClass();
                            if(connect!=null){
                                //입고 취소 프로시저 돌리기
                                String query = "EXEC dbo.SP_PDA_WM00010_CANCEL_BARCODE '" + SPreInputA + "','kjkim','ko'";
                                Statement st = connect.createStatement();
                                ResultSet rs = st.executeQuery(query);

                                while (rs.next()){
                                    if(rs.getString(1).isEmpty()!=true) {
                                        LIST_Barcode_Cancel.add(rs.getString(3));
                                        LIST_Name_Cancel.add(rs.getString(5));
                                        LIST_Number_Cancel.add(rs.getString(6));
                                        Alert1.setText(LIST_Barcode_Cancel.size() + " Scanned");
                                        CustomListBarcodeCancel adapterCancel = new CustomListBarcodeCancel(BarcodeInput.this);
                                        BarcodeCancelListview = (ListView) findViewById(R.id.listview_barcode_cancel);
                                        BarcodeCancelListview.setAdapter(adapterCancel);
                                    }
                                }
                            }else{
                                ConnectionResult="Check Connection";
                            }
                        }catch(Exception ex){
                            //Toast.makeText(getApplicationContext(), "데이터베이스에 안들어갔습니다.", Toast.LENGTH_SHORT).show();
                        }   //상품바코드 데이터베이스 추가끝
                    }
                    else if(LocationBarcodeInput == null){
                    }
                } catch (Exception e) {
                }

            }
        });


        //LocationBarcodeInput 텍스트 뷰 setFilters
        LocationBarcodeInput.setFilters(new InputFilter[]{new InputFilter() {
            @Override
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                if(LocationBarcodeInput != null && LocationBarcodeInput.length() > 10){
                    //String SPreInput을 초기화 (랙위치 텍스트뷰에 랙위치값이 있으면)
                    SPreInputR.replace(SPreInputR, "");
                    System.out.println(SPreInputR);
                }
              return source;
            }
        },new InputFilter.LengthFilter(20)});


        //바코드 입고 버튼
        rg_btn1.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                //Alert1 초기화
                Alert1.setText("0 Scanned");
                //버튼 클릭 이벤트시에만 색깔 나오고, 아닐경우 회색표시
                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    rg_btn1.setBackgroundColor(Color.LTGRAY);
                }else if(event.getAction() == MotionEvent.ACTION_UP) {
                    rg_btn1.setBackgroundColor(R.drawable.colors);
                    rg_btn2.setBackgroundColor(Color.LTGRAY);

                }
                return false;
            }
        });
        rg_btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //CustomListBarcodeInput adapterInput = new CustomListBarcodeInput(BarcodeInput.this);
                //BarcodeInputListview = (ListView) findViewById(R.id.listview_barcode_input);
                //BarcodeInputListview.setAdapter(adapterInput);
                //버튼이 눌러져 있는지 아닌지 표시(버튼이 눌러져있으면 "1", 안눌러져있으면 "0")
                //RgBtn1Click.replace(RgBtn1Click, "1");
                RgBtn1Click = "1";
                RgBtn2Click = "0";
                //입고취소 리스트뷰 클리어
                LIST_Name_Cancel.clear();
                LIST_Number_Cancel.clear();
                LIST_Barcode_Cancel.clear();
                CustomListBarcodeCancel adapterCancel = new CustomListBarcodeCancel(BarcodeInput.this);
                BarcodeCancelListview = (ListView) findViewById(R.id.listview_barcode_cancel);
                BarcodeCancelListview.setAdapter(adapterCancel);

            }
        });




        //바코드 출고 버튼
        rg_btn2.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                //버튼 클릭 이벤트시에만 색깔 나오고, 아닐경우 회색표시
                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    rg_btn2.setBackgroundColor(Color.LTGRAY);
                }else if(event.getAction() == MotionEvent.ACTION_UP) {
                    rg_btn2.setBackgroundColor(R.drawable.colors);
                    rg_btn1.setBackgroundColor(Color.LTGRAY);

                }
                return false;
            }
        });
        rg_btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //CustomListBarcodeCancel adapterCancel = new CustomListBarcodeCancel(BarcodeInput.this);
                //BarcodeCancelListview = (ListView) findViewById(R.id.listview_barcode_cancel);
                //BarcodeCancelListview.setAdapter(adapterCancel);
                //버튼이 눌러져 있는지 아닌지 표시(버튼이 눌러져있으면 "1", 안눌러져있으면 "0")
                RgBtn1Click = "0";
                RgBtn2Click = "1";
                //입고처리 리스트뷰 클리어
                LIST_Name_Input.clear();
                LIST_Number_Input.clear();
                LIST_Barcode_Input.clear();
                CustomListBarcodeInput adapterInput = new CustomListBarcodeInput(BarcodeInput.this);
                BarcodeInputListview = (ListView) findViewById(R.id.listview_barcode_input);
                BarcodeInputListview.setAdapter(adapterInput);
            }
        });



        back = findViewById(R.id.back1);
        logout = findViewById(R.id.logout1);

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


    //바코드입고 관련 어뎁터
    public class CustomListBarcodeInput extends ArrayAdapter<String> {
        private final Activity context;

        public CustomListBarcodeInput(Activity context) {
            super(context, R.layout.barcode_input_listview_input, LIST_Name_Input);
            this.context = context;
            //리스트뷰 활성화, 비활성화 설정
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
            //approve.setText("승인");

            return rowView;
        }

    }


    //바코드취소 관련 어뎁터
    public class CustomListBarcodeCancel extends ArrayAdapter<String> {
        private final Activity context;

        public CustomListBarcodeCancel(Activity context) {
            super(context, R.layout.barcode_input_listview_cancel, LIST_Name_Cancel);
            this.context = context;
            //리스트뷰 활성화, 비활성화 설정
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
            //approve.setText("승인");

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