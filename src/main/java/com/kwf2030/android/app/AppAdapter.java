package com.kwf2030.android.app;

import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.kwf2030.android.util.Nulls;

public abstract class AppAdapter<H extends AppViewHolder> extends RecyclerView.Adapter<H> {
  public static final int VIEW_TYPE_HEADER = 0x1000;
  public static final int VIEW_TYPE_NORMAL = 0x0000;

  private boolean mHasHeader;

  public AppAdapter() {
    mHasHeader = $getHeaderLayoutResource() != 0;
  }

  @Override
  public H onCreateViewHolder(ViewGroup parent, int viewType) {
    View view;

    if (mHasHeader && viewType == VIEW_TYPE_HEADER) {
      view = LayoutInflater.from(parent.getContext()).inflate($getHeaderLayoutResource(), parent, false);

    } else {
      view = LayoutInflater.from(parent.getContext()).inflate($getLayoutResource(viewType), parent, false);
    }

    H holder = Nulls.requireNonNull($getViewHolder(view, mHasHeader));
    holder.$initViews(viewType);

    return holder;
  }

  @Override
  public void onBindViewHolder(H holder, int position) {
    if (getItemViewType(position) == VIEW_TYPE_HEADER) {
      $initHeaderViewHolder(holder);
      $initHeaderViewHolderEvents(holder);

    } else {
      $initViewHolder(holder, position);
      $initViewHolderEvents(holder, position);
    }
  }

  @Override
  public int getItemCount() {
    int count = $getItemCount();
    return count == 0 ? 0 : (mHasHeader ? count + 1 : count);
  }

  @Override
  public int getItemViewType(int position) {
    if (!mHasHeader || position != 0) {
      return $getItemViewType(position);
    }
    return VIEW_TYPE_HEADER;
  }

  @LayoutRes
  protected abstract int $getLayoutResource(int viewType);

  @LayoutRes
  protected abstract int $getHeaderLayoutResource();

  protected abstract int $getItemCount();

  protected abstract int $getItemViewType(int position);

  @NonNull
  protected abstract H $getViewHolder(@NonNull View view, boolean hasHeader);

  protected abstract void $initViewHolder(@NonNull H holder, int position);

  protected abstract void $initViewHolderEvents(@NonNull H holder, int position);

  protected abstract void $initHeaderViewHolder(@NonNull H holder);

  protected abstract void $initHeaderViewHolderEvents(@NonNull H holder);
}
