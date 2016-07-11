package com.kwf2030.android.business;

import android.os.Environment;
import com.kwf2030.android.app.App;

public class BusinessApp extends App {
  //数据保存目录
  public static final String APP_DIR = Environment.getExternalStorageDirectory().getPath() + "/114/air/";

  //缓存目录
  public final static String TMP_DIR = APP_DIR + ".temp/";

  @Override
  public void onCreate() {
    super.onCreate();
  }
}
