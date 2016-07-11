package com.kwf2030.android.app;

import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.*;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.*;
import android.widget.TextView;
import android.widget.Toast;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.BottomBarBadge;
import com.roughike.bottombar.OnMenuTabClickListener;
import com.tbruyelle.rxpermissions.Permission;
import com.tbruyelle.rxpermissions.RxPermissions;
import com.tiexing.airticket.util.Event;
import com.tiexing.airticket.util.EventBus;
import com.tiexing.airticket.util.Logs;
import com.tiexing.airticket.util.Nulls;
import com.woyaou.R;
import com.woyaou.widget.customview.dialog.LoadingDialog;
import rx.Subscription;
import rx.functions.Action1;

import java.util.List;

import static android.content.pm.PackageManager.GET_META_DATA;
import static android.text.TextUtils.isEmpty;

/**
 * 所有Activity的基类
 * <p/>
 * 子类应该继承AbstractAppActivity而不是直接继承本类
 *
 * @param <P> Presenter类型
 */
public abstract class AppActivity<P extends AppPresenter> extends AppCompatActivity implements AppView<P>, Handler.Callback {
  protected final String _tag = getClass().getSimpleName();

  private CoordinatorLayout mRootLayout;
  private AppBarLayout mAppBarLayout;
  private SwipeRefreshLayout mSwipeRefreshLayout;
  private ViewGroup mContentLayout;
  private TextView tvTitle;
  private LoadingDialog mLoadingDialog;

  private Toolbar mToolbar;
  private BottomBar mBottombar;
  private FloatingActionButton mFab;

  private ActionBar mActionBar;

  private AppHandler<? extends AppActivity<P>> mHandler;
  private Subscription mSubscription;

  protected P mPresenter;
  protected App mAppContext;
  protected AppData mAppData;
  protected FragmentManager mFragmentManager;

  private boolean onStartCalled;

  private Action1<Event> mSubscriptionAction = new Action1<Event>() {
    @Override
    public void call(Event event) {
      onEvent(Nulls.requireNonNull(event));
    }
  };

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    mPresenter = getPresenter();
    mAppContext = App.getContext();
    mAppData = AppData.getInstance();
    mFragmentManager = getSupportFragmentManager();

    setContentView(R.layout.activity_root);

    //获取Intent供之后处理参数
    processIntent();

    //初始化Appbar/SwipeRefresh/Bottombar/FloatingActionButton等
    configureViews(savedInstanceState);

    //添加布局文件
    attachActivityLayout();

