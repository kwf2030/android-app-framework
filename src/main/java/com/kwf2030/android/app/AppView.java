package com.kwf2030.android.app;

public interface AppView<P extends AppPresenter> {
  @Nullable
  P getPresenter();

  void showProgressIndicator();

  void hideProgressIndicator();

  void showLoading(@NonNull String message);

  void hideLoading();

  void showToast(@NonNull String message);
}
