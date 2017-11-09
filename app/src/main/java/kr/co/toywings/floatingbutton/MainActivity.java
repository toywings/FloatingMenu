package kr.co.toywings.floatingbutton;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

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
        data.BUTTON = new ImageView(this);
        data.BUTTON.setOnClickListener(new View.OnClickListener()
            {
            @Override
            public void onClick(View view)
                {
                Toast.makeText(MainActivity.this, "대기표 보관함", Toast.LENGTH_SHORT).show();
                }
            });
        list.add(data);
        data = new Data();
        data.NAME = "스탬프 보관함";
        data.IMAGE = R.drawable.floating_menu_btn2;
        data.BUTTON = new ImageView(this);
        data.BUTTON.setOnClickListener(new View.OnClickListener()
            {
            @Override
            public void onClick(View view)
                {
                Toast.makeText(MainActivity.this, "스탬프 보관함", Toast.LENGTH_SHORT).show();
                }
            });
        list.add(data);
        data = new Data();
        data.NAME = "영업점 보관함";
        data.IMAGE = R.drawable.floating_menu_btn3;
        data.BUTTON = new ImageView(this);
        data.BUTTON.setOnClickListener(new View.OnClickListener()
            {
            @Override
            public void onClick(View view)
                {
                Toast.makeText(MainActivity.this, "영업점 보관함", Toast.LENGTH_SHORT).show();
                }
            });
        list.add(data);
        data = new Data();
        data.NAME = "쿠폰 보관함";
        data.IMAGE = R.drawable.floating_menu_btn4;
        data.BUTTON = new ImageView(this);
        data.BUTTON.setOnClickListener(new View.OnClickListener()
            {
            @Override
            public void onClick(View view)
                {
                Toast.makeText(MainActivity.this, "쿠폰 보관함", Toast.LENGTH_SHORT).show();
                }
            });
        list.add(data);
        data = new Data();
        data.NAME = "홈";
        data.IMAGE = R.drawable.ic_floating_home;
        data.BUTTON = new ImageView(this);
        data.BUTTON.setOnClickListener(new View.OnClickListener()
            {
            @Override
            public void onClick(View view)
                {
                Toast.makeText(MainActivity.this, "홈", Toast.LENGTH_SHORT).show();
                }
            });
        list.add(data);

        Collections.reverse(list);
        ImageView top = new ImageView(this);
        top.setOnClickListener(new View.OnClickListener()
            {
            @Override
            public void onClick(View view)
                {
                Toast.makeText(MainActivity.this, "Top", Toast.LENGTH_SHORT).show();
                }
            });
        fm.setButtons(list, top);
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
