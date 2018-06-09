package com.example.jimison.sqlitedemo;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper{

    private final static String DB_NAME = "web.db";
    private final static int VERSION = 1;


    public DBHelper(Context context) {
        super(context, DB_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //android中_id必須要加底線，其他的sql則不用
        String sql = "create table member(_id integer primary key autoincrement,name varchar(20),tel varchar(20),email varchar(50))";
        db.execSQL(sql); //開啟資料庫並執行sql這串指令
    }

    @Override //可寫可不寫，建議不寫
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //這次範例當版本有差異時,刪掉資料表,但實務上是不會刪的
        String sql = "drop table if exists member";
        db.execSQL(sql);
        onCreate(db); //重新呼叫onCreate()，重新建立資料表
    }
}
