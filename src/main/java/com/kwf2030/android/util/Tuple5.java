package com.kwf2030.android.util;

import android.support.annotation.NonNull;

public class Tuple5<T1, T2, T3, T4, T5> {
  public final T1 data1;

  public final T2 data2;

  public final T3 data3;

  public final T4 data4;

  public final T5 data5;

  Tuple5(@NonNull T1 data1, @NonNull T2 data2, @NonNull T3 data3, @NonNull T4 data4, @NonNull T5 data5) {
    this.data1 = data1;
    this.data2 = data2;
    this.data3 = data3;
    this.data4 = data4;
    this.data5 = data5;
  }
}
