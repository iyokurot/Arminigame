package com.example.yokuro.arminigame;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ResultActivity extends AppCompatActivity implements View.OnClickListener{

    private Button returnTotop;
    private Button retryButton;
    private Button rankingButton;
    private TextView resultScore,eval;
    private Long score;

    private final static String TAG_TOP="return";
    private final static String TAG_RETRY="retry";
    private final static String TAG_RANK="ranking";

    private int level=0;
    private final long [] maxScores={
            4500,
            7500,
            10000
    };

    String BR;
    @Override
    public void onCreate(Bundle bundle){
        super.onCreate(bundle);
        setContentView(R.layout.activity_result);
        Intent intent=getIntent();
        score=intent.getLongExtra("score",0);
        level=intent.getIntExtra("level",0);


        returnTotop=findViewById(R.id.retitle);
        returnTotop.setTag(TAG_TOP);
        returnTotop.setOnClickListener(this);
        retryButton=findViewById(R.id.retry);
        retryButton.setTag(TAG_RETRY);
        retryButton.setOnClickListener(this);
        rankingButton=findViewById(R.id.addrank);
        rankingButton.setTag(TAG_RANK);
        rankingButton.setOnClickListener(this);
        resultScore=findViewById(R.id.result);
        eval=findViewById(R.id.hyouka);

        StringBuffer bf = new StringBuffer();
        BR=System.getProperty("line.separator");
        bf.append("Score ");
        bf.append(BR+score+"点");
        resultScore.setText(bf);

        evalByScore(maxScores[level-1]);
    }

    private void evalByScore(long lvscore){
        long minus=lvscore/5;
        if(score>lvscore){
            eval.setText("千里眼の持ち主！");
        }else if(score>lvscore-minus) {
            eval.setText("自慢できる級");
        }else if(score>lvscore-minus*2) {
            eval.setText("中の上");
        }else if(score>lvscore-minus*3) {
            eval.setText("凡人級");
        }else if(score>lvscore-minus*4){
            eval.setText("微妙");
        }else if(score>0){
            eval.setText("星に翻弄されたね★");
        }else{
            eval.setText("まわりをよく見よう");
        }
    }

    @Override
    public void onClick(View view){
        String tag=(String)view.getTag();
        if(TAG_TOP.equals(tag)) {
            Intent intent = new Intent(this, StartActivity.class);
            startActivity(intent);
        }
        else if(TAG_RETRY.equals(tag)){
            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra("level",level);
            startActivity(intent);
        }else if(TAG_RANK.equals(tag)){
            //ランキング情報取得**
            Intent intent = new Intent(this,RankRegistActivity.class);
            intent.putExtra("level",level);
            intent.putExtra("score",score);
            //アクティビティの開始
            startActivity(intent);
        }

    }


}
