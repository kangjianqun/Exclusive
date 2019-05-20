package com.kjq.common.utils.socket

import java.util.*

/**
 * <p>classContent</p>
 *
 * @author 康建群 948182974---->>>2018/10/19 9:01
 * @version 1.0.0
 */
interface DataListener {
    fun returnDataOfTCP(data :String){

    }
    fun okAndNoTCP(data: String){

    }
    fun returnDataOfUDP(type:Int, data:Objects, udpOfIp:String){

    }
    fun returnDataOfUDP(feedbackData: Int, realData: ByteArray, s_ip: String){

    }

}