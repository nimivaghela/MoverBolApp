package com.moverbol.custom.dialogs;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.moverbol.R;
import com.moverbol.adapters.CustomSpinnerBolAdapter;
import com.moverbol.databinding.CalculateETABinding;
import com.moverbol.model.forETA.ETAResponseModel;
import com.moverbol.model.moveProcess.AddressListResponseModel;
import com.moverbol.viewmodels.moveProcess.MoveProcessViewModel;

import java.util.ArrayList;
import java.util.List;


public class CalculateETADialog extends BaseDialogFragment {

    private static final String PHONE_NUMBER_KEY = "phone_number_key";
    private static final String SELECTED_ADDRESS_KEY = "selected_address_key";
    private CalculateETABinding binding;
    private List<String> addressList;
    private MoveProcessViewModel viewModel;
    private ETACalculationClickListener listener;

    private String mEtaString = "";
    private String mPhoneNumber = "";
    private String mSelectedAddress;
    private boolean mShouldShowSendETA;

    private CustomSpinnerBolAdapter<String> mSpinnerBolAdapter;

    public static void startTOCalculateETA(FragmentManager fragmentManager, String phoneNumber, List<String> addressList) {
        CalculateETADialog dialog = new CalculateETADialog();

        dialog.addressList = addressList;
        dialog.mShouldShowSendETA = false;
        dialog.mPhoneNumber = phoneNumber;

        dialog.show(fragmentManager, "dialog");
    }

    public static void startToSendETA(FragmentManager fragmentManager, List<String> addressList, String destinationAddress, String etaString) {
        CalculateETADialog dialog = new CalculateETADialog();

        dialog.addressList = addressList;
        dialog.mEtaString = etaString;

        dialog.mSelectedAddress = destinationAddress;
        dialog.mShouldShowSendETA = true;

        dialog.show(fragmentManager, "dialog");
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.dialog_calculate_eta, container, false);

        setCancelable(false);

        initialise(savedInstanceState);
        setActionListener();
        setViewModelObservers();

        binding.edtxtEta.setText(mEtaString);

        binding.setShouldShowSendETA(mShouldShowSendETA);

        return binding.getRoot();
    }

    private void setViewModelObservers() {
        viewModel.etaResponseModelLive.observe(this, new Observer<ETAResponseModel>() {
            @Override
            public void onChanged(@Nullable ETAResponseModel etaResponseModel) {
                if (binding != null && etaResponseModel != null) {
                    if (etaResponseModel.getFirstRowEtaInString() != null) {
                        binding.setShouldShowSendETA(true);
                        binding.setShouldShowError(false);
                        binding.edtxtEta.setText(etaResponseModel.getFirstRowEtaInString());
                    } else if (etaResponseModel.getIsZeroResultForETA()) {
                        binding.setShouldShowError(true);
                        binding.setShouldShowSendETA(false);
                        binding.edtxtError.setText(R.string.driving_routes_error);
                    }
                }
            }
        });


        viewModel.addressListRespinseModelLive.observe(this, new Observer<AddressListResponseModel>() {
            @Override
            public void onChanged(@Nullable AddressListResponseModel addressListResponseModel) {
                addressList.clear();
                addressList.add(0, "--Select Destination Address--");
                if (addressListResponseModel != null) {
                    addressList.addAll(addressListResponseModel.getAddressStringList());
                }

                mSpinnerBolAdapter.setDataList(addressList);
                binding.spinDestination.setAdapter(mSpinnerBolAdapter);
                if (mSelectedAddress != null && !mSelectedAddress.isEmpty()) {

                    for (int i = 0; i < addressList.size(); i++) {
                        if (TextUtils.equals(mSelectedAddress, addressList.get(i))) {
                            binding.spinDestination.setSelection(i);
                            break;
                        }
                    }

                }
            }
        });
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            FragmentActivity activity = (FragmentActivity) context;
            viewModel = ViewModelProviders.of(activity).get(MoveProcessViewModel.class);
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(context.toString()
                    + " must be a FragmentActivity");
        }


        try {
            listener = (ETACalculationClickListener) context;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(context.toString()
                    + " must implement CalculateETADialog.ETACalculationClickListener");
        }

    }

    private void initialise(Bundle savedInstanceState) {
        if (addressList == null) {
            addressList = new ArrayList<>();
        }
        addressList.add(0, "--Select Destination Address--");
        mSpinnerBolAdapter = new CustomSpinnerBolAdapter<>(getContext(), R.layout.layout_spinner_item, addressList);

        binding.spinDestination.setAdapter(mSpinnerBolAdapter);

        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey(PHONE_NUMBER_KEY)) {
                mPhoneNumber = savedInstanceState.getString(PHONE_NUMBER_KEY);
            }
            if (savedInstanceState.containsKey(SELECTED_ADDRESS_KEY)) {
                mSelectedAddress = savedInstanceState.getString(SELECTED_ADDRESS_KEY);
            }
        }
    }

    private void setActionListener() {

        binding.spinDestination.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (getContext() == null) {
                    return;
                }

//                mSelectedAddress = addressList.get(position);

                /*binding.tilEta.setVisibility(View.GONE);
                binding.tilError.setVisibility(View.GONE);
                binding.tilDuration.setVisibility(View.GONE);
                binding.btnSend.setVisibility(View.GONE);
                binding.btnSubmit.setVisibility(View.VISIBLE);*/

                /*mShouldShowSendETA = false;
                binding.setShouldShowSendETA(mShouldShowSendETA);*/

//                binding.setSelectedAddress(addressList.get(position));
                binding.edtxtDestination.setText(addressList.get(position));

                if (position == 0) {
                    binding.btnSubmit.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.text_login_gray));
                    binding.btnSubmit.setClickable(false);
                } else {
                    binding.btnSubmit.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorPrimary));
                    binding.btnSubmit.setClickable(true);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        binding.btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                binding.edtxtDestination.setText((String) binding.spinDestination.getSelectedItem());

                if (listener != null) {
                    listener.onCalculateETAClicked(binding.spinDestination.getSelectedItem().toString(), CalculateETADialog.this);
                }
            }
        });

        binding.btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    mSelectedAddress = binding.edtxtDestination.getText().toString();
                    listener.onSendETAClicked(binding.edtxtEta.getText().toString(), mSelectedAddress, mPhoneNumber, CalculateETADialog.this);
                }
            }
        });

        binding.btnChangeDestination.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.edtxtDestination.setText("");
                binding.setShouldShowError(false);
                binding.setShouldShowSendETA(false);
            }
        });

        binding.imgClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        /*binding.edtxtEta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(), R.style.TimePickerDialogStyle, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        binding.edtxtEta.setText(hourOfDay + ":" + minute);
                    }
                }, 5, 4, DateFormat.is24HourFormat(getActivity()));

                timePickerDialog.show();
            }

        });*/
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        mSelectedAddress = addressList.get(binding.spinDestination.getSelectedItemPosition());
        outState.putString(PHONE_NUMBER_KEY, mPhoneNumber);
        outState.putString(SELECTED_ADDRESS_KEY, mSelectedAddress);

        super.onSaveInstanceState(outState);
    }


    public interface ETACalculationClickListener {
        void onCalculateETAClicked(String destinationAddress, CalculateETADialog dialog);

        void onSendETAClicked(String etaString, String selectedAddress, String phoneNumber, CalculateETADialog dialog);
    }


}
