package jzeus.jianying

import com.fasterxml.jackson.annotation.JsonProperty
import jzeus.uuid.uuid
import java.io.File

sealed interface Draft {
    /**
     * 剪映草稿元数据
     * @property cloudPackageCompletedTime 云端打包完成时间
     * @property draftCloudCapcutPurchaseInfo 云端Capcut购买信息
     * @property draftCloudLastActionDownload 云端最后动作是否为下载
     * @property draftCloudMaterials 云端材料
     * @property draftCloudPurchaseInfo 云端购买信息
     * @property draftCloudTemplateId 云端模板ID
     * @property draftCloudTutorialInfo 云端教程信息
     * @property draftCloudVideocutPurchaseInfo 云端视频剪辑购买信息
     * @property draftCover 草稿封面
     * @property draftDeeplinkUrl 草稿深度链接URL
     * @property draftEnterpriseInfo 企业信息
     * @property draftFoldPath 草稿文件夹路径
     * @property draftId 草稿ID
     * @property draftIsAiPackagingUsed 是否使用AI打包
     * @property draftIsAiShorts 是否为AI短视频
     * @property draftIsAiTranslate 是否使用AI翻译
     * @property draftIsArticleVideoDraft 是否为文章视频草稿
     * @property draftIsFromDeeplink 是否来自深度链接
     * @property draftIsInvisible 是否为隐形草稿
     * @property draftMaterials 草稿素材
     * @property draftMaterialsCopiedInfo 复制的草稿材料信息
     * @property draftName 草稿名称
     * @property draftNewVersion 草稿新版本
     * @property draftRemovableStorageDevice 可移动存储设备
     * @property draftRootPath 草稿根路径
     * @property draftSegmentExtraInfo 草稿段落额外信息
     * @property draftTimelineMaterialsSize 时间线材料大小
     * @property draftType 草稿类型
     * @property tmDraftCloudCompleted 草稿云端完成时间
     * @property tmDraftCloudModified 草稿云端修改时间
     * @property tmDraftCreate 草稿创建时间
     * @property tmDraftModified 草稿修改时间
     * @property tmDraftRemoved 草稿移除时间
     * @property tmDuration 持续时间
     * @author dongjak
     * @created 2024/10/09
     * @version 1.0
     * @since 1.0
     */
    data class MetaInfo(
        @JsonProperty("cloud_package_completed_time")
        val cloudPackageCompletedTime: String? = null,

        @JsonProperty("draft_cloud_capcut_purchase_info")
        val draftCloudCapcutPurchaseInfo: String? = null,

        @JsonProperty("draft_cloud_last_action_download")
        val draftCloudLastActionDownload: Boolean? = null,

        @JsonProperty("draft_cloud_materials")
        val draftCloudMaterials: List<Any> = mutableListOf(),

        @JsonProperty("draft_cloud_purchase_info")
        val draftCloudPurchaseInfo: String? = null,

        @JsonProperty("draft_cloud_template_id")
        val draftCloudTemplateId: String? = null,

        @JsonProperty("draft_cloud_tutorial_info")
        val draftCloudTutorialInfo: String? = null,

        @JsonProperty("draft_cloud_videocut_purchase_info")
        val draftCloudVideocutPurchaseInfo: String? = null,

        @JsonProperty("draft_cover")
        val draftCover: String = "draft_cover.jpg",

        @JsonProperty("draft_deeplink_url")
        val draftDeeplinkUrl: String? = null,

        @JsonProperty("draft_enterprise_info")
        val draftEnterpriseInfo: EnterpriseInfo = EnterpriseInfo(),

        @JsonProperty("draft_fold_path")
        val draftFoldPath: String? = null,

        @JsonProperty("draft_id")
        val draftId: String = uuid(
            upper = true, formats = mutableListOf(Pair(8, "-"), Pair(12, "-"), Pair(16, "-"), Pair(20, "-"))
        ),

        @JsonProperty("draft_is_ai_packaging_used")
        val draftIsAiPackagingUsed: Boolean = false,

        @JsonProperty("draft_is_ai_shorts")
        val draftIsAiShorts: Boolean = false,

        @JsonProperty("draft_is_ai_translate")
        val draftIsAiTranslate: Boolean = false,

        @JsonProperty("draft_is_article_video_draft")
        val draftIsArticleVideoDraft: Boolean = false,

        @JsonProperty("draft_is_from_deeplink")
        val draftIsFromDeeplink: String = "false",

        @JsonProperty("draft_is_invisible")
        val draftIsInvisible: Boolean = false,

        @JsonProperty("draft_materials")
        val draftMaterials: List<Material> = mutableListOf(),

        @JsonProperty("draft_materials_copied_info")
        val draftMaterialsCopiedInfo: List<Any> = mutableListOf(),

        @JsonProperty("draft_name")
        val draftName: String? = null,

        @JsonProperty("draft_new_version")
        val draftNewVersion: String = "",

        @JsonProperty("draft_removable_storage_device")
        val draftRemovableStorageDevice: String = "D:",

        @JsonProperty("draft_root_path")
        val draftRootPath: String? = null,

        @JsonProperty("draft_segment_extra_info")
        val draftSegmentExtraInfo: List<Any> = mutableListOf(),

        @JsonProperty("draft_timeline_materials_size_")
        val draftTimelineMaterialsSize: Int = 8016,

        @JsonProperty("draft_type")
        val draftType: String = "",

        @JsonProperty("tm_draft_cloud_completed")
        val tmDraftCloudCompleted: String = "",

        @JsonProperty("tm_draft_cloud_modified")
        val tmDraftCloudModified: Int = 0,

        @JsonProperty("tm_draft_create")
        val tmDraftCreate: Long = 1720784146489727,

        @JsonProperty("tm_draft_modified")
        val tmDraftModified: Long = 1720785106585349,

        @JsonProperty("tm_draft_removed")
        val tmDraftRemoved: Int = 0,

        @JsonProperty("tm_duration")
        val tmDuration: Int = 0
    )

    /**
     * 企业信息
     * @property draftEnterpriseExtra 企业额外信息
     * @property draftEnterpriseId 企业ID
     * @property draftEnterpriseName 企业名称
     * @property enterpriseMaterial 企业材料
     * @author dongjak
     * @created 2024/10/09
     * @version 1.0
     * @since 1.0
     */
    data class EnterpriseInfo(
        @JsonProperty("draft_enterprise_extra") val draftEnterpriseExtra: String? = null,

        @JsonProperty("draft_enterprise_id") val draftEnterpriseId: String? = null,

        @JsonProperty("draft_enterprise_name") val draftEnterpriseName: String? = null,

        @JsonProperty("enterprise_material") val enterpriseMaterial: List<Any>? = null
    )

    /**
     * 表示一个时间范围
     * @property duration 持续时间
     * @property start 开始时间
     * @author dongjak
     * @created 2024/10/09
     * @version 1.0
     * @since 1.0
     */
    data class TimeRange(
        val duration: Int? = null, val start: Int? = null
    )


    /**
     * 图片素材
     * @property createTime 创建时间,Unix时间戳
     * @property duration 持续时间,以微秒为单位
     * @property extraInfo 额外信息,例如文件名
     * @property filePath 文件路径
     * @property height 图片高度,以像素为单位
     * @property id 图片素材的唯一标识符
     * @property importTime 导入时间,Unix时间戳
     * @property importTimeMs 导入时间,以微秒为单位
     * @property itemSource 素材来源
     * @property md5 文件的MD5哈希值,用于校验
     * @property metetype 素材类型,例如 "photo"
     * @property roughcutTimeRange 粗剪时间范围
     * @property subTimeRange 子时间范围
     * @property type 类型,例如`0`代表图片素材
     * @property width 图片宽度,以像素为单位
     * @author dongjak
     * @created 2024/10/09
     * @version 1.0
     * @since 1.0
     */
    data class ImageMaterial(
        @JsonProperty("create_time") val createTime: Long? = null,

        @JsonProperty("duration") val duration: Int? = null,

        @JsonProperty("extra_info") val extraInfo: String? = null,

        @JsonProperty("file_Path") val filePath: String? = null,

        @JsonProperty("height") val height: Int? = null,

        @JsonProperty("id") val id: String? = null,

        @JsonProperty("import_time") val importTime: Long? = null,

        @JsonProperty("import_time_ms") val importTimeMs: Long? = null,

        @JsonProperty("item_source") val itemSource: Any? = null,

        @JsonProperty("md5") val md5: String? = null,

        @JsonProperty("metetype") val metetype: String? = null,

        @JsonProperty("roughcut_time_range") val roughcutTimeRange: TimeRange? = null,

        @JsonProperty("sub_time_range") val subTimeRange: TimeRange? = null,

        @JsonProperty("type") val type: Int? = null,

        @JsonProperty("width") val width: Int? = null
    )


