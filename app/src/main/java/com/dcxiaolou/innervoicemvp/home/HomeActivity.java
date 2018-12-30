package com.dcxiaolou.innervoicemvp.home;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.dcxiaolou.innervoicemvp.R;
import com.dcxiaolou.innervoicemvp.adapter.FragmentAdapter;
import com.dcxiaolou.innervoicemvp.base.BaseActivity;
import com.dcxiaolou.innervoicemvp.center.CenterFragment;
import com.dcxiaolou.innervoicemvp.data.DataStore;
import com.dcxiaolou.innervoicemvp.message.MessageFragment;
import com.dcxiaolou.innervoicemvp.pushArticle.PushArticleActivity;
import com.dcxiaolou.innervoicemvp.pushSecret.PushSecretActivity;
import com.dcxiaolou.innervoicemvp.treeHole.TreeHoleFragment;
import com.dcxiaolou.innervoicemvp.utils.Constants;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends BaseActivity implements View.OnClickListener, ViewPager.OnPageChangeListener {

    private ViewPager mViewPager;

    private RadioButton mHomeRb, mTreeHoleRb, mMessageRb, mCenterRb;

    private ImageView pushIv, pushCloseIv;

    private RelativeLayout pushInclude;

    private RadioGroup mainPageRadioPage;

    private CardView pushArticleCv, pushSecretCv;

    //分界线
    private LinearLayout llBoundary;

    private Intent intent;

    private long exitTime = 0; //通过计算按键间隔时间差，实现按两次BACK键退出程序

    @Override
    protected int setLayout() {
        return R.layout.activity_home;
    }

    @Override
    protected void init() {

        permissionRequest();

        mViewPager = (ViewPager) findViewById(R.id.view_pager);

        mHomeRb = (RadioButton) findViewById(R.id.home_rb);
        mTreeHoleRb = (RadioButton) findViewById(R.id.tree_hole_rb);
        mMessageRb = (RadioButton) findViewById(R.id.message_rb);
        mCenterRb = (RadioButton) findViewById(R.id.center_rb);

        pushIv = (ImageView) findViewById(R.id.iv_push);
        pushCloseIv = (ImageView) findViewById(R.id.iv_push_close);

        pushInclude = (RelativeLayout) findViewById(R.id.include_push);

        mainPageRadioPage = (RadioGroup) findViewById(R.id.radio_group_main_page);

        pushArticleCv = (CardView) findViewById(R.id.cv_push_article);
        pushSecretCv = (CardView) findViewById(R.id.cv_push_secret);

        llBoundary = (LinearLayout) findViewById(R.id.ll_boundary);

        //给按钮添加监听器，在按钮点击后，页面要相应的切换
        mHomeRb.setOnClickListener(this);
        mTreeHoleRb.setOnClickListener(this);
        mMessageRb.setOnClickListener(this);
        mCenterRb.setOnClickListener(this);

        pushIv.setOnClickListener(this);
        pushCloseIv.setOnClickListener(this);

        pushArticleCv.setOnClickListener(this);
        pushSecretCv.setOnClickListener(this);

        //给ViewPager设置监听器，在页面滑动切换后，按钮要相应的切换
        mViewPager.setOnPageChangeListener(this);

        //给ViewPager设置adapter
        List<Fragment> mFragments = new ArrayList<>();

        mFragments.add(new HomeFragment());
        mFragments.add(new TreeHoleFragment());
        mFragments.add(new MessageFragment());
        mFragments.add(new CenterFragment());

        FragmentPagerAdapter adapter = new FragmentAdapter(getSupportFragmentManager(), mFragments);

        mViewPager.setAdapter(adapter);

        LoginOrSinginChangeViewPage();

    }

    private void LoginOrSinginChangeViewPage() {
        intent = getIntent();
        int changePageNum = intent.getIntExtra(Constants.HOMEACTIVITY_DISTINGUISH_PAGEVIEW, -1);
        if (changePageNum == 2) {
            mViewPager.setCurrentItem(changePageNum);
            mMessageRb.setChecked(true);
        } else if (changePageNum == 3) {
            mViewPager.setCurrentItem(changePageNum);
            mCenterRb.setChecked(true);
        }
    }

    private void permissionRequest() {

        //SD卡读写权限申请
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
        }

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 2);
        }

    }

    //SD卡读写权限申请响应
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.d("TAG", "read permission");
                } else {
                    Log.d("TAG", "filed read permission");
                }
                break;
            case 2:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.d("TAG", "write permission");
                } else {
                    Log.d("TAG", "filed write permission");
                }
                break;
            default:
                break;
        }

    }

    @Override
    protected void setListener() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.home_rb:
                mViewPager.setCurrentItem(0);
                break;
            case R.id.tree_hole_rb:
                mViewPager.setCurrentItem(1);
                break;
            case R.id.message_rb:
                mViewPager.setCurrentItem(2);
                break;
            case R.id.center_rb:
                mViewPager.setCurrentItem(3);
                break;
            case R.id.iv_push:
                pushInclude.setVisibility(View.VISIBLE);
                mViewPager.setVisibility(View.GONE);
                mainPageRadioPage.setVisibility(View.GONE);
                llBoundary.setVisibility(View.GONE);
                break;
            case R.id.iv_push_close:
                pushInclude.setVisibility(View.GONE);
                mViewPager.setVisibility(View.VISIBLE);
                mainPageRadioPage.setVisibility(View.VISIBLE);
                llBoundary.setVisibility(View.VISIBLE);
                break;
            case R.id.cv_push_article:
                intent = new Intent(this, PushArticleActivity.class);
                startActivity(intent);
                break;
            case R.id.cv_push_secret:
                intent = new Intent(this, PushSecretActivity.class);
                startActivity(intent);
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
                mHomeRb.setChecked(true);
                break;
            case 1:
                mTreeHoleRb.setChecked(true);
                break;
            case 2:
                mMessageRb.setChecked(true);
                break;
            case 3:
                mCenterRb.setChecked(true);
                break;
            default:
                break;
        }
    }

    @Override
    public void onPageScrollStateChanged(int i) {

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exit();
            return false;
        }

        return super.onKeyDown(keyCode, event);
    }

    private void exit() {

        if (System.currentTimeMillis() - exitTime > 2000) {
            Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
            exitTime = System.currentTimeMillis();
        } else {
            finish();
            System.exit(0);
        }

    }
}
