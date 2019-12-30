package com.example.smartnoticeboard;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Signup extends AppCompatActivity {
    TextView btnLogin;
    Button btnSignup;
    EditText email,password;
    boolean check;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        btnLogin=(TextView) findViewById(R.id.login);
        btnSignup=(Button) findViewById(R.id.sigup);
        email=(EditText) findViewById(R.id.email);
        password=(EditText) findViewById(R.id.password);
        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Email = email.getText().toString().trim();
                String Password = password.getText().toString().trim();

                if (!Patterns.EMAIL_ADDRESS.matcher(Email).matches()) {

                    email.setError("Invalid Email");
                    email.setFocusable(true);

                } else if (Password.length() < 6) {
                    password.setError("Password Length Must Be greater than 6 characters");
                    password.setFocusable(true);

                } else {
                    //progressDialog.setMessage("Registering ....");

                    Intent intent=new Intent(Signup.this,Register.class);
                    intent.putExtra("Email",Email);
                    intent.putExtra("Password",Password);
                    startActivity(intent);
                    finish();
                }
            }
        });
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Signup.this,Login.class));
            }
        });
    }
}
