package com.kjq.common.ui.designs.navigation

import android.util.SparseArray
import com.kjq.common.R

/**
 * <p>classContent</p>
 *
 * @author 康建群 948182974---->>>2018/10/17 15:09
 * @version 1.0.0
 */
class MSwitching {
    var imageRes = R.drawable.designs_svg_star_black_24dp
    var imageClickRes = R.drawable.designs_svg_star_black_24dp
    var txtRes = R.string.common_default_txt
    var txtClickRes = R.string.common_checked_default
    var txtColorIntegerRes = R.color.common_control_white
    var txtColorClickIntegerRes = R.color.common_colorPrimary

    constructor()

    constructor(imageRes: Int, imageClickRes: Int, txtRes: Int, txtClickRes: Int) {
        this.imageRes = imageRes
        this.imageClickRes = imageClickRes
        this.txtRes = txtRes
        this.txtClickRes = txtClickRes
    }

    constructor(imageRes: Int, imageClickRes: Int, txtRes: Int, txtClickRes: Int, txtColorIntegerRes: Int, txtColorClickIntegerRes: Int) {
        this.imageRes = imageRes
        this.imageClickRes = imageClickRes
        this.txtRes = txtRes
        this.txtClickRes = txtClickRes
        this.txtColorIntegerRes = txtColorIntegerRes
        this.txtColorClickIntegerRes = txtColorClickIntegerRes
    }


    object Util {
        fun getDefault():SparseArray<MSwitching>{
            val sparseArray = SparseArray<MSwitching>()
            sparseArray.put(0, MSwitching())
            sparseArray.put(1, MSwitching())
            sparseArray.put(2, MSwitching())
            sparseArray.put(3, MSwitching())
            return sparseArray
        }
    }


}