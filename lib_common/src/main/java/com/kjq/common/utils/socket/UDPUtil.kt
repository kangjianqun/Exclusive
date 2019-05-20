package com.kjq.common.utils.socket

import android.content.Context


import com.kjq.common.utils.Utils
import com.kjq.common.utils.network.WIFIUtils

import java.io.IOException
import java.net.DatagramPacket
import java.net.DatagramSocket
import java.net.InetAddress
import java.net.SocketException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors


/**
 *
 * Created by Administrator on 2018/1/9 0009.
 */

class UDPUtil public constructor() {
    private var target_port = "1025"

    private var mSocket: DatagramSocket? = null
    private var mI_port = 1025
    private var mS_targetIP: String? = null
    /**
     * 返回udp生命线程是否存活
     * @return 是否
     */
    /**
     * 是否接收UDP
     * @param b 是否
     */
    var b_UDPLife = true
    private val mB_isScan = false
    var mB_isClose = false //是否关闭

    private var mListener: DataListener? = null

    private object UDPSocketUtilsHolder {
        val INSTANCE = UDPUtil()
    }

    /**
     * 设置UDP配置 IP 端口 监听器
     * @param s_targetIP IP
     * @param s_port 端口
     */
    fun setUDPIPAndPort(s_targetIP: String, s_port: String) {
        mB_isClose = false
        mS_targetIP = s_targetIP
        mI_port = Integer.parseInt(s_port)
    }

    fun setUDPIPAndPort(s_targetIP: String, target_port: String, dataListener: DataListener) {
        mB_isClose = false
        mS_targetIP = s_targetIP
        mI_port = Integer.parseInt(target_port)
        mListener = dataListener
    }

    fun setTargetIP(s_targetIP: String) {
        mS_targetIP = s_targetIP
    }

    fun setListener(listener: DataListener) {
        this.mListener = listener
    }

    fun getTarget_port(): String {
        return target_port
    }

    fun setTarget_port(target_port: String): UDPUtil {
        this.target_port = target_port
        return this
    }

    /**
     * 关闭UDP
     */
    fun close() {
        if (mSocket != null) {
            mB_isClose = true
            mSocket!!.close()
            mSocket = null
        }
    }

    fun sendContent(sS_content: String): Boolean {
        if (mS_targetIP == null || mS_targetIP!!.length == 0) {
            val sContext = Utils.getContext()
            val mWIFIUtils = WIFIUtils.getInstance()
            mWIFIUtils.setWifi(sContext)
            startScanDevice(sS_content, mWIFIUtils.ipAddress)
            return true
        } else {
            return sendContent(mS_targetIP!!, mI_port, sS_content)
        }
    }

    /**
     * 开始扫描设备
     * @param s_content 扫描设备需要发送的内容
     * @param i_localIP 本机IP
     */
    fun startScanDevice(s_content: String, i_localIP: Int) {
        mEService.execute {
            var sS_prefixIP = "192.168.1."
            if (i_localIP != 0) {
                val sS_localIP = WIFIUtils.intIpToStringIp(i_localIP)
                sS_prefixIP = sS_localIP.substring(0, sS_localIP.lastIndexOf(".") + 1)
            }
            //                Log.d(TAG, "run: "+ Thread.currentThread().getName()+"--"+sS_prefixIP);
            for (sI_suffixIP in 1..254) {
                if (!mB_isClose) {
                    val sS_IP = sS_prefixIP + sI_suffixIP
                    sendContent(sS_IP, Integer.parseInt(target_port), s_content)
                }
            }
        }

    }

    /**
     * 发送内容
     * @param msgSend 需要发送的内容
     */
    private fun sendContent(s_targetIP: String, i_port: Int, msgSend: String): Boolean {
        if (mSocket == null) {
            return false
        }
        val sTargetAddress: InetAddress
        val sB_sendData = msgSend.toByteArray()
        try {
            sTargetAddress = InetAddress.getByName(s_targetIP)
        } catch (e: UnknownHostException) {
            e.printStackTrace()
            return false
        }

        val sD_sendPacket = DatagramPacket(
                sB_sendData, sB_sendData.size, sTargetAddress, i_port)
        try {
            mSocket!!.send(sD_sendPacket)
        } catch (e: IOException) {
            e.printStackTrace()
            return false
        }

        return true
    }

    /*开始接收UDP*/
    fun startReceiverUDP() {
        if (mSocket != null) {
            close()
        }
        mEService.execute {
            try {
                mSocket = DatagramSocket()
                mSocket!!.soTimeout = 2000 /*设置超时的时间*/
            } catch (e: SocketException) {
                e.printStackTrace()
            }

            while (b_UDPLife) {
                try {
                    val sPacket = DatagramPacket(mB_receiverBuf, mB_receiverBuf.size)
                    mSocket!!.receive(sPacket)
                    val realData = ByteArray(sPacket.length)
                    System.arraycopy(mB_receiverBuf, 0, realData, 0, realData.size)
                    var sS_ip = sPacket.address.toString()
                    sS_ip = sS_ip.substring(1, sS_ip.length)
                    if (mListener != null) {
                        mListener!!.returnDataOfUDP(FEEDBACK_DATA, realData, sS_ip)
                    }
                } catch (ignored: SocketTimeoutException) {
                } catch (e: Exception) {
                    e.printStackTrace()
                }

            }
            mSocket!!.close()
        }
    }

    companion object {
        private val TAG = "myLog"
        const val FEEDBACK_DATA = 0 /*UDP远程返回的内容*/
        private val mEService = Executors.newCachedThreadPool()

        private val mB_receiverBuf = ByteArray(1024)

        val instance: UDPUtil
            get() {
                UDPSocketUtilsHolder.INSTANCE.mB_isClose = false
                return UDPSocketUtilsHolder.INSTANCE
            }
    }
}
