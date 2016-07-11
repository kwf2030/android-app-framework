package com.kwf2030.android.business.example.viewmodel;

import android.os.Parcel;
import android.os.Parcelable;

public class Flight implements Parcelable {
  //唯一标识
  public String id;

  //起飞日期（2016-06-01）
  public String startDate;

  //起飞时间(12:30)
  public String startTime;

  //起飞机场（南京禄口机场）
  public String startAirport;

  //起飞航站楼（T2）
  public String startTerminal;

  //到达日期
  public String arrivalDate;

  //到达时间
  public String arrivalTime;

  //到达机场
  public String arrivalAirport;

  //到达航站楼
  public String arrivalTerminal;

  //价格（1200.0）
  public float price;

  //折扣（0.85）
  public float discount;

  //舱位（经济舱）
  public String airClass;

  //机型（中型机）
  public String airType;

  //航班号（MU2509）
  public String flightNo;

  //航空公司（南方航空）
  public String airCompany;

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Flight flight = (Flight) o;
    if (id != null ? !id.equals(flight.id) : flight.id != null) return false;
    if (flightNo != null ? !flightNo.equals(flight.flightNo) : flight.flightNo != null) return false;
    return airCompany != null ? airCompany.equals(flight.airCompany) : flight.airCompany == null;
  }

  @Override
  public int hashCode() {
    int result = id != null ? id.hashCode() : 0;
    result = 31 * result + (flightNo != null ? flightNo.hashCode() : 0);
    result = 31 * result + (airCompany != null ? airCompany.hashCode() : 0);
    return result;
  }

  @Override
  public String toString() {
    return "Flight{" +
      "id='" + id + '\'' +
      ", startDate='" + startDate + '\'' +
      ", startTime='" + startTime + '\'' +
      ", startAirport='" + startAirport + '\'' +
      ", startTerminal='" + startTerminal + '\'' +
      ", arrivalDate='" + arrivalDate + '\'' +
      ", arrivalTime='" + arrivalTime + '\'' +
      ", arrivalAirport='" + arrivalAirport + '\'' +
      ", arrivalTerminal='" + arrivalTerminal + '\'' +
      ", price=" + price +
      ", discount=" + discount +
      ", airClass='" + airClass + '\'' +
      ", airType='" + airType + '\'' +
      ", flightNo='" + flightNo + '\'' +
      ", airCompany='" + airCompany + '\'' +
      '}';
  }

  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(id);
    dest.writeString(startDate);
    dest.writeString(startTime);
    dest.writeString(startAirport);
    dest.writeString(startTerminal);
    dest.writeString(arrivalDate);
    dest.writeString(arrivalTime);
    dest.writeString(arrivalAirport);
    dest.writeString(arrivalTerminal);
    dest.writeFloat(price);
    dest.writeFloat(discount);
    dest.writeString(airClass);
    dest.writeString(airType);
    dest.writeString(flightNo);
    dest.writeString(airCompany);
  }

  public static final Creator<Flight> CREATOR = new Creator<Flight>() {
    @Override
    public Flight createFromParcel(Parcel in) {
      Flight flight = new Flight();
      flight.id = in.readString();
      flight.startDate = in.readString();
      flight.startTime = in.readString();
      flight.startAirport = in.readString();
      flight.startTerminal = in.readString();
      flight.arrivalDate = in.readString();
      flight.arrivalTime = in.readString();
      flight.arrivalAirport = in.readString();
      flight.arrivalTerminal = in.readString();
      flight.price = in.readFloat();
      flight.discount = in.readFloat();
      flight.airClass = in.readString();
      flight.airType = in.readString();
      flight.flightNo = in.readString();
      flight.airCompany = in.readString();
      return flight;
    }

    @Override
    public Flight[] newArray(int size) {
      return new Flight[size];
    }
  };
}
