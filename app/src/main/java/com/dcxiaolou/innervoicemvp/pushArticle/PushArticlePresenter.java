package com.dcxiaolou.innervoicemvp.pushArticle;

import com.dcxiaolou.innervoicemvp.data.DataStore;
import com.dcxiaolou.innervoicemvp.utils.ImageUtil;

import java.io.File;

public class PushArticlePresenter implements PushArticleContract.Presenter {

    private PushArticleContract.View rootView;

    private DataStore mDataStore;

    public PushArticlePresenter(PushArticleContract.View rootView) {
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
