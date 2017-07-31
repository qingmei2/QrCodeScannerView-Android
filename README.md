# QrCodeScannerView-Android

[![](https://www.jitpack.io/v/qingmei2/QrCodeScannerView-Android.svg)](https://www.jitpack.io/#qingmei2/QrCodeScannerView-Android)

#### 简单易上手的 Android 扫描二维码控件
[中文文档](https://github.com/qingmei2/QrCodeScannerView-Android/blob/master/README_CN.md)

### If you have a problem (using a problem, or encounter a bug), welcome to provide your issues! Thank you for using QrCodeScannerView-Android!

## Index
* [Introduction](#Introduction)
* [Usage](#Usage)
* [ChangeLog](#ChangeLog)
* [ScanResult](#ScanResult)

## <span id="Introduction">Introduction</span>

QrCodeScannerView-Android is a simple tool that can help you decode Qr Code and create a Qr code by content

base on：

https://github.com/zxing/zxing

https://github.com/dlazaro66/QRCodeReaderView

- [x] QrCode Decode 
- [x] QrCode Encode
- [x] Switch Camera in Scanning
- [x] Open / stop QrCode scanning function
- [x] It's convenient for customing your scan screen UI
- [x] 【coming soon...】decode QrCode picture in Android devices

## <span id="Usage">Usage</span>

### 1. Add code into your Project Build.gradle:
```
allprojects {
  repositories {
    ...
    maven { url 'https://jitpack.io' }
  }
}
```
### 2.Add code into your Module Build.gradle:
```
dependencies {
    compile 'com.github.qingmei2:QrCodeScannerView-Android:1.1.2'
}
```
### 3.Add permissions in AndroidManifest.xml:

```
    <uses-permission android:name="android.permission.CAMERA" />

    <uses-feature android:name="android.hardware.camera" />
    <uses-feature android:name="android.hardware.camera.autofocus" />
```
### 4.Add views in your activity_layout:

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
### 5.init configuration it your activity：
```

mScannerView.setAutofocusInterval(2000L);

mScannerView.setOnQRCodeReadListener(new QRCodeScannerView.OnQRCodeScannerListener() {
    @Override
    public void onDecodeFinish(String text, PointF[] points) {
        Log.d("tag", "result ： " + text); 
    }
});
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
mScannerView.setBackCamera();
```

## API(Please reading the Sample code)
### 1、QRCodeScannerView（Camera/decode）
| Method                                                                    |detail                  |about |
| -------------                                                           | -------------             | -----|
| setQRDecodingEnabled(boolean qrDecodingEnabled)                         | Enable decoding，default true   |  |
| setAutofocusInterval（int times）                                        | Auto focus，default 2s     |   |
| setOnQRCodeReadListener(OnQRCodeScannerListener onQRCodeScannerListener)| Call back of decode result | need implement |
| setFrontCamera()/setBackCamera()                                        | Default face oritation       |need implement |
| setOnCheckCameraPermissionListener(OnCheckCameraPermissionListener listener)|Call back of the Camera permission  | need implement |
| switchCameraFace()                                                      | Switch the Camera           | |
| startCamera()                                                           | Start scan Qrcode       | should use it in activity.onResume() |
| stopCamera()                                                            | Stop scan Qrcode        |should use it in activity.onPause() |

### 2、QRCoverView（UI）
|Method                                                                    |detail                    |about |
| -------------                                                           | -------------             | -----|
| setCoverViewOutsideColor(int colorRes)                                  | Scan rect outside color         |  |
| setCoverViewCornerColor(int colorRes)                                   | Scan corner color           |   |
| setCoverViewCorner(int cornerH, int cornerW, boolean isDp)              | Scan corner color length and width    | |
| setCoverViewConnerFace(boolean isOutside)                               | Scan corner outside of scan Rect  |default false |
| setCoverViewScanner(int width, int height)                              | Scan rect width&height      |  |
| RectF getViewFinderRect()                                               | Get the Scan rect       | |

also you can init UI by this:

```
    <com.qingmei2.library.view.QRCoverView
        android:id="@+id/cover"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        app:cornerColor="#0f0"  
        app:cornerLength="16dp" 
        app:cornerWidth="3dp"   
        app:cornerOutside="false"
        app:laserVisible="false" 
        app:laserColor="#0f0"    
        app:outsideBackgroundColor="#22000000" 
        app:scanRectHeight="250dp"    
        app:scanRectWidth="250dp"     
        />

```

### 3、QRCodeEncoder （V1.1.2 Support）

#### Use it：

```
public QRCodeEncoder(Activity activity)

//use it in activity：
qrCodeEncoder = new QRCodeEncoder(this);  //init

qrCodeEncoder.createQrCode2ImageView(textContent, ivQRCode);
//or
qrCodeEncoder.createQrCode2ImageView(textContent, ivQRCode, R.mipmap.ic_launcher);
```

|Method                                                                    |detail                    |about|
| -------------                                                           | -------------             | -----|
| Bitmap createQrCode(String content, int widthAndHeight);                | createQrCode     |   |
| void createQrCode2ImageView(String content, ImageView imageView);       | create QrCode into ImageView |   |
| also you can add a icon into QrCode Bitmap                                   |                         | |
| Bitmap createQrCode(String content, int width, int iconRes);            | createQrCode with icon     |  |
| void createQrCode2ImageView(String content, ImageView imageView, int iconRes); | create QrCode with icon into ImageView |   |

Override Methods：
#### Bitmap createQrCode():
![createQrcode](https://github.com/qingmei2/QrCodeScannerView-Android/blob/master/pic/createQrcode.png)

#### void createQrCode2ImageView():
![create2ImageView](https://github.com/qingmei2/QrCodeScannerView-Android/blob/master/pic/create2ImageView.png)

Result：
![create_demo_normal](https://github.com/qingmei2/QrCodeScannerView-Android/blob/master/pic/create_demo_normal.png)
![create_demo_icon](https://github.com/qingmei2/QrCodeScannerView-Android/blob/master/pic/create_demo_icon.png)

## ChangeLog
### v1.1.2
#### Add:
* QrCodeEncoder
The assistant class can help you create a QR code bitmap into ImageView.
* QrCoverView
now you can use attributes in your layout.


## ScanResult

![scanResult](https://github.com/ButQingMei/QrCodeScannerView-Android/blob/master/pic/scanResult.gif)

