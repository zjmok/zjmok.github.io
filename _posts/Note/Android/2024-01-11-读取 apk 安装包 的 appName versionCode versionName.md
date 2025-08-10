---
layout: post
tags: Android
---

# 读取 apk 安装包 的 appName versionCode versionName

读取 apk 安装包的应用名称（appName）、版本代码（versionCode）和版本名称（versionName）

## Java 直接读取（不太推荐）

可以通过解析 apk 文件的元数据来实现。

<details markdown='1'><summary>展开/收起</summary>

创建一个Java类，比如 `ApkInfoReader.java`：

```
import java.io.File;
import java.io.FileInputStream;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class ApkInfoReader {
    public static void main(String[] args) {
        String apkPath = "path/to/your/app.apk"; // 替换成 apk 文件路径
        readApkInfo(apkPath);
    }

    public static void readApkInfo(String apkPath) {
        try {
            JarFile jarFile = new JarFile(apkPath);
            ZipInputStream zipStream = new ZipInputStream(new FileInputStream(apkPath));

            JarEntry entry;
            while ((entry = (JarEntry) zipStream.getNextEntry()) != null) {
                String entryName = entry.getName();

                if (entryName.endsWith("AndroidManifest.xml")) {
                    // 读取 AndroidManifest.xml 文件来获取应用信息
                    // 这里可以使用一些 XML 解析的库来解析 Manifest 文件，比如 SAXParser 或 DOMParser
                    // 在这里简化，直接打印出文件内容
                    System.out.println("Manifest File Content:\n");

                    byte[] buffer = new byte[1024];
                    int bytesRead;
                    while ((bytesRead = zipStream.read(buffer)) != -1) {
                        System.out.write(buffer, 0, bytesRead);
                    }
                    System.out.println("\n");

                    break;
                }
            }

            // 关闭输入流
            zipStream.close();

            // 获取版本信息
            String versionCode = jarFile.getManifest().getMainAttributes().getValue("Manifest-Version");
            String versionName = jarFile.getManifest().getMainAttributes().getValue("Bundle-Version");

            // 打印应用信息
            System.out.println("App Name: " + jarFile.getName());
            System.out.println("Version Code: " + versionCode);
            System.out.println("Version Name: " + versionName);

            jarFile.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

```

这里使用了Java的 `JarFile` 和 `ZipInputStream` 来读取 apk 文件的内容，并尝试解析 AndroidManifest.xml 文件来获取应用信息。

</details>

<p/>

## 使用 aapt 读取（推荐）

使用 Android SDK 的 aapt/aapt2

脚本

```
#!/bin/bash

echo "------"
echo "请检查 ANDROID_HOME 及 AAPT2 配置是否正确"
echo "------"
echo "ANDROID_HOME: ${ANDROID_HOME}"
AAPT2="${ANDROID_HOME}/build-tools/34.0.0/aapt2"
echo "AAPT2: ${AAPT2}"
echo "------"
$AAPT2 version
echo "------"

# 检查aapt2命令是否执行成功
if [ $? -ne 0 ]; then
  echo "获取应用信息失败，请检查APK文件路径是否正确。"
  continue
fi
	
while true; do

  read -p "请输入APK文件路径（路径不含空格）: " APK_PATH

  # if [ "$APK_PATH" = "" ]
  if [ -z "$APK_PATH" ]; then # 检查变量是否为空的标准写法
    break

  else

    # 使用aapt2命令来获取应用信息
    appInfo=$(
      $AAPT2 dump badging "$APK_PATH" |
        grep -E "application-label:|application-label-zh:|package: name|versionCode|versionName|compileSdkVersion|platformBuildVersionCode"
    )

    # 检查aapt2命令是否执行成功
    if [ $? -ne 0 ]; then
      echo "获取应用信息失败，请检查APK文件路径是否正确。"
      continue
    fi

    # 提取应用信息并赋值给变量
    appName=$(echo "$appInfo" | grep "application-label:" | awk -F"'" '{print $2}')
    appNameZh=$(echo "$appInfo" | grep "application-label-zh:" | awk -F"'" '{print $2}')
    packageName=$(echo "$appInfo" | grep "package: name" | awk -F"'" '{print $2}')
    versionCode=$(echo "$appInfo" | grep "versionCode" | awk -F"'" '{print $4}')
    versionName=$(echo "$appInfo" | grep "versionName" | awk -F"'" '{print $6}')
    compileSdk=$(echo "$appInfo" | grep "compileSdkVersion" | awk -F"'" '{print $12}')
    targetSdk=$(echo "$appInfo" | grep "platformBuildVersionCode" | awk -F"'" '{print $10}')

    # 打印应用信息
    echo "appName: $appName"
    # echo "appNameZh: $appNameZh"
    echo "packageName: $packageName"
    echo "versionCode: $versionCode"
    echo "versionName: $versionName"
    echo "compileSdk: $compileSdk"
    echo "targetSdk: $targetSdk"

  fi

done

read -p "请按任意键继续. . ." FXCK
```

## 使用 apktool 反编译工具读取（推荐）

工具下载

<https://ibotpeaches.github.io/Apktool/>

```
apktool d path/to/your/app.apk -o outputFolder

cd outputFolder

cat AndroidManifest.xml
// or
more AndroidManifest.xml
```

我写的反编译相关文章及资料

<https://github.com/cymok/apk-reverse>

## 使用 QuickLook + apk 插件（推荐）

QuickLook

releases <https://github.com/QL-Win/QuickLook/releases>

plugins <https://github.com/QL-Win/QuickLook/wiki/Available-Plugins>

1. 先安装 QuickLook 程序，这个软件很强大，插件装好后比 MacOS 可预览的格式还多

2. 在插件列表里下载 apk 插件，这里插件支持很多格式

插件安装：空格键预览插件文件，然后点击安装，最后重启 QuickLook 即可预览相应格式的文件
