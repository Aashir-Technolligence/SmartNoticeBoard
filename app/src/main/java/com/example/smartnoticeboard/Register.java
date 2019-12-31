package com.example.smartnoticeboard;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Register extends AppCompatActivity {
    Button btnRegister;
    ProgressDialog progressDialog;

    EditText name,contact,age,department;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        final String currentUser= FirebaseAuth.getInstance().getCurrentUser().getUid();
        name=(EditText) findViewById(R.id.name);
        contact=(EditText) findViewById(R.id.contact);
        age=(EditText) findViewById(R.id.age);
        btnRegister=(Button) findViewById(R.id.register);
        department=(EditText) findViewById(R.id.department);
        progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("Registering..... ");
        final FirbaseAuthenticationClass firbaseAuthenticationClass=new FirbaseAuthenticationClass();
        final String Email=getIntent().getStringExtra("Email");
        final String Password=getIntent().getStringExtra("Password");

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Name = name.getText().toString();
                String Age = age.getText().toString();
                String Contact = contact.getText().toString();
                String Department = department.getText().toString();
                if(Name.equals("")){
                    name.setError("Enter valid name");
                    name.setFocusable(true);
                }
                else if(Age.equals("")){
                    age.setError("Enter valid age");
                    age.setFocusable(true);
                }
                else if(Contact.equals("")){
                    contact.setError("Enter valid contact number");
                    contact.setFocusable(true);
                }
                else if(Department.equals("")){
                    department.setError("Enter valid department");
                    department.setFocusable(true);
                }
                else{
                    progressDialog.show();
                    firbaseAuthenticationClass.RegisterUser(Email, Password,Contact,Name,Age,Department,Register.this,progressDialog);


                }
            }
        });



    }

    @Override
    public void onBackPressed() {
        Intent intent=new Intent(Register.this,Signup.class);
        startActivity(intent);
    }
}
