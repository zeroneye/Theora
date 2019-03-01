package xieao.theora.client.gui.inventory;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumHand;
import net.minecraftforge.fml.network.FMLPlayMessages;
import xieao.theora.world.IInteractObj;

import javax.annotation.Nullable;

public class GuiFactory {
    @Nullable
    public static GuiScreen get(FMLPlayMessages.OpenContainer openContainer) {
        EntityPlayer player = Minecraft.getInstance().player;
        PacketBuffer buffer = openContainer.getAdditionalData();
        String str = buffer.readString(32767);
        if (str.equals("tile.gui")) {
            TileEntity tileEntity = Minecraft.getInstance().world.getTileEntity(buffer.readBlockPos());
            if (tileEntity instanceof IInteractObj) {
                IInteractObj obj = (IInteractObj) tileEntity;
                String guiId = obj.getGuiID();
                if (guiId.equals(openContainer.getId().toString())) {
                    return obj.getGui(player, EnumHand.MAIN_HAND);
                }
            }
        } else if (str.equals("item.gui")) {
            EnumHand hand = buffer.readEnumValue(EnumHand.class);
            ItemStack held = player.getHeldItem(hand);
            if (held.getItem() instanceof IInteractObj) {
                IInteractObj obj = (IInteractObj) held.getItem();
                return obj.getGui(player, hand);
            }
        }
        return null;
    }
}
