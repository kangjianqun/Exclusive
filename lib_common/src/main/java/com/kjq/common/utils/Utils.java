package com.kjq.common.utils;


import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;



import androidx.annotation.NonNull;
import androidx.annotation.StringRes;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.kjq.common.utils.data.StringUtils;

/**
 * <p>Utils初始化相关 </p>
 */
public class Utils {

    private static Context context;

    private Utils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    /**
     * 初始化工具类
     *
     * @param context 上下文
     */
    public static void init(Context context) {
        Utils.context = context.getApplicationContext();
    }

    /**
     * 获取ApplicationContext
     *
     * @return ApplicationContext
     */
    public static Context getContext() {
        if (context != null) return context;
        throw new NullPointerException("u should init first");
    }

    /**
     * View获取Activity的工具
     *
     * @param view view
     * @return Activity
     */
    public static
    @NonNull
    Activity getActivity(View view) {
        Context context = view.getContext();

        while (context instanceof ContextWrapper) {
            if (context instanceof Activity) {
                return (Activity) context;
            }
            context = ((ContextWrapper) context).getBaseContext();
        }

        throw new IllegalStateException("View " + view + " is not attached to an Activity");
    }

    /**
     * 全局获取String的方法
     *
     * @param id 资源Id
     * @return String
     */
    public static String getString(@StringRes int id) {
        return context.getResources().getString(id);
    }

    /**
     * 判断App是否是Debug版本
     *
     * @return {@code true}: 是<br>{@code false}: 否
     */
    public static boolean isAppDebug() {
        if (StringUtils.isSpace(context.getPackageName())) return false;
        try {
            PackageManager pm = context.getPackageManager();
            ApplicationInfo ai = pm.getApplicationInfo(context.getPackageName(), 0);
            return ai != null && (ai.flags & ApplicationInfo.FLAG_DEBUGGABLE) != 0;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }


    /**
     * The {@code fragment} is added to the container view with id {@code frameId}. The operation is
     * performed by the {@code fragmentManager}.
     */
    public static void addFragmentToActivity(@NonNull FragmentManager fragmentManager,
                                             @NonNull Fragment fragment, int frameId) {
        checkNotNull(fragment);
        FragmentTransaction transaction = checkNotNull(fragmentManager).beginTransaction();
        transaction.add(frameId, fragment);
        transaction.commit();
    }

    public static <T> T checkNotNull(T obj) {
        if (obj == null) {
            throw new NullPointerException();
        }
        return obj;
    }

    /**
     * 从Activity 获取 rootView 根节点 ,取出最外层布局，
     * 用于判断Activity 根布局的类别
     * @param activity 活动
     * @return 当前activity布局的根节点
     */
    public static View getRootView(@NonNull Activity activity){
        return getRootFrame(activity).getChildAt(0);
    }

    /**
     * 获取从Activity 获取 rootView 布局
     * @param activity 活动
     * @return 当前activity根布局
     */

    @NonNull
    public static FrameLayout getRootFrame(@NonNull Activity activity) {
        View sRootView = activity.findViewById(android.R.id.content);
        if (sRootView instanceof FrameLayout) {
            return (FrameLayout) sRootView;
        }
        ViewGroup viewGroup = (ViewGroup) activity.getWindow().getDecorView();
        sRootView = viewGroup.getChildAt(viewGroup.getChildCount() - 1);
        if (sRootView instanceof FrameLayout) {
            return (FrameLayout) sRootView;
        } else {
            sRootView = new FrameLayout(activity);
            int sHeight = Utils.checkNotNull(activity.getActionBar()).getHeight();
            ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT
                    , ViewGroup.LayoutParams.MATCH_PARENT);
            viewGroup.addView(sRootView, lp);
            return (FrameLayout) sRootView;
        }
    }

    /**
     * 得到状态栏的高度
     * @param context 上下文
     * @return 状态栏高度
     */
    public static int getStatusBarHeight(Context context){
        int result = 0;
        int resId = context.getResources().getIdentifier("status_bar_height","dimen","android");
        if(resId > 0){
            result = context.getResources().getDimensionPixelSize(resId);
        }
        return result;
    }

    /**
     * 思路:直接设置状态栏的颜色
     */
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public static void setStatusBarUpperAPI21(Activity activity) {
        Window window = activity.getWindow();
        //取消设置悬浮透明状态栏,ContentView便不会进入状态栏的下方了

        //设置悬浮透明状态栏
//        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        //设置状态栏颜色
//        window.setStatusBarColor(ContextCompat.getColor(activity,android.R.color.transparent));
    }

    /**
     * 思路:设置状态栏悬浮透明，然后制造一个和状态栏等尺寸的View设置好颜色填进去，就好像是状态栏着色了一样
     */
    public static void setStatusBarUpperAPI19(Activity activity) {
        Window window = activity.getWindow();
        //设置悬浮透明状态栏
        window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        ViewGroup mContentView = activity.findViewById(Window.ID_ANDROID_CONTENT);
        int statusBarHeight = getStatusBarHeight(activity);
//        int statusColor = ContextCompat.getColor(activity,android.R.color.transparent);

        View mTopView = mContentView.getChildAt(0);
        if (mTopView != null && mTopView.getLayoutParams() != null &&
                mTopView.getLayoutParams().height == statusBarHeight) {
//            mTopView.setBackgroundColor(statusColor);
            return;
        }

        //制造一个和状态栏等尺寸的 View
        mTopView = new View(activity);
        ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, statusBarHeight);
//        mTopView.setBackgroundColor(statusColor);
        //将view添加到第一个位置
        mContentView.addView(mTopView, 0, lp);
    }

    /**
     * 设置全屏沉浸
     * @param activity 活动
     */
    public static void onHide(Activity activity) {
        int flags = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            activity.getWindow().getDecorView().setSystemUiVisibility(
                    flags | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
            );
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            activity.getWindow().getDecorView().setSystemUiVisibility(flags);
        }
    }
}