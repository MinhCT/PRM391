package com.project.group2.attendancetool.activity.teacher.Fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.gson.Gson;
import com.project.group2.attendancetool.R;
import com.project.group2.attendancetool.activity.teacher.CustomListAdapter;
import com.project.group2.attendancetool.interfaces.IVolleyJsonCallback;
import com.project.group2.attendancetool.model.Classes;
import com.project.group2.attendancetool.model.Course;
import com.project.group2.attendancetool.model.Slot;
import com.project.group2.attendancetool.model.SlotInformation;
import com.project.group2.attendancetool.response.GetTermByTeacherResponse;
import com.project.group2.attendancetool.request.AttendanceManagement;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class SlotListFragment extends Fragment {
    private AttendanceManagement attendanceManagement;
    ListView lvSlotList;
    Spinner spDate;
    private List<String> dateToChoose;

    public SlotListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        attendanceManagement = new AttendanceManagement(this.getContext());
        lvSlotList = (ListView)getView().findViewById(R.id.lvSlotList);
        spDate = (Spinner) getView().findViewById(R.id.spDate);

        List<String> dateToAdd = new ArrayList<>();
        Date date = new Date();
        Date yesterday = new Date();
        Date beforeYesterday = new Date();
        yesterday.setDate(date.getDate()-1);
        beforeYesterday.setDate(yesterday.getDate()-1);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        dateToAdd.add(formatter.format(date));
        dateToAdd.add(formatter.format(yesterday));
        dateToAdd.add(formatter.format(beforeYesterday));
        loadDateToSpinner(dateToAdd);

        spDate.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                final String choosingDate = (String)adapterView.getItemAtPosition(i);
                attendanceManagement.getSlotList(new IVolleyJsonCallback() {
                    @Override
                    public void onJsonRequestSucess(JSONObject result) {
                        Gson gson = new Gson();
                        String resultInString = result.toString();
                        GetTermByTeacherResponse response = gson.fromJson(resultInString,GetTermByTeacherResponse.class);
                        lvSlotList.setAdapter(new CustomListAdapter(getContext(), response.getSlotInformationList(), choosingDate));
                    }
                }, choosingDate);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                Toast.makeText(getContext(),"nothing selected",Toast.LENGTH_SHORT).show();
            }
        });


        super.onViewCreated(view, savedInstanceState);
    }

    private void loadDateToSpinner(List<String> stringDates) {
        dateToChoose = new ArrayList<>();
        List<String> termNameList = new ArrayList<>();
        for (String date : stringDates) {
            dateToChoose.add(date);
        }
        ArrayAdapter<String> dateSpinnerAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, dateToChoose);
        spDate.setAdapter(dateSpinnerAdapter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_slot_list, container, false);
    }

}
