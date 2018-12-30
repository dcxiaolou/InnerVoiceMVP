package com.dcxiaolou.innervoicemvp.pushArticle;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.webkit.ValueCallback;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.Toast;

import com.dcxiaolou.innervoicemvp.R;
import com.dcxiaolou.innervoicemvp.base.BaseActivity;
import com.dcxiaolou.innervoicemvp.utils.ImageUtil;
import com.dcxiaolou.innervoicemvp.utils.ReWebChomeClient;
import com.dcxiaolou.innervoicemvp.utils.ReWebViewClient;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class PushArticleActivity extends BaseActivity implements PushArticleContract.View, ReWebChomeClient.OpenFileChooserCallBack {

    private static final String TAG = "PushArticleActivity";
    private static final int REQUEST_CODE_PICK_IMAGE = 0;
    private static final int REQUEST_CODE_IMAGE_CAPTURE = 1;
    private WebView mWebView;
    private Intent mSourceIntent;
    private ValueCallback<Uri> mUploadMsg;
    private ValueCallback<Uri[]> mUploadMsg5Plus;

    private List<String> selectImgPath = new ArrayList<>();
    private List<String> bmobImgPath = new ArrayList<>();

    private ImageView pushArticleBackIv, pushArticleIv;

    private String path = "";

    private PushArticleContract.Presenter mPresenter;

    @Override
    protected int setLayout() {
        return R.layout.activity_push_article;
    }

    @Override
    protected void init() {

        pushArticleBackIv = (ImageView) findViewById(R.id.iv_push_article_back);
        pushArticleIv = (ImageView) findViewById(R.id.iv_push_article);

        mWebView = (WebView) findViewById(R.id.web_view_push_article);


        mWebView.setWebChromeClient(new ReWebChomeClient(this));
        mWebView.setWebViewClient(new ReWebViewClient());
        WebSettings webSettings = mWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);

        mPresenter = new PushArticlePresenter(this);
        mPresenter.fixDirPath();
        //这里加载自己部署的（也可加载本地资源）
        mWebView.loadUrl("file:///android_asset/Eleditor-master/demo/index.html");

    }

    @Override
    protected void setListener() {

        pushArticleIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(PushArticleActivity.this, "发布成功{of course not ∩( ・ω・)∩萌萌哒}", Toast.LENGTH_SHORT).show();
            }
        });

        pushArticleBackIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //结束当前，返回主页
                finish();
            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        switch (requestCode) {
            case REQUEST_CODE_IMAGE_CAPTURE:
            case REQUEST_CODE_PICK_IMAGE: {
                try {
                    if (mUploadMsg == null && mUploadMsg5Plus == null) {
                        return;
                    }
                    String sourcePath = ImageUtil.retrievePath(this, mSourceIntent, data);
                    Log.d(TAG, "sourcePath: " + sourcePath);//图片选择返回的路径
                    selectImgPath.add(sourcePath);
                    if (TextUtils.isEmpty(sourcePath) || !new File(sourcePath).exists()) {
                        Log.w(TAG, "sourcePath empty or not exists.");
                        break;
                    }
                    Uri uri = Uri.fromFile(new File(sourcePath));
                    //Log.d(TAG, "uri: " + uri);
                    if (mUploadMsg != null) {
                        mUploadMsg.onReceiveValue(uri);
                        mUploadMsg = null;
                    } else {
                        mUploadMsg5Plus.onReceiveValue(new Uri[]{uri});
                        mUploadMsg5Plus = null;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            }
        }
    }

    @Override
    public void openFileChooserCallBack(ValueCallback<Uri> uploadMsg, String acceptType) {
        mUploadMsg = uploadMsg;
        showOptions();
    }

    @Override
    public void showFileChooserCallBack(ValueCallback<Uri[]> filePathCallback) {
        mUploadMsg5Plus = filePathCallback;
        showOptions();
    }

    @Override
    public void showOptions() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setOnCancelListener(new ReOnCancelListener());
        alertDialog.setTitle(R.string.options);
        alertDialog.setItems(R.array.options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == 0) {
                    mSourceIntent = ImageUtil.choosePicture();
                    startActivityForResult(mSourceIntent, REQUEST_CODE_PICK_IMAGE);
                } else {
                    mSourceIntent = ImageUtil.takeBigPicture(PushArticleActivity.this);
                    startActivityForResult(mSourceIntent, REQUEST_CODE_IMAGE_CAPTURE);
                }
            }
        });
        alertDialog.show();
    }

    private class ReOnCancelListener implements DialogInterface.OnCancelListener {

        @Override
        public void onCancel(DialogInterface dialogInterface) {
            if (mUploadMsg != null) {
                mUploadMsg.onReceiveValue(null);
                mUploadMsg = null;
            }
            if (mUploadMsg5Plus != null) {
                mUploadMsg5Plus.onReceiveValue(null);
                mUploadMsg5Plus = null;
            }
        }
    }

}
