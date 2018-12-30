package com.dcxiaolou.innervoicemvp.guide;
/**
 * 管理新手引导模块中的视图界面
 */
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dcxiaolou.innervoicemvp.R;
import com.dcxiaolou.innervoicemvp.base.BaseActivity;
import com.dcxiaolou.innervoicemvp.home.HomeActivity;

import java.util.ArrayList;

public class GuideActivity extends BaseActivity implements GuideContract.View, View.OnClickListener {

    private GuideContract.Presenter mPresenter;
    private TextView TvStartUse;
    private ViewPager vp;
    private ArrayList<View> mViews;

    private TextView tvGuide1, tvGuide2, tvGuide3, tvGuide4, tvPrevious, tvNext, tvIgnore;

    @Override
    protected int setLayout() {
        return R.layout.activity_guide;
    }

    @Override
    protected void init() {

        mPresenter = new GuidePresenter(this);
        vp=findViewById(R.id.vp);
        mViews=new ArrayList<>();
        View guide01=LayoutInflater.from(getApplicationContext()).inflate(R.layout.guide01,vp,false);
        View guide02=LayoutInflater.from(getApplicationContext()).inflate(R.layout.guide02,vp,false);
        View guide03=LayoutInflater.from(getApplicationContext()).inflate(R.layout.guide03,vp,false);
        View guide04=LayoutInflater.from(getApplicationContext()).inflate(R.layout.guide04,vp,false);
        TvStartUse=guide04.findViewById(R.id.tv_start_use);
        mViews.add(guide01);
        mViews.add(guide02);
        mViews.add(guide03);
        mViews.add(guide04);
        vp.setAdapter(new GuideAdapter());

        tvGuide1 = findViewById(R.id.tv_guide_1);
        tvGuide2 = findViewById(R.id.tv_guide_2);
        tvGuide3 = findViewById(R.id.tv_guide_3);
        tvGuide4 = findViewById(R.id.tv_guide_4);
        tvPrevious = findViewById(R.id.tv_previous);
        tvNext = findViewById(R.id.tv_next);

        tvIgnore = findViewById(R.id.tv_ignore);

        tvPrevious.setVisibility(View.GONE);

    }

    @Override
    protected void setListener() {

        TvStartUse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.saveFirstRunState();
                finish();
            }
        });

        tvPrevious.setOnClickListener(this);
        tvNext.setOnClickListener(this);

        tvIgnore.setOnClickListener(this);

        vp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                switch (i) {
                    case 0:
                        tvPrevious.setVisibility(View.GONE);
                        tvGuide1.setBackgroundResource(R.drawable.shape_small_green_circle);
                        tvGuide2.setBackgroundResource(R.drawable.shape_small_dark_gray_circle);
                        break;
                    case 1:
                        tvPrevious.setVisibility(View.VISIBLE);
                        tvGuide1.setBackgroundResource(R.drawable.shape_small_dark_gray_circle);
                        tvGuide2.setBackgroundResource(R.drawable.shape_small_green_circle);
                        tvGuide3.setBackgroundResource(R.drawable.shape_small_dark_gray_circle);
                        break;
                    case 2:
                        tvNext.setVisibility(View.VISIBLE);
                        tvGuide2.setBackgroundResource(R.drawable.shape_small_dark_gray_circle);
                        tvGuide3.setBackgroundResource(R.drawable.shape_small_green_circle);
                        tvGuide4.setBackgroundResource(R.drawable.shape_small_dark_gray_circle);
                        break;
                    case 3:
                        tvNext.setVisibility(View.GONE);
                        tvGuide3.setBackgroundResource(R.drawable.shape_small_dark_gray_circle);
                        tvGuide4.setBackgroundResource(R.drawable.shape_small_green_circle);
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });

    }

    @Override
    public void toHomeView() {
        toNewView(HomeActivity.class);
    }

    @Override
    public void onClick(View v) {
        int currentPage = vp.getCurrentItem();
        switch (v.getId()) {
            case R.id.tv_previous:
                if (currentPage > 0)
                    vp.setCurrentItem(currentPage - 1);
                break;
            case R.id.tv_next:
                if (currentPage < 3)
                    vp.setCurrentItem(currentPage + 1);
                break;
            case R.id.tv_ignore:
                mPresenter.saveFirstRunState();
                finish();
                break;
            default:
                break;
        }
    }

    private class GuideAdapter extends PagerAdapter {
        @Override
        public int getCount() {
            return mViews.size();
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
            return view == o;
        }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {
            container.addView(mViews.get(position));
            return mViews.get(position);
        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            container.removeView(mViews.get(position));
        }
    }

}
