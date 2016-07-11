package com.kwf2030.android.app;

import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;

import java.lang.ref.WeakReference;

import static com.tiexing.airticket.util.Nulls.requireNonNull;

class AppHandler<T extends Handler.Callback> extends Handler {
  private WeakReference<T> wrapper;

  AppHandler(@NonNull T wrapper) {
    this.wrapper = new WeakReference<>(requireNonNull(wrapper));
  }

  @Override
  public void handleMessage(@NonNull Message msg) {
    requireNonNull(msg);
    if (wrapper != null) {
      T w = wrapper.get();
      if (w != null) {
        w.handleMessage(msg);
      }
    }
  }

  void release() {
    if (wrapper != null) {
      wrapper.clear();
      wrapper = null;
    }
  }
}
