package com.project.group2.attendancetool.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by User on 3/6/2018.
 */
public class StudentAttendance extends Student implements Parcelable {
    private String AttendanceStatus;

    public StudentAttendance(String studentId, String fullName, String email, String image, String attendanceStatus) {
        super(studentId, fullName, email, image);
        AttendanceStatus = attendanceStatus;
    }

    public StudentAttendance(Parcel in) {
        super();
        setStudentId(in.readString());
        setEmail(in.readString());
        setFullName(in.readString());
        setImage(in.readString());
        AttendanceStatus = in.readString();
    }

    public String getAttendanceStatus() {
        return AttendanceStatus;
    }

    public static final Creator<StudentAttendance> CREATOR = new Creator<StudentAttendance>() {
        @Override
        public StudentAttendance createFromParcel(Parcel in) {
            return new StudentAttendance(in);
        }

        @Override
        public StudentAttendance[] newArray(int size) {
            return new StudentAttendance[size];
        }
    };


    public void setAttendanceStatus(String attendanceStatus) {
        AttendanceStatus = attendanceStatus;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(getStudentId());
        parcel.writeString(getEmail());
        parcel.writeString(getFullName());
        parcel.writeString(getImage());
        parcel.writeString(AttendanceStatus);
    }


}
