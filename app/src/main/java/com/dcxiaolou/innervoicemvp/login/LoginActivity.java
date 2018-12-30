package com.dcxiaolou.innervoicemvp.login;

import android.content.Intent;

import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.dcxiaolou.innervoicemvp.R;
import com.dcxiaolou.innervoicemvp.base.BaseActivity;

import com.dcxiaolou.innervoicemvp.center.CenterFragment;
import com.dcxiaolou.innervoicemvp.home.HomeActivity;
import com.dcxiaolou.innervoicemvp.message.MessageFragment;
import com.dcxiaolou.innervoicemvp.singIn.SingInActivity;
import com.dcxiaolou.innervoicemvp.utils.Constants;

import java.util.regex.Pattern;

import de.hdodenhof.circleimageview.CircleImageView;

public class LoginActivity extends BaseActivity implements LoginContract.View, View.OnClickListener {

    /**
     * 正则表达式：验证密码
     */
    public static final String REGEX_PASSWORD = "^[a-zA-Z0-9]{6,12}$";

    /**
     * 正则表达式：验证手机号
     */
    public static final String REGEX_MOBILE = "^((17[0-9])|(14[0-9])|(13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$";

    private LoginContract.Presenter mPresenter;

    private CircleImageView userImage;
    private EditText nickNameEt, passwordEt;
    private CheckBox rememberPassword;
    private TextView logIn, singIn;

    /*
    * 记录来自哪个Activity
    * */
    private String originView;

    @Override
    protected int setLayout() {
        return R.layout.activity_login;
    }

    @Override
    protected void init() {

        mPresenter = new LoginPresenter(this);

        userImage = (CircleImageView) findViewById(R.id.user_image);
        nickNameEt = (EditText) findViewById(R.id.phone_number);
        passwordEt = (EditText) findViewById(R.id.password);
        rememberPassword = (CheckBox) findViewById(R.id.remember_password);
        logIn = (TextView) findViewById(R.id.log_in_Tv);
        singIn = (TextView) findViewById(R.id.sing_in_tv);

        Intent intent = getIntent();
        originView = intent.getStringExtra(Constants.LOGIN_DISTINGUISH_ACTIVITY);

    }

    @Override
    protected void setListener() {
        logIn.setOnClickListener(this);
        singIn.setOnClickListener(this);
    }

    @Override
    public void toOriginView() {
        Toast.makeText(this, "登录成功", Toast.LENGTH_SHORT).show();
        Intent originViewIntent;
        if (originView.equals(CenterFragment.class.getName())) {
            originViewIntent = new Intent(this, HomeActivity.class);
            originViewIntent.putExtra(Constants.HOMEACTIVITY_DISTINGUISH_PAGEVIEW, 3);
            startActivity(originViewIntent);
        } else if (originView.equals(MessageFragment.class.getName())) {
            originViewIntent = new Intent(this, HomeActivity.class);
            originViewIntent.putExtra(Constants.HOMEACTIVITY_DISTINGUISH_PAGEVIEW, 2);
            startActivity(originViewIntent);
        }
        finish();
    }

    @Override
    public void showErrorMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.log_in_Tv:
                final String nickName = nickNameEt.getText().toString().trim();
                final String password = passwordEt.getText().toString().trim();
                Log.d("TAG", password);
                if ("".equals(nickName.trim())) {
                    Toast.makeText(this, "请输入昵称", Toast.LENGTH_SHORT).show();
                } else if (password.equals("") || !Pattern.matches(REGEX_PASSWORD, password)) {
                    Toast.makeText(this, "请输入正确的密码", Toast.LENGTH_SHORT).show();
                } else {
                    mPresenter.login(nickName, password);
                }
                break;

            case R.id.sing_in_tv:
                Intent singInIntent = new Intent(this, SingInActivity.class);
                singInIntent.putExtra(Constants.LOGIN_DISTINGUISH_ACTIVITY, originView);
                this.startActivity(singInIntent);
                break;

            default:
                break;
        }
    }
}
