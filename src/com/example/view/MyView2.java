package com.example.view;

import java.util.Arrays;
import java.util.Comparator;

import android.bluetooth.BluetoothAdapter.LeScanCallback;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

public class MyView2 extends View {

	private int screenWidth;
	private int screenHeight;
	private Paint mPaint;
	private int numOfPoint;
	private Canvas mCanvas;
	private int height, width;
	float maxX = 0, maxY = 0;

	class Coordinate {
		float x;
		float y;
	}
	
	Coordinate[] coordinate; 

	float[] x = { 12, 23, 42, 22, 43, 56, 32, 14, 45, 78, 64, 32, 13, 43,
			56.5f, 36, 82, 45 };
	float[] y = { 24, 63, 35, 54, 56, 25, 73, 25, 17, 51, 16, 78, 44, 64, 24,
			26, 73, 45 };
	
	

	public MyView2(Context context) {
		this(context, null);
	}

	public MyView2(Context context, AttributeSet attribute) {
		super(context, attribute);
		mPaint = new Paint();
		mPaint.setColor(Color.BLUE);
		mPaint.setStrokeWidth(2.0f);
		screenWidth = getContext().getResources().getDisplayMetrics().widthPixels;
		screenHeight = getContext().getResources().getDisplayMetrics().heightPixels;
		numOfPoint = 11;
		mCanvas = new Canvas();
		coordinate = new Coordinate[18];
		for (int i=0; i<18; i++)
		{
			coordinate[i] = new Coordinate();
			coordinate[i].x = x[i];
			coordinate[i].y = y[i];
		}
	}

	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		super.onDraw(canvas);
		canvas.drawLine(80, (int) (screenHeight * 0.666),
				(int) (screenWidth * 0.955), (int) (screenHeight * 0.666),
				mPaint);
		canvas.drawLine(80, (int) (screenHeight * 0.666), 80, 50, mPaint);
		height = (int) (screenHeight * 0.666 - 65);
		width = (int) (screenWidth * 0.955 - 105);
		drawArrow(canvas);
		drawPointX(numOfPoint, canvas);
		drawPointY(canvas);
		drawNumX(canvas);
		drawNumY(canvas);
	}
	
	//画箭头
	void drawArrow(Canvas canvas) {
		canvas.drawLine((int) (screenWidth * 0.955 - 10),
				(int) (screenHeight * 0.666 - 10), (int) (screenWidth * 0.955),
				(int) (screenHeight * 0.666), mPaint);
		canvas.drawLine((int) (screenWidth * 0.955 - 10),
				(int) (screenHeight * 0.666 + 10), (int) (screenWidth * 0.955),
				(int) (screenHeight * 0.666), mPaint);
		canvas.drawLine(70, 60, 80, 50, mPaint);
		canvas.drawLine(90, 60, 80, 50, mPaint);
		draGraph(canvas,coordinate);
	}

	//在x轴上画点
	void drawPointX(int loc, Canvas mCanvas) {
		mPaint.setColor(Color.RED);
		mPaint.setStrokeWidth(7.0f);
		int y = (int) (screenHeight * 0.666);
		int length = (int) (screenWidth * 0.955 - 80) / numOfPoint;
		for (int i = 0; i < numOfPoint; i++) {
			mCanvas.drawCircle(i * length + 80, y, 3.0f, mPaint);
		}
	}

	//在y轴上画点
	void drawPointY(Canvas canvas) {
		Paint mPaint = new Paint();
		mPaint.setColor(Color.RED);
		mPaint.setStrokeWidth(7.0f);
		int length = (int) (screenHeight * 0.666 - 50) / numOfPoint;
		int y = (int) (screenHeight * 0.666);
		for (int i = 0; i < numOfPoint; i++) {
			canvas.drawCircle(80, y - i * length, 3.0f, mPaint);
		}
	}

	//画折线图
	public void draGraph(Canvas canvas, Coordinate[]coordinate) {
		int num = x.length;
		float left = 0,right = 0;
		Paint mPaint = new Paint();
		mPaint.setColor(Color.YELLOW);
		mPaint.setStrokeWidth(3.0f);
		maxX = 0;
		maxY = 0;
		float stepX = 0, stepY = 0;
		Arrays.sort(x);
		Arrays.sort(y);
		Arrays.sort(coordinate,new MyCompartor());
		maxX = x[num - 1];
		maxY = y[num - 1];
		stepX = maxX / width;
		stepY = maxY / height;
		for (int i=0; i<18; i++)
		{
			x[i] = coordinate[i].x;
			y[i] = coordinate[i].y;
		}
		x[0] = x[0] / stepX;
		y[0] = y[0] / stepY;
		drawCoor("( " + (int)coordinate[0].x  + " , " + (int)coordinate[0].y + " )",x[0] + 80,(float) (screenHeight * 0.666 - y[0]),false,canvas);
		for (int i = 1; i < num; i++) {
			x[i] = x[i] / stepX;
			y[i] = y[i] / stepY;
			canvas.drawLine(x[i - 1] + 80,
					(int) (screenHeight * 0.666 - y[i - 1]), x[i] + 80,
					(int) (screenHeight * 0.666 - y[i]), mPaint);
			if (i < num - 1)
			{
				left = (y[i] - y[i - 1]) / (x[i] - x[i - 1]);
				right = (y[i + 1] - y[i]) / (x[i + 1] - x[i]);
				if (left < right)
				{
					drawCoor("( " + (int)coordinate[i].x + " , " + (int)coordinate[i].y + " )",x[i] + 80,(float) (screenHeight * 0.666 - y[i]),false,canvas);
				}
				else {
					drawCoor("( " + (int)coordinate[i].x  + " , " + (int)coordinate[i].y + " )",x[i] + 80,(float) (screenHeight * 0.666 - y[i]),true,canvas);
				}
			}
			else {
				drawCoor("( " + (int)coordinate[i].x  + " , " + (int)coordinate[i].y + " )",x[i] + 80,(float) (screenHeight * 0.666 - y[i]),false,canvas);
			}
			
		}
	}
	
	void drawNumX(Canvas canvas)
	{
		int step = (int)maxX / 10;
		float stepx = (float) ((screenWidth * 0.955 - 80) / numOfPoint);
		Paint mPaint = new Paint();
		mPaint.setStrokeWidth(3.0f);
		mPaint.setColor(Color.BLUE);
		mPaint.setTextSize(20);
		for (int i=1; i<numOfPoint; i++)
		{
			canvas.drawText("" + i * step, i * stepx + 70, (float) (screenHeight * 0.666 + 30), mPaint);
		}
		canvas.drawText("0", 70, (float) (screenHeight * 0.666 + 30), mPaint);
	}
	
	void drawNumY(Canvas canvas)
	{
		int step = (int)maxY / 10;
		float stepy = (float) ((screenHeight * 0.666 - 50) / numOfPoint);
		Paint mPaint = new Paint();
		mPaint.setStrokeWidth(3.0f);
		mPaint.setColor(Color.BLUE);
		mPaint.setTextSize(20);
		for (int i=1; i<numOfPoint; i++)
		{
			canvas.drawText("" + i * step, 50, (float) (screenHeight * 0.666 - i * stepy + 10), mPaint);
		}
	}
	
	void drawCoor(String string,float x,float y,boolean up,Canvas canvas)
	{
		Paint paint = new Paint();
		paint.setColor(Color.BLUE);
		paint.setTextSize(25);
		paint.setStrokeWidth(3.0f);
		if (up == true)
		{
			canvas.drawText(string, x - 10, y - 10, mPaint);
		}
		else {
			canvas.drawText(string, x - 10, y + 10, mPaint);
		}
	}
	

	class MyCompartor implements Comparator<Object> {

		@Override
		public int compare(Object lhs, Object rhs) {
			float x1 = ((Coordinate)lhs).x;
			float x2 = ((Coordinate)rhs).x;
			if (x1 <= x2)
				return -1;	
			else {
				return 1;
			}
			
		}
	}
	

}
