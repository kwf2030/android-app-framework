package com.kwf2030.android.util;

import android.support.annotation.NonNull;

public class Tuple1<T> {
  public final T data;

  Tuple1(@NonNull T data) {
    this.data = data;
  }
}
