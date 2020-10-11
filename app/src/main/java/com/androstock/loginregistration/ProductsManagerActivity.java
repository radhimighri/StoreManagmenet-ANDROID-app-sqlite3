package com.androstock.loginregistration;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class ProductsManagerActivity extends Activity implements View.OnClickListener {
    EditText  edtLabel, edtNum,edtquantity,edtPurPrice,edtSalePrice;
    Button btnAfficher, btnSupprimer, btnAjouter, btnAffichertout,btnModifier,btnVider;
    Button btnSuivant, btnPrecednt, btnPremier,btnDernier,btnLogout;
    SQLiteDatabase bd;
    Cursor c1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products_manager);

        TextView userloggedname = (TextView)findViewById(R.id.loggedInuserNameID);
        Bundle b = getIntent().getExtras();
        String user = b.getString("username");

        new SweetAlertDialog(ProductsManagerActivity.this, SweetAlertDialog.SUCCESS_TYPE)
                .setTitleText("Mr. "+b.getString("username")+" Welcome to your X-Store management interface")
                //.setContentText()
                .setConfirmText("Let's start")
                .show();
        userloggedname.setText("User: "+user);

        edtNum = (EditText)findViewById(R.id.edtNum);
        edtLabel = (EditText)findViewById(R.id.edtLabel);
        edtquantity = (EditText)findViewById(R.id.edtquantity);
        edtPurPrice= (EditText)findViewById(R.id.edtPurPrice);
        edtSalePrice= (EditText)findViewById(R.id.edtSalePrice);
        btnAjouter = (Button) findViewById(R.id.btnAjouter);
        btnSupprimer = (Button)findViewById(R.id.btnSupprimer);
        btnAfficher = (Button)findViewById(R.id.btnAfficher);
        btnAffichertout = (Button)findViewById(R.id.btnAffichertout);
        btnModifier = (Button)findViewById(R.id.btnModifier);
        btnVider = (Button)findViewById(R.id.btnVider);
        btnSuivant = (Button)findViewById(R.id.btnSuivant);
        btnPrecednt = (Button)findViewById(R.id.btnPrecedent);
        btnPremier = (Button)findViewById(R.id.btnPremier);
        btnDernier = (Button)findViewById(R.id.btnDernier);
        btnLogout = (Button)findViewById(R.id.btnLogout);
        btnAjouter.setOnClickListener(this);
        btnSupprimer.setOnClickListener(this);
        btnAffichertout.setOnClickListener(this);
        btnAfficher.setOnClickListener(this);
        btnSuivant.setOnClickListener(this);
        btnPrecednt.setOnClickListener(this);
        btnPremier.setOnClickListener(this);
        btnDernier.setOnClickListener(this);
        btnModifier.setOnClickListener(this);
        btnVider.setOnClickListener(this);
        btnLogout.setOnClickListener(this);
        bd = openOrCreateDatabase("ProductsManagment", Context.MODE_PRIVATE,null);
        bd.execSQL("Create Table IF NOT EXISTS Products(num integer primary key autoincrement ,label VARCHAR,quantity integer,pur_price varchar, sale_price varchar);");

    }

    public void onClick(View view){
        if(view==btnAjouter){
            if(edtLabel.getText().toString().trim().length()==0 || edtquantity.getText().toString().trim().length()==0|| edtPurPrice.getText().toString().trim().length()==0|| edtSalePrice.getText().toString().trim().length()==0){

                new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("Attention !")
                        .setContentText("Fill all the fields please !!!")
                        .setConfirmText("Okey")
                        .show();


                return;
            }
            bd.execSQL("INSERT INTO Products(label,quantity,pur_price,sale_price) VALUES ('"+edtLabel.getText()+"','"+edtquantity.getText()+"','"+edtPurPrice.getText()+"','"+edtSalePrice.getText()+"'); ");
            //AfficherMessage("Well Done","Product added with success");
            new SweetAlertDialog(ProductsManagerActivity.this, SweetAlertDialog.SUCCESS_TYPE)
                    .setTitleText("Well Done...")
                    .setContentText("Product added with success !!!")
                    .show();
            videTexe();
        }
        if(view==btnAffichertout){
            Cursor c=bd.rawQuery("SELECT * FROM Products",null);
            if(c.getCount()==0){
                //AfficherMessage("Error","No data found !!!");
                new SweetAlertDialog(ProductsManagerActivity.this, SweetAlertDialog.ERROR_TYPE)
                        .setTitleText("Error...")
                        .setContentText("No data found !!!")
                        .show();
                return;
            }
            StringBuffer buffer = new StringBuffer();
            while (c.moveToNext()){
                buffer.append("Product_ID :"+c.getInt(0)+"\n");
                buffer.append("Label :"+c.getString(1)+"\n");
                buffer.append("Quantity :"+c.getString(2)+"\n");
                buffer.append("Purchase_price :"+c.getString(3)+"\n");
                buffer.append("Sale_price :"+c.getString(4)+"\n\n");
                buffer.append("=================================\n\n");
            }
            c.close();
            AfficherMessage("My products :",buffer.toString());
        }

        if(view==btnSupprimer){
            if (edtNum.getText().toString().trim().length() == 0) {
                new SweetAlertDialog(ProductsManagerActivity.this, SweetAlertDialog.ERROR_TYPE)
                        .setTitleText("Oops...")
                        .setContentText("Enter the product's number please !!!")
                        .show();
                return;
            }
            new SweetAlertDialog(ProductsManagerActivity.this, SweetAlertDialog.WARNING_TYPE)
                    .setTitleText("Are you sure?")
                    .setContentText("You won't be able to recover this product!")
                    .setConfirmText("Delete!")
                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sDialog) {
                            sDialog.dismissWithAnimation();
                            Cursor c = bd.rawQuery("SELECT * FROM Products WHERE num=" + edtNum.getText(), null);
                            if (c.moveToFirst()) {
                                bd.execSQL("DELETE FROM Products where num=" + edtNum.getText());

                                new SweetAlertDialog(ProductsManagerActivity.this, SweetAlertDialog.SUCCESS_TYPE)
                                        .setTitleText("Well Done...")
                                        .setContentText("Product deleted with success !!!")
                                        .show();                                videTexe();

                            }else {
                                new SweetAlertDialog(ProductsManagerActivity.this, SweetAlertDialog.ERROR_TYPE)
                                        .setTitleText("Error...")
                                        .setContentText("Invalid Number !!!")
                                        .show();
                            }
                        }
                    })
                    .setCancelButton("Cancel", new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sDialog) {
                            sDialog.dismissWithAnimation();
                        }
                    })
                    .show();

        }


        if(view==btnAfficher){
            try{
                if (edtNum.getText().toString().trim().length() == 0) {
                    new SweetAlertDialog(ProductsManagerActivity.this, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("Oops...")
                            .setContentText("Enter the product's number please !!!")
                            .show();
                    return;
                }                Cursor c1 = bd.rawQuery("Select * from Products where num=" + edtNum.getText(), null);
                if(c1.getCount()==0){
                    //AfficherMessage("Error","No data found !!!");
                    new SweetAlertDialog(ProductsManagerActivity.this, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("Error...")
                            .setContentText("No data found !!!")
                            .show();
                    return;
                }
                if(c1.moveToFirst())
                {
                    edtNum.setText(c1.getString(0));
                    edtLabel.setText(c1.getString(1));
                    edtquantity.setText(c1.getString(2));
                    edtPurPrice.setText(c1.getString(3));
                    edtSalePrice.setText(c1.getString(4));
                }
            }
            catch (Exception se){
                Toast.makeText(ProductsManagerActivity.this, "message"+se.getMessage().toString(), Toast.LENGTH_LONG).show();
            }
        }


        if(view==btnModifier){

            if (edtNum.getText().toString().trim().length() == 0) {
                new SweetAlertDialog(ProductsManagerActivity.this, SweetAlertDialog.ERROR_TYPE)
                        .setTitleText("Error...")
                        .setContentText("Enter the product's number please !!!")
                        .show();
                //AfficherMessage("Error", "Enter the product's number please !!!");
                return;
            }
            c1 = bd.rawQuery("SELECT * FROM Products WHERE num=" + edtNum.getText(), null);
            if (c1.getCount()!=0) {
                if(!(edtLabel.getText().toString().trim().length()==0 || edtquantity.getText().toString().trim().length()==0|| edtPurPrice.getText().toString().trim().length()==0|| edtSalePrice.getText().toString().trim().length()==0)) {
                    bd.execSQL("update Products set label='" + edtLabel.getText() + "' where num= " + edtNum.getText());
                    bd.execSQL("update Products set quantity= '" + edtquantity.getText() + "' where num= '" + edtNum.getText() + "' ");
                    bd.execSQL("update Products set pur_price= '" + edtPurPrice.getText() + "' where num= '" + edtNum.getText() + "' ");
                    bd.execSQL("update Products set sale_price= '" + edtSalePrice.getText() + "' where num= '" + edtNum.getText() + "' ");
                    //AfficherMessage("Well Done", "Product updated with success");
                    new SweetAlertDialog(ProductsManagerActivity.this, SweetAlertDialog.SUCCESS_TYPE)
                            .setTitleText("Well Done...")
                            .setContentText("Product updated with success !!!")
                            .show();
                    videTexe();
                } else {
                    //AfficherMessage("Error", "Invalid Number !!! ");
                    new SweetAlertDialog(ProductsManagerActivity.this, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("Error...")
                            .setContentText("Fill all the fields please !!!")
                            .show();
                }
            }
            else {
                new SweetAlertDialog(ProductsManagerActivity.this, SweetAlertDialog.ERROR_TYPE)
                        .setTitleText("Error...")
                        .setContentText("Invalid Number !!!")
                        .show();
                //AfficherMessage("Error", "dd !!! ");
            }
            //videTexe();

        }

        if(view==btnPremier){
            c1 = bd.rawQuery("select * from Products",null);
            if(c1.getCount()==0){
                //AfficherMessage("Error","No data found !!!");
                new SweetAlertDialog(ProductsManagerActivity.this, SweetAlertDialog.ERROR_TYPE)
                        .setTitleText("Error...")
                        .setContentText("No data found !!!")
                        .show();
                return;
            }
            if(c1.moveToFirst())
            {
                edtNum.setText(c1.getString(0));
                edtLabel.setText(c1.getString(1));
                edtquantity.setText(c1.getString(2));
                edtPurPrice.setText(c1.getString(3));
                edtSalePrice.setText(c1.getString(4));
            }
        }

        if(view==btnDernier){
            c1 = bd.rawQuery("select * from Products",null);
            if(c1.getCount()==0){
                //AfficherMessage("Error","No data found !!!");
                new SweetAlertDialog(ProductsManagerActivity.this, SweetAlertDialog.ERROR_TYPE)
                        .setTitleText("Error...")
                        .setContentText("No data found !!!")
                        .show();
                return;
            }
            if(c1.moveToLast())
            {
                edtNum.setText(c1.getString(0));
                edtLabel.setText(c1.getString(1));
                edtquantity.setText(c1.getString(2));
                edtPurPrice.setText(c1.getString(3));
                edtSalePrice.setText(c1.getString(4));
            }
        }

        if(view==btnSuivant) {
//            c1 = bd.rawQuery("select * from Products",null);
//            if(c1.getCount()==0){
//                //AfficherMessage("Error","No data found !!!");
//                new SweetAlertDialog(ProductsManagerActivity.this, SweetAlertDialog.ERROR_TYPE)
//                        .setTitleText("Error...")
//                        .setContentText("No data found !!!")
//                        .show();
//                return;
//            }
            if(c1.moveToNext()) {
                edtNum.setText(c1.getString(0));
                edtLabel.setText(c1.getString(1));
                edtquantity.setText(c1.getString(2));
                edtPurPrice.setText(c1.getString(3));
                edtSalePrice.setText(c1.getString(4));
            }
        }



        if(view==btnPrecednt) {
//            c1 = bd.rawQuery("select * from Products",null);
//            if(c1.getCount()==0){
//                //AfficherMessage("Error","No data found !!!");
//                new SweetAlertDialog(ProductsManagerActivity.this, SweetAlertDialog.ERROR_TYPE)
//                        .setTitleText("Error...")
//                        .setContentText("No data found !!!")
//                        .show();
//                return;
//            }
            if(c1.moveToPrevious()) {
                edtNum.setText(c1.getString(0));
                edtLabel.setText(c1.getString(1));
                edtquantity.setText(c1.getString(2));
                edtPurPrice.setText(c1.getString(3));
                edtSalePrice.setText(c1.getString(4));
            }
        }


        if(view==btnVider) {
            videTexe();
        }

        if(view==btnLogout)
        {
            final Intent myIntent = new Intent(this, LoginActivity.class);

            new SweetAlertDialog(ProductsManagerActivity.this, SweetAlertDialog.WARNING_TYPE)
                    .setTitleText("Are you sure ?")
                    .setContentText("Do you want to disconnect ?")
                    .setConfirmText("Logout")
                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sDialog) {
                            sDialog.dismissWithAnimation();
                            startActivity(myIntent);

                        }
                    })
                    .setCancelButton("Cancel", new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sDialog) {
                            sDialog.dismissWithAnimation();
                        }
                    })
                    .show();


        }



    }



    public void AfficherMessage(String titre,String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(titre);
        builder.setMessage(message);
        builder.show();
    }

    public void videTexe(){
        edtNum.setText("");
        edtLabel.setText("");
        edtquantity.setText("");
        edtPurPrice.setText("");
        edtSalePrice.setText("");
    }


}
