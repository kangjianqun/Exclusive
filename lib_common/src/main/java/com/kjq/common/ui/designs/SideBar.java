package com.kjq.common.ui.designs;


import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ListView;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.kjq.common.utils.data.Constant;


/**
 * 适用与list View的快速选择
 */
public class SideBar extends View {
	private char[] l;
	private SectionIndexer sectionIndexter = null;
	private ListView list;
	private TextView mDialogText;
	Paint paint = new Paint();

	// private int m_nItemHeight = 50;
	// private Context mContext;
	public SideBar(Context context) {
		super(context);
		// mContext = context;

		// m_nItemHeight = (ETGlobal.H - 200) / 35;
		init();
	}

	public SideBar(Context context,TextView hintView){
		super(context);
		init();
		mDialogText = hintView;
	}

	public SideBar(Context context, AttributeSet attrs) {
		super(context, attrs);
		// mContext = context;
		// m_nItemHeight = (ETGlobal.H - 200) / 35;
		init();
	}

	public SideBar(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// mContext = context;
		// m_nItemHeight = (ETGlobal.H - 200) / 35;
		init();
	}

	private void init() {
		l = new char[] { 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K',
				'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W',
				'X', 'Y', 'Z' };
	}

	public void setListView(ListView _list) {
		list = _list;
		sectionIndexter = (SectionIndexer) _list.getAdapter();

	}

	public void setDialogText(TextView dialogText) {
		mDialogText = dialogText;
	}

	@SuppressLint("ClickableViewAccessibility")
	public boolean onTouchEvent(MotionEvent event) {
		super.onTouchEvent(event);

		float y = event.getY();
		int idx = (int) (y / getHeight() * l.length);
		//int idx = i / m_nItemHeight;
		if (idx >= l.length) {
			idx = l.length - 1;
		} else if (idx < 0) {
			idx = 0;
		}
		if (event.getAction() == MotionEvent.ACTION_DOWN
				|| event.getAction() == MotionEvent.ACTION_MOVE) {
			// ETTool.MessageBox((Activity) mContext, 1.0f, ""+l[idx], false);
			// Toast.makeText(mContext, ""+l[idx], Toast.LENGTH_SHORT).show();
			if (mDialogText != null){
				mDialogText.setVisibility(View.VISIBLE);
				mDialogText.setText("" + l[idx]);
			}

			if (sectionIndexter == null) {
				sectionIndexter = (SectionIndexer) list.getAdapter();
			}
			if (sectionIndexter == null) {
				return true;
			}
			int position = sectionIndexter.getPositionForSection(l[idx]);
			if (position == -1) {
				return true;
			}
			list.setSelection(position);
		} else {
			if (mDialogText != null){
				mDialogText.setVisibility(View.INVISIBLE);
			}
		}
		return true;
	}

	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		int height = getHeight();// 获取对应高度
		int width = getWidth(); // 获取对应宽度
		int singleHeight = height / l.length;// 获取每一个字母的高度

		if (Constant.AppInfo.H < 320){
			return;
		}
		for (int i = 0; i < l.length; i++) {
			paint.setColor(Color.rgb(33,56,98));
			paint.setTypeface(Typeface.DEFAULT_BOLD);
			paint.setAntiAlias(true);
			paint.setTextSize(30);
			// x坐标等于中间-字符串宽度的一半.
			float xPos = width / 2 - paint.measureText(String.valueOf(l[i]))
					/ 2;
			float yPos = singleHeight * i + singleHeight;
			canvas.drawText(String.valueOf(l[i]), xPos, yPos, paint);
			paint.reset();// 重置画笔
		}

		// paint.setColor(0xff595c61);
		// paint.setTextSize(26);
		// paint.setTextAlign(Paint.Align.CENTER);
		// float widthCenter = getMeasuredWidth() / 2;
		// for (int i = 0; i < l.length; i++) {
		// canvas.drawText(String.valueOf(l[i]), widthCenter, ((ETGlobal.H -
		// 200)/ l.length) * 0.75f + (i * ((ETGlobal.H - 200)/ l.length) *
		// 0.75f), paint);
		// }

	}
}

