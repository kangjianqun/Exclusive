package com.kjq.common.ui.designs;

import android.transition.TransitionManager;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;

public class ConstraintUtil {

    private ConstraintLayout constraintLayout;
    private ConstraintSet applyConstraintSet = new ConstraintSet();
    private ConstraintSet resetConstraintSet = new ConstraintSet();

    public ConstraintUtil(ConstraintLayout constraintLayout) {
        this.constraintLayout = constraintLayout;
        resetConstraintSet.clone(constraintLayout);
    }

    /**
     * 重置
     */
    public void reSet()
    {
        resetConstraintSet.applyTo(constraintLayout);
    }

    /**
     * 带动画的重置
     */
    public void reSetWidthAnim()
    {
        TransitionManager.beginDelayedTransition(constraintLayout);
        resetConstraintSet.applyTo(constraintLayout);
    }
}
