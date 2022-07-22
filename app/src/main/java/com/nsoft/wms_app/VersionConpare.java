package com.nsoft.wms_app;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.zxing.datamatrix.decoder.Version;

import org.jsoup.Jsoup;
import org.jsoup.select.Elements;
import org.w3c.dom.Document;

import java.io.IOException;
import java.util.regex.Pattern;

class versionCompare extends AsyncTask<Void, Void, String> {
    private AppCompatActivity appCompatActivity = new AppCompatActivity();
    private final String APP_VERSION_NAME = BuildConfig.VERSION_NAME;
    private final String APP_PACKAGE_NAME = BuildConfig.APPLICATION_ID;

    private final String STORE_URL = "https://play.google.com/store/apps/details?id=com.nexon.axe"; // nexon를 예를 들었습니다.



    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(Void... params) {
        try{
            Document doc = (Document) Jsoup.connect(STORE_URL).get();
            //Elements Version = doc.select(".htlgb");
            //.select()가 안되서 일단 null로넣어 놓음
            Elements Version = null;
            for (int i = 0; i < Version.size(); i++) {
                String VersionMarket = Version.get(i).text();
                if (Pattern.matches("^[0-9]{1}.[0-9]{1}.[0-9]{1}$", VersionMarket)) {
                    return VersionMarket;
                }
            }
        }catch(IOException e){
            e.printStackTrace();
        }
        return null;
    }


    @Override
    protected void onPostExecute(String s) { //s는 마켓의 버전입니다.
        if(s != null){
            if(!s.equals(APP_VERSION_NAME)){ //APP_VERSION_NAME는 현재 앱의
                System.out.println(APP_VERSION_NAME);
                /*
                mDialog.setMessage("최신 버전이 출시되었습니다. 업데이트 후 사용 가능합니다.")
                        .setCancelable(false)
                        .setPositiveButton("업데이트 바로가기",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,
                                                        int id) {
                                        Intent marketLaunch = new Intent(
                                                Intent.ACTION_VIEW);
                                        marketLaunch.setData(Uri
                                                .parse(STORE_URL));
                                        startActivity(marketLaunch);
                                        finish();
                                    }
                                });


                AlertDialog alert = mDialog.create();
                alert.setTitle("업데이트 알림");
                alert.show();
                 */
            }
        }
        super.onPostExecute(s);
    }
}

