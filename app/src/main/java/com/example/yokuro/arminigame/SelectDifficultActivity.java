package com.example.yokuro.arminigame;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class SelectDifficultActivity extends AppCompatActivity implements View.OnClickListener{
    private Button level1;
    private Button level2;
    private Button level3;
    private final static String TAG_ONE="one";
    private final static String TAG_TWO="two";
    private final static String TAG_THREE="three";

    private TextView infoHint;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_difficult);

        level1=findViewById(R.id.level1);
        level1.setTag(TAG_ONE);
        level1.setOnClickListener(this);

        level2=findViewById(R.id.level2);
        level2.setTag(TAG_TWO);
        level2.setOnClickListener(this);

        level3=findViewById(R.id.level3);
        level3.setTag(TAG_THREE);
        level3.setOnClickListener(this);

        String BR = System.getProperty("line.separator");
        infoHint=findViewById(R.id.infoHint);
        infoHint.setText("ヒントボタンを押すと方角を"+BR+"教えてくれるよ（減点）");



    }

    @Override
    public void onClick(View view){

        String tag =(String) view.getTag();
        if(TAG_ONE.equals(tag)){
            startMainActivity(1);
        }
        else if(TAG_TWO.equals(tag)){
            startMainActivity(2);
        }
        else if(TAG_THREE.equals(tag)){
            startMainActivity(3);
        }
    }


    public void startMainActivity(int lv){

        Intent intent=new Intent(this,MainActivity.class);
        intent.putExtra("level",lv);
        startActivity(intent);
    }

}
