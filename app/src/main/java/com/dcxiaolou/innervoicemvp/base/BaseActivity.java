package com.dcxiaolou.innervoicemvp.base;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public abstract class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(setLayout());
        init();
        setListener();
    }

    /*
    * 加载布局文件，子类去实现
    * */
    protected abstract int setLayout();

    /*
    * 初始化所有属性
    * */
    protected abstract void init();

    /*
    * 设置监听器
    * */
    protected abstract void setListener();

    /*
    * 跳转到新页面
    * */
    protected void toNewView(Class activityClass) {
        Intent intent = new Intent();
        intent.setClass(getApplicationContext(), activityClass);
        startActivity(intent);
    }

}
