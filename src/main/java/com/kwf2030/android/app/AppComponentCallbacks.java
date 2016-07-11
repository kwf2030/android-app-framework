package com.kwf2030.android.app;

import android.content.ComponentCallbacks2;
import android.content.res.Configuration;

final class AppComponentCallbacks implements ComponentCallbacks2 {
  public static AppComponentCallbacks create() {
    return new AppComponentCallbacks();
  }

  private AppComponentCallbacks() {}

  @Override
  public void onTrimMemory(int level) {
  }

  @Override
  public void onConfigurationChanged(Configuration newConfig) {

  }

  @Override
  public void onLowMemory() {
  }
}
