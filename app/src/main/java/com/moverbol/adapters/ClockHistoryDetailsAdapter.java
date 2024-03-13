package com.moverbol.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.moverbol.R;
import com.moverbol.constants.Constants;
import com.moverbol.databinding.ClockBreakHistoryItemBinding;
import com.moverbol.databinding.ItemFooterClockBreakHistoryBinding;
import com.moverbol.databinding.ItemHeaderClockBreackHistoryBinding;
import com.moverbol.interfaces.OnClickChildAdpter;
import com.moverbol.model.billOfLading.ClockHistoryModel;
import com.moverbol.util.MoversPreferences;
import com.moverbol.util.Util;

import java.util.ArrayList;
import java.util.List;


public class ClockHistoryDetailsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final int TYPE_HEADER = 0;
    private final int TYPE_ITEMS = 1;
    private final int TYPE_FOOTER = 2;

    private List<ClockHistoryModel> homeList;
    private OnClickChildAdpter onClickChildAdpter;
    private boolean isShowTotal = false;
    private Context context;
    private final String mActivityFlag;
    private int showCharges = 0;

    public ClockHistoryDetailsAdapter(boolean isBillingSummary, String activityFlag) {
        this.homeList = new ArrayList<>();
        this.mActivityFlag = activityFlag;
    }

    public void setShowChargesFlag(int chargesMoveTypeFlag) {
        this.showCharges = chargesMoveTypeFlag;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        if (viewType == TYPE_HEADER) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_header_clock_breack_history, parent, false);
            return new ViewHolderHeader(v);
        } else if (viewType == TYPE_ITEMS) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_clock_break_history_item, parent, false);
            return new ViewHolderItem(v);
        } else if (viewType == TYPE_FOOTER) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_footer_clock_break_history, parent, false);
            return new ViewHolderFooter(v);
        }
        throw new RuntimeException("there is no type that matches the type " + viewType + " + make sure your using types correctly");

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ViewHolderItem) {
            ViewHolderItem viewHolderItem = ((ViewHolderItem) holder);
            viewHolderItem.binding.setObj(homeList.get(position - 1));
            viewHolderItem.binding.setActivityFlag(mActivityFlag);
            viewHolderItem.binding.setIsShowTotal(isShowTotal);
            viewHolderItem.binding.setIsShowCharges(showCharges);

            //   if (isShowTotal || !MoversPreferences.getInstance(context).getBolStarted(MoversPreferences.getInstance(context).getCurrentJobId())) {

            if (isShowTotal) {
                viewHolderItem.binding.imgEdit.setVisibility(View.GONE);
                viewHolderItem.binding.imgDelete.setVisibility(View.GONE);
            } else {
                if (viewHolderItem.binding.getObj().fullEndTime().equalsIgnoreCase("On going")) {
                    viewHolderItem.binding.imgEdit.setVisibility(View.INVISIBLE);
                    viewHolderItem.binding.imgDelete.setVisibility(View.INVISIBLE);
                } else {
                    viewHolderItem.binding.imgEdit.setVisibility(View.VISIBLE);
                    viewHolderItem.binding.imgDelete.setVisibility(View.VISIBLE);
                }
            }
            viewHolderItem.binding.executePendingBindings();
        } else if (holder instanceof ViewHolderHeader) {
            ViewHolderHeader viewHolderHeader = ((ViewHolderHeader) holder);
            // if (isShowTotal || !MoversPreferences.getInstance(context).getBolStarted(MoversPreferences.getInstance(context).getCurrentJobId())) {
            if (isShowTotal) {
                viewHolderHeader.bindingHeader.imgEdit.setVisibility(View.GONE);
                viewHolderHeader.bindingHeader.imgDelete.setVisibility(View.GONE);
            } else {
                viewHolderHeader.bindingHeader.imgEdit.setVisibility(View.INVISIBLE);
                viewHolderHeader.bindingHeader.imgDelete.setVisibility(View.INVISIBLE);
            }
            viewHolderHeader.bindingHeader.setIsShowCharges(showCharges);
            viewHolderHeader.bindingHeader.setIsShowTotal(isShowTotal);
            viewHolderHeader.bindingHeader.executePendingBindings();
        } else if (holder instanceof ViewHolderFooter) {
            ViewHolderFooter viewHolderFooter = (ViewHolderFooter) holder;
            viewHolderFooter.bindingFooter.setTotalCharges(getTotalCharges());
            long totalHours = calculateGrandTotalHours();
            viewHolderFooter.bindingFooter.txtTotalHours.setText(Util.getGeneralFormattedDecimalString((double) calculateGrandTotalHours() / (1000 * 3600)));
            String mJobId = MoversPreferences.getInstance(context).getCurrentJobId();
            if (MoversPreferences.getInstance(context).getBolStarted(mJobId) && mActivityFlag.equalsIgnoreCase(Constants.TRUE)) {
                viewHolderFooter.bindingFooter.setIsConsolidationJob(true);
                String roundedTotalHours = getRoundedGrandTotalHours(totalHours);
                viewHolderFooter.bindingFooter.txtTotalRoundedHours.setText(roundedTotalHours);
                String rate = homeList.get(1).getRate_hour();
                viewHolderFooter.bindingFooter.txtTotalRate.setText(String.format(context.getString(R.string.dollar_value), MoversPreferences.getInstance(context).getCurrencySymbol(), rate));
                String roundedCharges = Util.getGeneralFormattedDecimalString(Double.parseDouble(rate) * Double.parseDouble(roundedTotalHours));
                viewHolderFooter.bindingFooter.txtTotalRoundedCharges.setText(String.format(context.getString(R.string.dollar_value), MoversPreferences.getInstance(context).getCurrencySymbol(), roundedCharges));


            } else {
                viewHolderFooter.bindingFooter.setIsConsolidationJob(false);
            }


        }

    }

    @Override
    public int getItemCount() {
        if (homeList.size() > 0) {
            return homeList.size() + 1;
        }
        return 0;
    }

    public void setOnClickChildAdapter(OnClickChildAdpter onClickChildAdpter) {
        this.onClickChildAdpter = onClickChildAdpter;
    }

    public void setHomeList(List<ClockHistoryModel> homeList) {
        this.homeList = homeList;
        notifyDataSetChanged();
    }

    public void showTotal(boolean isShowTotal) {
        if (getItemCount() > 0) {
            this.isShowTotal = isShowTotal;
            if (isShowTotal && showCharges == 1) {
                homeList.add(new ClockHistoryModel());
            } else {
                if (showCharges == 1) {
                    homeList.remove(homeList.size() - 1);
                }
            }
        }
        notifyDataSetChanged();
    }

    public String getTotalCharges() {
        double totalCharges = 0.0;
        for (ClockHistoryModel clockHistoryModel : homeList) {
            totalCharges = totalCharges + Double.parseDouble(Util.removeFormatAmount(clockHistoryModel.displayCharge(context, mActivityFlag)));
        }
        return Util.getGeneralFormattedDecimalString(totalCharges);
    }

    private long calculateGrandTotalHours() {
        long totalHours = 0;
        for (ClockHistoryModel clockHistoryModel : homeList) {
            if (clockHistoryModel.getIsBillable() != null && clockHistoryModel.getIsBillable().equalsIgnoreCase(Constants.TRUE)) {
                totalHours = totalHours + clockHistoryModel.calculateTotalHour();
            }
        }
        return totalHours;
    }

    private String getRoundedGrandTotalHours(long totalHours) {
        String mJobId = MoversPreferences.getInstance(context).getCurrentJobId();
        return Util.getGeneralFormattedDecimalString((Util.setMinuteInterval(totalHours, Double.parseDouble(MoversPreferences.getInstance(context).getDefaultMinHours(mJobId)), MoversPreferences.getInstance(context).getIncrementMinValue(mJobId))) / (1000 * 3600));
    }


    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return TYPE_HEADER;
        } else if (isShowTotal && position == homeList.size() && showCharges == 1) {
            return TYPE_FOOTER;
        }
        return TYPE_ITEMS;
    }

    public List<ClockHistoryModel> getHomeList() {
        return homeList;
    }

    public ClockHistoryModel getItem(int position) {
        return homeList.get(position);
    }


    private class ViewHolderItem extends RecyclerView.ViewHolder {

        private final ClockBreakHistoryItemBinding binding;

        ViewHolderItem(View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);
            if (binding != null) {
                binding.imgEdit.setOnClickListener(v -> onClickChildAdpter.onclickAdapter(v, getAdapterPosition() - 1));
                binding.imgDelete.setOnClickListener(v -> onClickChildAdpter.onclickAdapter(v, getAdapterPosition() - 1));
            }
        }
    }

    private class ViewHolderHeader extends RecyclerView.ViewHolder {
        private final ItemHeaderClockBreackHistoryBinding bindingHeader;

        ViewHolderHeader(View itemView) {
            super(itemView);
            bindingHeader = DataBindingUtil.bind(itemView);
        }
    }

    class ViewHolderFooter extends RecyclerView.ViewHolder {
        private final ItemFooterClockBreakHistoryBinding bindingFooter;

        ViewHolderFooter(@NonNull View itemView) {
            super(itemView);
            bindingFooter = DataBindingUtil.bind(itemView);
        }
    }

}
