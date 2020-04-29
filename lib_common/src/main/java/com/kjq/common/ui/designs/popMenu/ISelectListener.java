package com.kjq.common.ui.designs.popMenu;

import android.view.View;

public interface ISelectListener {
    /**
     * 菜单被选择时的回调接口.
     *
     * @param view     被选择的内容的View.
     * @param item     被选择的菜单项.
     * @param position 被选择的位置.
     */
    void selected(View view, PopMenuModel item, int position);
}
