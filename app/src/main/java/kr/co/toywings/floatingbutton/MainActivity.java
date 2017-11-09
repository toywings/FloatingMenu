package kr.co.toywings.floatingbutton;

import android.app.Activity;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.Collections;

public class MainActivity extends Activity
    {

    private V_FloatingMenu fm;

    @Override
    protected void onCreate(Bundle savedInstanceState)
        {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fm = (V_FloatingMenu)findViewById(R.id.fm);

        ArrayList<Data> list = new ArrayList<>();
        Data data = new Data();
        data.NAME = "대기표 보관함";
        data.IMAGE = R.drawable.floating_menu_btn1;
        list.add(data);
        data = new Data();
        data.NAME = "스탬프 보관함";
        data.IMAGE = R.drawable.floating_menu_btn2;
        list.add(data);
        data = new Data();
        data.NAME = "영업점 보관함";
        data.IMAGE = R.drawable.floating_menu_btn3;
        list.add(data);
        data = new Data();
        data.NAME = "쿠폰 보관함";
        data.IMAGE = R.drawable.floating_menu_btn4;
        list.add(data);
        data = new Data();
        data.NAME = "홈";
        data.IMAGE = R.drawable.ic_floating_home;
        list.add(data);

        Collections.reverse(list);
        fm.setButtons(list);
        }

    @Override
    public void onBackPressed()
        {
        if(fm.isOpened())
            {
            fm.clickStart();
            }
        else{
            super.onBackPressed();
            }
        }
    }
