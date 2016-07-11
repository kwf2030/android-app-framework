package com.kwf2030.android.business.example.ui;

import android.content.Context;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.kwf2030.android.R;
import com.kwf2030.android.app.AppActivity;
import com.kwf2030.android.app.AppListAdapter;
import com.kwf2030.android.app.AppViewHolder;
import com.kwf2030.android.business.example.view.FlightListView;
import com.kwf2030.android.util.MultiMap;

import java.util.*;

public class FlightFilterFragment extends BottomSheetDialogFragment {
  private static final String[] FILTER_CONST = new String[]{"起飞时段", "航空公司", "机型", "舱位", "到达机场"};

  private LinearLayout mRootLayout;

  private RecyclerView mCategoryView;
  private RecyclerView mOptionView;

  private CategoryAdapter mCategoryAdapter;
  private OptionAdapter mOptionAdapter;

  private MultiMap<String, String> mRawData;
  private MultiMap<String, String> mChecked;

  private String mCurrentCategory;
  private boolean mIsDirectOnly;

  static FlightFilterFragment create(@NonNull MultiMap<String, String> data) {
    Bundle args = new Bundle();
    args.putParcelable(Args.DATA, data);
    FlightFilterFragment ret = new FlightFilterFragment();
    ret.setArguments(args);
    return ret;
  }

