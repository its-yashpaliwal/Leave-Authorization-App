package com.example.leave;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.SparseArray;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class NewApplication extends AppCompatActivity {
    Uri temp;
    private static final int CROP_PIC = 100;
    private static final int REQUEST_CAMERA_CODE = 99;
    final Calendar myCalendar1 = Calendar.getInstance();
    final Calendar myCalendar2 = Calendar.getInstance();
    private int code = 666;
    private static final int pic_id = 123;
    Bitmap bitmap;
    Bitmap photo;
    EditText editText1, editText2;
    FirebaseStorage storage;
    StorageReference storageReference;
    Integer cnt;
    String rs;
    Spinner type;
    int score = 0;
    DatePickerDialog dt1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_application);
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        Button capture = findViewById(R.id.capture);
        capture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, pic_id);
            }
        });

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.CAMERA
            }, REQUEST_CAMERA_CODE);
        }
        editText1 = (EditText) findViewById(R.id.startDate);
        editText2 = (EditText) findViewById(R.id.endDate);
        DatePickerDialog.OnDateSetListener date1 = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                myCalendar1.set(Calendar.YEAR, year);
                myCalendar1.set(Calendar.MONTH, month);
                myCalendar1.set(Calendar.DAY_OF_MONTH, day);

                updateLabel1();
            }
        };
        DatePickerDialog.OnDateSetListener date2 = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                myCalendar2.set(Calendar.YEAR, year);
                myCalendar2.set(Calendar.MONTH, month);
                myCalendar2.set(Calendar.DAY_OF_MONTH, day);

                updateLabel2();
            }
        };

        editText1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dt1 = new DatePickerDialog(NewApplication.this, date1, myCalendar1.get(Calendar.YEAR), myCalendar1.get(Calendar.MONTH), myCalendar1.get(Calendar.DAY_OF_MONTH));
                dt1.getDatePicker().setMinDate(new Date().getTime());
                dt1.show();
            }
        });
        editText2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog dt2 = new DatePickerDialog(NewApplication.this, date2, myCalendar2.get(Calendar.YEAR), myCalendar2.get(Calendar.MONTH), myCalendar2.get(Calendar.DAY_OF_MONTH));
                dt2.getDatePicker().setMinDate(myCalendar1.getTimeInMillis());
                dt2.show();
            }
        });

        Button apply = findViewById(R.id.applyButton);
        apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText start, end, reason;

                start = findViewById(R.id.startDate);
                end = findViewById(R.id.endDate);
                reason = findViewById(R.id.reason);
                type = findViewById(R.id.docSpinner);
                FirebaseAuth mAuth = FirebaseAuth.getInstance();
                String UID = mAuth.getUid();
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                application app = new application(start.getText().toString(), end.getText().toString(), reason.getText().toString(),
                        type.getSelectedItem().toString(), "Pending", true, score);
                Map<String, Object> mp = new HashMap<>();
                database.getReference().child("Users").child(UID).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        cnt = snapshot.child("num_app").getValue(Integer.class);
                        mp.put("num_app", ++cnt);
                        database.getReference().child("Users").child(UID).updateChildren(mp);
                        database.getReference().child("Applications").child(UID).child(cnt.toString()).setValue(app);
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        photo.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                        byte[] up = baos.toByteArray();
                        UploadTask uploadTask = storageReference.child(UID).child(String.valueOf(cnt)).putBytes(up);
                        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                Toast.makeText(NewApplication.this, "Application Submitted to Teacher!", Toast.LENGTH_SHORT).show();
                            }
                        });
                        uploadTask.addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(NewApplication.this, "Failed to upload", Toast.LENGTH_SHORT).show();
                            }
                        });
                        Intent intent = new Intent(getApplicationContext(), studentLanding.class);
                        startActivity(intent);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }
        });
        Button cancel = findViewById(R.id.cancelButton);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), studentLanding.class);
                startActivity(intent);
            }
        });


    }

    private void updateLabel1() {
        String myFormat = "dd/MM/yy";
        SimpleDateFormat dateFormat = new SimpleDateFormat(myFormat, Locale.US);
        editText1.setText(dateFormat.format(myCalendar1.getTime()));


    }

    private void updateLabel2() {
        String myFormat = "dd/MM/yy";
        SimpleDateFormat dateFormat = new SimpleDateFormat(myFormat, Locale.US);
        editText2.setText(dateFormat.format(myCalendar2.getTime()));
    }


    private void getTextFromImage(Bitmap bitmap) {
        TextRecognizer recognizer = new TextRecognizer.Builder(this).build();
        if (!recognizer.isOperational()) {
            Toast.makeText(this, "Error Occured", Toast.LENGTH_SHORT).show();
        } else {
            Frame frame = new Frame.Builder().setBitmap(bitmap).build();
            SparseArray<TextBlock> textBlockSparseArray = recognizer.detect(frame);
            StringBuilder stringBuilder = new StringBuilder();
            for (int i = 0; i < textBlockSparseArray.size(); i++) {
                TextBlock textBlock = textBlockSparseArray.valueAt(i);
                stringBuilder.append(textBlock.getValue());
                stringBuilder.append("\n");
            }
//            Toast.makeText(this, stringBuilder.toString(), Toast.LENGTH_SHORT).show();
            rs = stringBuilder.toString();
            Toast.makeText(this, rs, Toast.LENGTH_SHORT).show();
            score(rs);

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == pic_id) {
            photo = (Bitmap) data.getExtras().get("data");
            getTextFromImage(photo);
        }
    }

    public void score(String rs) {
        type = findViewById(R.id.docSpinner);
        if (type.getSelectedItem().toString().equals("Thermometer")) {
            therm(rs);
        }
    }

    private void therm(String rs) {
        try {
            Float fl = Float.valueOf(rs);
            Toast.makeText(this, fl.toString(), Toast.LENGTH_SHORT).show();
            if (fl < 99) score = 0;
            else if (99 <= fl && 100 >= fl) score = 1;
            else if (fl > 100) score = 2;

        }
        catch (Exception e){
            Toast.makeText(this, "Not recognized!", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(intent, pic_id);
        }
    }
}