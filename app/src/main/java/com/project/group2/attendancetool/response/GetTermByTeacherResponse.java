package com.project.group2.attendancetool.response;

import com.project.group2.attendancetool.model.Slot;
import com.project.group2.attendancetool.model.SlotInformation;

import java.util.List;

/**
 * Created by User on 3/3/2018.
 */

public class GetTermByTeacherResponse {
    private List<SlotInformation> SlotInformationList;

    public List<SlotInformation> getSlotInformationList() {
        return SlotInformationList;
    }
}
