package com.example.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;

import com.example.myview.R;

public class MyView extends View {
	private String mTitleText;
	/**
	 * �ı�����ɫ
	 */
	private int mTitleTextColor;
	/**
	 * �ı��Ĵ�С
	 */
	private int mTitleTextSize;

	/**
	 * ����ʱ�����ı����Ƶķ�Χ
	 */
	private Rect rect;
	private Paint mPaint;
	private Bitmap mImage;
	private int mImageScale;
	private Rect mTextBound;

	int mWidth = 0, mHeight = 0;

	public MyView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public MyView(Context context) {
		this(context, null);
	}

	public MyView(Context context, AttributeSet attrs, int defStyle) {
		// TODO Auto-generated constructor stub
		super(context, attrs, defStyle);
		/**
		 * ���������������Զ�����ʽ����
		 */
		TypedArray a = context.getTheme().obtainStyledAttributes(attrs,
				R.styleable.MyView, defStyle, 0);
		// Be sure to call StyledAttributes.recycle() when you are done with the
		// array.

		int n = a.getIndexCount();
		for (int i = 0; i < n; i++) {
			int attr = a.getIndex(i);
			switch (attr) {
			case R.styleable.MyView_image: {
				mImage = BitmapFactory.decodeResource(getResources(),
						a.getResourceId(attr, 0));
				break;
			}
			case R.styleable.MyView_imageScaleType: {
				mImageScale = a.getInt(attr, 0);
				break;
			}
			case R.styleable.MyView_titleText: {
				mTitleText = a.getString(attr);
				System.out.println("mTitleText = " + mTitleText);
				break;
			}
			case R.styleable.MyView_titleTextColor: {
				// Ĭ����ɫ����Ϊ��ɫ
				mTitleTextColor = a.getColor(attr, Color.BLACK);
				System.out.println("mTitleTextColor = " + mTitleTextColor);
				break;

			}
			case R.styleable.MyView_titleTextSize: {
				// Ĭ������Ϊ16sp��TypeValueҲ���԰�spת��Ϊpx
				mTitleTextSize = a.getDimensionPixelSize(attr, (int) TypedValue
						.applyDimension(TypedValue.COMPLEX_UNIT_SP, 16,
								getResources().getDisplayMetrics()));
				System.out.println("mTitleTextSize = " + mTitleTextSize);
				break;

			}

			}

		}
		a.recycle();

		/**
		 * ��û����ı��Ŀ�͸�
		 */
		mPaint = new Paint();
		mPaint.setTextSize(mTitleTextSize);
		mPaint.setColor(mTitleTextColor);
		rect = new Rect();
		mTextBound = new Rect();
		mPaint.getTextBounds(mTitleText, 0, mTitleText.length(), rect);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// int widthMode = MeasureSpec.getMode(widthMeasureSpec);
		// int widthSize = MeasureSpec.getSize(widthMeasureSpec);
		// int heightMode = MeasureSpec.getMode(heightMeasureSpec);
		// int heightSize = MeasureSpec.getSize(heightMeasureSpec);
		// int width;
		// int height;
		// if (widthMode == MeasureSpec.EXACTLY) {
		// width = widthSize;
		// } else {
		// mPaint.setTextSize(mTitleTextSize);
		// mPaint.getTextBounds(mTitleText, 0, mTitleText.length(), rect);
		// float textWidth = rect.width();
		// int desired = (int) (getPaddingLeft() + textWidth +
		// getPaddingRight());
		// width = desired;
		// }
		//
		// if (heightMode == MeasureSpec.EXACTLY) {
		// height = heightSize;
		// } else {
		// mPaint.setTextSize(mTitleTextSize);
		// mPaint.getTextBounds(mTitleText, 0, mTitleText.length(), rect);
		// float textHeight = rect.height();
		// int desired = (int) (getPaddingTop() + textHeight +
		// getPaddingBottom());
		// height = desired;
		// }
		//
		// setMeasuredDimension(width, height);
		// super.onMeasure(widthMeasureSpec, heightMeasureSpec);

		/**
		 * ���ÿ��
		 */
		int specMode = MeasureSpec.getMode(widthMeasureSpec);
		int specSize = MeasureSpec.getSize(widthMeasureSpec);

		if (specMode == MeasureSpec.EXACTLY)// match_parent , accurate
		{
			Log.e("xxx", "EXACTLY");
			mWidth = specSize;
		} else {
			// ��ͼƬ�����Ŀ�
			int desireByImg = getPaddingLeft() + getPaddingRight()
					+ mImage.getWidth();
			// ����������Ŀ�
			int desireByTitle = getPaddingLeft() + getPaddingRight()
					+ mTextBound.width();

			if (specMode == MeasureSpec.AT_MOST)// wrap_content
			{
				int desire = Math.max(desireByImg, desireByTitle);
				mWidth = Math.min(desire, specSize);
				Log.e("xxx", "AT_MOST");
			}
		}

		/***
		 * ���ø߶�
		 */

		specMode = MeasureSpec.getMode(heightMeasureSpec);
		specSize = MeasureSpec.getSize(heightMeasureSpec);
		if (specMode == MeasureSpec.EXACTLY)// match_parent , accurate
		{
			mHeight = specSize;
		} else {
			int desire = getPaddingTop() + getPaddingBottom()
					+ mImage.getHeight() + mTextBound.height();
			if (specMode == MeasureSpec.AT_MOST)// wrap_content
			{
				mHeight = Math.min(desire, specSize);
			}
		}
		setMeasuredDimension(mWidth, mHeight);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		// mPaint.setColor(Color.YELLOW);
		// canvas.drawRect(0, 0, getMeasuredWidth(), getMeasuredHeight(),
		// mPaint);
		//
		// mPaint.setColor(mTitleTextColor);
		// canvas.drawText(mTitleText, getWidth() / 2 - rect.width() / 2,
		// getHeight() / 2 + rect.height() / 2, mPaint);
		// super.onDraw(canvas);
		/**
		 * �߿�
		 */
		mPaint.setStrokeWidth(4);
		mPaint.setStyle(Paint.Style.STROKE);
		mPaint.setColor(Color.CYAN);
		canvas.drawRect(0, 0, getMeasuredWidth(), getMeasuredHeight(), mPaint);

		rect.left = getPaddingLeft();
		rect.right = mWidth - getPaddingRight();
		rect.top = getPaddingTop();
		rect.bottom = mHeight - getPaddingBottom();

		mPaint.setColor(mTitleTextColor);
		mPaint.setStyle(Style.FILL);
		/**
		 * ��ǰ���õĿ��С��������Ҫ�Ŀ�ȣ��������Ϊxxx...
		 */
		if (mTextBound.width() > mWidth) {
			TextPaint paint = new TextPaint(mPaint);
			String msg = TextUtils.ellipsize(mTitleText, paint,
					(float) mWidth - getPaddingLeft() - getPaddingRight(),
					TextUtils.TruncateAt.END).toString();
			canvas.drawText(msg, getPaddingLeft(),
					mHeight - getPaddingBottom(), mPaint);

		} else {
			// ������������������
			canvas.drawText(mTitleText, mWidth / 2 - mTextBound.width() * 1.0f / 2,
					mHeight - getPaddingBottom(), mPaint);
		}

		// ȡ��ʹ�õ��Ŀ�
		rect.bottom -= mTextBound.height();

		if (mImageScale == 0) {
			canvas.drawBitmap(mImage, null, rect, mPaint);
		} else {
			// ������еľ��η�Χ
			rect.left = mWidth / 2 - mImage.getWidth() / 2;
			rect.right = mWidth / 2 + mImage.getWidth() / 2;
			rect.top = (mHeight - mTextBound.height()) / 2 - mImage.getHeight()
					/ 2;
			rect.bottom = (mHeight - mTextBound.height()) / 2
					+ mImage.getHeight() / 2;

			canvas.drawBitmap(mImage, null, rect, mPaint);
		}
	}

}
