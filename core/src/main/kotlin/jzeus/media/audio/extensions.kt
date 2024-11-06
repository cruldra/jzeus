package jzeus.media.audio

import cn.hutool.core.io.unit.DataSize
import jzeus.file.siblingFile
import jzeus.os.exec
import jzeus.primitive.ceil
import jzeus.str.asCommandLine
import jzeus.uuid.uuid
import java.time.Duration

/**
 * 将这个字符串视作一个文件路径并基于这个路径创建一个[音频文件][AudioFile]对象
 * @param   block 对创建的[AudioFile]对象进行操作
 * @return  [AudioFile] 音频文件
 */
fun String.toAudioFile(block: AudioFile.() -> Unit = {}) = AudioFile(this).apply(block)

/**
 * 获取这个音频文件的时长
 * @return  [Duration] 时长
 */
val AudioFile.duration: Duration
    get() = Duration.ofSeconds(
        "ffprobe -v error -show_entries format=duration -of default=noprint_wrappers=1:nokey=1 ${this.name}"
            .asCommandLine().exec(
                this.parentFile,
            ).toDouble().ceil().toLong()
    )

/**
 * 获取这个音频文件的大小
 * @return  [DataSize] 大小
 */
val AudioFile.size: DataSize
    get() = DataSize.ofMegabytes(this.length() / 1024 / 1024)

/**
 * 通过提高或降低音频比特率来调整音频文件的大小
 * @param newSize 新的文件大小
 * @param outputFile 大小调整后的文件,如果不指定,则在当前音频文件所在的目录创建一个新文件
 */
fun AudioFile.resize(newSize: DataSize, outputFile: AudioFile? = null): AudioFile {

    val finalOutputFile = outputFile ?: this.siblingFile(this.nameWithoutExtension + uuid() + ".m4a")
    val targetBitrate = newSize.toMegabytes() * 8192 / this.duration.seconds
    "ffmpeg -i ${this.name} -c:a aac -b:a ${targetBitrate}k '${finalOutputFile.absolutePath}'".asCommandLine()
        .exec(this.parentFile)
    return finalOutputFile.absolutePath.toAudioFile()
}
