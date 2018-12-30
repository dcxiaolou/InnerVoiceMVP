package com.dcxiaolou.innervoicemvp.guide;

public interface GuideContract {

    interface View {
        /**
         * 跳转登入界面
         */
        void toHomeView();
    }

    interface Presenter {

        /**
         * 保存第一次启动的状态
         */
        void saveFirstRunState();

    }

}
