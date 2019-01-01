package com.dcxiaolou.innervoicemvp.questionAndAnswer;

import com.dcxiaolou.innervoicemvp.data.DataCallBack;
import com.dcxiaolou.innervoicemvp.data.DataStore;
import com.dcxiaolou.innervoicemvp.mode.QuestionResult;

import java.util.List;

public class ShowQuestionPresenter implements ShowQuestionContract.Presenter {

    private ShowQuestionContract.View rootView;

    private DataStore mDataStore;

    public ShowQuestionPresenter(ShowQuestionContract.View rootView) {
        this.rootView = rootView;
        mDataStore = DataStore.getINSTANCE();
    }

    @Override
    public void getQuestion(String limitNum, String skipNum) {
        mDataStore.getQuestion(new DataCallBack<List<QuestionResult>>() {
            @Override
            public void onSuccess(List<QuestionResult> data) {
                rootView.showQuestion(data);
            }

            @Override
            public void onFail(String message) {
                rootView.showErrorMessage(message);
            }
        }, limitNum, skipNum);
    }
}
