package com.dcxiaolou.innervoicemvp.data;

import android.os.AsyncTask;
import android.util.Log;

import com.dcxiaolou.innervoicemvp.data.entity.ADBanner;
import com.dcxiaolou.innervoicemvp.data.entity.CourseCollect;
import com.dcxiaolou.innervoicemvp.data.entity.CourseGuide;
import com.dcxiaolou.innervoicemvp.data.entity.ReadArticle;
import com.dcxiaolou.innervoicemvp.data.entity.User;
import com.dcxiaolou.innervoicemvp.mode.ReadArticleResult;
import com.dcxiaolou.innervoicemvp.utils.Constants;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 数据层对外实现类
 * 负责对整个App提供所需要的数据因为需要多界面去访问，为保持数据的唯一使用“单例设计模式”
 */

public class DataStore {

    private static DataStore INSTANCE = new DataStore();

    private static DataCallBack<List<String>> adBannerDataCallBack;

    private static DataCallBack<List<CourseGuide>> courseGuideDataCallBack;

    private static DataCallBack<List<ReadArticleResult>> readArticleResultDataCallBack;

    public static DataStore getINSTANCE() {
        return INSTANCE;
    }

    /**
     * 判断当前用户是否登入
     *
     * @return
     */
    public User getCurrentUser() {
        return BmobUser.getCurrentUser(User.class);
    }

    /*
    * 用户登录
    * */
    public void userLogin(User user, final DataCallBack<User> callBack) {
        user.login(new SaveListener<User>() {
            @Override
            public void done(User user, BmobException e) {
                if (e == null)
                    callBack.onSuccess(user);
                else
                    callBack.onFail("登录失败, " + e.getMessage());
            }
        });
    }

    /*
    * 用户注册
    * */
    public void userSingUp(User user, final DataCallBack<User> callBack) {
        user.signUp(new SaveListener<User>() {
            @Override
            public void done(User user, BmobException e) {
                if (e == null)
                    callBack.onSuccess(user);
                else
                    callBack.onFail("注册失败，" + e.getMessage());
            }
        });
    }

    /*
    * 获取首页广告banner
    * */
    public void getADBanner(final DataCallBack<List<String>> callBack) {
        adBannerDataCallBack = callBack;
        new getADBannerTask().execute();
    }

    /*
    * 耗时操作采用异步任务进行
    * */
    private static class getADBannerTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            BmobQuery<ADBanner> bmobQuery = new BmobQuery<>();
            bmobQuery.findObjects(new FindListener<ADBanner>() {
                @Override
                public void done(List<ADBanner> list, BmobException e) {
                    if (e == null) {
                        adBannerDataCallBack.onSuccess(parseDataForLink(list));
                    } else {
                        adBannerDataCallBack.onFail("ADBanner 获取失败");
                    }
                }
            });
            return null;
        }
    }

    /*
    * 解析从bmob获取到的数据，以得到链接
    * */
    private static List<String> parseDataForLink(List<ADBanner> adBanners) {
        List<String> links = new ArrayList<>();
        BmobFile bmobFile;
        for (ADBanner adBanner: adBanners) {
            bmobFile = adBanner.getBmobFile();
            links.add(bmobFile.getFileUrl());
        }
        return links;
    }

    public void getCourseGuides(final DataCallBack<List<CourseGuide>> callBack) {
        courseGuideDataCallBack = callBack;
        new getCourseGuidesTask().execute();
    }

    private static class getCourseGuidesTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            final List<CourseGuide> courseGuides = new ArrayList<>();
            // 使用okhttp获取课程引导信息
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder().url(Constants.COURSE_GUIDE_URL).build();
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    courseGuideDataCallBack.onFail("CourseCollect 获取失败");
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String result = response.body().string();
                    //Log.d(TAG, result);
                    // 使用Gson解析返回的json信息
                    Gson gson = new Gson();
                    CourseCollect courseCollect = gson.fromJson(result, CourseCollect.class);
                    CourseGuide courseGuide;
                    for (int i = 0; i < courseCollect.getData().getItems().size(); i++) {
                        courseGuide = new CourseGuide();
                        courseGuide.setId(courseCollect.getData().getItems().get(i).getId());
                        courseGuide.setTitle(courseCollect.getData().getItems().get(i).getTitle());
                        courseGuide.setCover(courseCollect.getData().getItems().get(i).getCover());
                        courseGuide.setJoinnum(courseCollect.getData().getItems().get(i).getJoinnum());
                        courseGuide.setTeacherName(courseCollect.getData().getItems().get(i).getTeacherName());
                        courseGuides.add(courseGuide);
                    }
                    courseGuideDataCallBack.onSuccess(courseGuides);
                }
            });

            return null;
        }
    }

    /*
    * 获取每日推荐（阅读模块文章信息）信息
    * */
    public void getReadArticle(final DataCallBack<List<ReadArticleResult>> callBack) {
        readArticleResultDataCallBack = callBack;
        new getReadArticleTask().execute();
    }

    private static class getReadArticleTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            final List<ReadArticleResult> readArticleResults = new ArrayList<>();
            BmobQuery<ReadArticle> query = new BmobQuery<>();
            query.setLimit(5); // 返回5条数据
            query.order("-createdAt");
            query.findObjects(new FindListener<ReadArticle>() {
                @Override
                public void done(List<ReadArticle> list, BmobException e) {
                    if (e == null) {
                        Log.d("TAG", "dailyBestRv size = " + list.size());
                        BmobFile file;
                        final Gson gson = new Gson();
                        for (ReadArticle readArticle : list) {
                            file = readArticle.getBmobFile();
                            if (file != null) {
                                String fileUrl = file.getUrl();
                                //String fileName = file.getFilename();
                                //Log.d(TAG, fileName);
                                // 使用okhttp获取相应得文章
                                OkHttpClient client = new OkHttpClient();
                                Request request = new Request.Builder().url(fileUrl).build();
                                client.newCall(request).enqueue(new Callback() {
                                    @Override
                                    public void onFailure(Call call, IOException e) {
                                        //e.printStackTrace();
                                        readArticleResultDataCallBack.onFail("ReadArticleResult 获取失败");
                                    }

                                    @Override
                                    public void onResponse(Call call, Response response) throws IOException {
                                        String result = response.body().string();
                                        //Log.d(TAG, result);
                                        // 使用Gson解析返回的json数据
                                        ReadArticleResult readArticleResult = gson.fromJson(result, ReadArticleResult.class);
                                        readArticleResults.add(readArticleResult);
                                        if (readArticleResults.size() == 5)
                                            readArticleResultDataCallBack.onSuccess(readArticleResults);
                                    }
                                });
                            } else {
                                //Log.d(TAG, "dailyBestRv bmobFile is null");
                                readArticleResultDataCallBack.onFail("dailyBestRv bmobFile is null");
                            }
                        }
                    } else {
                        e.printStackTrace();
                        readArticleResultDataCallBack.onFail("ReadArticle 获取失败");
                    }
                }
            });
            return null;
        }
    }

}
