package com.kwf2030.android.util;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import com.google.gson.Gson;

import static com.kwf2030.android.util.Nulls.requireNonEmpty;
import static com.kwf2030.android.util.Nulls.requireNonNull;

public final class Intents {
  private static final String KEY = "internal";

  @NonNull
  public static Intent of(@NonNull String action) {
    requireNonEmpty(action);
    return new Intent(action);
  }

  @NonNull
  public static Intent of(@NonNull Context ctx, @NonNull Class target) {
    requireNonNull(ctx);
    requireNonNull(target);
    return new Intent(ctx, target);
  }

  @NonNull
  public static Intent pack(@NonNull Object obj, Class type) {
    requireNonNull(obj);
    requireNonNull(type);
    Intent ret = new Intent();
    ret.putExtra(KEY, new Gson().toJson(obj, type));
    return ret;
  }

  @NonNull
  public static <T> T unpack(@NonNull Intent intent, Class<T> type) {
    requireNonNull(intent);
    requireNonNull(type);
    String pack = intent.getStringExtra(KEY);
    return new Gson().fromJson(pack, type);
  }
}
