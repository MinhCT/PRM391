package com.project.group2.attendancetool.activity.teacher;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.project.group2.attendancetool.R;
import com.project.group2.attendancetool.model.Classes;
import com.project.group2.attendancetool.model.Course;
import com.project.group2.attendancetool.model.Slot;
import com.project.group2.attendancetool.model.SlotInformation;

import java.util.Date;
import java.util.List;

/**
 * Created by User on 3/4/2018.
 */

public class CustomListAdapter extends BaseAdapter {
    private LayoutInflater layoutInflater;
    private Context context;
    private List<SlotInformation> listData;
    private String stringDate;

    public CustomListAdapter(Context context, List<SlotInformation> listData, String stringDate) {
        layoutInflater = LayoutInflater.from(context);
        this.context = context;
        this.listData = listData;
        this.stringDate = stringDate;
    }

    @Override
    public int getCount() {
        return listData.size();
    }

    @Override
    public Object getItem(int i) {
        return listData.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    static class ViewHolder{
        TextView tvSlot;
        TextView tvClass;
        TextView tvCourse;
        Button btnSlotDetail;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder;
        if(view == null){
            view = layoutInflater.inflate(R.layout.list_slot_layout,null);
            holder = new ViewHolder();
            holder.tvClass = (TextView) view.findViewById(R.id.tvClass);
            holder.tvCourse = (TextView) view.findViewById(R.id.tvCourse);
            holder.tvSlot = (TextView) view.findViewById(R.id.tvSlot);
            holder.btnSlotDetail = (Button)view.findViewById(R.id.btnSlotDetail);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        SlotInformation slotInformation = this.listData.get(i);
        final Slot slot = slotInformation.getSlot();
        final Course course = slotInformation.getCourse();
        final Classes classes = slotInformation.getClasses();
        //Slot 1 (7h30 - 9h00)
        holder.tvSlot.setText("Slot: "+ slot.getSlotId()+ "  (" + slot.getStartTime()+ " - "+slot.getEndTime()+")");
        //Class: IS1101
        holder.tvClass.setText("Class: "+classes.getClassName());
        //Subject: PRM391 - Mobile Programming
        holder.tvCourse.setText("Subject: "+course.getCourseName());

        holder.btnSlotDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, SlotDetailActivity.class);
                intent.putExtra("StringDate",stringDate);
                intent.putExtra("Slot",slot);
                intent.putExtra("Class",classes);
                intent.putExtra("Course", course);
                context.startActivity(intent);
            }
        });
        return view;
    }
}
