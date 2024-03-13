package com.moverbol.model.billOfLading;

import android.content.Context;
import android.text.TextUtils;

import com.moverbol.R;
import com.moverbol.constants.Constants;
import com.moverbol.model.billOfLading.newRequestChargesMoleds.CommonChargesRequestModel;
import com.moverbol.util.MoversPreferences;
import com.moverbol.util.Util;

import java.text.DecimalFormat;

/**
 * Created by AkashM on 12/3/18.
 */

public class BillOfLadingPojo {

    private BolDetailsPojo jobConfirmationDetailsPojo;
    private CommonChargesRequestModel moveChargesCalculated;

    private CommonChargesRequestModel valuationChargesCalculated;


    private int moveChargesPriceType;
    private double totalCalculatedMoveCharges;
    private double totalCalculatedValuationCharges;
    private boolean isNewValuationCalculated = false;

    private CouponDetailsModel couponDetailsModel;

    private boolean shouldShowDiscount;

    private boolean moveChargeChanged;

    private boolean storageChargeChanged;

    private boolean additionalChargeChanged;

    private boolean valuationChargeChanged;

    private boolean packingChargeChanged;

    private boolean cratingChargeChanged;

    private boolean storageRecurringChargeChanged;

    private double actualHours;

    private double actualTravelTime;

    public BillOfLadingPojo() {
    }

    public BolDetailsPojo getJobConfirmationDetailsPojo() {
        return jobConfirmationDetailsPojo;
    }

    public void setJobConfirmationDetailsPojo(BolDetailsPojo jobConfirmationDetailsPojo) {
        this.jobConfirmationDetailsPojo = jobConfirmationDetailsPojo;
    }


    public int getMoveChargesPriceType() {
        return moveChargesPriceType;
    }

    public void setMoveChargesPriceType(int moveChargesPriceType) {
        this.moveChargesPriceType = moveChargesPriceType;
    }


    public double getTotalCalculatedMoveCharges() {
        DecimalFormat decimalFormat = new DecimalFormat("#.##");
        return Double.parseDouble(decimalFormat.format(totalCalculatedMoveCharges));
    }

    public double getTotalCalculatedMoveDiscounted(Context context) {
        String jobId = MoversPreferences.getInstance(context).getCurrentJobId();
        double moverCharge;
        if (MoversPreferences.getInstance(context).isClockIsOn(jobId)) {
            moverCharge = Double.parseDouble(jobConfirmationDetailsPojo.getMinCharges());
        } else if (MoversPreferences.getInstance(context).getBolStarted(jobId)) {
            moverCharge = getTotalCalculatedMoveCharges();
        } else {

            if (jobConfirmationDetailsPojo.isClockStartFlag()) {
                if (jobConfirmationDetailsPojo.getMoveCharges() >= Double.parseDouble(jobConfirmationDetailsPojo.getMinCharges())) {
                    moverCharge = getTotalCalculatedMoveCharges();
                } else {
                    moverCharge = Double.parseDouble(jobConfirmationDetailsPojo.getMinCharges());
                }
            } else {
                moverCharge = getTotalCalculatedMoveCharges();
            }
        }
        return calculateTotalWithDiscount(moverCharge, jobConfirmationDetailsPojo.getMoveChargeDiscountType(), jobConfirmationDetailsPojo.getMoveChargeDiscountValue());

    }

    public String displayMoveCharge(Context context) {

        String minCharge = "$" + Util.getGeneralFormattedDecimalString(getTotalCalculatedMoveDiscounted(context)) + " (Min)";
        String totalMoveCharge = "$" + Util.getGeneralFormattedDecimalString(getTotalCalculatedMoveDiscounted(context));

        String jobId = MoversPreferences.getInstance(context).getCurrentJobId();
        if (MoversPreferences.getInstance(context).isClockIsOn(jobId)) {
            return minCharge;
        } else if (MoversPreferences.getInstance(context).getBolStarted(jobId)) {
            return totalMoveCharge;

        } else if (jobConfirmationDetailsPojo.isClockStartFlag() && jobConfirmationDetailsPojo.getMoveCharges() <= Double.parseDouble(jobConfirmationDetailsPojo.getMinCharges())) {
            return minCharge;
        }
        return totalMoveCharge;
    }


    public void setTotalCalculatedMoveCharges(double totalCalculatedMoveCharges) {
        this.totalCalculatedMoveCharges = totalCalculatedMoveCharges;
    }

    public double getTotalCalculatedValuationCharges() {
        DecimalFormat decimalFormat = new DecimalFormat("#.##");
        return Double.parseDouble(decimalFormat.format(this.totalCalculatedValuationCharges));
//        return totalCalculatedValuationCharges;
    }

    public void setTotalCalculatedValuationCharges(double totalCalculatedValuationCharges) {
        this.totalCalculatedValuationCharges = totalCalculatedValuationCharges;
    }


