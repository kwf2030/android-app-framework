package com.kwf2030.android.util;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.Collection;
import java.util.Map;

public final class Nulls {
  @NonNull
  public static <T> T requireNonNull(@Nullable T data) {
    if (data == null) {
      throw new NullPointerException("param can not be null");
    }
    return data;
  }

  @NonNull
  public static <T extends CharSequence> T requireNonEmpty(@Nullable T data) {
    if (data == null || data.length() == 0) {
      throw new NullPointerException("param can not be null or empty");
    }
    return data;
  }

  @NonNull
  public static <T extends Collection<E>, E> T requireNonEmpty(@Nullable T data) {
    if (data == null || data.isEmpty()) {
      throw new NullPointerException("param can not be null or empty");
    }
    return data;
  }

  @NonNull
  public static <T extends Map<K, V>, K, V> T requireNonEmpty(@Nullable T data) {
    if (data == null || data.isEmpty()) {
      throw new NullPointerException("param can not be null or empty");
    }
    return data;
  }

  public static <T> boolean isNull(@Nullable T data) {
    return data == null;
  }

  public static <T extends CharSequence> boolean isEmpty(@Nullable T data) {
    return data == null || data.length() == 0;
  }

  public static <T extends Collection<E>, E> boolean isEmpty(@Nullable T data) {
    return data == null || data.isEmpty();
  }

  public static <T extends Map<K, V>, K, V> boolean isEmpty(@Nullable T data) {
    return data == null || data.isEmpty();
  }

  public static <T> boolean isEmpty(@Nullable T[] data) {
    return data == null || data.length == 0;
  }
}
