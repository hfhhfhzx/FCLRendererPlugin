// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.jetbrains.kotlin.android) apply false
}

/*
val localSdkPath: String? by project.extra
if (localSdkPath != null) {
    android.sdkDirectory = File(localSdkPath)
}
*/
