package xieao.theora.core;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;
import xieao.theora.block.cauldron.TileCauldron;
import xieao.theora.block.heat.TileHeat;
import xieao.theora.core.lib.annotation.PreLoad;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

@PreLoad
public class ITiles {
    public static final Map<ResourceLocation, TileEntityType<?>> TYPES = new HashMap<>();
    public static final TileEntityType<?> HEAT;
    public static final TileEntityType<?> CAULDRON;

    static {
        HEAT = register("heat", TileHeat::new);
        CAULDRON = register("cauldron", TileCauldron::new);
    }

    @SuppressWarnings({"unchecked", "ConstantConditions"})
    static <T extends TileEntity> TileEntityType<T> register(String id, Supplier<? extends T> factoryIn) {
        TileEntityType<T> tileentitytype = (TileEntityType<T>) TileEntityType.Builder
                .create(factoryIn).build(null);
        tileentitytype.setRegistryName(id);
        ForgeRegistries.TILE_ENTITIES.register(tileentitytype);
        TYPES.put(tileentitytype.getRegistryName(), tileentitytype);
        return tileentitytype;
    }
}
