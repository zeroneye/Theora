package xieao.theora;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.event.lifecycle.InterModProcessEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.FMLModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import xieao.theora.core.command.TheoraCommand;
import xieao.theora.core.config.Config;
import xieao.theora.core.handler.InitHandler;
import xieao.theora.core.network.Network;

@Mod(Theora.MOD_ID)
public class Theora {
    public static final String MOD_ID = "theora";
    public static final Logger LOGGER = LogManager.getLogger(MOD_ID.toUpperCase());

    public static final Network NET = Network.creat();
    private final Proxy proxy;

    public Theora() {
        this.proxy = DistExecutor.runForDist(() -> Proxy.Client::new, () -> Proxy::new);
        FMLModLoadingContext.get().getModEventBus().addListener(this::setup);
        FMLModLoadingContext.get().getModEventBus().addListener(this::enqueueIMC);
        FMLModLoadingContext.get().getModEventBus().addListener(this::processIMC);
        MinecraftForge.EVENT_BUS.addListener(this::starting);
        Config.load();
    }

    void setup(FMLCommonSetupEvent event) {
        InitHandler.pre();
        this.proxy.setup(event);
    }

    void enqueueIMC(InterModEnqueueEvent event) {
        InitHandler.init();
        this.proxy.enqueueIMC(event);
    }

    void processIMC(InterModProcessEvent event) {
        InitHandler.post();
        this.proxy.processIMC(event);
    }

    void starting(FMLServerStartingEvent event) {
        new TheoraCommand(event.getCommandDispatcher());
    }

    public static ResourceLocation loc(String path) {
        return new ResourceLocation(MOD_ID, path);
    }
}
