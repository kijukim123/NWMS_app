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
    private String  SPreInput;

    Connection connect;              //database Connect
    String ConnectionResult = "";


    ArrayList<String> array_bar = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.barcode_input_main);



        rg_btn1 = findViewById(R.id.rg_btn1);
        rg_btn2 = findViewById(R.id.rg_btn2);

        EtPreInput = findViewById(R.id.et_pre_input);
        LocationBarcodeInput = findViewById(R.id.tv_barcode_location);

        BarcodeInputListview = (ListView) findViewById(R.id.listview_barcode_input);
        BarcodeCancelListview = (ListView) findViewById(R.id.listview_barcode_cancel);

        //프레임레이아웃에서 리스트뷰 비활성화 상태로 초기화
        BarcodeInputListview.setVisibility(View.INVISIBLE);
        BarcodeCancelListview.setVisibility(View.INVISIBLE);


        //리스트뷰 임시값
        LIST_Name_Input.add("1번");
        LIST_Name_Input.add("2번");
        LIST_Name_Input.add("3번");
        LIST_Number_Input.add("3개");
        LIST_Number_Input.add("2개");
        LIST_Number_Input.add("1개");
        LIST_Barcode_Input.add("A123456789");
        LIST_Barcode_Input.add("A123456888");
        LIST_Barcode_Input.add("A123456777");
        LIST_Name_Cancel.add("4번");
        LIST_Name_Cancel.add("5번");
        LIST_Number_Cancel.add("5개");
        LIST_Number_Cancel.add("6개");
        LIST_Barcode_Cancel.add("A555555555");
        LIST_Barcode_Cancel.add("A666666666");




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
                    System.out.println("EtPreInput:::::::" + EtPreInput);
                }
                return "";
            }
        },new InputFilter.LengthFilter(20)});

        EtPreInput.addTextChangedListener(new TextWatcher() {
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
                System.out.println("바코드 입력 감지 : " + EtPreInput);
                SPreInput = EtPreInput.getText().toString();


                try {
                    if(SPreInput.startsWith("R")){   //문자열이 R로 시작할 경우
                        LocationBarcodeInput.setText(SPreInput);
                    }
                    else if(SPreInput.startsWith("A")){
                        Toast.makeText(getApplicationContext(), "A scanned", Toast.LENGTH_SHORT).show();
                    }
                    else if(LocationBarcodeInput != null){
                        //Toast.makeText(getApplicationContext(), "렉이 스캔되지 않았습니다..", Toast.LENGTH_SHORT).show();
                        //EtPreInput.setText(null);
                        //SPreInput.replace(SPreInput, "");
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
                    SPreInput.replace(SPreInput, "");
                    System.out.println(SPreInput);
                }
              return source;
            }
        },new InputFilter.LengthFilter(20)});


        //바코드 입고 버튼
        rg_btn1.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                //버튼 클릭 이벤트시에만 색깔 나오고, 아닐경우 회색표시
                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    rg_btn1.setBackgroundColor(Color.LTGRAY);
                    //rg_btn2.setBackgroundColor(Color.BLUE);
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
                CustomListBarcodeInput adapter = new CustomListBarcodeInput(BarcodeInput.this);
                BarcodeInputListview = (ListView) findViewById(R.id.listview_barcode_input);
                BarcodeInputListview.setAdapter(adapter);

                /* 데이터베이스에서 값 불러와서 리스트뷰에 넣기
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
                */
            }
        });




        //바코드 출고 버튼
        rg_btn2.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                //버튼 클릭 이벤트시에만 색깔 나오고, 아닐경우 회색표시
                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    rg_btn2.setBackgroundColor(Color.LTGRAY);
                    //rg_btn1.setBackgroundColor(Color.BLUE);
                }else if(event.getAction() == MotionEvent.ACTION_UP) {
                    //rg_btn2.setBackgroundColor(Color.BLUE);
                    rg_btn2.setBackgroundColor(R.drawable.colors);
                    rg_btn1.setBackgroundColor(Color.LTGRAY);
                }
                return false;
            }
        });
        rg_btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CustomListBarcodeCancel adapter = new CustomListBarcodeCancel(BarcodeInput.this);
                BarcodeCancelListview = (ListView) findViewById(R.id.listview_barcode_cancel);
                BarcodeCancelListview.setAdapter(adapter);
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