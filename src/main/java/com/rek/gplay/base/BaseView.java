package com.rek.gplay.base;

import java.util.List;

public interface BaseView<T> {

    void showData(T data);

    void showNoData();

    void showError(String msg);

}