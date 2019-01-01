package com.dcxiaolou.innervoicemvp.course.ShowCourseIntroduce;

import com.dcxiaolou.innervoicemvp.data.DataCallBack;
import com.dcxiaolou.innervoicemvp.data.DataStore;
import com.dcxiaolou.innervoicemvp.data.entity.CourseIntroduceCommon;

import java.util.List;

public class CourseCommonPresenter implements CourseCommonContract.Presenter {

    private CourseCommonContract.View rootView;

    private DataStore mDataStore;

    public CourseCommonPresenter(CourseCommonContract.View rootView) {
        this.rootView = rootView;
        this.mDataStore = DataStore.getINSTANCE();
    }

    @Override
    public void getCourseCommon(String courseId) {
        mDataStore.getCourseCommon(new DataCallBack<List<CourseIntroduceCommon>>() {
            @Override
            public void onSuccess(List<CourseIntroduceCommon> data) {
                rootView.showCourseCommon(data);
            }

            @Override
            public void onFail(String message) {
                rootView.showErrorMessage(message);
            }
        }, courseId);
    }
}
