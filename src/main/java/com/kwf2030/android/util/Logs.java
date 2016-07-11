package com.kwf2030.android.util;

import android.support.annotation.NonNull;
import com.kwf2030.android.BuildConfig;

public class Logs {
  private static boolean sDebug = BuildConfig.DEBUG;

  public static int log4flw(@NonNull String msg) {
    return Logs.d("TAG_FLW", msg);
  }

  public static int v(@NonNull String tag, @NonNull String msg) {
    return sDebug ? android.util.Log.v(tag, msg) : 0;
  }

  public static int w(@NonNull String tag, @NonNull String msg) {
    return sDebug ? android.util.Log.w(tag, msg) : 0;
  }

  public static int e(@NonNull String tag, @NonNull String msg) {
    return sDebug ? android.util.Log.e(tag, msg) : 0;
  }

  public static int i(@NonNull String tag, @NonNull String msg) {
    return sDebug ? android.util.Log.i(tag, msg) : 0;
  }

  public static int e(@NonNull String tag, @NonNull String msg, Throwable tr) {
    return sDebug ? android.util.Log.e(tag, msg, tr) : 0;
  }

  public static int d(@NonNull String tag, @NonNull String msg, Throwable tr) {
    return sDebug ? android.util.Log.d(tag, msg, tr) : 0;
  }

  public static int d(@NonNull String tag, @NonNull String msg) {
    return sDebug ? android.util.Log.d(tag, msg) : 0;
  }

  public static int w(@NonNull String tag, Throwable tr) {
    return sDebug ? android.util.Log.w(tag, tr) : 0;
  }

  public static int i(@NonNull String tag, @NonNull String msg, Throwable tr) {
    return sDebug ? android.util.Log.i(tag, msg, tr) : 0;
  }

  public static int w(@NonNull String tag, @NonNull String msg, Throwable tr) {
    return sDebug ? android.util.Log.w(tag, msg, tr) : 0;
  }

  public static int v(@NonNull String tag, @NonNull String msg, Throwable tr) {
    return sDebug ? android.util.Log.v(tag, msg, tr) : 0;
  }

  public static int wtf(@NonNull String tag, Throwable tr) {
    return sDebug ? android.util.Log.wtf(tag, tr) : 0;
  }

  public static int wtf(@NonNull String tag, @NonNull String msg) {
    return sDebug ? android.util.Log.wtf(tag, msg) : 0;
  }

  public static int wtf(@NonNull String tag, @NonNull String msg, Throwable tr) {
    return sDebug ? android.util.Log.wtf(tag, msg, tr) : 0;
  }

  public static int println(int priority, @NonNull String tag, @NonNull String msg) {
    return sDebug ? android.util.Log.println(priority, tag, msg) : 0;
  }

  public static boolean isLoggable(@NonNull String s, int i) {
    return android.util.Log.isLoggable(s, i);
  }

  @NonNull
  public static String getStackTraceString(Throwable tr) {
    return sDebug ? android.util.Log.getStackTraceString(tr) : "";
  }

  public static void setLoggable(boolean loggable) {
    sDebug = loggable;
  }
}
