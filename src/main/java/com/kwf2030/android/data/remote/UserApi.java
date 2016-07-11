package com.kwf2030.android.data.remote;

import retrofit2.Response;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Header;
import retrofit2.http.POST;
import rx.Observable;

import java.util.Map;

public interface UserApi {
  @FormUrlEncoded
  @POST("/xxx/xxx")
  Observable<Response<String>> load(@FieldMap Map<String, String> params, @Header("token") String token);
}
