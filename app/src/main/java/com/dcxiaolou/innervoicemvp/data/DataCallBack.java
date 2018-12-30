package com.dcxiaolou.innervoicemvp.data;

public interface DataCallBack<T> {

    //获取数据成功
    void onSuccess(T data);

    //获取数据失败
    void onFail(String message);

}
