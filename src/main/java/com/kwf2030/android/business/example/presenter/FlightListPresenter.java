package com.kwf2030.android.business.example.presenter;

import android.support.annotation.NonNull;
import com.kwf2030.android.app.AppPresenter;
import com.kwf2030.android.business.example.model.FlightListModel;
import com.kwf2030.android.business.example.view.FlightListView;
import com.kwf2030.android.business.example.viewmodel.Flight;
import org.joda.time.LocalTime;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Action2;
import rx.functions.Func0;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

import java.util.*;

import static android.text.TextUtils.isEmpty;

//TODO clear filter and reverse sort not implemented
//TODO no limit filter not implemented
//TODO sort after filter logic incorrect(sortedResults)
public class FlightListPresenter extends AppPresenter<FlightListView> {
  private static final int PAGE_SIZE = 20;

  private FlightListModel model;

  private String departure;
  private String destination;
  private String startDate;
  private String endDate;

  private int pageNo;

  private List<Flight> originalResults;
  private List<Flight> sortedResults;

  private FlightListView.Filter filter;

  private FlightListPresenter(FlightListView view) {
    super(view);
    this.model = FlightListModel.Factory.create();
  }

  public static FlightListPresenter create(@NonNull FlightListView view) {
    return new FlightListPresenter(view);
  }

  public void loadFlightList(@NonNull String departure, @NonNull String destination, @NonNull String startDate, @NonNull String endDate) {
    if (isEmpty(departure) || isEmpty(destination) || isEmpty(startDate) || isEmpty(endDate)) {
      throw new RuntimeException("args can not be null or empty");
    }
    if (checkModification(departure, destination, startDate, endDate)) {
      pageNo = 0;
      save(departure, destination, startDate, endDate);
    }
    model.loadFlightList(departure, destination, startDate, endDate, pageNo++, PAGE_SIZE)
      .subscribeOn(Schedulers.io())
      .doOnError(new Action1<Throwable>() {
        @Override
        public void call(Throwable throwable) {
          //TODO handle error
          System.out.println(throwable.getMessage());
        }
      })
      .observeOn(AndroidSchedulers.mainThread())
      //TODO change to Observable to emit multiply Flight items, so we can do filter action
      .subscribe(new Action1<List<Flight>>() {
        @Override
        public void call(List<Flight> result) {
          if (result == null) {
            if (!view.hasItem()) {
              view.noData();
            }
            return;
          }
          if (originalResults == null) {
            originalResults = new ArrayList<>();
          }
          if (sortedResults == null) {
            sortedResults = new ArrayList<>();
          }
          originalResults.addAll(result);
          sortedResults.addAll(result);
          if (view.hasItem()) {
            view.showMore(result);
          } else {
            view.refresh(result);
          }
          view.hideProgressIndicator();
        }
      });
  }

  public void loadMore() {
    loadFlightList(departure, destination, startDate, endDate);
  }

  public void sortByStartTime() {
    Collections.sort(originalResults, byStartTime);
    view.refresh(originalResults);
  }

  public void sortByArrivalTime() {
    Collections.sort(originalResults, byArrivalTime);
    view.refresh(originalResults);
  }

  public void sortByConsumeTime() {
    Collections.sort(originalResults, byConsumeTime);
    view.refresh(originalResults);
  }

  public void sortByPrice() {
    Collections.sort(originalResults, byPrice);
    view.refresh(originalResults);
  }

  public void filter(@NonNull final FlightListView.Filter filter) {
    this.filter = filter;
    filterWithObservable();
  }

  private void filterWithObservable() {
    Observable.from(sortedResults).filter(new Func1<Flight, Boolean>() {
      @Override
      public Boolean call(Flight flight) {
        Collection<String> times = filter.getStartTime();
        if (times.isEmpty()) {
          return true;
        } else {
          LocalTime t = LocalTime.parse(flight.startTime);
          for (String s : times) {
            String[] time = s.split("-");
            if (LocalTime.parse(time[0].trim()).isBefore(t) && LocalTime.parse(time[1].trim()).isAfter(t)) {
              return true;
            }
          }
          return false;
        }
      }
    }).filter(new Func1<Flight, Boolean>() {
      @Override
      public Boolean call(Flight flight) {
        Collection<String> c = filter.getAirCompany();
        return c.isEmpty() || c.contains(flight.airCompany);
      }
    }).filter(new Func1<Flight, Boolean>() {
      @Override
      public Boolean call(Flight flight) {
        Collection<String> c = filter.getAirType();
        return c.isEmpty() || c.contains(flight.airType);
      }
    }).filter(new Func1<Flight, Boolean>() {
      @Override
      public Boolean call(Flight flight) {
        Collection<String> c = filter.getAirClass();
        return c.isEmpty() || c.contains(flight.airClass);
      }
    }).filter(new Func1<Flight, Boolean>() {
      @Override
      public Boolean call(Flight flight) {
        Collection<String> c = filter.getArrivalAirport();
        return c.isEmpty() || c.contains(flight.arrivalAirport);
      }
    }).collect(new Func0<List<Flight>>() {
      @Override
      public List<Flight> call() {
        return new ArrayList<>();
      }
    }, new Action2<List<Flight>, Flight>() {
      @Override
      public void call(List<Flight> flights, Flight flight) {
        flights.add(flight);
      }
    }).subscribe(new Action1<List<Flight>>() {
      @Override
      public void call(List<Flight> flights) {
        view.refresh(flights);
      }
    });
  }

