package com.example.yokuro.arminigame;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.Image;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.Display;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class StartActivity extends AppCompatActivity implements View.OnClickListener,SensorEventListener{
    private TextView title;
    private Button start;
    private Button ranking;
    private ImageView starIcon;

    private final static String TAG_START="start";
    private final static String TAG_RANK="ranking";

    private final int MP= ViewGroup.LayoutParams.MATCH_PARENT;

    private final static int MENU_SET=0;

    private SettingDAO settingDAO;

    private Sensor gyro;
    private SensorManager sensorManager;
    private int windowSizeX;
    private int windowSizeY;


    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_start);

        settingDAO = new SettingDAO(this);

        title = findViewById(R.id.title);
        start = findViewById(R.id.start);
        start.setTag(TAG_START);
        start.setOnClickListener(this);
        ranking = findViewById(R.id.rankingview);
        ranking.setTag(TAG_RANK);
        ranking.setOnClickListener(this);

        String BR = System.getProperty("line.separator");
        title.setText("周りを見渡して" + BR + "星をさがそう！" + BR + BR + "星を見つけたらタップ");

        settingDAO.create();

        starIcon = (ImageView) findViewById(R.id.star);

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        List<Sensor> list = sensorManager.getSensorList(Sensor.TYPE_GYROSCOPE);
        if (list.size() > 0) {
            gyro = list.get(0);
            sensorManager.registerListener(this, gyro, SensorManager.SENSOR_DELAY_UI);
        }
    }
    @Override
    public void onWindowFocusChanged(boolean haschange){
        LinearLayout layout=findViewById(R.id.startLayout);
        Point p=getViewSize(layout);
        windowSizeX=p.x;
        windowSizeY=p.y;
    }

    public Point getViewSize(View view){
        Point point=new Point(0,0);
        point.set(view.getWidth(),view.getHeight());
        return point;
    }

    @Override
    public void onClick(View view){
        String tag=(String)view.getTag();
        if(TAG_START.equals(tag)) {
            Intent intent = new Intent(this, SelectDifficultActivity.class);
            startActivity(intent);
        }
        else if(TAG_RANK.equals(tag)){
            Intent intent = new Intent(this,RankingViewActivity.class);
            startActivity(intent);
        }
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        if(sensorEvent.sensor.getType()==Sensor.TYPE_GYROSCOPE){
            float movex=sensorEvent.values[1]*100;
            float movey=sensorEvent.values[0]*100;
            int starsize=starIcon.getWidth();
            if(starIcon.getX()<0){
                starIcon.setX(0);
            }
            if(starIcon.getY()<0){
                starIcon.setY(0);
            }
            if(starIcon.getX()>windowSizeX-starsize){
                starIcon.setX(windowSizeX-starsize);
            }
            if(starIcon.getY()>windowSizeY-starsize){
                starIcon.setY(windowSizeY-starsize);
            }

            starIcon.setX(starIcon.getX()+movex);
            starIcon.setY(starIcon.getY()+movey);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
    @Override
    protected void onPause(){
        super.onPause();
        sensorManager.unregisterListener((SensorEventListener) this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        super.onCreateOptionsMenu(menu);
        MenuItem item0=menu.add(0,MENU_SET,0,"設定");
        item0.setIcon(android.R.drawable.ic_menu_manage)
                .setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem){
        int itemId=menuItem.getItemId();
        if(MENU_SET==itemId){
            passChangeArrert();
        }
        return true;
    }

    //pass変更確認アラート
    private void passChangeArrert(){
        LinearLayout layout=new LinearLayout(this);
        layout.setGravity(Gravity.CENTER_VERTICAL);
        layout.setOrientation(LinearLayout.VERTICAL);
        TextView textView=new TextView(this);
        textView.setText("パスワードを変更しますか？");
        textView.setTextColor(Color.GRAY);
        textView.setTextSize(16);
        layout.addView(textView);
        final EditText passEdt=new EditText(this);
        passEdt.setHint("現在のパスワードを入力");
        passEdt.setLayoutParams(new LinearLayout.LayoutParams(MP,MP));
        passEdt.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        layout.addView(passEdt);


        AlertDialog.Builder builder=new AlertDialog.Builder(this)
                .setTitle("設定")
                .setView(layout)
                .setPositiveButton("変更", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //
                        String nowPass=passEdt.getText().toString();
                        boolean passOK=settingDAO.passCheck(nowPass);
                        if(passOK){
                            passChange();
                        }else{
                            //パスワードが違う
                            toast(StartActivity.this,"パスワードが違います");
                        }
                    }
                })
                .setNegativeButton("キャンセル",null);
        builder.show();
    }

    private void passChange(){
        LinearLayout layout=new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setGravity(Gravity.CENTER_VERTICAL);

        TextView textView1=new TextView(this);
        textView1.setText("新しいパスワードを入力 *５文字以上*");
        textView1.setTextColor(Color.GRAY);
        textView1.setTextSize(16);
        layout.addView(textView1);

        final EditText editText1=new EditText(this);
        editText1.setLayoutParams(new LinearLayout.LayoutParams(MP,MP));
        editText1.setHint("新しいパスワード");
        editText1.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        layout.addView(editText1);

        TextView textView2=new TextView(this);
        textView2.setText("再度新しいパスワードを入力");
        textView2.setTextColor(Color.GRAY);
        textView2.setTextSize(16);
        layout.addView(textView2);

        final EditText editText2=new EditText(this);
        editText2.setLayoutParams(new LinearLayout.LayoutParams(MP,MP));
        editText2.setHint("新しいパスワード");
        editText2.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        layout.addView(editText2);

        AlertDialog.Builder builder=new AlertDialog.Builder(this)
                .setTitle("パスワード変更")
                .setView(layout)
                .setPositiveButton("変更", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String pass1=editText1.getText().toString();
                        String pass2=editText2.getText().toString();
                        if(pass1.length()>5) {
                            if (pass1.equals(pass2)) {
                                //DB変更処理
                                boolean passChangeAble=settingDAO.updatePass(pass1);
                                if(passChangeAble){
                                    toast(StartActivity.this, "変更しました");
                                }else {
                                    toast(StartActivity.this,"変更できませんでした");
                                }
                            } else {
                                toast(StartActivity.this, "一致していません");
                                passChange();
                            }
                        }else{
                            toast(StartActivity.this,"５文字以上で設定してください");
                            passChange();
                        }

                    }
                });
        builder.show();
    }

    //トースト表示
    private static void toast(Context context, String text){
        Toast toast=Toast.makeText(context,text,Toast.LENGTH_SHORT);
        toast.show();
    }
}
