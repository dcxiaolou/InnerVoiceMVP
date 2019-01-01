package com.dcxiaolou.innervoicemvp.course.ShowCourseIntroduce;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
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
import com.dcxiaolou.innervoicemvp.data.entity.CourseIntroduceCatalog;
import com.dcxiaolou.innervoicemvp.utils.SharedPreferencesUtils;
import com.zyao89.view.zloading.ZLoadingDialog;

import org.sufficientlysecure.htmltextview.HtmlTextView;

import java.util.List;

import static com.zyao89.view.zloading.Z_TYPE.CIRCLE;
import static com.zyao89.view.zloading.Z_TYPE.STAR_LOADING;

public class CourseCatalogFragment extends Fragment implements CourseCatalogContract.View {

    //当页面可见时，初始化界面
    private boolean init = false;
    //是否允许加载数据
    private boolean canLoadData = false;

    private CourseCatalogContract.Presenter mPresenter;

    private View rootView;

    private Handler mHandler = new Handler();

    private String courseId, catalogPath;

    private RecyclerView courseCatalogRv;

    private ZLoadingDialog dialog;

    /*
     *解决ViewPager + fragment 页面切换后数据丢失，此处是让数据在相应的页面可见时加载数据
     * 注意：setUserVisibleHint方法先与onActivityCreate方法执行
     */
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        Log.d("TAG", "isVisibleToUser 2: " + isVisibleToUser);
        //防止viewpager预加载下一个页面后（rootView != null），下一个页面无法加载数据
        if (isVisibleToUser && (canLoadData || rootView == null)) {
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
        Log.d("TAG", "onCreateView2");
        Log.d("TAG", "init2 = " + init);
        if (rootView == null) {
            canLoadData = true;
            //当页面为空时才去加载界面，防止加载好的有数据的界面被空的新界面覆盖
            rootView = (View) inflater.inflate(R.layout.fragment_course_catalog, container, false);
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

        courseCatalogRv = (RecyclerView) rootView.findViewById(R.id.course_catalog_rv);

        // https://m.xinli001.com/lesson/getPeriodList?lesson_id=194&__from__=detail
        courseId = SharedPreferencesUtils.getString("courseId");
        // Log.d(TAG, "courseId = " + courseId);
        catalogPath = "https://m.xinli001.com/lesson/getPeriodList?lesson_id=" + courseId + "&__from__=detail";
        //Log.d(TAG, "catalogPath = " + catalogPath);
        // 使用okhttp获取课程目录
        mPresenter = new CourseCatalogPresenter(this);
        mPresenter.getCourseCatalog(catalogPath);

    }

    @Override
    public void showCourseCatalog(final List<CourseIntroduceCatalog> data) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                LinearLayoutManager manager = new LinearLayoutManager(getContext());
                manager.setOrientation(LinearLayoutManager.VERTICAL);
                courseCatalogRv.setLayoutManager(manager);
                CourseIntroduceCatalogAdapter adapter = new CourseIntroduceCatalogAdapter(data);
                courseCatalogRv.setAdapter(adapter);
                dialog.dismiss();
            }
        });
    }

    @Override
    public void showErrorMessage(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }
}
