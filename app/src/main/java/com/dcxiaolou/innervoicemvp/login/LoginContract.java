package com.dcxiaolou.innervoicemvp.login;

public interface LoginContract {

    interface View {

        /*
         * 登录成功后，跳转到原始界面
         * */
        void toOriginView();

        /*
         * 登录失败后，显示错误信息
         * */
        void showErrorMessage(String message);

    }

    interface Presenter {

        /*
         * 用户登录
         * */
        void login(String nickName, String password);

    }

}
