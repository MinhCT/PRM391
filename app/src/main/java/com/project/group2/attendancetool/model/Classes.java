package com.project.group2.attendancetool.model;

import java.io.Serializable;

/**
 * Created by User on 3/4/2018.
 */

public class Classes implements Serializable {
    public String ClassId;
    public String ClassName;

    public Classes(String classId, String className) {
        ClassId = classId;
        ClassName = className;
    }

    public String getClassId() {
        return ClassId;
    }

    public void setClassId(String classId) {
        ClassId = classId;
    }

    public String getClassName() {
        return ClassName;
    }

    public void setClassName(String className) {
        ClassName = className;
    }
}
