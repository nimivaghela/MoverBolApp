package com.moverbol.views.activities.moveprocess;

import android.content.Context;
import android.content.Intent;
import androidx.databinding.DataBindingUtil;
import android.os.Bundle;

import com.moverbol.R;
import com.moverbol.adapters.PaymentHistoryAdapter;
import com.moverbol.custom.activities.BaseAppCompactActivity;
import com.moverbol.databinding.PaymentModeListBinding;
import com.moverbol.model.billOfLading.payment.PaymentHistoryDetailsModel;

import java.util.ArrayList;

public class PaymentHistoryActivity extends BaseAppCompactActivity {

    private static final String EXTRA_PAYMENT_HISTORY_LIST = "payment_history_list";
    private PaymentModeListBinding binding;
    private PaymentHistoryAdapter adapter;
    private ArrayList<PaymentHistoryDetailsModel> paymentHistoryDetailsModelList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initialisation();
        setActionListeners();
        setIntentData();


        setToolbar(binding.toolbar.toolbar, getString(R.string.payment_history), R.drawable.ic_arrow_back_white_24dp);
    }

    private void initialisation() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_payment_made_details);
        adapter = new PaymentHistoryAdapter();
        paymentHistoryDetailsModelList = new ArrayList<>();

        binding.setAdapter(adapter);
    }

    private void setActionListeners() {

    }

    private void setIntentData() {
        if(getIntent().hasExtra(EXTRA_PAYMENT_HISTORY_LIST)) {
            paymentHistoryDetailsModelList = getIntent().getParcelableArrayListExtra(EXTRA_PAYMENT_HISTORY_LIST);
            adapter.setHomeList(paymentHistoryDetailsModelList);
        }
    }

    public static void start(Context context, ArrayList<PaymentHistoryDetailsModel> paymentHistoryDetailsModelList) {
        Intent starter = new Intent(context, PaymentHistoryActivity.class);
        starter.putParcelableArrayListExtra(EXTRA_PAYMENT_HISTORY_LIST, paymentHistoryDetailsModelList);
        context.startActivity(starter);
    }

}
