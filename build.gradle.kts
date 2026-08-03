// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.jetbrains.kotlin.android) apply false
}

extra.set("appName", "XXX Renderer")
// 获取 git commit 计数
extra["gitCommitCount"] = runGitCommand("rev-list", "--count", "HEAD")?.toIntOrNull() ?: 1
// 最新的以 v 开头的 git tag
extra["gitTag"] = runGitCommand("describe", "--tags", "--match", "v*", "--abbrev=0")?.removePrefix("v") ?: "1.0.0"
extra["gitHash"] = runGitCommand("rev-parse", "--short", "HEAD") ?: "unknown"
extra["gitHashLong"] = runGitCommand("rev-parse", "HEAD") ?: "unknown"
extra["gitBranch"] = runGitCommand("rev-parse", "--abbrev-ref", "HEAD") ?: "unknown"

fun runGitCommand(vararg args: String): String? = runCatching {
    ProcessBuilder(listOf("git") + args)
        .directory(projectDir)
        .redirectErrorStream(true)
        .start()
        .let { process ->
            val output = process.inputStream.bufferedReader().readText().trim()
            if (process.waitFor() == 0 && output.isNotBlank()) output else null
        }
}.getOrNull()
