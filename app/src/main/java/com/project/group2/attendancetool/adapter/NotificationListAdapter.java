package com.project.group2.attendancetool.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.google.gson.Gson;
import com.project.group2.attendancetool.R;
import com.project.group2.attendancetool.activity.teacher.SlotDetailActivity;
import com.project.group2.attendancetool.enums.ELogTag;
import com.project.group2.attendancetool.interfaces.IVolleyCallback;
import com.project.group2.attendancetool.model.Notification;
import com.project.group2.attendancetool.request.AttendanceManagement;
import com.project.group2.attendancetool.request.NotificationManagement;
import com.project.group2.attendancetool.response.GetNotificationsByTeacherResponse;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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

    @SuppressLint("SetTextI18n")
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        final Notification notification = data.get(position);
        View eachItemListView = convertView;

        if (eachItemListView == null) {
            eachItemListView = inflater.inflate(R.layout.list_item_notification, parent, false);
        }
        if (notification != null) {
            eachItemListView.setTag(notification.getScheduleId());

            TextView tvReportedStudentId = eachItemListView.findViewById(R.id.tvReportedStudentId);
            tvReportedStudentId.setText(notification.getStudentId());

            TextView tvReportedCourseName = eachItemListView.findViewById(R.id.tvReportedCourseName);
            tvReportedCourseName.setText(notification.getCourse().getCourseName());

            TextView tvReportedClassId = eachItemListView.findViewById(R.id.tvReportedClassId);
            tvReportedClassId.setText(notification.getClasses().getClassId());

            TextView tvReportedDate = eachItemListView.findViewById(R.id.tvReportedDate);
            tvReportedDate.setText(notification.getDate());

            TextView tvReportedSlotNo = eachItemListView.findViewById(R.id.tvReportedSlotNo);
            tvReportedSlotNo.setText("Slot " + notification.getSlot().getSlotId());

            // Set event listener to edit attendance and decline attendance
            TextView tvEditAttendance = eachItemListView.findViewById(R.id.tvEditAttendance);
            tvEditAttendance.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent slotDetailIntent = new Intent(context, SlotDetailActivity.class);
                    String stringDate = "";
                    try {
                        Date formattedDate = new SimpleDateFormat("dd/MMMMM/yyyy").parse(notification.getDate());
                        stringDate = new SimpleDateFormat("yyyy-MM-dd").format(formattedDate);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    slotDetailIntent.putExtra("StringDate", stringDate);
                    slotDetailIntent.putExtra("Slot", notification.getSlot());
                    slotDetailIntent.putExtra("Class", notification.getClasses());
                    slotDetailIntent.putExtra("Course", notification.getCourse());
                    context.startActivity(slotDetailIntent);
                }
            });
        }

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
                        try {
                            fetchNotificationsList();
                        } catch (Exception e) {
                            Log.e(ELogTag.UI_COMPONENT_LOAD_ERROR.toString(), "Failed to reload fragment");
                        }
                    }
                }, scheduleId);
            }
        });

        return eachItemListView;
    }

    private void fetchNotificationsList() {
        NotificationManagement notificationManagement = new NotificationManagement(context);
        notificationManagement.getNotifications(new IVolleyCallback() {
            @Override
            public void onSuccess(String result) {
                Gson gson = new Gson();
                GetNotificationsByTeacherResponse getNotificationsByTeacherResponse
                        = gson.fromJson(result, GetNotificationsByTeacherResponse.class);
                refreshNotifications(getNotificationsByTeacherResponse.getNotifications());
            }
        });
    }

    private void refreshNotifications(List<Notification> notifications) {
        this.data.clear();
        this.data.addAll(notifications);
        notifyDataSetChanged();
    }

}
