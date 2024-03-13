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
import com.moverbol.adapters.CrewAdapter;
import com.moverbol.custom.fragment.BaseFragment;
import com.moverbol.databinding.CrewBinding;
import com.moverbol.interfaces.ConfirmationInterface;
import com.moverbol.model.ManPowerPojo;
import com.moverbol.util.DividerItemDecoration;
import com.moverbol.viewmodels.moveProcess.ConfirmationDetailsViewModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sumeet on 11/9/17.
 */

public class CrewFragment extends BaseFragment {

    private CrewAdapter adapter;
    private ConfirmationInterface confirmationInterface;
    private ConfirmationDetailsViewModel viewModel;
    private CrewBinding crewBinding;

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
        crewBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_crew, container, false);

         crewBinding.rvCrew.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));
        if (confirmationInterface != null) {
            crewBinding.rvCrew.addOnScrollListener(new RecyclerView.OnScrollListener() {

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
        return crewBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        viewModel = ViewModelProviders.of(this.getActivity()).get(ConfirmationDetailsViewModel.class);

        List<ManPowerPojo> crewList = new ArrayList<>();

        if(adapter == null){
            adapter = new CrewAdapter(crewList, new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        }

        crewBinding.setAdapter(adapter);

        /*viewModel.getJobConfirmationLive().observe(getActivity(), new Observer<JobConfirmationPojo>() {
            @Override
            public void onChanged(@Nullable JobConfirmationPojo jobConfirmationPojo) {
                adapter.setHomeList(jobConfirmationPojo.getCrewList());
            }
        });*/

    }
}