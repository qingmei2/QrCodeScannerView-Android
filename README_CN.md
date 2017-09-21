# QrCodeScannerView-Android
### 简单易上手的 Android 扫描二维码控件 @Alpha
# 中文文档
[![](https://www.jitpack.io/v/qingmei2/QrCodeScannerView-Android.svg)](https://www.jitpack.io/#qingmei2/QrCodeScannerView-Android)

## 如果您遇到了什么问题（使用问题，或者遇到了bug），欢迎Issues！感谢您使用QrCodeScannerView-Android！

## 目录
* [功能简介](#功能简介)
* [依赖和使用](#依赖和使用)
* [常见问题](#常见问题)
* [改动日志](#改动日志)
* [效果示例](#效果示例)

## 功能简介

QrCodeScannerView-Android是基于大名鼎鼎的Zxing二维码框架，在@dlazaro66大神开发的QRCodeReaderView上进行再次开发的一个二维码扫描Library，
提供了二维码扫描相关功能，还可以自定义的二维码扫描框、扫描线、自定义UI布局等等。

该项目参考了以下项目：

https://github.com/zxing/zxing

https://github.com/dlazaro66/QRCodeReaderView

再次感谢上述大神。

- [x] 二维码解析（Decode）
- [x] 二维码生成 (Encode)
- [x] 前/后置摄像头切换
- [x] 开启/停止二维码扫描功能
- [x] 自定义扫描界面UI，轻松定制自己的扫描框、扫描背景、扫描线相关属性
- [x] 获得扫描框的坐标Rect(判断所扫二维码是否在扫描框内等)
- [x] 【1.1.3版本重构创作中...】生成二维码图片并保存、相册解析二维码图片等

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
    compile 'com.github.qingmei2:QrCodeScannerView-Android:1.1.2'
}
```
### 3.在你的AndroidManifest.xml中添加权限:

```
    <uses-permission android:name="android.permission.CAMERA" />

    <uses-feature android:name="android.hardware.camera" />
    <uses-feature android:name="android.hardware.camera.autofocus" />
```
### 4.直接在你的布局文件xml中使用
QRCodeScannerView（二维码扫描业务层View，主要处理Camera扫描二维码相关逻辑）

QRCoverView(二维码扫描UI层View，主要负责对应UI的改变，比如扫描框大小，背景颜色，扫描框边角大小颜色等等):
```
<?xml version="1.0" encoding="utf-8"?>
<FrameLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    tools:context="com.qingmei2.qrcodedemos.MainActivity">

    <com.qingmei2.library.view.QRCodeScannerView
        android:id="@+id/scanner"
        android:layout_width="match_parent"
        android:layout_height="match_parent" />

    <com.qingmei2.library.view.QRCoverView
        android:id="@+id/cover"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        app:cornerColor="#0f0"
        app:laserVisible="false"
        app:outsideBackgroundColor="#22000000"
        app:scanRectHeight="250dp"
        app:scanRectWidth="250dp" />

</FrameLayout>

```
### 5.在对应的Activity中的onCreate()中初始化配置：
```
//自动聚焦间隔2s
mScannerView.setAutofocusInterval(2000L);
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
|方法名                                                                    |方法说明                    |补充 |
| -------------                                                           | -------------             | -----|
| setQRDecodingEnabled(boolean qrDecodingEnabled)                         | 打开二维码扫描开关，默认打开   |  |
| setAutofocusInterval（int times）                                        | 设置自动聚焦间隔，默认2s     |   |
| setOnQRCodeReadListener(OnQRCodeScannerListener onQRCodeScannerListener)| 二维码解析结果回调            | 需要开发者实现 |
| setFrontCamera()/setBackCamera()                                        | 设置Camera默认打开方向        |需要开发者实现 |
| setOnCheckCameraPermissionListener(OnCheckCameraPermissionListener listener)| Camera权限监听回调       | 需要开发者实现 |
| switchCameraFace()                                                      | 切换前置/后置摄像头           |扫描过程中切换摄像头 |
| startCamera()                                                           | 开始扫描       | 建议放在Activity的onResume()方法中 |
| stopCamera()                                                            | 停止扫描       |建议放在Activity的onPause()方法中 |

### 2、QRCoverView（UI相关）
|方法名                                                                    |方法说明                    |补充 |
| -------------                                                           | -------------             | -----|
| setCoverViewOutsideColor(int colorRes)                                 | 设置扫描框外部背景颜色         |  |
| setCoverViewCornerColor(int colorRes)                                   | 设置扫描框颜色              |   |
| setCoverViewCorner(int cornerH, int cornerW, boolean isDp)              | 设置扫描框边角长度H和厚度W        | |
| setCoverViewConnerFace(boolean isOutside)                               | 设置扫描框边角的位置是否在外部  |默认为扫描框内部 |
| setCoverViewScanner(int width, int height)                              | 设置扫描框的宽高(dp)       |  |
| RectF getViewFinderRect()                                               |获得扫描框的坐标Rect       |可判断二维码是否在扫描框内等 |

当然您也可以直接在xml中声明扫描界面的UI属性:

```
    <com.qingmei2.library.view.QRCoverView
        android:id="@+id/cover"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        app:cornerColor="#0f0"  //扫描框边角颜色
        app:cornerLength="16dp" //扫描框边角长度
        app:cornerWidth="3dp"   //扫描框边角厚度
        app:cornerOutside="false"//扫描框边角在扫描框内还是外
        app:laserVisible="false" //扫描线是否可见
        app:laserColor="#0f0"    //扫描线颜色
        app:outsideBackgroundColor="#22000000" //扫描框外部背景色
        app:scanRectHeight="250dp"    //扫描框宽度
        app:scanRectWidth="250dp"     //扫描框高度
        />

```

### 3、QRCodeEncoder 二维码生成相关（1.1.2版本新增）

在1.1.2版本中，笔者为开发者封装了简单的二维码生成功能
#### 使用方式
直接new一个就可以了：

```
//构造方法：
public QRCodeEncoder(Activity activity)
//activity中使用：
qrCodeEncoder = new QRCodeEncoder(this);  //初始化

qrCodeEncoder.createQrCode2ImageView(textContent, ivQRCode);//生成二维码
//或者
qrCodeEncoder.createQrCode2ImageView(textContent, ivQRCode, R.mipmap.ic_launcher);//生成带Icon的二维码
```

|方法名                                                                    |方法说明                    |补充 |
| -------------                                                           | -------------             | -----|
| Bitmap createQrCode(String content, int widthAndHeight);                | 生成二维码并以Bitmap返回     |   |
| void createQrCode2ImageView(String content, ImageView imageView);       | 在指定的ImageView中生成二维码 |   |
| 当然，您也可以添加简单的Icon在二维码中：                                       |                         | |
| Bitmap createQrCode(String content, int width, int iconRes);            | 生成带Icon的二维码并以Bitmap返回     |  |
| void createQrCode2ImageView(String content, ImageView imageView, int iconRes); | 在指定的ImageView中生成带Icon的二维码 |   |

当然不止这么简单，我们提供了各种各样的重载方法：
#### Bitmap createQrCode():
![createQrcode](https://github.com/qingmei2/QrCodeScannerView-Android/blob/master/pic/createQrcode.png)

#### void createQrCode2ImageView():
![create2ImageView](https://github.com/qingmei2/QrCodeScannerView-Android/blob/master/pic/create2ImageView.png)

效果实例：
![create_demo_normal](https://github.com/qingmei2/QrCodeScannerView-Android/blob/master/pic/create_demo_normal.png)
![create_demo_icon](https://github.com/qingmei2/QrCodeScannerView-Android/blob/master/pic/create_demo_icon.png)

#### 「重要」如果有更多需求，建议阅读QRCoverView.java的源码，或者提Issues，我会在第一时间回复

## 常见问题：

## 1、gradle编译冲突怎么回事？
QrCodeScannerView 本身是基于Zxing 3.2.1实现的，所以在Compile QrCodeScannerView的时候会自动Compile Zxing 3.2.1库，如果您的项目中用了其他二维码扫描库引起冲突（比如其他版本的Zxing），请放心将其移除。

## 2、QrCodeScannerView不支持相册识别读取二维码吗？不支持本地生成二维码并存储吗？
关于「相册识别解析二维码」是笔者准备马上添加的功能，关于「本地生成二维码并存储」，QrCodeEncoder已经能够生成Bitmap返回（已经进行简单压缩处理），如果有需求，可以google或者百度，封装简单的工具类，将Bitmap存储本地即可。

## 3、QrCodeScannerView不支持识别条形码吗？
正如这个library的名字所述，QrCodeScannerView是一个简单的二维码扫描控件，暂时还没有准备添加条形码识别的支持。

## 已知BUG：
暂无。

## 改动日志
### v1.1.2
#### Add:
* QrCodeEncoder
The assistant class can help you create a QR code bitmap into ImageView.
* QrCoverView
now you can use attributes in your layout.


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

### 如果这个库对你的开发有所帮助，望请不吝star，如果遇到Bug或者建议，欢迎issue，我会第一时间进行回复，提前感谢，鞠躬！！！
