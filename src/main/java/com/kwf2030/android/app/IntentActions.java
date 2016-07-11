package com.kwf2030.android.app;

import android.content.Intent;
import android.net.ConnectivityManager;

import java.util.Arrays;
import java.util.List;

final class IntentActions {
  static final String ACTION_SAMPLE = "com.tiexing.airticket.flight.sample";

  private static String[] ACTIONS = {
    Intent.ACTION_BOOT_COMPLETED,

    ConnectivityManager.CONNECTIVITY_ACTION,

    ACTION_SAMPLE,
  };

  static List<String> getActions() {
    return Arrays.asList(ACTIONS);
  }
}
