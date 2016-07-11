package com.kwf2030.android.util;

import android.support.annotation.NonNull;
import rx.Observable;
import rx.subjects.PublishSubject;
import rx.subjects.SerializedSubject;
import rx.subjects.Subject;

public final class EventBus {
  private static Subject<Event, Event> sSubject;

  @NonNull
  public static Observable<Event> observable() {
    if (sSubject == null) {
      synchronized (EventBus.class) {
        if (sSubject == null) {
          sSubject = new SerializedSubject<>(PublishSubject.<Event>create());
        }
      }
    }
    return sSubject;
  }

  public static <T> void post(@NonNull Event<T> event) {
    Nulls.requireNonNull(event);
    if (sSubject != null) {
      sSubject.onNext(event);
    }
  }

  public static void destroy() {
    if (sSubject != null) {
      sSubject.onCompleted();
      sSubject = null;
    }
  }
}
