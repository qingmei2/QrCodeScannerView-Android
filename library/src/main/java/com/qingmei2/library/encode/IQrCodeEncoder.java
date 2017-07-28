package com.qingmei2.library.encode;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

/**
 * Created by QingMei on 2017/7/27.
 * desc:
 */

interface IQrCodeEncoder {

    Bitmap createQrCode(String content, int widthAndHeight);

    void createQrCode2ImageView(String content, ImageView imageView);

    Bitmap createQrCode(String content, int width, int iconRes);

    Bitmap createQrCode(String content, int width, Drawable iconDrawable);

    Bitmap createQrCode(String content, int width, Bitmap iconBitmap);

    void createQrCode2ImageView(String content, ImageView imageView, int iconRes);

    void createQrCode2ImageView(String content, ImageView imageView, Drawable iconDrawable);

    void createQrCode2ImageView(String content, ImageView imageView, Bitmap iconBitmap);

    Bitmap createQrCode(String content, int width, int iconRes, boolean hasIcon);

    Bitmap createQrCode(String content, int width, Drawable iconDrawable, boolean hasIcon);

    Bitmap createQrCode(String content, int width, Bitmap iconBitmap, boolean hasIcon);

    void createQrCode2ImageView(String content, ImageView imageView, int iconRes, boolean hasIcon);

    void createQrCode2ImageView(String content, ImageView imageView, Drawable iconDrawable, boolean hasIcon);

    void createQrCode2ImageView(String content, ImageView imageView, Bitmap iconBitmap, boolean hasIcon);

    Bitmap getBitmapByRes(int resId);

    Bitmap getBitmapByDrawable(Drawable drawable);

    Bitmap addIcon2QrCode(Bitmap icon, Bitmap qrCode);
}
