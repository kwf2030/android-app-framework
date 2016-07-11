package com.kwf2030.android.app;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;

import java.util.Set;

import static com.kwf2030.android.util.Nulls.requireNonNull;

public final class AppPreference {
  private static final String APP_PREF_NAME = "app";

  private SharedPreferences mPref;
  private SharedPreferences.OnSharedPreferenceChangeListener mListener;

  AppPreference(@NonNull App ctx) {
    mPref = requireNonNull(ctx).getSharedPreferences(APP_PREF_NAME, Context.MODE_PRIVATE);
  }

  public void setListener(@NonNull SharedPreferences.OnSharedPreferenceChangeListener listener) {
    mListener = requireNonNull(listener);
    mPref.registerOnSharedPreferenceChangeListener(listener);
  }

  public boolean contains(@NonNull String key) {
    return mPref.contains(requireNonNull(key));
  }

  public int get(@NonNull String key, int defaultValue) {
    return mPref.getInt(requireNonNull(key), defaultValue);
  }

  public long get(@NonNull String key, long defaultValue) {
    requireNonNull(key);
    return mPref.getLong(key, defaultValue);
  }

  public float get(@NonNull String key, float defaultValue) {
    return mPref.getFloat(requireNonNull(key), defaultValue);
  }

  public boolean get(@NonNull String key, boolean defaultValue) {
    return mPref.getBoolean(requireNonNull(key), defaultValue);
  }

  @NonNull
  public String get(@NonNull String key, @NonNull String defaultValue) {
    return mPref.getString(requireNonNull(key), requireNonNull(defaultValue));
  }

  @NonNull
  public Set<String> get(@NonNull String key, @NonNull Set<String> defaultValue) {
    requireNonNull(key);
    requireNonNull(defaultValue);
    return mPref.getStringSet(key, defaultValue);
  }

  public void set(@NonNull String key, int value) {
    requireNonNull(key);
    mPref.edit().putInt(key, value).apply();
  }

  public void set(@NonNull String key, long value) {
    requireNonNull(key);
    mPref.edit().putLong(key, value).apply();
  }

  public void set(@NonNull String key, float value) {
    requireNonNull(key);
    mPref.edit().putFloat(key, value).apply();
  }

  public void set(@NonNull String key, boolean value) {
    requireNonNull(key);
    mPref.edit().putBoolean(key, value).apply();
  }

  public void set(@NonNull String key, @NonNull String value) {
    requireNonNull(key);
    requireNonNull(value);
    mPref.edit().putString(key, value).apply();
  }

  public void set(@NonNull String key, @NonNull Set<String> value) {
    requireNonNull(key);
    requireNonNull(value);
    mPref.edit().putStringSet(key, value).apply();
  }

  void removeListener() {
    if (mListener != null) {
      mPref.unregisterOnSharedPreferenceChangeListener(mListener);
    }
  }
}
