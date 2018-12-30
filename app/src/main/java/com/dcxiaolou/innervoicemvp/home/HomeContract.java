package com.dcxiaolou.innervoicemvp.home;

import com.dcxiaolou.innervoicemvp.data.entity.CourseGuide;
import com.dcxiaolou.innervoicemvp.mode.ReadArticleResult;
import com.dcxiaolou.innervoicemvp.pushArticle.PushArticleActivity;

import java.util.List;

public interface HomeContract {

    interface View {

        /*
        * 展示首页ADBanner
        * */
        void showADBanner(List<String> data);

        /*
        * 展示课程推荐
        * */
        void showCourseGuide(List<CourseGuide> courseGuides);

        /*
        * 展示每日推荐
        * */
        void showDailyBest(List<ReadArticleResult> readArticleResults);

        /*
        * 显示错误信息
        * */
        void showErrorMessage(String message);

    }

    interface Presenter {

        /*
        * 获取首页ADBanner
        * */
        void getADBanner();

        /*
        * 获取课程推荐信息
        * */
        void getCourseGuides();

        /*
        * 获取每日推荐信息
        * */
        void getDailyBest();

    }

}
