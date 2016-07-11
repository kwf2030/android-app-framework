package com.kwf2030.android.app;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

final class AppActivityLifecycleCallbacks implements Application.ActivityLifecycleCallbacks {
  public static AppActivityLifecycleCallbacks create() {
    return new AppActivityLifecycleCallbacks();
  }

  private AppActivityLifecycleCallbacks() {}

  @Override
  public void onActivityCreated(Activity activity, Bundle savedInstanceState) {

  }

  @Override
  public void onActivityStarted(Activity activity) {

  }

  @Override
  public void onActivityResumed(Activity activity) {

  }

  @Override
  public void onActivityPaused(Activity activity) {

  }

  @Override
  public void onActivityStopped(Activity activity) {

  }

  @Override
  public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

  }

  @Override
  public void onActivityDestroyed(Activity activity) {

  }
}
