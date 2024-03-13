package com.moverbol.custom.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.moverbol.R;

import androidx.fragment.app.Fragment;

/**
 * Created by Admin on 05-10-2017.
 */

public class SignatureFragment extends Fragment {
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        //Returning the layout file after inflating
        //Change R.layout.tab1 in you classes
        return inflater.inflate(R.layout.layout_signature_fragment, container, false);
    }
}
