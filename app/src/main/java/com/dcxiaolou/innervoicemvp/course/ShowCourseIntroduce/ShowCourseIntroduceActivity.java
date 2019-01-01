package com.dcxiaolou.innervoicemvp.course.ShowCourseIntroduce;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;

import com.bumptech.glide.Glide;
import com.dcxiaolou.innervoicemvp.R;
import com.dcxiaolou.innervoicemvp.adapter.FragmentAdapter;
import com.dcxiaolou.innervoicemvp.base.BaseActivity;
import com.dcxiaolou.innervoicemvp.utils.SharedPreferencesUtils;

import java.util.ArrayList;
import java.util.List;

public class ShowCourseIntroduceActivity extends BaseActivity implements View.OnClickListener, ViewPager.OnPageChangeListener {

    public static final String COURSE_ID = "course_id";
    public static final String COVER_PATH = "cover_path";
    public static final String COURSE_TITLE = "cover_title";

    private ImageView coverImageView;

    private RadioButton detailRb, catalogRb, commonRb;

    private ViewPager viewPager;

    private String courseId, coverPath, courseTitle;

    @Override
    protected int setLayout() {
        return R.layout.activity_show_course_introduce;
    }

    @Override
    protected void init() {

        // 获取courseId和coverPath，并将其用SharePreferences保存到文件中
        courseId = getIntent().getStringExtra(COURSE_ID);
        coverPath = getIntent().getStringExtra(COVER_PATH);
        courseTitle = getIntent().getStringExtra(COURSE_TITLE);
        SharedPreferencesUtils.saveString("courseId", courseId);
        SharedPreferencesUtils.saveString("courseCover", coverPath);
        SharedPreferencesUtils.saveString("courseTitle", courseTitle);

        coverImageView = (ImageView) findViewById(R.id.cover_image);

        detailRb = (RadioButton) findViewById(R.id.rb_detail);
        catalogRb = (RadioButton) findViewById(R.id.rb_catalog);
        commonRb = (RadioButton) findViewById(R.id.rb_common);

        viewPager = (ViewPager) findViewById(R.id.view_pager);

        // 使用glide加载课程封面
        Glide.with(this).load(coverPath).into(coverImageView);

        // 给ViewGroup设置适配器
        List<Fragment> fragments = new ArrayList<>();
        fragments.add(new CourseDetailFragment());
        fragments.add(new CourseCatalogFragment());
        fragments.add(new CourseCommonFragment());
        FragmentPagerAdapter adapter = new FragmentAdapter(getSupportFragmentManager(), fragments);
        viewPager.setAdapter(adapter);

    }

    @Override
    protected void setListener() {
        detailRb.setOnClickListener(this);
        catalogRb.setOnClickListener(this);
        commonRb.setOnClickListener(this);

        viewPager.setOnPageChangeListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rb_detail:
                viewPager.setCurrentItem(0);
                break;
            case R.id.rb_catalog:
                viewPager.setCurrentItem(1);
                break;
            case R.id.rb_common:
                viewPager.setCurrentItem(2);
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
                detailRb.setChecked(true);
                break;
            case 1:
                catalogRb.setChecked(true);
                break;
            case 2:
                commonRb.setChecked(true);
                break;
            default :
                break;
        }
    }

    @Override
    public void onPageScrollStateChanged(int i) {

    }
}
