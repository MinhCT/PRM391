package com.project.group2.attendancetool.response;

import com.project.group2.attendancetool.model.Schedule;

import java.util.List;

/**
 * Response Model for a list of schedule records
 */
public class ScheduleResponse {
    private List<Schedule> schedules;

    public List<Schedule> getSchedules() {
        return schedules;
    }
}
