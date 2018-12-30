package com.dcxiaolou.innervoicemvp.splash;
/**
 * 契约类
 * 用于限定View和Presenter的行为
 */
public interface SplashContract {
    /**
     * 用于限定View的行为
     */
    interface View {
        /**
         * 跳转到新手引导界面
         */
        void toGuideView();
        /**
         * 跳转到主界面
         */
        void toHomeView();
    }
    /**
     * 用于限定Presenter的行为
     */
    interface Presenter {
        /**
         * 判断是否是第一次启动App
         */
        void isFirstRun();
    }

}
