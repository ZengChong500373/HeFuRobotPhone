package com.hefu.robotphone.http;


import hefu.robotphone.sdk.bean.TokenBean;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;

public interface YSMethods {

    @FormUrlEncoded
    @POST("/api/lapp/token/get")
    Observable<TokenBean> rxGetToken(@Field("appKey") String appKey, @Field("appSecret") String appSecret);
}
