package com.kjq.common.ui.designs;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.NinePatchDrawable;
import android.os.Build;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewDebug;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.ColorRes;
import androidx.annotation.DrawableRes;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.databinding.InverseBindingMethod;
import androidx.databinding.InverseBindingMethods;
import androidx.databinding.ObservableArrayList;
import androidx.databinding.ObservableInt;

import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.kjq.common.R;
import com.kjq.common.base.mvvm.adapter.LayoutManagers;
import com.kjq.common.ui.designs.dialog.litener.IViewEven;
import com.kjq.common.utils.GlideUtil;
import com.kjq.common.utils.ScreenSizeUtils;
import com.kjq.common.utils.data.StringUtils;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

/**
 * 头像控件
 */
@InverseBindingMethods({
        @InverseBindingMethod(type = HeadPortraitView.class, attribute = "commonSrc", event = "commonSrcChanged"),
        @InverseBindingMethod(type = HeadPortraitView.class, attribute = "commonText", event = "commonTextChanged"),
        @InverseBindingMethod(type = HeadPortraitView.class, attribute = "commonTextVisibility", event = "commonTextVisibilityChanged"),
        @InverseBindingMethod(type = HeadPortraitView.class, attribute = "commonImgArray", event = "commonImgArrayChanged")})
public class HeadPortraitView extends CardView {
    public static final int I_VISIBLE = 0;
    public static final int I_INVISIBLE = 1;
    public static final int I_GONE = 2;

    /**
     * 边框的宽度
     */
    private String mText;
    private int mI_resId;
    private ImageView mImageView;
    private NineGridImageView<String> mNineGridImageView;
    private TextView mTextView;
    private @ColorRes int mI_textShadow = R.color.common_black;
    private int mI_text;
    private int mI_textShadowH;
    private int mI_textAndShadowShow ;
    private CustomCircle mCustomCircle;

    public HeadPortraitView(Context context) {
        this(context,null);
    }

