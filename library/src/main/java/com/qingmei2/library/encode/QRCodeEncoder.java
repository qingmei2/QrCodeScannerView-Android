package com.qingmei2.library.encode;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.util.DisplayMetrics;
import android.widget.ImageView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by QingMei2 on 2017/7/19.
 * desc:
 */
public class QRCodeEncoder implements IQrCodeEncoder {

    private static final int NO_ICON_RES = 0;

    private Activity activity;

    private BitmapCompressor bmpConpressor;

    public QRCodeEncoder(Activity activity) {
        setActivity(activity);
        bmpConpressor = new BitmapCompressor();
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    @Override
    public Bitmap createQrCode(String content, int width) {
        return createQrCode(content, width, NO_ICON_RES);
    }

    @Override
    public Bitmap createQrCode(String content, int width, int iconRes) {
        return createQrCode(content, width, iconRes, true);
    }

    @Override
    public Bitmap createQrCode(String content, int width, Drawable iconDrawable) {
        return createQrCode(content, width, iconDrawable, true);
    }

    @Override
    public Bitmap createQrCode(String content, int width, Bitmap iconBitmap) {
        return createQrCode(content, width, iconBitmap, true);
    }

    @Override
    public void createQrCode2ImageView(String content, ImageView imageView) {
        this.createQrCode2ImageView(content, imageView, NO_ICON_RES, false);
    }

    @Override
    public void createQrCode2ImageView(String content, ImageView imageView, int iconRes) {
        this.createQrCode2ImageView(content, imageView, iconRes, true);
    }

    @Override
    public void createQrCode2ImageView(String content, ImageView imageView, Drawable iconDrawable) {
        this.createQrCode2ImageView(content, imageView, iconDrawable, true);
    }

    @Override
    public void createQrCode2ImageView(String content, ImageView imageView, Bitmap iconBitmap) {
        this.createQrCode2ImageView(content, imageView, iconBitmap, true);
    }

    @Override
    public void createQrCode2ImageView(String content, ImageView imageView, int iconRes, boolean hasIcon) {
        Bitmap iconBitmap = null;
        if (iconRes != NO_ICON_RES)
            iconBitmap = getBitmapByRes(iconRes);
        this.createQrCode2ImageView(content, imageView, iconBitmap, hasIcon);
    }

    @Override
    public void createQrCode2ImageView(String content, ImageView imageView, Drawable iconDrawable, boolean hasIcon) {
        Bitmap iconBitmap = null;
        if (iconDrawable != null)
            iconBitmap = getBitmapByDrawable(iconDrawable);
        this.createQrCode2ImageView(content, imageView, iconBitmap, hasIcon);
    }

    @Override
    public void createQrCode2ImageView(String content, ImageView imageView, Bitmap iconBitmap, boolean hasIcon) {
        DisplayMetrics dm = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
        int mScreenWidth = dm.widthPixels;

        Bitmap qrCode = createQrCode(content, mScreenWidth, iconBitmap, hasIcon);
        if (qrCode != null) {
            imageView.setImageBitmap(qrCode);
        }
    }

    @Override
    public Bitmap createQrCode(String content, int width, int iconRes, boolean hasIcon) {
        Bitmap iconBitmap = null;
        if (iconRes != NO_ICON_RES)
            iconBitmap = getBitmapByRes(iconRes);
        return createQrCode(content, width, iconBitmap, hasIcon);
    }

    @Override
    public Bitmap createQrCode(String content, int width, Drawable iconDrawable, boolean hasIcon) {
        Bitmap iconBitmap = null;
        if (iconDrawable != null)
            iconBitmap = getBitmapByDrawable(iconDrawable);
        return createQrCode(content, width, iconBitmap, hasIcon);
    }

    /**
     * Create QrCode with text content.
     * We suggest that developer adjusts the content length, more content text means more difficult for scanning result on the device.
     *
     * @param content    QrCode content
     * @param width      QrCode width&Height
     * @param iconBitmap center icon if you want add icon in Bitmap
     * @param hasIcon    if false,iconBitmap will not show on QrCode bitmap
     * @return QrCode bitmap
     */
    @Override
    public Bitmap createQrCode(String content, int width, Bitmap iconBitmap, boolean hasIcon) {
        try {
            //配置参数
            Map<EncodeHintType, Object> hints = new HashMap<>();
            hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
            //容错级别
            hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
            // TODO set blank width, default is 4
            // hints.put(EncodeHintType.MARGIN, 2);

            BitMatrix bitMatrix = new QRCodeWriter().encode(content, BarcodeFormat.QR_CODE, width, width, hints);
            int[] pixels = new int[width * width];
            // 下面这里按照二维码的算法，逐个生成二维码的图片，
            // 两个for循环是图片横列扫描的结果
            for (int y = 0; y < width; y++) {
                for (int x = 0; x < width; x++) {
                    if (bitMatrix.get(x, y)) {
                        pixels[y * width + x] = 0xff000000; //black
                    } else {
                        pixels[y * width + x] = 0xffffffff; //white
                    }
                }
            }

            Bitmap bitmap = Bitmap.createBitmap(width, width, Bitmap.Config.ARGB_8888);
            bitmap.setPixels(pixels, 0, width, 0, 0, width, width);

            //Add the Icon to the QrCode bitmap center
            if (iconBitmap != null && hasIcon) {
                bitmap = addIcon2QrCode(iconBitmap, bitmap);
            }

            //compress bitmap
            return bmpConpressor.compressBitmap(bitmap);
        } catch (WriterException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Bitmap getBitmapByRes(int resId) {
        return BitmapFactory.decodeResource(activity.getResources(), resId);
    }

    @Override
    public Bitmap getBitmapByDrawable(Drawable drawable) {
        return Bitmap.createBitmap(
                drawable.getIntrinsicWidth(),
                drawable.getIntrinsicHeight(),
                drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
                        : Bitmap.Config.RGB_565);
    }

    /**
     * Add the Icon to the QrCode bitmap center
     *
     * @param icon   the icon
     * @param qrCode the qrCode
     * @return QrCode Bitmap with icon
     */
    @Override
    public Bitmap addIcon2QrCode(Bitmap icon, Bitmap qrCode) {
        if (qrCode == null) {
            return null;
        }

        if (icon == null) {
            return qrCode;
        }

        int srcWidth = qrCode.getWidth();
        int srcHeight = qrCode.getHeight();
        int iconWidth = icon.getWidth();
        int iconHeight = icon.getHeight();

        if (srcWidth == 0 || srcHeight == 0) {
            return null;
        }

        if (iconWidth == 0 || iconHeight == 0) {
            return qrCode;
        }

        //Icon size/ QrCode size = 1:5
        float scaleFactor = srcWidth * 1.0f / 5 / iconWidth;
        Bitmap bitmap = Bitmap.createBitmap(srcWidth, srcHeight, Bitmap.Config.ARGB_8888);
        try {
            Canvas canvas = new Canvas(bitmap);
            canvas.drawBitmap(qrCode, 0, 0, null);
            canvas.scale(scaleFactor, scaleFactor, srcWidth / 2, srcHeight / 2);
            canvas.drawBitmap(icon, (srcWidth - iconWidth) / 2, (srcHeight - iconHeight) / 2, null);

            canvas.save(Canvas.ALL_SAVE_FLAG);
            canvas.restore();
        } catch (Exception e) {
            bitmap = null;
            e.getStackTrace();
        }
        return bitmap;
    }
}
