package com.dcxiaolou.innervoicemvp.login;

import android.util.Log;

import com.dcxiaolou.innervoicemvp.data.DataCallBack;
import com.dcxiaolou.innervoicemvp.data.DataStore;
import com.dcxiaolou.innervoicemvp.data.entity.User;

public class LoginPresenter implements LoginContract.Presenter {

    private LoginContract.View rootView;

    private DataStore mDataStore;

    public LoginPresenter(LoginContract.View rootView) {
        this.rootView = rootView;
        this.mDataStore = DataStore.getINSTANCE();
    }

    @Override
    public void login(String nickName, String password) {
        User user = new User();
        user.setUsername(nickName);
        user.setPassword(password);
        mDataStore.userLogin(user, new DataCallBack<User>() {
            @Override
            public void onSuccess(User data) {
                rootView.toOriginView();
            }

            @Override
            public void onFail(String message) {
                rootView.showErrorMessage(message);
            }
        });
    }
}
