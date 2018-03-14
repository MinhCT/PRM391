package com.project.group2.attendancetool.activity.teacher;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.ListView;

import com.google.gson.Gson;
import com.project.group2.attendancetool.R;
import com.project.group2.attendancetool.adapter.StudentListWithCheckboxAdapter;
import com.project.group2.attendancetool.helper.Base64Coverter;
import com.project.group2.attendancetool.helper.JsonObjectConverter;
import com.project.group2.attendancetool.interfaces.IVolleyCallback;
import com.project.group2.attendancetool.model.AttendanceRequest;
import com.project.group2.attendancetool.model.StudentAttendance;
import com.project.group2.attendancetool.request.AttendanceManagement;
import com.sangcomz.fishbun.FishBun;
import com.sangcomz.fishbun.adapter.image.impl.GlideAdapter;
import com.sangcomz.fishbun.define.Define;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class TakeAttendanceActivity extends AppCompatActivity {

    private Animator currentAnimator;
    private int shortAnimationDuration;
    private ArrayList<Uri> selectedImagesPaths;
    private ArrayList<Bitmap> selectedImageBitmaps;

    @BindView(R.id.ivAttendanceImg1)
    ImageView ivAttendanceImg1;
    @BindView(R.id.ivAttendanceImg2)
    ImageView ivAttendanceImg2;
    @BindView(R.id.ivAttendanceImg3)
    ImageView ivAttendanceImg3;
    @BindView(R.id.ivExpandedImage)
    ImageView ivExpandedImage;
    @BindView(R.id.lvStudentWithAttendances)
    ListView lvStudentWithAttendances;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_attendance);
        setupUI();

        // Retrieve and cache the system's default "short" animation time.
        shortAnimationDuration = getResources().getInteger(
                android.R.integer.config_shortAnimTime);
    }

    private void setupUI() {
        ButterKnife.bind(this);

        Intent intent = getIntent();
        ArrayList<StudentAttendance> studentAttendances = intent.getParcelableArrayListExtra("studentListWithAttendance");
        lvStudentWithAttendances.setAdapter(new StudentListWithCheckboxAdapter(studentAttendances, this));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case Define.ALBUM_REQUEST_CODE:
                selectedImagesPaths = data.getParcelableArrayListExtra(Define.INTENT_PATH);
                selectedImageBitmaps = new ArrayList<>();

                Picasso.with(this).load(selectedImagesPaths.get(0)).into(ivAttendanceImg1);
                Picasso.with(this).load(selectedImagesPaths.get(1)).into(ivAttendanceImg2);
                Picasso.with(this).load(selectedImagesPaths.get(2)).into(ivAttendanceImg3);
                break;
        }
    }

    @OnClick(R.id.btnSelectImages)
    void pickImagesFromGallery() {
        FishBun.with(this).setImageAdapter(new GlideAdapter())
                .setMaxCount(3)
                .setReachLimitAutomaticClose(true)
                .setAllViewTitle("Choose Attendance Images")
                .startAlbum();
    }

    @OnClick({R.id.ivAttendanceImg1, R.id.ivAttendanceImg2, R.id.ivAttendanceImg3})
    void zoomImage(View thumbView) {
        // If there's an animation in progress, cancel it immediately and proceed with this one.
        if (currentAnimator != null) {
            currentAnimator.cancel();
        }

        loadZoomedInImage(thumbView); // Load the high-resolution "zoomed-in" image.

        final Rect startBounds = new Rect();
        final Rect finalBounds = new Rect();
        final Point globalOffset = new Point();

        thumbView.getGlobalVisibleRect(startBounds);
        findViewById(R.id.container_take_attendance_activity)
                .getGlobalVisibleRect(finalBounds, globalOffset);
        startBounds.offset(-globalOffset.x, -globalOffset.y);
        finalBounds.offset(-globalOffset.x, -globalOffset.y);

        float startScale;
        if ((float) finalBounds.width() / finalBounds.height()
                > (float) startBounds.width() / startBounds.height()) {
            // Extend start bounds horizontally
            startScale = (float) startBounds.height() / finalBounds.height();
            float startWidth = startScale * finalBounds.width();
            float deltaWidth = (startWidth - startBounds.width()) / 2;
            startBounds.left -= deltaWidth;
            startBounds.right += deltaWidth;
        } else {
            // Extend start bounds vertically
            startScale = (float) startBounds.width() / finalBounds.width();
            float startHeight = startScale * finalBounds.height();
            float deltaHeight = (startHeight - startBounds.height()) / 2;
            startBounds.top -= deltaHeight;
            startBounds.bottom += deltaHeight;
        }

        //thumbView.setAlpha(0f);
        ivExpandedImage.setVisibility(View.VISIBLE);

        // Set the pivot point for SCALE_X and SCALE_Y transformations
        // to the top-left corner of the zoomed-in view (the default
        // is the center of the view).
        ivExpandedImage.setPivotX(0f);
        ivExpandedImage.setPivotY(0f);

        // Construct and run the parallel animation of the four translation and
        // scale properties (X, Y, SCALE_X, and SCALE_Y).
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet
                .play(ObjectAnimator.ofFloat(ivExpandedImage, View.X,
                        startBounds.left, finalBounds.left))
                .with(ObjectAnimator.ofFloat(ivExpandedImage, View.Y,
                        startBounds.top, finalBounds.top))
                .with(ObjectAnimator.ofFloat(ivExpandedImage, View.SCALE_X,
                        startScale, 1f))
                .with(ObjectAnimator.ofFloat(ivExpandedImage,
                        View.SCALE_Y, startScale, 1f));
        animatorSet.setDuration(shortAnimationDuration);
        animatorSet.setInterpolator(new DecelerateInterpolator());
        animatorSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                currentAnimator = null;
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                currentAnimator = null;
            }
        });
        animatorSet.start();
        currentAnimator = animatorSet;

        // Upon clicking the zoomed-in image, it should zoom back down
        // to the original bounds and show the thumbnail instead of
        // the expanded image.
        final float startScaleFinal = startScale;
        ivExpandedImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                if (currentAnimator != null) {
                    currentAnimator.cancel();
                }

                // Animate the four positioning/sizing properties in parallel,
                // back to their original values.
                AnimatorSet set = new AnimatorSet();
                set.play(ObjectAnimator
                        .ofFloat(ivExpandedImage, View.X, startBounds.left))
                        .with(ObjectAnimator
                                .ofFloat(ivExpandedImage,
                                        View.Y, startBounds.top))
                        .with(ObjectAnimator
                                .ofFloat(ivExpandedImage,
                                        View.SCALE_X, startScaleFinal))
                        .with(ObjectAnimator
                                .ofFloat(ivExpandedImage,
                                        View.SCALE_Y, startScaleFinal));
                set.setDuration(shortAnimationDuration);
                set.setInterpolator(new DecelerateInterpolator());
                set.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        //view.setAlpha(1f);
                        ivExpandedImage.setVisibility(View.GONE);
                        currentAnimator = null;
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {
                        //view.setAlpha(1f);
                        ivExpandedImage.setVisibility(View.GONE);
                        currentAnimator = null;
                    }
                });
                set.start();
                currentAnimator = set;
            }
        });
    }

    private void loadZoomedInImage(View view) {
        switch (view.getId()) {
            case R.id.ivAttendanceImg1:
                Picasso.with(this).load(selectedImagesPaths.get(0)).into(ivExpandedImage);
                break;
            case R.id.ivAttendanceImg2:
                Picasso.with(this).load(selectedImagesPaths.get(1)).into(ivExpandedImage);
                break;
            case R.id.ivAttendanceImg3:
                Picasso.with(this).load(selectedImagesPaths.get(2)).into(ivExpandedImage);
                break;
        }
    }

    @OnClick(R.id.btnSubmitImages)
    void submitImages() {
        AttendanceManagement attendanceManagement = new AttendanceManagement(this);
        attendanceManagement.submitAttendanceImages(new IVolleyCallback() {
            @Override
            public void onSuccess(String result) {

            }
        }, buildTakeAttendanceJSONObjectRequest());
    }

    private JSONObject buildTakeAttendanceJSONObjectRequest() {
        String[] base64ImageStrings = new String[3];
        Intent intent = getIntent();
        SharedPreferences userInfoPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        List<String> imageUrls = new ArrayList<>();

        base64ImageStrings[0] = Base64Coverter.toBase64(((BitmapDrawable)ivAttendanceImg1.getDrawable()).getBitmap());
        base64ImageStrings[1] = Base64Coverter.toBase64(((BitmapDrawable)ivAttendanceImg2.getDrawable()).getBitmap());
        base64ImageStrings[2] = Base64Coverter.toBase64(((BitmapDrawable)ivAttendanceImg3.getDrawable()).getBitmap());
        int slotId = intent.getIntExtra("slotId", -1);
        String date = intent.getStringExtra("date");

        // Create Object to ready to convert to JSONObject
        imageUrls.add(base64ImageStrings[0]);
        imageUrls.add(base64ImageStrings[1]);
        imageUrls.add(base64ImageStrings[2]);
        AttendanceRequest attendanceRequest = new AttendanceRequest(
                userInfoPreferences.getString("userId", null),
                userInfoPreferences.getString("userRole", null),
                imageUrls,
                "IS1101",
                slotId,
                "2018-02-05"
        );

        JsonObjectConverter<AttendanceRequest> converter = new JsonObjectConverter<>();
        return converter.toJsonObject(attendanceRequest);
    }
}

