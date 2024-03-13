package com.moverbol.views.fragments.confirmation;

import androidx.lifecycle.ViewModelProviders;
import android.content.Context;
import androidx.databinding.DataBindingUtil;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.moverbol.R;
import com.moverbol.adapters.TruckAdapter;
import com.moverbol.custom.fragment.BaseFragment;
import com.moverbol.databinding.TruckBinding;
import com.moverbol.interfaces.ConfirmationInterface;
import com.moverbol.model.TruckPojo;
import com.moverbol.util.DividerItemDecoration;
import com.moverbol.viewmodels.moveProcess.ConfirmationDetailsViewModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sumeet on 11/9/17.
 */

public class TruckFragment extends BaseFragment {

    private ConfirmationDetailsViewModel viewModel;
    private ConfirmationInterface confirmationInterface;
    private TruckBinding truckBinding;
    private TruckAdapter adapter;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof ConfirmationInterface) {
            confirmationInterface = (ConfirmationInterface) context;
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        truckBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_truck, container, false);
        /*if (viewModel == null) {
            viewModel = new TruckVM();
        }
        truckVM.loadData();*/
//        truckBinding.setTruckVM(truckVM);
        truckBinding.rvCrew.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));
        if (confirmationInterface != null) {
            truckBinding.rvCrew.addOnScrollListener(new RecyclerView.OnScrollListener() {

                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);
                    if (dy > 0) {
                        //&& mFloatingActionButton.getVisibility() == View.VISIBLE
                        //mFloatingActionButton.hide();
                        confirmationInterface.showHideFAB(View.INVISIBLE);
                    } else if (dy < 0) {
                        //&& mFloatingActionButton.getVisibility() != View.VISIBLE
                        //mFloatingActionButton.show();
                        confirmationInterface.showHideFAB(View.VISIBLE);
                    }
                }
            });
        }
        return truckBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        viewModel = ViewModelProviders.of(this.getActivity()).get(ConfirmationDetailsViewModel.class);

        List<TruckPojo> truckList = new ArrayList<>();

        if(adapter == null){
            adapter = new TruckAdapter(truckList, new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        }

        truckBinding.setAdapter(adapter);

        /*viewModel.getJobConfirmationLive().observe(this.getActivity(), new Observer<JobConfirmationPojo>() {
            @Override
            public void onChanged(@Nullable JobConfirmationPojo jobConfirmationPojo) {
                adapter.setHomeList(jobConfirmationPojo.getTrucks());
            }
        });*/

    }
}