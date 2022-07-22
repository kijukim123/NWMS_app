package com.nsoft.wms_app;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ClipData;
import android.content.ClipDescription;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.InputEvent;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.w3c.dom.Document;

import java.io.IOException;
import java.util.regex.Pattern;

public class TestActivity extends AppCompatActivity {

    //앱 현재 버전
    private final String APP_VERSION_NAME = BuildConfig.VERSION_NAME;
    private TextView textView;
    private Button ButtonURL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        textView = findViewById(R.id.tv_barcode);
        //텍스트뷰에서 현재 앱 버전 확인
        //textView.setText(APP_VERSION_NAME);
        //System.out.println("현재앱 버전 : "+ APP_VERSION_NAME);

        ButtonURL = findViewById(R.id.btnUrl);
        ButtonURL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Intent intentURL = new Intent(Intent.ACTION_VIEW, Uri.parse("ftp://192.168.0.202:2022/"));
                //Intent intentURL = new Intent(Intent.ACTION_VIEW, Uri.parse("http://192.168.0.202/"));
                //startActivity(intentURL);
                textView.setText(readFromClipboard());
            }
        });


        //dispatchKeyEvent(KeyEvent keyEvent);
        try{
            if(dispatchKeyEvent1(null)){Toast.makeText(TestActivity.this, "someTHing", Toast.LENGTH_SHORT).show();}
            System.out.println(dispatchKeyEvent1(null));
            }catch(Exception e){
            Toast.makeText(TestActivity.this, "에러", Toast.LENGTH_SHORT).show();
        }


        //textView.setText(readFromClipboard());

    }



    //바코드 확인 관련 코드(zxing Library)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);

        if(result != null){
            if(result.getContents() == null){
                Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show();
            }else{
                Toast.makeText(this, "Scanned:" + result.getContents(), Toast.LENGTH_LONG).show();
            }
        }else{
            super.onActivityResult(requestCode, resultCode, data);
        }
    }


    //PDA 바코드 관련 코드(액션감지?)
    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (KeyEvent.ACTION_UP == event.getAction() && event.getDeviceId() == -1 && event.getFlags() == 8 && event.getKeyCode() == 66 && event.getSource() == 257) {
            String scan = readFromClipboard();
        }
        return false;
    }

    //PDA 바코드 관련 코드(스캔한 바코드 클립보드에서 불러오기?)
    public String readFromClipboard() {
        ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            if (clipboard.hasPrimaryClip()) {
                ClipDescription description = clipboard.getPrimaryClipDescription();
                ClipData data = clipboard.getPrimaryClip();
                if (data != null && description != null && description.hasMimeType(ClipDescription.MIMETYPE_TEXT_PLAIN))
                    return String.valueOf(data.getItemAt(0).getText());
            }
        }
        return null;
    }

    //@Override
    public boolean dispatchKeyEvent1(KeyEvent KEvent)
    {
        int keyaction = KEvent.getAction();

        if(keyaction == KeyEvent.ACTION_DOWN)
        {
            int keycode = KEvent.getKeyCode();
            int keyunicode = KEvent.getUnicodeChar(KEvent.getMetaState() );
            char character = (char) keyunicode;

            System.out.println("DEBUG MESSAGE KEY=" + character + " KEYCODE=" +  keycode);
        }


        return super.dispatchKeyEvent(KEvent);
    }

}









