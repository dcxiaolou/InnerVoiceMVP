package com.dcxiaolou.innervoicemvp.course.ShowCourseIntroduce;

import com.dcxiaolou.innervoicemvp.data.DataCallBack;
import com.dcxiaolou.innervoicemvp.data.DataStore;

public class CourseDetailPresenter implements CourseDetailContract.Presenter {

    private CourseDetailContract.View rootView;

    private DataStore mDataStore;

    public CourseDetailPresenter(CourseDetailContract.View rootView) {
        this.rootView = rootView;
        this.mDataStore = DataStore.getINSTANCE();
    }

    @Override
    public void getCourseIntroduce(String courseId) {
        mDataStore.getCourseIntroduce(new DataCallBack<String>() {
            @Override
            public void onSuccess(String data) {
                rootView.showCourseIntroduce(data);
            }

            @Override
            public void onFail(String message) {
                rootView.showErrorMessage(message);
            }
        }, courseId);
    }
}
