package com.dcxiaolou.innervoicemvp.Read;

import android.content.Context;
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
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

/*
* 阅读模块详情页的心理健康碎片 823
* */

public class ShowReadArticleFragment7 extends Fragment implements ReadArticleFragmentContract.View {

    //bmob数据查询分页
    private int skipNum = 0;
    //标记是否还有数据可以加载
    private boolean haveMoreData = false;
    //当页面可见时，初始化界面
    private boolean init = false;
    //是否允许加载数据
    private boolean canLoadData = false;

    private ReadArticleFragmentContract.Presenter mPresenter;

    private View rootView = null;
    private Handler mHandler = new Handler();

    private List<ReadArticleResult> readArticleResults = new ArrayList<>();

    private SmartRefreshLayout smartRefreshLayout;

    private RecyclerView showArticleItemRv;

    private ReadArticleAdapter adapter;

    /*
     *解决ViewPager + fragment 页面切换后数据丢失，此处是让数据在相应的页面可见时加载数据
     * 注意：setUserVisibleHint方法先与onActivityCreate方法执行
     */
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        Log.d("TAG", "isVisibleToUser 7: " + isVisibleToUser);
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
        Log.d("TAG", "onCreateView7");
        Log.d("TAG", "init7 = " + init);
        if (rootView == null) {
            canLoadData = true;
            //当页面为空时才去加载界面，防止加载好的有数据的界面被空的新界面覆盖
            rootView = (View) inflater.inflate(R.layout.fragment_show_article_7, container, false);
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

        showArticleItemRv = (RecyclerView) rootView.findViewById(R.id.show_article_item_rv_7);
        smartRefreshLayout = (SmartRefreshLayout) rootView.findViewById(R.id.fragment_show_article_7_smart_refresh_layout);
        adapter = new ReadArticleAdapter(readArticleResults);

        smartRefreshLayout.autoRefresh(); //首次加载自动刷新
        smartRefreshLayout.finishRefresh(); //结束刷新

        //下拉刷新
        smartRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshLayout) {
                if (haveMoreData) {
                    readArticleResults.clear();
                    //从bmob获取ArticleResult
                    mPresenter.getReadArticle(Constants.READ_ARTICLE_TYPE_7, "" + skipNum);
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
                    mPresenter.getReadArticle(Constants.READ_ARTICLE_TYPE_7, "" + skipNum);
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
