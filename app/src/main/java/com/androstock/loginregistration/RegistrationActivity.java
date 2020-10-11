package com.androstock.loginregistration;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import cn.pedant.SweetAlert.SweetAlertDialog;


public class RegistrationActivity extends AppCompatActivity implements View.OnClickListener {
    EditText edtUsername, edtEmail,edtPwd,edtConfirmPwd;
    Button   btnSignUp;
    SQLiteDatabase bd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        TextView signIn_text = findViewById(R.id.signIn_text);
        signIn_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegistrationActivity.this, LoginActivity.class));
                finish();
            }
        });


        edtUsername = (EditText)findViewById(R.id.usernameID);
        edtEmail = (EditText)findViewById(R.id.emailID);
        edtPwd = (EditText)findViewById(R.id.pwdID);
        edtConfirmPwd = (EditText)findViewById(R.id.pwdConfirmID);
        btnSignUp = (Button) findViewById(R.id.btnSignUpID);

        btnSignUp.setOnClickListener(this);

        bd = openOrCreateDatabase("users_management", Context.MODE_PRIVATE,null);
        bd.execSQL("Create Table IF NOT EXISTS users(id_user integer primary key autoincrement ,username VARCHAR,email varchar,password varchar);");

    }

    public void onClick(View view){
        if(view==btnSignUp){
            if(edtUsername.getText().toString().trim().length()==0 || edtEmail.getText().toString().trim().length()==0|| edtPwd.getText().toString().trim().length()==0 || edtConfirmPwd.getText().toString().trim().length()==0){

                new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("Attention !")
                        .setContentText("Fill all the fields please !!!")
                        .setConfirmText("Okey")
                        .show();


                return;
            }
            if(! edtPwd.getText().toString().equals(edtConfirmPwd.getText().toString()))
            {
                new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("Attention !")
                        .setContentText("Password doesn't match")
                        .setConfirmText("Okey")
                        .show();
                return;
            }

            String emaill = edtEmail.getText().toString().trim();

            String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

            if(!emaill.matches(emailPattern))
            {
                new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("Attention !")
                        .setContentText("Check your Email Format please !!!")
                        .setConfirmText("Okey")
                        .show();
                return;
            }

            if(!checkMail(edtEmail.getText().toString()))
            {
                new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("Attention !")
                        .setContentText("Email Already exists !!! ")
                        .setConfirmText("Okey")
                        .show();
                return;
            }

            bd.execSQL("INSERT INTO users(username,email,password) VALUES ('"+edtUsername.getText()+"','"+edtEmail.getText()+"','"+edtPwd.getText()+"'); ");
            new SweetAlertDialog(RegistrationActivity.this, SweetAlertDialog.SUCCESS_TYPE)
                    .setTitleText("Well Done...")
                    .setContentText("Account was successfully created !!!")
                    .show();
            videTexe();
        }



    }

    public Boolean checkMail(String email){
        SQLiteDatabase bd;
        bd = openOrCreateDatabase("users_management", Context.MODE_PRIVATE,null);
        bd.execSQL("Create Table IF NOT EXISTS users(id_user integer primary key autoincrement ,username VARCHAR,email varchar,password varchar);");

        Cursor c = bd.rawQuery("Select * from users where email=?",new String[]{email});
        if(c.getCount()>0) return false;
        else return true;
    }

    public void videTexe(){
        edtUsername.setText("");
        edtEmail.setText("");
        edtPwd.setText("");
        edtConfirmPwd.setText("");
    }

}
