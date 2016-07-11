package com.kwf2030.android.app;

import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import static com.tiexing.airticket.util.Nulls.requireNonNull;

/**
 * ViewHolder基类
 */
public abstract class AppViewHolder extends RecyclerView.ViewHolder {
  private boolean mHasHeader;

  public AppViewHolder(@NonNull View view, boolean hasHeader) {
    super(requireNonNull(view));
    mHasHeader = hasHeader;
  }

  @SuppressWarnings("unchecked")
  @NonNull
  protected <T extends View> T $(@IdRes int resId) {
    return (T) itemView.findViewById(resId);
  }

  public int $getPosition() {
    int pos = getLayoutPosition();
    return mHasHeader ? pos - 1 : pos;
  }

  /**
   * 覆写此方法初始化View，例如：
   *
   * protected void $initViews() {
   *   textView = $(R.id.text);
   *   imageView = $(R.id.image);
   * }
   * @param viewType
   */
  protected abstract void $initViews(int viewType);
}
