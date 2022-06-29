package com.example.todolist;

import static java.util.stream.Collectors.mapping;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    AppCompatButton loginBtn;
    AppCompatButton signUpBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mapping();
        onLoginBtn();
        onSignUp();
    }

    private void onSignUp() {
        signUpBtn.setOnClickListener(v->{
            dialogSignIn();
        });
    }

    private void onLoginBtn() {
        loginBtn.setOnClickListener(v->{
            dialogLogin();
        });
    }

    private void mapping() {
        loginBtn = findViewById(R.id.loginBtn);
        signUpBtn = findViewById(R.id.signUpBtn);
    }

    private void dialogLogin(){
        DBHelper dbHelper = new DBHelper(this);
        Dialog loginDialog = new Dialog(this);
        loginDialog.setContentView(R.layout.login_dialog);
        loginDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        TextInputEditText nameEdit = loginDialog.findViewById(R.id.nameLG);
        TextInputEditText password = loginDialog.findViewById(R.id.passwordLG);
        AppCompatButton loginBtnDialog = loginDialog.findViewById(R.id.loginBtnDialog);
        AppCompatButton cancelBtn = loginDialog.findViewById(R.id.cancelBtn);
        loginDialog.show();
        cancelBtn.setOnClickListener(v->{
            loginDialog.dismiss();
        });
        loginBtnDialog.setOnClickListener(v->{
            String name = Objects.requireNonNull(nameEdit.getText()).toString();
            String password_ = Objects.requireNonNull(password.getText()).toString();
            User user = new User(name,password_);
            if(!TextUtils.isEmpty(name)&&!TextUtils.isEmpty(password_)){
                if(dbHelper.checkAlreadyExistsAccount(user)){
                    toHomeActivity(user.getName());
                }else{
                    Toast.makeText(this, "Can't not find this account", Toast.LENGTH_SHORT).show();
                }
            }else{
                Toast.makeText(this, "Empty username or password", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void toHomeActivity(String name) {
        Intent intent = new Intent(this,HomeActivity.class);
        intent.putExtra("nameUser",name);
        startActivity(intent);
    }

    private void dialogSignIn(){
        Dialog signInDialog = new Dialog(this);
        signInDialog.setContentView(R.layout.sign_up_dialog);
        signInDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        TextInputEditText nameEdit = signInDialog.findViewById(R.id.nameEditTextSI);
        TextInputEditText password = signInDialog.findViewById(R.id.passwordEditSI);
        AppCompatButton signInBtn = signInDialog.findViewById(R.id.signInBtn);
        AppCompatButton cancelBtn = signInDialog.findViewById(R.id.cancelBtn);
        signInDialog.show();
        cancelBtn.setOnClickListener(v->{
            signInDialog.dismiss();
        });
        signInBtn.setOnClickListener(v->{
           String name = Objects.requireNonNull(nameEdit.getText()).toString();
           String password_ = Objects.requireNonNull(password.getText()).toString();
           User user = new User(name,password_);
           DBHelper dbHelper = new DBHelper(this);
           if(!TextUtils.isEmpty(name) && !TextUtils.isEmpty(password_)){
               dbHelper.insertUser(user);
               toHomeActivity(user.getName());
           }else{
               Toast.makeText(this, "Empty username or password", Toast.LENGTH_SHORT).show();
           }
        });
    }



}