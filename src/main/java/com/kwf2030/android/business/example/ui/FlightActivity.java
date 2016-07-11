package com.kwf2030.android.business.example.ui;

import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentTransaction;
import com.kwf2030.android.R;
import com.kwf2030.android.app.AppActivity;
import com.kwf2030.android.util.MultiMap;

public class FlightActivity extends AppActivity {
  @Override
  protected int getLayoutResource() {
    return R.layout.activity_flight;
  }

  @Override
  protected boolean shouldShowToolbarBackButton() {
    return false;
  }

  @NonNull
  @Override
  protected String getToolbarTitle() {
    return "南京 至 北京";
  }

  @Override
  protected boolean shouldEnableSwipeRefresh() {
    return false;
  }

  @Override
  protected boolean shouldShowBottombar() {
    return false;
  }

  @Override
  protected boolean shouldShowFloatingActionButton() {
    return true;
  }

  @Override
  protected void onFloatingActionButtonClick() {
    FragmentTransaction trans = mFragmentManager.beginTransaction();
    FlightFilterFragment f = FlightFilterFragment.create(mockData());
    trans.add(f, "FlightFilterFragment").addToBackStack(null).commit();
  }

  @Override
  public boolean handleMessage(@NonNull Message msg) {
    if (msg.what == 1) {
      postToFragment("FlightListFragment", msg);
    }
    return true;
  }

  private MultiMap<String, String> mockData() {
    MultiMap<String, String> map = new MultiMap<>();
    map.put("起飞时段", "不限");
    map.put("起飞时段", "00:00 - 06:00");
    map.put("起飞时段", "06:00 - 12:00");
    map.put("起飞时段", "12:00 - 18:00");
    map.put("起飞时段", "18:00 - 24:00");
    map.put("航空公司", "不限");
    map.put("航空公司", "东方航空");
    map.put("航空公司", "南方航空");
    map.put("航空公司", "亚航");
    map.put("航空公司", "海南航空");
    map.put("航空公司", "吉祥航空");
    map.put("机型", "不限");
    map.put("机型", "大型机");
    map.put("机型", "中型机");
    map.put("机型", "小型机");
    map.put("舱位", "不限");
    map.put("舱位", "经济舱");
    map.put("舱位", "公务舱");
    map.put("舱位", "头等舱");
    map.put("到达机场", "不限");
    map.put("到达机场", "北京首都机场");
    map.put("到达机场", "北京南苑机场");
    return map;
  }
}
