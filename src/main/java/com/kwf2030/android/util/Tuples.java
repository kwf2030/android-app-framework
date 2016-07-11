package com.kwf2030.android.util;

import android.support.annotation.NonNull;

import static com.kwf2030.android.util.Nulls.requireNonNull;

public class Tuples {
  @NonNull
  public static <T> Tuple1<T> of(@NonNull T data) {
    return new Tuple1<>(requireNonNull(data));
  }

  @NonNull
  public static <T1, T2> Tuple2<T1, T2> of(@NonNull T1 data1, @NonNull T2 data2) {
    return new Tuple2<>(requireNonNull(data1), requireNonNull(data2));
  }

  @NonNull
  public static <T1, T2, T3> Tuple3<T1, T2, T3> of(@NonNull T1 data1, @NonNull T2 data2, @NonNull T3 data3) {
    return new Tuple3<>(requireNonNull(data1), requireNonNull(data2), requireNonNull(data3));
  }

  @NonNull
  public static <T1, T2, T3, T4> Tuple4<T1, T2, T3, T4> of(@NonNull T1 data1, @NonNull T2 data2, @NonNull T3 data3, @NonNull T4 data4) {
    return new Tuple4<>(requireNonNull(data1), requireNonNull(data2), requireNonNull(data3), requireNonNull(data4));
  }

  @NonNull
  public static <T1, T2, T3, T4, T5> Tuple5<T1, T2, T3, T4, T5> of(@NonNull T1 data1, @NonNull T2 data2, @NonNull T3 data3, @NonNull T4 data4, @NonNull T5 data5) {
    return new Tuple5<>(requireNonNull(data1), requireNonNull(data2), requireNonNull(data3), requireNonNull(data4), requireNonNull(data5));
  }
}
