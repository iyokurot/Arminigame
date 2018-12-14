package com.example.yokuro.arminigame;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;

public class RankRegistActivity extends AppCompatActivity implements View.OnClickListener{
    private EditText nameEdit;
    private Button registButton;
    private TextView levelText;
    private TextView scoreText;

    private int level;
    private long score;

    private final static String TAG_REGIST="register";

    private RanksDAO ranksDAO;
    private SettingDAO settingDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rank_regist);
        Intent intent=getIntent();
        level=intent.getIntExtra("level",0);
        score=intent.getLongExtra("score",0);

        levelText=findViewById(R.id.levelText);
        levelText.setText("level:"+level);
        nameEdit=(EditText)findViewById(R.id.rankname);

        scoreText=findViewById(R.id.scoreText);
        scoreText.setText("Score "+score);

        registButton=findViewById(R.id.register);
        registButton.setTag(TAG_REGIST);
        registButton.setOnClickListener(this);

        ranksDAO=new RanksDAO(this);
        settingDAO=new SettingDAO(this);
    }


    @Override
    public void onClick(View view){
        String tag=(String)view.getTag();
        if(TAG_REGIST.equals(tag)){
            final String name=nameEdit.getText().toString();
            if(!"".equals(name)){
                StringBuffer bf = new StringBuffer();
                String BR=System.getProperty("line.separator");
                bf.append("Level:"+level);
                bf.append(BR+"Score "+score);
                bf.append(BR+"登録名:"+name);
                bf.append(BR+"この内容で登録しますか？");
                AlertDialog.Builder builder=new AlertDialog.Builder(this);
                builder.setMessage(bf)
                        .setPositiveButton("登録", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //DBアクセス
                                RankingEB data=new RankingEB();
                                data.setID(settingDAO.getNewId());
                                data.setName(name);
                                data.setLevel(level);
                                data.setScore(Integer.parseInt(String.valueOf(score)));
                                data.setDate(getDate());
                                ranksDAO.create(data);
                                settingDAO.updateId();
                                toast(RankRegistActivity.this,"登録しました");
                                startRankingViewActivity(data.getID());
                            }
                        }).setNegativeButton("キャンセル", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        toast(RankRegistActivity.this,"キャンセルしました");
                    }
                });
                builder.show();
            }else{
                toast(this,"名前を記入してください");
            }
        }

    }

    private String getDate(){
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy/MM/dd/HH:mm:ss");
        String now=sdf.format(System.currentTimeMillis());
        return  now;
    }

    //RankingActivity遷移メソッド
    private void startRankingViewActivity(int id){
        Intent intent=new Intent(this,RankingViewActivity.class);
        intent.putExtra("level",level);
        intent.putExtra("id",id);
        startActivity(intent);
    }

    private static void toast(Context context, String text){
        Toast toast=Toast.makeText(context,text,Toast.LENGTH_SHORT);
        toast.show();
    }
}
