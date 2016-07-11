package com.kwf2030.android.app;

import android.support.annotation.NonNull;
import android.view.View;

public class AbstractAppAdapter<H extends AppViewHolder> extends AppAdapter<H> {
  @Override
  protected int $getLayoutResource(int viewType) {
    throw new RuntimeException("must be overridden");
  }

  @Override
  protected int $getHeaderLayoutResource() {
    return 0;
  }

  @Override
  protected int $getItemCount() {
    throw new RuntimeException("must be overridden");
  }

  @Override
  protected int $getItemViewType(int position) {
    return VIEW_TYPE_NORMAL;
  }

  @NonNull
  @Override
  protected H $getViewHolder(@NonNull View view, boolean hasHeader) {
    throw new RuntimeException("must be overridden");
  }

  @Override
  protected void $initViewHolder(@NonNull H holder, int position) {
    throw new RuntimeException("must be overridden");
  }

  @Override
  protected void $initViewHolderEvents(@NonNull H holder, int position) { }

  @Override
  protected void $initHeaderViewHolder(@NonNull H holder) { }

  @Override
  protected void $initHeaderViewHolderEvents(@NonNull H holder) { }
}
