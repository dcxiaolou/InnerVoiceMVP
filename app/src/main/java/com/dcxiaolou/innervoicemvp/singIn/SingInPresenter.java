package com.dcxiaolou.innervoicemvp.singIn;

import com.dcxiaolou.innervoicemvp.data.DataCallBack;
import com.dcxiaolou.innervoicemvp.data.DataStore;
import com.dcxiaolou.innervoicemvp.data.entity.User;

import cn.bmob.v3.datatype.BmobFile;

public class SingInPresenter implements SingInContract.Presenter {

    private SingInContract.View rootView;

    private DataStore mDataStore;

    public SingInPresenter(SingInContract.View rootView) {
        this.rootView = rootView;
        this.mDataStore = DataStore.getINSTANCE();
    }

    @Override
    public void singIn(String phone, String nickName, String password, BmobFile avatar) {
        User user = new User();
        user.setMobilePhoneNumber(phone);
        user.setUsername(nickName);
        user.setPassword(password);
        user.setAvatar(avatar);
        mDataStore.userSingUp(user, new DataCallBack<User>() {
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
