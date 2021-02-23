package com.rek.gplay.base;

import com.rek.gplay.bean.ResponseBean;

import rx.Observable;

public interface BaseModel<T> {

    //    void getDatas(Callback<ResponseBean<T>> callback);
    Observable<ResponseBean<T>> getDatas();

}
