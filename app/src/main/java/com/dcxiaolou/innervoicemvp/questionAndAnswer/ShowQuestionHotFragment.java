package com.dcxiaolou.innervoicemvp.questionAndAnswer;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.dcxiaolou.innervoicemvp.R;
import com.dcxiaolou.innervoicemvp.mode.QuestionResult;
import com.dcxiaolou.innervoicemvp.utils.Constants;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

/*
 * 问答功能界面的碎片，用来显示最热问答
 * */
public class ShowQuestionHotFragment extends Fragment implements ShowQuestionContract.View {

    private ShowQuestionContract.Presenter mPresenter;

    //bmob数据查询分页
    private int skipNum = 0;
    //标记是否还有数据可以加载
    private boolean haveMoreData = false;
    //当页面可见时，初始化界面
    private boolean init = false;
    //是否允许加载数据
    private boolean canLoadData = false;

    private View rootView;
    private Handler mHandler = new Handler();

    private SmartRefreshLayout refreshLayout;

    private RecyclerView recyclerView;

    private List<QuestionResult> questionResults = new ArrayList<>();

    private ShowQuestionAdapter adapter;

    /*
     *解决ViewPager + fragment 页面切换后数据丢失，此处是让数据在相应的页面可见时加载数据
     * 注意：setUserVisibleHint方法先与onActivityCreate方法执行
     */
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        Log.d("TAG", "isVisibleToUser 3: " + isVisibleToUser);
        //防止viewpager预加载下一个页面后（rootView != null），下一个页面无法加载数据
        if (isVisibleToUser && (canLoadData || rootView == null )) {
            skipNum = 0;
            haveMoreData = true;
            if (rootView != null) {
                Log.d("TAG", "rootView: " + rootView);
                init();
            } else {
                init = true;
            }
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d("TAG", "onCreateView3");
        Log.d("TAG", "init3 = " + init);
        if (rootView == null) {
            canLoadData = true;
            //当页面为空时才去加载界面，防止加载好的有数据的界面被空的新界面覆盖
            rootView = (View) inflater.inflate(R.layout.fragment_question_hot, container, false);
        }
        if (init) {
            init();
            init = false;
        }
        return rootView;
    }

    private void init() {

        mPresenter = new ShowQuestionPresenter(this);

        canLoadData = false;

        refreshLayout = (SmartRefreshLayout) rootView.findViewById(R.id.question_hot_smart_refresh_layout);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.question_hot_rv);

        refreshLayout.autoRefresh(); //第一次加载自动下拉刷新一次
        refreshLayout.finishRefresh(); //结束刷新

        //下拉刷新
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshLayout) {
                if (haveMoreData) {
                    questionResults.clear();
                    mPresenter.getQuestion(Constants.QUESTION_HOT_LIMIT_NUM, "" + skipNum);
                } else {
                    Toast.makeText(getContext(), "φ(>ω<*) 暂无更新", Toast.LENGTH_SHORT).show();
                }
                refreshLayout.finishRefresh();
            }
        });
        //上拉加载更多
        refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshLayout) {
                if (haveMoreData) {
                    mPresenter.getQuestion(Constants.QUESTION_HOT_LIMIT_NUM, "" + skipNum);
                } else {
                    Toast.makeText(getContext(), "φ(>ω<*) 没有更多文章了", Toast.LENGTH_SHORT).show();
                }
                refreshLayout.finishLoadMore();
            }
        });
    }

    @Override
    public void showQuestion(final List<QuestionResult> data) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                questionResults.addAll(data);
                Log.d("TAG", "questionResults.size() = " + questionResults.size());
                Log.d("TAG", "data.size() = " + data.size());
                if (data.size() < 10)
                    haveMoreData = false;
                else
                    skipNum += 10;
                List<QuestionResult> latestQuestionResult = new ArrayList<>();
                for (int i = 0; i < questionResults.size(); i += 2)
                    latestQuestionResult.add(questionResults.get(i));
                LinearLayoutManager manager = new LinearLayoutManager(getContext());
                recyclerView.setLayoutManager(manager);
                adapter = new ShowQuestionAdapter(latestQuestionResult);
                recyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
                recyclerView.scrollToPosition(latestQuestionResult.size() - 5 - 1);//从新定位
            }
        });
    }

    @Override
    public void showErrorMessage(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }
}
