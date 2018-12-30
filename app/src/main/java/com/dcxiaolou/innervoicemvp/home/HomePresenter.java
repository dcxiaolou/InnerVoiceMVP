package com.dcxiaolou.innervoicemvp.home;

import com.dcxiaolou.innervoicemvp.data.DataCallBack;
import com.dcxiaolou.innervoicemvp.data.DataStore;
import com.dcxiaolou.innervoicemvp.data.entity.CourseGuide;
import com.dcxiaolou.innervoicemvp.mode.ReadArticleResult;

import java.util.List;

public class HomePresenter implements HomeContract.Presenter {

    private HomeContract.View rootView;
    private DataStore mDataStore;

    public HomePresenter(HomeContract.View view) {
        this.rootView = view;
        mDataStore = DataStore.getINSTANCE();
    }

    @Override
    public void getADBanner() {
        mDataStore.getADBanner(new DataCallBack<List<String>>() {
            @Override
            public void onSuccess(List<String> data) {
                rootView.showADBanner(data);
            }

            @Override
            public void onFail(String message) {
                rootView.showErrorMessage(message);
            }
        });
    }

    @Override
    public void getCourseGuides() {

        mDataStore.getCourseGuides(new DataCallBack<List<CourseGuide>>() {
            @Override
            public void onSuccess(List<CourseGuide> data) {
                rootView.showCourseGuide(data);
            }

            @Override
            public void onFail(String message) {
                rootView.showErrorMessage(message);
            }
        });

    }

    @Override
    public void getDailyBest() {
        mDataStore.getReadArticle(new DataCallBack<List<ReadArticleResult>>() {
            @Override
            public void onSuccess(List<ReadArticleResult> data) {
                rootView.showDailyBest(data);
            }

            @Override
            public void onFail(String message) {
                rootView.showErrorMessage(message);
            }
        });
    }
}