    /**
     * 表示草稿中的一个素材
     * @property type 素材类型
     * @property value 素材列表
     * @author dongjak
     * @created 2024/10/09
     * @version 1.0
     * @since 1.0
     */
    data class Material(
        @JsonProperty("type") val type: Int? = null,

        @JsonProperty("value") val value: List<ImageMaterial>? = null
    )


    /**
     * 画布配置
     * @property height 画布高度
     * @property ratio 画布比例
     * @property width 画布宽度
     * @author dongjak
     * @created 2024/10/09
     * @version 1.0
     * @since 1.0
     */
    data class CanvasConfig(
        val height: Int, val ratio: String, val width: Int
    )


    /**
     * 配置
     * @property adjustMaxIndex 调整最大索引
     * @property attachmentInfo 附件信息
     * @property combinationMaxIndex 组合最大索引
     * @property exportRange 导出范围
     * @property extractAudioLastIndex 提取音频最后索引
     * @property lyricsRecognitionId 歌词识别ID
     * @property lyricsSync 歌词同步
     * @property lyricsTaskinfo 歌词任务信息
     * @property maintrackAdsorb 主轨吸附
     * @property materialSaveMode 材料保存模式
     * @property multiLanguageCurrent 当前多语言
     * @property multiLanguageList 多语言列表
     * @property multiLanguageMain 主多语言
     * @property multiLanguageMode 多语言模式
     * @property originalSoundLastIndex 原始声音最后索引
     * @property recordAudioLastIndex 录音最后索引
     * @property stickerMaxIndex 贴纸最大索引
     * @property subtitleKeywordsConfig 字幕关键词配置
     * @property subtitleRecognitionId 字幕识别ID
     * @property subtitleSync 字幕同步
     * @property subtitleTaskinfo 字幕任务信息
     * @property systemFontList 系统字体列表
     * @property videoMute 视频静音
     * @property zoomInfoParams 缩放信息参数
     * @author [Your Name]
     * @created 2024/10/09
     * @version 1.0
     * @since 1.0
     */
    data class Config(
        @JsonProperty("adjust_max_index") val adjustMaxIndex: Int = 1,
        @JsonProperty("attachment_info") val attachmentInfo: List<Any> = mutableListOf(),
        @JsonProperty("combination_max_index") val combinationMaxIndex: Int = 1,
        @JsonProperty("export_range") val exportRange: Any? = null,
        @JsonProperty("extract_audio_last_index") val extractAudioLastIndex: Int = 1,
        @JsonProperty("lyrics_recognition_id") val lyricsRecognitionId: String = "",
        @JsonProperty("lyrics_sync") val lyricsSync: Boolean = true,
        @JsonProperty("lyrics_taskinfo") val lyricsTaskinfo: List<Any> = mutableListOf(),
        @JsonProperty("maintrack_adsorb") val maintrackAdsorb: Boolean = true,
        @JsonProperty("material_save_mode") val materialSaveMode: Int = 0,
        @JsonProperty("multi_language_current") val multiLanguageCurrent: String = "none",
        @JsonProperty("multi_language_list") val multiLanguageList: List<Any> = mutableListOf(),
        @JsonProperty("multi_language_main") val multiLanguageMain: String = "none",
        @JsonProperty("multi_language_mode") val multiLanguageMode: String = "none",
        @JsonProperty("original_sound_last_index") val originalSoundLastIndex: Int = 1,
        @JsonProperty("record_audio_last_index") val recordAudioLastIndex: Int = 1,
        @JsonProperty("sticker_max_index") val stickerMaxIndex: Int = 1,
        @JsonProperty("subtitle_keywords_config") val subtitleKeywordsConfig: Any? = null,
        @JsonProperty("subtitle_recognition_id") val subtitleRecognitionId: String = "",
        @JsonProperty("subtitle_sync") val subtitleSync: Boolean = true,
        @JsonProperty("subtitle_taskinfo") val subtitleTaskinfo: List<Any> = mutableListOf(),
        @JsonProperty("system_font_list") val systemFontList: List<Any> = mutableListOf(),
        @JsonProperty("video_mute") val videoMute: Boolean = false,
        @JsonProperty("zoom_info_params") val zoomInfoParams: Any? = null
    )

    /**
     * 关键帧
     * @property adjusts 调整
     * @property audios 音频
     * @property effects 效果
     * @property filters 滤镜
     * @property handwrites 手写
     * @property stickers 贴纸
     * @property texts 文本
     * @property videos 视频
     * @author [Your Name]
     * @created 2024/10/09
     * @version 1.0
     * @since 1.0
     */
    data class Keyframes(
        val adjusts: List<Any> = mutableListOf(),
        val audios: List<Any> = mutableListOf(),
        val effects: List<Any> = mutableListOf(),
        val filters: List<Any> = mutableListOf(),
        val handwrites: List<Any> = mutableListOf(),
        val stickers: List<Any> = mutableListOf(),
        val texts: List<Any> = mutableListOf(),
        val videos: List<Any> = mutableListOf()
    )

    /**
     * 草稿内容
     * @property canvasConfig 画布配置
     * @property colorSpace 色彩空间
     * @property config 配置
     * @property cover 封面
     * @property createTime 创建时间
     * @property duration 持续时间(微秒)
     * @property extraInfo 额外信息
     * @property fps FPS
     * @property freeRenderIndexModeOn 自由渲染索引模式开启
     * @property groupContainer 组容器
     * @property id ID
     * @property keyframeGraphList 关键帧图表列表
     * @property keyframes 关键帧
     * @property lastModifiedPlatform 最后修改平台
     * @property materials 素材
     * @property mutableConfig 可变配置
     * @property name 名称
     * @property newVersion 新版本
     * @property platform 平台
     * @property relationships 关系
     * @property renderIndexTrackModeOn 渲染索引轨道模式开启
     * @property retouchCover 修饰封面
     * @property source 来源
     * @property staticCoverImagePath 静态封面图片路径
     * @property timeMarks 时间标记
     * @property tracks 轨道
     * @property updateTime 更新时间
     * @property version 版本
     * @author [Your Name]
     * @created 2024/10/09
     * @version 1.0
     * @since 1.0
     */
    data class Content(
        @JsonProperty("canvas_config")
        var canvasConfig: CanvasConfig? = null,
        @JsonProperty("color_space")
        val colorSpace: Int? = null,
        val config: Config? = null,
        val cover: String? = null,
        @JsonProperty("create_time")
        val createTime: Long? = null,
        var duration: Long? = null,
        @JsonProperty("extra_info")
        val extraInfo: Any? = null,
        val fps: Float? = null,
        @JsonProperty("free_render_index_mode_on")
        val freeRenderIndexModeOn: Boolean? = null,
        @JsonProperty("group_container")
        val groupContainer: Any? = null,
        val id: String = uuid(
            upper = true, formats = mutableListOf(Pair(8, "-"), Pair(12, "-"), Pair(16, "-"), Pair(20, "-"))
        ),
        @JsonProperty("keyframe_graph_list")
        val keyframeGraphList: List<Any>? = null,
        val keyframes: Keyframes? = null,
        @JsonProperty("last_modified_platform")
        val lastModifiedPlatform: Platform? = null,
        val materials: Materials = Materials(),
        @JsonProperty("mutable_config")
        val mutableConfig: Any? = null,
        val name: String? = null,
        @JsonProperty("new_version")
        val newVersion: String? = null,
        val platform: Platform? = null,
        val relationships: List<Any>? = null,
        @JsonProperty("render_index_track_mode_on")
        val renderIndexTrackModeOn: Boolean? = null,
        @JsonProperty("retouch_cover")
        val retouchCover: Any? = null,
        val source: String? = null,
        @JsonProperty("static_cover_image_path")
        val staticCoverImagePath: String? = null,
        @JsonProperty("time_marks")
        val timeMarks: Any? = null,
        val tracks: List<Track> = mutableListOf(Track(type = "video")),
        @JsonProperty("update_time")
        val updateTime: Long? = null,
        val version: Int? = null
    )

