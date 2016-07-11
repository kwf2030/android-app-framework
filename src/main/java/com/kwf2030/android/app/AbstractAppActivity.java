package com.kwf2030.android.app;

import android.os.Bundle;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.MenuItem;
import android.view.View;

import java.util.Collections;
import java.util.List;

public class AbstractAppActivity<P extends AppPresenter> extends AppActivity<P> {
  @Nullable
  @Override
  public P getPresenter() {
    return null;
  }

  @Override
  protected int getLayoutResource() {
    return 0;
  }

  @Nullable
  @Override
  protected View getLayoutView() {
    return null;
  }

  @NonNull
  @Override
  protected List<? extends AppFragment<? extends AppPresenter>> getFragments() {
    return Collections.emptyList();
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
  protected void processArguments(@Nullable Bundle args) {
  }

  @Override
  protected boolean shouldEnableSwipeRefresh() {
    return false;
  }

  @Override
  protected void onSwipeRefresh() {
    List<Fragment> fragments = mFragmentManager.getFragments();
    for (Fragment fragment : fragments) {
      ((AppFragment) fragment).onSwipeRefresh();
    }
  }

  @Override
  protected boolean shouldShowToolbar() {
    return true;
  }

  @Override
  protected int getToolbarLogo() {
    return 0;
  }

  @NonNull
  @Override
  protected String getToolbarTitle() {
    return "";
  }

  @NonNull
  @Override
  protected String getToolbarSubtitle() {
    return "";
  }

  @Override
  protected int getToolbarMenu() {
    return 0;
  }

  @Override
  protected boolean shouldShowToolbarBackButton() {
    return true;
  }

  @Override
  protected void onToolbarMenuClick(@NonNull MenuItem item) {
  }

  @Override
  protected void onToolbarBack() {
    finish();
  }

  @Override
  protected boolean shouldShowBottombar() {
    return false;
  }

  @Override
  protected void onBottombarSelected(int menuItemId) {
  }

  @Override
  protected void onBottombarReSelected(int menuItemId) {
  }

  @Override
  protected boolean shouldShowFloatingActionButton() {
    return false;
  }

  @Override
  protected int getFloatingActionButtonResource() {
    return 0;
  }

  @Override
  protected void onFloatingActionButtonClick() {
  }

  @Override
  protected void onPermissionGranted(@NonNull String permission) {
  }

  @Override
  protected void onPermissionDenied(@NonNull String permission) {
  }

  @Override
  public boolean handleMessage(@NonNull Message msg) {
    return true;
  }

  @Override
  protected void onEvent(@NonNull Event event) {
  }
}
