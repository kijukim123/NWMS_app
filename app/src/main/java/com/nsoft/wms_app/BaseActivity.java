package com.nsoft.wms_app;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

public class BaseActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
    }


    /**********  뒤로가기, 로그아웃   ******************/
    public void mainbtn(ImageButton back, ImageButton logout, String Email){

        back.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                moveActivity(MainActivity.class,Email);
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                moveActivity(PopupActivity.class,Email);
            }
        });
    }

    /******* SharedPreferences 메서드  ******/
    /******* (경로: shift2번 -> Device File Explorer /data/data/{packageName}/shared_prefs/{keyName}.xml **********/
    public void record(String name, String key, String value) {
        SharedPreferences pref = getSharedPreferences(name, MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        Set<String> oldSet = pref.getStringSet(key, new HashSet<String>());

        //make a copy, update it and save it
        Set<String> set = new HashSet<String>();
        set.add(value);
        set.addAll(oldSet);

        editor.putStringSet(key, set);
        editor.commit();
    }

    public void removekey(String key) {

        SharedPreferences pref = getSharedPreferences(key, MODE_PRIVATE);
        SharedPreferences.Editor edit = pref.edit();
        edit.remove(key);
        edit.commit();

    }


    /******* DB와 연결하는 함수 ******/
    public Statement connectDB() {

        Connection connect;              //database Connect
        Statement st = null;

        try {
            ConnectionHelper connectionHelper = new ConnectionHelper();
            connect = connectionHelper.ConnectionClass();

            if (connect != null) {

                st = connect.createStatement();
            }
        } catch (Exception ex) {
        }

        return st;
    }


    /******* 토스트 메시지 ******/
    public void MakeToast(Activity activity, String msg) {
        Toast.makeText(activity, msg, Toast.LENGTH_SHORT).show();
    }

    /******* Intent 함수    *************/
    public void moveActivity(Class next) {
        Intent intent = new Intent(this, next);
        startActivity(intent);
    }

    public void moveActivity(Class next, String str1) {
        Intent intent = new Intent(this, next);

        intent.putExtra("str1", str1);
        startActivity(intent);
    }
    public void moveActivity(Class next, String str1,String str2) {
        Intent intent = new Intent(this, next);

        intent.putExtra("str1", str1);
        intent.putExtra("str2", str2);
        startActivity(intent);
    }

    public void moveActivity(Class next, String str1,String str2,String str3) {
        Intent intent = new Intent(this, next);

        intent.putExtra("str1", str1);
        intent.putExtra("str2", str2);
        intent.putExtra("str3",str3);
        startActivity(intent);
    }



    /*************** 로그 등록 *********************/
    public String regLog(String menu, String userid, String action) {

        SimpleDateFormat dataFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:sss", Locale.KOREA);
        SimpleDateFormat ymdFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.KOREA);

        String query = "INSERT INTO LOG_TEST VALUES ('" + menu + "'" + "," + "'NSTORM_WMS_PDA'" + "," + "'" + userid + "'" +
                "," + "'" + "tempIP" + "'" + "," + "'" + action + "'" + "," + "'" + dataFormat.format(new Date()) + "'" + "," +
                "'" + ymdFormat.format(new Date()) + "');";

        return query;
    }


}



