    /**
     * 轨道信息
     * @property attribute 属性
     * @property flag 标志
     * @property id ID
     * @property isDefaultName 是否默认名称
     * @property name 名称
     * @property segments 片段列表
     * @property type 类型
     * @author [Your Name]
     * @created 2024/10/09
     * @version 1.0
     * @since 1.0
     */
    data class Track(
        val attribute: Int = 0,

        val flag: Int = 0,

        val id: String = uuid(
            upper = true, formats = mutableListOf(Pair(8, "-"), Pair(12, "-"), Pair(16, "-"), Pair(20, "-"))
        ),

        @JsonProperty("is_default_name") val isDefaultName: Boolean = true,

        val name: String = "",

        val segments: List<Segment> = emptyList(),

        val type: String = "video"
    )

    /**
     * 剪辑
     * @property alpha 透明度
     * @property flip 翻转
     * @property rotation 旋转
     * @property scale 缩放
     * @property transform 变换
     * @author [Your Name]
     * @created 2024/10/09
     * @version 1.0
     * @since 1.0
     */
    data class Clip(
        val alpha: Float = 1.0f,

        val flip: Flip = Flip(),

        val rotation: Float = 0.0f,

        val scale: Scale = Scale(),

        val transform: Transform = Transform()
    )

    /**
     * 翻转
     * @property horizontal 水平翻转
     * @property vertical 垂直翻转
     * @author [Your Name]
     * @created 2024/10/09
     * @version 1.0
     * @since 1.0
     */
    data class Flip(
        val horizontal: Boolean = false,

        val vertical: Boolean = false
    )

    /**
     * 缩放
     * @property x x轴缩放
     * @property y y轴缩放
     * @author [Your Name]
     * @created 2024/10/09
     * @version 1.0
     * @since 1.0
     */
    data class Scale(
        val x: Float = 1.0f,

        val y: Float = 1.0f
    )

    /**
     * 变换
     * @property x x轴变换
     * @property y y轴变换
     * @author [Your Name]
     * @created 2024/10/09
     * @version 1.0
     * @since 1.0
     */
    data class Transform(
        val x: Float = 0.0f,

        val y: Float = 0.0f
    )

    /**
     * 片段信息
     * @property captionInfo 字幕信息
     * @property cartoon 卡通
     * @property clip 剪辑
     * @property commonKeyframes 常见关键帧
     * @property enableAdjust 启用调整
     * @property enableColorCorrectAdjust 启用颜色校正调整
     * @property enableColorCurves 启用颜色曲线
     * @property enableColorMatchAdjust 启用颜色匹配调整
     * @property enableColorWheels 启用颜色轮
     * @property enableLut 启用LUT
     * @property enableSmartColorAdjust 启用智能颜色调整
     * @property extraMaterialRefs 额外素材引用
     * @property groupId 组ID
     * @property hdrSettings HDR设置
     * @property id ID
     * @property intensifiesAudio 强化音频
     * @property isPlaceholder 是否占位符
     * @property isToneModify 是否音调修改
     * @property keyframeRefs 关键帧引用
     * @property lastNonzeroVolume 最后一个非零音量
     * @property materialId 素材ID
     * @property renderIndex 渲染索引
     * @property responsiveLayout 响应布局
     * @property reverse 反向
     * @property sourceTimerange 源时间范围
     * @property speed 速度
     * @property targetTimerange 目标时间范围
     * @property templateId 模板ID
     * @property templateScene 模板场景
     * @property trackAttribute 轨道属性
     * @property trackRenderIndex 轨道渲染索引
     * @property uniformScale 统一缩放
     * @property visible 可见性
     * @property volume 音量
     * @author [Your Name]
     * @created 2024/10/09
     * @version 1.0
     * @since 1.0
     */
    data class Segment(
        @JsonProperty("caption_info") val captionInfo: String? = null,

        val cartoon: Boolean = false,

        val clip: Clip = Clip(),

        @JsonProperty("common_keyframes") val commonKeyframes: List<String> = emptyList(),

        @JsonProperty("enable_adjust") val enableAdjust: Boolean = true,

        @JsonProperty("enable_color_correct_adjust") val enableColorCorrectAdjust: Boolean = false,

        @JsonProperty("enable_color_curves") val enableColorCurves: Boolean = true,

        @JsonProperty("enable_color_match_adjust") val enableColorMatchAdjust: Boolean = false,

        @JsonProperty("enable_color_wheels") val enableColorWheels: Boolean = true,

        @JsonProperty("enable_lut") val enableLut: Boolean = true,

        @JsonProperty("enable_smart_color_adjust") val enableSmartColorAdjust: Boolean = false,

        @JsonProperty("extra_material_refs") val extraMaterialRefs: List<String> = emptyList(),

        @JsonProperty("group_id") val groupId: String = "",

        @JsonProperty("hdr_settings") val hdrSettings: HDRSettings? = HDRSettings(),

        val id: String = uuid(
            upper = true, formats = mutableListOf(Pair(8, "-"), Pair(12, "-"), Pair(16, "-"), Pair(20, "-"))
        ),

        @JsonProperty("intensifies_audio") val intensifiesAudio: Boolean = false,

        @JsonProperty("is_placeholder") val isPlaceholder: Boolean = false,

        @JsonProperty("is_tone_modify") val isToneModify: Boolean = false,

        @JsonProperty("keyframe_refs") val keyframeRefs: List<String> = emptyList(),

        @JsonProperty("last_nonzero_volume") val lastNonzeroVolume: Float = 1.0f,

        @JsonProperty("material_id") val materialId: String? = null,

        @JsonProperty("render_index") val renderIndex: Int = 0,

        @JsonProperty("responsive_layout") val responsiveLayout: ResponsiveLayout = ResponsiveLayout(),

        val reverse: Boolean = false,

        @JsonProperty("source_timerange") val sourceTimerange: TimeRange? = TimeRange(),

        val speed: Float = 1.0f,

        @JsonProperty("target_timerange") val targetTimerange: TimeRange = TimeRange(),

        @JsonProperty("template_id") val templateId: String = "",

        @JsonProperty("template_scene") val templateScene: String = "default",

        @JsonProperty("track_attribute") val trackAttribute: Int = 0,

        @JsonProperty("track_render_index") val trackRenderIndex: Int = 0,

        @JsonProperty("uniform_scale") val uniformScale: UniformScale = UniformScale(),

        val visible: Boolean = true,

        val volume: Float = 1.0f
    )

    /**
     * 响应式布局
     * @property enable 启用
     * @property horizontalPosLayout 水平位置布局
     * @property sizeLayout 大小布局
     * @property targetFollow 目标跟随
     * @property verticalPosLayout 垂直位置布局
     * @author [Your Name]
     * @created 2024/10/09
     * @version 1.0
     * @since 1.0
     */
    data class ResponsiveLayout(
        val enable: Boolean = false,

        @JsonProperty("horizontal_pos_layout") val horizontalPosLayout: Int = 0,

        @JsonProperty("size_layout") val sizeLayout: Int = 0,

        @JsonProperty("target_follow") val targetFollow: String = "",

        @JsonProperty("vertical_pos_layout") val verticalPosLayout: Int = 0
    )

    /**
     * 统一缩放
     * @property on 启用
     * @property value 值
     * @author [Your Name]
     * @created 2024/10/09
     * @version 1.0
     * @since 1.0
     */
    data class UniformScale(
        val on: Boolean = true,

        val value: Float = 1.0f
    )

