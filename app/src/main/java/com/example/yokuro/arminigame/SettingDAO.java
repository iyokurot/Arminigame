package com.example.yokuro.arminigame;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SettingDAO {
    private final static String DB_NAME="settings.db";
    private final static int DB_VERSION=1;
    private final static String DB_TABLE="settings";

    private SQLiteDatabase db;

    SettingDAO(Context context){
        DBHelper dbHelper=new DBHelper(context);
        db=dbHelper.getWritableDatabase();
        //create();
    }

    //Create
    public void create(){
        Cursor c=db.query(DB_TABLE,new String[]{"pass","nowid"},null,null,null,null,null);
        if(c.getCount()==0){
            ContentValues values=new ContentValues();
            values.put("pass","hogehoge");
            values.put("nowid",0);
            db.insert(DB_TABLE,"",values);
        }
        c.close();
    }
    //Read
    public String getpass(){
        Cursor c=db.query(DB_TABLE,new String[]{"pass","nowid"},null,null,null,null,null);
        c.moveToFirst();
        String pass=c.getString(0);
        c.close();
        return pass;
    }
    public int getSize(){
        Cursor c=db.query(DB_TABLE,new String[]{"pass","nowid"},null,null,null,null,null);
        return c.getCount();
    }
    //pass
    public boolean passCheck(String str){
        Cursor c=db.query(DB_TABLE,new String[]{"pass","nowid"},null,null,null,null,null);
        c.moveToFirst();
        String pass=c.getString(0);

        if(pass.equals(str)){
            return true;
        }else{
            return false;
        }
    }
    //id
    public int getNewId(){
        Cursor c=db.query(DB_TABLE,new String[]{"pass","nowid"},null,null,null,null,null);
        c.moveToFirst();
        int newId=c.getInt(1);
        return newId;
    }

    //Update
    public void updateId(){
        Cursor c=db.query(DB_TABLE,new String[]{"pass","nowid"},null,null,null,null,null);
        c.moveToFirst();
        ContentValues values=new ContentValues();
        values.put("nowid",c.getInt(1)+1);
        db.update(DB_TABLE,values,null,null);
    }

    //UpdatePass
    public boolean updatePass(String newPass){
        Cursor c=db.query(DB_TABLE,new String[]{"pass"},null,null,null,null,null);
        c.moveToFirst();
        ContentValues values=new ContentValues();
        values.put("pass",newPass);
        db.update(DB_TABLE,values,null,null);
        return true;
    }




    //DBHelper
    private static class DBHelper extends SQLiteOpenHelper {
        public DBHelper(Context context){
            super(context,DB_NAME,null,DB_VERSION);
        }
        @Override
        public void onCreate(SQLiteDatabase db){
            db.execSQL("create table if not exists "+DB_TABLE+"(pass text,nowid int)");
        }
        @Override
        public void onUpgrade(SQLiteDatabase db,int oldVersion,int newVersion){
            db.execSQL("drop table if exists "+DB_TABLE);
            onCreate(db);
        }
    }
}
