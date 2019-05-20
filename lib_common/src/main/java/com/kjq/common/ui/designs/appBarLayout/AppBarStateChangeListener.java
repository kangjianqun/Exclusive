package com.kjq.common.ui.designs.appBarLayout;

import com.google.android.material.appbar.AppBarLayout;

/**
 * <p>封装原生监听</p>
 *
 * @author 康建群 948182974---->>>2018/8/23 15:09
 * @version 1.0.0
 */
public abstract class AppBarStateChangeListener implements AppBarLayout.OnOffsetChangedListener {
    public enum State {
        EXPANDED,
        COLLAPSED,
        IDLE
    }

    private State mCurrentState = State.IDLE;

    @Override
    public final void onOffsetChanged(AppBarLayout appBarLayout, int i) {

        int sI_h = appBarLayout.getTotalScrollRange();

        int sI_slide = Math.abs(i);

        float sV = (float) sI_slide/sI_h;

        onStateChanged(appBarLayout,sV);

        if (i == 0) {
            if (mCurrentState != State.EXPANDED) {
                onStateChanged(appBarLayout, State.EXPANDED);
            }
            mCurrentState = State.EXPANDED;
        }else if (sI_slide >= sI_h) {
            if (mCurrentState != State.COLLAPSED) {
                onStateChanged(appBarLayout, State.COLLAPSED);
            }
            mCurrentState = State.COLLAPSED;
        }else {
            if (mCurrentState != State.IDLE) {
                onStateChanged(appBarLayout, State.IDLE);
            }
            mCurrentState = State.IDLE;
        }
    }

    public void onStateChanged(AppBarLayout appBarLayout, State state){

    }

    public void onStateChanged(AppBarLayout appBarLayout, float percentage){

    }
}