    /**
     * HDR设置
     * @property intensity 强度
     * @property mode 模式
     * @property nits 尼特
     * @author [Your Name]
     * @created 2024/10/09
     * @version 1.0
     * @since 1.0
     */
    data class HDRSettings(
        val intensity: Float = 1.0f,

        val mode: Int = 1,

        val nits: Int = 1000
    )

    /**
     * 平台信息
     * @property appId 应用ID
     * @property appSource 应用来源
     * @property appVersion 应用版本
     * @property deviceId 设备ID
     * @property hardDiskId 硬盘ID
     * @property macAddress MAC地址
     * @property os 操作系统
     * @property osVersion 操作系统版本
     * @author [Your Name]
     * @created 2024/10/09
     * @version 1.0
     * @since 1.0
     */
    data class Platform(
        @JsonProperty("app_id") val appId: Int = 3704,
        @JsonProperty("app_source") val appSource: String = "lv",
        @JsonProperty("app_version") val appVersion: String = "5.9.0",
        @JsonProperty("device_id") val deviceId: String = "93c3be64246ff28979c8f97ecb5e96a9",
        @JsonProperty("hard_disk_id") val hardDiskId: String = "95fde6ca35187cfd091c19dae20a7c86",
        @JsonProperty("mac_address") val macAddress: String = "1f9453637d15522c8f952a03aefa9e74,d04e333df6159c278b5e57296362720e",
        val os: String = "windows",
        @JsonProperty("os_version") val osVersion: String = "10.0.22631"
    )

    /**
     * 贴纸动画
     * @property animations 动画列表
     * @property id ID
     * @property multiLanguageCurrent 多语言当前状态
     * @property type 类型
     * @author [Your Name]
     * @created 2024/10/09
     * @version 1.0
     * @since 1.0
     */
    data class StickerAnimation(
        val animations: List<String> = emptyList(),

        val id: String = uuid(
            upper = true, formats = mutableListOf(Pair(8, "-"), Pair(12, "-"), Pair(16, "-"), Pair(20, "-"))
        ),

        @JsonProperty("multi_language_current") val multiLanguageCurrent: String = "none",

        val type: String = "sticker_animation"
    )

    /**
     * 音频配置
     * @property audioChannelMapping 音频通道映射
     * @property id ID
     * @property isConfigOpen 配置是否开启
     * @property type 类型
     * @author [Your Name]
     * @created 2024/10/09
     * @version 1.0
     * @since 1.0
     */
    data class AudioConfig(
        @JsonProperty("audio_channel_mapping") val audioChannelMapping: Int = 0,

        val id: String = uuid(
            upper = true, formats = mutableListOf(Pair(8, "-"), Pair(12, "-"), Pair(16, "-"), Pair(20, "-"))
        ),

        @JsonProperty("is_config_open") val isConfigOpen: Boolean = false,

        val type: String = "none"
    )

    /**
     * 材料
     * @property aiTranslates AI翻译
     * @property audioBalances 音频平衡
     * @property audioEffects 音频效果
     * @property audioFades 音频淡入淡出
     * @property audioTrackIndexes 音轨索引
     * @property audios 音频
     * @property beats 节拍
     * @property canvases 画布
     * @property chromas 色度
     * @property colorCurves 色彩曲线
     * @property digitalHumans 数字人
     * @property drafts 草稿
     * @property effects 效果
     * @property flowers 花朵
     * @property greenScreens 绿幕
     * @property handwrites 手写
     * @property hsl 色相饱和度亮度
     * @property images 图片
     * @property logColorWheels 日志色轮
     * @property loudnesses 响度
     * @property manualDeformations 手动变形
     * @property masks 遮罩
     * @property materialAnimations 材料动画
     * @property materialColors 材料颜色
     * @property multiLanguageRefs 多语言参考
     * @property placeholders 占位符
     * @property pluginEffects 插件效果
     * @property primaryColorWheels 主色轮
     * @property realtimeDenoises 实时降噪
     * @property shapes 形状
     * @property smartCrops 智能裁剪
     * @property smartRelights 智能光照
     * @property soundChannelMappings 声道映射
     * @property speeds 速度
     * @property stickers 贴纸
     * @property tailLeaders 片尾
     * @property textTemplates 文本模板
     * @property texts 文本
     * @property timeMarks 时间标记
     * @property transitions 转场
     * @property videoEffects 视频效果
     * @property videoTrackings 视频追踪
     * @property videos 视频
     * @property vocalBeautifys 人声美化
     * @property vocalSeparations 人声分离
     * @author [Your Name]
     * @created 2024/10/09
     * @version 1.0
     * @since 1.0
     */
    data class Materials(
        @JsonProperty("ai_translates") val aiTranslates: List<Any> = mutableListOf(),
        @JsonProperty("audio_balances") val audioBalances: List<Any> = mutableListOf(),
        @JsonProperty("audio_effects") val audioEffects: List<Any> = mutableListOf(),
        @JsonProperty("audio_fades") val audioFades: List<Any> = mutableListOf(),
        @JsonProperty("audio_track_indexes") val audioTrackIndexes: List<Any> = mutableListOf(),
        val audios: List<Any> = mutableListOf(),
        val beats: List<Any> = mutableListOf(),
        val canvases: List<Canvas> = mutableListOf(),
        val chromas: List<Any> = mutableListOf(),
        @JsonProperty("color_curves") val colorCurves: List<Any> = mutableListOf(),
        @JsonProperty("digital_humans") val digitalHumans: List<DigitalHuman> = mutableListOf(),
        val drafts: List<Any> = mutableListOf(),
        val effects: List<Any> = mutableListOf(),
        val flowers: List<Any> = mutableListOf(),
        @JsonProperty("green_screens") val greenScreens: List<Any> = mutableListOf(),
        val handwrites: List<Any> = mutableListOf(),
        val hsl: List<Any> = mutableListOf(),
        val images: List<Any> = mutableListOf(),
        @JsonProperty("log_color_wheels") val logColorWheels: List<Any> = mutableListOf(),
        val loudnesses: List<Any> = mutableListOf(),
        @JsonProperty("manual_deformations") val manualDeformations: List<Any> = mutableListOf(),
        val masks: List<Any> = mutableListOf(),
        @JsonProperty("material_animations") val materialAnimations: List<StickerAnimation> = mutableListOf(),
        @JsonProperty("material_colors") val materialColors: List<Any> = mutableListOf(),
        @JsonProperty("multi_language_refs") val multiLanguageRefs: List<Any> = mutableListOf(),
        val placeholders: List<Any> = mutableListOf(),
        @JsonProperty("plugin_effects") val pluginEffects: List<Any> = mutableListOf(),
        @JsonProperty("primary_color_wheels") val primaryColorWheels: List<Any> = mutableListOf(),
        @JsonProperty("realtime_denoises") val realtimeDenoises: List<Any> = mutableListOf(),
        val shapes: List<Any> = mutableListOf(),
        @JsonProperty("smart_crops") val smartCrops: List<Any> = mutableListOf(),
        @JsonProperty("smart_relights") val smartRelights: List<Any> = mutableListOf(),
        @JsonProperty("sound_channel_mappings") val soundChannelMappings: List<AudioConfig> = mutableListOf(),
        val speeds: List<SpeedConfig> = mutableListOf(),
        val stickers: List<Any> = mutableListOf(),
        @JsonProperty("tail_leaders") val tailLeaders: List<Any> = mutableListOf(),
        @JsonProperty("text_templates") val textTemplates: List<Any> = mutableListOf(),
        val texts: List<TextMaterial> = mutableListOf(),
        @JsonProperty("time_marks") val timeMarks: List<Any> = mutableListOf(),
        val transitions: List<Any> = mutableListOf(),
        @JsonProperty("video_effects") val videoEffects: List<Any> = mutableListOf(),
        @JsonProperty("video_trackings") val videoTrackings: List<Any> = mutableListOf(),
        val videos: List<Photo> = mutableListOf(),
        @JsonProperty("vocal_beautifys") val vocalBeautifys: List<Any> = mutableListOf(),
        @JsonProperty("vocal_separations") val vocalSeparations: List<VocalSeparation> = mutableListOf()
    )