    //初始化布局文件
    initViews();
  }

  @Override
  protected void onNewIntent(Intent intent) {
    super.onNewIntent(intent);
  }

  @Override
  protected void onRestart() {
    super.onRestart();
  }

  @Override
  protected void onStart() {
    super.onStart();
    initMessenger();
    if (!onStartCalled) {
      onStartCalled = true;
      initWork();
    }
  }

  @Override
  protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
    super.onRestoreInstanceState(savedInstanceState);
  }

  @Override
  protected void onResume() {
    super.onResume();
  }

  @Override
  protected void onPause() {
    super.onPause();
  }

  @Override
  protected void onStop() {
    super.onStop();
    releaseMessenger();
  }

  @Override
  protected void onSaveInstanceState(@NonNull Bundle outState) {
    super.onSaveInstanceState(outState);
    mBottombar.onSaveInstanceState(outState);
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    // App.getRefWatcher().watch(this);
  }

  @Override
  public boolean onCreateOptionsMenu(@NonNull Menu menu) {
    int res = getToolbarMenu();
    if (res != 0) {
      getMenuInflater().inflate(res, menu);
    }
    return super.onCreateOptionsMenu(menu);
  }

  @Override
  public boolean onOptionsItemSelected(@NonNull MenuItem item) {
    onToolbarMenuClick(item);
    return super.onOptionsItemSelected(item);
  }

  @Override
  public void onBackPressed() {
    super.onBackPressed();
  }

  @SuppressWarnings("unchecked")
  public <T extends View> T $(@IdRes int resId) {
    return (T) findViewById(resId);
  }

  @SuppressWarnings("unchecked")
  public <T extends View> T $(@NonNull View view, @IdRes int resId) {
    return (T) view.findViewById(resId);
  }

  public int getColour(@ColorRes int resId) {
    return ContextCompat.getColor(this, resId);
  }

  public void checkPermissions(@NonNull String... permissions) {
    RxPermissions.getInstance(this).requestEach(permissions).subscribe(new Action1<Permission>() {
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

  public void post(int what) {
    if (mHandler != null) {
      mHandler.sendEmptyMessage(what);
    }
  }

  public void post(int what, int arg1, int arg2) {
    if (mHandler != null) {
      Message.obtain(mHandler, what, arg1, arg2).sendToTarget();
    }
  }

  public void post(@NonNull Message msg) {
    Message copy = Message.obtain();
    copy.copyFrom(msg);
    if (mHandler != null) {
      mHandler.sendMessage(copy);
    }
  }

  public void showToast(@NonNull String text) {
    Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
  }

  public void showSnackbar(@NonNull String text) {
    Snackbar.make(mFab, text, Snackbar.LENGTH_SHORT).show();
  }

  public void showSnackbar(@NonNull String text, @NonNull String action, @NonNull View.OnClickListener listener) {
    Snackbar sb = Snackbar.make(mFab, text, Snackbar.LENGTH_LONG);
    sb.setAction(action, listener);
  }

  private void processIntent() {
    processArguments(getIntent().getExtras());
  }

  private boolean attachActivityLayout() {
    return attachActivityResource() || attachActivityView() || attachActivityFragments();
  }

  private boolean attachActivityResource() {
    int res = getLayoutResource();
    if (res != 0) {
      LayoutInflater.from(this).inflate(res, mContentLayout, true);
      return true;
    }
    return false;
  }

  private boolean attachActivityView() {
    View content = getLayoutView();
    if (content != null) {
      mContentLayout.addView(content);
      return true;
    }
    return false;
  }

  private boolean attachActivityFragments() {
    List<? extends AppFragment<? extends AppPresenter>> fragments = getFragments();
    if (Nulls.isEmpty(fragments)) {
      return false;
    }

    for (AppFragment<? extends AppPresenter> fragment : fragments) {
      mFragmentManager.beginTransaction()
          .add(R.id.content_layout, fragment, fragment.getClass().getSimpleName())
          .commit();
    }
    return true;
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
      mSubscription = null;
    }
  }

  private void configureViews(@Nullable Bundle savedInstanceState) {
    mRootLayout = $(R.id.root_layout);
    initAppBarLayout();
    initSwipeRefreshLayout();
    initContentLayout();
    initToolbar();
    initBottombar(savedInstanceState);
    initFloatingActionButton();
  }

  /* ==================== AppBarLayout ==================== */
  private void initAppBarLayout() {
    mAppBarLayout = $(R.id.appbar_layout);
  }

  public void attachToAppbar(View view) {
    mAppBarLayout.addView(view);
  }

  /* ==================== SwipeRefreshLayout ==================== */
  private void initSwipeRefreshLayout() {
    mSwipeRefreshLayout = $(R.id.swipe_refresh_layout);
    mSwipeRefreshLayout.setEnabled(shouldEnableSwipeRefresh());
    mSwipeRefreshLayout.setColorSchemeResources(R.color.colorPrimaryDark);
    mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
      @Override
      public void onRefresh() {
        onSwipeRefresh();
      }
    });
  }

  public void setSwipeRefreshLayoutEnabled(boolean enabled) {
    mSwipeRefreshLayout.setEnabled(enabled);
  }

  /**
   * 获取资源id
   */
  public int getColorRes(int id) {
    return ContextCompat.getColor(mAppContext, id);
  }

  @Override
  public void showProgressIndicator() {
    runOnUiThread(new Runnable() {
      @Override
      public void run() {
        mSwipeRefreshLayout.setRefreshing(true);
      }
    });
  }

  @Override
  public void hideProgressIndicator() {
    runOnUiThread(new Runnable() {
      @Override
      public void run() {
        mSwipeRefreshLayout.setRefreshing(false);
      }
    });
  }

  /* ==================== ContentLayout ==================== */
  private void initContentLayout() {
    mContentLayout = $(R.id.content_layout);
  }

  /* ==================== Toolbar ==================== */
  private void initToolbar() {
    tvTitle = $(R.id.tv_title);
    mToolbar = $(R.id.toolbar);
    setSupportActionBar(mToolbar);

    mActionBar = getSupportActionBar();
    if (mActionBar == null) {
      throw new RuntimeException("getSupportActionBar() returns null");
    }
    mActionBar.setDisplayShowTitleEnabled(false);
    CharSequence title = getToolbarTitle();
    if (!isEmpty(title)) {
      setToolbarTitle(title);
      String subtitle = getToolbarSubtitle();
      if (!isEmpty(subtitle)) {
        setToolbarSubtitle(subtitle);
      }
    } else {
      ActivityInfo info = null;
      try {
        info = getPackageManager().getActivityInfo(new ComponentName(this, this.getClass()), GET_META_DATA);
      } catch (PackageManager.NameNotFoundException e) {
        e.printStackTrace();
      }
      if (info != null && info.labelRes != 0) {
        setToolbarTitle(getString(info.labelRes));
      }
    }

    int logo = getToolbarLogo();
    if (logo != 0) {
      setToolbarLogo(logo);
    }

    toggleUpButton(shouldShowToolbarBackButton());
    toggleToolbar(shouldShowToolbar());

    mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        onToolbarBack();
      }
    });
  }

  public void toggleToolbar(boolean show) {
    if (show && mToolbar.getVisibility() == View.GONE) {
      mToolbar.setVisibility(View.VISIBLE);
    } else if (!show && mToolbar.getVisibility() == View.VISIBLE) {
      mToolbar.setVisibility(View.GONE);
    }
  }

  public void setToolbarTitle(@NonNull CharSequence title) {
//    mActionBar.setTitle(title);
    tvTitle.setText(title);
  }

  public void setToolbarSubtitle(@NonNull CharSequence subtitle) {
    mActionBar.setSubtitle(subtitle);
  }

  public void setToolbarLogo(@DrawableRes int resId) {
    mActionBar.setLogo(resId);
  }

  public void toggleUpButton(boolean show) {
    mActionBar.setDisplayHomeAsUpEnabled(show);
  }

  /* ==================== Bottombar ==================== */
  private void initBottombar(@Nullable Bundle savedInstanceState) {
    //TODO attachShy will cause Toolbar invisible
    // mBottombar = BottomBar.attachShy(mRootLayout, mContentLayout, savedInstanceState);
    mBottombar = BottomBar.attach(this, savedInstanceState);
    mBottombar.noTopOffset();
    mBottombar.noNavBarGoodness();
    mBottombar.noTabletGoodness();
    mBottombar.useFixedMode();
    mBottombar.hideShadow();
    mBottombar.setBackgroundColor(getColour(R.color.white));
    mBottombar.setItems(R.menu.menu_bottom_bar);
    mBottombar.setOnMenuTabClickListener(new OnMenuTabClickListener() {
      @Override
      public void onMenuTabSelected(@MenuRes int menuItemId) {
        onBottombarSelected(menuItemId);
      }

      @Override
      public void onMenuTabReSelected(@MenuRes int menuItemId) {
        onBottombarReSelected(menuItemId);
      }
    });
    toggleBottombar(shouldShowBottombar());
  }

  public void toggleBottombar(boolean show) {
    if (show && mBottombar.getVisibility() != View.VISIBLE) {
      mBottombar.show();
    } else if (!show && mBottombar.getVisibility() == View.VISIBLE) {
      mBottombar.hide();
    }
  }

  public void setBottombarBadge(int index, @ColorInt int color, int num) {
    BottomBarBadge badge = mBottombar.makeBadgeForTabAt(index, color, num);
    badge.setAutoShowAfterUnSelection(true);
    badge.show();
  }

  /* ==================== FloatingActionButton ==================== */
  private void initFloatingActionButton() {
    mFab = $(R.id.fab);
    mFab.setBackgroundResource(getFloatingActionButtonResource());
    mFab.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        onFloatingActionButtonClick();
      }
    });
    toggleFloatingActionButton(shouldShowFloatingActionButton());
  }

  public void toggleFloatingActionButton(boolean show) {
    if (show && !mFab.isShown()) {
      mFab.show();
    } else if (!show && mFab.isShown()) {
      mFab.hide();
    }
  }

  @Override
  public void showLoading(@NonNull final String message) {
    runOnUiThread(new Runnable() {
      @Override
      public void run() {
        if (mLoadingDialog == null) {
          mLoadingDialog = new LoadingDialog(AppActivity.this);
        }
        if (!Nulls.isEmpty(message)) {
          mLoadingDialog.setTitle(message);
        }
        if (!mLoadingDialog.isShowing()) {
          mLoadingDialog.show();
        }
      }
    });
  }

  @Override
  public void hideLoading() {
    runOnUiThread(new Runnable() {
      @Override
      public void run() {
        if (mLoadingDialog != null && mLoadingDialog.isShowing()) {
          mLoadingDialog.dismiss();
        }
      }
    });
  }

  /* ==================== Abstract methods should be overridden ==================== */

  @Nullable
  @Override
  public abstract P getPresenter();

  @LayoutRes
  protected abstract int getLayoutResource();

  @Nullable
  protected abstract View getLayoutView();

  @NonNull
  protected abstract List<? extends AppFragment<? extends AppPresenter>> getFragments();

  protected abstract void initViews();

  protected abstract void initWork();

  protected abstract boolean shouldReInitWork();

  protected abstract void processArguments(@Nullable Bundle args);

  protected abstract boolean shouldEnableSwipeRefresh();

  protected abstract void onSwipeRefresh();

  protected abstract boolean shouldShowToolbar();

  @DrawableRes
  protected abstract int getToolbarLogo();

  @NonNull
  protected abstract String getToolbarTitle();

  @NonNull
  protected abstract String getToolbarSubtitle();

  @MenuRes
  protected abstract int getToolbarMenu();

  protected abstract boolean shouldShowToolbarBackButton();

  protected abstract void onToolbarMenuClick(@NonNull MenuItem item);

  protected abstract void onToolbarBack();

  protected abstract boolean shouldShowBottombar();

  protected abstract void onBottombarSelected(int menuItemId);

  protected abstract void onBottombarReSelected(int menuItemId);

  protected abstract boolean shouldShowFloatingActionButton();

  @DrawableRes
  protected abstract int getFloatingActionButtonResource();

  protected abstract void onFloatingActionButtonClick();

  protected abstract void onPermissionGranted(@NonNull String permission);

  protected abstract void onPermissionDenied(@NonNull String permission);

  @Override
  public abstract boolean handleMessage(Message msg);

  protected abstract void onEvent(@NonNull Event event);
}
