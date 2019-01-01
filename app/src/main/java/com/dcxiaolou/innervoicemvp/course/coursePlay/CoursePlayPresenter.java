package com.dcxiaolou.innervoicemvp.course.coursePlay;

import com.dcxiaolou.innervoicemvp.data.DataCallBack;
import com.dcxiaolou.innervoicemvp.data.DataStore;

public class CoursePlayPresenter implements CoursePlayContract.Presenter {

    private CoursePlayContract.View rootView;

    private DataStore mDataStore;

    public CoursePlayPresenter(CoursePlayContract.View rootView) {
        this.rootView = rootView;
        this.mDataStore = DataStore.getINSTANCE();
    }

    @Override
    public void getCoursePlay(String courseFileName) {
        mDataStore.getCourseForPlay(new DataCallBack<String>() {
            @Override
            public void onSuccess(String data) {
                rootView.showCoursePlay(data);
            }

            @Override
            public void onFail(String message) {
                rootView.showErrorMessage(message);
            }
        }, courseFileName);
    }
}
