package com.example.todolist;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class DBHelper extends SQLiteOpenHelper {
    private static final String DB_NAME ="Todolist.db";
    private static final String TB_TODO="todo_tb";
    private static final String _ID="id";
    private static final String _NAME ="name";
    private static final String _PASSWORD ="password";
    private static final String TB_TODO_USER ="todoList_tb";
    private static final String _ID_TODO ="id";
    private static final String _ID_USER ="idUser";
    private static final String _NAME_TODO ="name";
    private static final String _START_TIME ="StartTime";
    private static final String _END_TIME ="EndTime";
    private static final String _DATE ="date";
    private static final String _TYPE ="type";

    public DBHelper(@Nullable Context context) {
        super(context,DB_NAME,null,1);
    }

    @SuppressLint("SQLiteString")
    @Override
    public void onCreate(@NonNull SQLiteDatabase db) {
        String query = "CREATE TABLE "+TB_TODO +" ("+_ID+" INTEGER PRIMARY KEY AUTOINCREMENT, " +_NAME+" TEXT, "+_PASSWORD+" TEXT );";
        String query2 = "CREATE TABLE "+TB_TODO_USER+" ("+_ID_TODO+" INTEGER PRIMARY KEY AUTOINCREMENT, "+_ID_USER+" INTEGER, "
                +_NAME_TODO+" TEXT, "+_START_TIME+" STRING, "+ _END_TIME+" STRING, "+_DATE+" TEXT, "+_TYPE+" INTEGER );";
        db.execSQL(query);
        db.execSQL(query2);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    public void insertUser(@NonNull User user){
        SQLiteDatabase db =getWritableDatabase();
        ContentValues content = new ContentValues();
        content.put(_NAME,user.getName());
        content.put(_PASSWORD,user.getPassword());
        db.insert(TB_TODO,null,content);
    }

    public int getIdUser(String name, String password){
        SQLiteDatabase db = getReadableDatabase();
        String query = "SELECT * FROM "+TB_TODO+" WHERE "+_NAME +"= ? AND " +_PASSWORD +" = ?";
        Cursor cursor = db.rawQuery(query, new String[]{name,password});
        if(cursor != null && cursor.moveToFirst()){
            return cursor.getInt(0);
        }
        return -1;
    }

    public boolean checkAlreadyExistsAccount(User user) {
        SQLiteDatabase db = getReadableDatabase();
        String query = "SELECT * FROM "+TB_TODO+" WHERE "+_NAME +"= ? AND " +_PASSWORD +" = ?";
        Cursor cursor = db.rawQuery(query, new String[]{user.getName(),user.getPassword()});
        if(cursor.getCount() > 0) return true;
        else return false;
    }

    public void insertToDo(Todo todo){
        int check;
        SQLiteDatabase db = getWritableDatabase();
        ContentValues content = new ContentValues();
        content.put(_ID_USER,todo.getIdUser());
        content.put(_NAME_TODO,todo.getName());
        content.put(_START_TIME,todo.getStartTime());
        content.put(_END_TIME,todo.getEndTime());
        content.put(_DATE,todo.getDate());
        if(todo.isType()) check =1;
        else check =0;
        content.put(_TYPE,check);

        db.insert(TB_TODO_USER,null,content);
    }

    public ArrayList<Todo> getAllTodo(){
        ArrayList<Todo> listTodo = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        String query = "SELECT * FROM "+TB_TODO_USER;
        @SuppressLint("Recycle") Cursor cursor = db.rawQuery(query,null);
        while(cursor.moveToNext()){
            int id = cursor.getInt(0);
            int idUser = cursor.getInt(1);
            String nameTodo = cursor.getString(2);
            String startTime = cursor.getString(3);
            String endTime = cursor.getString(4);
            String date = cursor.getString(5);
            listTodo.add(new Todo(id,idUser,nameTodo,startTime,endTime,date));
        }
        return listTodo;

    }

    public ArrayList<Todo> getToDoByDate(int idUser_, String date_){
        ArrayList<Todo> listTodo = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        String query = "SELECT * FROM "+TB_TODO_USER+" WHERE "+_ID_USER +"= ? AND " +_DATE +" = ?";
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(idUser_),date_});
        while (cursor.moveToNext()){
            int id = cursor.getInt(0);
            int idUser = cursor.getInt(1);
            String nameTodo = cursor.getString(2);
            String startTime = cursor.getString(3);
            String endTime = cursor.getString(4);
            String date = cursor.getString(5);
            listTodo.add(new Todo(id,idUser,nameTodo,startTime,endTime,date));
        }
        return listTodo;
    }

    public boolean deleteTodo(Todo todo){
        SQLiteDatabase db = getWritableDatabase();
        return db.delete(TB_TODO_USER,_ID_TODO+"="+todo.getId(),null)>0;
    }
}
