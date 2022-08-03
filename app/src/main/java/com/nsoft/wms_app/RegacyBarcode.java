package com.nsoft.wms_app;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
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
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import org.w3c.dom.Text;

import java.security.Key;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.regex.Pattern;

public class RegacyBarcode extends BaseActivity {
    private InputMethodManager inputMethodManager;
    private String sregacy, spname,pnum;
    private String a=null;
    private Statement r_st = connectDB();
    private ResultSet r_rs;


    //리스트뷰, 리스트뷰 어뎁터 초기화
    private ListView listview;
    private ListAdapter2 listViewAdapter;


    private String StrPreInput;
    private String StrSave;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regacy_barcode);


        LinearLayout focus = findViewById(R.id.focus);
        EditText hidden = findViewById(R.id.hidden);
        TextView pname = findViewById(R.id.pname);
        TextView regacy = findViewById(R.id.regacy);
        TextView notice = findViewById(R.id.notice);
        EditText amount = findViewById(R.id.amount);
        Button btn_store = findViewById(R.id.btn_store);
        Button btn_clear = findViewById(R.id.btn_clear);
        ImageButton search = findViewById(R.id.search);

        listview = findViewById(R.id.listview);
        listViewAdapter = new ListAdapter2();

        //InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);


        /*********뒤로가기, 로그아웃 **********/
        //String Email = ((MainActivity) MainActivity.context).Email;  //사용자 아이디
        ImageButton back = findViewById(R.id.back);
        ImageButton logout = findViewById(R.id.logout);
        //mainbtn(back, logout, Email);

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



        /********* 팝업에서 선택한 품명 표시 ***********/
        Intent intent = getIntent();
        //spname = intent.getStringExtra("str1");
        //pnum = intent.getStringExtra("str2");
        //sregacy = intent.getStringExtra("str3");

        //regacy.setText(sregacy);
        //pname.setText(spname);

        if(notice.getText().toString().equals("")){
            notice.setText("바코드를 스캔하세요.");
        }

        /********바코드 ************/

        //hidden.setTextIsSelectable(true);
        //hidden.setShowSoftInputOnFocus(false);



        //editText (한글,영어,숫자)만 받기
        hidden.setFilters(new InputFilter[]{new InputFilter() {
            @Override
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                Pattern ps = Pattern.compile("^[a-zA-Z0-9가-힣ㄱ-ㅎㅏ-ㅣ\\u318D\\u119E\\u11A2\\u2022\\u2025a\\u00B7\\uFE55]+$");
                if (source.equals("") || ps.matcher(source).matches()) {
                    return source;
                }
                else if(regacy != null && regacy.length() >10){
                    //랙위치 텍스트뷰에 유효한 값이 있으면 EditText 초기화
                    //hidden.setText(null);
                    //hidden.setText("");
                    //SPreInputR.replace(SPreInputR, "");
                    //StrSave.replace(StrSave, "");
                    //System.out.println("EtPreInput:::::::" + hidden);
                }
                else if(StrSave != null && StrSave.length() >10){
                    //랙위치 텍스트뷰에 유효한 값이 있으면 EditText 초기화
                    //hidden.setText(null);
                    //hidden.setText("");
                    //System.out.println("EtPreInput:::::::" + hidden);
                }
                return "";
            }
        },new InputFilter.LengthFilter(20)});





        /*
        hidden.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                hidden.setText("");
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

         */








        hidden.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                switch (keyCode) {
                    case KeyEvent.KEYCODE_ENTER:
                        //키보드 내리기
                        inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

                        StrPreInput = hidden.getText().toString();



                        System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!EditText : " + hidden);
                        System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!StrPreInput : " + StrPreInput);



                        if(StrPreInput.startsWith("A")) {
                            regacy.setText(StrPreInput);
                            try {
                                r_rs = r_st.executeQuery("EXEC dbo.SP_PDA_WM00110_INQUERY '" + StrPreInput + "','ko'");  //A220715000000005
                                while (r_rs.next()) {
                                    if (r_rs.getString(1).equals("F")) { //사용할 수 없는 레거시 바코드
                                        //hidden.setText(null);
                                        notice.setText(r_rs.getString(2));
                                        notice.setTextColor(Color.parseColor("#FFFF0000"));
                                        regacy.setTextColor(Color.parseColor("#FFFF0000"));
                                        StrSave = StrPreInput;
                                        System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!StrSave : " + StrSave);
                                        hidden.setText("");


                                    } else if (r_rs.getString(1).equals("T")) {
                                        regacy.setTextColor(Color.parseColor("#000000"));
                                        notice.setTextColor(Color.parseColor("#000000"));
                                        notice.setText(r_rs.getString(2));
                                        StrSave = StrPreInput;
                                        System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!StrSave : " + StrSave);
                                        hidden.setText("");


                                        search.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                moveActivity(Regacy_Popup.class, regacy.getText().toString());
                                            }
                                        });
                                    }
                                }
                            } catch (SQLException e) {
                            }
                            break;
                        }




                }
                return false;
            }
        });















        /************ 저장 버튼****************/
        btn_store.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                a = amount.getText().toString();

                /**** 저장 기능*******/
                Statement s_st=connectDB();
                ResultSet s_rs;

                try{
                    s_rs = s_st.executeQuery("EXEC dbo.SP_PDA_WM00110_SAVE '"+"1001"+"','"+sregacy+"','"+pnum+"','"+a+"','kjkim','ko'");
                    while(s_rs.next()){
                        notice.setText(s_rs.getString(1));
                        listViewAdapter.addItem(
                                s_rs.getString(2),
                                s_rs.getString(3),
                                spname, a);
                        //리스트뷰에 어뎁터 set
                        listview.setAdapter(listViewAdapter);
                    }
                }catch (SQLException e) {

                }
            }
        });

        btn_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listViewAdapter.clearItem();
            }
        });


    }


}


