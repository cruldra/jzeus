package jzeus.jianying

import jzeus.any.Size
import jzeus.failure.failure
import jzeus.file.subFile
import jzeus.file.raiseForNotDirectory
import jzeus.json.objectMapper
import jzeus.list.add
import java.io.File

fun <R> JianyingDesktop.use(block: JianyingDesktop.() -> R): R {
    try {
        this.start()
        return block(this)
    } finally {
        this.stop()
    }
}

fun ClickniumService.sleep(seconds: Long): ClickniumService {
    jzeus.async.sleep(seconds)
    return this
}

fun Draft.Draft.resize(size: Size) {
    this.content.canvasConfig =
        Draft.CanvasConfig(
            height = size.height,
            ratio = size.ratio ?: failure("没有指定画布比例"),
            width = size.width
        )
}

/**
 * 添加文本轨道
 *
 * 在剪映客户端中添加一个文本轨道的逻辑是:
 *
 * 1. 在tracks中添加一个 Track
 * 2. 如果有多个文本片段,则创建Segment然后添加到Track的segments中
 * 3. 为每个Segment在materials.texts中添加一个 TextMaterial
 * 4. 为每个Segment在materials.material_animations中添加一个 StickerAnimation
 * 5. 每个Segment的extra_material_refs中添加对应的StickerAnimation的id
 * 6. 每个Segment的material_id指向对应的TextMaterial的id
 *
 * @param text 文本内容
 * @param maxLengthPerSegment 每个片段的最大长度
 */
fun Draft.Draft.addTextTrack(text: String, maxLengthPerSegment: Int = 500): Draft.Draft {

    val fontSize = 12.0f
    val scale = 1.0f
    val lineSpacing = 0.02f

    // 根据每个片段的最大长度获取轨道中每个片段的文本
    val segmentTexts = text.chunked(maxLengthPerSegment)

    val textTrack = Draft.Track(
        segments = mutableListOf(),
        type = "text"
    )

    segmentTexts.forEachIndexed { i, segmentText ->
        val stickerAnimation = Draft.StickerAnimation()
        val textContent = Draft.TextContent(
            text = segmentText,
            styles = listOf(
                Draft.Style(
                    size = fontSize,
                    range = listOf(0, segmentText.length)
                )
            )
        )
        val textMaterial = Draft.TextMaterial(
            content = objectMapper.writeValueAsString(textContent),
            fontSize = fontSize,
            lineSpacing = lineSpacing
        )
        val segment = Draft.Segment(
            clip = Draft.Clip(
                scale = Draft.Scale(
                    x = scale,
                    y = scale
                )
            ),
            renderIndex = 14003,
            extraMaterialRefs = listOf(stickerAnimation.id),
            materialId = textMaterial.id,
            targetTimerange = Draft.TimeRange(
                start = i * 3000000,
                duration = 3000000
            ),
            hdrSettings = null,
            sourceTimerange = null,
            enableAdjust = false,
            enableLut = false
        )
        content.materials.materialAnimations.add(stickerAnimation)
        content.materials.texts.add(textMaterial)
        textTrack.segments.add(segment)
    }

    // 计算新的视频时长
    val duration = 3000000 * segmentTexts.size
    content.duration = (content.duration?.plus(duration) ?: duration).toLong()

    content.tracks.add(textTrack)
    return this
}


fun Draft.Draft.save(rootDir: File): Draft.Draft {
    val draftDir = rootDir.subFile(this.name)
    draftDir.mkdirs()
    draftDir.raiseForNotDirectory("保存草稿失败,因为[${draftDir.absolutePath}]不是一个目录")
    this.files = Draft.Draft.Files(
        draftDir.subFile("draft_meta_info.json") {
            objectMapper.writeValue(this, this@save.meta)
        },
        draftDir.subFile("draft_content.json") {
            objectMapper.writeValue(this, this@save.content)
        }
    )

    return this
}
