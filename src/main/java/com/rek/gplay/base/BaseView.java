package com.rek.gplay.base;

import java.util.List;

public interface BaseView<T> {

    void initView();

    void requestData();

    void showData(T data);

    void showNoData();

    void showError(String msg);

}
