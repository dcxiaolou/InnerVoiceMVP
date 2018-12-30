package com.dcxiaolou.innervoicemvp.singIn;

import cn.bmob.v3.datatype.BmobFile;

public interface SingInContract {

    interface View {

        /*
         * 注册成功后，跳转到原始界面
         * */
        void toOriginView();

        /*
         * 注册失败后，显示错误信息
         * */
        void showErrorMessage(String message);

    }

    interface Presenter {

        /*
         * 用户注册
         * */
        void singIn(String phone, String nickName, String password, BmobFile avatar);

    }

}
