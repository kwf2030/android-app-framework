package com.kwf2030.android.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import com.kwf2030.android.R;
import com.kwf2030.android.util.Nulls;
import com.kwf2030.android.util.Tuple2;
import org.joda.time.LocalDate;

import java.util.*;

//TODO text size from xml has bug
//TODO setting of first day not implemented
public class CalendarView extends ViewPager {
  private float mDayTextSize;
  private float mTodayTextSize;
  private float mWeekTextSize;
  private float mMonthTextSize;
  private float mText1TextSize;
  private float mText2TextSize;

  private int mDayTextColor;
  private int mTodayTextColor;
  private int mWeekTextColor;
  private int mMonthTextColor;
  private int mText1TextColor;
  private int mText2TextColor;

  private int mDayBackgroundColor;
  private int mTodayBackgroundColor;
  private int mWeekBackgroundColor;
  private int mMonthBackgroundColor;
  private int mText1BackgroundColor;
  private int mText2BackgroundColor;

  private int mDisabledTextColor;

  private int mSelectedDayBackground;

  private Context mContext;

  private List<View> mPagerViews;

  private LocalDate mSelectedDate;

  private int mFirstDayOfWeek;

  private String[] mOrderedDayOfWeekCn;

  private Callback mCallback;

  private int mMonths = 12;

  private LocalDate mToday = LocalDate.now();

  private String mMonthFormat = "yyyy - MM";

  private Map<LocalDate, Tuple2<String, String>> mBadge;

  private OnClickListener mPreviousMonth = new OnClickListener() {
    @Override
    public void onClick(View v) {
      int i = getCurrentItem();
      if (i > 0) {
        setCurrentItem(i - 1);
      }
    }
  };

  private OnClickListener mNextMonth = new OnClickListener() {
    @Override
    public void onClick(View v) {
      int i = getCurrentItem();
      if (i < mMonths - 1) {
        setCurrentItem(i + 1);
      }
    }
  };

  private OnPageChangeListener mPageChangeListener = new OnPageChangeListener() {
    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public void onPageSelected(int position) {
    }

    @Override
    public void onPageScrollStateChanged(int state) {
    }
  };

  public CalendarView(Context context) {
    this(context, null);
  }

  public CalendarView(Context context, AttributeSet attrs) {
    super(context, attrs);
    mContext = context;

    float textSize = 16;
    float textSizeSmall = 10;
    float textSizeMonth = 22;

    int textColor = Color.parseColor("#000000");
    int textColorToday = Color.parseColor("#FF0000");

    int backgroundColor = Color.parseColor("#00000000");

    TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CalendarView);

    mDayTextSize = a.getDimension(R.styleable.CalendarView_dayTextSize, textSize);
    mTodayTextSize = a.getDimension(R.styleable.CalendarView_todayTextSize, textSize);
    mWeekTextSize = a.getDimension(R.styleable.CalendarView_weekTextSize, textSize);
    mMonthTextSize = a.getDimension(R.styleable.CalendarView_monthTextSize, textSizeMonth);
    mText1TextSize = a.getDimension(R.styleable.CalendarView_text1TextSize, textSizeSmall);
    mText2TextSize = a.getDimension(R.styleable.CalendarView_text2TextSize, textSizeSmall);

    mDayTextColor = a.getColor(R.styleable.CalendarView_dayTextColor, textColor);
    mTodayTextColor = a.getColor(R.styleable.CalendarView_todayTextColor, textColorToday);
    mWeekTextColor = a.getColor(R.styleable.CalendarView_weekTextColor, textColor);
    mMonthTextColor = a.getColor(R.styleable.CalendarView_monthTextColor, textColor);
    mText1TextColor = a.getColor(R.styleable.CalendarView_text1TextColor, textColor);
    mText2TextColor = a.getColor(R.styleable.CalendarView_text2TextColor, textColor);

    mDayBackgroundColor = a.getColor(R.styleable.CalendarView_dayBackgroundColor, backgroundColor);
    mTodayBackgroundColor = a.getColor(R.styleable.CalendarView_todayBackgroundColor, backgroundColor);
    mWeekBackgroundColor = a.getColor(R.styleable.CalendarView_weekBackgroundColor, backgroundColor);
    mMonthBackgroundColor = a.getColor(R.styleable.CalendarView_monthBackgroundColor, backgroundColor);
    mText1BackgroundColor = a.getColor(R.styleable.CalendarView_text1BackgroundColor, backgroundColor);
    mText2BackgroundColor = a.getColor(R.styleable.CalendarView_text2BackgroundColor, backgroundColor);

    mDisabledTextColor = a.getColor(R.styleable.CalendarView_disabledTextColor, Color.parseColor("#9F9F9F"));
    mSelectedDayBackground = a.getColor(R.styleable.CalendarView_selectedBackgroundColor, Color.parseColor("#C4C4C4"));

