package com.dcxiaolou.innervoicemvp.data;

import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.dcxiaolou.innervoicemvp.data.entity.ADBanner;
import com.dcxiaolou.innervoicemvp.data.entity.Answer;
import com.dcxiaolou.innervoicemvp.data.entity.CourseDetail;
import com.dcxiaolou.innervoicemvp.data.entity.CourseIntroduce;
import com.dcxiaolou.innervoicemvp.data.entity.CourseIntroduceCatalog;
import com.dcxiaolou.innervoicemvp.data.entity.CourseIntroduceForShow;
import com.dcxiaolou.innervoicemvp.data.entity.FM;
import com.dcxiaolou.innervoicemvp.mode.AnswerResult;
import com.dcxiaolou.innervoicemvp.data.entity.CourseCollect;
import com.dcxiaolou.innervoicemvp.data.entity.CourseGuide;
import com.dcxiaolou.innervoicemvp.data.entity.Question;
import com.dcxiaolou.innervoicemvp.data.entity.ReadArticle;
import com.dcxiaolou.innervoicemvp.data.entity.User;
import com.dcxiaolou.innervoicemvp.mode.CourseDetailResult;
import com.dcxiaolou.innervoicemvp.mode.CourseIntroduceCatalogResult;
import com.dcxiaolou.innervoicemvp.data.entity.CourseIntroduceCommon;
import com.dcxiaolou.innervoicemvp.mode.CourseIntroduceCommonResult;
import com.dcxiaolou.innervoicemvp.mode.FMResult;
import com.dcxiaolou.innervoicemvp.mode.QuestionResult;
import com.dcxiaolou.innervoicemvp.mode.ReadArticleResult;
import com.dcxiaolou.innervoicemvp.utils.Constants;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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

    private static DataCallBack<List<QuestionResult>> questionResultDataCallBack;

    private static DataCallBack<List<AnswerResult>> ansertResultDataCallBack;

    private static DataCallBack<String> courseIntroduceDataCallBack;

    private static DataCallBack<List<CourseIntroduceCatalog>> courseIntroduceCatalogDataCallBack;

    private static DataCallBack<List<CourseIntroduceCommon>> courseIntroduceCommonDataCallBack;

    private static DataCallBack<String> courseDetailDataCallBack;

    private static DataCallBack<List<FMResult>> fmResultDataCallBack;


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
        for (ADBanner adBanner : adBanners) {
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
     * 获取每日推荐、阅读模块文章信息
     * */
    public void getReadArticle(final DataCallBack<List<ReadArticleResult>> callBack, String type, String skipNum) {
        readArticleResultDataCallBack = callBack;
        new getReadArticleTask().execute(type, skipNum);
    }

    private static class getReadArticleTask extends AsyncTask<String, Void, Void> {
        @Override
        protected Void doInBackground(String... params) {
            Log.d("TAG", "params[1] = " + params[1] + " params[0] = " + params[0]);
            final List<ReadArticleResult> readArticleResults = new ArrayList<>();
            BmobQuery<ReadArticle> query = new BmobQuery<>();
            query.addQueryKeys("article");
            query.setLimit(5); // 返回5条数据
            query.setSkip(Integer.parseInt(params[1])); //每次跳过的数据项，即分页
            query.addWhereEqualTo("type", params[0]); //文章类型
            query.order("-createdAt");
            query.findObjects(new FindListener<ReadArticle>() {
                @Override
                public void done(List<ReadArticle> list, BmobException e) {
                    if (e == null) {
                        final int size = list.size();
                        Log.d("TAG", "ReadArticle size = " + size);
                        BmobFile file;
                        final Gson gson = new Gson();
                        for (ReadArticle readArticle : list) {
                            file = readArticle.getBmobFile();
                            if (file != null) {
                                String fileUrl = file.getUrl();
                                // 使用okhttp获取相应得文章
                                OkHttpClient client = new OkHttpClient();
                                Request request = new Request.Builder().url(fileUrl).build();
                                client.newCall(request).enqueue(new Callback() {
                                    @Override
                                    public void onFailure(Call call, IOException e) {
                                        e.printStackTrace();
                                        readArticleResultDataCallBack.onFail("ReadArticleResult 获取失败");
                                    }

                                    @Override
                                    public void onResponse(Call call, Response response) throws IOException {
                                        String result = response.body().string();
                                        //Log.d(TAG, result);
                                        // 使用Gson解析返回的json数据
                                        ReadArticleResult readArticleResult = gson.fromJson(result, ReadArticleResult.class);
                                        readArticleResults.add(readArticleResult);
                                        if (readArticleResults.size() == size)
                                            readArticleResultDataCallBack.onSuccess(readArticleResults);
                                    }
                                });
                            } else {
                                readArticleResultDataCallBack.onFail("ReadArticle bmobFile is null");
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

    /*
     * 问答模块 问题信息的获取
     * */
    public void getQuestion(final DataCallBack<List<QuestionResult>> callBack, String limitNum, String skipNum) {
        questionResultDataCallBack = callBack;
        new getQuestionTask().execute(limitNum, skipNum);
    }

    private static class getQuestionTask extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... strings) {
            final List<QuestionResult> questionResults = new ArrayList<>();
            BmobQuery<Question> query = new BmobQuery<>();
            query.addQueryKeys("question");
            query.setLimit(Integer.parseInt(strings[0])); // 返回5条数据
            query.setSkip(Integer.parseInt(strings[1])); //每次跳过的数据项，即分页
            query.order("-createdAt");
            query.findObjects(new FindListener<Question>() {
                @Override
                public void done(List<Question> list, BmobException e) {
                    if (e == null) {
                        final int size = list.size();
                        Log.d("TAG", "Question size = " + size);
                        BmobFile file;
                        final Gson gson = new Gson();
                        for (Question question : list) {
                            file = question.getQuestion();
                            final String idStr = ", \"id\": \"" + file.getFilename() + "\"";
                            //Log.d("TAG", "idStr = " + idStr);
                            if (file != null) {
                                String fileUrl = file.getUrl();
                                // 使用okhttp获取相应得文章
                                OkHttpClient client = new OkHttpClient();
                                Request request = new Request.Builder().url(fileUrl).build();
                                client.newCall(request).enqueue(new Callback() {
                                    @Override
                                    public void onFailure(Call call, IOException e) {
                                        e.printStackTrace();
                                        questionResultDataCallBack.onFail("QuestionResult 获取失败");
                                    }

                                    @Override
                                    public void onResponse(Call call, Response response) throws IOException {
                                        String result = response.body().string();
                                        String newResult = "";
                                        for (int i = 0; i < result.indexOf('}'); i++)
                                            newResult += result.charAt(i);
                                        //Log.d("TAG", newResult);
                                        newResult += idStr + "}";
                                        //Log.d("TAG", newResult);
                                        // 使用Gson解析返回的json数据
                                        QuestionResult questionResult = gson.fromJson(newResult, QuestionResult.class);
                                        questionResults.add(questionResult);
                                        if (questionResults.size() == size)
                                            questionResultDataCallBack.onSuccess(questionResults);
                                    }
                                });
                            } else {
                                questionResultDataCallBack.onFail("Question bmobFile is null");
                            }
                        }
                    } else {
                        e.printStackTrace();
                        questionResultDataCallBack.onFail("Question 获取失败");
                    }
                }
            });
            return null;
        }
    }

    /*
     * 问答模块 回答信息的获取
     * */
    public void getAnsewr(final DataCallBack<List<AnswerResult>> callBack, String type) {
        ansertResultDataCallBack = callBack;
        new getAnswerTask().execute(type);
    }

    private static class getAnswerTask extends AsyncTask<String, Void, Void> {
        @Override
        protected Void doInBackground(String... strings) {
            final List<AnswerResult> answerResults = new ArrayList<>();
            BmobQuery<Answer> query = new BmobQuery<>();
            query.addQueryKeys("answer");
            query.addWhereEqualTo("type", strings[0]);
            query.setLimit(100);
            query.order("-createdAt");
            query.findObjects(new FindListener<Answer>() {
                @Override
                public void done(List<Answer> list, BmobException e) {
                    if (e == null) {
                        final int size = list.size();
                        Log.d("TAG", "Answer size = " + size);
                        BmobFile file;
                        final Gson gson = new Gson();
                        for (Answer answer : list) {
                            file = answer.getAnswer();
                            if (file != null) {
                                String fileUrl = file.getUrl();
                                // 使用okhttp获取相应得文章
                                OkHttpClient client = new OkHttpClient();
                                Request request = new Request.Builder().url(fileUrl).build();
                                client.newCall(request).enqueue(new Callback() {
                                    @Override
                                    public void onFailure(Call call, IOException e) {
                                        e.printStackTrace();
                                        ansertResultDataCallBack.onFail("AnswerResult 获取失败");
                                    }

                                    @Override
                                    public void onResponse(Call call, Response response) throws IOException {
                                        String result = response.body().string();
                                        // 使用Gson解析返回的json数据
                                        AnswerResult answerResult = gson.fromJson(result, AnswerResult.class);
                                        answerResults.add(answerResult);
                                        if (answerResults.size() == size)
                                            ansertResultDataCallBack.onSuccess(answerResults);
                                    }
                                });
                            } else {
                                ansertResultDataCallBack.onFail("Question bmobFile is null");
                            }
                        }
                    } else {
                        e.printStackTrace();
                        ansertResultDataCallBack.onFail("Question 获取失败");
                    }
                }
            });
            return null;
        }
    }

    /*
     * 课程模块 获取课程简介
     * */
    public void getCourseIntroduce(DataCallBack<String> callBack, String courseId) {
        courseIntroduceDataCallBack = callBack;
        new getCourseIntroduceTask().execute(courseId);
    }

    private static class getCourseIntroduceTask extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(final String... strings) {
            BmobQuery<CourseIntroduce> query = new BmobQuery<>();
            query.addQueryKeys("introduce");
            query.findObjects(new FindListener<CourseIntroduce>() {
                @Override
                public void done(List<CourseIntroduce> list, BmobException e) {
                    if (e == null) {
                        Log.d("TAG", "list size = " + list.size());
                        String filePath = null;
                        for (CourseIntroduce courseIntroduce : list) {
                            BmobFile file = courseIntroduce.getIntroduce();
                            String filename = file.getFilename();
                            //Log.d("TAG", filename);
                            if (filename.equals(strings[0] + ".json")) {
                                filePath = file.getUrl();
                                //Log.d("TAG", filePath);
                            }
                        }
                        if (filePath != null) {
                            // 获取json文件
                            OkHttpClient client = new OkHttpClient();
                            Request request = new Request.Builder().url(filePath).build();
                            client.newCall(request).enqueue(new Callback() {
                                @Override
                                public void onFailure(Call call, IOException e) {
                                    e.printStackTrace();
                                    courseIntroduceDataCallBack.onFail("CourseIntroduceForShow 获取失败");
                                }

                                @Override
                                public void onResponse(Call call, Response response) throws IOException {
                                    final String result = response.body().string();
                                    //Log.d("TAG", result);
                                    Gson gson = new Gson();
                                    CourseIntroduceForShow courseIntroduceForShow = gson.fromJson(result, CourseIntroduceForShow.class);
                                    List<String> contents = courseIntroduceForShow.getIntroduce();
                                    final String content;
                                    if (contents.size() == 0) {
                                        content = "<h2  style=\" text-align:center; \">!!!∑(ﾟДﾟノ)ノ该课程暂无介绍</h2>";
                                    } else {
                                        content = contents.get(0);
                                        content.replace("\n", "");
                                    }
                                    //Log.d("TAG", "content = " + content);
                                    courseIntroduceDataCallBack.onSuccess(content);
                                }
                            });
                        }
                    } else {
                        e.printStackTrace();
                        courseIntroduceDataCallBack.onFail("CourseIntroduce 获取失败");
                    }
                }
            });
            return null;
        }
    }

    /*
     * 课程模块 获取课程目录
     * */
    public void getCourseCatalog(DataCallBack<List<CourseIntroduceCatalog>> callBack, String catalogPath) {
        courseIntroduceCatalogDataCallBack = callBack;
        new getCourseCatalogTask().execute(catalogPath);
    }

    private static class getCourseCatalogTask extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... strings) {
            final List<CourseIntroduceCatalog> courseIntroduceCatalogs = new ArrayList<>();
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder().url(strings[0]).build();
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    e.printStackTrace();
                    courseIntroduceCatalogDataCallBack.onFail("CourseIntroduceCatalogResult 获取失败");
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String result = response.body().string();
                    //Log.d(TAG, result);
                    Gson gson = new Gson();
                    CourseIntroduceCatalogResult catalogResult = gson.fromJson(result, CourseIntroduceCatalogResult.class);
                    // 有些课程的目录信息在NoParentListBean中，有些在PassListBean中
                    List<CourseIntroduceCatalogResult.DataBean.NoParentListBean> noParentListBeans = catalogResult.getData().getNoParentList();
                    List<CourseIntroduceCatalogResult.DataBean.PassListBean> passListBeans = catalogResult.getData().getPassList();
                    CourseIntroduceCatalog catalog;
                    int size;
                    if (noParentListBeans.size() != 0) {
                        CourseIntroduceCatalogResult.DataBean.NoParentListBean bean;
                        size = noParentListBeans.size();
                        for (int i = 0; i < size; i++) {
                            bean = noParentListBeans.get(i);
                            // Log.d(TAG, bean.getTitle());
                            catalog = new CourseIntroduceCatalog();
                            catalog.setTitle(bean.getTitle());
                            int time = Integer.valueOf(bean.getDuration());
                            // 时间转换
                            String duration = "" + time / 60 + "'" + time % 60 + "\"";
                            catalog.setTime(duration);
                            catalog.setIndex(bean.getIndex());
                            //Log.d(TAG, catalog.getTitle());
                            courseIntroduceCatalogs.add(catalog);
                        }
                    } else if (passListBeans.get(0).getChild().size() != 0) {
                        CourseIntroduceCatalogResult.DataBean.PassListBean.ChildBean childBean;
                        size = passListBeans.get(0).getChild().size();
                        for (int i = 0; i < size; i++) {
                            childBean = passListBeans.get(0).getChild().get(i);
                            // Log.d(TAG, bean.getTitle());
                            catalog = new CourseIntroduceCatalog();
                            catalog.setTitle(childBean.getTitle());
                            int time = Integer.valueOf(childBean.getDuration());
                            String duration = "" + time / 60 + "'" + time % 60 + "\"";
                            catalog.setTime(duration);
                            catalog.setIndex(childBean.getIndex());
                            //Log.d(TAG, catalog.getTitle());
                            courseIntroduceCatalogs.add(catalog);
                        }
                    }
                    //Log.d("TAG", "courseIntroduceCatalogs.size() = " + courseIntroduceCatalogs.size());
                    courseIntroduceCatalogDataCallBack.onSuccess(courseIntroduceCatalogs);
                }
            });
            return null;
        }
    }

    /*
     * 课程模块 获取课程评论
     * */
    public void getCourseCommon(DataCallBack<List<CourseIntroduceCommon>> callBack, String courseId) {
        courseIntroduceCommonDataCallBack = callBack;
        new getCourseCommonTask().execute(courseId);
    }

    private static class getCourseCommonTask extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... strings) {
            final List<CourseIntroduceCommon> commons = new ArrayList<>();
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder().url(strings[0]).build();
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    e.printStackTrace();
                    courseIntroduceCommonDataCallBack.onFail("CourseIntroduceCommonResult 获取失败");
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String result = response.body().string();
                    //Log.d(TAG, result);
                    Gson gson = new Gson();
                    CourseIntroduceCommonResult commonResult = gson.fromJson(result, CourseIntroduceCommonResult.class);
                    List<CourseIntroduceCommonResult.DataBean> dataBeans = commonResult.getData();
                    CourseIntroduceCommonResult.DataBean dataBean;
                    CourseIntroduceCommon common;
                    for (int i = 0; i < dataBeans.size(); i++) {
                        dataBean = dataBeans.get(i);
                        common = new CourseIntroduceCommon();
                        common.setId("" + dataBean.getId());
                        common.setUserId("" + dataBean.getUserId());
                        common.setUserName(dataBean.getUserNickname());
                        common.setUserImagePath(dataBean.getUserAvatar());
                        common.setPushTime("" + dataBean.getCreated());
                        common.setContent(dataBean.getContent());
                        common.setLikeNum("" + dataBean.getZanNum());
                        common.setReplyNum("" + dataBean.getCollegeRateReplyVos().size());
                        common.setReplayCommons(dataBean.getCollegeRateReplyVos());
                        commons.add(common);
                    }
                    //Log.d("TAG", "commons.size() = " + commons.size());
                    courseIntroduceCommonDataCallBack.onSuccess(commons);
                }
            });
            return null;
        }
    }

    /*
     * 课程模块 获取课程以进行播放
     * */
    public void getCourseForPlay(DataCallBack<String> callBack, String courseFileName) {
        courseDetailDataCallBack = callBack;
        new getCourseForPlayTask().execute(courseFileName);
    }

    private static class getCourseForPlayTask extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(final String... strings) {
            BmobQuery<CourseDetail> query = new BmobQuery<>();
            query.addQueryKeys("detail");
            query.setLimit(200); //默认最大返回100条
            query.findObjects(new FindListener<CourseDetail>() {
                @Override
                public void done(List<CourseDetail> list, BmobException e) {
                    String courseUrl = "";
                    if (e == null) {
                        String fileName;
                        BmobFile file;
                        for (CourseDetail detail : list) {
                            file = detail.getDetail();
                            fileName = file.getFilename();
                            //Log.d("TAG", "fileName = " + fileName);
                            //Log.d("TAG", "strings[0] = " + strings[0]);
                            if (fileName.equals(strings[0])) {
                                //Log.d(TAG, fileName);
                                courseUrl = file.getUrl();
                                break;
                            }
                        }
                        if (courseUrl == null) {
                            courseDetailDataCallBack.onFail("哎呀Σ( ° △ °|||)，视频不见了！");
                        } else {
                            OkHttpClient client = new OkHttpClient();
                            Request request = new Request.Builder().url(courseUrl).build();
                            client.newCall(request).enqueue(new Callback() {
                                @Override
                                public void onFailure(Call call, IOException e) {
                                    e.printStackTrace();
                                }

                                @Override
                                public void onResponse(Call call, Response response) throws IOException {
                                    String result = response.body().string();
                                    Gson gson = new Gson();
                                    CourseDetailResult detailResult = gson.fromJson(result, CourseDetailResult.class);
                                    String coursePlayUrl = detailResult.getMedia();
                                    courseDetailDataCallBack.onSuccess(coursePlayUrl);
                                }
                            });
                        }
                    } else {
                        courseDetailDataCallBack.onFail("courseUrl 获取失败");
                    }
                }
            });
            return null;
        }
    }

}
