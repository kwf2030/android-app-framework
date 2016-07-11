package com.kwf2030.android.util;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.*;

public class MultiMap<K, V> implements Parcelable {
  private final Map<K, List<V>> mContainer;

  public MultiMap() {
    mContainer = new LinkedHashMap<>();
  }

  @NonNull
  public MultiMap<K, V> put(@NonNull K key, @NonNull V value) {
    Nulls.requireNonNull(key);
    Nulls.requireNonNull(value);
    if (mContainer.containsKey(key)) {
      mContainer.get(key).add(value);
    } else {
      List<V> l = new ArrayList<>();
      l.add(value);
      mContainer.put(key, l);
    }
    return this;
  }

  @NonNull
  public MultiMap<K, V> putAll(@NonNull K key, @NonNull Iterable<? extends V> values) {
    Nulls.requireNonNull(key);
    Nulls.requireNonNull(values);
    List<V> l;
    if (mContainer.containsKey(key)) {
      l = mContainer.get(key);
    } else {
      l = new ArrayList<>();
      mContainer.put(key, l);
    }
    for (V value : values) {
      if (value != null) {
        l.add(value);
      }
    }
    return this;
  }

  @NonNull
  public MultiMap<K, V> remove(@NonNull K key, @NonNull V value) {
    Nulls.requireNonNull(key);
    Nulls.requireNonNull(value);
    List<V> l = mContainer.get(key);
    if (l != null) {
      l.remove(value);
    }
    return this;
  }

  @NonNull
  public MultiMap<K, V> remove(@NonNull K key) {
    reset(key);
    mContainer.remove(key);
    return this;
  }

  @NonNull
  public MultiMap<K, V> reset(@NonNull K key) {
    List<V> l = mContainer.get(Nulls.requireNonNull(key));
    if (l != null) {
      l.clear();
    }
    return this;
  }

  @NonNull
  public MultiMap<K, V> removeAll() {
    resetAll();
    mContainer.clear();
    return this;
  }

  @NonNull
  public MultiMap<K, V> resetAll() {
    Set<Map.Entry<K, List<V>>> entries = mContainer.entrySet();
    for (Map.Entry<K, List<V>> entry : entries) {
      entry.getValue().clear();
    }
    return this;
  }

  @NonNull
  public MultiMap<K, V> replaceKey(@NonNull K oldKey, @NonNull K newKey) {
    Nulls.requireNonNull(oldKey);
    Nulls.requireNonNull(newKey);
    List<V> l = mContainer.get(oldKey);
    if (l != null) {
      mContainer.put(newKey, l);
      mContainer.remove(oldKey);
    }
    return this;
  }

  @NonNull
  public MultiMap<K, V> replaceValue(@NonNull K key, @NonNull V oldValue, @NonNull V newValue) {
    Nulls.requireNonNull(key);
    Nulls.requireNonNull(oldValue);
    Nulls.requireNonNull(newValue);
    List<V> l = mContainer.get(key);
    if (l != null) {
      int i = l.indexOf(oldValue);
      if (i != -1) {
        l.set(i, newValue);
      }
    }
    return this;
  }

  @NonNull
  public MultiMap<K, V> replaceAll(@NonNull K key, @NonNull Iterable<V> newValues) {
    Nulls.requireNonNull(key);
    Nulls.requireNonNull(newValues);
    List<V> l = mContainer.get(key);
    if (l != null) {
      l.clear();
      for (V value : newValues) {
        if (value != null) {
          l.add(value);
        }
      }
    }
    return this;
  }

  @Nullable
  public List<V> get(@NonNull K key) {
    Nulls.requireNonNull(key);
    return mContainer.get(key);
  }

  @NonNull
  public List<K> keys() {
    List<K> l = new ArrayList<>();
    Set<K> ks = mContainer.keySet();
    for (K key : ks) {
      l.add(key);
    }
    return l;
  }

  @NonNull
  public List<V> values() {
    List<V> l = new ArrayList<>();
    Collection<List<V>> c = mContainer.values();
    for (List<V> v : c) {
      l.addAll(v);
    }
    return l;
  }

  @NonNull
  public Map<K, List<V>> asMap() {
    return mContainer;
  }

  public int size() {
    return mContainer.size();
  }

  public int size(@NonNull K key) {
    List<V> l = mContainer.get(Nulls.requireNonNull(key));
    return l == null ? -1 : l.size();
  }

  public boolean isEmpty() {
    return mContainer.isEmpty();
  }

  public boolean isEmpty(@NonNull K key) {
    List<V> l = mContainer.get(Nulls.requireNonNull(key));
    return l == null || l.isEmpty();
  }

  public boolean containsKey(@NonNull K key) {
    return mContainer.containsKey(Nulls.requireNonNull(key));
  }

  public boolean containsValue(@NonNull V value) {
    Nulls.requireNonNull(value);
    Collection<List<V>> c = mContainer.values();
    for (List<V> v : c) {
      if (v.contains(value)) {
        return true;
      }
    }
    return false;
  }

  public boolean containsEntry(@NonNull K key, @NonNull V value) {
    Nulls.requireNonNull(value);
    List<V> l = mContainer.get(Nulls.requireNonNull(key));
    return l != null && l.contains(value);
  }

  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeMap(mContainer);
  }

  public static final Creator<MultiMap> CREATOR = new Creator<MultiMap>() {
    @Override
    public MultiMap createFromParcel(Parcel in) {
      MultiMap multimap = new MultiMap();
      in.readMap(multimap.mContainer, getClass().getClassLoader());
      return multimap;
    }

    @Override
    public MultiMap[] newArray(int size) {
      return new MultiMap[size];
    }
  };
}