  @Override
  public void onAttach(Context context) {
    super.onAttach(context);
  }

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    Bundle args = getArguments();
    if (args != null) {
      mRawData = args.getParcelable(Args.DATA);
    }
    mChecked = new MultiMap<>();
  }

  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    mRootLayout = (LinearLayout) inflater.inflate(R.layout.fragment_flight_filter, null);
    mCategoryView = (RecyclerView) mRootLayout.findViewById(R.id.flight_list_filter_category);
    mOptionView = (RecyclerView) mRootLayout.findViewById(R.id.flight_list_filter_option);
    mRootLayout.findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        cancel();
      }
    });
    mRootLayout.findViewById(R.id.confirm).setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        FlightListView.Filter result = confirm();
        Message msg = new Message();
        msg.what = 1;
        msg.obj = result;
        ((AppActivity) getActivity()).post(msg);
        cancel();
      }
    });
    ((AppCompatCheckBox) mRootLayout.findViewById(R.id.direct_only)).setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
      @Override
      public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        mIsDirectOnly = isChecked;
      }
    });
    return mRootLayout;
  }

  @Override
  public void onStart() {
    super.onStart();
    mCurrentCategory = "起飞时段";
    mCategoryAdapter = new CategoryAdapter(Arrays.asList(FILTER_CONST));
    mCategoryView.setAdapter(mCategoryAdapter);
    Collection<String> c = mRawData.get("起飞时段");
    if (c != null) {
      mOptionAdapter = new OptionAdapter(new ArrayList<>(c));
      mOptionView.setAdapter(mOptionAdapter);
    }
  }

  private FlightListView.Filter confirm() {
    return new FlightListView.FilterAdapter() {
      @Override
      public boolean isDirectOnly() {
        return mIsDirectOnly;
      }

      @NonNull
      @Override
      public Collection<String> getStartTime() {
        Collection<String> c = mChecked.get("起飞时段");
        return c == null ? Collections.<String>emptyList() : c;
      }

      @NonNull
      @Override
      public Collection<String> getArrivalTime() {
        Collection<String> c = mChecked.get("到达时段");
        return c == null ? Collections.<String>emptyList() : c;
      }

      @NonNull
      @Override
      public Collection<String> getStartAirport() {
        Collection<String> c = mChecked.get("起飞机场");
        return c == null ? Collections.<String>emptyList() : c;
      }

      @NonNull
      @Override
      public Collection<String> getArrivalAirport() {
        Collection<String> c = mChecked.get("到达机场");
        return c == null ? Collections.<String>emptyList() : c;
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
        Collection<String> c = mChecked.get("机型");
        return c == null ? Collections.<String>emptyList() : c;
      }

      @NonNull
      @Override
      public Collection<String> getAirClass() {
        Collection<String> c = mChecked.get("舱位");
        return c == null ? Collections.<String>emptyList() : c;
      }

      @NonNull
      @Override
      public Collection<String> getAirCompany() {
        Collection<String> c = mChecked.get("航空公司");
        return c == null ? Collections.<String>emptyList() : c;
      }
    };
  }

  private void cancel() {
    dismiss();
  }

  private void showCategory(String category) {
    Collection<String> options = mRawData.get(category);
    if (options == null) {
      return;
    }
    List<String> data;
    if (options instanceof List) {
      data = (List<String>) options;
    } else {
      data = new ArrayList<>(options);
    }
    mOptionAdapter.setItems(data);
    mOptionAdapter.notifyDataSetChanged();
  }

  private void selectCategory(@NonNull String category) {
    mCurrentCategory = category;
  }

  private void toggleOption(@NonNull String option, boolean checked) {
    if (checked) {
      mChecked.put(mCurrentCategory, option);
    } else {
      mChecked.remove(mCurrentCategory, option);
    }
  }

  private static class Args {
    private static final String DATA = "data";
  }

  /* ==================== Category Adapter ==================== */
  private class CategoryAdapter extends AppListAdapter<CategoryAdapter.ViewHolder, String> {
    CategoryAdapter(@NonNull List<String> items) {
      super(items);
    }

    @Override
    protected int getLayoutResource(int viewType) {
      return R.layout.item_flight_filter_category;
    }

    @NonNull
    @Override
    protected CategoryAdapter.ViewHolder getViewHolder(@NonNull View view, int viewType) {
      return new ViewHolder(view, viewType);
    }

    @Override
    protected void initViewHolder(@NonNull CategoryAdapter.ViewHolder holder, @NonNull String value) {
      holder.text.setText(value);
    }

    @Override
    protected void initViewHolderEvents(@NonNull final CategoryAdapter.ViewHolder holder, int position) {
      holder.itemView.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
          String category = holder.text.getText().toString();
          selectCategory(category);
          showCategory(category);
        }
      });
    }

    class ViewHolder extends AppViewHolder {
      TextView text;

      ViewHolder(View view, int viewType) {
        super(view, viewType);
      }

      @Override
      protected void initViews(int viewType) {
        text = $(R.id.filter_category);
      }
    }
  }

  /* ==================== Option Adapter ==================== */
  private class OptionAdapter extends AppListAdapter<OptionAdapter.ViewHolder, String> {
    OptionAdapter(@NonNull List<String> items) {
      super(items);
    }

    @Override
    protected int getLayoutResource(int viewType) {
      return R.layout.item_flight_filter_option;
    }

    @NonNull
    @Override
    protected OptionAdapter.ViewHolder getViewHolder(@NonNull View view, int viewType) {
      return new ViewHolder(view, viewType);
    }

    @Override
    protected void initViewHolder(@NonNull OptionAdapter.ViewHolder holder, @NonNull String value) {
      holder.icon.setVisibility(View.GONE);
      holder.text.setText(value);
      holder.checkBox.setChecked(false);
    }

    @Override
    protected void initViewHolderEvents(@NonNull final OptionAdapter.ViewHolder holder, final int position) {
      holder.itemView.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
          holder.checkBox.setChecked(!holder.checkBox.isChecked());
        }
      });
      holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
          toggleOption(holder.text.getText().toString(), isChecked);
        }
      });
    }

    void setItems(@NonNull List<String> items) {
      mItems = items;
    }

    class ViewHolder extends AppViewHolder {
      ImageView icon;
      TextView text;
      AppCompatCheckBox checkBox;

      ViewHolder(View view, int viewType) {
        super(view, viewType);
      }

      @Override
      protected void initViews(int viewType) {
        icon = $(R.id.flight_list_filter_img);
        text = $(R.id.flight_list_filter_text);
        checkBox = $(R.id.flight_list_filter_checkbox);
      }
    }
  }
}
