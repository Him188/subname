#!/usr/bin/env kotlin

import java.io.File

val workDir: File = File(".")

val videoExtensions = listOf("mkv", "mp4", "avi")
val subtitleExtensions = listOf("srt", "ass")

val videoFiles = workDir.listFiles().orEmpty().filter { it.extension.lowercase() in videoExtensions }.toList()
val subtitleFiles = workDir.listFiles().orEmpty().filter { it.extension.lowercase() in subtitleExtensions }.toList()

fun findCommonPrefix(strings: List<String>): String {
    if (strings.isEmpty()) return ""
    var prefix = strings[0]
    for (str in strings) {
        while (!str.startsWith(prefix)) {
            prefix = prefix.substring(0, prefix.length - 1)
        }
    }
    return prefix
}

fun findCommonSuffix(strings: List<String>): String {
    if (strings.isEmpty()) return ""
    var suffix = strings[0]
    for (str in strings) {
        while (!str.endsWith(suffix)) {
            suffix = suffix.substring(1)
        }
    }
    return suffix
}

data class EpisodeDetail(
    val original: File,
    val prefix: String,
    val episode: String,
    val suffix: String,
)

val File.extensionWithLanguage: String
    get() {
        val name = this.nameWithoutExtension
        val index = name.lastIndexOf(".")
        return if (index != -1 && name.lastIndex - index <= 4) {
            name.substring(index + 1) + "." + this.extension
        } else {
            this.extension
        }
    }

val File.nameWithoutExtensionAndLanguage: String
    get() {
        val name = this.nameWithoutExtension
        val index = name.lastIndexOf(".")
        return if (index != -1 && name.lastIndex - index <= 4) {
            name.substring(0, index)
        } else {
            name
        }
    }

fun extractEpisodeDetails(files: List<File>): List<EpisodeDetail> {
    val prefix = findCommonPrefix(files.map { it.nameWithoutExtensionAndLanguage })
    val suffix = findCommonSuffix(files.map { it.nameWithoutExtensionAndLanguage })

    return files.map { file ->
        val episode = file.nameWithoutExtensionAndLanguage.removePrefix(prefix).removeSuffix(suffix)
        EpisodeDetail(original = file, prefix, episode, suffix)
    }
}

val subtitles = extractEpisodeDetails(subtitleFiles)
val videos = extractEpisodeDetails(videoFiles)

println("Videos:")
videos.forEach {
    println(" - ${it.episode}")
}

println("Subtitles:")
subtitles.forEach {
    println(" - ${it.episode}")
}

for (subtitle in subtitles) {
    val video = videos.firstOrNull { video -> video.episode == subtitle.episode } ?: continue

    println(
        """|Matched: ${subtitle.original}
           |   with: ${video.original}""".trimMargin()
    )

    subtitle.original.renameTo(File("${video.original.parent}/${video.prefix}${video.episode}${video.suffix}.${subtitle.original.extensionWithLanguage}"))
}