package com.qingmei2.library.encode;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

/**
 * Created by QingMei on 2017/7/28.
 * desc:
 */

public class BitmapCompressor implements IBitmapCompressor {

    private int bmpQuality = 64 * 1024;    //compress bitmap size -> 64k

    /**
     * set compress bitmap size,with unit KB,default value is 64KB
     * @param bmpQuality
     */
    public void setBmpQuality(int bmpQuality) {
        this.bmpQuality = bmpQuality * 1024;
    }

    @Override
    public Bitmap compressBitmap(Bitmap bmp) {
        byte[] bytes = compressBitmap2ByteArray(bmp);
        ByteArrayInputStream inputs = new ByteArrayInputStream(bytes);
        return BitmapFactory.decodeStream(inputs, null, null);
    }

    private byte[] compressBitmap2ByteArray(Bitmap bmp) {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, output);
        float zoom = (float) Math.sqrt(32 * 1024 / (float) output.toByteArray().length); //获取缩放比例

        // set Rect datas
        Matrix matrix = new Matrix();
        matrix.setScale(zoom, zoom);

        // create new bitmap with Rect datas
        Bitmap resultBitmap = Bitmap.createBitmap(bmp, 0, 0, bmp.getWidth(), bmp.getHeight(), matrix, true);

        output.reset();

        resultBitmap.compress(Bitmap.CompressFormat.JPEG, 100, output);

        // if result still > bmpQuality,compress bitmap continue.
        while (output.toByteArray().length > bmpQuality) {
            matrix.setScale(0.9f, 0.9f);//scale 0.1 every time

            resultBitmap = Bitmap.createBitmap(
                    resultBitmap, 0, 0,
                    resultBitmap.getWidth(), resultBitmap.getHeight(), matrix, true);

            output.reset();
            resultBitmap.compress(Bitmap.CompressFormat.JPEG, 100, output);
        }
        return output.toByteArray();
    }
}
