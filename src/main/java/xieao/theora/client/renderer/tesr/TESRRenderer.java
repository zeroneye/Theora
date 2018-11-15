package xieao.theora.client.renderer.tesr;

import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import xieao.theora.common.block.binding.TileBindingCenter;
import xieao.theora.common.block.binding.TileBindingRing;
import xieao.theora.common.block.cauldron.TileCauldron;

public class TESRRenderer {

    public static void register() {
        bindTESR(TileCauldron.class, new RenderCauldron());
        bindTESR(TileBindingCenter.class, new RenderBindingStone());
        bindTESR(TileBindingRing.class, new RenderBindingRing());
    }

    private static <T extends TileEntity> void bindTESR(Class<T> tileEntityClass, TileEntitySpecialRenderer<? super T> specialRenderer) {
        ClientRegistry.bindTileEntitySpecialRenderer(tileEntityClass, specialRenderer);
    }
}
