package com.kjq.common.utils.application;

public interface IApplicationDelegate {
    void onCreate();

    void onTerminate();

    void onLowMemory();

    void onTrimMemory(int level);
}
