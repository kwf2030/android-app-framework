package com.kwf2030.android.data;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import com.kwf2030.android.app.App;
import com.kwf2030.android.util.Nulls;

import java.util.Set;

public final class LocalPreference {
  private static final String PREF_APP = "app";
  private final SharedPreferences mPreference;

  private SharedPreferences.OnSharedPreferenceChangeListener mListener;

  public LocalPreference(@NonNull App ctx) {
    mPreference = Nulls.requireNonNull(ctx).getSharedPreferences(PREF_APP, Context.MODE_PRIVATE);
  }

  public void setListener(@NonNull SharedPreferences.OnSharedPreferenceChangeListener listener) {
    mListener = listener;
    mPreference.registerOnSharedPreferenceChangeListener(mListener);
  }

  public void removeListener() {
    if (mListener != null) {
      mPreference.unregisterOnSharedPreferenceChangeListener(mListener);
      mListener = null;
    }
  }

  public boolean contains(@NonNull String key) {
    return mPreference.contains(key);
  }

  public int get(@NonNull String key, int defaultValue) {
    return mPreference.getInt(key, defaultValue);
  }

  public long get(@NonNull String key, long defaultValue) {
    return mPreference.getLong(key, defaultValue);
  }

  public float get(@NonNull String key, float defaultValue) {
    return mPreference.getFloat(key, defaultValue);
  }

  public boolean get(@NonNull String key, boolean defaultValue) {
    return mPreference.getBoolean(key, defaultValue);
  }

  @NonNull
  public String get(@NonNull String key, @NonNull String defaultValue) {
    return mPreference.getString(key, defaultValue);
  }

  @NonNull
  public Set<String> get(@NonNull String key, @NonNull Set<String> defaultValue) {
    return mPreference.getStringSet(key, defaultValue);
  }

  public void set(@NonNull String key, int value) {
    mPreference.edit().putInt(key, value).apply();
  }

  public void set(@NonNull String key, long value) {
    mPreference.edit().putLong(key, value).apply();
  }

  public void set(@NonNull String key, float value) {
    mPreference.edit().putFloat(key, value).apply();
  }

  public void set(@NonNull String key, boolean value) {
    mPreference.edit().putBoolean(key, value).apply();
  }

  public void set(@NonNull String key, @NonNull String value) {
    mPreference.edit().putString(key, value).apply();
  }

  public void set(@NonNull String key, @NonNull Set<String> value) {
    mPreference.edit().putStringSet(key, value).apply();
  }
}