    /**
     * 人声分离
     * @property choice 选择
     * @property id ID
     * @property productionPath 制作路径
     * @property timeRange 时间范围
     * @property type 类型
     * @author [Your Name]
     * @created 2024/10/09
     * @version 1.0
     * @since 1.0
     */
    data class VocalSeparation(
        val choice: Int = 0,

        val id: String = uuid(
            upper = true, formats = mutableListOf(Pair(8, "-"), Pair(12, "-"), Pair(16, "-"), Pair(20, "-"))
        ),

        @JsonProperty("production_path") val productionPath: String = "",

        @JsonProperty("time_range") val timeRange: TimeRange? = null,

        val type: String = "vocal_separation"
    )

    /**
     * 裁剪
     * @property lowerLeftX 左下角X坐标
     * @property lowerLeftY 左下角Y坐标
     * @property lowerRightX 右下角X坐标
     * @property lowerRightY 右下角Y坐标
     * @property upperLeftX 左上角X坐标
     * @property upperLeftY 左上角Y坐标
     * @property upperRightX 右上角X坐标
     * @property upperRightY 右上角Y坐标
     * @author [Your Name]
     * @created 2024/10/09
     * @version 1.0
     * @since 1.0
     */
    data class Crop(
        @JsonProperty("lower_left_x") val lowerLeftX: Float = 0.0f,

        @JsonProperty("lower_left_y") val lowerLeftY: Float = 1.0f,

        @JsonProperty("lower_right_x") val lowerRightX: Float = 1.0f,

        @JsonProperty("lower_right_y") val lowerRightY: Float = 1.0f,

        @JsonProperty("upper_left_x") val upperLeftX: Float = 0.0f,

        @JsonProperty("upper_left_y") val upperLeftY: Float = 0.0f,

        @JsonProperty("upper_right_x") val upperRightX: Float = 1.0f,

        @JsonProperty("upper_right_y") val upperRightY: Float = 0.0f
    )

    /**
     * 照片
     * @property aigcType AIGC类型
     * @property audioFade 音频淡入淡出
     * @property cartoonPath 卡通路径
     * @property categoryId 类别ID
     * @property categoryName 类别名称
     * @property checkFlag 检查标志
     * @property crop 裁剪
     * @property cropRatio 裁剪比例
     * @property cropScale 裁剪比例
     * @property duration 持续时间
     * @property extraTypeOption 额外类型选项
     * @property formulaId 公式ID
     * @property freeze 冻结
     * @property hasAudio 是否有音频
     * @property height 高度
     * @property id ID
     * @property intensifiesAudioPath 强化音频路径
     * @property intensifiesPath 强化路径
     * @property isAiGenerateContent 是否是AI生成内容
     * @property isCopyright 是否有版权
     * @property isTextEditOverdub 是否文本编辑配音
     * @property isUnifiedBeautyMode 是否统一美颜模式
     * @property localId 本地ID
     * @property localMaterialId 本地素材ID
     * @property materialId 素材ID
     * @property materialName 素材名称
     * @property materialUrl 素材URL
     * @property matting 抠图
     * @property mediaPath 媒体路径
     * @property objectLocked 对象锁定
     * @property originMaterialId 原始素材ID
     * @property path 路径
     * @property pictureFrom 图片来源
     * @property pictureSetCategoryId 图片集类别ID
     * @property pictureSetCategoryName 图片集类别名称
     * @property requestId 请求ID
     * @property reverseIntensifiesPath 反向强化路径
     * @property reversePath 反向路径
     * @property smartMotion 智能运动
     * @property source 来源
     * @property sourcePlatform 来源平台
     * @property stable 稳定
     * @property teamId 团队ID
     * @property type 类型
     * @property videoAlgorithm 视频算法
     * @property width 宽度
     * @author [Your Name]
     * @created 2024/10/09
     * @version 1.0
     * @since 1.0
     */
    data class Photo(
        @JsonProperty("aigc_type") val aigcType: String = "none",

        @JsonProperty("audio_fade") val audioFade: Float? = null,

        @JsonProperty("cartoon_path") val cartoonPath: String = "",

        @JsonProperty("category_id") val categoryId: String = "",

        @JsonProperty("category_name") val categoryName: String = "local",

        @JsonProperty("check_flag") val checkFlag: Int = 63487,

        val crop: Crop = Crop(),

        @JsonProperty("crop_ratio") val cropRatio: String = "free",

        @JsonProperty("crop_scale") val cropScale: Float = 1.0f,

        val duration: Long = 10800000000,

        @JsonProperty("extra_type_option") val extraTypeOption: Int = 0,

        @JsonProperty("formula_id") val formulaId: String = "",

        val freeze: Float? = null,

        @JsonProperty("has_audio") val hasAudio: Boolean = false,

        val height: Int = 1536,

        val id: String = uuid(
            upper = true, formats = mutableListOf(Pair(8, "-"), Pair(12, "-"), Pair(16, "-"), Pair(20, "-"))
        ),

        @JsonProperty("intensifies_audio_path") val intensifiesAudioPath: String = "",

        @JsonProperty("intensifies_path") val intensifiesPath: String = "",

        @JsonProperty("is_ai_generate_content") val isAiGenerateContent: Boolean = false,

        @JsonProperty("is_copyright") val isCopyright: Boolean = false,

        @JsonProperty("is_text_edit_overdub") val isTextEditOverdub: Boolean = false,

        @JsonProperty("is_unified_beauty_mode") val isUnifiedBeautyMode: Boolean = false,

        @JsonProperty("local_id") val localId: String = "",

        @JsonProperty("local_material_id") val localMaterialId: String = "",

        @JsonProperty("material_id") val materialId: String = "",

        @JsonProperty("material_name") val materialName: String = "",

        @JsonProperty("material_url") val materialUrl: String = "",

        val matting: Matting = Matting(),

        @JsonProperty("media_path") val mediaPath: String = "",

        @JsonProperty("object_locked") val objectLocked: Boolean? = null,

        @JsonProperty("origin_material_id") val originMaterialId: String = "",

        val path: String = "",

        @JsonProperty("picture_from") val pictureFrom: String = "none",

        @JsonProperty("picture_set_category_id") val pictureSetCategoryId: String = "",

        @JsonProperty("picture_set_category_name") val pictureSetCategoryName: String = "",

        @JsonProperty("request_id") val requestId: String = "",

        @JsonProperty("reverse_intensifies_path") val reverseIntensifiesPath: String = "",

        @JsonProperty("reverse_path") val reversePath: String = "",

        @JsonProperty("smart_motion") val smartMotion: Float? = null,

        val source: Int = 0,

        @JsonProperty("source_platform") val sourcePlatform: Int = 0,

        val stable: Stable = Stable(),

        @JsonProperty("team_id") val teamId: String = "",

        val type: String = "photo",

        @JsonProperty("video_algorithm") val videoAlgorithm: VideoAlgorithm = VideoAlgorithm(),

        val width: Int = 1024
    )

    /**
     * 稳定
     * @property matrixPath 矩阵路径
     * @property stableLevel 稳定等级
     * @property timeRange 时间范围
     * @author [Your Name]
     * @created 2024/10/09
     * @version 1.0
     * @since 1.0
     */
    data class Stable(
        @JsonProperty("matrix_path") val matrixPath: String = "",

        @JsonProperty("stable_level") val stableLevel: Int = 0,

        @JsonProperty("time_range") val timeRange: TimeRange = TimeRange()
    )

    /**
     * 算法
     * @property algorithmId 算法ID
     * @property type 类型
     * @author [Your Name]
     * @created 2024/10/09
     * @version 1.0
     * @since 1.0
     */
    data class Algorithm(
        @JsonProperty("algorithm_id") val algorithmId: String = "",

        val type: String = ""
    )

    /**
     * 降噪
     * @property level 等级
     * @author [Your Name]
     * @created 2024/10/09
     * @version 1.0
     * @since 1.0
     */
    data class NoiseReduction(
        val level: Int = 0
    )

