package com.rek.gplay.base;

import com.rek.gplay.bean.ResponseBean;

import rx.Observable;

public interface BaseModel<T> {

    Observable<ResponseBean<T>> getData();

}
