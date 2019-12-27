package com.example.smartnoticeboard;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;

public class Login extends AppCompatActivity {
    Button btnLogin,btnSignup;
    EditText email,password;
    ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        btnLogin=(Button) findViewById(R.id.login);
        btnSignup=(Button) findViewById(R.id.sigup);
        email=(EditText) findViewById(R.id.email);
        password=(EditText) findViewById(R.id.password);
        progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("Logging In..... ");
        final FirbaseAuthenticationClass firbaseAuthenticationClass=new FirbaseAuthenticationClass();
        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Login.this,Signup.class));
            }
        });
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String EMAIL = email.getText().toString().trim();
                String PASSWORD = password.getText().toString().trim();
                if (!Patterns.EMAIL_ADDRESS.matcher(EMAIL).matches()){
                    email.setError("Invalid email");
                    email.setFocusable(true);
                }else {
                    progressDialog.show();
                    firbaseAuthenticationClass.LoginUser(EMAIL,PASSWORD, Login.this, progressDialog);
//                    if(check==true) {
//                        startActivity(new Intent(Login.this, MainActivity.class));
//                        Toast.makeText(getApplicationContext(), "return true", Toast.LENGTH_SHORT).show();
//                    }
//                    else{
//                        Toast.makeText(getApplicationContext(), "return false", Toast.LENGTH_SHORT).show();
//
//                    }

                }
            }
        });
    }
}
