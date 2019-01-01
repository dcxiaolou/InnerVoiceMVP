package com.dcxiaolou.innervoicemvp.questionAndAnswer.showQuestionAndAnswer;

import com.dcxiaolou.innervoicemvp.data.DataCallBack;
import com.dcxiaolou.innervoicemvp.data.DataStore;
import com.dcxiaolou.innervoicemvp.mode.AnswerResult;

import java.util.List;

public class ShowQuestionAndAnswerPresenter implements ShowQuestionAndAnswerContract.Presenter {

    private ShowQuestionAndAnswerContract.View rootView;

    private DataStore mDataStore;

    public ShowQuestionAndAnswerPresenter(ShowQuestionAndAnswerContract.View rootView) {
        this.rootView = rootView;
        this.mDataStore = DataStore.getINSTANCE();
    }

    @Override
    public void getAnswer(String type) {
        mDataStore.getAnsewr(new DataCallBack<List<AnswerResult>>() {
            @Override
            public void onSuccess(List<AnswerResult> data) {
                rootView.showAnswer(data);
            }

            @Override
            public void onFail(String message) {
                rootView.showErrorMessage(message);
            }
        }, type);
    }
}
