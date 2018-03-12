package com.project.group2.attendancetool.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.project.group2.attendancetool.R;
import com.project.group2.attendancetool.activity.teacher.SlotDetailActivity;
import com.project.group2.attendancetool.interfaces.IVolleyCallback;
import com.project.group2.attendancetool.model.Notification;
import com.project.group2.attendancetool.request.AttendanceManagement;

import java.util.ArrayList;

/**
 * Custom List Adapter for Notification Fragment
 */
public class NotificationListAdapter extends ArrayAdapter<Notification> {

    private Context context;
    private ArrayList<Notification> data;
    private static LayoutInflater inflater = null;


    public NotificationListAdapter(ArrayList<Notification> data, Context context) {
        super(context, R.layout.list_item_notification, data);
        this.data = data;
        this.context = context;
        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Notification notification = data.get(position);
        View eachItemListView = convertView;

        if (eachItemListView == null) {
            eachItemListView = inflater.inflate(R.layout.list_item_notification, parent, false);
        }
        if (notification != null) {
            eachItemListView.setTag(notification.getScheduleId());

            TextView tvReportedStudentId = eachItemListView.findViewById(R.id.tvReportedStudentId);
            tvReportedStudentId.setText(notification.getStudentId());

            TextView tvReportedCourseName = eachItemListView.findViewById(R.id.tvReportedCourseName);
            tvReportedCourseName.setText(notification.getCourseName());

            TextView tvReportedClassId = eachItemListView.findViewById(R.id.tvReportedClassId);
            tvReportedClassId.setText(notification.getClassId());

            TextView tvReportedDate = eachItemListView.findViewById(R.id.tvReportedDate);
            tvReportedDate.setText(notification.getDate());

            TextView tvReportedSlotNo = eachItemListView.findViewById(R.id.tvReportedSlotNo);
            tvReportedSlotNo.setText("Slot " + notification.getSlotId());
        }

        // Set event listener to edit attendance and decline attendance
        TextView tvEditAttendance = eachItemListView.findViewById(R.id.tvEditAttendance);
        tvEditAttendance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent slotDetailIntent = new Intent(context, SlotDetailActivity.class);
                context.startActivity(slotDetailIntent);
            }
        });

        TextView tvDeclineAttendance = eachItemListView.findViewById(R.id.tvDeclineAttendance);
        final View tempEachItemListView = eachItemListView;
        tvDeclineAttendance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                long scheduleId = (long) tempEachItemListView.getTag();
                AttendanceManagement attendanceManagement = new AttendanceManagement(context);
                attendanceManagement.declineAttendance(new IVolleyCallback() {
                    @Override
                    public void onSuccess(String result) {

                    }
                }, scheduleId);
            }
        });

        return  eachItemListView;
    }

}
