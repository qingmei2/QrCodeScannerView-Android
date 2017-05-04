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
### 3.直接在你的布局文件xml中使用QRCodeScannerView（二维码扫描业务层View，主要处理Camera扫描二维码相关逻辑）和QRCoverView(二维码扫描UI层View，主要负责对应UI的改变，比如扫描框大小，背景颜色，扫描框边角大小颜色等等):
```
 <FrameLayout
        android:id="@+id/fl_container"
        android:layout_width="match_parent"
        android:layout_height="0dp"
        android:layout_weight="1">
        
        <com.mei_husky.library.view.QRCodeScannerView
            android:id="@+id/scan_view"
            android:layout_width="match_parent"
            android:layout_height="match_parent" />

        <com.mei_husky.library.view.QRCoverView
            android:id="@+id/cover_view"
            android:layout_width="match_parent"
            android:layout_height="match_parent" />
</FrameLayout>
```


