package com.dcxiaolou.innervoicemvp.course.ShowCourseIntroduce;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.dcxiaolou.innervoicemvp.R;
import com.dcxiaolou.innervoicemvp.utils.SharedPreferencesUtils;
import com.zyao89.view.zloading.ZLoadingDialog;

import org.sufficientlysecure.htmltextview.HtmlHttpImageGetter;
import org.sufficientlysecure.htmltextview.HtmlTextView;

import static com.zyao89.view.zloading.Z_TYPE.STAR_LOADING;

/*
 * 课程推荐（简介）界面的详情页碎片
 * */
public class CourseDetailFragment extends Fragment implements CourseDetailContract.View {

    //当页面可见时，初始化界面
    private boolean init = false;
    //是否允许加载数据
    private boolean canLoadData = false;

    private CourseDetailContract.Presenter mPresenter;

    private String courseId, coverPath;

    private View rootView;
    private Handler mHandler = new Handler();

    private HtmlTextView courseDetail;

    private ZLoadingDialog dialog;

    /*
     *解决ViewPager + fragment 页面切换后数据丢失，此处是让数据在相应的页面可见时加载数据
     * 注意：setUserVisibleHint方法先与onActivityCreate方法执行
     */
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        Log.d("TAG", "isVisibleToUser 1: " + isVisibleToUser);
        //防止viewpager预加载下一个页面后（rootView != null），下一个页面无法加载数据
        if (isVisibleToUser && (canLoadData || rootView == null )) {
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
        Log.d("TAG", "onCreateView1");
        Log.d("TAG", "init1 = " + init);
        if (rootView == null) {
            canLoadData = true;
            //当页面为空时才去加载界面，防止加载好的有数据的界面被空的新界面覆盖
            rootView = (View) inflater.inflate(R.layout.fragment_course_detail, container, false);
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

        dialog = new ZLoadingDialog(getContext());
        dialog.setLoadingBuilder(STAR_LOADING)//设置类型
                .setLoadingColor(Color.rgb(230, 230, 250))//颜色
                .setHintText("Loading...")
                .setHintTextSize(16) // 设置字体大小 dp
                .setHintTextColor(Color.rgb(230, 230, 250))  // 设置字体颜色
                .setDurationTime(0.5) // 设置动画时间百分比 - 0.5倍
                .setDialogBackgroundColor(Color.parseColor("#55CCCCCC")) // 设置背景色，默认白色
                .show();

        courseDetail = (HtmlTextView) rootView.findViewById(R.id.course_detail);
        courseId = SharedPreferencesUtils.getString("courseId");

        mPresenter = new CourseDetailPresenter(this);
        // 从bmob查询该课程id为courseId的json文件的url
        mPresenter.getCourseIntroduce(courseId);

    }

    @Override
    public void showCourseIntroduce(final String content) {
        mHandler.post(new Runnable() { // 显示课程详细信息
            @Override
            public void run() {
                courseDetail.setHtml(content, new HtmlHttpImageGetter(courseDetail));
                dialog.dismiss();
            }
        });
    }

    @Override
    public void showErrorMessage(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }
}
