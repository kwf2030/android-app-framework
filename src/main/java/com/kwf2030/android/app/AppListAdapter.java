package com.kwf2030.android.app;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import com.tiexing.airticket.util.Nulls;

import java.util.List;

/**
 * 数据为List时的便捷Adapter
 *
 * 调用setOnItemClickListener()为item设置监听器
 *
 * @param <H> ViewHolder
 * @param <E> List中的数据
 */
public abstract class AppListAdapter<H extends AppViewHolder, E> extends AbstractAppAdapter<H> {
  protected List<E> mItems;

  private OnItemClickListener<H, E> mItemClickListener;

  public AppListAdapter(@NonNull List<E> items) {
    mItems = Nulls.requireNonNull(items);
  }

  @Override
  public H onCreateViewHolder(ViewGroup parent, int viewType) {
    final H holder = super.onCreateViewHolder(parent, viewType);
    holder.itemView.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        if (mItemClickListener != null) {
          int pos = holder.$getPosition();
          if (pos != -1) {
            mItemClickListener.onItemClick(holder, mItems.get(pos));
          }
        }
      }
    });

    return holder;
  }

  @Override
  protected void $initViewHolder(@NonNull H holder, int position) {
    $initViewHolder(holder, mItems.get(holder.$getPosition()));
  }

  @Override
  public int $getItemCount() {
    return mItems.size();
  }

  public void setOnItemClickListener(@NonNull OnItemClickListener<H, E> listener) {
    mItemClickListener = listener;
  }

  /**
   * 直接传入数据的$initViewHolder版本
   *
   * 注意，如果List中该索引处的数据为null，则不会调用此方法
   *
   * @param holder ViewHolder
   * @param value value
   */
  protected abstract void $initViewHolder(@NonNull H holder, @Nullable E value);

  public interface OnItemClickListener<H extends AppViewHolder, E> {
    void onItemClick(@NonNull H holder, @Nullable E value);
  }
}
