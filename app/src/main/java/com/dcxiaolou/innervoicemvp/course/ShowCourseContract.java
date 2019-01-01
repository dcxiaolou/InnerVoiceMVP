package com.dcxiaolou.innervoicemvp.course;

import com.dcxiaolou.innervoicemvp.data.entity.CourseGuide;

import java.util.List;

public interface ShowCourseContract {

    interface View {

        /*
        * 显示课程列表
        * */
        void showCourseList(List<CourseGuide> data);

        /*
        * 显示错误信息
        * */
        void showErrorMessage(String message);

    }

    interface  Presenter {

        /*
        * 获取课程列表
        * */
        void getCourseList();
    }

}
