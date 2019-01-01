package com.dcxiaolou.innervoicemvp.course.ShowCourseIntroduce;

import com.dcxiaolou.innervoicemvp.data.entity.CourseIntroduceCommon;

import java.util.List;

public interface CourseCommonContract {

    interface View {
        /*
        * 显示课程评论信息
        * */
        void showCourseCommon(List<CourseIntroduceCommon> data);
        /*
        * 显示错误信息
        * */
        void showErrorMessage(String message);
    }

    interface Presenter {
        /*
        * 获取课程评论信息
        * */
        void getCourseCommon(String courseId);
    }

}
