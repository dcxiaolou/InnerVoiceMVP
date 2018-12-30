package com.dcxiaolou.innervoicemvp.treeHole;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.dcxiaolou.innervoicemvp.R;
import com.dcxiaolou.innervoicemvp.ui.viewpagercards.CardFragmentPagerAdapter;
import com.dcxiaolou.innervoicemvp.ui.viewpagercards.CardItem;
import com.dcxiaolou.innervoicemvp.ui.viewpagercards.CardPagerAdapter;
import com.dcxiaolou.innervoicemvp.ui.viewpagercards.ShadowTransformer;

public class TreeHoleFragment extends Fragment implements TreeHoleContract.View, View.OnClickListener {

    private TreeHoleContract.Presenter mPresenter;

    private FragmentManager manager;

    private ViewPager mViewPager;

    private CardPagerAdapter mCardAdapter;
    private ShadowTransformer mCardShadowTransformer;
    private CardFragmentPagerAdapter mFragmentCardAdapter;
    private ShadowTransformer mFragmentCardShadowTransformer;

    private ImageView previousLeft, nextRight;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_tree_hole, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        manager = getChildFragmentManager(); //解决ViewGroup的Fragment中加入ViewGroup丢失Fragment的问题

        mViewPager = (ViewPager) view.findViewById(R.id.tree_hole_viewPager);

        previousLeft = (ImageView) view.findViewById(R.id.previous_left);
        nextRight = (ImageView) view.findViewById(R.id.next_right);

        previousLeft.setOnClickListener(this);
        nextRight.setOnClickListener(this);

        mCardAdapter = new CardPagerAdapter();
        mCardAdapter.addCardItem(new CardItem(R.string.title_1, R.string.text_1));
        mCardAdapter.addCardItem(new CardItem(R.string.title_2, R.string.text_2));
        mCardAdapter.addCardItem(new CardItem(R.string.title_3, R.string.text_3));
        mCardAdapter.addCardItem(new CardItem(R.string.title_4, R.string.text_4));
        mCardAdapter.addCardItem(new CardItem(R.string.title_5, R.string.text_5));
        mCardAdapter.addCardItem(new CardItem(R.string.title_6, R.string.text_6));
        mCardAdapter.addCardItem(new CardItem(R.string.title_7, R.string.text_7));
        mCardAdapter.addCardItem(new CardItem(R.string.title_8, R.string.text_8));
        mCardAdapter.addCardItem(new CardItem(R.string.title_9, R.string.text_9));
        mCardAdapter.addCardItem(new CardItem(R.string.title_10, R.string.text_10));
        mFragmentCardAdapter = new CardFragmentPagerAdapter(manager, dpToPixels(2, view.getContext()));

        mCardShadowTransformer = new ShadowTransformer(mViewPager, mCardAdapter);
        mFragmentCardShadowTransformer = new ShadowTransformer(mViewPager, mFragmentCardAdapter);

        mViewPager.setAdapter(mCardAdapter);
        mViewPager.setPageTransformer(false, mCardShadowTransformer);
        mViewPager.setOffscreenPageLimit(3);

        mCardShadowTransformer.enableScaling(true);
        mFragmentCardShadowTransformer.enableScaling(true);

    }

    public static float dpToPixels(int dp, Context context) {
        return dp * (context.getResources().getDisplayMetrics().density);
    }

    @Override
    public void onClick(View v) {
        int position = mViewPager.getCurrentItem();
        switch (v.getId()) {
            case R.id.previous_left:
                if (position == 0)
                    Toast.makeText(getContext(), "(｀・ω・´)，左边没有了哦，看看右边吧", Toast.LENGTH_SHORT).show();
                else {
                    mViewPager.setCurrentItem(position - 1);
                    previousLeft.setImageResource(R.drawable.previous_left);
                    nextRight.setImageResource(R.drawable.next_right_press);
                }
                break;
            case R.id.next_right:
                if (position == 9)
                    Toast.makeText(getContext(), "φ(>ω<*) ，已经是最后一个了哦", Toast.LENGTH_SHORT).show();
                else {
                    mViewPager.setCurrentItem(position + 1);
                    nextRight.setImageResource(R.drawable.next_right);
                    previousLeft.setImageResource(R.drawable.previous_left_press);
                }
                break;
            default:
                break;
        }
    }
}
