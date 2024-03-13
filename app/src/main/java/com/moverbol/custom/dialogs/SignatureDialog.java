package com.moverbol.custom.dialogs;

import androidx.databinding.DataBindingUtil;
import android.os.Bundle;
import androidx.annotation.Nullable;
import com.google.android.material.tabs.TabLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.moverbol.R;
import com.moverbol.adapters.HomePagerAdapter;
import com.moverbol.custom.fragment.SignatureFragment;
import com.moverbol.databinding.TabbedSignatureBinding;

/**
 * Created by Admin on 05-10-2017.
 */

public class SignatureDialog extends BaseDialogFragment implements TabLayout.OnTabSelectedListener {
    private TabbedSignatureBinding tabbedSignatureBinding;

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        tabbedSignatureBinding = DataBindingUtil.inflate(inflater, R.layout.layout_signature_tabbed_dialog, container, false);
        tabbedSignatureBinding.setTabbedsignaturedialog(this);
        HomePagerAdapter adapter = new HomePagerAdapter(getChildFragmentManager());
        adapter.addFragment(new SignatureFragment(), getActivity().getString(R.string.origin));
        adapter.addFragment(new SignatureFragment(), getActivity().getString(R.string.destination));

        tabbedSignatureBinding.signPager.setAdapter(adapter);
        tabbedSignatureBinding.signTablayout.setupWithViewPager(tabbedSignatureBinding.signPager);
        tabbedSignatureBinding.signTablayout.addOnTabSelectedListener(this);
        setCancelable(false);
        tabbedSignatureBinding.ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        return tabbedSignatureBinding.getRoot();
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        tabbedSignatureBinding.signPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }
    public void dismissDialog(View view) {
        dismiss();
    }
}