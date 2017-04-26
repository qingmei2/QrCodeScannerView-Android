package com.mei_husky.library.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;

import com.mei_husky.library.util.DpUtils;

import static android.util.TypedValue.COMPLEX_UNIT_DIP;

/**
 * 这个View覆盖于QRCodeScannerView之上，用于UI的绘制，比如扫描框，扫描线,扫描框内外的背景颜色以及透明度等等。
 */
public class QRCoverView extends View {

  private final Paint paint = new Paint();
  private final Paint cornerPaint = new Paint();

  private final float top;
  private final float left;
  private final float cornerH;
  private final float cornerW;
  private Context context;

  public RectF getViewFinderRect() {
    return viewFinderRect;
  }

  private RectF viewFinderRect;

  public QRCoverView(Context context, AttributeSet attrs) {
    super(context, attrs);
    this.context = context;

    DisplayMetrics displayMetrics = getResources().getDisplayMetrics();

    left = (displayMetrics.widthPixels - DpUtils.dip2px(context, 540)) / 2;
    top = (displayMetrics.heightPixels - DpUtils.dip2px(context, 540)) / 3;
    Log.i("tag","屏幕宽高=" + displayMetrics.widthPixels + "*" + displayMetrics.heightPixels );
    Log.i("tag","扫描区域 left & top=" + left + "  &  " + top );
    Log.i("tag","矩形区域 宽高=" + DpUtils.dip2px(context, 540) + "  *  " + DpUtils.dip2px(context, 540) );
//    top = TypedValue.applyDimension(COMPLEX_UNIT_DIP, 28, displayMetrics);
//    left = TypedValue.applyDimension(COMPLEX_UNIT_DIP, 40, displayMetrics);
    cornerH = TypedValue.applyDimension(COMPLEX_UNIT_DIP, 48, displayMetrics);
    cornerW = TypedValue.applyDimension(COMPLEX_UNIT_DIP, 8, displayMetrics);
    paint.setColor(ContextCompat.getColor(getContext(), android.R.color.darker_gray));
    cornerPaint.setColor(ContextCompat.getColor(getContext(), android.R.color.holo_red_dark));
  }

  @Override
  protected void onDraw(Canvas canvas) {
    viewFinderRect = new RectF(left, top, left + DpUtils.dip2px(context, 540), top + DpUtils.dip2px(context, 540));

    canvas.drawRect(0, 0, viewFinderRect.left, getHeight(), paint);
    canvas.drawRect(viewFinderRect.left, 0, getWidth(), viewFinderRect.top, paint);
    canvas.drawRect(viewFinderRect.right, viewFinderRect.top, getWidth(), getHeight(), paint);
    canvas.drawRect(viewFinderRect.left, viewFinderRect.bottom, viewFinderRect.right,
            getHeight(), paint);

    canvas.drawRect(viewFinderRect.left - cornerW, viewFinderRect.top - cornerW,
            viewFinderRect.left, viewFinderRect.top + cornerH - cornerW, cornerPaint);
    canvas.drawRect(viewFinderRect.left, viewFinderRect.top - cornerW,
            viewFinderRect.left + cornerH - cornerW, viewFinderRect.top, cornerPaint);
    canvas.drawRect(viewFinderRect.right - cornerH + cornerW, viewFinderRect.top - cornerW,
            viewFinderRect.right + cornerW, viewFinderRect.top, cornerPaint);
    canvas.drawRect(viewFinderRect.right, viewFinderRect.top, viewFinderRect.right + cornerW,
            viewFinderRect.top + cornerH - cornerW, cornerPaint);
    canvas.drawRect(viewFinderRect.right, viewFinderRect.bottom - cornerH + cornerW,
            viewFinderRect.right + cornerW, viewFinderRect.bottom + cornerW, cornerPaint);
    canvas.drawRect(viewFinderRect.right - cornerH + cornerW, viewFinderRect.bottom,
            viewFinderRect.right, viewFinderRect.bottom + cornerW, cornerPaint);
    canvas.drawRect(viewFinderRect.left - cornerW, viewFinderRect.bottom - cornerH + cornerW,
            viewFinderRect.left, viewFinderRect.bottom + cornerW, cornerPaint);
    canvas.drawRect(viewFinderRect.left, viewFinderRect.bottom,
            viewFinderRect.left + cornerH - cornerW, viewFinderRect.bottom + cornerW, cornerPaint);

  }

  public void setBackColorByState(int state){
//    paint.setColor(getResources().getColor(bgStateColors[state]));
    Log.i("tag","paint.setcolor==" + state);
    invalidate();
  }

}
