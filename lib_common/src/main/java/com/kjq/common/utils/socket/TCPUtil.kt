package com.kjq.common.utils.socket

import java.io.ByteArrayOutputStream
import java.io.IOException
import java.net.InetSocketAddress
import java.nio.ByteBuffer
import java.nio.channels.SelectionKey
import java.nio.channels.Selector
import java.nio.channels.SocketChannel
import java.util.concurrent.Executors


/**
 *
 * Created by Administrator on 2018/1/4 0004.
 */

class TCPUtil public constructor() {
    private val TAG = "TCPUtil"

    var mS_lastIndexOf = "/r/n"
    /* 信道选择器*/
    var selector: Selector? = null
        private set
    /*服务器通信的信道*/
    private var mChannel: SocketChannel? = null
    /*远端服务器ip地址*/
    private var mRemoteIp: String? = null
    /*远端服务器端口*/
    private var mPort: Int = 0
    /*是否加载过的标识*/
    private var mIsInit = false
    private var mListener: DataListener? = null

    /**
     * 是否链接着
     *
     * @return 是否
     */
    private val isConnect: Boolean
        get() = if (!mIsInit) {
            false
        } else mChannel!!.isConnected

    private var mS_dataBuff = ""

    fun getS_lastIndexOf(): String {
        return mS_lastIndexOf
    }

    fun setS_lastIndexOf(s_lastIndexOf: String): TCPUtil {
        mS_lastIndexOf = s_lastIndexOf
        return this
    }

    /*重连*/
    fun reConnect() {
        close()
        connectTCP()
    }

    /**
     * 链接远端地址
     * @param remoteIp ip
     * @param port     端口
     * @param listener listener
     */
    fun connect(remoteIp: String, port: Int, listener: DataListener?) {
        mRemoteIp = remoteIp
        mPort = port
        mListener = listener
    }

    /**
     * 发送一个测试数据到服务器,检测服务器是否关闭
     * @return 是否关闭
     */
    fun canConnectServer(): Boolean {
        var sB_ok = true
        if (!isConnect) {
            sB_ok = false
        }
        try {
            mChannel!!.socket().sendUrgentData(0xff)
        } catch (e: Exception) {
            //            e.printStackTrace();
            sB_ok = false
        }

        return sB_ok
    }

