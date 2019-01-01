package com.dcxiaolou.innervoicemvp.questionAndAnswer;

import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.RadioButton;

import com.dcxiaolou.innervoicemvp.R;
import com.dcxiaolou.innervoicemvp.adapter.FragmentAdapter;
import com.dcxiaolou.innervoicemvp.base.BaseActivity;

import java.util.ArrayList;
import java.util.List;
/*
 * 问答功能详情页问题项展示
 * */
public class ShowQuestionActivity extends BaseActivity implements View.OnClickListener, ViewPager.OnPageChangeListener {

    private RadioButton questionAllRb, questionLatestRb, questionHotRb;

    private ViewPager questionViewPager;
    private List<Fragment> fragments;

    @Override
    protected int setLayout() {
        return R.layout.activity_show_question;
    }

    @Override
    protected void init() {
        questionAllRb = (RadioButton) findViewById(R.id.rb_all);
        questionLatestRb = (RadioButton) findViewById(R.id.rb_latest);
        questionHotRb = (RadioButton) findViewById(R.id.rb_hot);

        questionViewPager = (ViewPager) findViewById(R.id.question_view_pager);

        fragments = new ArrayList<>();
        fragments.add(new ShowQuestionAllFragment());
        fragments.add(new ShowQuestionLatestFragment());
        fragments.add(new ShowQuestionHotFragment());
        FragmentAdapter adapter = new FragmentAdapter(getSupportFragmentManager(), fragments);
        questionViewPager.setAdapter(adapter);
    }

    @Override
    protected void setListener() {
        questionAllRb.setOnClickListener(this);
        questionLatestRb.setOnClickListener(this);
        questionHotRb.setOnClickListener(this);

        questionViewPager.addOnPageChangeListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rb_all:
                questionViewPager.setCurrentItem(0);
                break;
            case R.id.rb_latest:
                questionViewPager.setCurrentItem(1);
                break;
            case R.id.rb_hot:
                questionViewPager.setCurrentItem(2);
                break;
            default:
                break;
        }
    }

    @Override
    public void onPageScrolled(int i, float v, int i1) {

    }

    @Override
    public void onPageSelected(int i) {
        switch (i) {
            case 0:
                questionAllRb.setChecked(true);
                break;
            case 1:
                questionLatestRb.setChecked(true);
                break;
            case 2:
                questionHotRb.setChecked(true);
                break;
            default:
                break;
        }
    }

    @Override
    public void onPageScrollStateChanged(int i) {

    }
}
