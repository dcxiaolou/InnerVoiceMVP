package com.dcxiaolou.innervoicemvp.singIn;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.ContentUris;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.dcxiaolou.innervoicemvp.R;
import com.dcxiaolou.innervoicemvp.base.BaseActivity;
import com.dcxiaolou.innervoicemvp.center.CenterFragment;
import com.dcxiaolou.innervoicemvp.home.HomeActivity;
import com.dcxiaolou.innervoicemvp.message.MessageFragment;
import com.dcxiaolou.innervoicemvp.utils.Constants;

import java.io.File;
import java.util.regex.Pattern;

import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UploadFileListener;
import de.hdodenhof.circleimageview.CircleImageView;

public class SingInActivity extends BaseActivity implements SingInContract.View, View.OnClickListener {

    private static final int CHOOSE_PHOTO = 1;

    /**
     * 正则表达式：验证密码
     */
    public static final String REGEX_PASSWORD = "^[a-zA-Z0-9]{6,12}$";

    /**
     * 正则表达式：验证手机号
     */
    public static final String REGEX_MOBILE = "^((17[0-9])|(14[0-9])|(13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$";

    private SingInContract.Presenter mPresenter;

    private CircleImageView userImage;
    private EditText phoneNumberEt, userNameEt, passwordEt;
    private TextView singInTv, hintTv;

    private String imagePath;

    @Override
    protected int setLayout() {
        return R.layout.activity_sing_in;
    }

    @Override
    protected void init() {

        mPresenter = new SingInPresenter(this);

        userImage = (CircleImageView) findViewById(R.id.user_image);
        phoneNumberEt = (EditText) findViewById(R.id.phone_number);
        passwordEt = (EditText) findViewById(R.id.password);
        userNameEt = (EditText) findViewById(R.id.user_name);
        singInTv = (TextView) findViewById(R.id.sing_in);
        hintTv = (TextView) findViewById(R.id.hint);

    }

    @Override
    protected void setListener() {
        userImage.setOnClickListener(this);
        singInTv.setOnClickListener(this);
    }

    @Override
    public void toOriginView() {
        Toast.makeText(this, "注册成功", Toast.LENGTH_SHORT).show();
        Intent intent = getIntent();
        String originView = intent.getStringExtra(Constants.LOGIN_DISTINGUISH_ACTIVITY);
        Log.d("TAG", "originView: " + originView);
        if (originView.equals(CenterFragment.class.getName())) {
            intent = new Intent(this, HomeActivity.class);
            intent.putExtra(Constants.HOMEACTIVITY_DISTINGUISH_PAGEVIEW, 3);
            startActivity(intent);
        } else if (originView.equals(MessageFragment.class.getName())) {
            intent = new Intent(this, HomeActivity.class);
            intent.putExtra(Constants.HOMEACTIVITY_DISTINGUISH_PAGEVIEW, 2);
            startActivity(intent);
        }
        finish();
    }

    @Override
    public void showErrorMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        Log.d("TAG", message);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.user_image:
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) !=
                        PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(SingInActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                } else {
                    //从相册中选择图片
                    openAlbum();
                    hintTv.setVisibility(View.GONE);
                }
                break;
            case R.id.sing_in:
                final String phoneNumber = phoneNumberEt.getText().toString();
                final String userName = userNameEt.getText().toString().trim();
                final String password = passwordEt.getText().toString().trim();
                if (!Pattern.matches(REGEX_MOBILE, phoneNumber)) {
                    Toast.makeText(this, "请输入正确的手机号", Toast.LENGTH_SHORT).show();
                } else if ("".equals(userName.trim())) {
                    Toast.makeText(this, "昵称不能为空哦", Toast.LENGTH_SHORT).show();
                } else if (!Pattern.matches(REGEX_PASSWORD, password)) {
                    Toast.makeText(this, "密码格式不正确，请输入6-12位的数字、字母哦", Toast.LENGTH_SHORT).show();
                } else if (imagePath == null) {
                    Toast.makeText(this, "头像还是空的哦", Toast.LENGTH_SHORT).show();
                } else {
                    //Log.d("TAG", "imagePath2: " + imagePath);
                    final BmobFile avatar = new BmobFile(new File(imagePath));
                    //先要将文件上传，才能存入数据库
                    avatar.upload(new UploadFileListener() {
                        @Override
                        public void done(BmobException e) {
                            mPresenter.singIn(phoneNumber, userName, password, avatar);
                        }
                    });

                }
                break;
        }

    }

    private void openAlbum() {
        Intent intent = new Intent("android.intent.action.GET_CONTENT");
        intent.setType("image/*");
        startActivityForResult(intent, CHOOSE_PHOTO); // 打开相册
    }

    //SD卡读权限动态申请
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openAlbum();
                } else {
                    Toast.makeText(this, "You denied the permission", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        switch (requestCode) {
            case CHOOSE_PHOTO:
                if (resultCode == RESULT_OK) {
                    // 判断手机系统版本号
                    if (Build.VERSION.SDK_INT >= 19) {
                        //4.4以上版本使用这个方法处理图片
                        handleImageOkKitKat(data);
                    } else {
                        // 4.4 以下版本使用这个方法处理图片
                        handleImageBeforeKitKat(data);
                    }
                }
                break;
            default:
                break;
        }
    }

    @TargetApi(19)
    private void handleImageOkKitKat(Intent data) {
        imagePath = null;
        Uri uri = data.getData();
        if (DocumentsContract.isDocumentUri(this, uri)) {
            // 如果是document类型的Uri，则通过document id处理
            String docId = DocumentsContract.getDocumentId(uri);
            if ("com.android.providers.media.documents".equals(uri.getAuthority())) {
                String id = docId.split(":")[1]; // 解析出数字格式的id
                String selection = MediaStore.Images.Media._ID + "=" + id;
                imagePath = getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, selection);
            } else if ("com.android.providers.downloads.documents".equals(uri.getAuthority())) {
                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(docId));
                imagePath = getImagePath(contentUri, null);
            }
        } else if ("content".equalsIgnoreCase(uri.getScheme())) {
            // 如果是content类型的Uri，则使用普通方法处理
            imagePath = getImagePath(uri, null);
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            // 如果是file类型的Uri，直接获取图片路径
            imagePath = uri.getPath();
        }
        displayImage(imagePath); // 根据图片路径显示图图片
    }

    private void handleImageBeforeKitKat(Intent data) {
        Uri uri = data.getData();
        imagePath = getImagePath(uri, null);
        displayImage(imagePath);
    }

    private String getImagePath(Uri uri, String selection) {
        String path = null;
        // 通过Uri和selection来获取真实图片路径
        Cursor cursor = getContentResolver().query(uri, null, selection, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst())
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            cursor.close();
        }
        return path;
    }

    private void displayImage(String imagePath) {
        if (imagePath != null) {
            Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
            userImage.setImageBitmap(bitmap);
        } else {
            Toast.makeText(this, "Filed to get image", Toast.LENGTH_SHORT);
        }
    }

}