    /**
     * 视频算法
     * @property algorithms 算法
     * @property complementFrameConfig 补帧配置
     * @property deflicker 去闪烁
     * @property gameplayConfigs 游戏配置
     * @property motionBlurConfig 运动模糊配置
     * @property noiseReduction 降噪
     * @property path 路径
     * @property qualityEnhance 质量增强
     * @property timeRange 时间范围
     * @author [Your Name]
     * @created 2024/10/09
     * @version 1.0
     * @since 1.0
     */
    data class VideoAlgorithm(
        val algorithms: List<Algorithm> = mutableListOf(),

        @JsonProperty("complement_frame_config") val complementFrameConfig: String? = null,

        val deflicker: String? = null,

        @JsonProperty("gameplay_configs") val gameplayConfigs: List<String> = mutableListOf(),

        @JsonProperty("motion_blur_config") val motionBlurConfig: String? = null,

        @JsonProperty("noise_reduction") val noiseReduction: NoiseReduction? = null,

        val path: String = "",

        @JsonProperty("quality_enhance") val qualityEnhance: String? = null,

        @JsonProperty("time_range") val timeRange: TimeRange? = null
    )

    /**
     * 抠图
     * @property flag 标志
     * @property hasUseQuickBrush 是否使用快速刷
     * @property hasUseQuickEraser 是否使用快速橡皮擦
     * @property interactiveTime 交互时间
     * @property path 路径
     * @property strokes 笔触
     * @author [Your Name]
     * @created 2024/10/09
     * @version 1.0
     * @since 1.0
     */
    data class Matting(
        val flag: Int = 0,

        @JsonProperty("has_use_quick_brush") val hasUseQuickBrush: Boolean = false,

        @JsonProperty("has_use_quick_eraser") val hasUseQuickEraser: Boolean = false,

        val interactiveTime: List<Int> = mutableListOf(),

        val path: String = "",

        val strokes: List<String> = mutableListOf()
    )

    /**
     * 速度配置
     * @property curveSpeed 曲线速度
     * @property id ID
     * @property mode 模式
     * @property speed 速度
     * @property type 类型
     * @author [Your Name]
     * @created 2024/10/09
     * @version 1.0
     * @since 1.0
     */
    data class SpeedConfig(
        @JsonProperty("curve_speed") val curveSpeed: Float? = null,

        val id: String = uuid(
            upper = true, formats = mutableListOf(Pair(8, "-"), Pair(12, "-"), Pair(16, "-"), Pair(20, "-"))
        ),

        val mode: Int = 0,

        val speed: Float = 1.0f,

        val type: String = "speed"
    )

    /**
     * 数字人
     * @property background 背景
     * @property digitalHumanId 数字人ID
     * @property digitalHumanSource 数字人来源
     * @property entrance 入口
     * @property id ID
     * @property localTaskId 本地任务ID
     * @property mask 遮罩
     * @property resourceId 资源ID
     * @property ttsMetas TTS元数据列表
     * @property type 类型
     * @property videoMeta 视频元数据
     * @property voiceInfo 语音信息
     * @author [Your Name]
     * @created 2024/10/09
     * @version 1.0
     * @since 1.0
     */
    data class DigitalHuman(
        val background: String? = null,
        @JsonProperty("digital_human_id") val digitalHumanId: String? = null,
        @JsonProperty("digital_human_source") val digitalHumanSource: String? = null,
        val entrance: String? = null,
        val id: String? = null,
        @JsonProperty("local_task_id") val localTaskId: String? = null,
        val mask: String? = null,
        @JsonProperty("resource_id") val resourceId: String? = null,
        @JsonProperty("tts_metas") val ttsMetas: List<TTSMeta>? = null,
        val type: String? = null,
        @JsonProperty("video_meta") val videoMeta: VideoMeta? = null,
        @JsonProperty("voice_info") val voiceInfo: VoiceInfo? = null
    )

    /**
     * TTS元数据
     * @property text 文本内容
     * @property textSegId 文本段ID
     * @property ttsPath TTS音频路径
     * @property ttsPayload TTS负载信息
     * @property ttsStart TTS开始时间
     * @author [Your Name]
     * @created 2024/10/09
     * @version 1.0
     * @since 1.0
     */
    data class TTSMeta(
        val text: String,
        @JsonProperty("text_seg_id") val textSegId: String,
        @JsonProperty("tts_path") val ttsPath: String,
        @JsonProperty("tts_payload") val ttsPayload: String,
        @JsonProperty("tts_start") val ttsStart: Int
    )

    data class VideoMeta(
        val path: String? = null,
    )

    /**
     * 语音信息
     * @property isAiCloneTone 是否为AI克隆音调
     * @property isUgc 是否为UGC
     * @property resourceId 资源ID
     * @property speakerId 说话者ID
     * @property speed 语速
     * @property toneCategoryId 音调类别ID
     * @property toneCategoryName 音调类别名称
     * @property toneEffectId 音效ID
     * @property toneEffectName 音效名称
     * @property tonePlatform 音调平台
     * @property toneSecondCategoryId 音调二级类别ID
     * @property toneSecondCategoryName 音调二级类别名称
     * @property toneType 音调类型
     * @author [Your Name]
     * @created 2024/10/09
     * @version 1.0
     * @since 1.0
     */
    data class VoiceInfo(
        @JsonProperty("is_ai_clone_tone") val isAiCloneTone: Boolean,
        @JsonProperty("is_ugc") val isUgc: Boolean,
        @JsonProperty("resource_id") val resourceId: String,
        @JsonProperty("speaker_id") val speakerId: String,
        val speed: Float,
        @JsonProperty("tone_category_id") val toneCategoryId: String,
        @JsonProperty("tone_category_name") val toneCategoryName: String,
        @JsonProperty("tone_effect_id") val toneEffectId: String,
        @JsonProperty("tone_effect_name") val toneEffectName: String,
        @JsonProperty("tone_platform") val tonePlatform: String,
        @JsonProperty("tone_second_category_id") val toneSecondCategoryId: String,
        @JsonProperty("tone_second_category_name") val toneSecondCategoryName: String,
        @JsonProperty("tone_type") val toneType: String
    )

    /**
     * 字幕模板信息
     * @property categoryId 分类ID
     * @property categoryName 分类名称
     * @property effectId 效果ID
     * @property isNew 是否新建
     * @property path 路径
     * @property requestId 请求ID
     * @property resourceId 资源ID
     * @property resourceName 资源名称
     * @property sourcePlatform 来源平台
     * @author [Your Name]
     * @created 2024/10/09
     * @version 1.0
     * @since 1.0
     */
    data class CaptionTemplateInfo(
        @JsonProperty("category_id") val categoryId: String = "",

        @JsonProperty("category_name") val categoryName: String = "",

        @JsonProperty("effect_id") val effectId: String = "",

        @JsonProperty("is_new") val isNew: Boolean = false,

        val path: String = "",

        @JsonProperty("request_id") val requestId: String = "",

        @JsonProperty("resource_id") val resourceId: String = "",

        @JsonProperty("resource_name") val resourceName: String = "",

        @JsonProperty("source_platform") val sourcePlatform: Int = 0
    )

    /**
     * 文本材料字体
     * @property categoryId 分类ID
     * @property categoryName 分类名称
     * @property effectId 效果ID
     * @property fileUri 文件URI
     * @property id Id
     * @property path 路径
     * @property requestId 请求ID
     * @property resourceId 资源ID
     * @property sourcePlatform 来源平台
     * @property teamId 团队ID
     * @property title 标题
     * @author [Your Name]
     * @created 2024/10/09
     * @version 1.0
     * @since 1.0
     */
    data class TextMaterialFont(
        @JsonProperty("category_id") val categoryId: String,

        @JsonProperty("category_name") val categoryName: String,

        @JsonProperty("effect_id") val effectId: String,

        @JsonProperty("file_uri") val fileUri: String,

        val id: String,

        val path: String,

        @JsonProperty("request_id") val requestId: String,

        @JsonProperty("resource_id") val resourceId: String,

        @JsonProperty("source_platform") val sourcePlatform: Int,

        @JsonProperty("team_id") val teamId: String,

        val title: String
    )

