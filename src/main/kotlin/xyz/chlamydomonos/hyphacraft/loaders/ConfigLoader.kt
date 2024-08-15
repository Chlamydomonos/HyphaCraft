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

    val XENOLICHEN_BLACKLIST = PLANTS_BUILDER
        .comment("Block blacklist for Xenolichen")
        .defineListAllowEmpty("xenolichen_blacklist", mutableListOf<String>(), NameUtil::validateBlockName)

    val MYCOVASTUS_BLACKLIST = PLANTS_BUILDER
        .comment("Block blacklist for Mycovastus")
        .defineListAllowEmpty("mycovastus_blacklist", mutableListOf<String>(), NameUtil::validateBlockName)

    val TUMIDUSIO_BLACKLIST = PLANTS_BUILDER
        .comment("Block blacklist for Tumidusio")
        .defineListAllowEmpty("tumidusio_blacklist", mutableListOf<String>(), NameUtil::validateBlockName)

    val CARNIVORAVITIS_AFFECT_CREATIVE_PLAYER = PLANTS_BUILDER
        .comment("Whether Carnivoravitis affects creative player")
        .define("carnivoravitis_affect_creative_player", false)

    val PULVERIUM_AFFECT_CREATIVE_PLAYER = PLANTS_BUILDER
        .comment("Whether Pulverium affects creative player")
        .define("pulverium_affect_creative_player", false)

    val FULGURFUNGUS_AFFECT_CREATIVE_PLAYER = PLANTS_BUILDER
        .comment("Whether Fulgurfungus affects creative player")
        .define("fulgurfungus_affect_creative_player", false)

    private val FINAL_BUILDER = PLANTS_BUILDER.pop()

    fun register(context: ModLoadingContext) {
        val container = context.activeContainer
        container.registerConfig(ModConfig.Type.COMMON, FINAL_BUILDER.build())
    }
}