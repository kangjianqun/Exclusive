package com.kjq.common.ui.designs.navigation;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;


import com.kjq.common.R;
import com.kjq.common.utils.ScreenSizeUtils;
import com.kjq.common.utils.data.StringUtils;

import org.jetbrains.annotations.NotNull;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * <p>classContent</p>
 *
 * @author 康建群 948182974---->>>2018/10/17 14:38
 * @version 1.0.0
 */
public class SwitchingFigureView extends LinearLayout {

    /**
     * 当前item的位置
     */
    private int mI_currentIndex;

    private IItemClickListener mItemClickListener;

    private SparseArray<LinearLayout> mLinearLayoutSparseArray = new SparseArray<>();
    private SparseArray<MSwitching> mSwitchingSparseArray;

    private String mS_methodName;
    private String mS_className;


    public void addItemClickListener(IItemClickListener iItemClickListener){
        mItemClickListener = iItemClickListener;
    }

    public SwitchingFigureView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initAttr(context,attrs);
        initView();
        initListener();
    }

    public SwitchingFigureView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttr(context,attrs);
        initView();
        initListener();
    }

    private void initAttr(Context context,AttributeSet attributeSet){
        @SuppressLint("CustomViewStyleable")
        final TypedArray a = context.obtainStyledAttributes(attributeSet, R.styleable.SwitchingFigureView);
        if (a.hasValue(R.styleable.SwitchingFigureView_commonDSFSparseArrayMethod)){
            mS_methodName = a.getString(R.styleable.SwitchingFigureView_commonDSFSparseArrayMethod);
        }
        if (a.hasValue(R.styleable.SwitchingFigureView_commonDSFSparseArrayMethodClassName)){
            mS_className = a.getString(R.styleable.SwitchingFigureView_commonDSFSparseArrayMethodClassName);
        }
        a.recycle();
    }

    @SuppressWarnings("unchecked")
    private void initView() {
        if (!StringUtils.isEmpty(mS_methodName) && !StringUtils.isEmpty(mS_className)){
            try {
                Class sClass = Class.forName(mS_className);
                Method m = sClass.getDeclaredMethod(mS_methodName);
                Object sO = m.invoke(sClass);
                mSwitchingSparseArray = (SparseArray<MSwitching>)sO;
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }

        if (mSwitchingSparseArray == null) {
            mSwitchingSparseArray = MSwitching.Util.INSTANCE.getDefault();
        }

        for (int i = 0; i < mSwitchingSparseArray.size(); i++) {
            boolean sB_select = i == 0;

            MSwitching sSwitching = mSwitchingSparseArray.get(i);
            LinearLayout sLinearLayout = new LinearLayout(getContext());
            sLinearLayout.setLayoutParams(new LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 1.0f));
            sLinearLayout.setOrientation(VERTICAL);
            sLinearLayout.setGravity(Gravity.CENTER);

            ImageView sImageView = new ImageView(getContext());
            int sI = ScreenSizeUtils.INSTANCE.dp2px(getContext(), 24);
            LayoutParams sLayoutParams_img = new LayoutParams(sI, sI);
            sLayoutParams_img.topMargin = 12;
            sImageView.setLayoutParams(sLayoutParams_img);

            TextView sTextView = new TextView(getContext());
            sTextView.setGravity(Gravity.CENTER);
            sTextView.setTextSize(10);
            sTextView.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));

            sLinearLayout.addView(sImageView);
            sLinearLayout.addView(sTextView);
            mLinearLayoutSparseArray.put(i, sLinearLayout);
            setNavigationView(!sB_select,sLinearLayout,sSwitching);
        }

        LinearLayout sLinearLayout = new LinearLayout(getContext());
        sLinearLayout.setOrientation(HORIZONTAL);
        sLinearLayout.setGravity(Gravity.CENTER_VERTICAL);
        sLinearLayout.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ScreenSizeUtils.INSTANCE.dp2px(getContext(), 48)));
        sLinearLayout.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.common_black5c));
        for (int i = 0; i < mLinearLayoutSparseArray.size(); i++) {
            sLinearLayout.addView(mLinearLayoutSparseArray.get(i));
        }
        addView(sLinearLayout);
    }

    /**
     * 初始化绑定点击事件
     */
    private void initListener() {
        for (int i = 0; i < mLinearLayoutSparseArray.size(); i++) {
            final int finalI = i;

            mLinearLayoutSparseArray.get(i).setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {{
                    if (mI_currentIndex == finalI){
                        return;
                    }
                    mI_currentIndex = finalI;
                    if (mItemClickListener == null) {
                        return;
                    }
                    boolean sB_ = mItemClickListener.itemClick(SwitchingFigureView.this, mI_currentIndex);
                    if (sB_){
                        refreshView(mI_currentIndex);
                    }
                }
                }
            });
        }
    }

    /**
     * 刷新布局
     * @param selectItem 选中的item索引
     */
    private void refreshView(int selectItem){
        for (int i = 0; i < mLinearLayoutSparseArray.size(); i++) {
            LinearLayout sLinearLayout = mLinearLayoutSparseArray.get(i);
            MSwitching sSwitching = mSwitchingSparseArray.get(i);
            setNavigationView(true,sLinearLayout,sSwitching);
        }
        LinearLayout sLinearLayout = mLinearLayoutSparseArray.get(selectItem);
        MSwitching sSwitching = mSwitchingSparseArray.get(selectItem);
        setNavigationView(false,sLinearLayout,sSwitching);
    }

    /**
     *  设置item布局默认与选中的显示效果
     * @param isDefault 是否显示默认
     * @param linearLayout item根布局
     * @param switching 对象
     */
    private void setNavigationView(boolean isDefault, @NotNull LinearLayout linearLayout, @NotNull  MSwitching switching){
        for (int j = 0; j < linearLayout.getChildCount(); j++) {
            View sView = linearLayout.getChildAt(j);
            LayoutParams sLayoutParams = (LayoutParams) sView.getLayoutParams();
            if (sView instanceof TextView){
                TextView sTextView = (TextView) sView;
                sTextView.setText(isDefault ? switching.getTxtRes() : switching.getTxtClickRes());
                int sI_color = isDefault ? switching.getTxtColorIntegerRes() : switching.getTxtColorClickIntegerRes();
                sTextView.setTextColor(ContextCompat.getColor(getContext(),sI_color));
                sTextView.setVisibility(isDefault ? View.GONE : View.VISIBLE);
                sLayoutParams.topMargin = 8;
                sLayoutParams.bottomMargin = 8;
            }else if (sView instanceof ImageView){
                ImageView sImageView = (ImageView)sView;
                sImageView.setImageResource(isDefault ? switching.getImageRes():switching.getImageClickRes());
                int sI = ScreenSizeUtils.INSTANCE.dp2px(getContext(), isDefault ? 24 : 20);
                sLayoutParams.topMargin = isDefault ? 8 : 12;
                sLayoutParams.weight = sI;
                sLayoutParams.height = sI;
            }
            sView.setLayoutParams(sLayoutParams);
        }
    }

}
