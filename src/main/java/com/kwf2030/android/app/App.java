package com.kwf2030.android.app;

import android.app.Application;
import android.content.IntentFilter;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import com.kwf2030.android.BuildConfig;
import com.kwf2030.android.data.LocalDatabase;
import com.kwf2030.android.data.LocalPreference;
import com.kwf2030.android.data.RemoteService;
import com.kwf2030.android.data.local.TxDatabaseSource;
import io.requery.Persistable;
import io.requery.android.sqlite.DatabaseSource;
import io.requery.sql.EntityDataStore;
import io.requery.sql.TableCreationMode;

public class App extends Application {
  public static final boolean DEBUG = BuildConfig.DEBUG;

  private static App sInstance;

  //版本信息
  private static String sVersionName;
  private static int sVersionCode;

  //内存泄露监控器
  private static RefWatcher sRefWatcher;

  @NonNull
  public static App getContext() {
    return sInstance;
  }

  @NonNull
  public static String getVersionName() {
    return sVersionName == null ? "null" : sVersionName;
  }

  public static int getVersionCode() {
    return sVersionCode;
  }

  static RefWatcher getRefWatcher() {
    return sRefWatcher;
  }

  @Override
  public void onCreate() {
    super.onCreate();
    sInstance = this;

    try {
      PackageInfo pkg = sInstance.getPackageManager().getPackageInfo(sInstance.getPackageName(), PackageManager.GET_ACTIVITIES);
      sVersionName = pkg.versionName;
      sVersionCode = pkg.versionCode;

    } catch (PackageManager.NameNotFoundException e) {
      e.printStackTrace();
    }

    //设置未捕获异常处理器
    Thread.setDefaultUncaughtExceptionHandler(AppExceptionHandler.create());

    //注册Activity生命周期回调函数
    registerActivityLifecycleCallbacks(AppActivityLifecycleCallbacks.create());

    //注册组件处理器，用于内存不足时释放资源
    registerComponentCallbacks(AppComponentCallbacks.create());

    //初始化数据管理器
    initAppData();

    //注册全局广播
    initBroadcastReceiver();

    //内存泄漏监控
    //sRefWatcher = LeakCanary.install(this);
  }

  private void initAppData() {
    AppData appData = AppData.getInstance();
    LocalPreference preference = new LocalPreference(this);
    appData.setPreference(preference);

    RemoteService service = new RemoteService();
    appData.setService(service);

    String dbName = "tx_airticket";
    int dbVersion = 1;
    boolean dbLog = true;

    try {
      ApplicationInfo appInfo = getPackageManager().getApplicationInfo(getPackageName(), PackageManager.GET_META_DATA);
      dbName = appInfo.metaData.getString("database_name");
      dbVersion = appInfo.metaData.getInt("database_version");
      dbLog = appInfo.metaData.getBoolean("database_log");

    } catch (PackageManager.NameNotFoundException e) {
      e.printStackTrace();
    }

    DatabaseSource source = new TxDatabaseSource(this, Models.DEFAULT, dbName, dbVersion);
    source.setLoggingEnabled(dbLog);

    if (DEBUG) {
      source.setTableCreationMode(TableCreationMode.DROP_CREATE);
    } else {
      source.setTableCreationMode(TableCreationMode.CREATE_NOT_EXISTS);
    }

    EntityDataStore<Persistable> dataStore = new EntityDataStore<>(source.getConfiguration());
    LocalDatabase database = new LocalDatabase(dataStore, dbName, dbVersion, dbLog);
    appData.setDatabase(database);
  }

  private void initBroadcastReceiver() {
    IntentFilter filter = new IntentFilter();
    for (String action : IntentActions.getActions()) {
      if (!TextUtils.isEmpty(action)) {
        filter.addAction(action);
      }
    }
    registerReceiver(new AppReceiver(), filter);
  }
}
