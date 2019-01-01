package com.dcxiaolou.innervoicemvp.course.ShowCourseIntroduce;

import com.dcxiaolou.innervoicemvp.data.DataCallBack;
import com.dcxiaolou.innervoicemvp.data.DataStore;
import com.dcxiaolou.innervoicemvp.data.entity.CourseIntroduceCatalog;

import java.util.List;

public class CourseCatalogPresenter implements CourseCatalogContract.Presenter {

    private CourseCatalogContract.View rootView;

    private DataStore mDataStore;

    public CourseCatalogPresenter(CourseCatalogContract.View rootView) {
        this.rootView = rootView;
        this.mDataStore = DataStore.getINSTANCE();
    }

    @Override
    public void getCourseCatalog(String catalogPath) {
        mDataStore.getCourseCatalog(new DataCallBack<List<CourseIntroduceCatalog>>() {
            @Override
            public void onSuccess(List<CourseIntroduceCatalog> data) {
                rootView.showCourseCatalog(data);
            }

            @Override
            public void onFail(String message) {
                rootView.showErrorMessage(message);
            }
        }, catalogPath);
    }
}