    public double getTotal(Context context) {
        double amountDue = getTotalCharges(context) - this.jobConfirmationDetailsPojo.getDepositeAmount();
        if (amountDue > 0) {
            return amountDue;
        } else {
            return 0;
        }
    }


    public double getTotalCharges(Context context) {
        double total = getSubTotal(context);

        double discount = calculateBottomLineDiscount(context);

        double couponDiscount = 0;

        if (this.jobConfirmationDetailsPojo.getHasCoupon() && this.couponDetailsModel != null) {
            if (this.isCouponTypePercentage()) {
                couponDiscount = (this.couponDetailsModel.getCouponValue() / 100) * total;
            } else {
                couponDiscount = this.couponDetailsModel.getCouponValue();
            }
        }

        double totalWithDiscounts = total - discount - couponDiscount;

        double tax = 0;

        if (this.jobConfirmationDetailsPojo.getServiceTaxValue() != 0) {
            tax = calculateServiceTax(context);
        }

        return Util.getFormattedDouble((totalWithDiscounts + tax), Constants.DoubleFormats.FORMAT_FOR_DIGITS);
    }

    public boolean isNewValuationCalculated() {
        return isNewValuationCalculated;
    }

    public void setNewValuationCalculated(boolean newValuationCalculated) {
        isNewValuationCalculated = newValuationCalculated;
    }

    public CouponDetailsModel getCouponDetailsModel() {
        return couponDetailsModel;
    }

    public void setCouponDetailsModel(CouponDetailsModel couponDetailsModel) {
        this.couponDetailsModel = couponDetailsModel;
    }

    public boolean isCouponTypePercentage() {
        return this.couponDetailsModel != null && this.couponDetailsModel.getCouponType() != null && TextUtils.equals(this.couponDetailsModel.getCouponType(), Constants.CouponTypes.COUPON_TYPE_PERCENTAGE);
    }

    public boolean isShouldShowDiscount() {
        return shouldShowDiscount;
    }

    public void setShouldShowDiscount(boolean shouldShowDiscount) {
        this.shouldShowDiscount = shouldShowDiscount;
    }

    public boolean isMoveChargeChanged() {
        return moveChargeChanged;
    }

    public void setMoveChargeChanged(boolean moveChargeChanged) {
        this.moveChargeChanged = moveChargeChanged;
    }

    public boolean isStorageChargeChanged() {
        return storageChargeChanged;
    }

    public void setStorageChargeChanged(boolean storageChargeChanged) {
        this.storageChargeChanged = storageChargeChanged;
    }

    public boolean isAdditionalChargeChanged() {
        return additionalChargeChanged;
    }

    public void setAdditionalChargeChanged(boolean additionalChargeChanged) {
        this.additionalChargeChanged = additionalChargeChanged;
    }

    public boolean isValuationChargeChanged() {
        return valuationChargeChanged;
    }

    public void setValuationChargeChanged(boolean valuationChargeChanged) {
        this.valuationChargeChanged = valuationChargeChanged;
    }

    public boolean isPackingChargeChanged() {
        return packingChargeChanged;
    }

    public void setPackingChargeChanged(boolean packingChargeChanged) {
        this.packingChargeChanged = packingChargeChanged;
    }

    public boolean isCratingChargeChanged() {
        return cratingChargeChanged;
    }

    public void setCratingChargeChanged(boolean cratingChargeChanged) {
        this.cratingChargeChanged = cratingChargeChanged;
    }

    public boolean isStorageRecurringChargeChanged() {
        return storageRecurringChargeChanged;
    }

    public void setStorageRecurringChargeChanged(boolean storageRecurringChargeChanged) {
        this.storageRecurringChargeChanged = storageRecurringChargeChanged;
    }

    public CommonChargesRequestModel getMoveChargesCalculated() {
        return moveChargesCalculated;
    }

    public void setMoveChargesCalculated(CommonChargesRequestModel moveChargesCalculated) {
        this.moveChargesCalculated = moveChargesCalculated;
    }

    public CommonChargesRequestModel getValuationChargesCalculated() {
        return valuationChargesCalculated;
    }

    public void setValuationChargesCalculated(CommonChargesRequestModel valuationChargesCalculated) {
        this.valuationChargesCalculated = valuationChargesCalculated;
    }


    public double getActualHours() {
        return actualHours;
    }

    public void setActualHours(double actualHours) {
        this.actualHours = actualHours;
    }

    public double getActualTravelTime() {
        return actualTravelTime;
    }

    public void setActualTravelTime(double actualTravelTime) {
        this.actualTravelTime = actualTravelTime;
    }

    public double calculateTotalWithDiscount(double amountToSubtractDiscountFrom, int discountType, double discountValue) {
        double totalValue = amountToSubtractDiscountFrom;
        if (discountType == Constants.NumValueTypes.NUM_VALUE_TYPE_AMOUNT) {
            totalValue = amountToSubtractDiscountFrom - discountValue;
        } else if (discountType == Constants.NumValueTypes.NUM_VALUE_TYPE_PERCENTAGE) {
            totalValue = amountToSubtractDiscountFrom - ((discountValue * amountToSubtractDiscountFrom) / 100);
        }

        if (totalValue < 0) {
            totalValue = 0;
        }

        return totalValue;
    }

