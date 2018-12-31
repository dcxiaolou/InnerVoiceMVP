package com.dcxiaolou.innervoicemvp.Read;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;

import com.dcxiaolou.innervoicemvp.R;
import com.dcxiaolou.innervoicemvp.base.BaseActivity;

import java.util.ArrayList;
import java.util.List;

public class ShowReadArticleActivity extends BaseActivity {

    private String titles[] = {"心理科普", "婚恋情感", "家庭关系", "人际社交", "自我察觉", "成长学习", "心理健康", "职场技能"};

    private TabLayout menuTabLayout;
    private ViewPager viewPager;
    private List<String> strings = new ArrayList<String>();
    private List<Fragment> fragments = new ArrayList<Fragment>();

    @Override
    protected int setLayout() {
        return R.layout.activity_show_read_article;
    }

    @Override
    protected void init() {
        menuTabLayout = (TabLayout) findViewById(R.id.menu_tab_layout);
        viewPager = (ViewPager) findViewById(R.id.view_pager);
        for (String str : titles)
            strings.add(str);
        fragments.add(new ShowReadArticleFragment1());
        fragments.add(new ShowReadArticleFragment2());
        fragments.add(new ShowReadArticleFragment3());
        fragments.add(new ShowReadArticleFragment4());
        fragments.add(new ShowReadArticleFragment5());
        fragments.add(new ShowReadArticleFragment6());
        fragments.add(new ShowReadArticleFragment7());
        fragments.add(new ShowReadArticleFragment8());
        viewPager.setAdapter(new TabLayoutMenuAdapter(fragments, strings,
                getSupportFragmentManager(), this));
        menuTabLayout.setupWithViewPager(viewPager);
    }

    @Override
    protected void setListener() {

    }
}