    /**
     * 文本材料
     * @property addType 添加类型
     * @property alignment 对齐
     * @property backgroundAlpha 背景透明度
     * @property backgroundColor 背景颜色
     * @property backgroundHeight 背景高度
     * @property backgroundHorizontalOffset 背景水平偏移
     * @property backgroundRoundRadius 背景圆角半径
     * @property backgroundStyle 背景样式
     * @property backgroundVerticalOffset 背景垂直偏移
     * @property backgroundWidth 背景宽度
     * @property baseContent 基础内容
     * @property boldWidth 粗体宽度
     * @property borderAlpha 边框透明度
     * @property borderColor 边框颜色
     * @property borderWidth 边框宽度
     * @property captionTemplateInfo 字幕模板信息
     * @property checkFlag 检查标志
     * @property comboInfo 组合信息
     * @property content 内容,TextContent类的json字符串
     * @property fixedHeight 固定高度
     * @property fixedWidth 固定宽度
     * @property fontCategoryId 字体分类ID
     * @property fontCategoryName 字体分类名称
     * @property fontId 字体ID
     * @property fontName 字体名称
     * @property fontPath 字体路径
     * @property fontResourceId 字体资源ID
     * @property fontSize 字体大小
     * @property fontSourcePlatform 字体来源平台
     * @property fontTeamId 字体团队ID
     * @property fontTitle 字体标题
     * @property fontUrl 字体URL
     * @property fonts 字体
     * @property forceApplyLineMaxWidth 强制应用行最大宽度
     * @property globalAlpha 全局透明度
     * @property groupId 组ID
     * @property hasShadow 有阴影
     * @property id ID
     * @property initialScale 初始缩放
     * @property innerPadding 内部填充
     * @property isRichText 是否富文本
     * @property italicDegree 斜体角度
     * @property ktvColor KTV颜色
     * @property language 语言
     * @property layerWeight 层权重
     * @property letterSpacing 字母间距
     * @property lineFeed 换行
     * @property lineMaxWidth 行最大宽度
     * @property lineSpacing 行间距
     * @property multiLanguageCurrent 多语言当前状态
     * @property name 名称
     * @property originalSize 原始尺寸
     * @property presetCategory 预设分类
     * @property presetCategoryId 预设分类ID
     * @property presetHasSetAlignment 预设已设置对齐
     * @property presetId 预设ID
     * @property presetIndex 预设索引
     * @property presetName 预设名称
     * @property recognizeTaskId 识别任务ID
     * @property recognizeType 识别类型
     * @property relevanceSegment 相关片段
     * @property shadowAlpha 阴影透明度
     * @property shadowAngle 阴影角度
     * @property shadowColor 阴影颜色
     * @property shadowDistance 阴影距离
     * @property shadowPoint 阴影点
     * @property shadowSmoothing 阴影平滑
     * @property shapeClipX 形状剪辑X
     * @property shapeClipY 形状剪辑Y
     * @property sourceFrom 来源
     * @property styleName 样式名称
     * @property subType 子类型
     * @property subtitleKeywords 字幕关键词
     * @property subtitleTemplateOriginalFontsize 字幕模板原始字体大小
     * @property textAlpha 文本透明度
     * @property textColor 文本颜色
     * @property textCurve 文本曲线
     * @property textPresetResourceId 文本预设资源ID
     * @property textSize 文本大小
     * @property textToAudioIds 文本到音频ID
     * @property ttsAutoUpdate TTS自动更新
     * @property type 类型
     * @property typesetting 排版
     * @property underline 下划线
     * @property underlineOffset 下划线偏移
     * @property underlineWidth 下划线宽度
     * @property useEffectDefaultColor 使用效果默认颜色
     * @property words 单词
     * @author [Your Name]
     * @created 2024/10/09
     * @version 1.0
     * @since 1.0
     */
    data class TextMaterial(
        @JsonProperty("add_type") val addType: Int = 0,
        val alignment: Int = 1,
        @JsonProperty("background_alpha") val backgroundAlpha: Float = 1.0f,
        @JsonProperty("background_color") val backgroundColor: String = "",
        @JsonProperty("background_height") val backgroundHeight: Float = 0.14f,
        @JsonProperty("background_horizontal_offset") val backgroundHorizontalOffset: Float = 0.0f,
        @JsonProperty("background_round_radius") val backgroundRoundRadius: Float = 0.0f,
        @JsonProperty("background_style") val backgroundStyle: Int = 0,
        @JsonProperty("background_vertical_offset") val backgroundVerticalOffset: Float = 0.0f,
        @JsonProperty("background_width") val backgroundWidth: Float = 0.14f,
        @JsonProperty("base_content") val baseContent: String = "",
        @JsonProperty("bold_width") val boldWidth: Float = 0.0f,
        @JsonProperty("border_alpha") val borderAlpha: Float = 1.0f,
        @JsonProperty("border_color") val borderColor: String = "",
        @JsonProperty("border_width") val borderWidth: Float = 0.08f,
        @JsonProperty("caption_template_info") val captionTemplateInfo: CaptionTemplateInfo = CaptionTemplateInfo(),
        @JsonProperty("check_flag") val checkFlag: Int = 7,
        @JsonProperty("combo_info") val comboInfo: ComboInfo = ComboInfo(),
        val content: String = "{\"styles\":[{\"fill\":{\"alpha\":1.0,\"content\":{\"render_type\":\"solid\",\"solid\":{\"alpha\":1.0,\"color\":[1.0,1.0,1.0]}}},\"font\":{\"id\":\"\",\"path\":\"D:/Program Files/JianyingPro5.9.0/5.9.0.11632/Resources/Font/SystemFont/zh-hans.ttf\"},\"range\":[0,4],\"size\":15.0}],\"text\":\"默认文本\"}",
        @JsonProperty("fixed_height") val fixedHeight: Float = -1.0f,
        @JsonProperty("fixed_width") val fixedWidth: Float = -1.0f,
        @JsonProperty("font_category_id") val fontCategoryId: String = "",
        @JsonProperty("font_category_name") val fontCategoryName: String = "",
        @JsonProperty("font_id") val fontId: String = "",
        @JsonProperty("font_name") val fontName: String = "",
        @JsonProperty("font_path") val fontPath: String = "D:/Program Files/JianyingPro5.9.0/5.9.0.11632/Resources/Font/SystemFont/zh-hans.ttf",
        @JsonProperty("font_resource_id") val fontResourceId: String = "",
        @JsonProperty("font_size") val fontSize: Float? = null,
        @JsonProperty("font_source_platform") val fontSourcePlatform: Int = 0,
        @JsonProperty("font_team_id") val fontTeamId: String = "",
        @JsonProperty("font_title") val fontTitle: String = "none",
        @JsonProperty("font_url") val fontUrl: String = "",
        val fonts: List<TextMaterialFont> = mutableListOf(),
        @JsonProperty("force_apply_line_max_width") val forceApplyLineMaxWidth: Boolean = false,
        @JsonProperty("global_alpha") val globalAlpha: Float = 1.0f,
        @JsonProperty("group_id") val groupId: String = "",
        @JsonProperty("has_shadow") val hasShadow: Boolean = false,
        val id: String = uuid(
            upper = true, formats = mutableListOf(Pair(8, "-"), Pair(12, "-"), Pair(16, "-"), Pair(20, "-"))
        ),
        @JsonProperty("initial_scale") val initialScale: Float = 1.0f,
        @JsonProperty("inner_padding") val innerPadding: Float = -1.0f,
        @JsonProperty("is_rich_text") val isRichText: Boolean = false,
        @JsonProperty("italic_degree") val italicDegree: Int = 0,
        @JsonProperty("ktv_color") val ktvColor: String = "",
        val language: String = "",
        @JsonProperty("layer_weight") val layerWeight: Int = 1,
        @JsonProperty("letter_spacing") val letterSpacing: Float = 0.0f,
        @JsonProperty("line_feed") val lineFeed: Int = 1,
        @JsonProperty("line_max_width") val lineMaxWidth: Float = 0.82f,
        @JsonProperty("line_spacing") val lineSpacing: Float = 0.02f,
        @JsonProperty("multi_language_current") val multiLanguageCurrent: String = "none",
        val name: String = "",
        @JsonProperty("original_size") val originalSize: List<String> = mutableListOf(),
        @JsonProperty("preset_category") val presetCategory: String = "",
        @JsonProperty("preset_category_id") val presetCategoryId: String = "",
        @JsonProperty("preset_has_set_alignment") val presetHasSetAlignment: Boolean = false,
        @JsonProperty("preset_id") val presetId: String = "",
        @JsonProperty("preset_index") val presetIndex: Int = 0,
        @JsonProperty("preset_name") val presetName: String = "",
        @JsonProperty("recognize_task_id") val recognizeTaskId: String = "",
        @JsonProperty("recognize_type") val recognizeType: Int = 0,
        @JsonProperty("relevance_segment") val relevanceSegment: List<String> = mutableListOf(),
        @JsonProperty("shadow_alpha") val shadowAlpha: Float = 0.9f,
        @JsonProperty("shadow_angle") val shadowAngle: Float = -45.0f,
        @JsonProperty("shadow_color") val shadowColor: String = "",
        @JsonProperty("shadow_distance") val shadowDistance: Float = 5.0f,
        @JsonProperty("shadow_point") val shadowPoint: ShadowPoint = ShadowPoint(),
        @JsonProperty("shadow_smoothing") val shadowSmoothing: Float = 0.45f,
        @JsonProperty("shape_clip_x") val shapeClipX: Boolean = false,
        @JsonProperty("shape_clip_y") val shapeClipY: Boolean = false,
        @JsonProperty("source_from") val sourceFrom: String = "",
        @JsonProperty("style_name") val styleName: String = "",
        @JsonProperty("sub_type") val subType: Int = 0,
        @JsonProperty("subtitle_keywords") val subtitleKeywords: String? = null,
        @JsonProperty("subtitle_template_original_fontsize") val subtitleTemplateOriginalFontsize: Float = 0.0f,
        @JsonProperty("text_alpha") val textAlpha: Float = 1.0f,
        @JsonProperty("text_color") val textColor: String = "#FFFFFF",
        @JsonProperty("text_curve") val textCurve: String? = null,
        @JsonProperty("text_preset_resource_id") val textPresetResourceId: String = "",
        @JsonProperty("text_size") val textSize: Int = 30,
        @JsonProperty("text_to_audio_ids") val textToAudioIds: List<String> = mutableListOf(),
        @JsonProperty("tts_auto_update") val ttsAutoUpdate: Boolean = false,
        val type: String = "text",
        val typesetting: Int = 0,
        val underline: Boolean = false,
        @JsonProperty("underline_offset") val underlineOffset: Float = 0.22f,
        @JsonProperty("underline_width") val underlineWidth: Float = 0.05f,
        @JsonProperty("use_effect_default_color") val useEffectDefaultColor: Boolean = true,
        val words: Words = Words()
    )

