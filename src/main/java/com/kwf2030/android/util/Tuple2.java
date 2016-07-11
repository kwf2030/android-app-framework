package com.kwf2030.android.util;

import android.support.annotation.NonNull;

public class Tuple2<T1, T2> {
  public final T1 data1;

  public final T2 data2;

  Tuple2(@NonNull T1 data1, @NonNull T2 data2) {
    this.data1 = data1;
    this.data2 = data2;
  }
}
