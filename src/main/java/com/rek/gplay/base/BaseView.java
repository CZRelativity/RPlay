package com.rek.gplay.base;

public interface BaseView<T> {

    void showData(T data);

    void showNoData();

    void showError(String msg);

}
