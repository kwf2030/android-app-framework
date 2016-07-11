package com.kwf2030.android.util;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import static com.kwf2030.android.util.Nulls.requireNonNull;

public final class Event<T> implements Parcelable {
  public int what;
  public int arg1;
  public int arg2;
  public T data;

  public Event(int what) {
    this.what = what;
  }

  public Event(int what, int arg1) {
    this.what = what;
    this.arg1 = arg1;
  }

  public Event(int what, @NonNull T data) {
    requireNonNull(data);
    this.what = what;
    this.data = data;
  }

  public Event(int what, int arg1, int arg2) {
    this.what = what;
    this.arg1 = arg1;
    this.arg2 = arg2;
  }

  public Event(int what, int arg1, int arg2, @NonNull T data) {
    requireNonNull(data);
    this.what = what;
    this.arg1 = arg1;
    this.arg2 = arg2;
    this.data = data;
  }

  public boolean hasData() {
    return data != null;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Event event = (Event) o;
    return what == event.what && arg1 == event.arg1 && arg2 == event.arg2;
  }

  @Override
  public int hashCode() {
    int result = what;
    result = 31 * result + arg1;
    result = 31 * result + arg2;
    return result;
  }

  @Override
  public String toString() {
    return "Event{" +
      "\n  what = " + what +
      "\n  arg1 = " + arg1 +
      "\n  arg2 = " + arg2 +
      "\n  data = " + data +
      '}';
  }

  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeInt(what);
    dest.writeInt(arg1);
    dest.writeInt(arg2);
    boolean p = data != null && data instanceof Parcelable;
    dest.writeByte(p ? (byte) 1 : (byte) 0);
    if (p) {
      dest.writeParcelable((Parcelable) data, 0);
    }
  }

  public static final Creator<Event> CREATOR = new Creator<Event>() {
    @Override
    public Event createFromParcel(Parcel in) {
      Event e = new Event(in.readInt(), in.readInt(), in.readInt());
      if (in.readByte() == 1) {
        e.data = in.readParcelable(getClass().getClassLoader());
      }
      return e;
    }

    @Override
    public Event[] newArray(int size) {
      return new Event[size];
    }
  };
}
