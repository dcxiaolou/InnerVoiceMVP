package com.dcxiaolou.innervoicemvp.center;

import com.dcxiaolou.innervoicemvp.data.DataStore;
import com.dcxiaolou.innervoicemvp.data.entity.User;

public class CenterPresenter implements CenterContract.Presenter {

    private CenterContract.View rootView;

    private DataStore mDataStore;

    public CenterPresenter(CenterContract.View rootView) {
        this.rootView = rootView;
        this.mDataStore = DataStore.getINSTANCE();
    }

    @Override
    public boolean isLogin() {
        if (mDataStore.getCurrentUser() == null)
            return false;
        else
            return true;
    }

    @Override
    public User getCurrentUser() {
        return mDataStore.getCurrentUser();
    }
}
