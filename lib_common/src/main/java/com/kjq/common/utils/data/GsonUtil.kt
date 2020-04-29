package com.kjq.common.utils.data

import com.google.gson.Gson
import java.util.*

object GsonUtil {
    fun <T> parse(json: String, classOfT: Class<T>): T {
        return Gson().fromJson(json, classOfT)
    }

    fun toJson(o : Any): String {
        return Gson().toJson(o)
    }
}