  /*private void filterWithFor() {
    List<Flight> items = new ArrayList<>();
    for (Flight flight : sortedResults) {
      boolean ok = true;

      Collection<String> times = filter.getStartTime();
      LocalTime i = LocalTime.parse(flight.startTime);
      for (String s : times) {
        String[] time = s.split("-");
        ok = LocalTime.parse(time[0].trim()).isBefore(i) && LocalTime.parse(time[1].trim()).isAfter(i);
      }
      if (!ok) continue;

      Collection<String> company = filter.getAirCompany();
      if (!company.isEmpty()) {
        ok = company.contains(flight.airCompany);
        if (!ok) continue;
      }

      Collection<String> type = filter.getAirType();
      if (!type.isEmpty()) {
        ok = type.contains(flight.airType);
        if (!ok) continue;
      }

      Collection<String> classes = filter.getAirClass();
      if (!classes.isEmpty()) {
        ok = classes.contains(flight.airClass);
        if (!ok) continue;
      }

      Collection<String> airport = filter.getArrivalAirport();
      if (!airport.isEmpty()) {
        ok = airport.contains(flight.arrivalAirport);
        if (!ok) continue;
      }

      items.add(flight);
    }

    if (!items.isEmpty()) {
      view.refresh(items);
      sortedResults.clear();
      sortedResults.addAll(items);
    }
  }*/

  private boolean checkModification(@NonNull String departure, @NonNull String destination, @NonNull String startDate, @NonNull String endDate) {
    boolean modified = false;
    if (!departure.equalsIgnoreCase(this.departure)
      || !destination.equalsIgnoreCase(this.destination)
      || !startDate.equalsIgnoreCase(this.startDate)
      || !endDate.equalsIgnoreCase(this.endDate)) {
      modified = true;
    }
    return modified;
  }

  private void save(@NonNull String departure, @NonNull String destination, @NonNull String startDate, @NonNull String endDate) {
    this.departure = departure;
    this.destination = destination;
    this.startDate = startDate;
    this.endDate = endDate;
  }

  private Comparator<Flight> byStartTime = new Comparator<Flight>() {
    @Override
    public int compare(Flight one, Flight other) {
      return one.startTime.compareToIgnoreCase(other.startTime);
    }
  };

  private Comparator<Flight> byArrivalTime = new Comparator<Flight>() {
    @Override
    public int compare(Flight one, Flight other) {
      return one.arrivalTime.compareToIgnoreCase(other.arrivalTime);
    }
  };

  private Comparator<Flight> byConsumeTime = new Comparator<Flight>() {
    @Override
    public int compare(Flight one, Flight other) {
      int s1 = Integer.parseInt(one.startTime.replace(":", ""));
      int s2 = Integer.parseInt(one.arrivalTime.replace(":", ""));
      int a1 = Integer.parseInt(other.startTime.replace(":", ""));
      int a2 = Integer.parseInt(other.arrivalTime.replace(":", ""));
      return (s1 - s2) - (a1 + a2);

      // TODO maybe a bit slower(not sure), should do a benchmark later
      // return Minutes.minutesBetween(LocalTime.parse(one.arrivalTime), LocalTime.parse(one.startTime)).getValue(0) - Minutes.minutesBetween(LocalTime.parse(other.arrivalTime), LocalTime.parse(other.startTime)).getValue(0);
    }
  };

  private Comparator<Flight> byPrice = new Comparator<Flight>() {
    @Override
    public int compare(Flight one, Flight other) {
      return one.price == other.price ? 0 : (one.price > other.price ? 1 : -1);
    }
  };
}
