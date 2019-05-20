package com.kjq.common.utils;

import android.content.Context;
import android.content.Intent;

import java.io.Serializable;

public class IntentSerializable extends Intent implements Serializable {

    public IntentSerializable(Context context, Class<?> personalCenterShuckActivityClass) {
        super(context,personalCenterShuckActivityClass);
    }
}
