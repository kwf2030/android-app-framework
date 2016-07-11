package com.kwf2030.android.data;

import android.support.annotation.NonNull;
import io.requery.Persistable;
import io.requery.sql.EntityDataStore;

import java.util.List;

/**
 * 数据库操作封装
 *
 * 不要直接使用本类，使用AppData替代
 */
public final class LocalDatabase {
  private final EntityDataStore<Persistable> mDataStore;

  private String mDatabaseName;
  private int mDatabaseVersion;
  private boolean mDatabaseLogEnabled;

  public LocalDatabase(@NonNull EntityDataStore<Persistable> dataStore, String name, int version, boolean log) {
    mDataStore = dataStore;
    mDatabaseName = name;
    mDatabaseVersion = version;
    mDatabaseLogEnabled = log;
  }

  public void insert(@NonNull Persistable data) {
    mDataStore.insert(data);
  }

  public void insert(@NonNull List<Persistable> data) {
    mDataStore.insert(data);
  }

  public void insertOrUpdate(@NonNull Persistable data) {
    mDataStore.upsert(data);
  }

  public  void insertOrUpdate(@NonNull List<Persistable> data) {
    mDataStore.upsert(data);
  }

  public void delete(@NonNull Persistable data) {
    mDataStore.delete(data);
  }

  public void delete(@NonNull Class<Persistable> clazz) {
    mDataStore.delete(clazz).get().value();
  }

  public void update(@NonNull Persistable data) {
    mDataStore.update(data);
  }

  @NonNull
  public List<Persistable> query(@NonNull Class<Persistable> clazz) {
    return mDataStore.select(clazz).get().toList();
  }

  @NonNull
  public List<Persistable> query(@NonNull Class<Persistable> clazz, @NonNull String sql, @NonNull Object... params) {
    return mDataStore.raw(clazz, sql, params).toList();
  }

  @NonNull
  public EntityDataStore<Persistable> getDataStore() {
    return mDataStore;
  }

  public String getName() {
    return mDatabaseName;
  }

  public int getVersion() {
    return mDatabaseVersion;
  }

  public boolean isLogEnabled() {
    return mDatabaseLogEnabled;
  }
}
