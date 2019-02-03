package xieao.theora.core.command;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import xieao.theora.api.Consts;

public class TheoraCommand {
    public static void register(FMLServerStartingEvent event) {
        event.getCommandDispatcher().register(
                LiteralArgumentBuilder.literal(Consts.MOD_ID)
        );
    }
}
