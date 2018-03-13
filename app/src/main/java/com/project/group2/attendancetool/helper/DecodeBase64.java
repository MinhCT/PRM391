package com.project.group2.attendancetool.helper;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

/**
 * Helper class to help convert base64 to appropriate types
 */
public class DecodeBase64 {
    public static Bitmap toImageBitmap(String base64String) {
        byte[] imgBytes = Base64.decode(base64String, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(imgBytes, 0, imgBytes.length);
    }
}

