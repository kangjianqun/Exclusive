package com.kjq.common.ui.designs.universalRecyclerAdapter.animAction;

import android.animation.Animator;
import android.view.View;

/**
 * https://github.com/CymChad/BaseRecyclerViewAdapterHelper
 */
public interface BaseAnimation {
    Animator[] getAnimators(View view);
}
