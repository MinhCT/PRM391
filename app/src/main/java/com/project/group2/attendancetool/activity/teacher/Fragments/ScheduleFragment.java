package com.project.group2.attendancetool.activity.teacher.Fragments;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.project.group2.attendancetool.request.AttendanceManagement;
import com.project.group2.attendancetool.response.CourseResponse;
import com.project.group2.attendancetool.response.ScheduleResponse;
import com.project.group2.attendancetool.response.TermResponse;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class ScheduleFragment extends Fragment {
    private AttendanceManagement attendanceManagement;
    private Spinner spTerm, spCourse;
    private List<String> termIdList;
    private List<String> courseIdList;
    private String selectedTermId;
    private TableLayout tbLayoutTeacherSchedule;

    public ScheduleFragment() {
        // Required empty public constructor
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        attendanceManagement = new AttendanceManagement(getContext());
        spTerm = getView().findViewById(R.id.spTerm);
        spCourse = getView().findViewById(R.id.spCourse);
        tbLayoutTeacherSchedule = getView().findViewById(R.id.tbLayoutTeacherSchedule);
        attendanceManagement.getTerms(new IVolleyCallback() {
            @Override
            public void onSuccess(String result) {
                Gson gson = new Gson();
                TermResponse termsResponse = gson.fromJson(result, TermResponse.class);
                loadTermToSpinner(termsResponse.getTerms());
            }
        });

        spTerm.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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
        spCourse.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String selectedCourseId = courseIdList.get(i);
                attendanceManagement.getSchedules(new IVolleyCallback() {
                    @Override
                    public void onSuccess(String result) {
                        Gson gson = new Gson();
                        ScheduleResponse teacherScheduleResponse = gson.fromJson(result, ScheduleResponse.class);
                        loadStudentScheduleTable(teacherScheduleResponse.getSchedules());
                    }
                }, selectedCourseId, selectedTermId);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void loadStudentScheduleTable(List<Schedule> scheduleList) {
        tbLayoutTeacherSchedule.removeAllViews(); // Clear old data rows
        for (Schedule schedule : scheduleList) {
            final TableRow scheduleRow = new TableRow(getContext());
            scheduleRow.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
            scheduleRow.setTag(schedule.getScheduleId());

            TableRow.LayoutParams eachCellLayout = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,  TableRow.LayoutParams.WRAP_CONTENT, 1.0f);
            eachCellLayout.setMargins(10,0,0,10);

            TextView dayCell = new TextView(getContext());
            dayCell.setLayoutParams(eachCellLayout);
            dayCell.setText(schedule.getDate());
//            dayCell.setBackground(getResources().getDrawable(R.drawable.cell_shape_2));

            TextView slotCell = new TextView(getContext());
            slotCell.setLayoutParams(eachCellLayout);
            slotCell.setText(String.valueOf(schedule.getSlotNo()));
//            slotCell.setBackground(getResources().getDrawable(R.drawable.cell_shape_2));

            TextView lecturerCell = new TextView(getContext());
            lecturerCell.setLayoutParams(eachCellLayout);
            lecturerCell.setText(schedule.getTeacherId());
//            lecturerCell.setBackground(getResources().getDrawable(R.drawable.cell_shape_2));

            TextView classCell = new TextView(getContext());
            classCell.setLayoutParams(eachCellLayout);
            classCell.setText(schedule.getClassId());
//            classCell.setBackground(getResources().getDrawable(R.drawable.cell_shape_2));

            TextView statusCell = new TextView(getContext());
            statusCell.setLayoutParams(eachCellLayout);
            statusCell.setText("");
//            statusCell.setBackground(getResources().getDrawable(R.drawable.cell_shape_2));

            scheduleRow.addView(dayCell, 0);
            scheduleRow.addView(slotCell, 1);
            scheduleRow.addView(lecturerCell, 2);
            scheduleRow.addView(classCell, 3);
            scheduleRow.addView(statusCell, 4);

            final TextView reportCell = new TextView(getContext());
            reportCell.setText(schedule.getReportStatus());

            scheduleRow.addView(reportCell);

            // Add the row to the current table schedule
            tbLayoutTeacherSchedule.addView(scheduleRow);
        }
    }

    private void loadTermToSpinner(List<Term> termList) {
        termIdList = new ArrayList<>();
        List<String> termNameList = new ArrayList<>();
        for (Term term : termList) {
            termIdList.add(term.getTermId());
            termNameList.add(term.getTermName());
        }
        ArrayAdapter<String> termSpinnerAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, termNameList);
        spTerm.setAdapter(termSpinnerAdapter);
    }

    private void loadCourseToSpinner(List<Course> courseList) {
        courseIdList = new ArrayList<>();
        List<String> courseNameList = new ArrayList<>();
        for (Course course : courseList) {
            courseIdList.add(course.getCourseId());
            courseNameList.add(course.getCourseName());
        }
        ArrayAdapter<String> courseSpinnerAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, courseNameList);
        spCourse.setAdapter(courseSpinnerAdapter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_schedule, container, false);
    }

}
