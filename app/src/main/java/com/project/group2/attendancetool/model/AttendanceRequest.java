package com.project.group2.attendancetool.model;

import java.util.List;

/**
 * Created by minhc on 14/03/2018.
 */

public class AttendanceRequest {
    private String UserId;
    private String RoleName;
    private List<String> ImageUrls;
    private String GalleryName;
    private int SlotId;
    private String Date;

    public AttendanceRequest() {

    }

    public AttendanceRequest(String userId, String roleName, List<String> imageUrls, String galleryName, int slotId, String date) {
        UserId = userId;
        RoleName = roleName;
        ImageUrls = imageUrls;
        GalleryName = galleryName;
        SlotId = slotId;
        Date = date;
    }

    public String getUserId() {
        return UserId;
    }

    public void setUserId(String userId) {
        UserId = userId;
    }

    public String getRoleName() {
        return RoleName;
    }

    public void setRoleName(String roleName) {
        RoleName = roleName;
    }

    public List<String> getImageUrls() {
        return ImageUrls;
    }

    public void setImageUrls(List<String> imageUrls) {
        ImageUrls = imageUrls;
    }

    public String getGalleryName() {
        return GalleryName;
    }

    public void setGalleryName(String galleryName) {
        GalleryName = galleryName;
    }

    public int getSlotId() {
        return SlotId;
    }

    public void setSlotId(int slotId) {
        SlotId = slotId;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }
}
