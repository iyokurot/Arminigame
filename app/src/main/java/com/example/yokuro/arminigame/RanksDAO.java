package com.example.yokuro.arminigame;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class RanksDAO {
    private final static String DB_NAME="ranking.db";
    private final static int DB_VERSION=1;
    private final static String DB_TABLE="ranking";

    private SQLiteDatabase db;

    RanksDAO(Context context){
        DBHelper dbHelper=new DBHelper(context);
        db=dbHelper.getWritableDatabase();
    }
    //C------------------------
    public void create(RankingEB item){
        ContentValues values=new ContentValues();
        Cursor c=db.query(DB_TABLE,new String[]{"id","name","difficult","score","date"},null,null,null,null,null);
        values.put("id",item.getID());
        values.put("name",item.getName());
        values.put("difficult",item.getLevel());
        values.put("score",item.getScore());
        values.put("date",item.getDate());
        db.insert(DB_TABLE,"",values);

    }

    //R-------------------------
    public int getSize(){
        Cursor c=db.query(DB_TABLE,new String[]{"id","name","difficult","score","date"},null,null,null,null,null);
        return c.getCount();
    }
    public ArrayList<RankingEB> getListLevel(int level){
        ArrayList<RankingEB>list=new ArrayList<>();
        Cursor c=db.query(DB_TABLE,new String[]{"id","name","difficult","score","date"},"difficult=?",new String[]{String.valueOf(level)},null,null,"score desc");
        if(c.getCount()!=0){
            boolean exist=c.moveToFirst();
            while(exist){
                RankingEB rank=new RankingEB();
                rank.setID(c.getInt(0));
                rank.setName(c.getString(1));
                rank.setLevel(c.getInt(2));
                rank.setScore(c.getInt(3));
                rank.setDate(c.getString(4));
                list.add(rank);
                exist=c.moveToNext();
            }
        }
        c.close();
        return list;
    }

    //U-------------------------------
    public void update(){

    }
    //D------------------------------
    public void deleteByLevel(int level){
        Cursor c=db.query(DB_TABLE,new String[]{"id","name","difficult","score","date"},null,null,null,null,null);
        db.delete(DB_TABLE,"difficult=?",new String[]{String.valueOf(level)});
        c.close();
    }

    public void deleteAll(){
        Cursor c=db.query(DB_TABLE,new String[]{"id","name","difficult","score","date"},null,null,null,null,null);
        db.delete(DB_TABLE,null,null);
        c.close();
    }


    //Helper
    private static class DBHelper extends SQLiteOpenHelper{
        public DBHelper(Context context){
            super(context,DB_NAME,null,DB_VERSION);
        }
        @Override
        public void onCreate(SQLiteDatabase db){
            db.execSQL("create table if not exists "+DB_TABLE+"(id integer primary key,name text,difficult int,score integer,date text)");
        }
        @Override
        public void onUpgrade(SQLiteDatabase db,int oldVersion,int newVersion){
            db.execSQL("drop table if exists "+DB_TABLE);
            onCreate(db);
        }
    }
}
