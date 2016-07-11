package com.kwf2030.android.business.example.model;

import android.support.annotation.NonNull;
import com.kwf2030.android.app.AppModel;
import com.kwf2030.android.business.example.viewmodel.Flight;
import rx.Single;

import java.util.List;

public interface FlightListModel extends AppModel {
  @NonNull
  Single<List<Flight>> loadFlightList(@NonNull String departure, @NonNull String destination, @NonNull String startDate, @NonNull String endDate, int pageNo, int pageSize);

  class Factory {
    @NonNull
    public static FlightListModel create() {
      return new FlightListModelImpl();
    }
  }
}
