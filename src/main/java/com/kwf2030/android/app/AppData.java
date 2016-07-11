package com.kwf2030.android.app;

import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.kwf2030.android.data.LocalDatabase;
import com.kwf2030.android.data.LocalPreference;
import com.kwf2030.android.data.RemoteService;
import com.kwf2030.android.util.Nulls;
import io.requery.Persistable;
import io.requery.sql.EntityDataStore;

import java.util.List;
import java.util.Set;

public final class AppData {
  private static AppData sInstance;

  private LocalPreference mPreference;

  private LocalDatabase mDatabase;

  private RemoteService mService;

  @NonNull
  public static AppData getInstance() {
    if (sInstance == null) {
      synchronized (AppData.class) {
        if (sInstance == null) {
          sInstance = new AppData();
        }
      }
    }
    return sInstance;
  }

  private AppData() {
  }

  public void setPreferenceListener(@NonNull SharedPreferences.OnSharedPreferenceChangeListener listener) {
    mPreference.setListener(Nulls.requireNonNull(listener));
  }

  public void removePreferenceListener() {
    mPreference.removeListener();
  }

  public boolean containsPreference(@NonNull String key) {
    return mPreference.contains(Nulls.requireNonNull(key));
  }

  @NonNull
  public Set<String> get(@NonNull String key, @NonNull Set<String> defaultValue) {
    return mPreference.get(Nulls.requireNonNull(key), Nulls.requireNonNull(defaultValue));
  }

  public void set(@NonNull String key, float value) {
    mPreference.set(Nulls.requireNonNull(key), value);
  }

  public void set(@NonNull String key, @NonNull String value) {
    mPreference.set(Nulls.requireNonNull(key), value);
  }

  public void set(@NonNull String key, long value) {
    mPreference.set(Nulls.requireNonNull(key), value);
  }

  public float get(@NonNull String key, float defaultValue) {
    return mPreference.get(Nulls.requireNonNull(key), defaultValue);
  }

  public void set(@NonNull String key, int value) {
    mPreference.set(Nulls.requireNonNull(key), value);
  }

  public int get(@NonNull String key, int defaultValue) {
    return mPreference.get(Nulls.requireNonNull(key), defaultValue);
  }

  public void set(@NonNull String key, boolean value) {
    mPreference.set(Nulls.requireNonNull(key), value);
  }

  public boolean get(@NonNull String key, boolean defaultValue) {
    return mPreference.get(Nulls.requireNonNull(key), defaultValue);
  }

  public void set(@NonNull String key, @NonNull Set<String> value) {
    mPreference.set(Nulls.requireNonNull(key), Nulls.requireNonNull(value));
  }

  @NonNull
  public String get(@NonNull String key, @NonNull String defaultValue) {
    return mPreference.get(Nulls.requireNonNull(key), Nulls.requireNonNull(defaultValue));
  }

  public long get(@NonNull String key, long defaultValue) {
    return mPreference.get(Nulls.requireNonNull(key), defaultValue);
  }

  public void insert(@NonNull Persistable data) {
    mDatabase.insert(Nulls.requireNonNull(data));
  }

  @NonNull
  public List<Persistable> query(@NonNull Class<Persistable> clazz) {
    return mDatabase.query(Nulls.requireNonNull(clazz));
  }

  @NonNull
  public List<Persistable> query(@NonNull Class<Persistable> clazz, @NonNull String sql, @Nullable Object... params) {
    if (params == null) {
      return mDatabase.query(Nulls.requireNonNull(clazz), Nulls.requireNonNull(sql));
    } else {
      return mDatabase.query(Nulls.requireNonNull(clazz), Nulls.requireNonNull(sql), params);
    }
  }

  public void insertOrUpdate(@NonNull Persistable data) {
    mDatabase.insertOrUpdate(Nulls.requireNonNull(data));
  }

  public void insert(@NonNull List<Persistable> data) {
    mDatabase.insert(Nulls.requireNonNull(data));
  }

  public void insertOrUpdate(@NonNull List<Persistable> data) {
    mDatabase.insertOrUpdate(Nulls.requireNonNull(data));
  }

  public void delete(@NonNull Persistable data) {
    mDatabase.delete(Nulls.requireNonNull(data));
  }

  public void update(@NonNull Persistable data) {
    mDatabase.update(Nulls.requireNonNull(data));
  }

  public void delete(@NonNull Class<Persistable> clazz) {
    mDatabase.delete(Nulls.requireNonNull(clazz));
  }

  @NonNull
  public EntityDataStore<Persistable> getDataStore() {
    return mDatabase.getDataStore();
  }

  public String getDatabaseName() {
    return mDatabase.getName();
  }

  public int getDatabaseVersion() {
    return mDatabase.getVersion();
  }

  public boolean isDatabaseLogEnabled() {
    return mDatabase.isLogEnabled();
  }

  public void switchToTestAddress() {
    mService.switchToTestAddress();
  }

  public void switchToProdAddress() {
    mService.switchToProdAddress();
  }

  public boolean isTestAddress() {
    return mService.isTestAddress();
  }

  public <T> T api(@NonNull String name) {
    return mService.get(Nulls.requireNonNull(name));
  }

  void setPreference(@NonNull LocalPreference preference) {
    mPreference = Nulls.requireNonNull(preference);
  }

  void setService(@NonNull RemoteService service) {
    mService = Nulls.requireNonNull(service);
  }

  void setDatabase(@NonNull LocalDatabase database) {
    mDatabase = Nulls.requireNonNull(database);
  }
}
