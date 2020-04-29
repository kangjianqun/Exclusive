package com.kjq.common.ui.designs.pickerView.utils;

import android.view.Gravity;

import com.kjq.common.R;

import org.jetbrains.annotations.Contract;

/**
 * Created by Sai on 15/8/9.
 */
public class PickerViewAnimateUtil {
    private static final int INVALID = -1;
    /**
     * Get default animation resource when not defined by the user
     *
     * @param gravity       the animGravity of the dialog
     * @param isInAnimation determine if is in or out animation. true when is is
     * @return the id of the animation resource
     */
    @Contract(pure = true)
    public static int getAnimationResource(int gravity, boolean isInAnimation) {
        switch (gravity) {
            case Gravity.BOTTOM:
                return isInAnimation ? R.anim.common_picker_view_slide_in_bottom : R.anim.common_picker_view_slide_out_bottom;
        }
        return INVALID;
    }
}
