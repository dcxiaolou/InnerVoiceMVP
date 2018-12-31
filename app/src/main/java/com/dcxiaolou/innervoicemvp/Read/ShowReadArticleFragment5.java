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
import java.util.ArrayList;
import java.util.List;

/*
* 阅读模块详情页的自我察觉碎片 2199
* */

public class ShowReadArticleFragment5 extends Fragment implements ReadArticleFragmentContract.View {

    private ReadArticleFragmentContract.Presenter mPresenter;

    private Handler mHandler = new Handler();

    private SmartRefreshLayout smartRefreshLayout;

    private RecyclerView showArticleItemRv;

    private ReadArticleAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_show_article_5, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mPresenter = new ReadArticleFragmentPresenter(this);
        mPresenter.getReadArticle(Constants.READ_ARTICLE_TYPE_5, "" + 0);

        showArticleItemRv = (RecyclerView) view.findViewById(R.id.show_article_item_rv_5);
        smartRefreshLayout = (SmartRefreshLayout) view.findViewById(R.id.fragment_show_article_5_smart_refresh_layout);

    }

    @Override
    public void showReadArticle(final List<ReadArticleResult> data) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                Log.d("TAG", "fragment5 readArticleResults.size() = " + data.size());
                LinearLayoutManager manager = new LinearLayoutManager(getContext());
                showArticleItemRv.setLayoutManager(manager);
                adapter = new ReadArticleAdapter(data);
                showArticleItemRv.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public void showErrorMessage(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }

}