package com.kjq.common.ui.designs.popMenu;

import android.view.View;

import androidx.annotation.NonNull;

import com.kjq.common.base.mvvm.base.BaseItemViewModel;
import com.kjq.common.ui.designs.popMenu.entity.FreedomEntity;

public class FreedomItemVM extends BaseItemViewModel<FreedomPopupVM> {
    public FreedomEntity mEntity;

    public FreedomItemVM(@NonNull FreedomPopupVM viewModel, FreedomEntity entity) {
        super(viewModel);
        mEntity = entity;
    }

    @Override
    protected void onBaseClick(View view) {
        viewModel.clickResponse(mEntity.mI_type);
    }
}
