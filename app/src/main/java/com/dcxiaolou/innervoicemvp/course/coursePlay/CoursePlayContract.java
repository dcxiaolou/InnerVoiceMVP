package com.dcxiaolou.innervoicemvp.course.coursePlay;

public interface CoursePlayContract {

    interface View {
        /*
        * 对课程进行播放
        * */
        void showCoursePlay(String data);
        /*
        * 显示错误信息
        * */
        void showErrorMessage(String message);
    }

    interface Presenter {
        /*
        * 获取课程播放信息
        * */
        void getCoursePlay(String courseFileName);
    }

}
