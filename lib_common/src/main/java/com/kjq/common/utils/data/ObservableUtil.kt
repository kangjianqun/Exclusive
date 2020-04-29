package com.kjq.common.utils.data

import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.databinding.ObservableInt

object ObservableUtil {

    fun setValue(observable: ObservableInt, value: Int?) {
        if (value != null) {
            observable.set(value)
        }
    }

    fun setValue(observableBoolean: ObservableBoolean,value:Boolean){
        observableBoolean.set(value)
    }

    fun <T> setValue(observableField: ObservableField<T>,value:T?){

        if (value is String){
            if (!StringUtils.isEmpty(value)){
                observableField.set(value)
            }

        }else{
            if (value != null){
                observableField.set(value)
            }
        }
    }
}
