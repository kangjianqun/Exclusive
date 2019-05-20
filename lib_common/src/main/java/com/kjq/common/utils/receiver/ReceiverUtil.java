package com.kjq.common.utils.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import java.util.List;


public class ReceiverUtil extends BroadcastReceiver {

    private IReceiverListener mReceiverListener;
    private Context mContext;
    IntentFilter sIntentFilter = new IntentFilter();

    public ReceiverUtil(Context context, List<String> strings,IReceiverListener listener) {
        mReceiverListener = listener;
        mContext = context;
        for (String sS_action :
                strings) {
            sIntentFilter.addAction(sS_action);
        }
    }

    public void onClose(){
        if (mReceiverListener != null){
            try{
                mContext.unregisterReceiver(this);
                mContext = null;
                mReceiverListener = null;
            }catch (Exception ignored){ }
        }
    }

    public void observeReceiver(){
        mContext.registerReceiver(this,sIntentFilter);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String sS_action = intent.getAction();
        if (mReceiverListener != null){
            mReceiverListener.receiverReturn(sS_action);
        }
    }
}
