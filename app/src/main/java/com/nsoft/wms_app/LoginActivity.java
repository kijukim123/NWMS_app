package com.nsoft.wms_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import android.content.Intent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.nsoft.wms_app.R;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;


public class LoginActivity extends AppCompatActivity {

    private EditText etEmail, etPass;        // 로그인 입력필드
    private Button btnLogin;
    private Spinner spinnerLang;
    private String Email, Pwd;

    private FirebaseAuth mFirebaseAuth;      // 파이어베이스 인증
    private DatabaseReference mDatabaseRef;  // 실시간 데이터베이스

    Connection connect;              //database Connect
    String ConnectionResult = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        etEmail = findViewById(R.id.et_login_email);
        etPass = findViewById(R.id.et_login_pass);
        btnLogin = findViewById(R.id.btn_login1);
        spinnerLang = findViewById(R.id.spinner_language);

        mFirebaseAuth = FirebaseAuth.getInstance();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("WMS_app");

/*
        //회원가입 버튼을 클릭 시 수행
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("btnRegister Listener called");
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

 */


        /*
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //EditText에 현재 입력되어있는 값을 get(가져온다)해온다.
                String userEmail = etEmail.getText().toString();
                String userPass = etPass.getText().toString();

                mFirebaseAuth.signInWithEmailAndPassword(userEmail, userPass).addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            //로그인 성공
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(intent);
                            finish();      //현재 액티비티 파괴
                        }else{
                            Toast.makeText(LoginActivity.this, "login failed", Toast.LENGTH_SHORT).show();
                        }

                    }
                });

                //Toast.makeText(getApplicationContext(),"로그인에 성공하였습니다.", Toast.LENGTH_SHORT).show();
            }
        });

         */


    }

    //데이터베이스 테이블에 있는 값을 안드로이드 텍스트뷰에 가져옴
    //database Connect(GetDataFromSQL)
    public void GetTextFromSQL(View v){
        etEmail = findViewById(R.id.et_login_email);
        etPass = findViewById(R.id.et_login_pass);

        try{
            ConnectionHelper connectionHelper = new ConnectionHelper();
            connect = connectionHelper.ConnectionClass();
            if(connect!=null){
                String query = "Select * from KKJ_EMP";
                Statement st = connect.createStatement();
                ResultSet rs = st.executeQuery(query);

                while (rs.next()){
                    etEmail.setText(rs.getString(1));
                    etPass.setText(rs.getString(2));
                }
            }else{
                ConnectionResult="Check Connection";
            }
        }catch(Exception ex){
        }
    }



    //안드로이드에서 입력 값을 받아서 데이터베이스에 입력
    public void InsertTextToSQL(View v){
        Email = etEmail.getText().toString();
        Pwd = etPass.getText().toString();

        try{
            ConnectionHelper connectionHelper = new ConnectionHelper();
            connect = connectionHelper.ConnectionClass();
            if(connect!=null){
                String query = "INSERT INTO KKJ_EMP VALUES ('" + Email + "', '" + Pwd + "');";
                Statement st = connect.createStatement();
                ResultSet rs = st.executeQuery(query);

                while (rs.next()){
                    etEmail.setText(rs.getString(1));
                    etPass.setText(rs.getString(2));
                }

            }else{
                ConnectionResult="Check Connection";
            }
        }catch(Exception ex){
        }
    }



}