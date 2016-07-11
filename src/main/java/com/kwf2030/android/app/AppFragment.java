package com.kwf2030.android.app;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.*;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.tbruyelle.rxpermissions.Permission;
import com.tbruyelle.rxpermissions.RxPermissions;
import com.tiexing.airticket.util.Event;
import com.tiexing.airticket.util.EventBus;
import com.tiexing.airticket.util.Logs;
import com.tiexing.airticket.util.Nulls;
import rx.Subscription;
import rx.functions.Action1;

public abstract class AppFragment<P extends AppPresenter> extends Fragment implements AppView<P>, Handler.Callback {
  protected final String _tag = getClass().getSimpleName();

  private View mRootView;

  private AppHandler<? extends AppFragment<P>> mHandler;
  private Subscription mSubscription;

  protected P mPresenter;
  protected App mAppContext;
  protected AppData mAppData;
  protected AppActivity mActivity;
  protected FragmentManager mFragmentManager;

  private boolean onStartCalled;

  private Action1<Event> mSubscriptionAction = new Action1<Event>() {
    @Override
    public void call(Event event) {
      onEvent(Nulls.requireNonNull(event));
    }
  };

  @Override
  public void onAttach(Context context) {
    super.onAttach(context);
    mPresenter = getPresenter();
    mAppContext = App.getContext();
    mAppData = AppData.getInstance();
    if (context instanceof AppActivity) {
      mActivity = (AppActivity) context;
      mFragmentManager = mActivity.getSupportFragmentManager();
    }
  }

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    processArguments(getArguments());
  }

  @Override
  @Nullable
  public View onCreateView(LayoutInflater inflater, ViewGroup container, @Nullable Bundle savedInstanceState) {
    mRootView = inflater.inflate(getLayoutResource(), container, false);
    initViews();
    return mRootView;
  }

  @Override
  public void onActivityCreated(@Nullable Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);
  }

  @Override
  public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
    super.onViewStateRestored(savedInstanceState);
  }

  @Override
  public void onStart() {
    super.onStart();
    initMessenger();

    if (!onStartCalled || shouldReInitWork()) {
      onStartCalled = true;
      initWork();
    }
  }

  @Override
  public void onResume() {
    super.onResume();
  }

  @Override
  public void onPause() {
    super.onPause();
  }

  @Override
  public void onStop() {
    super.onStop();
    releaseMessenger();
  }

  @Override
  public void onDestroyView() {
    super.onDestroyView();
  }

  @Override
  public void onSaveInstanceState(@NonNull Bundle outState) {
    super.onSaveInstanceState(outState);
  }

  @Override
  public void onDestroy() {
    super.onDestroy();
    // App.getRefWatcher().watch(this);
  }

  @Override
  public void onDetach() {
    super.onDetach();
  }

  @SuppressWarnings("unchecked")
  protected <T extends View> T $(@IdRes int resId) {
    return (T) mRootView.findViewById(resId);
  }

  @SuppressWarnings("unchecked")
  protected <T extends View> T $(View view, @IdRes int resId) {
    return (T) view.findViewById(resId);
  }

  public void checkPermissions(@NonNull String... permissions) {
    RxPermissions.getInstance(mActivity).requestEach(permissions).subscribe(new Action1<Permission>() {
      @Override
      public void call(Permission permission) {
        String name = permission.name;
        if (permission.granted) {
          onPermissionGranted(name);
        } else {
          onPermissionDenied(name);
        }
      }

    }, new Action1<Throwable>() {
      @Override
      public void call(Throwable throwable) {
        Logs.e("Permission", "permission check failed");
      }
    });
  }

  public int getColour(@ColorRes int resId) {
    return mActivity.getColour(resId);
  }

  @Override
  public void showToast(@NonNull String text) {
    mActivity.showToast(text);
  }

  public void showSnackbar(@NonNull String text) {
    mActivity.showSnackbar(text);
  }

  public void showSnackbar(@NonNull String text, @NonNull String action, @NonNull View.OnClickListener listener) {
    mActivity.showSnackbar(text, action, listener);
  }

  public void attachToAppbar(View view) {
    mActivity.attachToAppbar(view);
  }

  public void setSwipeRefreshLayoutEnabled(boolean enabled) {
    mActivity.setSwipeRefreshLayoutEnabled(enabled);
  }

  @Override
  public void showProgressIndicator() {
    mActivity.showProgressIndicator();
  }

  @Override
  public void hideProgressIndicator() {
    mActivity.hideProgressIndicator();
  }

  public void toggleToolbar(boolean show) {
    mActivity.toggleToolbar(show);
  }

  public void setToolbarTitle(@NonNull CharSequence title) {
    mActivity.setToolbarTitle(title);
  }

  public void setToolbarSubtitle(@NonNull CharSequence subtitle) {
    mActivity.setToolbarSubtitle(subtitle);
  }

  public void setToolbarLogo(@DrawableRes int resId) {
    mActivity.setToolbarLogo(resId);
  }

  public void toggleUpButton(boolean show) {
    mActivity.toggleUpButton(show);
  }

  public void toggleBottombar(boolean show) {
    mActivity.toggleBottombar(show);
  }

  public void setBottombarBadge(int index, @ColorInt int color, int num) {
    mActivity.setBottombarBadge(index, color, num);
  }

  public void toggleFloatingActionButton(boolean show) {
    mActivity.toggleFloatingActionButton(show);
  }

  @Override
  public void showLoading(@NonNull String message) {
    mActivity.showLoading(message);
  }

  @Override
  public void hideLoading() {
    mActivity.hideLoading();
  }

  protected void post(int what) {
    if (mHandler != null) {
      mHandler.sendEmptyMessage(what);
    }
  }

  protected void post(int what, int arg1, int arg2) {
    if (mHandler != null) {
      Message.obtain(mHandler, what, arg1, arg2).sendToTarget();
    }
  }

  protected void post(@NonNull Message msg) {
    Message copy = Message.obtain();
    copy.copyFrom(msg);
    if (mHandler != null) {
      mHandler.sendMessage(copy);
    }
  }

  private void initMessenger() {
    if (mHandler == null) {
      mHandler = new AppHandler<>(this);
    }

    if (mSubscription == null) {
      mSubscription = EventBus.observable().subscribe(mSubscriptionAction);
    }
  }

  private void releaseMessenger() {
    if (mHandler != null) {
      mHandler.removeCallbacksAndMessages(null);
      mHandler.release();
      mHandler = null;
    }

    if (mSubscription != null) {
      mSubscription.unsubscribe();
    }
  }

  @Nullable
  @Override
  public abstract P getPresenter();

  @LayoutRes
  protected abstract int getLayoutResource();

  protected abstract void processArguments(@Nullable Bundle args);

  protected abstract void initViews();

  protected abstract void initWork();

  protected abstract boolean shouldReInitWork();

  protected abstract void onSwipeRefresh();

  protected abstract void onPermissionGranted(@NonNull String permission);

  protected abstract void onPermissionDenied(@NonNull String permission);

  @Override
  public abstract boolean handleMessage(Message msg);

  protected abstract <E> void onEvent(@NonNull Event<E> event);
}
