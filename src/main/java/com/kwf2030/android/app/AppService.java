package com.kwf2030.android.app;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

public class AppService extends Service {
  @Nullable
  @Override
  public IBinder onBind(Intent intent) {
    return null;
  }
}
