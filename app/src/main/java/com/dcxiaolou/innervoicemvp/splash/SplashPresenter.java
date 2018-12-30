package com.dcxiaolou.innervoicemvp.splash;

import com.dcxiaolou.innervoicemvp.data.DataStore;
import com.dcxiaolou.innervoicemvp.utils.Constants;
import com.dcxiaolou.innervoicemvp.utils.SharedPreferencesUtils;

/**
 * 中间件，完成SplashActivity与数据层之间的交互
 */
public class SplashPresenter implements SplashContract.Presenter {

    private SplashContract.View rootView;
    private DataStore mDataStore;

    public SplashPresenter(SplashContract.View rootView) {

        this.rootView = rootView;
        this.mDataStore = DataStore.getINSTANCE();

    }

    @Override
    public void isFirstRun() {

        //是否第一次安装运行
        if (SharedPreferencesUtils.getBoolean(Constants.SP_KEY_FIRST_RUN)) {
            //是，跳转到主页面
            rootView.toHomeView();
        } else {
            //否，跳转到引导界面
            rootView.toGuideView();
        }

    }

}
