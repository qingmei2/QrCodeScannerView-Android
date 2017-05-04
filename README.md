# QrCodeScannerView-Android

该项目参考了以下项目：
https://github.com/dlazaro66/QRCodeReaderView
在此特别感谢上述作者，喜欢原作的可以去使用原项目。同时欢迎大家下载体验本控件，如果使用过程中遇到什么问题，欢迎反馈。

## 一、依赖&用法：
### 1.添加到你的 Project  Build.gradle:
```
allprojects {
  repositories {
    ...
    maven { url 'https://jitpack.io' }
  }
}
```
### 2.添加到你的 Module  Build.gradle:
```
dependencies {
    compile 'com.github.ButQingMei:QrCodeScannerView-Android:1.0'
    compile 'com.google.zxing:core:3.2.1'
}
```
### 3.直接在你的布局文件xml中使用
QRCodeScannerView（二维码扫描业务层View，主要处理Camera扫描二维码相关逻辑）

QRCoverView(二维码扫描UI层View，主要负责对应UI的改变，比如扫描框大小，背景颜色，扫描框边角大小颜色等等):
```
 <FrameLayout
        android:id="@+id/fl_container"
        android:layout_width="match_parent"
        android:layout_height="0dp"
        android:layout_weight="1">
        
        <com.mei_husky.library.view.QRCodeScannerView
            android:id="@+id/scanner_view"
            android:layout_width="match_parent"
            android:layout_height="match_parent" />

        <com.mei_husky.library.view.QRCoverView
            android:id="@+id/cover_view"
            android:layout_width="match_parent"
            android:layout_height="match_parent" />
            
</FrameLayout>
```
### 4.在对应的Activity中的onCreate()中初始化配置：
```
//自动聚焦间隔2s
mScannerView.setAutofocusInterval(2000L);
//闪光灯
mScannerView.setTorchEnabled(true);
//扫描结果监听处理
mScannerView.setOnQRCodeReadListener(new QRCodeScannerView.OnQRCodeScannerListener() {
    @Override
    public void onDecodeFinish(String text, PointF[] points) {
        Log.d("tag", "扫描结果 ： " + text);   //扫描到的内容
        
        //接下来是处理二维码是否在扫描框中的逻辑
        RectF finderRect = mCoverView.getViewFinderRect();
        Log.d("tag", "points.length = " + points.length);
        boolean isContain = true;
        //依次判断扫描结果的每个point是否都在扫描框内
        for (int i = 0, length = points.length; i < length; i++) {
            if (!finderRect.contains(points[i].x, points[i].y)) {
                isContain = false;  //只要有一个不在，说明二维码不完全在扫描框中
                break;
            }
        }
        if (isContain) {
            Toast.makeText(MainActivity.this, "扫描成功！Result = " + text, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(MainActivity.this, "扫描失败！请将二维码图片摆放在正确的扫描区域中...", Toast.LENGTH_SHORT).show();
        }
    }
});
//相机权限监听
mScannerView.setOnCheckCameraPermissionListener(new QRCodeScannerView.OnCheckCameraPermissionListener() {
    @Override
    public boolean onCheckCameraPermission() {
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.CAMERA}, PERMISSION_REQUEST_CAMERA);
            return false;
        }
    }
});
//开启后置摄像头
mScannerView.setBackCamera();
```


