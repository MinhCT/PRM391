package com.project.group2.attendancetool.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.project.group2.attendancetool.R;
import com.project.group2.attendancetool.helper.Base64Coverter;
import com.project.group2.attendancetool.model.StudentAttendance;
import com.project.group2.attendancetool.utils.Constants;

import java.util.ArrayList;

/**
 * Adapter for list view used in Take Attendance Activity
 */
public class StudentListWithCheckboxAdapter extends ArrayAdapter<StudentAttendance> {

    private Context context;
    private ArrayList<StudentAttendance> data;
    private static LayoutInflater inflater = null;

    public ArrayList<StudentAttendance> getData() {
        return data;
    }

    public StudentListWithCheckboxAdapter(ArrayList<StudentAttendance> data, Context context) {
        super(context, R.layout.list_item_student_in_take_attendance, data);
        this.data = data;
        this.context = context;
        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    static class ViewHolder{
        ImageView ivStudentImage;
        TextView tvStudentName;
        CheckBox cbAttendanceStatus;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        final StudentAttendance studentAttendance = data.get(position);
        View eachItemListView = convertView;
        ViewHolder holder;
        if (eachItemListView == null) {
            eachItemListView = inflater.inflate(R.layout.list_item_student_in_take_attendance, parent, false);
            holder = new ViewHolder();
            holder.ivStudentImage = eachItemListView.findViewById(R.id.ivStudentImage);
            holder.tvStudentName = eachItemListView.findViewById(R.id.tvStudentName);
            holder.cbAttendanceStatus = eachItemListView.findViewById(R.id.cbAttendanceStatus);
            eachItemListView.setTag(holder);
        } else {
            holder = (ViewHolder) eachItemListView.getTag();
            holder.cbAttendanceStatus.setOnCheckedChangeListener(null);
        }
        holder.ivStudentImage.setImageBitmap(Base64Coverter.toImageBitmap(studentAttendance.getImage()));
        holder.tvStudentName.setText(studentAttendance.getFullName());
        holder.cbAttendanceStatus.setChecked(Constants.AttendanceStatus.PRESENTED.equalsIgnoreCase(studentAttendance.getAttendanceStatus()) ? true : false);
//        if (studentAttendance.getAttendanceStatus().equalsIgnoreCase(Constants.AttendanceStatus.PRESENTED)) {
//            holder.cbAttendanceStatus.setChecked(true);
//        } else {
//            holder.cbAttendanceStatus.setChecked(false);
//        }

        holder.cbAttendanceStatus.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                studentAttendance.setAttendanceStatus(b ? Constants.AttendanceStatus.PRESENTED : Constants.AttendanceStatus.ABSENT);
            }
        });


        return eachItemListView;
    }
}
