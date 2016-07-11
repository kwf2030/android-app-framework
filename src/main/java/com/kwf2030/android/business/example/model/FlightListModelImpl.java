package com.kwf2030.android.business.example.model;

import android.support.annotation.NonNull;
import com.kwf2030.android.business.example.viewmodel.Flight;
import rx.Single;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

class FlightListModelImpl implements FlightListModel {
  @NonNull
  @Override
  public <I, O> Single<O> doAsync(@NonNull I param) {
    throw new RuntimeException("not implemented");
  }

  @NonNull
  @Override
  public Single<List<Flight>> loadFlightList(@NonNull String departure, @NonNull String destination, @NonNull String startDate, @NonNull String endDate, int pageNo, int pageSize) {
    return Single.just(mockList());
  }

  private List<Flight> mockList() {
    List<Flight> list = new ArrayList<>();
    Flight bean1 = mockBean("03:35", 1010.0f, "南方航空", "首都机场", "经济舱");
    Flight bean2 = mockBean("06:15", 1003, "亚洲航空", "北京机场", "经济舱");
    Flight bean3 = mockBean("07:09", 688, "日本航空", "阿里机场", "商务舱");
    Flight bean4 = mockBean("01:58", 2093.7f, "厦门航空", "百度机场", "商务舱");
    Flight bean5 = mockBean("05:30", 1657, "东方航空", "腾讯机场", "头等舱");
    Flight bean6 = mockBean("04:28", 888, "南方航空", "首都机场", "经济舱");
    Flight bean7 = mockBean("08:00", 2987, "东方航空", "上海机场", "经济舱");
    Flight bean8 = mockBean("07:10", 4690, "亚洲航空", "上海机场", "经济舱");
    Flight bean9 = mockBean("02:46", 2098.99f, "海南航空", "腾讯机场", "头等舱");
    Flight bean10 = mockBean("01:10", 1972, "海南航空", "首都机场", "经济舱");
    Flight bean11 = mockBean("09:20", 500, "南方航空", "首都机场", "头等舱");
    Flight bean12 = mockBean("08:40", 1111.12f, "吉祥航空", "北京机场", "商务舱");
    Collections.addAll(list, bean1, bean2, bean3, bean4, bean5, bean6, bean7, bean8, bean9, bean10, bean11, bean12);
    return list;
  }

  private Flight mockBean(String startTime, float price, String airCompany, String airport, String airClass) {
    Flight ret = new Flight();
    ret.startTime = startTime;
    ret.startAirport = "禄口机场";
    ret.startTerminal = "T2";
    ret.arrivalTime = "11:15";
    ret.arrivalAirport = airport;
    ret.arrivalTerminal = "T1";
    ret.price = price;
    ret.airClass = airClass;
    ret.airCompany = airCompany;
    ret.flightNo = "MU223";
    ret.discount = 0.77f;
    return ret;
  }
}
