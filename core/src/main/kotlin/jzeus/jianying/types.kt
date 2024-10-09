package jzeus.jianying

import com.fasterxml.jackson.annotation.JsonProperty
import jzeus.uuid.uuid

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
        val draftCloudMaterials: List<Any> = listOf(),

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
        val draftId: String =  uuid(upper = true, formats = listOf(Pair(8, "-"), Pair(12, "-"), Pair(16, "-"), Pair(20, "-"))),

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
        val draftMaterials: List<Material> = listOf(),

        @JsonProperty("draft_materials_copied_info")
        val draftMaterialsCopiedInfo: List<Any> = listOf(),

        @JsonProperty("draft_name")
        val draftName: String? = null,

        @JsonProperty("draft_new_version")
        val draftNewVersion: String = "",

        @JsonProperty("draft_removable_storage_device")
        val draftRemovableStorageDevice: String = "D:",

        @JsonProperty("draft_root_path")
        val draftRootPath: String? = null,

        @JsonProperty("draft_segment_extra_info")
        val draftSegmentExtraInfo: List<Any> = listOf(),

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
        @JsonProperty("draft_enterprise_extra")
        val draftEnterpriseExtra: String? = null,

        @JsonProperty("draft_enterprise_id")
        val draftEnterpriseId: String? = null,

        @JsonProperty("draft_enterprise_name")
        val draftEnterpriseName: String? = null,

        @JsonProperty("enterprise_material")
        val enterpriseMaterial: List<Any>? = null
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
        val duration: Int? = null,
        val start: Int? = null
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
        @JsonProperty("create_time")
        val createTime: Long? = null,

        @JsonProperty("duration")
        val duration: Int? = null,

        @JsonProperty("extra_info")
        val extraInfo: String? = null,

        @JsonProperty("file_Path")
        val filePath: String? = null,

        @JsonProperty("height")
        val height: Int? = null,

        @JsonProperty("id")
        val id: String? = null,

        @JsonProperty("import_time")
        val importTime: Long? = null,

        @JsonProperty("import_time_ms")
        val importTimeMs: Long? = null,

        @JsonProperty("item_source")
        val itemSource: Any? = null,

        @JsonProperty("md5")
        val md5: String? = null,

        @JsonProperty("metetype")
        val metetype: String? = null,

        @JsonProperty("roughcut_time_range")
        val roughcutTimeRange: TimeRange? = null,

        @JsonProperty("sub_time_range")
        val subTimeRange: TimeRange? = null,

        @JsonProperty("type")
        val type: Int? = null,

        @JsonProperty("width")
        val width: Int? = null
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
        @JsonProperty("type")
        val type: Int? = null,

        @JsonProperty("value")
        val value: List<ImageMaterial>? = null
    )
}
