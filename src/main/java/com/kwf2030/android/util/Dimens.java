package com.kwf2030.android.util;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;

import static android.util.TypedValue.*;

public final class Dimens {
  public static float dp2px(@NonNull Context ctx, float dp) {
    Nulls.requireNonNull(ctx);
    return applyDimension(COMPLEX_UNIT_DIP, dp, ctx.getResources().getDisplayMetrics());
  }

  public static float sp2px(@NonNull Context ctx, float sp) {
    Nulls.requireNonNull(ctx);
    return applyDimension(COMPLEX_UNIT_SP, sp, ctx.getResources().getDisplayMetrics());
  }

  public static int screenWidth(@NonNull Context ctx) {
    Nulls.requireNonNull(ctx);
    return ctx.getResources().getDisplayMetrics().widthPixels;
  }

  public static int screenHeight(@NonNull Context ctx) {
    Nulls.requireNonNull(ctx);
    return ctx.getResources().getDisplayMetrics().heightPixels;
  }

  @NonNull
  public static int[] locationInWindow(@NonNull View view) {
    Nulls.requireNonNull(view);
    int[] ret = new int[2];
    view.getLocationInWindow(ret);
    return ret;
  }

  @NonNull
  public static int[] locationOnScreen(@NonNull View view) {
    Nulls.requireNonNull(view);
    int[] ret = new int[2];
    view.getLocationOnScreen(ret);
    return ret;
  }
}
