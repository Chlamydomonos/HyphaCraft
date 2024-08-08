package xyz.chlamydomonos.hyphacraft.datagen

import net.minecraft.core.HolderLookup
import net.minecraft.data.PackOutput
import net.minecraft.data.recipes.RecipeCategory
import net.minecraft.data.recipes.RecipeOutput
import net.minecraft.data.recipes.RecipeProvider
import net.minecraft.data.recipes.ShapelessRecipeBuilder
import net.minecraft.world.item.Item
import xyz.chlamydomonos.hyphacraft.loaders.BlockLoader
import xyz.chlamydomonos.hyphacraft.loaders.ItemLoader
import xyz.chlamydomonos.hyphacraft.utils.NameUtil
import java.util.concurrent.CompletableFuture

class ModRecipeProvider(
    output: PackOutput,
    registries: CompletableFuture<HolderLookup.Provider>
) : RecipeProvider(output, registries) {
    override fun buildRecipes(recipeOutput: RecipeOutput) {
        nineToOne(ItemLoader.ROTTEN_FUNGUS_BALL, BlockLoader.ROTTEN_FUNGUS_HEAP.item).save(recipeOutput)
        nineToOne(ItemLoader.SPORE_POWDER, BlockLoader.SPORE_HEAP.item).save(recipeOutput)
        nineToOne(ItemLoader.HUMUS, BlockLoader.HUMUS_HEAP.item).save(recipeOutput)
    }

    private fun nineToOne(ingredient: Item, output: Item): ShapelessRecipeBuilder {
        return ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, output)
            .requires(ingredient, 9)
            .unlockedBy("has_${NameUtil.path(ingredient)}", has(ingredient))
    }
}