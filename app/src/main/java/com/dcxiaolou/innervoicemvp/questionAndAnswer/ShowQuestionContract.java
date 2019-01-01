package com.dcxiaolou.innervoicemvp.questionAndAnswer;

import com.dcxiaolou.innervoicemvp.mode.QuestionResult;

import java.util.List;

public interface ShowQuestionContract {

    interface View {

        /*
        * 问答模块数据显示
        * */
        void showQuestion(List<QuestionResult> data);

        /*
        * 错误信息显示
        * */
        void showErrorMessage(String message);

    }

    interface Presenter {

        /*
        * 问答模块数据获取
        * */
        void getQuestion(String limitNum, String skipNum);

    }

}
