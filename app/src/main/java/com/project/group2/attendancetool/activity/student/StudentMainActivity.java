package com.project.group2.attendancetool.activity.student;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.project.group2.attendancetool.R;
import com.project.group2.attendancetool.interfaces.IVolleyCallback;
import com.project.group2.attendancetool.model.Course;
import com.project.group2.attendancetool.model.Schedule;
import com.project.group2.attendancetool.model.Term;
import com.project.group2.attendancetool.response.CourseResponse;
import com.project.group2.attendancetool.response.ScheduleResponse;
import com.project.group2.attendancetool.response.TermResponse;
import com.project.group2.attendancetool.util.AttendanceManagement;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Main activity for role Student, perform logics in displaying schedule
 */
public class StudentMainActivity extends AppCompatActivity {

    @BindView(R.id.spinnerTerm)
    Spinner spinnerTerm;
    @BindView(R.id.spinnerCourse)
    Spinner spinnerCourse;
    @BindView(R.id.tableLayoutStudentSchedule)
    TableLayout tableLayoutStudentSchedule;

    private List<String> termIdList;
    private List<String> courseIdList;

    private String selectedTermId;

    private AttendanceManagement attendanceManagement;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_main);
        attendanceManagement = new AttendanceManagement(this);

        // Initialize controls and load all terms corresponding to this user whenever view is created
        setupUI();
        attendanceManagement.getTerms(new IVolleyCallback() {
            @Override
            public void onSuccess(String result) {
                Gson gson = new Gson();
                TermResponse termsResponse = gson.fromJson(result, TermResponse.class);
                loadTermToSpinner(termsResponse.getTerms());
            }
        });
    }

    /**
     * Set up ui controls for this activity
     */
    private void setupUI() {
        ButterKnife.bind(this);
        spinnerTerm.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectedTermId = termIdList.get(i);
                attendanceManagement.getCourses(new IVolleyCallback() {
                               @Override
                               public void onSuccess(String result) {
                                   Gson gson = new Gson();
                                   CourseResponse courseResponse = gson.fromJson(result, CourseResponse.class);
                                   loadCourseToSpinner(courseResponse.getCourses());
                               }
                           }, selectedTermId
                );
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
        spinnerCourse.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String selectedCourseId = courseIdList.get(i);
                attendanceManagement.getSchedules(new IVolleyCallback() {
                    @Override
                    public void onSuccess(String result) {
                        Gson gson = new Gson();
                        ScheduleResponse studentScheduleResponse = gson.fromJson(result, ScheduleResponse.class);
                        loadStudentScheduleTable(studentScheduleResponse.getSchedules());
                    }
                }, selectedCourseId, selectedTermId);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    /**
     * Fill the spinner with terms, with each item having term name
     * as display label and term id as value of that item
     *
     * @param termList - a list of terms acquire after a successful api call
     */
    private void loadTermToSpinner(List<Term> termList) {
        termIdList = new ArrayList<>();
        List<String> termNameList = new ArrayList<>();
        for (Term term : termList) {
            termIdList.add(term.getTermId());
            termNameList.add(term.getTermName());
        }
        ArrayAdapter<String> termSpinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, termNameList);
        spinnerTerm.setAdapter(termSpinnerAdapter);
    }

    /**
     * Fill the spinner with courses, with each item having
     * course name as display label and course id as value of that item
     *
     * @param courseList - a list of courses acquire after a successful api call
     */
    private void loadCourseToSpinner(List<Course> courseList) {
        courseIdList = new ArrayList<>();
        List<String> courseNameList = new ArrayList<>();
        for (Course course : courseList) {
            courseIdList.add(course.getCourseId());
            courseNameList.add(course.getCourseName());
        }
        ArrayAdapter<String> courseSpinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, courseNameList);
        spinnerCourse.setAdapter(courseSpinnerAdapter);
    }

    /**
     * Load schedule list to a schedule table with each item displayed as a single row
     *
     * @param scheduleList - a list of schedule records obtained after web service call
     */
    private void loadStudentScheduleTable(List<Schedule> scheduleList) {
        for (Schedule schedule : scheduleList) {
            final TableRow scheduleRow = new TableRow(this);
            scheduleRow.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
            scheduleRow.setTag(schedule.getSchduleId());

            TableRow.LayoutParams eachCellLayout = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT, 1.0f);

            TextView dayCell = new TextView(this);
            dayCell.setLayoutParams(eachCellLayout);
            dayCell.setText(schedule.getDay());

            TextView slotCell = new TextView(this);
            slotCell.setLayoutParams(eachCellLayout);
            slotCell.setText(String.valueOf(schedule.getSlotNo()));

            TextView lecturerCell = new TextView(this);
            lecturerCell.setLayoutParams(eachCellLayout);
            lecturerCell.setText(schedule.getTeacherId());

            TextView classCell = new TextView(this);
            classCell.setLayoutParams(eachCellLayout);
            classCell.setText(schedule.getClassId());

            TextView statusCell = new TextView(this);
            statusCell.setLayoutParams(eachCellLayout);
            statusCell.setText(schedule.getAttendanceStatus());

            scheduleRow.addView(dayCell, 0);
            scheduleRow.addView(slotCell, 1);
            scheduleRow.addView(lecturerCell, 2);
            scheduleRow.addView(classCell, 3);
            scheduleRow.addView(statusCell, 4);

            final TextView reportCell = new TextView(this);
            reportCell.setText(schedule.getReportStatus());

            // Check the attendance status and report status to create a clickable text view
            // that student can use to report his/her attendance if the attendance status is Absent
            if (schedule.getAttendanceStatus().equalsIgnoreCase("Absent")
                    && schedule.getReportStatus().equalsIgnoreCase("Not Reported")) {
                reportCell.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        attendanceManagement.reportAttendance(new IVolleyCallback() {
                            @Override
                            public void onSuccess(String result) {
                                Toast.makeText(getApplicationContext(), "Your report has been sent successfully", Toast.LENGTH_SHORT).show();
                                reportCell.setText("Reported");
                            }
                        }, Long.parseLong(scheduleRow.getTag().toString()));
                    }
                });
            }
            scheduleRow.addView(reportCell);

            // Add the row to the current table schedule
            tableLayoutStudentSchedule.addView(scheduleRow);
        }
    }
}