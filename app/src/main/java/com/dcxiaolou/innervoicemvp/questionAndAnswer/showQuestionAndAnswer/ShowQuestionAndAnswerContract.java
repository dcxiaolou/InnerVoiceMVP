package com.dcxiaolou.innervoicemvp.questionAndAnswer.showQuestionAndAnswer;

import com.dcxiaolou.innervoicemvp.mode.AnswerResult;

import java.util.List;

public interface ShowQuestionAndAnswerContract {

    interface View {

        /*
        * 显示回答
        * */
        void showAnswer(List<AnswerResult> data);

        /*
        * 显示错误信息
        * */
        void showErrorMessage(String message);

    }

    interface Presenter {

        void getAnswer(String type);

    }

}
