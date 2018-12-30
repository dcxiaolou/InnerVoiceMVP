package com.dcxiaolou.innervoicemvp.pushSecret;

import com.dcxiaolou.innervoicemvp.data.DataStore;
import com.dcxiaolou.innervoicemvp.pushArticle.PushArticleContract;
import com.dcxiaolou.innervoicemvp.utils.ImageUtil;

import java.io.File;

public class PushSecretPresenter implements PushSecretContract.Presenter {

    private PushSecretContract.View rootView;

    private DataStore mDataStore;

    public PushSecretPresenter(PushSecretContract.View rootView) {
        this.rootView = rootView;
        this.mDataStore = DataStore.getINSTANCE();
    }

    @Override
    public void fixDirPath() {
        String path = ImageUtil.getDirPath();
        File file = new File(path);
        if (!file.exists()) {
            file.mkdirs();
        }
    }

}
