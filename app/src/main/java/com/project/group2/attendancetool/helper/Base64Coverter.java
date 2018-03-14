package com.project.group2.attendancetool.helper;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import java.io.ByteArrayOutputStream;

/**
 * Helper class to help convert base64 to appropriate types
 */
public class Base64Coverter {
    public static Bitmap toImageBitmap(String base64String) {
        byte[] imgBytes = Base64.decode(base64String, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(imgBytes, 0, imgBytes.length);
    }

    public static String toBase64(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 10, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();

        String encodedString = null;
        try {
            System.gc();
            encodedString = Base64.encodeToString(byteArray, Base64.DEFAULT);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return encodedString;
    }
}

