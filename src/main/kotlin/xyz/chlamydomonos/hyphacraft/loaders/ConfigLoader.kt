package xyz.chlamydomonos.hyphacraft.loaders

import net.neoforged.fml.ModLoadingContext
import net.neoforged.fml.config.ModConfig
import net.neoforged.neoforge.common.ModConfigSpec
import xyz.chlamydomonos.hyphacraft.utils.NameUtil

object ConfigLoader {
    private val BUILDER = ModConfigSpec.Builder()

    private val PLANTS_BUILDER = BUILDER
        .comment("Plants Config")
        .push("plants")

    val XENOLICHEN_TAGS = PLANTS_BUILDER
        .comment("Tags of blocks erodible by Xenolichen")
        .defineListAllowEmpty("xenolichen_tags", mutableListOf(
            "minecraft:base_stone_overworld",
            "minecraft:sand",
            "c:ores"
        ), NameUtil::validateTagName)

    val XENOLICHEN_BLOCKS = PLANTS_BUILDER
        .comment("Additional blocks erodible by Xenolichen besides xenolichen_tags")
        .defineListAllowEmpty("xenolichen_blocks", mutableListOf(
            "minecraft:sandstone",
            "minecraft:red_sandstone",
            "minecraft:gravel",
            "minecraft:cobblestone",
            "minecraft:cobbled_deepslate",
            "minecraft:mossy_cobblestone"
        ), NameUtil::validateBlockName)

    val XENOLICHEN_BLACKLIST = PLANTS_BUILDER
        .comment("Block blacklist for Xenolichen")
        .defineListAllowEmpty("xenolichen_blacklist", mutableListOf<String>(), NameUtil::validateBlockName)

    val MYCOVASTUS_TAGS = PLANTS_BUILDER
        .comment("Tags of blocks erodible by Mycovastus")
        .defineListAllowEmpty("mycovastus_tags", mutableListOf(
            "minecraft:dirt"
        ), NameUtil::validateTagName)

    val MYCOVASTUS_BLOCKS = PLANTS_BUILDER
        .comment("Additional blocks erodible by Mycovastus besides mycovastus_tags")
        .defineListAllowEmpty("mycovastus_blocks", mutableListOf(
            "minecraft:moss_block",
            "minecraft:clay"
        ), NameUtil::validateBlockName)

    val MYCOVASTUS_BLACKLIST = PLANTS_BUILDER
        .comment("Block blacklist for Mycovastus")
        .defineListAllowEmpty("mycovastus_blacklist", mutableListOf<String>(), NameUtil::validateBlockName)

    val TUMIDUSIO_TAGS = PLANTS_BUILDER
        .comment("Tags of blocks erodible by Tumidusio")
        .defineListAllowEmpty("tumidusio_tags", mutableListOf(
            "minecraft:logs_that_burn",
            "minecraft:leaves"
        ), NameUtil::validateTagName)

    val TUMIDUSIO_BLOCKS = PLANTS_BUILDER
        .comment("Additional blocks erodible by Tumidusio besides tumidusio_tags")
        .defineListAllowEmpty("tumidusio_blocks", mutableListOf(
            "minecraft:pumpkin"
        ), NameUtil::validateBlockName)

    val TUMIDUSIO_BLACKLIST = PLANTS_BUILDER
        .comment("Block blacklist for Tumidusio")
        .defineListAllowEmpty("tumidusio_blacklist", mutableListOf<String>(), NameUtil::validateBlockName)

    private val MISC_BUILDER = PLANTS_BUILDER.pop()
        .comment("Miscellaneous Config")
        .push("misc")

    val ALIEN_SOIL_DESTROYED = MISC_BUILDER
        .comment("Blocks that will be destroyed when alien soil generates below")
        .defineListAllowEmpty("alien_soil_destroyed", mutableListOf(
            "minecraft:moss_carpet"
        ), NameUtil::validateBlockName)

    private val FINAL_BUILDER = MISC_BUILDER.pop()

    fun register(context: ModLoadingContext) {
        val container = context.activeContainer
        container.registerConfig(ModConfig.Type.COMMON, FINAL_BUILDER.build())
    }
}