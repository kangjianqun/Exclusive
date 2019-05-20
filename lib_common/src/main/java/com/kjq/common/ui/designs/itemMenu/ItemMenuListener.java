package com.kjq.common.ui.designs.itemMenu;

/**
 * 侧滑菜单监听器
 * Created by Administrator on 2017/11/29 0029.
 */

public interface ItemMenuListener {
    /*删除*/
    void onDelete(int position);
    /*更多*/
    void onMore(int position);
    /*置顶*/
    void onTop(int position);
}
