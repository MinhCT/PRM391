package com.project.group2.attendancetool.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.project.group2.attendancetool.R;
import com.project.group2.attendancetool.helper.Base64Coverter;
import com.project.group2.attendancetool.model.StudentAttendance;

import java.util.ArrayList;

/**
 * Adapter for list view used in Take Attendance Activity
 */
public class StudentListWithCheckboxAdapter extends ArrayAdapter<StudentAttendance> {

    private Context context;
    private ArrayList<StudentAttendance> data;
    private static LayoutInflater inflater = null;

    public StudentListWithCheckboxAdapter(ArrayList<StudentAttendance> data, Context context) {
        super(context, R.layout.list_item_student_in_take_attendance, data);
        this.data = data;
        this.context = context;
        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        StudentAttendance studentAttendance = data.get(position);
        View eachItemListView = convertView;

        if (eachItemListView == null) {
            eachItemListView = inflater.inflate(R.layout.list_item_student_in_take_attendance, parent, false);
        }

        if (studentAttendance != null) {
            ImageView ivStudentImage = eachItemListView.findViewById(R.id.ivStudentImage);
            TextView tvStudentName = eachItemListView.findViewById(R.id.tvStudentName);
            CheckBox cbAttendanceStatus = eachItemListView.findViewById(R.id.cbAttendanceStatus);

            ivStudentImage.setImageBitmap(Base64Coverter.toImageBitmap(studentAttendance.getImage()));
            tvStudentName.setText(studentAttendance.getFullName());
            if (studentAttendance.getAttendanceStatus().equalsIgnoreCase("Presented")) {
                cbAttendanceStatus.setChecked(true);
            } else {
                cbAttendanceStatus.setChecked(false);
            }
        }

        return eachItemListView;
    }
}
