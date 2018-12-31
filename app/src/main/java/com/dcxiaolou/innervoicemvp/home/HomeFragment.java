package com.dcxiaolou.innervoicemvp.home;
/*
 * 主页碎片
 * */
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.dcxiaolou.innervoicemvp.R;
import com.dcxiaolou.innervoicemvp.data.entity.CourseGuide;
import com.dcxiaolou.innervoicemvp.mode.ReadArticleResult;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import com.youth.banner.listener.OnBannerListener;
import com.youth.banner.loader.ImageLoader;

import java.util.List;

public class HomeFragment extends Fragment implements HomeContract.View, OnBannerListener {

    private final static String TAG = "HomeFragment";

    private Handler mHandler = new Handler();

    private HomeContract.Presenter mPresenter;

    //ADBanner
    private Banner mAdBanner;
    //课程推荐
    private RecyclerView recyclerView;
    //每日精选
    private RecyclerView dailyBestRv;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mPresenter = new HomePresenter(this);

        mAdBanner = (Banner) view.findViewById(R.id.adbanner);
        mPresenter.getADBanner();

        recyclerView = (RecyclerView) view.findViewById(R.id.course_recommend_rv);
        mPresenter.getCourseGuides();

        dailyBestRv = (RecyclerView) view.findViewById(R.id.daily_best);
        mPresenter.getDailyBest();

    }

    @Override
    public void showADBanner(List<String> bannerPathList) {
        mAdBanner.setImageLoader(new GlideImageLoader()); // 设置图片加载器
        mAdBanner.setImages(bannerPathList); // 设置图片集合
        mAdBanner.setBannerAnimation(Transformer.Stack); // 设置banner动画效果
        mAdBanner.isAutoPlay(true); // 设置自动轮播
        mAdBanner.setDelayTime(2000); // 设置轮播时间
        mAdBanner.setIndicatorGravity(BannerConfig.CENTER); // 设置指示器位置（当banner模式中有指示器时）
        mAdBanner.start(); // banner设置方法全部调用完时最后使用
    }

    @Override
    public void showCourseGuide(final List<CourseGuide> courseGuides) {

        mHandler.post(new Runnable() {
            @Override
            public void run() {
                GridLayoutManager manager = new GridLayoutManager(getContext(), 1);
                manager.setOrientation(LinearLayoutManager.HORIZONTAL);
                recyclerView.setLayoutManager(manager);
                CourseIntroduceAdapter courseIntroduceAdapter = new CourseIntroduceAdapter(courseGuides);
                recyclerView.setAdapter(courseIntroduceAdapter);
            }
        });

    }

    @Override
    public void showDailyBest(final List<ReadArticleResult> readArticleResults) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                //采用NestedScrollView + RecyclerView （NestedScrollView代替ScrollView可解决滑动冲突问题）
                //解决触摸到RecyclerView的时候滑动有些粘连的感觉
                dailyBestRv.setNestedScrollingEnabled(false);
                LinearLayoutManager manager = new LinearLayoutManager(getContext());
                manager.setOrientation(LinearLayoutManager.VERTICAL);
                dailyBestRv.setLayoutManager(manager);
                ReadArticleAdapter readArticleAdapter = new ReadArticleAdapter(readArticleResults);
                dailyBestRv.setAdapter(readArticleAdapter);
            }
        });
    }

    @Override
    public void showErrorMessage(String message) {

        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();

    }

    /*
    * banner点击事件
    * */
    @Override
    public void OnBannerClick(int position) {

    }

    //重写banner图片加载器
    public class GlideImageLoader extends ImageLoader {

        @Override
        public void displayImage(Context context, Object path, ImageView imageView) {
            Glide.with(context).load(path).into(imageView);
        }
    }

}
