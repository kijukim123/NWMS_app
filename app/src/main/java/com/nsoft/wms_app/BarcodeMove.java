package com.nsoft.wms_app;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.barcode_move);

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


        EtPreMove.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                switch(keyCode){
                    case KeyEvent.KEYCODE_ENTER:
                        SPreInput = EtPreMove.getText().toString();


                        System.out.println("!!!!!!!!!!!!!!!!!!!EnterKeyOn!!!!!!!!!!!!!!!!!!!!");
                        System.out.println(SPreInput);
                        if(SPreInput.startsWith("R")){
                            System.out.println("Rstart");
                            SPreInputR = SPreInput;
                            TvMoveLocation.setText(SPreInputR);
                            SPreInput = "";
                            System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!SpreInput: " + SPreInput);
                            EtPreMove.setText("");

                        }
                        else if(SPreInput.startsWith("A")){
                            System.out.println("Astart");
                            SPreInputA = SPreInput;
                            System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!SpreInputA: " + SPreInputA);
                            SPreInput = "";
                            EtPreMove.setText("");
                        }
                }
                return false;
            }
        });



    }
}