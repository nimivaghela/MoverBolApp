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
import com.moverbol.adapters.MaterialAdapter;
import com.moverbol.custom.fragment.BaseFragment;
import com.moverbol.databinding.MaterialBinding;
import com.moverbol.interfaces.ConfirmationInterface;
import com.moverbol.util.DividerItemDecoration;
import com.moverbol.viewmodels.moveProcess.ConfirmationDetailsViewModel;

/**
 * Created by sumeet on 11/9/17.
 */

public class MaterialFragment extends BaseFragment {

    private ConfirmationDetailsViewModel viewModel;
    private ConfirmationInterface confirmationInterface;
    private MaterialAdapter adapter;
    private MaterialBinding materialBinding;

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
        materialBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_material, container, false);
        /*if (materialVM == null) {
            materialVM = new MaterialVM();
        }*/


//        materialVM.loadData();
//        materialBinding.setMaterialVM(materialVM);
        materialBinding.rvCrew.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));
        if (confirmationInterface != null) {
            materialBinding.rvCrew.addOnScrollListener(new RecyclerView.OnScrollListener() {

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
        return materialBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        viewModel = ViewModelProviders.of(this.getActivity()).get(ConfirmationDetailsViewModel.class);

        /*if(adapter == null){
            adapter = new MaterialAdapter(viewModel.getJobConfirmationLive().getValue().getMaterials(), new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        }*/

        materialBinding.setAdapter(adapter);
      /*  viewModel.getJobConfirmationLive().observe(this.getActivity(), new Observer<JobConfirmationPojo>() {
            @Override
            public void onChanged(@Nullable JobConfirmationPojo jobConfirmationPojo) {
                adapter.setHomeList(jobConfirmationPojo.getMaterials());
            }
        });*/
    }
}