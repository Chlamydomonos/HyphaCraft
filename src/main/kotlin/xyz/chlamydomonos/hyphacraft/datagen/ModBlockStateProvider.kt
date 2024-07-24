package xyz.chlamydomonos.hyphacraft.datagen

import net.minecraft.data.PackOutput
import net.neoforged.neoforge.client.model.generators.BlockStateProvider
import net.neoforged.neoforge.common.data.ExistingFileHelper
import xyz.chlamydomonos.hyphacraft.HyphaCraft
import xyz.chlamydomonos.hyphacraft.loaders.BlockLoader

class ModBlockStateProvider(output: PackOutput, exFileHelper: ExistingFileHelper) :
    BlockStateProvider(output, HyphaCraft.MODID, exFileHelper) {
    override fun registerStatesAndModels() {
        simpleBlockWithItem(BlockLoader.ALIEN_ROCK.block, cubeAll(BlockLoader.ALIEN_ROCK.block))
    }
}