package com.kjq.common.ui.designs;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.kjq.common.R;
import com.kjq.common.utils.ScreenSizeUtils;
import com.kjq.common.utils.data.StringUtils;
import com.kjq.common.utils.performance.AppManager;

import static android.content.Context.INPUT_METHOD_SERVICE;

public class SearchViewExtend extends LinearLayout implements TextWatcher, TextView.OnEditorActionListener {

    private ImageView mIV_sign;
    private EditText mET_content;
    private ImageView mIV_close;
    private TextWatcher mTextWatcher;
    private SearchClickListener mSearchClickListener;
    private SearchStartListener mSearchStartListener;
    public static final int Space8 = ScreenSizeUtils.INSTANCE.dip2px(8);

    public SearchViewExtend(Context context) {
        this(context,null);
    }

    public SearchViewExtend(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public SearchViewExtend(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
        initAttrs(attrs);
    }

    private void initView(){
        setOrientation(HORIZONTAL);
        mIV_sign = new ImageView(getContext());
        mIV_sign.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT));
        mET_content = new EditText(getContext());
        LayoutParams sLayoutParams = new LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT);
        sLayoutParams.weight = 1;
        mET_content.setLayoutParams(sLayoutParams);
        mIV_close = new ImageView(getContext());
        mIV_close.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT));
        addView(mIV_sign);
        addView(mET_content);
        addView(mIV_close);

        mIV_sign.setPadding(Space8,0,Space8,0);
        mET_content.setBackgroundColor(Color.TRANSPARENT);
        mET_content.setFocusable(true);
        mET_content.setFocusableInTouchMode(true);
        mET_content.requestFocus();
        mET_content.setTextSize(16);
        mET_content.setImeOptions(EditorInfo.IME_ACTION_SEARCH);
        mET_content.setOnEditorActionListener(this);
        mET_content.addTextChangedListener(this);
        mET_content.setPadding(Space8,0,Space8,0);
        mET_content.setMaxLines(1);
        mET_content.setMaxEms(50);
        mET_content.setInputType(InputType.TYPE_CLASS_TEXT);
        mIV_close.setPadding(Space8,0,Space8,0);
    }

    private void initAttrs(AttributeSet attrs){
        TypedArray sTypedArray = getContext().obtainStyledAttributes(attrs, R.styleable.SearchViewExtend);

        int sI_signId = sTypedArray.getResourceId(R.styleable.SearchViewExtend_commonSrc,0);
        int sI_closeId = sTypedArray.getResourceId(R.styleable.SearchViewExtend_commonCloseIcon,R.drawable.common_svg_ic_delete_forever_red_24dp);
        String sS_hint = sTypedArray.getString(R.styleable.SearchViewExtend_commonHint);
        if (StringUtils.isEmpty(sS_hint)){
            int sI_txtId = sTypedArray.getResourceId(R.styleable.SearchViewExtend_commonHint,0);
            if (sI_txtId != 0){
                mET_content.setHint(sI_txtId);
            }
        }else {
            mET_content.setHint(sS_hint);
        }
        mIV_sign.setImageResource(sI_signId);
        mIV_close.setImageResource(sI_closeId);
        mIV_close.setVisibility(View.INVISIBLE);
        mIV_close.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mET_content.setText("");
                closeV(false);
            }
        });
        sTypedArray.recycle();
    }

    private void closeV(boolean display){
        if (mIV_close == null){
            return;
        }
        if (display){
            mIV_close.setVisibility(VISIBLE);
            mIV_close.setEnabled(true);
        }else {
            mIV_close.setVisibility(INVISIBLE);
            mIV_close.setEnabled(false);
        }
    }

    /**
     * 显示键盘
     *
     * @param et 输入焦点
     */
    public void showInput(final EditText et) {
        et.requestFocus();
        InputMethodManager imm = (InputMethodManager) mET_content.getContext().getSystemService(INPUT_METHOD_SERVICE);
        imm.showSoftInput(et, InputMethodManager.SHOW_IMPLICIT);
    }

    /**
     * 隐藏键盘
     */
    protected void hideInput() {
        InputMethodManager imm = (InputMethodManager) mET_content.getContext().getSystemService(INPUT_METHOD_SERVICE);
        View v = AppManager.getAppManager().currentActivity().getWindow().peekDecorView();
        if (null != v) {
            imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
        }
    }

    public void addTextChangedListener(TextWatcher textWatcher) {
        mTextWatcher = textWatcher;
    }

    public void addSearchClickListener(SearchClickListener listener){
        mSearchClickListener = listener;
    }

    public void addSearchStartListener(SearchStartListener listener){
        mSearchStartListener = listener;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        if (mTextWatcher != null){
            mTextWatcher.beforeTextChanged(s, start, count, after);
        }
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        closeV(mET_content.getText().length() > 0);
        if (mTextWatcher != null){
            mTextWatcher.onTextChanged(s, start, before, count);
        }
    }

    @Override
    public void afterTextChanged(Editable s) {
        if (mTextWatcher != null){
            mTextWatcher.afterTextChanged(s);
        }
        if (mSearchStartListener != null){
            mSearchStartListener.start(s.toString());
        }
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_SEARCH){
            hideInput();
            if (mSearchClickListener != null){
                mSearchClickListener.onClick(mET_content.getText().toString());
            }
            if (mSearchStartListener != null){
                mSearchStartListener.start(mET_content.getText().toString());
            }
            return true;
        }
        return false;
    }

    public interface SearchClickListener{
        void onClick(String content);
    }

    public interface SearchStartListener{
        void start(String key);
    }
}
