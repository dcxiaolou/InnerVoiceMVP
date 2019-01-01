package com.dcxiaolou.innervoicemvp.course.ShowCourseIntroduce;

public interface CourseDetailContract {

    interface View {
        /*
        * 显示课程简介信息
        * */
        void showCourseIntroduce(String content);

        /*
        * 显示错误信息
        * */
        void showErrorMessage(String message);
    }

    interface Presenter {
        /*
        * 获取课程简介信息
        * */
        void getCourseIntroduce(String courseId);
    }

}
