package com.kjq.exclusive;

import android.content.Context;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.multidex.MultiDex;

import com.alibaba.android.arouter.launcher.ARouter;
import com.kjq.common.base.mvvm.base.BaseApplication;
import com.kjq.common.utils.SPUtil;
import com.kjq.common.utils.Utils;

import org.acra.ACRA;
import org.acra.ReportField;
import org.acra.ReportingInteractionMode;
import org.acra.annotation.ReportsCrashes;
import org.acra.collector.CrashReportData;
import org.acra.config.ACRAConfiguration;
import org.acra.config.ACRAConfigurationException;
import org.acra.config.ConfigurationBuilder;
import org.acra.sender.EmailIntentSender;
import org.acra.sender.ReportSender;
import org.acra.sender.ReportSenderException;
import org.acra.sender.ReportSenderFactory;

/**
 * <p>这里仅需做一些初始化的工作</p>
 *
 * @author 康建群 2018/8/2 14:20
 * @version V1.4.0
 */
@ReportsCrashes(
        reportSenderFactoryClasses ={MyApplication.CrashReportSenderFactory.class},
        mailTo = "13071074126@163.com",
        mode = ReportingInteractionMode.DIALOG,
        customReportContent = {
                ReportField.APP_VERSION_NAME,
                ReportField.ANDROID_VERSION,
                ReportField.PHONE_MODEL,
                ReportField.CUSTOM_DATA,
                ReportField.BRAND,
                ReportField.STACK_TRACE,
                ReportField.LOGCAT,
                ReportField.USER_COMMENT},
        resToastText = R.string.common_toast_text,
        resDialogText = R.string.common_dialog_text,
        resDialogTitle = R.string.common_dialog_title)
public class MyApplication extends BaseApplication {

    @Override
    public void onCreate() {
        super.onCreate();
        if (Utils.isAppDebug()) {
            //开启InstantRun之后，一定要在ARouter.init之前调用openDebug
            ARouter.openDebug();
            ARouter.openLog();
        }
        //路由初始化
        ARouter.init(this);
        //崩溃日志记录初始化
        ACRA.init(this);
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        // dex突破65535的限制
        MultiDex.install(this);
    }

    public class CrashReportSenderFactory implements ReportSenderFactory {
        /***
         * 注意这里必须要是空的构造方法
         */
        public CrashReportSenderFactory() {
        }

        @NonNull
        @Override
        public ReportSender create(@NonNull Context context, @NonNull ACRAConfiguration acraConfiguration) {
            return new CrashReportSender();
        }
    }

    /**
     * 发送崩溃日志
     */
    private class CrashReportSender implements ReportSender {
        CrashReportSender() {
            ACRA.getErrorReporter().putCustomData("PLATFORM", "ANDROID");
            ACRA.getErrorReporter().putCustomData("BUILD_ID", android.os.Build.ID);
            ACRA.getErrorReporter().putCustomData("DEVICE_NAME", android.os.Build.PRODUCT);
        }

        @Override
        public void send(@NonNull Context context, @NonNull CrashReportData crashReportData) throws ReportSenderException {

            ConfigurationBuilder sBuilder = new ConfigurationBuilder(MyApplication.getInstance());
            ACRAConfiguration s;
            try {
                s = sBuilder.build();
                EmailIntentSender emailSender = new EmailIntentSender(s);
                emailSender.send(context, crashReportData);
            } catch (ACRAConfigurationException e) {
                e.printStackTrace();
            }
        }
    }
}
