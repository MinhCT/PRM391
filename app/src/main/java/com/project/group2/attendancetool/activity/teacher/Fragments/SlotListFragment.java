package com.project.group2.attendancetool.activity.teacher.Fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.google.gson.Gson;
import com.project.group2.attendancetool.R;
import com.project.group2.attendancetool.activity.teacher.CustomListAdapter;
import com.project.group2.attendancetool.interfaces.IVolleyCallback;
import com.project.group2.attendancetool.interfaces.IVolleyJsonCallback;
import com.project.group2.attendancetool.model.Classes;
import com.project.group2.attendancetool.model.Course;
import com.project.group2.attendancetool.model.Slot;
import com.project.group2.attendancetool.model.SlotInformation;
import com.project.group2.attendancetool.response.GetTermByTeacherResponse;
import com.project.group2.attendancetool.util.AttendanceManagement;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class SlotListFragment extends Fragment {
    private AttendanceManagement attendanceManagement;
    ListView lvSlotList;

    public SlotListFragment() {
        // Required empty public constructor
    }

    private List<SlotInformation> getSlotInformationList(){
        List<SlotInformation> slotInformationList = new ArrayList<SlotInformation>();
        Slot slot = new Slot(1,"7h30","9h00");
        Course course = new Course("PRM391", "Mobile Programming");
        Classes classes = new Classes("IS1101","IS1101");
        SlotInformation slotInformation = new SlotInformation(slot, course, classes);
        slotInformationList.add(slotInformation);
        return slotInformationList;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        attendanceManagement = new AttendanceManagement(this.getContext());
        lvSlotList = (ListView)getView().findViewById(R.id.lvSlotList);
        Date date = new Date();
        attendanceManagement.getSlotList(new IVolleyJsonCallback() {
            @Override
            public void onJsonRequestSucess(JSONObject result) {
                Gson gson = new Gson();
                String resultInString = result.toString();
                GetTermByTeacherResponse response = gson.fromJson(resultInString,GetTermByTeacherResponse.class);
                lvSlotList.setAdapter(new CustomListAdapter(getContext(), response.getSlotInformationList()));
            }
        }, date);
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_slot_list, container, false);
    }

}
