package com.example.todolist;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DBHelper extends SQLiteOpenHelper {
    private static final String DB_NAME ="Todolist.db";
    private static final String TB_TODO="todo_tb";
    private static final String _ID="id";
    private static final String _NAME ="name";
    private static final String _PASSWORD ="password";

    public DBHelper(@Nullable Context context) {
        super(context,DB_NAME,null,1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE "+TB_TODO +" ("+_ID+" INTEGER PRIMARY KEY AUTOINCREMENT, " +_NAME+" TEXT, "+_PASSWORD+" TEXT );";
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    public void insertUser(User user){
        SQLiteDatabase db =getWritableDatabase();
        ContentValues content = new ContentValues();
        content.put(_NAME,user.getName());
        content.put(_PASSWORD,user.getPassword());
        db.insert(TB_TODO,null,content);
    }

    public boolean checkAlreadyExistsAccount(User user) {
        SQLiteDatabase db = getReadableDatabase();
        String query = "SELECT * FROM "+TB_TODO+" WHERE "+_NAME +"= ? AND " +_PASSWORD +" = ?";
        Cursor cursor = db.rawQuery(query, new String[]{user.getName(),user.getPassword()});
        if(cursor.getCount() > 0) return true;
        else return false;
    }
}
