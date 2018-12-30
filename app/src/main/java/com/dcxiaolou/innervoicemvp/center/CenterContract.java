package com.dcxiaolou.innervoicemvp.center;

import com.dcxiaolou.innervoicemvp.data.entity.User;

import cn.bmob.v3.datatype.BmobFile;

public interface CenterContract {

    interface View {

    }

    interface Presenter {

        /*
        * 是否登录
        * */
        boolean isLogin();

        /*
        * 获取当前用户
        * */
        User getCurrentUser();

    }

}
