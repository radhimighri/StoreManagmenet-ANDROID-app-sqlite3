package com.androstock.loginregistration;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import cn.pedant.SweetAlert.SweetAlertDialog;


public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        TextView signUp_text = findViewById(R.id.signUp);
        signUp_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, RegistrationActivity.class));
                finish();
            }
        });

            }

    public void OnclickLogin(View view) {

        final Intent RegIntent = new Intent(this, RegistrationActivity.class);
        EditText txtuser = (EditText) findViewById(R.id.NameTextField);
        EditText txtpwd = (EditText) findViewById(R.id.PwdTextField);
        if(checkUserUsername(txtuser.getText().toString())||checkUserPwd(txtpwd.getText().toString())) {

            new SweetAlertDialog(LoginActivity.this, SweetAlertDialog.WARNING_TYPE)
                    .setTitleText("Verify Your informations please ?")
                    .setContentText("Do you have an account ?")
                    .setConfirmText("No,Create")
                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sDialog) {
                            sDialog.dismissWithAnimation();
                            startActivity(RegIntent);

                        }
                    })
                    .setCancelButton("Yes", new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sDialog) {
                            sDialog.dismissWithAnimation();
                        }
                    })
                    .show();
        }
        else {
            Intent ProdIntent = new Intent(this, ProductsManagerActivity.class);
            Bundle b = new Bundle();
            b.putString("username", txtuser.getText().toString());
            ProdIntent.putExtras(b);
            startActivity(ProdIntent);

            SweetAlertDialog pDialog = new SweetAlertDialog(LoginActivity.this, SweetAlertDialog.SUCCESS_TYPE);
            pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
            pDialog.setTitleText("Loading ...");

            pDialog.show();
        }
    }

    public Boolean checkUserUsername(String username){
        SQLiteDatabase bd;
        bd = openOrCreateDatabase("users_management", Context.MODE_PRIVATE,null);
        bd.execSQL("Create Table IF NOT EXISTS users(id_user integer primary key autoincrement ,username VARCHAR,email varchar,password varchar);");

        Cursor c = bd.rawQuery("Select * from users where username=?",new String[]{username});
        if(c.getCount()>0) return false;
        else return true;
    }
    public Boolean checkUserPwd(String pwd){
        SQLiteDatabase bd;
        bd = openOrCreateDatabase("users_management", Context.MODE_PRIVATE,null);
        bd.execSQL("Create Table IF NOT EXISTS users(id_user integer primary key autoincrement ,username VARCHAR,email varchar,password varchar);");

        Cursor c = bd.rawQuery("Select * from users where password=?",new String[]{pwd});
        if(c.getCount()>0) return false;
        else return true;
    }


}
