package com.project.group2.attendancetool.activity.teacher;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.project.group2.attendancetool.R;
import com.project.group2.attendancetool.interfaces.IVolleyJsonCallback;
import com.project.group2.attendancetool.model.Classes;
import com.project.group2.attendancetool.model.Course;
import com.project.group2.attendancetool.model.Slot;
import com.project.group2.attendancetool.model.StudentAttendance;
import com.project.group2.attendancetool.request.SlotManagement;
import com.project.group2.attendancetool.response.GetSlotDetailResponse;

import org.json.JSONObject;

import java.util.ArrayList;

public class SlotDetailActivity extends AppCompatActivity implements View.OnClickListener {
    TextView tvAttendanceStatistic, tvSlot, tvClass, tvCourse;
    ListView lvStudents;
    SlotManagement slotManagement;
    Button btnTakeAttendance;
    private int totalStudents, attendedStudents;
    private GetSlotDetailResponse response;
    private String stringDate;
    private Slot slot;
    private Course course;
    private Classes classes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        slotManagement = new SlotManagement(this);
        setContentView(R.layout.activity_slot_detail);
        tvAttendanceStatistic = findViewById(R.id.tvAttendanceStatistic);
        tvSlot = findViewById(R.id.tvDetailSlot);
        tvClass = findViewById(R.id.tvDetailClass);
        tvCourse = findViewById(R.id.tvDetailSubject);
        lvStudents = findViewById(R.id.lvStudents);
        btnTakeAttendance = findViewById(R.id.btnTakeAttendance);

        Intent intent = this.getIntent();
        stringDate = intent.getStringExtra("StringDate");
        slot = (Slot) intent.getSerializableExtra("Slot");
        course = (Course) intent.getSerializableExtra("Course");
        classes = (Classes) intent.getSerializableExtra("Class");

        //Slot 1 (7h30 - 9h00)
        tvSlot.setText("Slot: " + slot.getSlotId() + "  (" + slot.getStartTime() + " - " + slot.getEndTime() + ")");
        //Class: IS1101
        tvClass.setText("Class: " + classes.getClassName());
        //Subject: PRM391 - Mobile Programming
        tvCourse.setText("Subject: " + course.getCourseName());

        slotManagement.getSlotDetail(new IVolleyJsonCallback() {
            @Override
            public void onJsonRequestSucess(JSONObject result) {
                Gson gson = new Gson();
                String resultInString = result.toString();
                response = gson.fromJson(resultInString, GetSlotDetailResponse.class);
                lvStudents.setAdapter(new CustomStudentsAdapter(getApplicationContext(), response.getStudents()));
                attendedStudents = response.getAttendedStudents();
                totalStudents = response.getStudents().size();
                tvAttendanceStatistic.setText("Attended Students: "+ attendedStudents + "/" + totalStudents);
                tvAttendanceStatistic.setTextColor(getResources().getColor(R.color.colorPrimary));
            }
        }, stringDate, slot.getSlotId(), classes.getClassId());

        Toast.makeText(getApplicationContext(), stringDate + " " + slot.getSlotId() + " " + classes.getClassId(), Toast.LENGTH_SHORT).show();
        btnTakeAttendance.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnTakeAttendance:
                Intent takeAttendanceIntent = new Intent(getApplicationContext(), TakeAttendanceActivity.class);
                ArrayList<StudentAttendance> studentAttendances = new ArrayList<>(response.getStudents());
                takeAttendanceIntent.putParcelableArrayListExtra("studentListWithAttendance", studentAttendances);
                takeAttendanceIntent.putExtra("Slot", slot);
                takeAttendanceIntent.putExtra("StringDate", stringDate);
                takeAttendanceIntent.putExtra("Course",course);
                takeAttendanceIntent.putExtra("Classes",classes);
                startActivity(takeAttendanceIntent);
                break;
        }
    }
}
