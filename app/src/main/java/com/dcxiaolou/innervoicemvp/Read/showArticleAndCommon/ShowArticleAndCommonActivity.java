package com.dcxiaolou.innervoicemvp.Read.showArticleAndCommon;
/*
 * 阅读模块文章及评论的详细内容展示
 * */

import android.graphics.drawable.Drawable;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dcxiaolou.innervoicemvp.R;
import com.dcxiaolou.innervoicemvp.base.BaseActivity;
import com.dcxiaolou.innervoicemvp.mode.ReadArticleResult;

import org.sufficientlysecure.htmltextview.HtmlHttpImageGetter;
import org.sufficientlysecure.htmltextview.HtmlTextView;

import java.util.ArrayList;
import java.util.List;

public class ShowArticleAndCommonActivity extends BaseActivity {

    public final static String ARTICLE_DETAIL = "article_detail";

    private ReadArticleResult articleResult;

    private TextView titleTv, pushTimeTv, readNumTv, likeNumTv, commonNumTv;

    private String title, pushTime, readNum, likeNum, context;

    private LinearLayout articleTagLayout;
    private List<String> tags = new ArrayList<>();

    private HtmlTextView articleContext;

    @Override
    protected int setLayout() {
        return R.layout.activity_show_article_and_common;
    }

    @Override
    protected void init() {
        titleTv = (TextView) findViewById(R.id.article_title);
        pushTimeTv = (TextView) findViewById(R.id.article_push_time);
        readNumTv = (TextView) findViewById(R.id.article_read_num);
        likeNumTv = (TextView) findViewById(R.id.article_like_num);
        articleTagLayout = (LinearLayout) findViewById(R.id.article_tag);
        articleContext = (HtmlTextView) findViewById(R.id.article_context);

        articleResult = (ReadArticleResult) getIntent().getSerializableExtra(ARTICLE_DETAIL);
        //Log.d(TAG, "title = " + articleResult.getTitle());
        title = articleResult.getTitle();
        tags = articleResult.getTag();
        pushTime = articleResult.getPush_time();
        readNum = articleResult.getCount();
        likeNum = articleResult.getLike().get(0);
        context = articleResult.getArticle_detail().get(0);

        titleTv.setText(title);

        //通过代码动态的向LinearLayout中添加TextView
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(20, 10, 10, 0);
        TextView tagTv;
        Drawable drawable = getResources().getDrawable(R.drawable.article_tag_bg);
        for (String tag : tags) {
            tagTv = new TextView(getApplicationContext());
            tagTv.setText(tag);
            tagTv.setPadding(10, 3, 10, 5);
            tagTv.setBackground(drawable);
            tagTv.setLayoutParams(layoutParams);
            articleTagLayout.addView(tagTv);
        }

        pushTimeTv.setText(pushTime);
        readNumTv.setText(readNum);
        likeNumTv.setText(likeNum);
        articleContext.setHtml(context, new HtmlHttpImageGetter(articleContext));
    }

    @Override
    protected void setListener() {

    }
}
