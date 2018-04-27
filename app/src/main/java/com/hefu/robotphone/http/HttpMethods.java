package com.hefu.robotphone.http;


import hefu.robotphone.sdk.bean.VersionBean;
import retrofit2.http.GET;
import rx.Observable;

public interface HttpMethods {

    @GET("5aa65ba2548b7a679e3cf952?api_token=38fb1bef224fa94b0fbe3ab82a4f4196&type=android")
    Observable<VersionBean> getVersionInfos();
}
