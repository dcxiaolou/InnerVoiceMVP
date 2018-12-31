package com.dcxiaolou.innervoicemvp.home;
/*
 * 主页碎片
 * */
import android.content.Context;
import android.content.Intent;
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
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.dcxiaolou.innervoicemvp.R;
import com.dcxiaolou.innervoicemvp.Read.ShowReadArticleActivity;
import com.dcxiaolou.innervoicemvp.consult.ConsultActivity;
import com.dcxiaolou.innervoicemvp.course.ShowCourseActivity;
import com.dcxiaolou.innervoicemvp.data.entity.CourseGuide;
import com.dcxiaolou.innervoicemvp.data.entity.ReadArticle;
import com.dcxiaolou.innervoicemvp.fm.ShowFMActivity;
import com.dcxiaolou.innervoicemvp.mode.ReadArticleResult;
import com.dcxiaolou.innervoicemvp.questionAndAnswer.ShowQuestionActivity;
import com.dcxiaolou.innervoicemvp.test.TestActivity;
import com.dcxiaolou.innervoicemvp.utils.Constants;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import com.youth.banner.listener.OnBannerListener;
import com.youth.banner.loader.ImageLoader;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment implements HomeContract.View, OnBannerListener, View.OnClickListener {

    private final static String TAG = "HomeFragment";

    private Handler mHandler = new Handler();

    private HomeContract.Presenter mPresenter;

    //ADBanner
    private Banner mAdBanner;
    //课程推荐
    private RecyclerView recyclerView;
    //每日精选
    private RecyclerView dailyBestRv;

    //子菜单项
    private LinearLayout readLayout, courseLayout, fmLayout, questionAndAnswerLayout, consultLayout, testLayout;

    private List<ReadArticleResult> readArticleResults = new ArrayList<>();

    //刷新模块
    private SmartRefreshLayout refreshLayout;

    private boolean isFirstLoad = true;
    private boolean haveMoreData = true;
    private int skipNum = 0;

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

        readLayout = (LinearLayout) view.findViewById(R.id.read_layout);
        courseLayout = (LinearLayout) view.findViewById(R.id.course_layout);
        fmLayout = (LinearLayout) view.findViewById(R.id.fm_layout);
        questionAndAnswerLayout = (LinearLayout) view.findViewById(R.id.question_and_answer_Linear_layout);
        consultLayout = (LinearLayout) view.findViewById(R.id.consult_linear_layout);
        testLayout = (LinearLayout) view.findViewById(R.id.test_linear_layout);

        refreshLayout = (SmartRefreshLayout) view.findViewById(R.id.smart_refresh_layout);

        //给子菜单项添加点击事件
        readLayout.setOnClickListener(this);
        courseLayout.setOnClickListener(this);
        fmLayout.setOnClickListener(this);
        questionAndAnswerLayout.setOnClickListener(this);
        consultLayout.setOnClickListener(this);
        testLayout.setOnClickListener(this);

        //首次加载数据
        if (isFirstLoad) {
            refreshLayout.autoRefresh();//首次加载启动自动刷新
            refreshLayout.finishRefresh();//结束刷新
            isFirstLoad = false;
        }

        //刷新模块添加下拉刷新监听
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshLayout) {
                Log.d("TAG", "setOnRefreshListener haveMoreData = " + haveMoreData);
                if (haveMoreData) {
                    readArticleResults.clear();
                    // 每日精选模块 采用RecyclerView来显示
                    mPresenter.getDailyBest(Constants.DAILY_INTRODUCE_ARTICLE_TYPE, "" + skipNum);
                } else {
                    Toast.makeText(getContext(), "φ(>ω<*) 暂无更新", Toast.LENGTH_SHORT).show();
                }
                refreshLayout.finishRefresh();
            }
        });

        //刷新模块添加上拉加载更多模块
        refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshLayout) {
                Log.d("TAG", "setOnLoadMoreListener haveMoreData = " + haveMoreData);
                if (haveMoreData) {
                    mPresenter.getDailyBest(Constants.DAILY_INTRODUCE_ARTICLE_TYPE, "" + skipNum);
                } else {
                    Toast.makeText(getContext(), "φ(>ω<*) 没有更多文章了", Toast.LENGTH_SHORT).show();
                }
                refreshLayout.finishLoadMore();
            }
        });

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
    public void showDailyBest(List<ReadArticleResult> data) {
        readArticleResults.addAll(data);
        Log.d("TAG", "readArticleResults.size() = " + readArticleResults.size());
        Log.d("TAG", "data.size() = " + data.size());
        if (data.size() < 5)
            haveMoreData = false;
        else
            skipNum += 5;
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
                readArticleAdapter.notifyDataSetChanged();//数据改变
                dailyBestRv.scrollToPosition(readArticleResults.size() - 5 - 1);//从新定位
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.read_layout:
                Intent readIntent = new Intent(getContext(), ShowReadArticleActivity.class);
                startActivity(readIntent);
                break;
            case R.id.course_layout:
                Intent courseIntent = new Intent(getContext(), ShowCourseActivity.class);
                startActivity(courseIntent);
                break;
            case R.id.fm_layout:
                Intent fmIntent = new Intent(getContext(), ShowFMActivity.class);
                startActivity(fmIntent);
                break;
            case R.id.question_and_answer_Linear_layout:
                Intent qaaIntent = new Intent(getContext(), ShowQuestionActivity.class);
                startActivity(qaaIntent);
                break;
            case R.id.consult_linear_layout:
                Intent consultIntent = new Intent(getContext(), ConsultActivity.class);
                startActivity(consultIntent);
                break;
            case R.id.test_linear_layout:
                Intent testIntent = new Intent(getContext(), TestActivity.class);
                startActivity(testIntent);
                break;
            default:
                break;
        }
    }

    //重写banner图片加载器
    public class GlideImageLoader extends ImageLoader {

        @Override
        public void displayImage(Context context, Object path, ImageView imageView) {
            Glide.with(context).load(path).into(imageView);
        }
    }

}
