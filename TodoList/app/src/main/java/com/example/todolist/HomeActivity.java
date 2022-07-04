package com.example.todolist;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.android.material.timepicker.TimeFormat;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;


public class HomeActivity extends AppCompatActivity implements onMenuItem{
    private String username ;
    private TextView userNameTXT;
    private TextView todayTxt;
    private ImageButton logout;
    private AppCompatButton btn_cal;
    private AppCompatButton btn_done;
    private FloatingActionButton btn_add;
    private ArrayList<Todo> listToDo= new ArrayList<>();
    private TodoAdapter adapter;
    private int idCurrent;
    private ListView lvToday;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        getDataFromDB();
        mapping();
        today();
        initView();

    }

    private void today() {
        todayTxt.setText(getCurrentDate());
    }

    @Override
    protected void onStart() {
        super.onStart();
        setName();
        getDataFromDB();
    }

    private void getDataFromDB() {
        DBHelper db = new DBHelper(this);
        for (Todo t : db.getAllTodo()) {
            if(t.getIdUser() == idCurrent ){
                if(Objects.equals(t.getDateObject(), getCurrentDate())){
                    listToDo.add(t);
                    Log.e("check1", t +getCurrentDate());
                }
            }
        }
    }


    private String getCurrentDate() {
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy", new Locale("vi","VN"));
        Date date = new Date();
        return format.format(date);
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
//        getDataFromDB();
    }

    private void initView() {
        onLogOutBtn();
        initLv();
        onDoneBtn();
        onCalBtn();
        onAddBtn();
    }

    private void initLv() {
        adapter = new TodoAdapter(listToDo,R.layout.layout_today,this,this);
        lvToday.setAdapter(adapter);
    }

    private void onAddBtn() {
        btn_add.setOnClickListener(v->{
            dialogAddTodo();
        });
    }

    @SuppressLint("SetTextI18n")
    private void dialogAddTodo() {
        Dialog dialogAdd = new Dialog(this);
        dialogAdd.setContentView(R.layout.add_dialog);
        AppCompatButton startTime = dialogAdd.findViewById(R.id.startBtn);
        AppCompatButton endTime = dialogAdd.findViewById(R.id.endBtn);
        TextView startTxt = dialogAdd.findViewById(R.id.startTime);
        TextView endTxt = dialogAdd.findViewById(R.id.endTime);
        EditText nameEdit = dialogAdd.findViewById(R.id.nameTodo);
        AppCompatButton add = dialogAdd.findViewById(R.id.addBtn);
        AppCompatButton cancel = dialogAdd.findViewById(R.id.cancelButton);
        dialogAdd.show();
        startTime.setOnClickListener(v->{
            MaterialTimePicker picker = showPicker();
           picker.show(getSupportFragmentManager(),"Start Time");
           picker.addOnPositiveButtonClickListener(v1 -> {
               startTxt.setText(picker.getHour()+":"+picker.getMinute());
           });
        });
        endTime.setOnClickListener(v->{
            MaterialTimePicker picker = showPicker();
            picker.show(getSupportFragmentManager(),"End Time");
            picker.addOnPositiveButtonClickListener(v2->{
                endTxt.setText(picker.getHour()+":"+picker.getMinute());
            });
        });

        cancel.setOnClickListener(v->{
            dialogAdd.dismiss();
        });
        add.setOnClickListener(v->{
            DBHelper db = new DBHelper(this);
            int idUser = idCurrent;
            String name = nameEdit.getText().toString();
            String start_ = startTxt.getText().toString();
            String end_ = endTxt.getText().toString();
            Todo todo = new Todo(idUser,name,start_,end_, 0);
            db.insertToDo(todo);
            adapter.getList().add(todo);
            adapter.notifyDataSetChanged();
            dialogAdd.dismiss();
        });
    }

    private MaterialTimePicker showPicker() {
        int clock;
        if(android.text.format.DateFormat.is24HourFormat(this))
            clock = TimeFormat.CLOCK_24H;
        else
            clock = TimeFormat.CLOCK_12H;
        MaterialTimePicker picker = new MaterialTimePicker.Builder().setTimeFormat(clock)
                .setHour(12)
                .setMinute(0)
                .setTitleText("Select Start Time").build();
        return picker;
    }

    private void onCalBtn() {
        btn_cal.setOnClickListener(v->{
            calenderActivityTrans();
        });
    }

    private void onLogOutBtn() {
        logout.setOnClickListener(v->{
            finish();
        });
    }
    
    private void onDoneBtn(){
        btn_done.setOnClickListener(v->{
            doneActivityTrans();
        });
    }

    private void doneActivityTrans() {
        Intent intent = new Intent(this,DoneActivity.class);
        intent.putExtra("id_",idCurrent);

        startActivity(intent);
    }

    private void calenderActivityTrans() {
        Intent intent = new Intent(this,CalendarActivity.class);
        intent.putExtra("id",idCurrent);
        startActivity(intent);
    }

    private void setName() {
        username = getIntent().getStringExtra("nameUser");
        idCurrent = getIntent().getIntExtra("id",0);
        userNameTXT.setText(username);
    }

    private void mapping() {
        userNameTXT = findViewById(R.id.userName);
        logout = findViewById(R.id.logOutBtn);
        btn_done = findViewById(R.id.done_btn);
        btn_cal = findViewById(R.id.cal_btn);
        btn_add = findViewById(R.id.floatingActionButton);
        lvToday = findViewById(R.id.lvToday);
        todayTxt = findViewById(R.id.todayTxt);
    }


    @Override
    public void onClickMenu(Todo todo, View view) {
        DBHelper db = new DBHelper(this);
        PopupMenu popupMenu = new PopupMenu(this, view);
        popupMenu.getMenuInflater().inflate(R.menu.menu, popupMenu.getMenu());
        popupMenu.show();
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.delete_btn:
                        db.deleteTodo(todo);
                        adapter.getList().remove(todo);
                        adapter.notifyDataSetChanged();
                        break;
                    case R.id.update_btn:
                        Dialog dialogAdd = new Dialog(HomeActivity.this);
                        dialogAdd.setContentView(R.layout.add_dialog);
                        AppCompatButton startTime = dialogAdd.findViewById(R.id.startBtn);
                        AppCompatButton endTime = dialogAdd.findViewById(R.id.endBtn);
                        TextView startTxt = dialogAdd.findViewById(R.id.startTime);
                        TextView endTxt = dialogAdd.findViewById(R.id.endTime);
                        EditText nameEdit = dialogAdd.findViewById(R.id.nameTodo);
                        AppCompatButton add = dialogAdd.findViewById(R.id.addBtn);
                        AppCompatButton cancel = dialogAdd.findViewById(R.id.cancelButton);
                        dialogAdd.show();
                        startTxt.setText(todo.getStartTime());
                        endTxt.setText(todo.getEndTime());
                        nameEdit.setText(todo.getName());
                        startTime.setOnClickListener(v->{
                            MaterialTimePicker picker = showPicker();
                            picker.show(getSupportFragmentManager(),"Start Time");
                            picker.addOnPositiveButtonClickListener(v1 -> {
                                startTxt.setText(picker.getHour()+":"+picker.getMinute());
                            });
                        });
                        endTime.setOnClickListener(v->{
                            MaterialTimePicker picker = showPicker();
                            picker.show(getSupportFragmentManager(),"End Time");
                            picker.addOnPositiveButtonClickListener(v2->{
                                endTxt.setText(picker.getHour()+":"+picker.getMinute());
                            });
                        });
                        cancel.setOnClickListener(v->{
                            dialogAdd.dismiss();
                        });
                        add.setOnClickListener(v->{
                            listToDo.clear();
                            DBHelper db = new DBHelper(HomeActivity.this);
                            int idUser = idCurrent;
                            String name = nameEdit.getText().toString();
                            String start_ = startTxt.getText().toString();
                            String end_ = endTxt.getText().toString();
                            Todo todo_ = new Todo(idUser,name,start_,end_, 0);
                            db.updateTodo(todo_,todo.getId());
                            listToDo.clear();
                            getDataFromDB();
                            initLv();
                            dialogAdd.dismiss();
                        });
                        break;
                    case R.id.done_btn:
                        DBHelper db = new DBHelper(HomeActivity.this);
                        db.isTodoDone(todo,todo.getId());
                        listToDo.clear();
                        getDataFromDB();
                        initLv();
                        break;
                }
                return true;
            }
        });

    }
}