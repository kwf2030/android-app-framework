package com.kwf2030.android.app;

import android.os.Bundle;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.kwf2030.android.util.Event;

public class AbstractAppFragment<P extends AppPresenter> extends AppFragment<P> {
  @Nullable
  @Override
  public P getPresenter() {
    return null;
  }

  @Override
  protected int getLayoutResource() {
    return 0;
  }

  @Override
  protected void processArguments(@Nullable Bundle args) {
  }

  @Override
  protected void initViews() {
  }

  @Override
  protected void initWork() {
  }

  @Override
  protected boolean shouldReInitWork() {
    return false;
  }

  @Override
  protected void onSwipeRefresh() {
  }

  @Override
  protected void onPermissionGranted(@NonNull String permission) {
  }

  @Override
  protected void onPermissionDenied(@NonNull String permission) {
  }

  @Override
  public boolean handleMessage(Message msg) {
    return true;
  }

  @Override
  protected <E> void onEvent(@NonNull Event<E> event) {
  }
}
