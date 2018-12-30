package com.dcxiaolou.innervoicemvp.splash;
/**
 * Splash模块中的View
 * 管理启动模块中的视图界面
 */
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.dcxiaolou.innervoicemvp.R;
import com.dcxiaolou.innervoicemvp.guide.GuideActivity;
import com.dcxiaolou.innervoicemvp.home.HomeActivity;
import com.dcxiaolou.innervoicemvp.ui.CountDownProgressBar;

/*
 * 使用android:theme="@android:style/Theme.NoTitleBar.Fullscreen"（全屏）需要继承Activity
 * 且其他Activity有使用android:theme="@style/Theme.AppCompat.NoActionBar"
 * */

public class SplashActivity extends Activity implements SplashContract.View {

    /*private TimeTask mTimeTask;*/
    private SplashContract.Presenter mPresenter;

    // 倒计时进度条
    private CountDownProgressBar cpb_countdown;
    //标记是否跳过欢迎页，解决跳过欢迎页后主页加载两次的bug
    private boolean isPass = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        init();

        setListener();

    }

    protected void init() {

        cpb_countdown = (CountDownProgressBar) findViewById(R.id.cpb_countdown);
        /*mTimeTask = new TimeTask();*/
        mPresenter = new SplashPresenter(this);
        /*mTimeTask.execute();*/
    }

    protected void setListener() {

        cpb_countdown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //把异步任务标记为停止
                /*mTimeTask.cancel(true);*/
                mPresenter.isFirstRun();
                finish();
            }
        });

        cpb_countdown.setDuration(3000, new CountDownProgressBar.OnFinishListener() {
            @Override
            public void onFinish() {
                /*mTimeTask.cancel(true);*/
                mPresenter.isFirstRun();
                finish();
            }
        });

    }

    protected void toNewView(Class activityClass) {
        Intent intent = new Intent();
        intent.setClass(getApplicationContext(), activityClass);
        startActivity(intent);
    }

    @Override
    public void toGuideView() {

        toNewView(GuideActivity.class);
        finish();

    }

    @Override
    public void toHomeView() {

        toNewView(HomeActivity.class);
        finish();

    }

    /**
     * 异步任务类
     * 第一个泛型，限定任务执行时是否需要传入外部数据
     * 第二个泛型，限定任务执行过程时发布任务进度的数据类型
     * 第三个泛型，限定任务执行完成时，返回的数据类型
     */
    /*private class TimeTask extends AsyncTask<Void, String, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            for (int i = 3; i >= 0; i--) {
                //判断异步任务是否标记为结束
                if (isCancelled()) {
                    break;
                }
                try {
                    //发布进度
                    publishProgress(i + "");
                    //线程休眠1秒
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            mPresenter.isFirstRun();
            finish();
        }

    }*/
}
