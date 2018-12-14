package com.example.yokuro.arminigame;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class RankingViewActivity extends AppCompatActivity implements View.OnClickListener{
    private Button lv1Button;
    private Button lv2Button;
    private Button lv3Button;
    private ListView rankingList;

    private final static String TAG_LV1="level1";
    private final static String TAG_LV2="level2";
    private final static String TAG_LV3="level3";

    private final static int MENU_DELETE=0;
    private ArrayList<RankingEB>ranks;

    private RanksDAO ranksDAO;
    private SettingDAO settingDAO;

    private final static int MP=ViewGroup.LayoutParams.MATCH_PARENT;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ranking_view);

        ActionBar actionBar=getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        ranksDAO=new RanksDAO(this);
        settingDAO=new SettingDAO(this);

        Intent intent=getIntent();
        int level=intent.getIntExtra("level",0);
        int id=intent.getIntExtra("id",0);
        if(level!=0){
            setRanks(level);
        }else{
            setRanks(1);
        }


        rankingList=findViewById(R.id.rankingdatalist);
        rankingList.setScrollingCacheEnabled(false);
        rankingList.setAdapter(new RankAdapter());

        lv1Button=findViewById(R.id.level1);
        lv1Button.setTag(TAG_LV1);
        lv2Button=findViewById(R.id.level2);
        lv2Button.setTag(TAG_LV2);
        lv3Button=findViewById(R.id.level3);
        lv3Button.setTag(TAG_LV3);
    }
    @Override
    public boolean dispatchKeyEvent(KeyEvent event){
        if(event.getAction()==KeyEvent.ACTION_DOWN){
            switch (event.getKeyCode()){
                case KeyEvent.KEYCODE_BACK:
                    return true;
            }
        }
        return super.dispatchKeyEvent(event);
    }


    @Override
    public void onClick(View view){
        String tag=(String)view.getTag();
        if(TAG_LV1.equals(tag)){
            setRanks(1);
        }else if(TAG_LV2.equals(tag)){
            setRanks(2);
        }else if(TAG_LV3.equals(tag)){
            setRanks(3);
        }

        ((BaseAdapter)rankingList.getAdapter()).notifyDataSetChanged();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        super.onCreateOptionsMenu(menu);
        MenuItem item0=menu.add(0,MENU_DELETE,0,"削除");
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem){
        int itemId=menuItem.getItemId();
        switch (itemId){
            case android.R.id.home:
                Intent intent=new Intent(this,StartActivity.class);
                startActivity(intent);
                break;
            case MENU_DELETE:
                deleteAlert();
                break;
        }
        return true;
    }

    private void setRanks(int level){
        ranks=new ArrayList<>();
        ranks=ranksDAO.getListLevel(level);
    }


    //削除処理用アラート
    private void deleteAlert(){
        final CharSequence[] deleteselection={"レベル１削除","レベル２削除","レベル３削除","全削除"};
        final int defaultSelect=0;
        final List<Integer> selectlist=new ArrayList<>();
        selectlist.add(defaultSelect);
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setTitle("削除確認")
                .setSingleChoiceItems(deleteselection, defaultSelect, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        selectlist.clear();
                        selectlist.add(i);
                    }
                })
                .setPositiveButton("削除", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        switch (selectlist.get(0)){
                            case 0:
                                deleteUserChecker(1);
                                break;
                            case 1:
                                deleteUserChecker(2);
                                break;
                            case 2:
                                deleteUserChecker(3);
                                break;
                            case 3:
                                deleteUserChecker(0);
                                break;
                        }
                    }
                })
                .setNegativeButton("キャンセル", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
        builder.show();
    }

    //削除ユーザー確認用Alert
    private void deleteUserChecker(final int deletefor){
        LinearLayout layout=new LinearLayout(this);
        layout.setGravity(Gravity.CENTER_VERTICAL);

        final EditText passEdit=new EditText(this);
        passEdit.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        passEdit.setLayoutParams(new LinearLayout.LayoutParams(MP,MP));
        layout.addView(passEdit);
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setTitle("パスワードを入力")
                .setView(layout)
                .setPositiveButton("実行", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        boolean passOK=settingDAO.passCheck(passEdit.getText().toString());
                        if(passOK){
                            //DB削除
                            if(deletefor==0){
                                ranksDAO.deleteAll();
                                setRanks(1);
                                setRanks(2);
                                setRanks(3);
                            }else{
                                ranksDAO.deleteByLevel(deletefor);
                                setRanks(deletefor);
                            }
                            toast(RankingViewActivity.this,"削除しました");
                            ((BaseAdapter)rankingList.getAdapter()).notifyDataSetChanged();
                        }else{
                            toast(RankingViewActivity.this,"パスワードが違います");
                        }
                    }
                });
        builder.show();
    }
    //Listview用アダプター
    private class RankAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return ranks.size();
        }

        @Override
        public Object getItem(int i) {
            return ranks.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            Context context=RankingViewActivity.this;
            RankingEB item=ranks.get(i);

            if(view==null){
                LinearLayout layout=new LinearLayout(context);
                layout.setPadding(10,10,10,10);
                layout.setGravity(Gravity.CENTER_VERTICAL);
                layout.setTag("listlayout");
                view=layout;

                TextView rankNo=new TextView(context);
                rankNo.setTag("rank");
                rankNo.setPadding(10,20,10,20);
                layout.addView(rankNo);

                TextView textView=new TextView(context);
                textView.setTag("name");
                textView.setPadding(10,20,10,20);
                layout.addView(textView);

                LinearLayout layout1=new LinearLayout(context);
                layout1.setLayoutParams(new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT
                ));
                layout1.setOrientation(LinearLayout.HORIZONTAL);
                layout1.setGravity(Gravity.RIGHT);
                layout.addView(layout1);

                TextView scoreText=new TextView(context);
                scoreText.setTag("score");
                scoreText.setPadding(10,20,10,20);
                layout1.addView(scoreText);


            }

            TextView rankNo=(TextView)view.findViewWithTag("rank");
            rankNo.setText(i+1+"位");

            TextView textView=(TextView)view.findViewWithTag("name");
            textView.setText(item.getName()+":"+item.getDate());

            TextView scoreText=(TextView)view.findViewWithTag("score");
            scoreText.setText(String.valueOf(item.getScore())+"点");

            if(i==0){//一位
                LinearLayout layout=(LinearLayout)view.findViewWithTag("listlayout");
                layout.setBackgroundColor(Color.YELLOW);
            }else if(i==1){//二位
                LinearLayout layout=(LinearLayout)view.findViewWithTag("listlayout");
                layout.setBackgroundColor(Color.GRAY);
            }else if(i==2){//三位
                LinearLayout layout=(LinearLayout)view.findViewWithTag("listlayout");
                layout.setBackgroundColor(Color.CYAN);
            }else{
                LinearLayout layout=(LinearLayout)view.findViewWithTag("listlayout");
                layout.setBackgroundColor(Color.argb(0,0,0,0));
            }
            return view;
        }
    }

    private static void toast(Context context, String text){
        Toast toast=Toast.makeText(context,text,Toast.LENGTH_SHORT);
        toast.show();
    }
}
