package com.kjq.common.ui.designs.explodeEffects;

import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.annotation.AnimRes;
import androidx.annotation.RequiresApi;


import com.kjq.common.R;
import com.kjq.common.utils.data.RandomData;

import java.util.ArrayList;


/**
 * <p>爆炸效果</p>
 *
 * @author 康建群 948182974---->>>2018/8/7 16:01
 * @version 1.0.0
 */
public class ExplodeEffects extends RelativeLayout {

    private int mI_count = 50;
    private int mI_style = StyleType.normal;
    private final static int KEY_TAG_IV = 10000;

    private ExplodeEffects mRootView;

    private float mF_x;
    private float mF_y;

    private @AnimRes
    int mI_aminOne;
    private @AnimRes int mI_amintwo;

    private ArrayList<Integer> mAList_imgRes;
    private SparseArray<ImageView> mSArray_iVs;
    private SparseArray<LinearLayout> mSArray_lLs;

    public int getI_count() {
        return mI_count;
    }

    public void setI_count(int i_count) {
        mI_count = i_count;
    }

    public void setStartXY(float f_x,float f_y){
        mF_x = f_x;
        mF_y = f_y;
    }

    public int getI_style() {
        return mI_style;
    }

    public void setI_style(int i_style) {
        mI_style = i_style;
    }

    public ArrayList<Integer> getAList_imgRes() {
        return mAList_imgRes;
    }

    public void setAList_imgRes(ArrayList<Integer> AList_imgRes) {
        mAList_imgRes = AList_imgRes;
    }

    public ExplodeEffects(Context context) {
        super(context);
        initView(context,null,-1,-1);
    }

