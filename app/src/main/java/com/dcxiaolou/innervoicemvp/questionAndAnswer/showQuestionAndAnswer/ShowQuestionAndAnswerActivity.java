package com.dcxiaolou.innervoicemvp.questionAndAnswer.showQuestionAndAnswer;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.dcxiaolou.innervoicemvp.R;
import com.dcxiaolou.innervoicemvp.base.BaseActivity;
import com.dcxiaolou.innervoicemvp.mode.AnswerResult;
import com.dcxiaolou.innervoicemvp.mode.QuestionResult;

import java.util.ArrayList;
import java.util.List;

public class ShowQuestionAndAnswerActivity extends BaseActivity implements ShowQuestionAndAnswerContract.View {

    public final static String QUESTION_POSITION = "question_position";
    public final static String QUESTION = "question";

    private Handler mHandler = new Handler();

    private ImageView questionUserImage;
    private TextView questionTitle, questionPushTime, questionViewNum, questionAnswerNum;
    private LinearLayout questionTag;
    private TextView questionContext;

    private RecyclerView answerRecyclerView;

    private List<AnswerResult> answerResults = new ArrayList<>();

    private ShowQuestionAndAnswerContract.Presenter mPresenter;

    @Override
    protected int setLayout() {
        return R.layout.activity_show_question_and_answer;
    }

    @SuppressLint("ResourceAsColor")
    @Override
    protected void init() {
        questionUserImage = (ImageView) findViewById(R.id.detail_question_user_img);
        questionTitle = (TextView) findViewById(R.id.detail_question_title);
        questionPushTime = (TextView) findViewById(R.id.detail_question_push_time);
        questionViewNum = (TextView) findViewById(R.id.detail_question_view_num);
        questionAnswerNum = (TextView) findViewById(R.id.detail_question_answer_num);
        questionTag = (LinearLayout) findViewById(R.id.detail_question_tag);
        questionContext = (TextView) findViewById(R.id.detail_question_context);

        answerRecyclerView = (RecyclerView) findViewById(R.id.detail_answer_recycler_view);

        Intent intent = getIntent();
        final int position = intent.getIntExtra(QUESTION_POSITION, -1) + 1;
        QuestionResult questionResult = (QuestionResult) intent.getSerializableExtra(QUESTION);
        String id = questionResult.getId();
        //Log.d("TAG", "position = " + position);
        Log.d("TAG", "id: " + id);
        String type = "";
        char ch;
        for (int i = 0; i < id.length(); i++) {
            ch = id.charAt(i);
            if (ch == '.')
                break;
            type += ch;
        }
        Log.d("TAG", "type: " + type);

        mPresenter = new ShowQuestionAndAnswerPresenter(this);
        mPresenter.getAnswer(type);

        RequestOptions options = RequestOptions.circleCropTransform()
                .diskCacheStrategy(DiskCacheStrategy.NONE) //不做磁盘缓存
                .skipMemoryCache(true) //不做内存缓存
                .placeholder(R.drawable.question_user_img); //占位图
        if (questionResult == null) {
            Log.d("TAG", "QuestionResult is null");
        }
        String imgUrl = questionResult.getQuestion_user_img();
        if (imgUrl == null)
            imgUrl = "https://avatar.csdn.net/A/A/A/2_dc_2701.jpg";
        Glide.with(this).load(imgUrl).apply(options).into(questionUserImage);
        questionTitle.setText(questionResult.getTitle());
        List<String> tags = questionResult.getQuestion_tag();
        //通过代码动态的向LinearLayout中添加TextView
        questionTag.removeAllViews(); //防止在RecyclerView中重复加载
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT
        );
        layoutParams.setMargins(10, 10, 10, 0);
        TextView tagTv;
        Drawable drawable = getResources().getDrawable(R.drawable.article_tag_bg);
        int tagSize = tags.size();
        if (tagSize != 0) {
            for (int j = 0; j < tagSize; j++) {
                if (j >= 3) break;
                tagTv = new TextView(this);
                tagTv.setText(tags.get(j));
                tagTv.setPadding(10, 3, 10, 5);
                tagTv.setBackground(drawable);
                tagTv.setLayoutParams(layoutParams);
                tagTv.setTextColor(R.color.default_text_color);
                questionTag.addView(tagTv);
            }
        }
        questionContext.setText(questionResult.getQuestion_content().trim());
        questionPushTime.setText(questionResult.getQuestion_push_time());
        questionViewNum.setText(questionResult.getQuestion_reader_num());
        questionAnswerNum.setText(questionResult.getQuestion_answer_num());

    }

    @Override
    protected void setListener() {

    }

    @Override
    public void showAnswer(final List<AnswerResult> data) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                LinearLayoutManager manager = new LinearLayoutManager(getApplicationContext());
                manager.setOrientation(LinearLayoutManager.VERTICAL);
                answerRecyclerView.setLayoutManager(manager);
                ShowQuestionAndAnswerAdapter adapter = new ShowQuestionAndAnswerAdapter(data);
                answerRecyclerView.setAdapter(adapter);
            }
        });
    }

    @Override
    public void showErrorMessage(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }
}
