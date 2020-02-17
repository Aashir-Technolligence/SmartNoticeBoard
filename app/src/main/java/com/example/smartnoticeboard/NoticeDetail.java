package com.example.smartnoticeboard;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.github.barteksc.pdfviewer.PDFView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class NoticeDetail extends AppCompatActivity {
    DatabaseReference dref = FirebaseDatabase.getInstance().getReference();
    TextView name;
    ImageView img;
    PDFView pdfview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice_detail);
        Intent i = getIntent();
        final String sem = i.getStringExtra("Sem");
        final String dep = i.getStringExtra("Dept");
        final String sub = i.getStringExtra("Sub");
        Toast.makeText(getApplicationContext(), sem + " " + dep + " " + sub, Toast.LENGTH_SHORT).show();
        name = findViewById(R.id.txtTeacherName);
        img = findViewById(R.id.imgNotice);
        pdfview = findViewById(R.id.pdfView);

        dref.child("Notice").child(dep).child(sem).child(sub).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                        if (dataSnapshot1.exists()) {
                            name.setText(dataSnapshot1.child("name").getValue().toString());
                            Picasso.get().load(dataSnapshot1.child("imgUrl").getValue().toString()).into(img);
                            Uri pdf = Uri.parse(dataSnapshot1.child("fileUrl").getValue().toString());
                            pdfview.fromUri(pdf).load();

                        } else {
                            Toast.makeText(getApplicationContext(), "No notice found.", Toast.LENGTH_LONG).show();
                        }
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "No notice found.", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