    /**
     * 是否注册成功
     * 每次读完数据后,需要重新注册selector读取数据
     * @return 是否成功
     */
    @Synchronized
    private fun repareRead(): Boolean {
        var bRes = false
        try {
            //打开并注册选择器到信道
            selector = Selector.open()
            if (selector != null) {
                mChannel!!.register(selector, SelectionKey.OP_READ)
                bRes = true
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return bRes
    }

    /**
     * 发送字符
     * @param msg 消息
     * @return 是否发送成功
     */
    fun sendMsg(msg: String): Boolean {
        var bRes = false
        try {
            bRes = sendMsg(msg.toByteArray(charset(BUFF_FORMAT)))
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return bRes
    }

    /**
     * 发送数据,此函数需要在独立的子线程中完成,可以考虑做一个发送队列
     * 自己开一个子线程对该队列进行处理,就好像connect一样
     * @param bt  字节
     * @return 是否成功
     */
    fun sendMsg(bt: ByteArray): Boolean {
        var bRes = false
        if (!mIsInit) {
            return false
        }
        try {
            val buf = ByteBuffer.wrap(bt)
            val nCount = mChannel!!.write(buf)
            if (nCount > 0) {
                bRes = true
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return bRes
    }

    /*关闭链接*/
    fun close() {
        mIsInit = false
        try {
            if (selector != null) {
                selector!!.close()
                selector = null
            }
            if (mChannel != null) {
                mChannel!!.socket().shutdownInput()
                mChannel!!.socket().shutdownOutput()
                mChannel!!.socket().close()
                mChannel!!.close()
                mChannel = null
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    /*开始连接tcp服务器*/
    fun connectTCP() {
        //需要在子线程下进行链接
        mEService.execute {
            try {
                /*打开监听信道,并设置为非阻塞模式*/
                val ad = InetSocketAddress(mRemoteIp, mPort)
                mChannel = SocketChannel.open(ad)
                if (mChannel != null) {
                    mChannel!!.socket().tcpNoDelay = false
                    mChannel!!.socket().keepAlive = true

                    /*设置超时时间*/
                    mChannel!!.socket().soTimeout = TIME_OUT
                    mChannel!!.configureBlocking(false)

                    mIsInit = repareRead()
                    /*创建读线程 用于循环读*/
                    mEService.execute { receiveMessage() }
                }
            } catch (e: Exception) {
                //                    e.printStackTrace();
            } finally {
                if (!mIsInit) {
                    close()
                    /*服务器连接失败*/
                    if (mListener != null) {
                        mListener!!.okAndNoTCP(DISCONNECT)
                    }
                }
            }
        }
    }

    private fun receiveMessage() {
        if (selector == null) {
            return
        }
        /*服务器连接成功*/
        if (mListener != null) {
            mListener!!.okAndNoTCP(CONNECT)
        }
        //        boolean bares = true;
        while (mIsInit) {
            if (!isConnect) {
                try {
                    Thread.sleep(1000)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
                continue
            }
            try {
                //有数据就一直接收
                while (mIsInit && selector!!.select() > 0) {
                    for (sk in selector!!.selectedKeys()) {
                        //如果有可读数据
                        if (sk.isReadable) {
                            //使用NIO读取channel中的数据
                            val sc = sk.channel() as SocketChannel
                            //读取缓存
                            val readBuffer = ByteBuffer.allocate(READ_BUFF_SIZE)
                            //实际的读取流
                            val read = ByteArrayOutputStream()
                            var nRead = 0
                            var nLen = 0
                            //单个读取流
                            var bytes: ByteArray
                            //读完为止
                            while (readData(sc,readBuffer) > 0) {
                                //整理
                                readBuffer.flip()
                                bytes = ByteArray(nRead)
                                nLen += nRead
                                //将读取的数据拷贝到字节流中
                                readBuffer.get(bytes)
                                //将字节流添加到实际读取流中
                                read.write(bytes)
                                //@ 需要增加一个解析器,对数据流进行解析
                                readBuffer.clear()
                            }

                            mS_dataBuff += read.toString()
                            if (mS_dataBuff.lastIndexOf(mS_lastIndexOf) > 0) {
                                if (mListener != null) {
                                    mListener!!.returnDataOfTCP(mS_dataBuff)
                                    mS_dataBuff = ""
                                }
                            }
                            //为下一次读取做准备
                            sk.interestOps(SelectionKey.OP_READ)
                        }
                        //删除此SelectionKey
                        selector!!.selectedKeys().remove(sk)
                    }
                }
            } catch (e: IOException) {
                //                Log.d("myLog", "IOException: "+e.toString());
                /*服务器意外断开，尝试重新连接*/
                if (mListener != null) {
                    mListener!!.okAndNoTCP(DISCONNECT)
                }
            } catch (e: Exception) {
                e.printStackTrace()
                //                Log.d("myLog", "Exception: " + e.toString());
            }
        }
    }

    private fun readData(socketChannel: SocketChannel, readBuffer :ByteBuffer):Int{
        return socketChannel.read(readBuffer)
    }

    companion object {
        const val DISCONNECT = "断开连接"
        const val CONNECT = "连接"
        /*默认链接超时时间*/
        private const val TIME_OUT = 5000
        /*读取buff的大小*/
        private const val READ_BUFF_SIZE = 1024
        /*消息流的格式*/
        private const val BUFF_FORMAT = "utf-8"
        private val mEService = Executors.newCachedThreadPool()
        /*单键实例*/
        private var gTcp: TCPUtil? = null

        @Synchronized
        fun instance(): TCPUtil {
            if (gTcp == null) {
                gTcp = TCPUtil()
            }
            return gTcp as TCPUtil
        }
    }

}