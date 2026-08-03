import com.android.build.api.dsl.ApplicationBuildType
import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
}

val appName = rootProject.extra.get("appName") as String
val gitCommitCount = rootProject.extra["gitCommitCount"] as Int
val gitTag = rootProject.extra["gitTag"] as String
val gitHash = rootProject.extra["gitHash"] as String

val properties: Properties? = loadPropertiesFromFile("signing.properties")
    fun getString(propertyName: String, environmentName: String, prompt: String): String =
        properties?.getProperty(propertyName)
            ?: System.getenv(environmentName)
            ?: System.console()?.readLine("\n$prompt: ").orEmpty()

fun loadPropertiesFromFile(fileName: String): Properties? =
    rootProject.file(fileName).takeIf { it.exists() }?.let { file ->
        Properties().apply { load(file.inputStream()) }
    }

android {
    namespace = "com.mio.plugin.renderer"
    compileSdk = 37
    compileSdkMinor = 0
    buildToolsVersion = "37.0.0"

    defaultConfig {
        applicationId = "com.mio.plugin.renderer"
        minSdk = 28
        targetSdk = 37
        versionCode = gitCommitCount
        versionName = gitTag
        
        multiDexEnabled = false
    }
    
    signingConfigs {
        // 签名配置
        // 支持：从 signing.properties 中读取；从环境变量中读取；手动输入。优先级由高到低
        // 如果以上三种都没有，就使用默认 debug 签名
        create("hasProperties") {
            if (properties != null) {
                storeFile = file(getString("storeFile", "STORE_FILE", "Store file"))
                storePassword = getString("storePassword", "STORE_PASSWORD", "Store password")
                keyAlias = getString("keyAlias", "KEY_ALIAS", "Key alias")
                keyPassword = getString("keyPassword", "KEY_PASSWORD", "Key password")
            }
            // 分别为是否启用 V1, V2, V3, V4 签名
            enableV1Signing = false
            enableV2Signing = true
            enableV3Signing = false
            enableV4Signing = false
        }
    }
    
    packaging {
        resources {
            excludes += "kotlin/**"
        }
        jniLibs {
            useLegacyPackaging = true
        }
    }
    
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }
    
    buildFeatures {
        resValues = true
    }

    buildTypes {
        val configSigning: ApplicationBuildType.() -> Unit = {
            val signingConfigName = if (properties != null) "hasProperties" else "debug"
            signingConfig = signingConfigs.findByName(signingConfigName)
        }
        
        release {
            configSigning()
            // R8 设置（新 DSL）
            optimization {
                enable = true //启用
                keepRules {
                    includeDefault = true //启用默认的 proguard-android-optimize.txt。此选项默认启用
                }
            }
        }
        
        debug {
            optimization {
                enable = false
            }
        }
        
        configureEach {
            // 应用名
            //app name
            resValue("string","app_name","$appName")
            // 包名后缀
            // package name Suffix
            applicationIdSuffix = ".xxx"

            // 渲染器在启动器内显示的名称
            // The name displayed by the renderer in the launcher
            manifestPlaceholders["des"] = ""
            // 渲染器的具体定义 格式为 名称:渲染器库名:EGL库名 例如 LTW:libltw.so:libltw.so
            // The specific definition format of a renderer is ${name}:${renderer library name}:${EGL library name}, for example:   LTW:libltw.so:libltw.so
            manifestPlaceholders["renderer"] = ""

            // 特殊Env
            // Special Env
            // DLOPEN=libxxx.so 用于加载额外库文件
            // DLOPEN=libxxx.so used to load external library
            // 如果有多个库,可以使用","隔开,例如  DLOPEN=libxxx.so,libyyy.so
            // If there are multiple libraries, you can use "," to separate them, for example  DLOPEN=libxxx.so,libyyy.so
            manifestPlaceholders["boatEnv"] = mutableMapOf<String,String>().apply {

            }.run {
                var env = ""
                forEach { (key, value) ->
                    env += "$key=$value:"
                }
                env.dropLast(1)
            }

            manifestPlaceholders["pojavEnv"] = mutableMapOf<String,String>().apply {

            }.run {
                var env = ""
                forEach { (key, value) ->
                    env += "$key=$value:"
                }
                env.dropLast(1)
            }

            // MC 版本配置
            // 为空则不限制
            // No restriction if empty
            // 最小支持的MC版本
            // The minimum supported MC version
            manifestPlaceholders["minMCVer"] = ""
            // 最大支持的MC版本
            // The maximum supported MC version
            manifestPlaceholders["maxMCVer"] = ""
        }
    }
}

kotlin {
    compilerOptions {
        jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_21)
    }
}

base {
    archivesName = "${appName}-v${gitTag}-${gitCommitCount}-${gitHash}"
}
