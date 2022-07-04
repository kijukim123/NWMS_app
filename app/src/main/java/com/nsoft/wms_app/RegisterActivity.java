package com.nsoft.wms_app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class RegisterActivity extends AppCompatActivity {

    private EditText etId, etPass, etName;
    private Button btnRegister;
    //private RegisterRequest registerRequest;

    DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
    DatabaseReference conditionRef = mRootRef.child("text");


    @Override
    protected void onStart(){
        super.onStart();

        conditionRef.addValueEventListener(new ValueEventListener(){
            @Override
            public void onDataChange(DataSnapshot dataSnapshot){
                String text = dataSnapshot.getValue(String.class);
                etId.setText(text);
            }

            @Override
            public void onCancelled(DatabaseError databaseError){

            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                conditionRef.setValue(etId.getText().toString());
            }
        });
    }

    /* 기존회워가입 버전
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //아이디 값 찾아주기
        etId = (EditText) findViewById(R.id.et_reg_id);
        etPass = (EditText)findViewById(R.id.et_reg_pass);
        etName = (EditText)findViewById(R.id.et_name);

        //회원가입 버튼 클릭시 수행
        btnRegister = findViewById(R.id.btn_register);

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //EditText에 현재 입력되어있는 값을 get(가져온다)해온다.
                String userID = etId.getText().toString();
                String userPass = etPass.getText().toString();
                String userName = etName.getText().toString();


                Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }

    */


}