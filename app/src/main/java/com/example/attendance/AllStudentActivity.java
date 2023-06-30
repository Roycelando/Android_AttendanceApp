package com.example.attendance;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class AllStudentActivity extends AppCompatActivity {

    Context context = AllStudentActivity.this;
    RecyclerView student_view;

    ArrayList<StudenDataModelClass> studenData = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_student);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);

        student_view = findViewById(R.id.student_view);

        // all data of students can be added here
        studenData.add(new StudenDataModelClass("Name 1", "Present"));
        studenData.add(new StudenDataModelClass("Name 2", "Present"));
        studenData.add(new StudenDataModelClass("Name 3", "Present"));
        studenData.add(new StudenDataModelClass("Name 4", "Present"));
        studenData.add(new StudenDataModelClass("Name 5", "Present"));

        student_view.setLayoutManager(new GridLayoutManager(context, 1, GridLayoutManager.VERTICAL, false));
        CustomAdapter customAdapter = new CustomAdapter(studenData, context,2);
        student_view.setNestedScrollingEnabled(false);
        student_view.setAdapter(customAdapter);


    }
}