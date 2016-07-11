package com.kwf2030.android.data.local;

public final class DataHolder {
  private static DataHolder sInstance = new DataHolder();

  private DataHolder() {}

  public static DataHolder getInstance() {
    return sInstance;
  }
}
