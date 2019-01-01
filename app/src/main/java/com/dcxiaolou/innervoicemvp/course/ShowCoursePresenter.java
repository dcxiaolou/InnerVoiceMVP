package com.dcxiaolou.innervoicemvp.course;

import com.dcxiaolou.innervoicemvp.data.DataCallBack;
import com.dcxiaolou.innervoicemvp.data.DataStore;
import com.dcxiaolou.innervoicemvp.data.entity.CourseGuide;

import java.util.List;

public class ShowCoursePresenter implements ShowCourseContract.Presenter {

    private ShowCourseContract.View rootView;

    private DataStore mDataStore;

    public ShowCoursePresenter(ShowCourseContract.View rootView) {
        this.rootView = rootView;
        this.mDataStore = DataStore.getINSTANCE();
    }

    @Override
    public void getCourseList() {
        mDataStore.getCourseGuides(new DataCallBack<List<CourseGuide>>() {
            @Override
            public void onSuccess(List<CourseGuide> data) {
                rootView.showCourseList(data);
            }

            @Override
            public void onFail(String message) {
                rootView.showErrorMessage(message);
            }
        });
    }
}
