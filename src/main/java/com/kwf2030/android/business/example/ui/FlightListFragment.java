package com.kwf2030.android.business.example.ui;

import android.Manifest;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import com.kwf2030.android.R;
import com.kwf2030.android.app.AppFragment;
import com.kwf2030.android.app.AppListAdapter;
import com.kwf2030.android.app.AppViewHolder;
import com.kwf2030.android.business.example.presenter.FlightListPresenter;
import com.kwf2030.android.business.example.view.FlightListView;
import com.kwf2030.android.business.example.viewmodel.Flight;
import com.kwf2030.android.widget.ListItemDivider;

import java.util.List;

import static android.text.TextUtils.isEmpty;

public class FlightListFragment extends AppFragment<FlightListPresenter> implements FlightListView {
  private RecyclerView mRecyclerView;
  private TextView mNoDataTextView;

  private Adapter mAdapter;

  private String mDeparture = "南京";
  private String mDestination = "北京";
  private String mStartDate = "2016-05-01";
  private String mReturnDate = "2016-05-06";

  public static FlightListFragment create(String departure, String destination, String startDate, String returnDate) {
    Bundle args = new Bundle();
    args.putString(Args.DEPARTURE, departure);
    args.putString(Args.DESTINATION, destination);
    args.putString(Args.START_DATE, startDate);
    args.putString(Args.RETURN_DATE, returnDate);
    FlightListFragment ret = new FlightListFragment();
    ret.setArguments(args);
    return ret;
  }

  @NonNull
  @Override
  public FlightListPresenter getPresenter() {
    return FlightListPresenter.create(this);
  }

  @LayoutRes
  @Override
  protected int getLayoutResource() {
    return R.layout.fragment_flight_list;
  }

  @Override
  protected void processArguments(@Nullable Bundle args) {
    if (args == null) {
      return;
    }
    mDeparture = args.getString(Args.DEPARTURE);
    mDestination = args.getString(Args.DESTINATION);
    mStartDate = args.getString(Args.START_DATE);
    mReturnDate = args.getString(Args.RETURN_DATE);
  }

  @Override
  protected void initViews() {
    mRecyclerView = $(R.id.flight_list);
    mNoDataTextView = $(R.id.no_data);
    mRecyclerView.setLayoutManager(new LinearLayoutManager(mActivity));
    mRecyclerView.addItemDecoration(new ListItemDivider(mActivity));
  }

  @Override
  protected void initWork() {
    if (isEmpty(mDeparture) || isEmpty(mDestination) || isEmpty(mStartDate) || isEmpty(mReturnDate)) {
      throw new NullPointerException("params must not be null or empty");
    }
    mPresenter.loadFlightList(mDeparture, mDestination, mStartDate, mReturnDate);
  }

  @Override
  protected void onSwipeRefresh() {
    mPresenter.loadMore();
  }

  @Override
  public boolean hasItem() {
    return mAdapter != null && mAdapter.getItemCount() > 0;
  }

  @Override
  public void refresh(@NonNull List<Flight> list) {
    if (list.isEmpty()) {
      noData();
      return;
    }
    mNoDataTextView.setVisibility(View.GONE);
    mRecyclerView.setVisibility(View.VISIBLE);
    if (mAdapter == null) {
      mAdapter = new Adapter(list);
      mRecyclerView.setAdapter(mAdapter);
    } else {
      mAdapter.setItems(list);
      mAdapter.notifyDataSetChanged();
    }
  }

  @Override
  public void showMore(@NonNull List<Flight> list) {
    if (list.isEmpty()) {
      return;
    }
    if (mAdapter != null) {
      mAdapter.notifyItemRangeInserted(mAdapter.getItemCount(), list.size());
    }
  }

  @Override
  public void noData() {
    mNoDataTextView.setVisibility(View.VISIBLE);
    mRecyclerView.setVisibility(View.GONE);
  }

  @Override
  public boolean handleMessage(Message msg) {
    if (msg.what == 1) {
      if (msg.obj != null) {
        mPresenter.filter((FlightListView.Filter) msg.obj);
      }
    }
    return true;
  }

  private View.OnClickListener mSortClickListener = new View.OnClickListener() {
    @Override
    public void onClick(View v) {
      switch (v.getId()) {
        case R.id.sort_start_time:
          mPresenter.sortByStartTime();
          showToast("by start time");
          break;

        case R.id.sort_arrival_time:
          mPresenter.sortByArrivalTime();
          showToast("by arrival time");
          break;

        case R.id.sort_consume_time:
          mPresenter.sortByConsumeTime();
          showToast("by consume time");
          break;

        case R.id.sort_price:
          mPresenter.sortByPrice();
          showToast("by price");
          break;
      }
    }
  };

