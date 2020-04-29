package com.kjq.common.base.mvvm.base;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * <p>返回工具</p>
 *
 * @author 康建群 948182974---->>>2018/10/8 16:28
 * @version 1.0.0
 */
public class BackUtil {

    /**
     * 将back事件分发给 FragmentManager 中管理的子Fragment，如果该 FragmentManager 中的所有Fragment都
     * 没有处理back事件，则尝试 FragmentManager.popBackStack()
     *
     * @return 如果处理了back键则返回 <b>true</b>
     * @see #handleBackPress(Fragment)
     * @see #handleBackPress(FragmentActivity)
     */
    private static boolean handleBackPress(@NotNull FragmentManager fragmentManager) {
        List<Fragment> fragments = fragmentManager.getFragments();
        for (int i = fragments.size() - 1; i >= 0; i--) {
            Fragment child = fragments.get(i);
            if (isFragmentBackHandled(child)) {
                return true;
            }
        }
//        if (fragmentManager.getBackStackEntryCount() > 0) {
//            fragmentManager.popBackStack();
//            return true;
//        }
        return false;
    }

    static boolean handleBackPress(@NotNull Fragment fragment) {
        return handleBackPress(fragment.getChildFragmentManager());
    }

    static boolean handleBackPress(@NotNull FragmentActivity fragmentActivity) {
        return handleBackPress(fragmentActivity.getSupportFragmentManager());
    }

    /**
     * 判断Fragment是否处理了Back键     *
     *
     * @return 如果处理了back键则返回 <b>true</b>
     */

    @Contract("null -> false")
    private static boolean isFragmentBackHandled(Fragment fragment) {
        return fragment != null
                && fragment.isVisible()
                && fragment.getUserVisibleHint() //for ViewPager
                && fragment instanceof IBackListener
                && ((IBackListener) fragment).onBackPressed();
    }
}