    public HeadPortraitView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public HeadPortraitView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setCardElevation(0.0f);
        setRadius(ScreenSizeUtils.INSTANCE.dip2px(5));
        initAttributeSet(attrs);
        initView();
    }

    private void initView(){
        LayoutParams sLayoutParams_match = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        mImageView = new ImageView(getContext());
        mImageView.setLayoutParams(sLayoutParams_match);
        mImageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        if (mI_resId != 0){
            mImageView.setImageResource(mI_resId);
        }

        mNineGridImageView = new NineGridImageView<>(getContext());
        mNineGridImageView.setLayoutParams(sLayoutParams_match);
        mNineGridImageView.setVisibility(GONE);

        LayoutParams sLayoutParams_circle = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ScreenSizeUtils.INSTANCE.dip2px(mI_textShadowH));
        sLayoutParams_circle.gravity = Gravity.CENTER | Gravity.BOTTOM;
        mCustomCircle = new CustomCircle(getContext());
        mCustomCircle.setLayoutParams(sLayoutParams_circle);
        mCustomCircle.setI_textShadow(mI_textShadow);
        mCustomCircle.setVisibility(mI_textAndShadowShow);

        mTextView = new TextView(getContext());
        LayoutParams sLayoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        sLayoutParams.gravity = Gravity.CENTER | Gravity.BOTTOM;
        if (StringUtils.isEmpty(mText) && mI_text != 0){
            mTextView.setText(String.valueOf(mI_text));
        }else {
            mTextView.setText(mText);
        }
        mTextView.setGravity(Gravity.CENTER);
        mTextView.setMaxLines(1);
        mTextView.setIncludeFontPadding(false);
        mTextView.setTextColor(Color.WHITE);
        mTextView.setLayoutParams(sLayoutParams);
        mTextView.setTextSize(mI_textShadowH);
        mTextView.setVisibility(mI_textAndShadowShow);

        addView(mImageView);
        addView(mNineGridImageView);
        addView(mCustomCircle);
        addView(mTextView);
    }

    private void initAttributeSet(AttributeSet attrs){
        TypedArray sTypedArray = getContext().obtainStyledAttributes(attrs, R.styleable.HeadPortraitView);
        mText = sTypedArray.getString(R.styleable.HeadPortraitView_commonText);
        mI_text = sTypedArray.getInt(R.styleable.HeadPortraitView_commonText,0);
        mI_resId = sTypedArray.getResourceId(R.styleable.HeadPortraitView_commonSrc,0);
        mI_textShadowH = sTypedArray.getInt(R.styleable.HeadPortraitView_commonTextShadowH,10);
        mI_textShadow = sTypedArray.getResourceId(R.styleable.HeadPortraitView_commonTextShadow,R.color.common_shadow);
        int sI_show = sTypedArray.getInt(R.styleable.HeadPortraitView_commonTextVisibility,I_VISIBLE);
        if (sI_show == I_VISIBLE){
            mI_textAndShadowShow = View.VISIBLE;
        }else if (sI_show == I_INVISIBLE){
            mI_textAndShadowShow = View.INVISIBLE;
        }else if (sI_show == I_GONE){
            mI_textAndShadowShow = View.GONE;
        }
        sTypedArray.recycle();
    }

    Bitmap drawable2Bitmap(Drawable drawable) {
        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable) drawable).getBitmap();
        } else if (drawable instanceof NinePatchDrawable) {
            Bitmap bitmap = Bitmap
                    .createBitmap(
                            drawable.getIntrinsicWidth(),
                            drawable.getIntrinsicHeight(),
                            drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
                                    : Bitmap.Config.RGB_565);
            Canvas canvas = new Canvas(bitmap);
            drawable.setBounds(0, 0, drawable.getIntrinsicWidth(),
                    drawable.getIntrinsicHeight());
            drawable.draw(canvas);
            return bitmap;
        } else {
            return null;
        }
    }

    public void setCommonSrc(final String url){
        if (!StringUtils.isEmpty(url)){
            mImageView.setTag(url);
            GlideUtil.newCacheInstance(url, new RequestListener<Drawable>() {
                @Override
                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target target, boolean isFirstResource) {
                    return false;
                }

                @Override
                public boolean onResourceReady(Drawable resource, Object model, Target target, DataSource dataSource, boolean isFirstResource) {
                    if (url.equals(mImageView.getTag())){
                        mImageView.setVisibility(VISIBLE);
                        mNineGridImageView.setVisibility(GONE);
                        mImageView.setImageDrawable(resource);
                    }
                    return false;
                }
            });
        }else {
            mImageView.setVisibility(GONE);
        }
    }

    public void setCommonSrc(@DrawableRes int i_resId){
        mImageView.setImageResource(i_resId);
    }

    public void setCommonText(String text){
        if (text.equals("0")){
            mCustomCircle.setVisibility(GONE);
            mTextView.setText("");
        }else {
            mCustomCircle.setVisibility(VISIBLE);
            mTextView.setText(text);
        }
    }

    public void setCommonText(@StringRes int i_resId){
        try {
            mTextView.setText(i_resId);
        }catch (Exception e){
            setCommonText(String.valueOf(i_resId));
        }
    }

    public void setCommonTextVisibility(ObservableInt observableInt){
        setCommonTextVisibility(observableInt.get());
    }

    public void setCommonTextVisibility(int visibility){
        mCustomCircle.setVisibility(visibility);
        mTextView.setVisibility(visibility);
    }

    public void setCommonImgArray(ObservableArrayList<String> observableArrayList){
        List<String> sList = new ArrayList<>(observableArrayList);
        setCommonImgArray(sList);
    }

    public void setCommonImgArray(List list){
        if (list == null || list.isEmpty()){
            return;
        }
        mImageView.setVisibility(GONE);
        mNineGridImageView.setVisibility(VISIBLE);
        mNineGridImageView.setAdapter(NineGridImageViewAdapter.getDefaultAdapter());
        mNineGridImageView.setImagesData(list);
    }
}
