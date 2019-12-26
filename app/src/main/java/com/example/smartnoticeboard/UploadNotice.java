package com.example.smartnoticeboard;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;

public class UploadNotice extends AppCompatActivity {
    Button btnSelect, btnUpload;
    EditText title;
    ImageView img;
    TextView filename;
    Uri pdfUri;//URI ARE ACTUALLY URLS THAT ARE MEANT FOR LOCAL STORAGE
    FirebaseStorage storage;//used for uploading files
    FirebaseDatabase database;//used to store the url of uploaded files.
    ProgressDialog progressDialog;
    int count = 0;
    int count1 = 0;
    private Uri filePath;
    DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
    final StorageReference storageReference = FirebaseStorage.getInstance().getReference();//returns root path

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_notice);

        btnSelect = findViewById(R.id.btnselect);
        btnUpload = findViewById(R.id.btnupload);
        filename = findViewById(R.id.textViewnotification);
        title = findViewById(R.id.txtTitle);
        img = findViewById(R.id.imgSelect);
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, 2);
            }

        });

        progressDialog = new ProgressDialog(this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setTitle("uploading file....");
        progressDialog.setProgress(0);

        btnSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(UploadNotice.this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    selectpdf();

                } else {
                    ActivityCompat.requestPermissions(UploadNotice.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 9);
                }
            }
        });
        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!title.getText().toString().isEmpty()) {
                    if (count1 >= 1) {//user has selected the file

                        if (count >= 1) {
                            progressDialog.show();
                            final String id = "1"; //FirebaseAuth.getInstance().getCurrentUser().getUid();
                            final String fileName = System.currentTimeMillis() + ".pdf";
                            storageReference.child("UploadsFile/" + fileName).putFile(pdfUri)
                                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                        @Override
                                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                                String url = taskSnapshot.getUploadSessionUri().toString();//return the url of uploaded file
                                            reference.child("Notice").child(id).child("fileUrl").setValue(url);
                                            final String push = FirebaseDatabase.getInstance().getReference().child("Services").push().getKey();
                                            StorageReference fileReference = storageReference.child("images/" + push);
                                            fileReference.putFile(filePath)
                                                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                                        @Override
                                                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                                            //Toast.makeText(getApplicationContext(), "Uploaded", Toast.LENGTH_SHORT).show();
                                                            if (filePath != null) {
                                                                Task<Uri> urlTask = taskSnapshot.getStorage().getDownloadUrl();
                                                                while (!urlTask.isSuccessful()) ;
                                                                Uri downloadUrl = urlTask.getResult();

                                                                reference.child("Notice").child(id).child("id").setValue(id);
                                                                reference.child("Notice").child(id).child("title").setValue(title.getText().toString());
                                                                reference.child("Notice").child(id).child("imgUrl").setValue(downloadUrl);


                                                                progressDialog.dismiss();
                                                                Toast.makeText(getApplicationContext(), "Your notice is saved in db.", Toast.LENGTH_LONG).show();

                                                            }
                                                        }
                                                    })
                                                    .addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {
                                                            Toast.makeText(getApplicationContext(), "Failed " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                                        }
                                                    });


                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(UploadNotice.this, "file not successfully uploaded...", Toast.LENGTH_LONG).show();

                                }
                            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                                    //track the progress of our upload
//                                    int currentprogress = (int) (100 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
//                                    progressDialog.setProgress(currentprogress);


                                }
                            });

                        } else
                            Toast.makeText(UploadNotice.this, "select an iamge", Toast.LENGTH_LONG).show();
                    } else
                        Toast.makeText(UploadNotice.this, "select a file", Toast.LENGTH_LONG).show();

                }
            }
        });

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {

        if (requestCode == 9 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            selectpdf();
        } else {
            Toast.makeText(UploadNotice.this, "please provide permission", Toast.LENGTH_LONG).show();
        }
    }

    private void selectpdf() {
        String[] mimeTypes =
                {"application/msword", "application/vnd.openxmlformats-officedocument.wordprocessingml.document", // .doc & .docx
                        "application/vnd.ms-powerpoint", "application/vnd.openxmlformats-officedocument.presentationml.presentation", // .ppt & .pptx
                        "application/vnd.ms-excel", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", // .xls & .xlsx
                        "text/plain",
                        "application/pdf",
                        "application/zip"};

        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            intent.setType(mimeTypes.length == 1 ? mimeTypes[0] : "*/*");
            if (mimeTypes.length > 0) {
                intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
            }
        } else {
            String mimeTypesStr = "";
            for (String mimeType : mimeTypes) {
                mimeTypesStr += mimeType + "|";
            }
            intent.setType(mimeTypesStr.substring(0, mimeTypesStr.length() - 1));
        }
        startActivityForResult(Intent.createChooser(intent, "ChooseFile"), 86);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == requestCode && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            filePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getApplication().getApplicationContext().getContentResolver(), filePath);
                img.setImageBitmap(bitmap);
                count = 1;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }//check weather user has selected a file of not
        if (requestCode == 86 && resultCode == RESULT_OK && data != null) {
            pdfUri = data.getData();//return the uri of selected file
            filename.setText("File is selected: " + data.getData().getLastPathSegment());
            count1 = 1;
        } else {
            Toast.makeText(UploadNotice.this, "please select the file", Toast.LENGTH_LONG).show();
        }

    }
}
