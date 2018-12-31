package com.dcxiaolou.innervoicemvp.Read;

import android.util.Log;

import com.dcxiaolou.innervoicemvp.data.DataCallBack;
import com.dcxiaolou.innervoicemvp.data.DataStore;
import com.dcxiaolou.innervoicemvp.mode.ReadArticleResult;

import java.util.List;

public class ReadArticleFragmentPresenter implements ReadArticleFragmentContract.Presenter {

    private ReadArticleFragmentContract.View rootView;

    private DataStore mDataStore;

    public ReadArticleFragmentPresenter(ReadArticleFragmentContract.View rootView) {
        this.rootView = rootView;
        this.mDataStore = DataStore.getINSTANCE();
    }

    @Override
    public void getReadArticle(String type, String skipNum) {
        mDataStore.getReadArticle(new DataCallBack<List<ReadArticleResult>>() {
            @Override
            public void onSuccess(List<ReadArticleResult> data) {
                rootView.showReadArticle(data);
                Log.d("TAG", "rootView = " + rootView.toString());
                Log.d("TAG", "data.size() = " + data.size());
            }

            @Override
            public void onFail(String message) {
                rootView.showErrorMessage(message);
            }
        }, type, skipNum);
    }
}
