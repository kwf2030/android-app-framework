package com.kwf2030.android.util;

import android.support.annotation.NonNull;

public class Tuple3<T1, T2, T3> {
  public final T1 data1;

  public final T2 data2;

  public final T3 data3;

  Tuple3(@NonNull T1 data1, @NonNull T2 data2, @NonNull T3 data3) {
    this.data1 = data1;
    this.data2 = data2;
    this.data3 = data3;
  }
}
