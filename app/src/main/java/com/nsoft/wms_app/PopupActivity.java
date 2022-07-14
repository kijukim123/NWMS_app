package com.nsoft.wms_app;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

public class PopupActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_popup_logout);

        Button btn_yes = (Button) findViewById(R.id.btn_yes);
        Button btn_no = (Button) findViewById(R.id.btn_no);

        btn_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PopupActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        btn_no.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PopupActivity.this, MainActivity.class);
                startActivity(intent);
            }
        }));
    }


    //바깥 레이어 클릭 시 안닫히게
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_OUTSIDE) {
            return false;
        }
        return true;
    }

    //안드로이드 백버튼 막기
    @Override
    public void onBackPressed() {
        return;
    }

}