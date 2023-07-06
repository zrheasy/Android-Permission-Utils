# Android-Permission-Utils
基于Activity Results Api封装的轻量级Android动态权限申请组件。

[下载地址](https://github.com/zrheasy/Android-Permission-Utils/releases/download/v1.0.0/permission-utils-v1.0.aar)

### 使用步骤
#### 1. 将aar添加到项目依赖中。
```groovy
dependencies {
    implementation fileTree(dir: 'libs', include: ['*.jar','*.aar'])
}
```

#### 2. 代码调用申请权限。
```kotlin
PermissionUtils.requestPermissions(this, arrayOf(permission)) { _, granted ->
    if (granted){
        // 已授权
    }else{
        // 未授权
    }
}
```