    /**
     * 组合信息
     * @property textTemplates 文本模板
     * @author [Your Name]
     * @created 2024/10/09
     * @version 1.0
     * @since 1.0
     */
    data class ComboInfo(
        @JsonProperty("text_templates") val textTemplates: List<String> = mutableListOf()
    )

    /**
     * 阴影点
     * @property x x轴阴影点
     * @property y y轴阴影点
     * @author [Your Name]
     * @created 2024/10/09
     * @version 1.0
     * @since 1.0
     */
    data class ShadowPoint(
        val x: Float = 0.6363961030678928f, val y: Float = -0.6363961030678928f
    )

    /**
     * 词语
     * @property endTime 结束时间
     * @property startTime 开始时间
     * @property text 文本
     * @author [Your Name]
     * @created 2024/10/09
     * @version 1.0
     * @since 1.0
     */
    data class Words(
        @JsonProperty("end_time") val endTime: List<String> = mutableListOf(),

        @JsonProperty("start_time") val startTime: List<String> = mutableListOf(),

        val text: List<String> = mutableListOf()
    )

    /**
     * 实体
     * @property alpha 透明度
     * @property color 颜色
     * @author [Your Name]
     * @created 2024/10/09
     * @version 1.0
     * @since 1.0
     */
    data class Solid(
        val alpha: Float? = null, val color: List<Int> = mutableListOf(1, 1, 1)
    )

    /**
     * 画布
     * @property albumImage 专辑图像
     * @property blur 模糊度
     * @property color 颜色
     * @property id ID
     * @property image 图像
     * @property imageId 图像ID
     * @property imageName 图像名称
     * @property sourcePlatform 来源平台
     * @property teamId 团队ID
     * @property type 类型
     * @author [Your Name]
     * @created 2024/10/09
     * @version 1.0
     * @since 1.0
     */
    data class Canvas(
        @JsonProperty("album_image") val albumImage: String = "",
        val blur: Float = 0.0f,
        val color: String = "",
        val id: String = uuid(
            upper = true, formats = mutableListOf(Pair(8, "-"), Pair(12, "-"), Pair(16, "-"), Pair(20, "-"))
        ),
        val image: String = "",
        @JsonProperty("image_id") val imageId: String = "",
        @JsonProperty("image_name") val imageName: String = "",
        @JsonProperty("source_platform") val sourcePlatform: Int = 0,
        @JsonProperty("team_id") val teamId: String = "",
        val type: String = "canvas_color"
    )


    /**
     * 样式
     * @property fill 填充
     * @property font 字体
     * @property range 范围
     * @property size 大小
     * @property strokes 笔触
     * @property useLetterColor 使用字母颜色
     * @author [Your Name]
     * @created 2024/10/09
     * @version 1.0
     * @since 1.0
     */
    data class Style(
        val fill: Fill = Fill(),

        val font: Font = Font(),

        val range: List<Int> = mutableListOf(0, 4),

        val size: Float = 15.0f,

        val strokes: List<Fill>? = null,

        @JsonProperty("useLetterColor")
        val useLetterColor: Boolean? = null
    )

    /**
     * 填充
     * @property alpha 透明度
     * @property content 内容
     * @property width 宽度
     * @author [Your Name]
     * @created 2024/10/09
     * @version 1.0
     * @since 1.0
     */
    data class Fill(
        val alpha: Float? = null,

        val content: Content = Content(),

        val width: Float? = null
    )

    /**
     * 字体
     * @property id 字体ID
     * @property path 字体路径
     * @author [Your Name]
     * @created 2024/10/09
     * @version 1.0
     * @since 1.0
     */
    data class Font(
        val id: String = "",

        val path: String = "D:/Program Files/JianyingPro5.9.0/5.9.0.11632/Resources/Font/SystemFont/zh-hans.ttf"
    )

    /**
     * 文本内容
     * @property styles 样式
     * @property text 文本
     * @author [Your Name]
     * @created 2024/10/09
     * @version 1.0
     * @since 1.0
     */
    data class TextContent(
        val styles: List<Style> = mutableListOf(),

        val text: String = "默认文本"
    )

    data class Draft(
        val name: String,
        val meta: MetaInfo = MetaInfo(),
        val content: Content = Content(),
        var files: Files? = null
    ) {


        data class Files(
            val metaJsonFile: File? = null,
            val contentJsonFile: File? = null,
        )
    }
}

data class ClickUiElementRequest(
    val locators: String,
    val index: Int? = null,
    val timeout: Int = 30
)
data class ClickLocationRequest(
    val x: Int,
    val y: Int,
    val times: Int = 1
)


data class TypeTextRequest(
    val locators: String,
    val text: String,
    val timeout: Int = 30,
)

data class SetClickniumLicenseRequest(
    @JsonProperty("license_str")
    val licenseStr: String
)

data class WindowMaximizeRequest(
    val locators: String,
    @JsonProperty("max_btn_locator")
    val maxBtnLocator: String,
    val timeout: Int = 30
)

data class KeyRequest(
    val key: String
)

data class ClickImageRequest(
    @JsonProperty("img_path")
    val imgPath: String,

    val confidence: Float = 0.8f,

    val times: Int = 1
)


data class Result(
    val success: Boolean,
    val message: String? = null,
    val data: Any? = null,
    val error: String? = null,
)
