package com.ada.library;

import static android.graphics.Paint.ANTI_ALIAS_FLAG;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.EditText;

/**
 * 实现输入框密码输入样式为大圆白点，并且自定义光标�?
 * 
 * @author jqd
 * 
 */
public class PasswordEditText extends EditText {

	private int textLength;
	int i = 1;
	private int passwordLength;
	private int passwordColor;
	private int cursorColor;
	private float passwordWidth;
	private float passwordRadius;
	private int cursorInterval;
	private int cursorWidth;
	// private ScheduledFuture future;
	private Paint passwordPaint = new Paint(ANTI_ALIAS_FLAG);
	private Paint borderPaint = new Paint(ANTI_ALIAS_FLAG);

	private ScheduledExecutorService scheduledExecutorService;

	private int width;

	private int height;
	/**
	 * 光标颜色
	 */
	private int[] color = new int[2] ;
	
	private Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			switch (msg.what) {
			case 0:
				invalidate();
				break;
			}
		}
	};
	

	public PasswordEditText(Context context, AttributeSet attrs) {
		super(context, attrs);
		Log.i("jqd", "enter PasswordEditText");
		final int defaultPasswordLength = 10;
		final int defaultPasswordColor = Color.WHITE;
		final int defaultCursorColor = Color.BLACK;
		final float defaultPasswordWidth = 10;
		final float defaultPasswordRadius = 3;
		/**
		 * 光标闪烁间隔
		 */
		final int defaultCursorInterval = 400;
		/**
		 * 光标宽度
		 */
		final int defaultCursorWidth = 4;
		TypedArray a = context.getTheme().obtainStyledAttributes(attrs,
				R.styleable.PasswordInputView, 0, 0);
		try {
			passwordLength = a.getInt(
					R.styleable.PasswordInputView_passwordLength,
					defaultPasswordLength);
			passwordColor = a.getColor(
					R.styleable.PasswordInputView_passwordColor,
					defaultPasswordColor);
			cursorInterval = a.getInt(
					R.styleable.PasswordInputView_cursorInterval,
					defaultCursorInterval);
			cursorWidth = a.getInt(
					R.styleable.PasswordInputView_cursorWidth,
					defaultCursorWidth);
			cursorColor = a.getColor(R.styleable.PasswordInputView_cursorColor,
					defaultCursorColor);
			passwordWidth = a.getDimension(
					R.styleable.PasswordInputView_passwordWidth,
					defaultPasswordWidth);
			passwordRadius = a.getDimension(
					R.styleable.PasswordInputView_passwordRadius,
					defaultPasswordRadius);
		} finally {
			a.recycle();
		}
		
		color[0] =  Color.TRANSPARENT ;
		color[1] = cursorColor;
		passwordPaint.setStrokeWidth(passwordWidth);
		passwordPaint.setStyle(Paint.Style.FILL);
		passwordPaint.setColor(passwordColor);
	}

	@SuppressLint("DrawAllocation")
	@Override
	protected void onDraw(Canvas canvas) {
		Log.i("jqd", "enter onDraw");
		width = getWidth();
		height = getHeight();

		// 话圆�?
		float cx, cy = height / 2;
		float half = width / passwordLength / 2;
		for (int i = 0; i < textLength; i++) {
			cx = width * i / passwordLength + half;
			canvas.drawCircle(cx, cy, passwordWidth, passwordPaint);
		}
		// 光标
		borderPaint.setStrokeWidth(cursorWidth);
		borderPaint.setColor(color[i]);
		float x = width * textLength / passwordLength;
		canvas.drawLine(x, 0, x, height, borderPaint);
	}

	@Override
	protected void onFocusChanged(boolean focused, int direction,
			Rect previouslyFocusedRect) {
		// TODO Auto-generated method stub
		if (focused) {
			changeCursor();
		} else {
			i = 1;
			mHandler.sendEmptyMessage(0);
			if (scheduledExecutorService != null) {
				scheduledExecutorService.shutdownNow();
				scheduledExecutorService = null;
			}
		}
		super.onFocusChanged(focused, direction, previouslyFocusedRect);
	}

	public void onDestroy() {
		if (scheduledExecutorService != null) {
			scheduledExecutorService.shutdownNow();
			scheduledExecutorService = null;
		}
	}

	private void changeCursor() {
		// TODO Auto-generated method stub

		if (scheduledExecutorService == null) {
			scheduledExecutorService = Executors
					.newSingleThreadScheduledExecutor();
		}
		Runnable runnable = new Runnable() {

			@Override
			public void run() {
				if (i == 1) {
					i = 0;
				} else {
					i = 1;
				}
				mHandler.sendEmptyMessage(0);
			}
		};
		scheduledExecutorService.scheduleWithFixedDelay(runnable, cursorInterval,
				cursorInterval, TimeUnit.MILLISECONDS);
	}

	@Override
	protected void onTextChanged(CharSequence text, int start,
			int lengthBefore, int lengthAfter) {
		super.onTextChanged(text, start, lengthBefore, lengthAfter);
		this.textLength = text.toString().length();
		invalidate();
	}

	public int getPasswordLength() {
		return passwordLength;
	}

	public void setPasswordLength(int passwordLength) {
		this.passwordLength = passwordLength;
		invalidate();
	}

	public int getPasswordColor() {
		return passwordColor;
	}

	public void setPasswordColor(int passwordColor) {
		this.passwordColor = passwordColor;
		passwordPaint.setColor(passwordColor);
		invalidate();
	}

	public float getPasswordWidth() {
		return passwordWidth;
	}

	public void setPasswordWidth(float passwordWidth) {
		this.passwordWidth = passwordWidth;
		passwordPaint.setStrokeWidth(passwordWidth);
		invalidate();
	}

	public float getPasswordRadius() {
		return passwordRadius;
	}

	public void setPasswordRadius(float passwordRadius) {
		this.passwordRadius = passwordRadius;
		invalidate();
	}
}
