package com.example.smartnoticeboard;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class StudentActivity extends AppCompatActivity {
    LinearLayout l1 ,l2 , l3;
    Spinner departmentSpinner , semesterSpinner , subjectSpinner;
    String department , semester , subject;
    int count = 0;
    int c= 0;
    ValueEventListener listener;
    ArrayAdapter<String> adapter;
    ArrayList<String> QuizSubject;
    DatabaseReference dref= FirebaseDatabase.getInstance().getReference();
    Button viewNoti;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student);
        l1 = (LinearLayout) findViewById(R.id.l1);
        l2 = (LinearLayout) findViewById(R.id.l2);
        l3 = (LinearLayout) findViewById(R.id.l3);
        viewNoti = (Button) findViewById(R.id.btnView);
        subjectSpinner = findViewById(R.id.spinnerSubject);
        l2.setVisibility(View.GONE);
        l3.setVisibility(View.GONE);

        QuizSubject = new ArrayList<>();
        adapter = new ArrayAdapter<>(this,android.R.layout.simple_spinner_dropdown_item,QuizSubject);
        subjectSpinner.setAdapter(adapter );

        departmentSpinner = findViewById(R.id.spinnerDeparments);
        departmentSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                department = parent.getItemAtPosition(position).toString();
                count++;
                if(count>1){
                    l2.setVisibility(View.VISIBLE);
                    l1.setVisibility(View.GONE);
                    semesterSpinner = findViewById(R.id.spinnerSemester);
                    semesterSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            semester = parent.getItemAtPosition(position).toString();
                            c++;
                            if(c>1){
                                l2.setVisibility(View.GONE);
                                l3.setVisibility(View.VISIBLE);
                                viewNoti.setVisibility(View.VISIBLE);
                                retrivedata();

                                viewNoti.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        subject = subjectSpinner.getSelectedItem().toString();

                                        Intent i = new Intent(StudentActivity.this , NoticeDetail.class);
                                        i.putExtra("Sub" , subject);
                                        i.putExtra("Dept" , department);
                                        i.putExtra("Sem" , semester);
                                        startActivity(i);
                                        //Toast.makeText(getApplicationContext() , subject +" " +department + " " + semester , Toast.LENGTH_SHORT).show();
                                    }
                                });

                            }
                        }

                        private void retrivedata() {
                            listener =   dref.child( department).child(semester).addValueEventListener( new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    for (DataSnapshot subject : dataSnapshot.getChildren()) {

                                        QuizSubject.add(subject.getKey());
                                    }
                                    adapter.notifyDataSetChanged();

                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            } );
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
}
