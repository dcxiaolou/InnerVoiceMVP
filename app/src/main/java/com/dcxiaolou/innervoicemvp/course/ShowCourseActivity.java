package com.dcxiaolou.innervoicemvp.course;

import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.dcxiaolou.innervoicemvp.R;
import com.dcxiaolou.innervoicemvp.base.BaseActivity;
import com.dcxiaolou.innervoicemvp.data.entity.CourseGuide;

import java.util.ArrayList;
import java.util.List;

/*
 * 课程模块引导页
 * */
public class ShowCourseActivity extends BaseActivity implements ShowCourseContract.View {

    private Handler mHandler = new Handler();

    private RecyclerView courseRecyclerView;

    private ShowCourseContract.Presenter mPresenter;

    @Override
    protected int setLayout() {
        return R.layout.activity_show_course;
    }

    @Override
    protected void init() {
        courseRecyclerView = (RecyclerView) findViewById(R.id.show_course_rv);
        mPresenter = new ShowCoursePresenter(this);
        mPresenter.getCourseList();
    }

    @Override
    protected void setListener() {

    }

    @Override
    public void showCourseList(final List<CourseGuide> data) {
        mHandler.post(new Runnable() { //执行在主线程中
            @Override
            public void run() {
                LinearLayoutManager manager = new LinearLayoutManager(getApplicationContext());
                manager.setOrientation(LinearLayoutManager.VERTICAL);
                courseRecyclerView.setLayoutManager(manager);
                ShowCourseAdapter adapter = new ShowCourseAdapter(data);
                courseRecyclerView.setAdapter(adapter);
            }
        });
    }

    @Override
    public void showErrorMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
