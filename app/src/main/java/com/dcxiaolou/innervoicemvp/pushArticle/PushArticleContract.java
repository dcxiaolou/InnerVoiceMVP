package com.dcxiaolou.innervoicemvp.pushArticle;

public interface PushArticleContract {

    interface View {

        /*
        * 显示操作选择
        * */
        void showOptions();

    }

    interface Presenter {

        void fixDirPath();

    }

}
