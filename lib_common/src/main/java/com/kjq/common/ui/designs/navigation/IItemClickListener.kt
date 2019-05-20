package com.kjq.common.ui.designs.navigation


/**
 * <p>classContent</p>
 *
 * @author 康建群 948182974---->>>2018/10/17 14:47
 * @version 1.0.0
 */
interface IItemClickListener {
    /**
     * item 点击事件[switchingFigureView] 控件,[int] item位置
     */
    fun itemClick(switchingFigureView: SwitchingFigureView, int: Int):Boolean
}