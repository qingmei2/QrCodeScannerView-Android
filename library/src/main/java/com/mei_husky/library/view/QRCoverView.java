package com.mei_husky.library.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;

import com.mei_husky.library.R;
import com.mei_husky.library.util.DpUtils;

import static android.util.TypedValue.COMPLEX_UNIT_DIP;

/**
 * 这个View覆盖于QRCodeScannerView之上，用于UI的绘制，比如扫描框，扫描线,扫描框内外的背景颜色以及透明度等等。
 */
public class QRCoverView extends View {

    private final Paint paint = new Paint();
    private final Paint cornerPaint = new Paint();

    //扫描框的宽和高
    private float scannerW;
    private float scannerH;
    //扫描框距离屏幕左上角的距离
    private float top;
    private float left;
    //扫描边框的长度和厚度
    private float cornerH;
    private float cornerW;
    private Context context;
    //扫描边框是否包裹扫描框
    private boolean isCornerOutside = false;

    public RectF getViewFinderRect() {
        return viewFinderRect;
    }

    private RectF viewFinderRect;

    public QRCoverView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;

        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();

        left = (displayMetrics.widthPixels - DpUtils.dip2px(context, 180)) / 2;
        top = (displayMetrics.heightPixels - DpUtils.dip2px(context, 180)) / 3;
//    Log.i("tag","屏幕宽高=" + displayMetrics.widthPixels + "*" + displayMetrics.heightPixels );
//    Log.i("tag","扫描区域 left & top=" + left + "  &  " + top );
//    Log.i("tag","矩形区域 宽高=" + DpUtils.dip2px(context, 540) + "  *  " + DpUtils.dip2px(context, 540) );
//    top = TypedValue.applyDimension(COMPLEX_UNIT_DIP, 28, displayMetrics);
//    left = TypedValue.applyDimension(COMPLEX_UNIT_DIP, 40, displayMetrics);
        cornerH = TypedValue.applyDimension(COMPLEX_UNIT_DIP, 48, displayMetrics);
        cornerW = TypedValue.applyDimension(COMPLEX_UNIT_DIP, 10, displayMetrics);
        paint.setColor(ContextCompat.getColor(getContext(), R.color.cover_bg));
        cornerPaint.setColor(ContextCompat.getColor(getContext(), R.color.colorPrimaryDark));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        viewFinderRect = new RectF(left, top, left + DpUtils.dip2px(context, 180), top + DpUtils.dip2px(context, 180));

        canvas.drawRect(0, 0, viewFinderRect.left, getHeight(), paint);
        canvas.drawRect(viewFinderRect.left, 0, getWidth(), viewFinderRect.top, paint);
        canvas.drawRect(viewFinderRect.right, viewFinderRect.top, getWidth(), getHeight(), paint);
        canvas.drawRect(viewFinderRect.left, viewFinderRect.bottom, viewFinderRect.right,
                getHeight(), paint);

        //扫描边框在扫描框外部
        if (isCornerOutside) {
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
        } else {
            //扫描边框在扫描框内部
            canvas.drawRect(viewFinderRect.left, viewFinderRect.top,
                    viewFinderRect.left + cornerW, viewFinderRect.top + cornerH, cornerPaint);
            canvas.drawRect(viewFinderRect.left + cornerW, viewFinderRect.top,
                    viewFinderRect.left + cornerH, viewFinderRect.top + cornerW, cornerPaint);

            canvas.drawRect(viewFinderRect.right - cornerH, viewFinderRect.top,
                    viewFinderRect.right, viewFinderRect.top + cornerW, cornerPaint);
            canvas.drawRect(viewFinderRect.right - cornerW, viewFinderRect.top + cornerW, viewFinderRect.right,
                    viewFinderRect.top + cornerH, cornerPaint);

            canvas.drawRect(viewFinderRect.right - cornerW, viewFinderRect.bottom - cornerH,
                    viewFinderRect.right, viewFinderRect.bottom, cornerPaint);
            canvas.drawRect(viewFinderRect.right - cornerH, viewFinderRect.bottom - cornerW,
                    viewFinderRect.right - cornerW, viewFinderRect.bottom, cornerPaint);

            canvas.drawRect(viewFinderRect.left, viewFinderRect.bottom - cornerH,
                    viewFinderRect.left + cornerW, viewFinderRect.bottom, cornerPaint);
            canvas.drawRect(viewFinderRect.left + cornerW, viewFinderRect.bottom - cornerW,
                    viewFinderRect.left + cornerH, viewFinderRect.bottom, cornerPaint);
        }

    }

    /**
     * 设置扫描框外部颜色
     *
     * @param colorRes
     */
    public QRCoverView setCoverViewOutsideColor(int colorRes) {
        paint.setColor(getResources().getColor(colorRes));
        return this;
    }

    /**
     * 设置扫描边框颜色
     *
     * @param colorRes
     */
    public QRCoverView setCoverViewCornerColor(int colorRes) {
        cornerPaint.setColor(getResources().getColor(colorRes));
        return this;
    }

    /**
     * 设置扫描边框宽高
     *
     * @param cornerH 扫描边框的长度
     * @param cornerW 扫描边框的厚度
     * @param isDp    若为true则将传入的参数单位视为dp，false 则单位视为px
     */
    public QRCoverView setCoverViewCorner(int cornerH, int cornerW, boolean isDp) {
        if (isDp) {
            cornerH = DpUtils.dip2px(getContext(), cornerH);
            cornerW = DpUtils.px2dip(getContext(), cornerW);
        }
        this.cornerH = cornerH;
        this.cornerW = cornerW;
        return this;
    }

    /**
     * 设置扫描边框宽高（像素）
     */
    public QRCoverView setCoverViewCorner(int cornerH, int cornerW) {
        return setCoverViewCorner(cornerH, cornerW, false);
    }

    /**
     * 设置扫描框的宽高
     *
     * @param width
     * @param height
     */
    public QRCoverView setCoverViewScanner(int width, int height) {
        this.scannerH = height;
        this.scannerW = width;
        return this;
    }


    /**
     * 设置扫描边框的位置，默认扫描边框在扫描框的内部；
     * 若调用该setCoverViewConnerFace(true)，则扫描边框则会包裹扫描框显示在界面上；
     * 否则默认扫描边框内贴在扫描框内（大多数app默认都是该模式）。
     *
     * @param isOutside
     */
    public QRCoverView setCoverViewConnerFace(boolean isOutside) {
        this.isCornerOutside = isOutside;
        return this;
    }

    /**
     * 重新执行onDraw()
     */
    public void commitUi() {
        invalidate();
    }
}
