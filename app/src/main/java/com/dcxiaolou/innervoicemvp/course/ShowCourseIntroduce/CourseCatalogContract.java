package com.dcxiaolou.innervoicemvp.course.ShowCourseIntroduce;

import com.dcxiaolou.innervoicemvp.data.entity.CourseIntroduceCatalog;

import java.util.List;

public interface CourseCatalogContract {

    interface View {
        /*
        * 显示课程目录
        * */
        void showCourseCatalog(List<CourseIntroduceCatalog> data);
        /*
        * 显示错误信息
        * */
        void showErrorMessage(String message);
    }

    interface Presenter {
        /*
        * 获取课程目录
        * */
        void getCourseCatalog(String catalogPath);
    }

}