    a.recycle();
  }

  @NonNull
  public CalendarView today(@NonNull Date today) {
    mToday = LocalDate.fromDateFields(today);
    return this;
  }

  @NonNull
  public Date today() {
    return mToday.toDate();
  }

  @NonNull
  public CalendarView months(int months) {
    mMonths = months;
    return this;
  }

  public int months() {
    return mMonths;
  }

  @NonNull
  public CalendarView monthFormat(@NonNull String format) {
    mMonthFormat = format;
    return this;
  }

  @NonNull
  public String monthFormat() {
    return mMonthFormat;
  }

  @NonNull
  public CalendarView selectedDate(@NonNull Date date) {
    mSelectedDate = LocalDate.fromDateFields(date);
    return this;
  }

  @Nullable
  public Date selectedDate() {
    return mSelectedDate == null ? null : mSelectedDate.toDate();
  }

  @NonNull
  public CalendarView firstDayOfWeek(int day) {
    if (day < 0 || day > 6) {
      throw new RuntimeException("invalid day of week, must be 0-6");
    }

    if (mOrderedDayOfWeekCn == null) {
      mOrderedDayOfWeekCn = new String[7];
    }

    mFirstDayOfWeek = day;

    mOrderedDayOfWeekCn[0] = getDayOfWeekCn(day++ % 7);
    mOrderedDayOfWeekCn[1] = getDayOfWeekCn(day++ % 7);
    mOrderedDayOfWeekCn[2] = getDayOfWeekCn(day++ % 7);
    mOrderedDayOfWeekCn[3] = getDayOfWeekCn(day++ % 7);
    mOrderedDayOfWeekCn[4] = getDayOfWeekCn(day++ % 7);
    mOrderedDayOfWeekCn[5] = getDayOfWeekCn(day++ % 7);
    mOrderedDayOfWeekCn[6] = getDayOfWeekCn(day % 7);

    return this;
  }

  public int firstDayOfWeek() {
    return mFirstDayOfWeek;
  }

  @NonNull
  public CalendarView badge(@NonNull Map<Date, Tuple2<String, String>> badge) {
    if (mBadge == null) {
      mBadge = new HashMap<>();
    }

    mBadge.clear();

    for (Map.Entry<Date, Tuple2<String, String>> entry : badge.entrySet()) {
      mBadge.put(LocalDate.fromDateFields(entry.getKey()), entry.getValue());
    }

    return this;
  }

  @NonNull
  public CalendarView callback(@NonNull Callback callback) {
    mCallback = callback;
    return this;
  }

  public void show() {
    if (mPagerViews == null) {
      mPagerViews = new ArrayList<>(mMonths);
    }

    mPagerViews.clear();

    LocalDate today = mToday.dayOfMonth().withMinimumValue();
    LocalDate date;

    for (int i = 0; i < mMonths; i++) {
      date = today.plusMonths(i);
      View view = LayoutInflater.from(getContext()).inflate(R.layout.view_calendar_layout, null);

      Button prev = (Button) view.findViewById(R.id.previous_month);
      Button next = (Button) view.findViewById(R.id.next_month);
      if (i != 0) {
        prev.setOnClickListener(mPreviousMonth);
      }
      if (i != mMonths - 1) {
        next.setOnClickListener(mNextMonth);
      }

      TextView month = (TextView) view.findViewById(R.id.month);
      month.setTextSize(mMonthTextSize);
      month.setTextColor(mMonthTextColor);
      month.setBackgroundColor(mMonthBackgroundColor);
      month.setText(date.toString(mMonthFormat));

      RecyclerView weekView = (RecyclerView) view.findViewById(R.id.week);
      weekView.setLayoutManager(new GridLayoutManager(mContext, 7));
      weekView.setAdapter(new WeekAdapter());

      RecyclerView dayView = (RecyclerView) view.findViewById(R.id.days);
      dayView.setLayoutManager(new GridLayoutManager(mContext, 7));
      dayView.setAdapter(new DayAdapter(date));

      mPagerViews.add(view);
    }

    setAdapter(new CalendarPagerAdapter());
    setCurrentItem(0);
    addOnPageChangeListener(mPageChangeListener);
  }

  @NonNull
  private String getDayOfWeekCn(int day) {
    switch (day) {
      case 0:
        return "日";

      case 1:
        return "一";

      case 2:
        return "二";

      case 3:
        return "三";

      case 4:
        return "四";

      case 5:
        return "五";

      case 6:
        return "六";
    }

    throw new RuntimeException("invalid day of week, must be 0-6");
  }

  private class CalendarPagerAdapter extends PagerAdapter {
    @Override
    public int getCount() {
      return mMonths;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
      return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
      View view = mPagerViews.get(position);
      container.addView(view);
      return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
      container.removeView(mPagerViews.get(position));
    }
  }

  private class DayAdapter extends RecyclerView.Adapter<DayHolder> {
    private int mTotalDays;
    private List<LocalDate> mDays;

    private DayAdapter(LocalDate date) {
      int firstDay = date.dayOfMonth().withMinimumValue().dayOfWeek().get();
      firstDay = firstDay == 7 ? 0 : firstDay;

      mTotalDays = date.dayOfMonth().getMaximumValue() + firstDay;

      mDays = new ArrayList<>(mTotalDays);
      for (int i = 0; i < mTotalDays; i++) {
        if (i < firstDay) {
          mDays.add(null);
        } else {
          mDays.add(date.plusDays(i - firstDay));
        }
      }
    }

    @Nullable
    private LocalDate getDay(int position) {
      return mDays.get(position);
    }

    @Override
    public DayHolder onCreateViewHolder(ViewGroup parent, int viewType) {
      View view = LayoutInflater.from(mContext).inflate(R.layout.view_calendar_day, null);
      DayHolder holder = new DayHolder(view, this);
      holder.init();
      return holder;
    }

    @Override
    public void onBindViewHolder(DayHolder holder, int position) {
      LocalDate day = mDays.get(position);

      if (day == null) {
        holder.day.setVisibility(View.GONE);
        return;
      }

      holder.day.setBackgroundColor(mDayBackgroundColor);
      holder.date.setTextSize(mDayTextSize);
      holder.date.setTextColor(mDayTextColor);

      holder.text1.setVisibility(View.GONE);
      holder.text2.setVisibility(View.GONE);

      if (day.isBefore(mToday)) {
        holder.day.setEnabled(false);
        holder.date.setTextColor(mDisabledTextColor);
        holder.text1.setTextColor(mDisabledTextColor);

      } else if (day.equals(mToday)) {
        holder.day.setBackgroundColor(mTodayBackgroundColor);
        holder.date.setTextSize(mTodayTextSize);
        holder.date.setTextColor(mTodayTextColor);
      }

      if (mSelectedDate != null && mSelectedDate.equals(day)) {
        holder.day.setBackgroundColor(mSelectedDayBackground);
      }

      holder.date.setText(day.dayOfMonth().getAsText());

      if (mBadge != null && mBadge.get(day) != null) {
        Tuple2<String, String> tuple = mBadge.get(day);
        if (!Nulls.isEmpty(tuple.data1)) {
          holder.text1.setVisibility(View.VISIBLE);
          holder.text1.setText(tuple.data1);
        }
        if (!Nulls.isEmpty(tuple.data2)) {
          holder.text2.setVisibility(View.VISIBLE);
          holder.text2.setText(tuple.data2);
        }
      }
    }

    @Override
    public int getItemCount() {
      return mTotalDays;
    }
  }

  private class DayHolder extends RecyclerView.ViewHolder {
    private DayAdapter mAdapter;

    FrameLayout day;
    TextView date, text1, text2;

    private DayHolder(View itemView, DayAdapter adapter) {
      super(itemView);
      mAdapter = adapter;
    }

    private void init() {
      day = (FrameLayout) itemView.findViewById(R.id.day);
      date = (TextView) itemView.findViewById(R.id.date);
      text1 = (TextView) itemView.findViewById(R.id.text1);
      text2 = (TextView) itemView.findViewById(R.id.text2);

      text1.setTextSize(mText1TextSize);
      text1.setTextColor(mText1TextColor);
      text1.setBackgroundColor(mText1BackgroundColor);

      text2.setTextSize(mText2TextSize);
      text2.setTextColor(mText2TextColor);
      text2.setBackgroundColor(mText2BackgroundColor);

      itemView.setOnClickListener(new OnClickListener() {
        @Override
        public void onClick(View v) {
          mSelectedDate = mAdapter.getDay(getLayoutPosition());
          mAdapter.notifyDataSetChanged();
          if (mCallback != null) {
            mCallback.onDateSelected(mSelectedDate.toDate());
          }
        }
      });
    }
  }

  private class WeekAdapter extends RecyclerView.Adapter<WeekHolder> {
    @Override
    public WeekHolder onCreateViewHolder(ViewGroup parent, int viewType) {
      View view = LayoutInflater.from(mContext).inflate(R.layout.view_calendar_day, null);
      WeekHolder holder = new WeekHolder(view);
      holder.init();
      return holder;
    }

    @Override
    public void onBindViewHolder(WeekHolder holder, int position) {
      holder.date.setTextSize(mWeekTextSize);
      holder.date.setTextColor(mWeekTextColor);
      holder.day.setBackgroundColor(mWeekBackgroundColor);

      int day = (mFirstDayOfWeek + position) % 7;
      if (day == 0 || day == 6) {
        holder.date.setTextColor(mTodayTextColor);
      }

      holder.date.setText(getDayOfWeekCn(day));
    }

    @Override
    public int getItemCount() {
      return 7;
    }
  }

  private class WeekHolder extends RecyclerView.ViewHolder {
    FrameLayout day;
    TextView date, text1, text2;

    private WeekHolder(View itemView) {
      super(itemView);
    }

    private void init() {
      day = (FrameLayout) itemView.findViewById(R.id.day);
      date = (TextView) itemView.findViewById(R.id.date);
      text1 = (TextView) itemView.findViewById(R.id.text1);
      text2 = (TextView) itemView.findViewById(R.id.text2);

      text1.setVisibility(View.GONE);
      text2.setVisibility(View.GONE);
    }
  }

  public interface Callback {
    void onDateSelected(@NonNull Date date);
  }
}
