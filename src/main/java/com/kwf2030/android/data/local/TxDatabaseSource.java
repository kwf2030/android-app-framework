package com.kwf2030.android.data.local;

import android.content.Context;
import android.support.annotation.Nullable;
import io.requery.android.sqlite.DatabaseSource;
import io.requery.meta.EntityModel;

public class TxDatabaseSource extends DatabaseSource {
  public TxDatabaseSource(Context context, EntityModel model, @Nullable String name, int version) {
    super(context, model, name, version);
  }
}
