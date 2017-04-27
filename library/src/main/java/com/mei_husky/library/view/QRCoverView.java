package com.mei_husky.library.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;

import com.mei_husky.library.R;
import com.mei_husky.library.util.DpUtils;

/**
 * 这个View覆盖于QRCodeScannerView之上，用于UI的绘制，比如扫描框，扫描线,扫描框内外的背景颜色以及透明度等等。
 */
public class QRCoverView extends View {

    private final Paint paint = new Paint();
    private final Paint cornerPaint = new Paint();

    //扫描框的宽和高
    private float scannerW;
    private float scannerH;
    //扫描框的上边距和左边距
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

        //默认大小为180dp
        left = (displayMetrics.widthPixels - DpUtils.dip2px(context, 180)) / 2;
        top = (displayMetrics.heightPixels - DpUtils.dip2px(context, 180)) / 3;
        //默认扫描框为16dp长，3dp宽
        cornerH = DpUtils.dip2px(context, 16);
        cornerW = DpUtils.dip2px(context, 3);
        //默认背景色和扫描边框颜色
        paint.setColor(ContextCompat.getColor(getContext(), R.color.cover_bg));
        cornerPaint.setColor(ContextCompat.getColor(getContext(), R.color.colorPrimaryDark));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //默认扫描框为180dp
        if (scannerW == 0f || scannerH == 0f) {
            viewFinderRect = new RectF(left, top, left + DpUtils.dip2px(context, 180), top + DpUtils.dip2px(context, 180));
        } else {
            viewFinderRect = new RectF(left, top, left + scannerW, top + scannerH);
        }
        drawScanBackground(canvas, viewFinderRect);
        drawScanCorner(canvas, viewFinderRect);
    }

    /**
     * 绘制扫描框外部背景
     *
     * @param canvas
     * @param viewFinderRect
     */
    private void drawScanBackground(Canvas canvas, RectF viewFinderRect) {
        canvas.drawRect(0, 0, viewFinderRect.left, getHeight(), paint);
        canvas.drawRect(viewFinderRect.left, 0, getWidth(), viewFinderRect.top, paint);
        canvas.drawRect(viewFinderRect.right, viewFinderRect.top, getWidth(), getHeight(), paint);
        canvas.drawRect(viewFinderRect.left, viewFinderRect.bottom, viewFinderRect.right,
                getHeight(), paint);
    }

    /**
     * 绘制扫描边框
     *
     * @param canvas
     * @param viewFinderRect
     */
    private void drawScanCorner(Canvas canvas, RectF viewFinderRect) {
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
            this.cornerH = DpUtils.dip2px(context, cornerH);
            this.cornerW = DpUtils.dip2px(context, cornerW);
        } else {
            this.cornerH = cornerH;
            this.cornerW = cornerW;
        }
        return this;
    }

    /**
     * 设置扫描边框宽高（像素）
     */
    public QRCoverView setCoverViewCorner(int cornerH, int cornerW) {
        return setCoverViewCorner(cornerH, cornerW, false);
    }

    /**
     * 设置扫描框的宽高(dp)
     *
     * @param width
     * @param height
     */
    public QRCoverView setCoverViewScanner(int width, int height) {
        //配置扫描框的宽高
        this.scannerH = DpUtils.dip2px(context, height);
        this.scannerW = DpUtils.dip2px(context, width);
        //重新获取扫描框的左边距和上边距
        left = (getResources().getDisplayMetrics().widthPixels - scannerW) / 2;
        top = (getResources().getDisplayMetrics().heightPixels - scannerH) / 3;
        return this;
    }


    /**
     * 设置扫描边框的位置；
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
        Log.i("tag", "重新绘制...");
        Log.i("tag", "扫描边框isOutSide=" + isCornerOutside);
        Log.i("tag", "扫描边框大小（h/w）=" + cornerH + "/" + cornerW);
        Log.i("tag", "扫描边框颜色...=" + cornerPaint.getColor());
        Log.i("tag", "扫描框大小（h/w）=" + scannerW + "/" + scannerH);
        invalidate();
    }
}
