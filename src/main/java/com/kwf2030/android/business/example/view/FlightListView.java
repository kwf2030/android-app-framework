package com.kwf2030.android.business.example.view;

import android.support.annotation.NonNull;
import com.kwf2030.android.app.AppView;
import com.kwf2030.android.business.example.presenter.FlightListPresenter;
import com.kwf2030.android.business.example.viewmodel.Flight;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

public interface FlightListView extends AppView<FlightListPresenter> {
  void refresh(@NonNull List<Flight> list);

  void showMore(@NonNull List<Flight> list);

  void noData();

  boolean hasItem();

  interface Filter {
    boolean isDirectOnly();

    @NonNull
    Collection<String> getStartTime();

    @NonNull
    Collection<String> getArrivalTime();

    @NonNull
    Collection<String> getStartAirport();

    @NonNull
    Collection<String> getArrivalAirport();

    @NonNull
    String getPrice();

    @NonNull
    String getDiscount();

    @NonNull
    Collection<String> getAirType();

    @NonNull
    Collection<String> getAirClass();

    @NonNull
    Collection<String> getAirCompany();
  }

  class FilterAdapter implements Filter {

    @Override
    public boolean isDirectOnly() {
      return false;
    }

    @NonNull
    @Override
    public Collection<String> getStartTime() {
      return Collections.emptyList();
    }

    @NonNull
    @Override
    public Collection<String> getArrivalTime() {
      return Collections.emptyList();
    }

    @NonNull
    @Override
    public Collection<String> getStartAirport() {
      return Collections.emptyList();
    }

    @NonNull
    @Override
    public Collection<String> getArrivalAirport() {
      return Collections.emptyList();
    }

    @NonNull
    @Override
    public String getPrice() {
      return "";
    }

    @NonNull
    @Override
    public String getDiscount() {
      return "";
    }

    @NonNull
    @Override
    public Collection<String> getAirType() {
      return Collections.emptyList();
    }

    @NonNull
    @Override
    public Collection<String> getAirClass() {
      return Collections.emptyList();
    }

    @NonNull
    @Override
    public Collection<String> getAirCompany() {
      return Collections.emptyList();
    }
  }
}
