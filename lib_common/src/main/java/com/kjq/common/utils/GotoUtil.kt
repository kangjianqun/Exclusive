package com.kjq.common.utils

import android.app.Activity
import android.content.Intent
import android.os.Bundle

import com.kjq.common.base.mvvm.base.BaseActivity
import com.kjq.common.base.mvvm.base.BaseFragment
import com.kjq.common.utils.performance.AppManager

object GotoUtil {

    private fun getBaseActivity(): BaseActivity<*, *> {

        val stackActivity = AppManager.getActivityStack()
        for (activity in stackActivity){
            if (activity is BaseActivity<*,*>){
                return activity
            }
        }
        throw Exception(" not BaseActivity")
    }

    private fun getActivity():Activity{
        return AppManager.getAppManager().currentActivity()
    }

    fun startActivity(clz : Class<*>, bundle: Bundle?):Boolean{
        val sActivity = getActivity()
        val intent = Intent(sActivity, clz)
        if (bundle != null) {
            intent.putExtras(bundle)
        }
        sActivity.startActivity(intent)
        return false
    }

    fun addFragment(fragment: BaseFragment<*, *>): Boolean {
        val sActivity = getBaseActivity()
        sActivity.addFragment(fragment)
        return true
    }

    fun generalShowFragment(path: String, bundle: Bundle?): Boolean {
        val sActivity = getBaseActivity()
        sActivity.startGeneralShowActivity(path, bundle)
        return true
    }
}
