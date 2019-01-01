package com.dcxiaolou.innervoicemvp.course.coursePlay;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.dcxiaolou.innervoicemvp.R;
import com.dcxiaolou.innervoicemvp.base.BaseActivity;
import com.dcxiaolou.innervoicemvp.course.ShowCourseActivity;
import com.dcxiaolou.innervoicemvp.utils.SharedPreferencesUtils;
import com.shuyu.gsyvideoplayer.GSYVideoManager;
import com.shuyu.gsyvideoplayer.utils.OrientationUtils;
import com.shuyu.gsyvideoplayer.video.StandardGSYVideoPlayer;

/*
 * 课程的播放
 * */
public class CoursePlayActivity extends BaseActivity implements CoursePlayContract.View {

    public final static String COURSE_NO = "course_no"; //当前是该课程的第几个音频

    private Context mContext;
    private Handler mHandler = new Handler();

    private String courseId, courseCover, courseTitle;
    private int courseNo;

    private String courseUrl;

    StandardGSYVideoPlayer videoPlayer;

    OrientationUtils orientationUtils;

    private String playType;

    private int playState = 1;

    private CoursePlayContract.Presenter mPresenter;

    @Override
    protected int setLayout() {
        return R.layout.activity_course_play;
    }

    @Override
    protected void init() {
        mContext = getApplicationContext();

        videoPlayer = (StandardGSYVideoPlayer) findViewById(R.id.course_player);

        Intent intent = getIntent();
        courseNo = intent.getIntExtra(COURSE_NO, 0) + 1; // 从0开始

        courseId = SharedPreferencesUtils.getString("courseId");
        courseCover = SharedPreferencesUtils.getString("courseCover");
        courseTitle = SharedPreferencesUtils.getString("courseTitle");
        //Log.d(TAG, courseId + " " + courseNo + " " + courseCover + " " + courseTitle);

        mPresenter = new CoursePlayPresenter(this);
        //从bmob获取相应的课程音频json文件地址
        String courseFileName = courseId + "_" + courseNo + ".json";
        mPresenter.getCoursePlay(courseFileName);
    }

    @Override
    protected void setListener() {

    }

    @Override
    public void showCoursePlay(final String data) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                int length = data.length();
                playType = data.substring(length - 3, length);
                //Log.d(TAG, "playType = " + playType); //获取视频的类型（MP3、MP4）
                videoPlayer.setUp(data, true, "测试视频");
                //增加封面
                final ImageView imageView = new ImageView(mContext);
                imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                RequestOptions options = new RequestOptions().diskCacheStrategy(DiskCacheStrategy.RESOURCE);
                options.override(500, 400);
                Glide.with(mContext).load(courseCover).apply(options).into(imageView);
                videoPlayer.setThumbImageView(imageView);
                //增加title
                videoPlayer.getTitleTextView().setText(courseTitle);
                //设置返回键
                videoPlayer.getBackButton().setImageResource(R.drawable.video_back);
                //设置旋转
                orientationUtils = new OrientationUtils(CoursePlayActivity.this, videoPlayer);
                //设置全屏按键功能,这是使用的是选择屏幕，而不是全屏
                videoPlayer.getFullscreenButton().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        orientationUtils.resolveByClick();
                    }
                });
                //是否可以滑动调整
                videoPlayer.setIsTouchWiget(true);
                //设置返回按键功能
                videoPlayer.getBackButton().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onBackPressed();
                    }
                });

                final int width = videoPlayer.getWidth();
                final int height = videoPlayer.getHeight();

                // 播放按钮
                final View playBtn = videoPlayer.getStartButton();
                playBtn.setVisibility(View.VISIBLE);
                playBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (playState == 1) { // 第一次播放
                            videoPlayer.startPlayLogic();
                            if (playType.equals("mp3")) { // 播放mp3时只显示光碟旋转动画
                                ViewGroup parent = (ViewGroup) imageView.getParent();
                                parent.removeView(imageView);
                                // 使用glide加载gif动画
                                RequestOptions options = new RequestOptions().diskCacheStrategy(DiskCacheStrategy.RESOURCE);
                                options.override(500, 400);
                                Glide.with(mContext).load(R.drawable.rotation).apply(options).into(imageView);
                                videoPlayer.addView(imageView);
                                playBtn.setY(height - 300);
                                playBtn.setX(20);
                            }
                            playState = 2;
                        } else if (playState == 2) { // 暂停
                            videoPlayer.onVideoPause();
                            if (playType.equals("mp3")) {
                                ViewGroup parent = (ViewGroup) imageView.getParent();
                                parent.removeView(imageView);
                                RequestOptions options = new RequestOptions().diskCacheStrategy(DiskCacheStrategy.RESOURCE);
                                options.override(500, 400);
                                Glide.with(mContext).load(courseCover).apply(options).into(imageView);
                                videoPlayer.addView(imageView);
                                playBtn.setY(height - 300);
                                playBtn.setX(20);
                            }
                            playBtn.setVisibility(View.VISIBLE);
                            playState = 3;
                        } else { // 恢复播放
                            videoPlayer.onVideoResume();
                            if (playType.equals("mp3")) {
                                ViewGroup parent = (ViewGroup) imageView.getParent();
                                parent.removeView(imageView);
                                // 使用glide加载gif动画
                                RequestOptions options = new RequestOptions().diskCacheStrategy(DiskCacheStrategy.RESOURCE);
                                options.override(500, 400);
                                Glide.with(mContext).load(R.drawable.rotation).apply(options).into(imageView);
                                videoPlayer.addView(imageView);
                                playBtn.setY(height - 300);
                                playBtn.setX(20);
                            }
                            playState = 2;
                        }
                    }
                });
            }
        });
    }

    @Override
    public void showErrorMessage(String message) {
        Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
        Intent backIntent = new Intent(mContext, ShowCourseActivity.class);
        backIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);//它可以关掉所要到的界面中间的activity
        startActivity(backIntent);
    }

    @Override
    protected void onPause() {
        super.onPause();
        videoPlayer.onVideoPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        videoPlayer.onVideoResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        GSYVideoManager.releaseAllVideos();
        if (orientationUtils != null)
            orientationUtils.releaseListener();
    }

    @Override
    public void onBackPressed() {
        //先返回正常状态
        if (orientationUtils.getScreenType() == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
            videoPlayer.getFullscreenButton().performClick();
            return;
        }
        //释放所有
        videoPlayer.setVideoAllCallBack(null);
        super.onBackPressed();
    }

}