    public ExplodeEffects(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context,attrs,-1,-1);
    }

    public ExplodeEffects(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context,attrs,defStyleAttr,-1);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public ExplodeEffects(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView(context,attrs,defStyleAttr,defStyleRes);
    }

    private void initView(final Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes){

        initParam(attrs, new IInitParam() {
            @Override
            public void back() {
                mRootView = ExplodeEffects.this;
                setContainersImg(context);
            }
        });
    }

    interface IInitParam {
        void back();
    }

    private void initParam(AttributeSet attrs, final IInitParam iinitParam){
        this.measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED);
        mSArray_iVs = new SparseArray<>();
        mSArray_lLs = new SparseArray<>();
        mAList_imgRes = new ArrayList<>();
        mAList_imgRes.add(R.drawable.common_svg_explode_effects_triangle);
        mAList_imgRes.add(R.drawable.common_svg_explode_effects_circle);
        mAList_imgRes.add(R.drawable.common_svg_explode_effects_square);

        final View sView = this;
        sView.post(new Runnable() {
            @Override
            public void run() {
                float x = (sView.getBottom() - sView.getTop()) / 2;
                float y = (sView.getRight() - sView.getLeft()) / 2;
                setStartXY(x,y);
                iinitParam.back();
            }
        });
    }

    private void setContainersImg(final Context context){

        Thread sThread = new Thread(new Runnable() {
            @Override
            public void run() {{
                int sI_imgResCount = 0;
                for (int i = 0; i < mI_count; i++) {
                    final LinearLayout sLinearLayout = new LinearLayout(context);
                    LayoutParams sLayoutParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    sLayoutParams.topMargin = RandomData.getRan(((int) mF_y - 200),((int) mF_y + 400));
                    sLayoutParams.leftMargin = RandomData.getRan(((int) mF_x - 150),((int) mF_x + 150));
                    sLinearLayout.setLayoutParams(sLayoutParams);
                    ImageView sImageView = new ImageView(context);
                    if (sI_imgResCount >= mAList_imgRes.size()) {
                        sI_imgResCount = 0;
                    }
                    sImageView.setImageResource(mAList_imgRes.get(sI_imgResCount));
                    sLinearLayout.addView(sImageView);
                    mSArray_iVs.put(i, sImageView);
                    mSArray_lLs.put(i,sLinearLayout);

                    setNormalAnim(i, mSArray_lLs.get(i), mSArray_iVs.get(i));

                    mRootView.post(new Runnable() {
                        @Override
                        public void run() {
                            mRootView.addView(sLinearLayout);
                        }
                    });

                    sI_imgResCount++;
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
            }
        });

        sThread.start();
    }

    private void setIVImgRes(Context context){
        int sI_imgResCount = 0;
        for (int i = 0; i < mI_count; i++) {
            ImageView sImageView = new ImageView(context);
            if (sI_imgResCount >= mAList_imgRes.size()) {
                sI_imgResCount = 0;
            }
            sImageView.setImageResource(mAList_imgRes.get(sI_imgResCount));
            sImageView.setX(mF_x);
            sImageView.setY(mF_y);
            this.addView(sImageView);
            sI_imgResCount++;
            mSArray_iVs.put(i, sImageView);
        }
    }

    private void bindAnim(){
        for (int i = 0; i < mSArray_iVs.size(); i++) {
            setNormalAnim(i, mSArray_lLs.get(i), mSArray_iVs.get(i));
        }
    }

    private AnimationSet getAnim(int index){
        String sS_index = String.valueOf(index);
        int sI_lent = sS_index.length();
        float sV_x;
        float sV_y;
        switch (sS_index.substring(0, sI_lent - 1)) {
            default:
                sV_x = RandomData.getRandom(500);
                sV_y = RandomData.getRandom(500);
                break;
            case "1":
            case "6":
                sV_x = 0 - RandomData.getRandom(1000);
                sV_y = 0 - RandomData.getRandom(500);
                break;
            case "2":
            case "7":
                sV_x = 0 - RandomData.getRandom(1000);
                sV_y = RandomData.getRandom(500);
                break;
            case "3":
            case "8":
                sV_x = RandomData.getRandom(500);
                sV_y = 0 - RandomData.getRandom(1000);
                break;
            case "4":
            case "9":
                sV_x = RandomData.getRandom(500);
                sV_y = 0 - RandomData.getRandom(1000);
                break;
            case "5":
            case "0":
                sV_x = 0 - RandomData.getRandom(500);
                sV_y = RandomData.getRandom(500);
                break;
        }
        //创建动画，参数表示他的子动画是否共用一个插值器
        AnimationSet animationSet = new AnimationSet(true);
        //添加动画
        animationSet.addAnimation(new TranslateAnimation(0, sV_x,0,sV_y));
        animationSet.addAnimation(new AlphaAnimation(0.1f, 0.6f));
        animationSet.addAnimation(new ScaleAnimation(0.0f, 1.9f, 0.0f, 1.9f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f));
        //设置插值器
        animationSet.setInterpolator(new LinearInterpolator());
        //设置动画持续时长
        animationSet.setDuration(6000);
        //设置动画结束之后是否保持动画的目标状态
        animationSet.setFillAfter(true);
//        //设置动画结束之后是否保持动画开始时的状态
//        animationSet.setFillBefore(false);
        //设置重复模式
        animationSet.setRepeatMode(AnimationSet.REVERSE);
        //设置重复次数
        animationSet.setRepeatCount(AnimationSet.INFINITE);
        //设置动画延时时间
        animationSet.setStartOffset(50);
        //取消动画
        animationSet.cancel();
        //释放资源
        animationSet.reset();

        return animationSet;
    }

    private void setNormalAnim(int index,View view,ImageView iv) {
        view.setTag(index);
        //创建动画，参数表示他的子动画是否共用一个插值器
        AnimationSet animationSet = getAnim(index);
        animationSet.setAnimationListener(new ReStartAnimationListener(view,iv));
        //开始动画
        view.startAnimation(animationSet);

        Animation animation = AnimationUtils.loadAnimation(this.getContext(),R.anim.designs_dialog_anim);
//        显示动画
        iv.setAnimation(animation);
    }

    /**
     * 重复启动动画
     */
    private class ReStartAnimationListener implements Animation.AnimationListener {

        private View mView,mV_img;

        ReStartAnimationListener(View view,View iV){
            mView = view;
            mV_img = iV;
        }

        public void onAnimationEnd(Animation animation) {
            //创建动画，参数表示他的子动画是否共用一个插值器
            AnimationSet animationSet = getAnim((Integer) mView.getTag());
            animationSet.setAnimationListener(new ReStartAnimationListener(mView,mV_img));
            //开始动画
            mView.startAnimation(animationSet);
            Animation animationR = AnimationUtils.loadAnimation(mV_img.getContext(),R.anim.designs_dialog_anim);
//        显示动画
            mV_img.setAnimation(animationR);
        }

        public void onAnimationRepeat(Animation animation) {

        }

        public void onAnimationStart(Animation animation) {

        }

    }
}
