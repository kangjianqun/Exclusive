package com.kjq.common.utils.network;

import okhttp3.FormBody;

/**
 *
 * Created by Administrator on 2018/3/16 0016.
 */

public interface IDComm {
    <C> FormBody.Builder getFormBody(C module);
    FormBody.Builder getFormBody();
}
