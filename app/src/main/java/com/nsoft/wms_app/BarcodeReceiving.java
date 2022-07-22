package com.nsoft.wms_app;

import androidx.annotation.ColorInt;
import androidx.annotation.ColorRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Switch;

public class BarcodeReceiving extends AppCompatActivity {

    private Button rg_btn1;
    private Button rg_btn2;
    private ImageButton back;
    private ImageButton logout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_barcode_receiving);

        rg_btn1 = findViewById(R.id.rg_btn1);
        rg_btn2 = findViewById(R.id.rg_btn2);

        rg_btn1.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

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
        rg_btn2.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

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


        back = findViewById(R.id.back1);
        logout = findViewById(R.id.logout1);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(BarcodeReceiving.this, MainActivity.class);
                startActivity(intent);
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(BarcodeReceiving.this, PopupActivity.class);
                startActivity(intent);
            }
        });


    }
}