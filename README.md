# QrCodeScannerView-Android

## 目录
* [功能简介](#功能简介)
* [依赖和使用](#依赖和使用)
* [效果示例](#效果示例)

## 功能简介
QrCodeScannerView-Android是基于大名鼎鼎的Zxing二维码框架，在@dlazaro66大神开发的QRCodeReaderView上进行再次开发的一个二维码扫描Library，
提供了二维码扫描相关功能，还可以自定义的二维码扫描框、扫描线、自定义UI布局等等。

该项目参考了以下项目：

https://github.com/zxing/zxing

https://github.com/dlazaro66/QRCodeReaderView

再次感谢上述大神。

- [x] 二维码解析
- [x] 前/后置摄像头切换扫描
- [x] 打开闪光灯
- [x] 开启/停止二维码扫描功能
- [x] 自定义扫描界面UI，轻松定制自己的扫描框、扫描背景、扫描线相关属性
- [x] 我将会一直维护这个Library，完善其更多功能。如果有更多需求或者发现BUG，欢迎在Issues中提票！提前感谢！

## 依赖和使用
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
        Log.d("tag", "扫描结果 ： " + text); 
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

## 相关API（详细使用方法请参照Sample中代码）
### 1、QRCodeScannerView（Camera/二维码扫描解析相关）
1.打开二维码扫描开关，默认打开
```
setQRDecodingEnabled(boolean qrDecodingEnabled)
```
2.设置自动聚焦间隔，默认2s
```
setAutofocusInterval(long autofocusIntervalInMs);
```
3.二维码解析结果回调【需要开发者实现】
```
setOnQRCodeReadListener(OnQRCodeScannerListener onQRCodeScannerListener)
```
4.Camera权限监听回调（用于Android 6.0）【需要开发者实现】
```
setOnCheckCameraPermissionListener(OnCheckCameraPermissionListener listener)
```
5.设置Camera默认打开方向【需要开发者实现】
```
setFrontCamera（）//前置摄像头
setBackCamera（） //后置摄像头
```
6.开始/停止扫描
```
startCamera() 建议放在Activity的onResume()方法中
stopCamera()  建议放在Activity的onPause()方法中
```
7.「可选」切换前置/后置摄像头
```
switchCameraFace()
```
### 2、QRCoverView（UI相关）
1.设置扫描框背景颜色
```
setCoverViewOutsideColor(int colorRes) 
```
2.设置扫描框边角颜色
```
ssetCoverViewCornerColor(int colorRes)
```
3.设置扫描框边角宽高
```
 /**
   * 设置扫描边框宽高
   *
   * @param cornerH 扫描边框的长度
   * @param cornerW 扫描边框的厚度
   * @param isDp    若为true则将传入的参数单位视为dp，false 则单位视为px
   */
  setCoverViewCorner(int cornerH, int cornerW, boolean isDp)
```
4. 设置扫描框边角的位置
```
/**
 * 若调用该setCoverViewConnerFace(true)，则扫描边框则会包裹扫描框显示在界面上；
 * 否则默认扫描边框内贴在扫描框内（大多数app默认都是该模式）。(详见效果示例-图5)
 *
 * @param isOutside
 */
setCoverViewConnerFace(boolean isOutside)
```
5.设置扫描框的宽高(dp)
```
setCoverViewScanner(int width, int height)
```
6.获得扫描框的坐标Rect
```
RectF getViewFinderRect()
```
7.「重要」如果有更多需求，建议阅读QRCoverView.java的源码，里面注释很清楚，一般的UI需求简单改几个成员变量的属性就能实现，下为部分源码及注释：
```
private final Paint laserPaint = new Paint();   //扫描线Paint
private boolean showLaser = true;      //是否显示扫描线
private float laserStartH = 0f;        //扫描线初始位置
private int laserChangeBounds = 30;    //渐变线的默认高度，默认30px
private int laserChangeTime = 40;      //UI刷新间隔

private QRHandler handler;

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
//扫描框的Rect
private RectF viewFinderRect;
...
...
...
@Override
protected void onDraw(Canvas canvas) {
    //默认扫描框为180dp
    if (scannerW == 0f || scannerH == 0f) {
        viewFinderRect = new RectF(left, top, left + ScannerDpUtils.dip2px(context, 180), top + ScannerDpUtils.dip2px(context, 180));
    } else {
        viewFinderRect = new RectF(left, top, left + scannerW, top + scannerH);
    }
    //绘制背景色
    drawScanBackground(canvas, viewFinderRect);
    //绘制扫描边框
    drawScanCorner(canvas, viewFinderRect);
    //绘制扫描线
    drawLaserLine(canvas, viewFinderRect);
}

```


## 效果示例

1.解析二维码

![scanResult](https://github.com/ButQingMei/QrCodeScannerView-Android/blob/master/pic/scanResult.gif)

2.设置扫描框背景

![setBg](https://github.com/ButQingMei/QrCodeScannerView-Android/blob/master/pic/setBg.gif)

3.切换前/后置摄像头

![switchCamera](https://github.com/ButQingMei/QrCodeScannerView-Android/blob/master/pic/switchCamera.gif)

4.切换扫描线的隐藏/显示

![switchLaser](https://github.com/ButQingMei/QrCodeScannerView-Android/blob/master/pic/switchLaser.gif)

5.设置扫描框角的位置

![outConner](https://github.com/ButQingMei/QrCodeScannerView-Android/blob/master/pic/outConner.gif)

6.停止扫描

![stopScan.gif](https://github.com/ButQingMei/QrCodeScannerView-Android/blob/master/pic/stopScan.gif)
