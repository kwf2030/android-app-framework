package com.kwf2030.android.app;

import android.support.annotation.NonNull;
import com.tiexing.airticket.util.Nulls;

public abstract class AppPresenter<V extends AppView> {
  protected final V mView;

  protected AppPresenter(@NonNull V view) {
    mView = Nulls.requireNonNull(view);
  }
}
