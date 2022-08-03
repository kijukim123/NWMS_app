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
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class BarcodeMove extends AppCompatActivity {

    private ImageButton back;
    private ImageButton logout;
    private TextView TvMoveLocation;
    private EditText EtPreMove;
    private Button btnReset;
    private List<String> LIST_Name_Move = new ArrayList<>();
    private List<String> LIST_Number_Move = new ArrayList<>();
    private List<String> LIST_Barcode_Move = new ArrayList<>();
    private String SPreInput;
    private String SPreInputR;
    private String SPreInputA;
    private Context mContext;
    private String UserId;
    private TextView Alert1;
    private TextView Alert2;
    private ListView BarcodeMoveListview;

    Connection connect;              //database Connect
    String ConnectionResult = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.barcode_move);

        //쿠키에서 UserId 값 받아오기
        mContext = this;
        UserId = PreferenceManager.getString(mContext, "UID");
        System.out.println(UserId + "///////////////////////////////////////////////////////////////");

        Alert1 = findViewById(R.id.barcode_move_alert1);
        Alert2 = findViewById(R.id.barcode_move_alert2);

        back = findViewById(R.id.back);
        logout = findViewById(R.id.logout);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), PopupActivity.class);
                startActivity(intent);
            }
        });

        TvMoveLocation = findViewById(R.id.tv_move_location);
        EtPreMove = findViewById(R.id.et_pre_move);
        btnReset = findViewById(R.id.button_move);

        //editText (한글,영어,숫자)만 받기
        EtPreMove.setFilters(new InputFilter[]{new InputFilter() {
            @Override
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                Pattern ps = Pattern.compile("^[a-zA-Z0-9가-힣ㄱ-ㅎㅏ-ㅣ\\u318D\\u119E\\u11A2\\u2022\\u2025a\\u00B7\\uFE55]+$");
                if (source.equals("") || ps.matcher(source).matches()) {
                    return source;
                }
                else if(TvMoveLocation != null && TvMoveLocation.length() >10){
                    //랙위치 텍스트뷰에 유효한 값이 있으면 EditText 초기화
                    //EtPreMove.setText(null);
                    //EtPreMove.setText("");
                }
                else if(SPreInputA != null && SPreInputA.length() >10){
                    //랙위치 텍스트뷰에 유효한 값이 있으면 EditText 초기화
                    //EtPreMove.setText(null);
                    //EtPreMove.setText("");
                }
                return "";
            }
        },new InputFilter.LengthFilter(20)});


        //엔터키 방식
        EtPreMove.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                switch(keyCode){
                    case KeyEvent.KEYCODE_ENTER:
                        SPreInput = EtPreMove.getText().toString();
                        System.out.println("!!!!!!!!!!!!!!!!!!!EnterKeyOn!!!!!!!!!!!!!!!!!!!!");
                        System.out.println(SPreInput);


                        if(SPreInput.startsWith("R")){
                            TvMoveLocation.setText(SPreInput);
                            SPreInputR = SPreInput;

                            try{  //렉바코드 데이터베이스 조회
                                ConnectionHelper connectionHelper = new ConnectionHelper();
                                connect = connectionHelper.ConnectionClass();

                                if(connect!=null){
                                    //바코드 타입 조회
                                    String query4 = "EXEC SP_PDA_COMMON_BARCODE_TYPE '" + SPreInputR + "','ko'";
                                    Statement st4 = connect.createStatement();
                                    ResultSet rs4 = st4.executeQuery(query4);
                                    System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!데이터베이스 연결만 됐을 경우!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
                                    while (rs4.next()){
                                        //조회가 제대로 됐을 경우
                                        if(rs4.getString(1).isEmpty()!=true) {
                                            TvMoveLocation.setText(rs4.getString(5));
                                            LIST_Name_Move.clear();
                                            LIST_Number_Move.clear();
                                            LIST_Barcode_Move.clear();
                                            System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!조회가 제대로 됐을 경우!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
                                            System.out.println(rs4.getString(5));
                                            EtPreMove.setText("");
                                        }
                                    }
                                }else{
                                    ConnectionResult="Check Connection";
                                }
                            }catch(Exception ex){
                                Toast.makeText(getApplicationContext(), "데이터베이스에 안들어갔습니다.", Toast.LENGTH_SHORT).show();
                            }
                        }


                        else if(SPreInput.startsWith("A")){
                            SPreInputA = SPreInput;
                            try{  //상품바코드 데이터베이스 추가
                                ConnectionHelper connectionHelper = new ConnectionHelper();
                                connect = connectionHelper.ConnectionClass();

                                System.out.println(SPreInput);
                                if(connect!=null){
                                    //입고처리 프로시저 돌리기
                                    String query = "EXEC dbo.SP_PDA_WM00010_SAVE_VALIDATION '" + SPreInputR + "','" + SPreInputA + "','" + UserId +  "','ko'";
                                    System.out.println("EXEC dbo.SP_PDA_WM00010_SAVE_VALIDATION '" + SPreInputR + "','" + SPreInputA + "','" + UserId +  "','ko'");
                                    Statement st = connect.createStatement();
                                    ResultSet rs = st.executeQuery(query);

                                    while (rs.next()){
                                        System.out.println(rs.getString(2) + "/////////////////////////////////////////");
                                        //입고가 제대로 됐을 경우
                                        if(rs.getString(2).isEmpty()==true) {
                                            try{
                                                if(connect!=null){  //데이터베이스 저장 프로시저 실행(TB_WM_BCOD)
                                                    String query3 = "EXEC dbo.SP_PDA_WM00010_SAVE '" + SPreInputR + "','" + SPreInputA + "','" + UserId +  "','ko'";
                                                    Statement st3 = connect.createStatement();
                                                    ResultSet rs3 = st3.executeQuery(query3);

                                                    while (rs3.next()){
                                                        System.out.println("반환값 : " + rs3.getString(5).toString().length());
                                                        if(rs3.getString(1).isEmpty()!=true) {
                                                            LIST_Barcode_Move.add(rs3.getString(1));
                                                            LIST_Name_Move.add(rs3.getString(3));
                                                            LIST_Number_Move.add(rs3.getString(4));
                                                            Alert1.setText(LIST_Barcode_Move.size() + " Scanned");
                                                            Alert2.setTextColor(Color.BLACK);
                                                            Alert2.setText("입고 처리가 완료되었습니다.");
                                                            BarcodeMove.CustomListBarcodeMove adapterInput = new BarcodeMove.CustomListBarcodeMove(BarcodeMove.this);
                                                            BarcodeMoveListview = (ListView) findViewById(R.id.listview_barcode_move);
                                                            BarcodeMoveListview.setAdapter(adapterInput);
                                                        }

                                                    }
                                                }else{
                                                    ConnectionResult="Check Connection";
                                                }
                                            }catch(Exception e){
                                                //Toast.makeText(getApplicationContext(), "데이터베이스에 안들어갔습니다.", Toast.LENGTH_SHORT).show();
                                            }

                                        }else if(rs.getString(2).length() == 15){
                                            System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!존재하지 않는 바코드입니다.!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
                                            Alert2.setTextColor(Color.RED);
                                            Alert2.setText("존재하지 않는 바코드입니다.");
                                        }
                                        else if(rs.getString(2).length() == 14){
                                            System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!이미 입고된 바코드입니다.!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
                                            Alert2.setTextColor(Color.RED);
                                            Alert2.setText("이미 입고된 바코드 입니다.");
                                        }
                                    }
                                }else{
                                    ConnectionResult="Check Connection";
                                }
                            }catch(Exception ex){
                                //System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
                                Toast.makeText(getApplicationContext(), "데이터베이스에 안들어갔습니다.", Toast.LENGTH_SHORT).show();
                            }
                        }



                }
                return false;
            }
        });



    }


    //바코드입고 관련 어뎁터
    public class CustomListBarcodeMove extends ArrayAdapter<String> {
        private final Activity context;

        public CustomListBarcodeMove(Activity context) {
            super(context, R.layout.barcode_move_listview, LIST_Name_Move);
            this.context = context;
        }

        @Override
        public View getView(int position, View view, ViewGroup parent) {
            LayoutInflater inflater = context.getLayoutInflater();
            View rowView = inflater.inflate(R.layout.barcode_move_listview, null, true);
            TextView ItemName = (TextView) rowView.findViewById(R.id.move_item_name);
            TextView ItemNumber = (TextView) rowView.findViewById(R.id.move_item_number);
            TextView ItemBarcode = (TextView) rowView.findViewById(R.id.move_item_barcode);
            //Button approve = (Button) rowView.findViewById(R.id.approve_user);
            ItemName.setText(LIST_Name_Move.get(position).toString());
            ItemNumber.setText(LIST_Number_Move.get(position).toString());
            ItemBarcode.setText(LIST_Barcode_Move.get(position).toString());
            //approve.setText("승인");

            return rowView;
        }

    }

}


