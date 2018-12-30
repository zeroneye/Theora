package xieao.theora.common.item.slates;

import net.minecraft.item.ItemStack;
import xieao.lib.item.ItemBase;
import xieao.theora.api.item.slate.IXPSlate;

public class ItemSlateXP extends ItemBase implements IXPSlate {

    @Override
    public Enum<?>[] getSubTypes() {
        return Level.values();
    }

    @Override
    public float getLiquidCost(ItemStack stack) {
        return (Level.values()[stack.getMetadata()].ordinal() + 1) * 3.0F;
    }

    @Override
    public int getXPMultiplier(ItemStack stack) {
        return Level.values()[stack.getMetadata()].ordinal() + 1;
    }

    public enum Level {
        LEVEL_1, LEVEL_2
    }
}