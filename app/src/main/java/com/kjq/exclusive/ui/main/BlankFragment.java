package com.kjq.exclusive.ui.main;


import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kjq.common.base.mvvm.base.BaseFragment;
import com.kjq.exclusive.BR;
import com.kjq.exclusive.R;
import com.kjq.exclusive.databinding.FragmentBlankBinding;

/**
 * A simple {@link Fragment} subclass.
 */
public class BlankFragment extends BaseFragment<FragmentBlankBinding,BlankVM> {


    public BlankFragment() {
        // Required empty public constructor
    }

    public static BlankFragment newInstance() {

        Bundle args = new Bundle();

        BlankFragment fragment = new BlankFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public int initContentView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return R.layout.fragment_blank;
    }

    @Override
    public int initVariableId() {
        return BR.viewModel;
    }


}
