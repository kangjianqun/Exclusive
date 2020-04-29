package com.kjq.common.ui.designs;

import android.content.Context;
import android.text.Layout;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatTextView;

import com.kjq.common.utils.data.StringUtils;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class ReadView extends AppCompatTextView {
    private int mI_uselessCount;
    public ReadView(Context context) {
        super(context);
    }

    public ReadView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ReadView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public int getI_uselessCount() {
        return mI_uselessCount;
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        mI_uselessCount = resize();
    }

    /**
     * 去除当前页无法显示的字
     * @return 去掉的字数
     */
    public int resize() {
        CharSequence oldContent = getText();
        CharSequence newContent = oldContent.subSequence(0, getCharNum());
        setText(newContent);
        return oldContent.length() - newContent.length();
    }

    /**
     * 去除当前页无法显示的字
     * @return 去掉的字数
     */
    public int displaySize() {
        return getCharNum();
    }

    /**
     * 获取当前页总字数
     */
    public int getCharNum() {
        return getLayout().getLineEnd(getLineNum());
    }

    /**
     * 获取当前页总行数
     */
    public int getLineNum() {
        Layout layout = getLayout();
        int topOfLastLine = getHeight() - getPaddingTop() - getPaddingBottom() - getLineHeight();
        return layout.getLineForVertical(topOfLastLine);
    }

    public ArrayList<Integer> page(@NotNull String msg){
        ArrayList<Integer> sList = new ArrayList<>();
        if (StringUtils.isEmpty(msg)){
            sList.add(0);
            return sList;
        }
        String sS_allMsg = msg;
        int sI_allLength = msg.length();
        while (sS_allMsg.length() > 0){
            setText(sS_allMsg);
            int sI_count = displaySize();
            if (sI_count <= sI_allLength){
                sList.add(sI_count);
            }
            sS_allMsg = sS_allMsg.substring(sI_count);
        }
        return sList;
    }
}
