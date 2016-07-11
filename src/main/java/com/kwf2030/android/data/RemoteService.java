package com.kwf2030.android.data;

import android.support.annotation.NonNull;
import com.kwf2030.android.app.AppData;
import com.kwf2030.android.data.remote.UserApi;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.schedulers.Schedulers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public final class RemoteService {
  private static final String PREF_DOMAIN = "pref_domain";

  private static final String DOMAIN_PROD = "http://api.xxx.xxx/";
  private static final String DOMAIN_TEST = "http://api.xxx.xxx:8000/";

  private final Map<String, Object> mServices = new HashMap<>();

  private String mAddress = AppData.getInstance().get(PREF_DOMAIN, DOMAIN_PROD);

  public RemoteService() {
    init();
  }

  public void switchToTestAddress() {
    if (!isTestAddress()) {
      mAddress = DOMAIN_TEST;
      AppData.getInstance().set(PREF_DOMAIN, mAddress);
      init();
    }
  }

  public void switchToProdAddress() {
    if (isTestAddress()) {
      mAddress = DOMAIN_PROD;
      AppData.getInstance().set(PREF_DOMAIN, mAddress);
      init();
    }
  }

  public boolean isTestAddress() {
    return DOMAIN_TEST.equalsIgnoreCase(mAddress);
  }

  @SuppressWarnings("unchecked")
  @NonNull
  public <T> T get(@NonNull String name) {
    Object service = mServices.get(name);
    if (service == null) {
      throw new NullPointerException("specified service not found");
    }
    return (T) service;
  }

  private void init() {
    //添加cookie，登录的时候没有cookie服务器验证不通过
    OkHttpClient client = new OkHttpClient.Builder()
      .readTimeout(1000, TimeUnit.MILLISECONDS)
      .connectTimeout(1000, TimeUnit.MILLISECONDS)
      //.cookieJar(new PersistentCookieJar(new SetCookieCache(), new SharedPrefsCookiePersistor(App.getContext())))
      .build();

    Retrofit retrofit = new Retrofit.Builder()
      .baseUrl(mAddress)
      .client(client)
      .addConverterFactory(GsonConverterFactory.create())
      .addCallAdapterFactory(RxJavaCallAdapterFactory.createWithScheduler(Schedulers.io()))
      .build();

    mServices.clear();

    for (Class c : apiList()) {
      mServices.put(c.getName(), retrofit.create(c));
    }
  }

  @NonNull
  private List<Class> apiList() {
    List<Class> list = new ArrayList<>();

    //Api
    list.add(UserApi.class);
    return list;
  }
}
