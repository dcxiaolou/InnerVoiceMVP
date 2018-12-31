package com.dcxiaolou.innervoicemvp.Read;

import com.dcxiaolou.innervoicemvp.mode.ReadArticleResult;

import java.util.List;

public interface ReadArticleFragmentContract {

    interface View {

        /*
        * 展示文章
        * */
        void showReadArticle(List<ReadArticleResult> data);

        /*
        * 显示错误信息
        * */
        void showErrorMessage(String message);

    }

    interface Presenter {

        /*
        * 获取文章
        * */
        void getReadArticle(String type, String skipNum);
    }

}
