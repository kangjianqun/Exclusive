package com.kjq.common.utils.data

object ArraysUtil {

    fun toString(strings: Array<String>, split: String): String {
        val sStringBuilder = StringBuilder()
        for ((sI, sS_value) in strings.withIndex()) {
            sStringBuilder.append(sS_value)
            if (sI != strings.size - 1) {
                sStringBuilder.append(split)
            }
        }
        val endChat = sStringBuilder.substring(sStringBuilder.length-1,sStringBuilder.length)
        return if (endChat == split){
            sStringBuilder.removeRange(sStringBuilder.length-1,sStringBuilder.length).toString()
        }else{
            sStringBuilder.toString()
        }
    }

    fun toString(string: String, split: String,index: Int): String{
        val sStringBuffer = StringBuilder()
        val strings = string.split(split)
        for ((sI_index, sS) in strings.withIndex()){
            if (sI_index < index){
                sStringBuffer.append(sS)
            }else{
                break
            }
            if (sI_index < index - 1){
                sStringBuffer.append(split)
            }
        }
        val endChat = sStringBuffer.substring(sStringBuffer.length-1,sStringBuffer.length)
        return if (endChat == split){
            sStringBuffer.removeRange(sStringBuffer.length-1,sStringBuffer.length).toString()
        }else{
            sStringBuffer.toString()
        }
    }
}
