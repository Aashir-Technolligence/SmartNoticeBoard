package com.example.smartnoticeboard;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddSemester extends AppCompatActivity {
    Spinner departmentSpinner , semesterSpinner ;
    String department , semester , subject;
    EditText subjectE;
    Button add;
    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    DatabaseReference databaseReference = firebaseDatabase.getReference();
    ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_semester);
        pd = new ProgressDialog(this);
        pd.setMessage("Inserting");
        departmentSpinner = findViewById(R.id.spinnerDeparments);
        departmentSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                department = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        semesterSpinner = findViewById(R.id.spinnerSemester);
        semesterSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                semester = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        subjectE = (EditText) findViewById(R.id.edtSubject);
        add = (Button) findViewById(R.id.btnAdd);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!subjectE.getText().toString().isEmpty()) {
                    pd.show();
                    databaseReference.child(department).child(semester).child(subjectE.getText().toString()).child("id").setValue(subjectE.getText().toString());
                    pd.dismiss();
                    Toast.makeText(getApplicationContext(), "Inserted", Toast.LENGTH_SHORT).show();
                    recreate();
                }
                else
                    Toast.makeText(getApplicationContext() , "Subject name must entered" , Toast.LENGTH_SHORT).show();
            }
        });

    }
}
