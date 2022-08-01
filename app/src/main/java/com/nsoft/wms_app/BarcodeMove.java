package com.nsoft.wms_app;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.InputFilter;
import android.text.Spanned;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class BarcodeMove extends AppCompatActivity {

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
                    EtPreMove.setText(null);
                    EtPreMove.setText("");
                }
                else if(SPreInputA != null && SPreInputA.length() >10){
                    //랙위치 텍스트뷰에 유효한 값이 있으면 EditText 초기화
                    EtPreMove.setText(null);
                    EtPreMove.setText("");
                }
                return "";
            }
        },new InputFilter.LengthFilter(20)});

    }
}