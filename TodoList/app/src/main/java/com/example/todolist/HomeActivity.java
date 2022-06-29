package com.example.todolist;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;

public class HomeActivity extends AppCompatActivity {
    private String username ;
    private TextView userNameTXT;
    private ImageButton logout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        mapping();
        initView();
    }

    private void initView() {
        setName();
        onLogOutBtn();
    }

    private void onLogOutBtn() {
        logout.setOnClickListener(v->{
            finish();
        });
    }

    private void setName() {
        username = getIntent().getStringExtra("nameUser");
        userNameTXT.setText(username);
    }

    private void mapping() {
        userNameTXT = findViewById(R.id.userName);
        logout = findViewById(R.id.logOutBtn);
    }


}