// public class SideBar extends View {
// // 触摸事件
// private OnTouchingLetterChangedListener onTouchingLetterChangedListener;
// // 26个字母
// public static String[] b = { "A", "B", "C", "D", "E", "F", "G", "H", "I",
// "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V",
// "W", "X", "Y", "Z", "#" };
// private int choose = -1;// 选中
// private Paint paint = new Paint();
//
// private TextView mTextDialog;
//
// public void setTextView(TextView mTextDialog) {
// this.mTextDialog = mTextDialog;
// }
//
//
// public SideBar(Context context, AttributeSet attrs, int defStyle) {
// super(context, attrs, defStyle);
// }
//
// public SideBar(Context context, AttributeSet attrs) {
// super(context, attrs);
// }
//
// public SideBar(Context context) {
// super(context);
// }
//
// /**
// * 重写这个方法
// */
// protected void onDraw(Canvas canvas) {
// super.onDraw(canvas);
// // 获取焦点改变背景颜色.
// int height = getHeight();// 获取对应高度
// int width = getWidth(); // 获取对应宽度
// int singleHeight = height / b.length;// 获取每一个字母的高度
//
// for (int i = 0; i < b.length; i++) {
// paint.setColor(Color.rgb(33, 65, 98));
// // paint.setColor(Color.WHITE);
// paint.setTypeface(Typeface.DEFAULT_BOLD);
// paint.setAntiAlias(true);
// paint.setTextSize(20);
// // 选中的状态
// if (i == choose) {
// paint.setColor(Color.parseColor("#3399ff"));
// paint.setFakeBoldText(true);
// }
// // x坐标等于中间-字符串宽度的一半.
// float xPos = width / 2 - paint.measureText(b[i]) / 2;
// float yPos = singleHeight * i + singleHeight;
// canvas.drawText(b[i], xPos, yPos, paint);
// paint.reset();// 重置画笔
// }
//
// }
//
// @Override
// public boolean dispatchTouchEvent(MotionEvent event) {
// final int action = event.getAction();
// final float y = event.getY();// 点击y坐标
// final int oldChoose = choose;
// final OnTouchingLetterChangedListener listener =
// onTouchingLetterChangedListener;
// final int c = (int) (y / getHeight() * b.length);//
// 点击y坐标所占总高度的比例*b数组的长度就等于点击b中的个数.
//
// switch (action) {
// case MotionEvent.ACTION_UP:
// setBackgroundDrawable(new ColorDrawable(0x00000000));
// choose = -1;//
// invalidate();
// if (mTextDialog != null) {
// mTextDialog.setVisibility(View.INVISIBLE);
// }
// break;
//
// default:
// setBackgroundDrawable(new ColorDrawable(0x00000000));
// //setBackgroundResource(R.drawable.sidebar_background);
// if (oldChoose != c) {
// if (c >= 0 && c < b.length) {
// if (listener != null) {
// listener.onTouchingLetterChanged(b[c]);
// }
// if (mTextDialog != null) {
// mTextDialog.setText(b[c]);
// mTextDialog.setVisibility(View.VISIBLE);
// }
//
// choose = c;
// invalidate();
// }
// }
//
// break;
// }
// return true;
// }
//
// /**
// * 向外公开的方法
// *
// * @param onTouchingLetterChangedListener
// */
// public void setOnTouchingLetterChangedListener(
// OnTouchingLetterChangedListener onTouchingLetterChangedListener) {
// this.onTouchingLetterChangedListener = onTouchingLetterChangedListener;
// }
//
// /**
// * 接口
// *
// * @author coder
// *
// */
// public interface OnTouchingLetterChangedListener {
// public void onTouchingLetterChanged(String s);
// }
//
// }
