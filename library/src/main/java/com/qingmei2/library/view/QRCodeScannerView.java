
package com.qingmei2.library.view;

import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.graphics.PointF;
import android.hardware.Camera;
import android.os.AsyncTask;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.WindowManager;

import com.google.zxing.BinaryBitmap;
import com.google.zxing.ChecksumException;
import com.google.zxing.DecodeHintType;
import com.google.zxing.FormatException;
import com.google.zxing.NotFoundException;
import com.google.zxing.PlanarYUVLuminanceSource;
import com.google.zxing.Result;
import com.google.zxing.ResultPoint;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.QRCodeReader;
import com.qingmei2.library.zxing.CameraManager;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.Map;

import static android.hardware.Camera.getCameraInfo;

public class QRCodeScannerView extends SurfaceView
        implements SurfaceHolder.Callback, Camera.PreviewCallback {

    private static final String TAG = QRCodeScannerView.class.getName();

    private boolean hasCameraPermission = false;  //是否拥有相机权限
    private int face = -1;  //前置/后置摄像头
    private QRCodeReader mQRCodeReader;
    private int mPreviewWidth;        //View预览宽度
    private int mPreviewHeight;       //View预览高度
    private CameraManager mCameraManager;
    private boolean mQrDecodingEnabled = true;  //QRCodeScannerView能否扫描
    private DecodeFrameTask decodeFrameTask;
    private Map<DecodeHintType, Object> decodeHints;

    public QRCodeScannerView(Context context) {
        this(context, null);
    }

    public QRCodeScannerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        if (isInEditMode()) {
            return;
        }
        if (checkCameraHardware()) {
            mCameraManager = new CameraManager(getContext());
            mCameraManager.setPreviewCallback(this);
            getHolder().addCallback(this);
            setBackCamera();  //默认使用后置摄像头
        } else {
            throw new RuntimeException("Error: Camera not found");
        }
    }

    /**
     * Set QR decoding enabled/disabled.
     * default value is true
     *
     * @param qrDecodingEnabled decoding enabled/disabled.
     */
    public void setQRDecodingEnabled(boolean qrDecodingEnabled) {
        this.mQrDecodingEnabled = qrDecodingEnabled;
    }

    /**
     * Set QR hints required for decoding
     *
     * @param decodeHints hints for decoding qrcode
     */
    public void setDecodeHints(Map<DecodeHintType, Object> decodeHints) {
        this.decodeHints = decodeHints;
    }

    /**
     * Starts camera preview and decoding
     */
    public void startCamera() {
        Log.d(TAG, "startCamera");
        mCameraManager.startPreview();
    }

    /**
     * Stop camera preview and decoding
     */
    public void stopCamera() {
        mCameraManager.stopPreview();
    }

    /**
     * Set Camera autofocus interval value
     * default value is 5000 ms.
     *
     * @param autofocusIntervalInMs autofocus interval value
     */
    public void setAutofocusInterval(long autofocusIntervalInMs) {
        if (mCameraManager != null) {
            mCameraManager.setAutofocusInterval(autofocusIntervalInMs);
        }
    }

    /**
     * 触发自动对焦
     */
    public void forceAutoFocus() {
        if (mCameraManager != null) {
            mCameraManager.forceAutoFocus();
        }
    }

    /**
     * 是否打开闪光灯
     *
     * @param enabled torch enabled/disabled.
     */
    public void setTorchEnabled(boolean enabled) {
        if (mCameraManager != null) {
            mCameraManager.setTorchEnabled(enabled);
        }
    }

    /**
     * Allows user to specify the camera ID, rather than determine
     * it automatically based on available cameras and their orientation.
     *
     * @param cameraId camera ID of the camera to use. A negative value means "no preference".
     */
    public void setPreviewCameraId(int cameraId) {
        mCameraManager.setPreviewCameraId(cameraId);
    }

    /**
     * Camera preview from device back camera
     */
    public void setBackCamera() {
        Log.d(TAG, "setBackCamera");
        setPreviewCameraId(Camera.CameraInfo.CAMERA_FACING_BACK);
        face = Camera.CameraInfo.CAMERA_FACING_BACK;
    }

    /**
     * Camera preview from device front camera
     */
    public void setFrontCamera() {
        setPreviewCameraId(Camera.CameraInfo.CAMERA_FACING_FRONT);
        face = Camera.CameraInfo.CAMERA_FACING_FRONT;
    }

    @Override
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();

        if (decodeFrameTask != null) {
            decodeFrameTask.cancel(true);
            decodeFrameTask = null;
        }
    }

    /**
     * 切换到前置/后置摄像头
     */
    public void switchCameraFace() {
        releaseCamera();
        //如果是前置,切换到后置;反之亦然
        face = face == Camera.CameraInfo.CAMERA_FACING_FRONT ? Camera.CameraInfo.CAMERA_FACING_BACK : Camera.CameraInfo.CAMERA_FACING_FRONT;
        openCamera(face);
    }


    /**
     * 打开对应相机资源
     */
    private void openCamera(int cameraFace) {
        try {
            mCameraManager.setPreviewCameraId(cameraFace);
            mCameraManager.openDriver(getHolder(), this.getWidth(), this.getHeight());
            mCameraManager.setDisplayOrientation(getCameraDisplayOrientation());
            mCameraManager.startPreview();
            mCameraManager.setPreviewCallback(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 释放相机资源
     */
    private void releaseCamera() {
        mCameraManager.setPreviewCallback(null);
        mCameraManager.stopPreview();
        mCameraManager.closeDriver();
    }

    /****************************************************
     *   SurfaceHolder.Callback接口，处理Camera相关逻辑
     ****************************************************/

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        Log.d(TAG, "surfaceCreated");

        //先检查相机权限，当没有相机权限时，return；
        if (onCheckCameraPermissionListener != null
                && onCheckCameraPermissionListener.onCheckCameraPermission()) {
            hasCameraPermission = true;
            holder.setFormat(PixelFormat.TRANSPARENT);
        } else {
            hasCameraPermission = false;
            return;
        }

        try {
            // Indicate camera, our View dimensions
            Log.d(TAG, "mCameraManager.openDriver");
            mCameraManager.openDriver(holder, this.getWidth(), this.getHeight());
        } catch (IOException e) {
            Log.w(TAG, "Can not openDriver: " + e.getMessage());
            mCameraManager.closeDriver();
        }

        try {
            mQRCodeReader = new QRCodeReader();
            mCameraManager.startPreview();
        } catch (Exception e) {
            Log.e(TAG, "Exception: " + e.getMessage());
            mCameraManager.closeDriver();
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        Log.d(TAG, "surfaceChanged");
        if (!hasCameraPermission)
            return;
        if (holder.getSurface() == null) {
            Log.e(TAG, "Error: preview surface does not exist");
            return;
        }

        if (mCameraManager.getPreviewSize() == null) {
            Log.e(TAG, "Error: preview size does not exist");
            return;
        }

        mPreviewWidth = mCameraManager.getPreviewSize().x;
        mPreviewHeight = mCameraManager.getPreviewSize().y;

        mCameraManager.stopPreview();

        // Fix the camera sensor rotation
        mCameraManager.setPreviewCallback(this);
        mCameraManager.setDisplayOrientation(getCameraDisplayOrientation());

        mCameraManager.startPreview();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        if (!hasCameraPermission)
            return;
        Log.d(TAG, "surfaceDestroyed");

        mCameraManager.setPreviewCallback(null);
        mCameraManager.stopPreview();
        mCameraManager.closeDriver();
    }

    // Called when camera take a frame
    @Override
    public void onPreviewFrame(byte[] data, Camera camera) {
        if (!mQrDecodingEnabled || decodeFrameTask != null
                && decodeFrameTask.getStatus() == AsyncTask.Status.RUNNING) {
            return;
        }

        decodeFrameTask = new DecodeFrameTask(this, decodeHints);
        decodeFrameTask.execute(data);
    }

    /**
     * Check if this device has a camera
     */

    private boolean checkCameraHardware() {
        if (getContext().getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
            // this device has a camera
            return true;
        } else if (getContext().getPackageManager()
                .hasSystemFeature(PackageManager.FEATURE_CAMERA_FRONT)) {
            // this device has a front camera
            return true;
        } else {
            // this device has any camera
            return getContext().getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY);
        }
    }

    /**
     * Fix for the camera Sensor on some devices (ex.: Nexus 5x)
     */
    @SuppressWarnings("deprecation")
    private int getCameraDisplayOrientation() {
        if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.GINGERBREAD) {
            return 90;
        }

        Camera.CameraInfo info = new Camera.CameraInfo();
        getCameraInfo(mCameraManager.getPreviewCameraId(), info);
        WindowManager windowManager =
                (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        int rotation = windowManager.getDefaultDisplay().getRotation();
        int degrees = 0;
        switch (rotation) {
            case Surface.ROTATION_0:
                degrees = 0;
                break;
            case Surface.ROTATION_90:
                degrees = 90;
                break;
            case Surface.ROTATION_180:
                degrees = 180;
                break;
            case Surface.ROTATION_270:
                degrees = 270;
                break;
            default:
                break;
        }
        //info.orientation  如果是前置 且横屏，则应该为270
        int result;
        if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            result = (info.orientation + degrees) % 360;
            result = (360 - result) % 360;  // compensate the mirror
        } else {  // back-facing
            result = (info.orientation - degrees + 360) % 360;
        }
        return result;
    }

    private static class DecodeFrameTask extends AsyncTask<byte[], Void, Result> {

        private final WeakReference<QRCodeScannerView> viewRef;
        private final WeakReference<Map<DecodeHintType, Object>> hintsRef;
        private final QRToViewPointTransformer qrToViewPointTransformer =
                new QRToViewPointTransformer();

        public DecodeFrameTask(QRCodeScannerView view, Map<DecodeHintType, Object> hints) {
            viewRef = new WeakReference<>(view);
            hintsRef = new WeakReference<>(hints);
        }

        @Override
        protected Result doInBackground(byte[]... params) {
            final QRCodeScannerView view = viewRef.get();
            if (view == null) {
                return null;
            }

            final PlanarYUVLuminanceSource source =
                    view.mCameraManager.buildLuminanceSource(params[0], view.mPreviewWidth,
                            view.mPreviewHeight);

            final HybridBinarizer hybBin = new HybridBinarizer(source);
            final BinaryBitmap bitmap = new BinaryBitmap(hybBin);

            try {
                return view.mQRCodeReader.decode(bitmap, hintsRef.get());
            } catch (ChecksumException e) {
                Log.d(TAG, "ChecksumException", e);
            } catch (NotFoundException e) {
//                Log.d(TAG, "No QR Code found");
            } catch (FormatException e) {
                Log.d(TAG, "FormatException", e);
            } finally {
                view.mQRCodeReader.reset();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Result result) {
            super.onPostExecute(result);

            final QRCodeScannerView view = viewRef.get();

            // Notify we found a QRCode
            if (view != null && result != null && view.mOnQRCodeScannerListener != null) {
                // Transform resultPoints to View coordinates
                final PointF[] transformedPoints =
                        transformToViewCoordinates(view, result.getResultPoints());
                view.mOnQRCodeScannerListener.onDecodeFinish(result.getText(), transformedPoints);
            }
        }

        /**
         * 将结果转换为SurfaceView的坐标（QRCodeScannerView的坐标）
         * <p>
         * This method is needed because coordinates are given in landscape camera coordinates when
         * device is in portrait mode and different coordinates otherwise.
         *
         * @return a new PointF array with transformed points
         */
        private PointF[] transformToViewCoordinates(QRCodeScannerView view, ResultPoint[] resultPoints) {
            int orientationDegrees = view.getCameraDisplayOrientation();
            Orientation orientation =
                    orientationDegrees == 90 || orientationDegrees == 270 ? Orientation.PORTRAIT
                            : Orientation.LANDSCAPE;
            Point viewSize = new Point(view.getWidth(), view.getHeight());
            Point cameraPreviewSize = view.mCameraManager.getPreviewSize();
            boolean isMirrorCamera =
                    view.mCameraManager.getPreviewCameraId() == Camera.CameraInfo.CAMERA_FACING_FRONT;

            return qrToViewPointTransformer.transform(resultPoints, isMirrorCamera, orientation, viewSize,
                    cameraPreviewSize);
        }
    }


    /****************************************************
     *          接口回调（你需要实现的接口）
     ****************************************************/

    /**
     * 二维码解析结果接口
     */
    public interface OnQRCodeScannerListener {
        /**
         *
         * @param scanResult 二维码扫描结果
         * @param points 二维码坐标集合
         */
        void onDecodeFinish(String scanResult, PointF[] points);
    }

    private OnQRCodeScannerListener mOnQRCodeScannerListener;

    /**
     * 添加二维码解析结果监听
     *
     * @param onQRCodeScannerListener
     */
    public void setOnQRCodeReadListener(OnQRCodeScannerListener onQRCodeScannerListener) {
        mOnQRCodeScannerListener = onQRCodeScannerListener;
    }

    /**
     * 相机权限接口
     */
    public interface OnCheckCameraPermissionListener {
        boolean onCheckCameraPermission();
    }

    /**
     * 相机权限监听器
     */
    private OnCheckCameraPermissionListener onCheckCameraPermissionListener;

    /**
     * 添加相机权限监听
     *
     * @param listener
     */
    public void setOnCheckCameraPermissionListener(OnCheckCameraPermissionListener listener) {
        this.onCheckCameraPermissionListener = listener;
    }

    /**
     * 被告知相机权限已授予
     */
    public void grantCameraPermission() {
        if (hasCameraPermission)
            return;
        else
            hasCameraPermission = true;

        if (getHolder().getSurface() != null) {
            surfaceCreated(getHolder());
            surfaceChanged(getHolder(), 0, 0, 0);
        }
    }

}
