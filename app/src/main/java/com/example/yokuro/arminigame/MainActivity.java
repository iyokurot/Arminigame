package com.example.yokuro.arminigame;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity implements View.OnClickListener,SensorEventListener{
    private Button hintButton;
    private TextView textx;
    private ImageView moveImage,moveImage2,moveImage3,moveImage4,moveImage5,moveImage6,moveImage7,moveImage8,moveImage9;
    private ArrayList<ImageView>moveImages;
    private Sensor gyro;
    private SensorManager sensorManager;
    private final static String []TAG_TAR={
            "target1",
            "target2",
            "target3",
            "target4",
            "target5",
            "target6",
            "target7",
            "target8"
    };
    private final static String TAG_HINT="hint";

    private long scoreTime=0;
    private long[] scoreListByLevel={
            /*スコア更新
            4500,
            7500,
            10000
            */
            8000,
            10000,
            13000
    };
    private Random rx,ry;

    private static final int NOW=0;

    private int level=0;
    private ArrayList<Button>buttons;
    private int [] buttonIds={
            R.id.star1,
            R.id.star2,
            R.id.star3,
            R.id.star4,
            R.id.star5,
            R.id.star6,
            R.id.star7,
            R.id.star8
    };
    private int []starnum={
            1,
            4,
            8
    };
    private int laststar=0;
    private int width=0;


    protected void onCreate(Bundle bundle){
        super.onCreate(bundle);
        LayoutInflater inflater=LayoutInflater.from(this);
        View view=inflater.inflate(R.layout.activity_main,null);
        setContentView(view);

        Intent intent=getIntent();
        level=intent.getIntExtra("level",0);
        scoreTime=scoreListByLevel[level-1];
        buttons=new ArrayList<>();


        createStar(starnum[level-1]);
        laststar=starnum[level-1];

        hintButton=(Button)findViewById(R.id.Hint);
        hintButton.setTag(TAG_HINT);
        hintButton.setOnClickListener(this);


        sensorManager= (SensorManager)getSystemService(Context.SENSOR_SERVICE);
        List<Sensor> list=sensorManager.getSensorList(Sensor.TYPE_GYROSCOPE);
        if(list.size()>0){
            gyro=list.get(0);
            sensorManager.registerListener(this,gyro,SensorManager.SENSOR_DELAY_UI);
        }

        textx=findViewById(R.id.Score);
        textx.setText("score : "+String.valueOf(scoreTime));


        moveImages=new ArrayList<>();
        moveImage=findViewById(R.id.moveImage);
        moveImages.add(moveImage);
        moveImage2=findViewById(R.id.moveImage2);
        moveImages.add(moveImage2);
        moveImage3=findViewById(R.id.moveImage3);
        moveImages.add(moveImage3);
        moveImage4=findViewById(R.id.moveImage4);
        moveImages.add(moveImage4);
        moveImage5=findViewById(R.id.moveImage5);
        moveImages.add(moveImage5);
        moveImage6=findViewById(R.id.moveImage6);
        moveImages.add(moveImage6);
        moveImage7=findViewById(R.id.moveImage7);
        moveImages.add(moveImage7);
        moveImage8=findViewById(R.id.moveImage8);
        moveImages.add(moveImage8);
        moveImage9=findViewById(R.id.moveImage9);
        moveImages.add(moveImage9);

        //時間ごとに呼び出し
        final Handler handler=new Handler();
        final Runnable r=new Runnable() {
            @Override
            public void run() {
                //スコア減少率
                scoreTime-=1.5;
                if(scoreTime<0){
                    scoreTime=0;
                }
                textx.setText("score : "+String.valueOf(scoreTime));
                handler.postDelayed(this,10);
            }
        };
        handler.post(r);

    }

    @Override
    public void onWindowFocusChanged(boolean hasfocus){
        width=moveImage.getWidth()-5;
        moveImage2.setX(width);
        moveImage3.setX(-width);
        moveImage4.setY(width);
        moveImage5.setY(-width);

        moveImage6.setX(width);
        moveImage6.setY(-width);
        moveImage7.setX(-width);
        moveImage7.setY(-width);
        moveImage8.setX(width);
        moveImage8.setY(width);
        moveImage9.setX(-width);
        moveImage9.setY(width);


    }
    //星生成メソッド
    public void createStar(int lv){
        for(int i=0;i<lv;i++) {
            Button button = findViewById(buttonIds[i]);
            button.setTag(TAG_TAR[i]);
            button.setOnClickListener(this);
            Random rx=new Random();
            Random ry=new Random();
            button.setX(rx.nextInt(10000)-5000);
            button.setY(ry.nextInt(3000)-1500);
            button.setVisibility(View.VISIBLE);
            buttons.add(button);
        }
    }



    @Override
    public void onSensorChanged(SensorEvent event){

        if(event.sensor.getType()==Sensor.TYPE_GYROSCOPE){

            float movey=event.values[1]*100;
            float movex = event.values[0]*100;
            //int Bwidth= button1.getWidth();


            for(Button star:buttons){
                int starW= star.getWidth();

                if(star.getX()>5800){
                    star.setTranslationX(-5800);
                }
                if(star.getX()<-5800){
                    star.setTranslationX(5800);
                }

                star.setX(star.getX()+movey);
                star.setY(star.getY()+movex);


            }


            for(ImageView image:moveImages){
                image.setX(image.getX()+movey);
                image.setY(image.getY()+movex);

                float moveDist=width+width/2;

                if(image.getX()>moveDist){
                    image.setX(-moveDist);
                }
                if(image.getX()<-moveDist){
                    image.setX(moveDist);
                }
                if(image.getY()>moveDist){
                    image.setY(-moveDist);
                }
                if(image.getY()<-moveDist){
                    image.setY(moveDist);
                }
            }
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
    public void onClick(View view){
        String tag=(String)view.getTag();
        if(TAG_TAR[0].equals(tag)){
            getTarget(0);
        }
        else if(TAG_TAR[1].equals(tag)){
            getTarget(1);
        }
        else if(TAG_TAR[2].equals(tag)){
            getTarget(2);
        }
        else if(TAG_TAR[3].equals(tag)){
            getTarget(3);
        }
        else if(TAG_TAR[4].equals(tag)){
            getTarget(4);
        }
        else if(TAG_TAR[5].equals(tag)){
            getTarget(5);
        }
        else if(TAG_TAR[6].equals(tag)){
            getTarget(6);
        }
        else if(TAG_TAR[7].equals(tag)){
            getTarget(7);
        }
        else if(TAG_HINT.equals(tag)){
            float x=0;
            float y=0;
            for(Button star:buttons){
                if(star.getVisibility()==View.VISIBLE){
                    x=star.getX();
                    y=star.getY();
                    break;
                }
            }
            String direction="";
            if(x>900){
                direction+="右";
            }else if(x<0){
                direction+="左";
            }
            if(y>1000){
                direction+="下";
            }else if(y<0){
                direction+="上";
            }
            toast(this,direction+"方面だよ");
            scoreTime-=1000;

        }else{
            toast(this,"未登録");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        super.onCreateOptionsMenu(menu);

        MenuItem item=menu.add(0,NOW,0,"");
        item.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        item.setIcon(R.drawable.hide);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int itemid=item.getItemId();
        if(itemid==NOW){
            StringBuffer bf=new StringBuffer();
            String BR=System.getProperty("line.separator");
            bf.append("現在位置");
            for(int i=0;i<buttons.size();i++) {
                if(buttons.get(i).getVisibility()==View.VISIBLE) {
                    bf.append(BR + "X : " + Integer.toString((int) buttons.get(i).getX()));
                    bf.append(BR + "Y : " + Integer.toString((int) buttons.get(i).getY()));
                }
            }
            String place=String.valueOf(bf);
            toast(this,place);
        }

        return true;
    }

    private void getTarget(int key){
        buttons.get(key).setVisibility(View.INVISIBLE);
        laststar--;
        //Score追加
        scoreTime+=500;
        //Clear判定
        if(laststar>0){
            toast(this,"見つけた！あと"+laststar+"個");
        }else{
            clearAction();
        }
    }

    private void clearAction(){
        long score=scoreTime;
        toast(this,"見つけた！");
        String putScore=String.valueOf(score);
        Intent intent=new Intent(this,ResultActivity.class);
        intent.putExtra("score",score);
        intent.putExtra("level",level);
        startActivity(intent);
    }

    private static void toast(Context context,String text){
        Toast toast=Toast.makeText(context,text,Toast.LENGTH_SHORT);
        toast.show();
    }
}
