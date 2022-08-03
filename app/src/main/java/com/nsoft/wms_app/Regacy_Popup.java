package com.nsoft.wms_app;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Regacy_Popup extends BaseActivity {
    String pname,pnum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regacy_popup);

        Button btn_close = findViewById(R.id.btn_close);
        Button btn_yes = findViewById(R.id.btn_yes);

        /************ 팝업창 선택, 닫기 버튼  ****************/
       // btn_close.setOnClickListener(v -> moveActivity(RegacyBarcode.class));

        //btn_yes.setOnClickListener(v -> moveActivity(RegacyBarcode.class, pname));


// 뒷배경 흐리게 하기
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        layoutParams.dimAmount = 0.7f;
        getWindow().setAttributes(layoutParams);

// 사이즈 조절
        DisplayMetrics dm = getApplicationContext().getResources().getDisplayMetrics();
        int width = (int) (dm.widthPixels * 0.9); // Display 사이즈의 90%
        int height = (int) (dm.heightPixels * 0.9); // Display 사이즈의 90%
        getWindow().getAttributes().width = width;
        getWindow().getAttributes().height = height;


        /****** spinner *****/
        String[] items = {"ALL", "완제품", "반제품", "자재", "기타"};
        Spinner category_spin = findViewById(R.id.category_spin);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_dropdown_item, items);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        category_spin.setAdapter(adapter);


        /************ 팝업 내용 표시 ******************/


        //리스트뷰, 리스트뷰 어뎁터 초기화
        ListView listView;
        ListAdapter listViewAdapter;

        listView = (ListView) findViewById(R.id.listview);
        listViewAdapter = new ListAdapter();


        category_spin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            Statement st;
            ResultSet rs;

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                st = connectDB();
                if (position == 0) {
                    listViewAdapter.clearItem();
                    try {
                        rs = st.executeQuery("EXEC dbo.SP_PDA_WM00110P1_INQUERY '','ko'");
                        int i = 1;
                        while (rs.next()) {
                            listViewAdapter.addItem(
                                    Integer.toString(i),
                                    rs.getString(1),
                                    rs.getString(2),
                                    rs.getString(3));
                            //리스트뷰에 어뎁터 set
                            listView.setAdapter(listViewAdapter);
                            i++;
                        }
                    } catch (SQLException e) {
                    }
                }
                if (position == 1) {
                    listViewAdapter.clearItem();
                    try {
                        rs = st.executeQuery("EXEC dbo.SP_PDA_WM00110P1_INQUERY 'A0A','ko'");
                        int i = 1;
                        while (rs.next()) {
                            listViewAdapter.addItem(
                                    Integer.toString(i),
                                    rs.getString(1),
                                    rs.getString(2),
                                    rs.getString(3));
                            //리스트뷰에 어뎁터 set
                            listView.setAdapter(listViewAdapter);
                            i++;
                        }
                    } catch (SQLException e) {
                    }
                }
                if (position == 2) {
                    listViewAdapter.clearItem();
                    try {
                        rs = st.executeQuery("EXEC dbo.SP_PDA_WM00110P1_INQUERY 'A0S','ko'");
                        int i = 1;
                        while (rs.next()) {
                            listViewAdapter.addItem(
                                    Integer.toString(i),
                                    rs.getString(1),
                                    rs.getString(2),
                                    rs.getString(3));
                            //리스트뷰에 어뎁터 set
                            listView.setAdapter(listViewAdapter);
                            i++;
                        }
                    } catch (SQLException e) {
                    }
                }
                if (position == 3) {
                    listViewAdapter.clearItem();
                    try {
                        rs = st.executeQuery("EXEC dbo.SP_PDA_WM00110P1_INQUERY 'A0M','ko'");
                        int i = 1;
                        while (rs.next()) {
                            listViewAdapter.addItem(
                                    Integer.toString(i),
                                    rs.getString(1),
                                    rs.getString(2),
                                    rs.getString(3));
                            //리스트뷰에 어뎁터 set
                            listView.setAdapter(listViewAdapter);
                            i++;
                        }
                    } catch (SQLException e) {
                    }
                }
                if (position == 4) {
                    listViewAdapter.clearItem();
                    try {
                        rs = st.executeQuery("EXEC dbo.SP_PDA_WM00110P1_INQUERY 'A0E','ko'");
                        int i = 1;
                        while (rs.next()) {
                            listViewAdapter.addItem(
                                    Integer.toString(i),
                                    rs.getString(1),
                                    rs.getString(2),
                                    rs.getString(3));
                            //리스트뷰에 어뎁터 set
                            listView.setAdapter(listViewAdapter);
                            i++;
                        }
                    } catch (SQLException e) {
                    }
                }

            }


            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        listView.setOnItemClickListener((parent, view, position, id) -> {
            view.setBackgroundColor(Color.parseColor("#F6CB8B"));

            //pname = listViewAdapter.getItem(2).toString();
            pname = listViewAdapter.listViewItemList.get(position).getRowtext3().toString();  //선택한 품명
            pnum = listViewAdapter.listViewItemList.get(position).getRowtext2().toString();   //품번
        });


        //Regacy_Popup 액티비티에서 얻어온 레거시 바코드
        Intent intent = getIntent();
        String sregacy = intent.getStringExtra("str1");

        /************ 팝업창 선택, 닫기 버튼  ****************/
        btn_close.setOnClickListener(v -> moveActivity(RegacyBarcode.class));
        btn_yes.setOnClickListener(v -> moveActivity(RegacyBarcode.class, pname,pnum,sregacy));


    }




}























