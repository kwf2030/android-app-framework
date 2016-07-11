package com.kwf2030.android.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import com.kwf2030.android.R;

public class ListItemDivider extends RecyclerView.ItemDecoration {
  private Drawable divider;

  public ListItemDivider(Context ctx) {
    this.divider = ctx.getResources().getDrawable(R.drawable.item_divider);
  }

  @Override
  public void onDrawOver(Canvas canvas, RecyclerView parent, RecyclerView.State state) {
    int left = parent.getPaddingLeft();
    int right = parent.getWidth() - parent.getPaddingRight();
    int c = parent.getChildCount();
    for (int i = 0; i < c; i++) {
      View child = parent.getChildAt(i);
      RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();
      int top = child.getBottom() + params.bottomMargin;
      int bottom = top + divider.getIntrinsicHeight();
      divider.setBounds(left, top, right, bottom);
      divider.draw(canvas);
    }
  }
}
