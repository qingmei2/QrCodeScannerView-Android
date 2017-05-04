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

