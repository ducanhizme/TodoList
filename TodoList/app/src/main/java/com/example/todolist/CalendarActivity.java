package com.example.todolist;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CalendarView;
import android.widget.ImageButton;
import android.widget.ListView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import android.widget.CalendarView.OnDateChangeListener;

public class CalendarActivity extends AppCompatActivity implements onMenuItem {
    ImageButton buttonBack;
    CalendarView cv;
    ListView lv;
    ArrayList<Todo> listTodo = new ArrayList<>();
    TodoAdapter adapter;
    int idCurrent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);
        mapping();
        getIdCurrent();
        getDataFromDB();
        onDateChange();
        onBackBtn();
        initView();
    }

    private void getIdCurrent() {
        idCurrent = getIntent().getIntExtra("id",0);
    }


    @Override
    protected void onPostResume() {
        super.onPostResume();
        getDataFromDB();
    }


    private String getDateFromCal(){
        long timesMilliseconds = cv.getDate();
        Date d = new Date(timesMilliseconds);
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy", new Locale("vi","VN"));
        return format.format(d);
    }

    private void getDataFromDB() {
        listTodo.clear();
        DBHelper db = new DBHelper(this);
        String day = getDateFromCal();
        listTodo.clear();
        for(Todo e :db.getAllTodo()){
            if(e.getDateObject().equals(day)){
                listTodo.add(e);
            }
        }
    }

    private void onDateChange(){
       DBHelper db = new DBHelper(this);
        cv.setOnDateChangeListener((view, year, month, dayOfMonth) -> {
            listTodo.clear();
            int monthR = month+1;
            Date date = new GregorianCalendar(year,monthR-1,dayOfMonth,0,0).getTime();
            SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy", new Locale("vi","VN"));
            String date_ = format.format(date);
            adapter.updateList(db.getToDoByDate(idCurrent,date_));
        });
    }

    private void initView() {
        initLv();
    }

    private void initLv() {
        adapter = new TodoAdapter(listTodo,R.layout.layout_cal,this,this);
        lv.setAdapter(adapter);

    }

    private void onBackBtn() {
        buttonBack.setOnClickListener(v->{
            finish();
        });
    }

    private void mapping() {
        buttonBack = findViewById(R.id.imageButton);
        cv = findViewById(R.id.calendarView);
        lv = findViewById(R.id.lvCal);
    }

    @Override
    public void onClickMenu(Todo todo, View view) {

    }
}