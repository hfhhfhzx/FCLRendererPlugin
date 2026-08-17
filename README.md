<div align="center">
<h1>FCLRendererPlugin</h1>
<img alt="Android" src="https://img.shields.io/badge/Android-3DDC84?style=flat&logo=android&logoColor=white">
<img alt="Kotlin" src="https://img.shields.io/badge/Kotlin-7F52FF?style=flat&logo=kotlin&logoColor=white">
<p><a href="https://github.com/FCL-Team/FoldCraftLauncher">FCL</a> 渲染器插件的示例仓库</p>
<p>Forked from <a href="https://github.com/ShirosakiMio/FCLRendererPlugin">ShirosakiMio/FCLRendererPlugin</a></p>
</div>

---

## 📖 项目背景

FCL (FoldCraftLauncher) 是一款功能强大的 Android 端 Minecraft 启动器，支持模组加载、版本隔离、渲染器切换等高级特性。其插件化架构允许开发者通过独立的渲染器插件来扩展 FCL 的渲染器，实现不同的 OpenGL ES 渲染后端

FCLRendererPlugin 正是这样一个插件项目模板，允许你快速生成一个渲染器插件

## 🛠️ 构建

本项目使用 Gradle 进行构建。要编译 APK，请执行以下步骤：

- 克隆仓库:
   ```bash
   git clone https://github.com/hfhhfhzx/FCLRendererPlugin.git
   cd FCLRendererPlugin
   ```

- 配置签名 (可选):
   
   重命名 `signing.properties.example` 为 `signing.properties` 并填入你的签名信息，然后复制你的签名文件为 `keystore`
   
   如果不需要签名，可以跳过此步

- 配置:
   - 打开 `build.gradle.kts`，根据注释配置 变量 `appName`
   - 替换 `app/src/main/jniLibs/arm64-v8a/libxx.so` 为你需要的 so 文件
   - 打开 `app/build.gradle.kts`，根据注释进行配置
   - 配置 `local.properties` (需要你自己创建)

- 修改代码 (可选):
   
   如果你对项目的部分代码不满意，可以修改它们

- 执行构建:
   ```bash
   ./gradlew assembleRelease
   ```
   构建好的 APK 文件将位于 app/build/outputs/apk/release/ 目录下

## 🚀 Android 本地构建指南

> [!WARNING]
> 在 Android 设备上构建为非主流
>
> 我无法像您保证方法是否适用
>
> 推荐于 Windows / Linux 设备上构建

- 安装 Termux
   - 于 [GitHub](https://github.com/termux/termux-app) 上下载并安装 Termux
   - 切换源
     ```bash
     sed -i 's@^\(deb.*stable main\)$@#\1\ndeb https://mirrors.tuna.tsinghua.edu.cn/termux/termux-packages-24 stable main@' $PREFIX/etc/apt/sources.list && pkg update
     ```
   - 打开 Termux，安装必要依赖
      ```bash
      pkg install git openjdk-21 tar wget -y
      ```
- 安装 `Android SDK` 和 `Android NDK`
   - 参照以下步骤进行安装
   ```bash
     mkdir -p android && cd android
     
     # 要下载的文件来自 https://github.com/HomuHomu833/android-ndk-custom 和 https://github.com/HomuHomu833/android-sdk-custom
     
     wget https://github.com/HomuHomu833/android-ndk-custom/releases/download/r30/android-ndk-r30-beta2-aarch64-linux-android.tar.xz
     wget https://github.com/HomuHomu833/android-sdk-custom/releases/download/37.0.0/android-sdk-aarch64-linux-android.tar.xz
     
     # 解压
     for f in android-*.tar.xz ; do tar -xf "$f" ; done
     # 删除残留
     rm -rf android-*.tar.xz
     # 将下载的 cmdline-tools 解压内容嵌套进 latest 子目录，以满足路径要求
     cd android-sdk/cmdline-tools && mkdir latest && cd latest && mv ../* . 2>/dev/null && cd $HOME
     
     # 配置
     echo 'export ANDROID_HOME=$HOME/android' >> ~/.bashrc && echo 'export PATH=$ANDROID_HOME/android-sdk/cmdline-tools/latest/bin:$PATH' >> ~/.bashrc
     # 请重启 Termux
     
     # 接受 Android SDK 的许可
     yes | sdkmanager --licenses
   ```

- 按照上一个篇章的步骤进行操作

- 解决 `aapt2` 问题 (只有当你遇到该问题时才这样做)
   ```bash
     # 指定 aapt2 路径并忽略警告
     echo 'android.aapt2FromMavenOverride=/data/data/com.termux/files/usr/bin/aapt2' >> ~/.gradle/gradle.properties && echo 'android.sync.suppressAgpWarnings=UNSUPPORTED_PROJECT_OPTION_USE' >> ~/.gradle/gradle.properties
   ```

## 🤝 贡献

欢迎提交 Issue 或 Pull Request 来帮助改进这个项目

## 🙏 鸣谢

- [FCL-Team/FoldCraftLauncher](https://github.com/FCL-Team/FoldCraftLauncher) - 一切的开始

- [ShirosakiMio/FCLRendererPlugin](https://github.com/ShirosakiMio/FCLRendererPlugin) - 上游

---

⭐ 如果这个项目对你有帮助，请给个 Star 支持一下！
