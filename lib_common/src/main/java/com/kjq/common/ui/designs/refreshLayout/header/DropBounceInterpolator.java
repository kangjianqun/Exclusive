package com.kjq.common.ui.designs.refreshLayout.header;

import android.view.animation.Interpolator;

/**
 * @author amyu
 *
 * {@link WaveView#mDropBounceHorizontalAnimator} と {@link WaveView#mDropVertexAnimator} にセットするInterpolator
 * WavePullToRefresh/DropBounceInterpolator.gcxにグラフの詳細
 */
public class DropBounceInterpolator implements Interpolator {

    /**
     * {@inheritDoc}
     * @param v 动画帧
     * @return 加速值
     */
    @Override
    public float getInterpolation(float v) {
        //y = -19 * (x - 0.125)^2 + 1.3     (0 <= x < 0.25)
        //y = -6.5 * (x - 0.625)^2 + 1.1    (0.5 <= x < 0.75)
        //y = 0                             (0.25 <= x < 0.5 && 0.75 <= x <=1)

        if (v < 0.25f) {
            return -38.4f * (float) Math.pow(v - 0.125, 2) + 0.6f;
        } else if (v >= 0.5 && v < 0.75) {
            return -19.2f * (float) Math.pow(v - 0.625, 2) + 0.3f;
        } else {
            return 0;
        }
    }
}