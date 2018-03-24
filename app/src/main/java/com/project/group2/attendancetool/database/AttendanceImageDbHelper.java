package com.project.group2.attendancetool.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.project.group2.attendancetool.enums.ELogTag;

/**
 * Local database to manage submitted attendance images
 */
public class AttendanceImageDbHelper extends SQLiteOpenHelper {

    private SQLiteDatabase db;

    private final String TABLE_NAME = "LocalAttendanceImage";

    public AttendanceImageDbHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        db = sqLiteDatabase;
        createAttendanceImageTable();
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
    }

    public void insertNewRow(AttendanceImageData imageData) {
        db = getWritableDatabase();
        db.insert(TABLE_NAME, null, prepareData(imageData));
    }

    public void deleteRow(long scheduleId) {
        db.delete(TABLE_NAME, "ScheduleId = " + scheduleId, null);
    }

    /**
     * Query out a record contains teacher id along with attendance image paths
     *
     * @param teacherId - id of the teacher
     * @param date      - date of the attendance submission
     * @param slotId    - the slot id of the attendance submission
     * @return - Data containing teacher id and the submitted attendance image paths
     */
    public AttendanceImageData getRow(String teacherId, String date, int slotId) {
        Cursor cursor = null;
        String[] selectedColumns = {"TeacherId", "LocalImgPathFirst", "LocalImgPathSecond", "LocalImgPathThird"};
        String[] queryArgs = {teacherId, date, String.valueOf(slotId)};
        String querySelection = "TeacherId = ? AND Date = ? AND SlotId = ?";

        try {
            db = getReadableDatabase();
            cursor = db.query(TABLE_NAME, selectedColumns, querySelection, queryArgs, null, null, null);
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                return new AttendanceImageData(
                        cursor.getString(cursor.getColumnIndex("TeacherId")),
                        cursor.getString(cursor.getColumnIndex("LocalImgPathFirst")),
                        cursor.getString(cursor.getColumnIndex("LocalImgPathSecond")),
                        cursor.getString(cursor.getColumnIndex("LocalImgPathThird"))
                );
            }
        } catch (Exception ex) {
            Log.e(ELogTag.SQLITE_ERROR.getTagDescription(), ex.getMessage());
        } finally {
            if (cursor != null) cursor.close();
        }

        return null;
    }

    /**
     * Prepare object data to parcelable data so that SQLite be able to use it as a content
     *
     * @param imageData - data for a new row, used to create or update a row
     * @return - a parcelable row data
     */
    private ContentValues prepareData(AttendanceImageData imageData) {
        ContentValues newRowValues = new ContentValues();
        newRowValues.put("Date", imageData.date);
        newRowValues.put("SlotId", imageData.slotId);
        newRowValues.put("TeacherId", imageData.teacherId);
        newRowValues.put("LocalImgPathFirst", imageData.localImgPathFirst);
        newRowValues.put("LocalImgPathSecond", imageData.localImgPathSecond);
        newRowValues.put("LocalImgPathThird", imageData.localImgPathThird);
        return newRowValues;
    }

    /**
     * Create table in local database to store attendance image data
     */
    private void createAttendanceImageTable() {
        final String CREATE_TABLE_SQL = "CREATE TABLE " + TABLE_NAME +
                "(Id INTEGER PRIMARY KEY NOT NULL," +
                "TeacherId VARCHAR(50) NOT NULL," +
                "Date VARCHAR(50) NOT NULL," +
                "SlotId INT NOT NULL," +
                "LocalImgPathFirst VARCHAR(300)," +
                "LocalImgPathSecond VARCHAR(300)," +
                "LocalImgPathThird VARCHAR(300))";
        execSql(CREATE_TABLE_SQL);
    }

    /**
     * Call the parent method to execute writable sql to the database
     *
     * @param sql - SQL string to be executed
     */
    private void execSql(String sql) {
        if (db.isReadOnly()) {
            db = getWritableDatabase();
        }
        db.execSQL(sql);
    }

    public static class AttendanceImageData {
        private String teacherId;
        private int slotId;
        private String date;
        private String localImgPathFirst;
        private String localImgPathSecond;
        private String localImgPathThird;

        public AttendanceImageData() {
        }

        public AttendanceImageData(String teacherId, int slotId, String date, String localImgPathFirst, String localImgPathSecond, String localImgPathThird) {
            this.teacherId = teacherId;
            this.slotId = slotId;
            this.date = date;
            this.localImgPathFirst = localImgPathFirst;
            this.localImgPathSecond = localImgPathSecond;
            this.localImgPathThird = localImgPathThird;
        }

        public AttendanceImageData(String teacherId, String localImgPathFirst, String localImgPathSecond, String localImgPathThird) {
            this.teacherId = teacherId;
            this.localImgPathFirst = localImgPathFirst;
            this.localImgPathSecond = localImgPathSecond;
            this.localImgPathThird = localImgPathThird;
        }

        public String getTeacherId() {
            return teacherId;
        }

        public int getSlotId() {
            return slotId;
        }

        public String getDate() {
            return date;
        }

        public String getLocalImgPathFirst() {
            return localImgPathFirst;
        }

        public String getLocalImgPathSecond() {
            return localImgPathSecond;
        }

        public String getLocalImgPathThird() {
            return localImgPathThird;
        }
    }

    public class DatabaseInfo {
        public static final String DB_NAME = "AttendanceImageLocal.db";
        public static final int DB_VERSION = 1; // can be changed here to update database version
    }
}
