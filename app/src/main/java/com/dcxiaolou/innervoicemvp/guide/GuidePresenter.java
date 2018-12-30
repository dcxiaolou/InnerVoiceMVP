package com.dcxiaolou.innervoicemvp.guide;

import com.dcxiaolou.innervoicemvp.utils.Constants;
import com.dcxiaolou.innervoicemvp.utils.SharedPreferencesUtils;

public class GuidePresenter implements GuideContract.Presenter {

    private GuideContract.View rootView;

    public GuidePresenter(GuideContract.View rootView) {
        this.rootView = rootView;
    }

    @Override
    public void saveFirstRunState() {
        SharedPreferencesUtils.saveBoolean(Constants.SP_KEY_FIRST_RUN, true);
        rootView.toHomeView();
    }
}
