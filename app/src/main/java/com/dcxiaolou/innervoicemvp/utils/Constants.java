package com.dcxiaolou.innervoicemvp.utils;
/*
* 封装App中公用的常量信息
* */
public class Constants {

    public Constants() {
    }

    /*
    * app是否是第一次运行
    * */
    public static final String SP_KEY_FIRST_RUN = "isFirst";


    /*
    * 用于LoginActivity区分Activity
    * */
    public static final String LOGIN_DISTINGUISH_ACTIVITY = "LOGIN_DISTINGUISH_ACTIVITY";

    /*
    * 用于主页区分viewpager
    * */
    public static final String HOMEACTIVITY_DISTINGUISH_PAGEVIEW = "HOMEACTIVITY_DISTINGUISH_PAGEVIEW";

    /*
    * 课程引导的url
    * */
    public static final String COURSE_GUIDE_URL = "http://bmob-cdn-22224.b0.upaiyun.com/2018/11/11/03e4f6834038a54f80124fdfb9c6555a.json";

    /*
    * 主页 每日推荐文章类型
    * */
    public static final String DAILY_INTRODUCE_ARTICLE_TYPE = "844";

    /*
    * 阅读 文章类型
    * 心理科普 792
    * 婚恋情感 876
    * 家庭关系 660
    * 人际社交 2206
    * 自我察觉 2199
    * 成长学习 862
    * 心理健康 823
    * 职场技能 844
    * */
    public static final String READ_ARTICLE_TYPE_1 = "792";
    public static final String READ_ARTICLE_TYPE_2 = "876";
    public static final String READ_ARTICLE_TYPE_3 = "660";
    public static final String READ_ARTICLE_TYPE_4 = "2206";
    public static final String READ_ARTICLE_TYPE_5 = "2199";
    public static final String READ_ARTICLE_TYPE_6 = "862";
    public static final String READ_ARTICLE_TYPE_7 = "823";
    public static final String READ_ARTICLE_TYPE_8 = "844";

    /*
    * 问答模块
    * 全部
    * 最新
    * 最热
    * */
    public static final String QUESTION_ALL_LIMIT_NUM = "5";
    public static final String QUESTION_LATEST_LIMIT_NUM = "10";
    public static final String QUESTION_HOT_LIMIT_NUM = "10";

}
