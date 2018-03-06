package com.project.group2.attendancetool.activity.teacher;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.project.group2.attendancetool.R;
import com.project.group2.attendancetool.interfaces.IVolleyJsonCallback;
import com.project.group2.attendancetool.model.Classes;
import com.project.group2.attendancetool.model.Course;
import com.project.group2.attendancetool.model.Slot;
import com.project.group2.attendancetool.request.SlotManagement;
import com.project.group2.attendancetool.response.GetSlotDetailResponse;

import org.json.JSONObject;

public class SlotDetailActivity extends Activity {
    TextView tvAttendanceStatistic, tvSlot, tvClass, tvCourse;
    ListView lvStudents;
    SlotManagement slotManagement;

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

        Intent intent = this.getIntent();
        String stringDate = intent.getStringExtra("StringDate");
        Slot slot = (Slot)intent.getSerializableExtra("Slot");
        Course course = (Course)intent.getSerializableExtra("Course");
        Classes classes = (Classes)intent.getSerializableExtra("Class");

        //Slot 1 (7h30 - 9h00)
        tvSlot.setText("Slot: "+ slot.getSlotId()+ "  (" + slot.getStartTime()+ " - "+slot.getEndTime()+")");
        //Class: IS1101
        tvClass.setText("Class: "+classes.getClassName());
        //Subject: PRM391 - Mobile Programming
        tvCourse.setText("Subject: "+course.getCourseName());

        slotManagement.getSlotDetail(new IVolleyJsonCallback() {
            @Override
            public void onJsonRequestSucess(JSONObject result) {
                Gson gson = new Gson();
                String resultInString = result.toString();
                GetSlotDetailResponse response = gson.fromJson(resultInString,GetSlotDetailResponse.class);
                lvStudents.setAdapter(new CustomStudentsAdapter(getApplicationContext(), response.getStudents()));
            }
        },stringDate, slot.getSlotId(), classes.getClassId());

        Toast.makeText(getApplicationContext(), stringDate + " " + slot.getSlotId() + " " + classes.getClassId(), Toast.LENGTH_SHORT).show();

        tvAttendanceStatistic.setText(Html.fromHtml(getResources().getString(R.string.attendance_statistic_example)));
    }
}
