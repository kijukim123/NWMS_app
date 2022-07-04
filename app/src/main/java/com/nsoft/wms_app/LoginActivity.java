package com.nsoft.wms_app;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.nsoft.wms_app.R;


public class LoginActivity extends AppCompatActivity {
    private EditText etId, etPass;
    private Button btnLogin, btnRegister;
    //private LoginRequest loginRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        etId = findViewById(R.id.et_login_id);
        etPass = findViewById(R.id.et_login_pass);
        btnLogin = findViewById(R.id.btn_login1);
        btnRegister = findViewById(R.id.btn_login2);


        //회원가입 버튼을 클릭 시 수행
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("btnRegister Listener called");
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });


        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //EditText에 현재 입력되어있는 값을 get(가져온다)해온다.
                String userID = etId.getText().toString();
                String userPass = etPass.getText().toString();

                Toast.makeText(getApplicationContext(),"로그인에 성공하였습니다.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}