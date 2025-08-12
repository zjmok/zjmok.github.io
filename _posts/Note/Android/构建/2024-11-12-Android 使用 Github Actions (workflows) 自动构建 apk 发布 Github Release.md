---
layout: post
tags: Dev Android
---

# Android 使用 Github Actions 自动构建 apk 发布 Github Release

GitHub Actions 是 GitHub 提供的 CI/CD（持续集成和持续部署）工具，
可以在每次推送代码时自动编译 APK，并在构建完成后自动发布到 GitHub Release

## 脚本和说明

在仓库根目录创建 `.github/workflows` 目录，然后创建一个 YAML 配置文件，命名随意

build_release.yaml

```yaml
name: Build APK and Release

# on 监听 push 的 v 开头的 tag
on:
  push:
#    branches:
#      - master
    tags:
      - v*

jobs:
  build:

    runs-on: ubuntu-latest

    steps:
      - uses: actions/checkout@v4 # 拉取代码
      - name: Set up JDK 17
        uses: actions/setup-java@v4 # 设置 JDK
        with:
          java-version: '17'
          distribution: 'temurin'
          cache: gradle

#      - name: Set up Android SDK
#        uses: android-actions/setup-android@v2
#        with:
#          sdk-version: '35.0.0'  # 设置 Android SDK
#
      - name: Grant execute permission for gradlew
        run: chmod +x gradlew
      - name: Build with Gradle
        run: ./gradlew assembleDebug # 编译

      - name: Upload APK to GitHub Release
        uses: softprops/action-gh-release@v2 # 上传 apk 到 Github Release
        with:
          files: app/build/outputs/apk/debug/*.apk # 构建命令执行后的输出文件的对应路径
        env:
          GITHUB_TOKEN: ${{ secrets.GITHUB_TOKEN }} # 授权 GitHub Actions 使用 GitHub API 上传文件
```

创建 `.github/workflows/build_release.yaml` 后，推送到 github 仓库，之后推送 v 开头的 tag 即可触发脚本，自动构建和发布

- `./gradlew assembleDebug` 和后面 apk 路径要对应，可以在本地执行编译查看对应的路径，`./gradlew assembleRelease` 的话还要配置好 ks

- 必须先打开仓库的 Workflow 读写权限：Settings --> Actioons --> General --> Workflow permissions --> **Read and write permissions**

---

## 可以上传 keystore 到 secrets 然后使用上传的 keystore

1. 将 jks 编码为 base64 格式，得到 base64 字符串
2. 打开项目 Settings --> Secrets and variables

添加机密（Secrets）

添加机密到 环境机密 或 仓库机密

Secret Name			| Value（示例）
---					| ---
KEYSTORE_BASE64		| Base64编码后的Keystore内容
KEYSTORE_PASSWORD	| your_keystore_password
KEY_ALIAS			| your_key_alias
KEY_PASSWORD		| your_key_password

区别是：
- 环境机密，有更多限制，可以限制分支或审批等。使用时要在 workflow 指定环境。gradle 不能直接读取，要在 workflow 传递给 gradle
- 仓库机密，仓库全部分支有效，不需要声明环境，gradle 可以在环境变量直接读取

3. 使用 环境机密 的完整脚本

build_release.yaml
```yaml
name: Build APK and Release

# on 监听 push 的 v 开头的 tag
on:
  push:
#    branches:
#      - master
    tags:
      - v*

jobs:
  build:

    runs-on: ubuntu-latest

    environment: zjmok.jks # 指定环境，对应仓库 Settings Environments 中的 environment 名称

    steps:
      - uses: actions/checkout@v4 # 拉取代码
      - name: Set up JDK 17
        uses: actions/setup-java@v4 # 设置 JDK
        with:
          java-version: '17'
          distribution: 'temurin'
          cache: gradle

#      - name: Set up Android SDK
#        uses: android-actions/setup-android@v2
#        with:
#          sdk-version: '35.0.0'  # 设置 Android SDK
#
      # https://docs.github.com/en/actions/how-tos/write-workflows/choose-what-workflows-do/use-secrets#storing-base64-binary-blobs-as-secrets
      # 解码 Base64 签名
      - name: Decode Keystore
        # 输出路径对应 app/build.gradle 脚本中的 storeFile 路径
        run: |
          mkdir -p app/keystore
          echo "${{ secrets.KEYSTORE_BASE64 }}" | base64 -d > app/keystore/release.jks

      - name: Grant execute permission for gradlew
        run: chmod +x gradlew
      - name: Build with Gradle
        run: ./gradlew assembleRelease # 编译
        env:
          # 将 Environment Secrets 注入到 Gradle 可读取的环境变量
          KEYSTORE_PASSWORD: ${{ secrets.KEYSTORE_PASSWORD }}
          KEY_ALIAS: ${{ secrets.KEY_ALIAS }}
          KEY_PASSWORD: ${{ secrets.KEY_PASSWORD }}

      - name: Upload APK to GitHub Release
        uses: softprops/action-gh-release@v2 # 上传 apk 到 Github Release
        with:
          files: app/build/outputs/apk/release/*.apk # 构建命令执行后的输出文件的对应路径
        env:
          GITHUB_TOKEN: ${{ secrets.GITHUB_TOKEN }} # 授权 GitHub Actions 使用 GitHub API 上传文件
```

build.gradle
```groovy
android {
    signingConfigs {
        // 使用 workflow 解码生成的 keystore 文件
        release {
            storeFile file("keystore/release.jks") // 解码文件对应此路径
            storePassword System.getenv("KEYSTORE_PASSWORD")
            keyAlias System.getenv("KEY_ALIAS")
            keyPassword System.getenv("KEY_PASSWORD")
        }
    }
}
```