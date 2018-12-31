package com.dcxiaolou.innervoicemvp.utils;

import android.support.v4.app.Fragment;

import com.dcxiaolou.innervoicemvp.Read.ShowReadArticleFragment1;
import com.dcxiaolou.innervoicemvp.Read.ShowReadArticleFragment2;
import com.dcxiaolou.innervoicemvp.Read.ShowReadArticleFragment3;
import com.dcxiaolou.innervoicemvp.Read.ShowReadArticleFragment4;
import com.dcxiaolou.innervoicemvp.Read.ShowReadArticleFragment5;
import com.dcxiaolou.innervoicemvp.Read.ShowReadArticleFragment6;
import com.dcxiaolou.innervoicemvp.Read.ShowReadArticleFragment7;
import com.dcxiaolou.innervoicemvp.Read.ShowReadArticleFragment8;

import java.util.WeakHashMap;

/*
* 统一创建和管理Fragment
* */
public class FragmentFactory {

    public FragmentFactory() {
    }

    //定义Map来存储创建好的fragment
    private static WeakHashMap<String, Fragment> mFragments = new WeakHashMap<>();

    /*
    * 创建Fragment的方法
    * pFragmentClass Fragment的类文件
    * return 创建好的Fragment
    * */
    public static Fragment create(Class pFragmentClass) {
        //判断是否已经创建过Fragment
        if (mFragments.get(pFragmentClass.getName()) == null) {
            //创建特定的Fragment
            if (pFragmentClass.isAssignableFrom(ShowReadArticleFragment1.class)) {
                mFragments.put(pFragmentClass.getName(), new ShowReadArticleFragment1());
            } else if (pFragmentClass.isAssignableFrom(ShowReadArticleFragment2.class)) {
                mFragments.put(pFragmentClass.getName(), new ShowReadArticleFragment2());
            } else if (pFragmentClass.isAssignableFrom(ShowReadArticleFragment3.class)) {
                mFragments.put(pFragmentClass.getName(), new ShowReadArticleFragment3());
            } else if (pFragmentClass.isAssignableFrom(ShowReadArticleFragment4.class)) {
                mFragments.put(pFragmentClass.getName(), new ShowReadArticleFragment4());
            } else if (pFragmentClass.isAssignableFrom(ShowReadArticleFragment5.class)) {
                mFragments.put(pFragmentClass.getName(), new ShowReadArticleFragment5());
            } else if (pFragmentClass.isAssignableFrom(ShowReadArticleFragment6.class)) {
                mFragments.put(pFragmentClass.getName(), new ShowReadArticleFragment6());
            } else if (pFragmentClass.isAssignableFrom(ShowReadArticleFragment7.class)) {
                mFragments.put(pFragmentClass.getName(), new ShowReadArticleFragment7());
            } else if (pFragmentClass.isAssignableFrom(ShowReadArticleFragment8.class)) {
                mFragments.put(pFragmentClass.getName(), new ShowReadArticleFragment8());
            }
        }
        return mFragments.get(pFragmentClass.getName());
    }

}
