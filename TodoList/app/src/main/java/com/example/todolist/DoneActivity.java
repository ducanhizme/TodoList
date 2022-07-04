package com.example.todolist;

import static java.util.stream.Collectors.mapping;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
import java.util.ArrayList;
public class DoneActivity extends AppCompatActivity implements onMenuItem{
    ImageButton backBtn;
    SearchView sv;
    ListView lv;
    TodoAdapter adapter;
    ArrayList<Todo> listToDoDone = new ArrayList<>();
    int idCurrent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_done);
        mapping();
        getIdIntent();
        getDataFromDB();
        initView();
        getIdIntent();
    }


    @Override
    protected void onStart() {
        super.onStart();
        getIdIntent();
    }

    private void getIdIntent() {
        idCurrent = getIntent().getIntExtra("id_",0);
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        getDataFromDB();
        initLv();
    }

    private void getDataFromDB() {
        DBHelper db = new DBHelper(this);
        listToDoDone = db.getDoneTodo(idCurrent,1);
    }

    private void initView() {
        onBackBtn();
        initLv();
        onSearchAction();
    }

    private void initLv() {
        adapter = new TodoAdapter(listToDoDone,R.layout.layout_cal,this,this);
        lv.setAdapter(adapter);
    }

    private void onSearchAction() {
        sv.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                ArrayList<Todo> filter = new ArrayList<>();
                for(Todo e : listToDoDone){
                    if(e.getName().toLowerCase().contains(newText.toLowerCase())){
                        filter.add(e);
                    }
                }
                adapter.getList().clear();
                adapter.updateList(filter);
                if(TextUtils.isEmpty(newText)){
                    adapter.getList().clear();
                    getDataFromDB();
                    adapter.updateList(listToDoDone);
                }
                return true;
            }
        });
    }

    private void onBackBtn() {
        backBtn.setOnClickListener(v->{
            finish();
        });
    }

    private void mapping() {
        backBtn = findViewById(R.id.backBtnDone);
        sv = findViewById(R.id.sv);
        lv = findViewById(R.id.listViewDone);
    }

    @Override
    public void onClickMenu(Todo todo, View view) {

    }
}