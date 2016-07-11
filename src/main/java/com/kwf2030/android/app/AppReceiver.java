package com.kwf2030.android.app;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.support.annotation.NonNull;
import android.util.Log;
import com.tiexing.airticket.util.Logs;

/**
 * 应用全局广播
 *
 * 不要在其他任何地方自行注册广播，所有广播统一到此类中处理
 *
 * 如果只是传递消息，使用Handler或EventBus替代，效率比广播高出很多
 *
 * 处理在dispatchAppBroadcast或dispatchSystemBroadcast中，不要在onReceive()中
 */
public class AppReceiver extends BroadcastReceiver {
  private static final String TAG = AppReceiver.class.getSimpleName();

  private String mPackageName;

  private void dispatchAppBroadcast(@NonNull String action) {
    if (IntentActions.ACTION_SAMPLE.equals(action)) {
      Logs.d(TAG, action);
    }
  }

  private void dispatchSystemBroadcast(@NonNull String action) {
    if (Intent.ACTION_BOOT_COMPLETED.equals(action)) {
      Log.d(TAG, action);

    } else if (ConnectivityManager.CONNECTIVITY_ACTION.equals(action)) {
      Logs.d(TAG, action);
    }
  }

  @Override
  public void onReceive(Context context, Intent intent) {
    Logs.d(TAG, "onReceive");

    String action = intent.getAction();

    if (action == null || action.isEmpty()) {
      return;
    }

    if (mPackageName == null || mPackageName.isEmpty()) {
      mPackageName = context.getPackageName();
    }

    if (action.startsWith(mPackageName)) {
      //应用自定义的广播
      dispatchAppBroadcast(action);

    } else {
      //系统广播
      dispatchSystemBroadcast(action);
    }
  }
}
