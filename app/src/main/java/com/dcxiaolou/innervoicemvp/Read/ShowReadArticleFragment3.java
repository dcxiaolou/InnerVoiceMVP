package com.dcxiaolou.innervoicemvp.Read;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
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
import com.dcxiaolou.innervoicemvp.home.ReadArticleAdapter;
import com.dcxiaolou.innervoicemvp.mode.ReadArticleResult;
import com.dcxiaolou.innervoicemvp.utils.Constants;
import com.dcxiaolou.innervoicemvp.utils.SharedPreferencesUtils;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

/*
* 阅读模块详情页的家庭关系碎片 660
* */

public class ShowReadArticleFragment3 extends Fragment implements ReadArticleFragmentContract.View {

    private View rootView = null;

    private ReadArticleFragmentContract.Presenter mPresenter;

    private Handler mHandler = new Handler();

    private SmartRefreshLayout smartRefreshLayout;

    private RecyclerView showArticleItemRv;

    private ReadArticleAdapter adapter;

    private List<ReadArticleResult> readArticleResults = new ArrayList<>();

    //bmob数据查询分页
    private int skipNum = 0;
    //标记是否还有数据可以加载
    private boolean haveMoreData = false;
    //当页面可见时，初始化界面
    private boolean init = false;
    private boolean canLoadData = false;

    /*
     *解决ViewPager + fragment 页面切换后数据丢失，此处是让数据在相应的页面中从新加载
     * 注意：setUserVisibleHint方法先与onActivityCreate方法执行
     */
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        Log.d("TAG", "isVisibleToUser 3: " + isVisibleToUser);
        if (isVisibleToUser && (canLoadData || rootView == null)) {
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
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d("TAG", "onCreateView3");
        Log.d("TAG", "init3 = " + init);
        if (rootView == null) {
            canLoadData = true;
            rootView = (View) inflater.inflate(R.layout.fragment_show_article_3, container, false);
        }
        if (init) {
            init();
            init = false;
        }
        return rootView;
    }

    private void init() {
        Log.d("TAG", "init: ");

        canLoadData = false;

        mPresenter = new ReadArticleFragmentPresenter(this);

        showArticleItemRv = (RecyclerView) rootView.findViewById(R.id.show_article_item_rv_3);
        smartRefreshLayout = (SmartRefreshLayout) rootView.findViewById(R.id.fragment_show_article_3_smart_refresh_layout);

        smartRefreshLayout.autoRefresh(); //首次加载自动刷新
        smartRefreshLayout.finishRefresh(); //结束刷新

        //下拉刷新
        smartRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshLayout) {
                if (haveMoreData) {
                    readArticleResults.clear();
                    //从bmob获取ArticleResult
                    mPresenter.getReadArticle(Constants.READ_ARTICLE_TYPE_3, "" + skipNum);
                } else {
                    Toast.makeText(getContext(), "φ(>ω<*) 暂无更新", Toast.LENGTH_SHORT).show();
                }
                smartRefreshLayout.finishRefresh();
            }
        });
        //上拉加载更多
        smartRefreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshLayout) {
                if (haveMoreData) {
                    mPresenter.getReadArticle(Constants.READ_ARTICLE_TYPE_3, "" + skipNum);
                } else {
                    Toast.makeText(getContext(), "φ(>ω<*) 没有更多文章了", Toast.LENGTH_SHORT).show();
                }
                smartRefreshLayout.finishLoadMore();
            }
        });
    }

    @Override
    public void showReadArticle(final List<ReadArticleResult> data) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                readArticleResults.addAll(data);
                Log.d("TAG", "readArticleResults.size() = " + readArticleResults.size());
                Log.d("TAG", "data.size() = " + data.size());
                if (data.size() < 5)
                    haveMoreData = false;
                else
                    skipNum += 5;
                LinearLayoutManager manager = new LinearLayoutManager(getContext());
                showArticleItemRv.setLayoutManager(manager);
                adapter = new ReadArticleAdapter(readArticleResults);
                showArticleItemRv.setAdapter(adapter);
                adapter.notifyDataSetChanged();
                showArticleItemRv.scrollToPosition(readArticleResults.size() - 5 - 1);//从新定位
            }
        });
    }

    @Override
    public void showErrorMessage(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }

}