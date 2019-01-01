package com.dcxiaolou.innervoicemvp.center;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.dcxiaolou.innervoicemvp.R;
import com.dcxiaolou.innervoicemvp.data.entity.User;
import com.dcxiaolou.innervoicemvp.login.LoginActivity;
import com.dcxiaolou.innervoicemvp.utils.Constants;
import com.dcxiaolou.innervoicemvp.utils.SharedPreferencesUtils;

import cn.bmob.v3.BmobUser;
import de.hdodenhof.circleimageview.CircleImageView;

public class CenterFragment extends Fragment implements CenterContract.View, View.OnClickListener {

    private CenterContract.Presenter mPresenter;

    private TextView logInOrSignIn;
    private RelativeLayout LagInRl;
    private CircleImageView headImageCi;
    private TextView userNameTv;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_center, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mPresenter = new CenterPresenter(this);

        logInOrSignIn = (TextView) view.findViewById(R.id.log_in_or_sign_in);

        logInOrSignIn.setOnClickListener(this);

        LagInRl = (RelativeLayout) view.findViewById(R.id.log_in_rl);

        headImageCi = (CircleImageView) view.findViewById(R.id.user_default_img);
        userNameTv = (TextView) view.findViewById(R.id.log_out);

        if (mPresenter.isLogin()) {
            User user = mPresenter.getCurrentUser();
            String avatarUrl = user.getAvatar().getFileUrl();
            Glide.with(this).load(avatarUrl).into(headImageCi);
            SharedPreferencesUtils.saveString("avatar", avatarUrl);
            userNameTv.setText(user.getUsername());
            logInOrSignIn.setText("退出登录");
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.log_in_or_sign_in:
                if (!mPresenter.isLogin()) {
                    Intent loginIntent = new Intent(getContext(), LoginActivity.class);
                    loginIntent.putExtra(Constants.LOGIN_DISTINGUISH_ACTIVITY, CenterFragment.class.getName());
                    startActivity(loginIntent);
                } else {
                    headImageCi.setImageResource(R.drawable.user_default_img);
                    userNameTv.setText("未登录");
                    logInOrSignIn.setText("点击登录/注册");
                    BmobUser.logOut();
                }
                break;
            default:
                break;
        }
    }

}
