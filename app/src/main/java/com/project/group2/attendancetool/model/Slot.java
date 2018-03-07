package com.project.group2.attendancetool.model;

import java.io.Serializable;

/**
 * Created by User on 3/3/2018.
 */

public class Slot implements Serializable {
    public int SlotId;
    public String StartTime;
    public String EndTime;

    public Slot(int slotId, String startTime, String endTime) {
        SlotId = slotId;
        StartTime = startTime;
        EndTime = endTime;
    }

    public int getSlotId() {
        return SlotId;
    }

    public void setSlotId(int slotId) {
        SlotId = slotId;
    }

    public String getStartTime() {
        return StartTime;
    }

    public void setStartTime(String startTime) {
        StartTime = startTime;
    }

    public String getEndTime() {
        return EndTime;
    }

    public void setEndTime(String endTime) {
        EndTime = endTime;
    }
}