  private View.OnClickListener mItemClickListener = new View.OnClickListener() {
    @Override
    public void onClick(View v) {
      checkPermission(1, Manifest.permission.CAMERA);
    }
  };

  private static class Args {
    private static final String DEPARTURE = "departure";
    private static final String DESTINATION = "destination";
    private static final String START_DATE = "start_date";
    private static final String RETURN_DATE = "return_date";
  }

  private class Adapter extends AppListAdapter<Adapter.ViewHolder, Flight> {
    private Adapter(@NonNull List<Flight> items) {
      super(items);
    }

    @Override
    protected int getLayoutResource(int viewType) {
      return R.layout.item_flight_list;
    }

    @Override
    protected int getHeaderLayoutResource() {
      return R.layout.item_flight_list_sort;
    }

    @NonNull
    @Override
    protected ViewHolder getViewHolder(@NonNull View view, int viewType) {
      return new ViewHolder(view, viewType);
    }

    @Override
    protected void initViewHolder(@NonNull ViewHolder holder, @NonNull Flight value) {
      // holder.startDate.setText(value.startDate);
      holder.startTime.setText(value.startTime);
      holder.startAirport.setText(value.startAirport);
      holder.startTerminal.setText(value.startTerminal);
      // holder.arrivalDate.setText(value.arrivalDate);
      holder.arrivalTime.setText(value.arrivalTime);
      holder.arrivalAirport.setText(value.arrivalAirport);
      holder.arrivalTerminal.setText(value.arrivalTerminal);
      holder.price.setText(String.format("%s", (int) value.price));
      holder.discount.setText(String.format("%s折", value.discount * 10));
      holder.airClass.setText(value.airClass);
      holder.flightNo.setText(value.flightNo);
      holder.airCompany.setText(value.airCompany);
    }

    @Override
    protected void initViewHolderEvents(@NonNull ViewHolder holder, int position) {
      holder.itemView.setOnClickListener(mItemClickListener);
    }

    @Override
    protected void initHeaderViewHolder(@NonNull ViewHolder holder) {
      holder.byStartTime.setOnClickListener(mSortClickListener);
      holder.byArrivalTime.setOnClickListener(mSortClickListener);
      holder.byConsumeTime.setOnClickListener(mSortClickListener);
      holder.byPrice.setOnClickListener(mSortClickListener);
    }

    void setItems(@NonNull List<Flight> items) {
      mItems = items;
    }

    class ViewHolder extends AppViewHolder {
      //起飞日期
      //TextView startDate;

      //起飞时间
      TextView startTime;

      //起飞机场
      TextView startAirport;

      //起飞航站楼
      TextView startTerminal;

      //到达日期
      //TextView arrivalDate;

      //到达时间
      TextView arrivalTime;

      //到达机场
      TextView arrivalAirport;

      //到达航站楼
      TextView arrivalTerminal;

      //价格
      TextView price;

      //折扣
      TextView discount;

      //舱位
      TextView airClass;

      //航班号
      TextView flightNo;

      //航空公司
      TextView airCompany;

      //排序按钮
      TextView byStartTime, byArrivalTime, byConsumeTime, byPrice;

      ViewHolder(View view, int viewType) {
        super(view, viewType);
      }

      @Override
      protected void initViews(int viewType) {
        if (viewType == VIEW_TYPE_HEADER) {
          byStartTime = $(R.id.sort_start_time);
          byArrivalTime = $(R.id.sort_arrival_time);
          byConsumeTime = $(R.id.sort_consume_time);
          byPrice = $(R.id.sort_price);
        } else {
          startTime = $(R.id.start_time);
          startAirport = $(R.id.start_airport);
          startTerminal = $(R.id.start_terminal);
          arrivalTime = $(R.id.arrival_time);
          arrivalAirport = $(R.id.arrival_airport);
          arrivalTerminal = $(R.id.arrival_terminal);
          price = $(R.id.price);
          discount = $(R.id.discount);
          airClass = $(R.id.airclass);
          flightNo = $(R.id.flight_no);
          airCompany = $(R.id.aircompany);
        }
      }
    }
  }
}
