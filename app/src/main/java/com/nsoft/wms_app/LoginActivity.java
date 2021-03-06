package com.nsoft.wms_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
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
    private Context mContext;

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

        mContext = this;



        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Email = etEmail.getText().toString();
                Pwd = etPass.getText().toString();

                //쿠키에 UserId값 저장
                PreferenceManager.setString(mContext, "UID", Email);

                try{
                    ConnectionHelper connectionHelper = new ConnectionHelper();
                    connect = connectionHelper.ConnectionClass();
                    if(connect!=null){
                        //String query = "INSERT INTO KKJ_EMP VALUES ('" + Email + "', '" + Pwd + "');";
                        //프로시저 돌리기
                        String query = "EXEC dbo.SP_PDA_NLOGIN_INQUERY_EXECUTE_LOGIN '" + Email + "', '" + Pwd + "', 'ko'";
                        Statement st = connect.createStatement();
                        ResultSet rs = st.executeQuery(query);

                        while (rs.next()){
                            if(rs.getString(1).isEmpty()!=true){
                                Toast.makeText(LoginActivity.this, "비밀번호가 잘못되었습니다.", Toast.LENGTH_SHORT).show();
                            }else if(rs.getString(1).isEmpty()){
                                Toast.makeText(LoginActivity.this, "로그인이 성공적으로 되었습니다.", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                startActivity(intent);
                            }
                        }

                    }else{
                        ConnectionResult="Check Connection";
                    }
                }catch(Exception ex){
                }
            }
        });




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


    //버전 비교 함수
    public static boolean compareVersion(String appVersion, String compareVersion) {
        boolean isNeedUpdate = false;
        String[] arrX = appVersion.split("[.]");
        String[] arrY = compareVersion.split("[.]");

        int length = Math.max(arrX.length, arrY.length);

        for(int i = 0; i < length; i++){
            int x, y;
            try {
                x = Integer.parseInt(arrX[i]);
            } catch (ArrayIndexOutOfBoundsException e) {
                x = 0;
            }
            try {
                y = Integer.parseInt(arrY[i]);
            } catch (ArrayIndexOutOfBoundsException e) {
                y = 0;
            }

            if(x > y) {
                // 앱 버전이 큼
                isNeedUpdate = false;
                break;
            }else if(x < y){
                // 비교 버전이 큼
                isNeedUpdate = true;
                break;
            } else {
                // 버전 동일
                isNeedUpdate = false;
            }
        }
        return isNeedUpdate;
    }



}