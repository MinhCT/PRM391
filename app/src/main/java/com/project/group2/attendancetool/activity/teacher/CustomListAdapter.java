package com.project.group2.attendancetool.activity.teacher;

import android.content.Context;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.project.group2.attendancetool.R;
import com.project.group2.attendancetool.model.Classes;
import com.project.group2.attendancetool.model.Course;
import com.project.group2.attendancetool.model.Slot;
import com.project.group2.attendancetool.model.SlotInformation;

import java.util.List;

/**
 * Created by User on 3/4/2018.
 */

public class CustomListAdapter extends BaseAdapter {
    private LayoutInflater layoutInflater;
    private Context context;
    private List<SlotInformation> listData;

    public CustomListAdapter(Context context, List<SlotInformation> listData) {
        layoutInflater = LayoutInflater.from(context);
        this.context = context;
        this.listData = listData;
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
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        SlotInformation slotInformation = this.listData.get(i);
        Slot slot = slotInformation.getSlot();
        Course course = slotInformation.getCourse();
        Classes classes = slotInformation.getClasses();
        //Slot 1 (7h30 - 9h00)
        holder.tvSlot.setText("Slot: "+ slot.getSlotId()+ "  (" + slot.getStartTime()+ " - "+slot.getEndTime()+")");
        //Class: IS1101
        holder.tvClass.setText("Class: "+classes.getClassName());
        //Subject: PRM391 - Mobile Programming
        holder.tvCourse.setText("Subject: "+course.getCourseName());
        return view;
    }
}