    public double totalWithMinDeposit(Context context) {
        return getTotalCharges(context) - jobConfirmationDetailsPojo.getDepositeAmount();
    }

    public double calculateCardConvenienceFee(Context context) {
        if (jobConfirmationDetailsPojo.getCreditCardConvinienceFeeType()) {
            return jobConfirmationDetailsPojo.getCreditCardConvinienceFeeValue();
        } else {
            double convenienceFeeValue = 0.0;
            if (totalWithMinDeposit(context) > 0) {
                convenienceFeeValue = (totalWithMinDeposit(context) * jobConfirmationDetailsPojo.getCreditCardConvinienceFeeValue()) / 100;
            }
            return Util.getFormattedDouble(convenienceFeeValue, Constants.DoubleFormats.FORMAT_FOR_DIGITS);
        }
    }


    public String getConvenienceFeeString(String currencySymbol, Context context) {
        double cardConvenienceFee = calculateCardConvenienceFee(context);
        if (jobConfirmationDetailsPojo.getCreditCardConvinienceFeeType()) {
            return currencySymbol + cardConvenienceFee;
        } else {
            return jobConfirmationDetailsPojo.getCreditCardConvinienceFeeValue() + "%" + " " + currencySymbol + cardConvenienceFee;
        }
    }

    public double getSubTotal(Context context) {
        double subTotal = getTotalCalculatedMoveDiscounted(context) + jobConfirmationDetailsPojo.getPackingMaterialChargesDiscounted() + jobConfirmationDetailsPojo.getAdditionalChargesDiscounted() + jobConfirmationDetailsPojo.getCratingChargesDiscounted();
        /*  if (!jobConfirmationDetailsPojo.shouldHideValuationAndStorage()) {*/
        subTotal = subTotal + jobConfirmationDetailsPojo.getStorageChargesDiscounted() + jobConfirmationDetailsPojo.getStorageChargesRecurringDiscounted() + getTotalCalculatedValuationCharges();
        /*}*/
        return Util.getFormattedDouble(subTotal, Constants.DoubleFormats.FORMAT_FOR_DIGITS);
    }

    public String displayServiceTax(Context context) {
        String serviceTax = Util.getGeneralFormattedDecimalString(calculateServiceTax(context));
        if (jobConfirmationDetailsPojo.getServiceTaxValueType()) {
            return String.format(context.getString(R.string.dollar_value), MoversPreferences.getInstance(context).getCurrencySymbol(), serviceTax);
        } else {
            return Util.getGeneralFormattedDecimalString(jobConfirmationDetailsPojo.getServiceTaxValue()) + "% " + String.format(context.getString(R.string.dollar_value), MoversPreferences.getInstance(context).getCurrencySymbol(), serviceTax);
        }
    }

    public String displayBottomLineDiscount(Context context) {
        String discount = Util.getGeneralFormattedDecimalString(calculateBottomLineDiscount(context));
        if (jobConfirmationDetailsPojo.getBottomLineChargeDiscountType()) {
            return String.format(context.getString(R.string.dollar_value), MoversPreferences.getInstance(context).getCurrencySymbol(), discount);
        } else {
            return Util.getGeneralFormattedDecimalString(jobConfirmationDetailsPojo.getBottomLineChargeDiscountValue()) + "%" + " ($" + discount + ")";
        }
    }

    public double calculateBottomLineDiscount(Context context) {
        double discount = 0;

        if (jobConfirmationDetailsPojo.getBottomLineChargeDiscountType()) {
            discount = jobConfirmationDetailsPojo.getBottomLineChargeDiscountValue();
        } else /*if (this.bottomLineChargeDiscountType == Constants.BolDiscountFlags.DISCOUNT_FLAG_PERCENTAGE)*/ {
            discount = ((jobConfirmationDetailsPojo.getBottomLineChargeDiscountValue() * getSubTotal(context)) / 100);
        }
        return Util.getFormattedDouble(discount, Constants.DoubleFormats.FORMAT_FOR_DIGITS);
    }

    public double calculateServiceTax(Context context) {
        double tax = 0;
        if (jobConfirmationDetailsPojo.getServiceTaxValueType()) {
            tax = jobConfirmationDetailsPojo.getServiceTaxValue();
        } else {
            // Minus sales tax in total Amount Prabhu (23 Sep 2021)
            double totalWithDiscount = getSubTotal(context) - calculateBottomLineDiscount(context) - jobConfirmationDetailsPojo.getPackingChargeSalesTax();
            //double totalWithDiscount = getSubTotal(context) - calculateBottomLineDiscount(context);
            tax = (totalWithDiscount * jobConfirmationDetailsPojo.getServiceTaxValue()) / 100;
        }
        return Util.getFormattedDouble(tax, Constants.DoubleFormats.FORMAT_FOR_DIGITS);
    }
}
