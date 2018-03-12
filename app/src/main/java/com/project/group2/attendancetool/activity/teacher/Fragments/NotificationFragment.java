package com.project.group2.attendancetool.activity.teacher.Fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.google.gson.Gson;
import com.project.group2.attendancetool.R;
import com.project.group2.attendancetool.adapter.NotificationListAdapter;
import com.project.group2.attendancetool.interfaces.IVolleyCallback;
import com.project.group2.attendancetool.model.Notification;
import com.project.group2.attendancetool.request.NotificationManagement;
import com.project.group2.attendancetool.response.GetNotificationsByTeacherResponse;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class NotificationFragment extends Fragment {

    @BindView(R.id.lvNotifications)
    ListView lvNotifications;

    public NotificationFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_notification, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        NotificationManagement notificationManagement = new NotificationManagement(getActivity());
        notificationManagement.getNotifications(new IVolleyCallback() {
            @Override
            public void onSuccess(String result) {
                Gson gson = new Gson();
                GetNotificationsByTeacherResponse getNotificationsByTeacherResponse
                        = gson.fromJson(result, GetNotificationsByTeacherResponse.class);
                loadNotificationsToListView(getNotificationsByTeacherResponse.getNotifications());
            }
        });
    }

    private void loadNotificationsToListView(List<Notification> notifications) {
        ArrayList<Notification> notificationArrayList = new ArrayList<>(notifications);
        NotificationListAdapter notificationListAdapter = new NotificationListAdapter(notificationArrayList, getActivity());
        lvNotifications.setAdapter(notificationListAdapter);
    }
}
