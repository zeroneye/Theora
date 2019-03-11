package xieao.theora.block.gate;

import com.mojang.authlib.GameProfile;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.util.Constants;
import xieao.theora.Theora;
import xieao.theora.api.TheoraAPI;
import xieao.theora.api.liquid.LiquidHandler;
import xieao.theora.api.player.GateData;
import xieao.theora.block.IInvBase;
import xieao.theora.block.TileBase;
import xieao.theora.client.gui.GuiGate;
import xieao.theora.core.ILiquids;
import xieao.theora.core.ITiles;
import xieao.theora.inventory.ContainerGate;
import xieao.theora.lib.util.PlayerUtil;
import xieao.theora.network.packet.playerdata.SyncGateData;
import xieao.theora.world.IInteractObj;

import javax.annotation.Nullable;
import java.util.UUID;

public class TileGate extends TileBase.Tickable implements IInvBase, IInteractObj {
    private final LiquidHandler handler = new LiquidHandler();
    private GameProfile owner = new GameProfile(new UUID(0L, 0L), "null");
    private boolean gateBase;

    @Nullable
    private EntityPlayer player;
    public int openTab;

    public TileGate() {
        super(ITiles.GATE);
        this.handler.add("slot.essence", ILiquids.ESSENCE, 1000.0F, 100.0F);
        setInvSize(4);
    }

    @Override
    public void readSync(NBTTagCompound compound) {
        super.readSync(compound);
        this.handler.read(compound);
        this.gateBase = compound.getBoolean("GateBase");
        if (compound.contains("OwnerId", Constants.NBT.TAG_STRING)) {
            String ownerId = compound.getString("OwnerId");
            String ownerName = compound.getString("OwnerName");
            setOwner(new GameProfile(UUID.fromString(ownerId), ownerName));
        }
    }

    @Override
    public NBTTagCompound writeSync(NBTTagCompound compound) {
        this.handler.write(compound);
        compound.putBoolean("GateBase", this.gateBase);
        if (!"null".equals(this.owner.getName())) {
            compound.putString("OwnerId", this.owner.getId().toString());
            compound.putString("OwnerName", this.owner.getName());
        }
        return super.writeSync(compound);
    }

    @Override
    public void tick() {
        if (!this.gateBase) return;
        if (isServerWorld()) {
            if (this.player == null) {
                this.player = PlayerUtil.get(getOwnerID());
            }
            if (this.player != null) {
                TheoraAPI.getPlayerData(this.player).ifPresent(playerData -> {
                    GateData gateData = playerData.gate;
                    gateData.setLastCheck(this.world.getGameTime());
                    gateData.setTileEntity(this);
                    syncPlayer(gateData);
                });
            }
        }
        super.tick();
    }

    public void syncPlayer(GateData gateData) {
        LiquidHandler handler = gateData.getLiquidHandler();
        handler.read(this.handler.serialize());
        boolean guiOpen = gateData.playerGuiOpen;
        if (guiOpen && this.world.getGameTime() % 10 == 0) {
            Theora.NET.toClient(new SyncGateData(gateData.serialize()), this.player);
        }
    }

    public LiquidHandler getHandler() {
        return handler;
    }

    public GameProfile getOwner() {
        return owner;
    }

    public void setOwner(GameProfile owner) {
        this.owner = owner;
    }

    public UUID getOwnerID() {
        return getOwner().getId();
    }

    public String getOwnerName() {
        return getOwner().getName();
    }

    public boolean isGateBase() {
        return gateBase;
    }

    public void setGateBase(boolean gateBase) {
        this.gateBase = gateBase;
    }

    @Nullable
    public EntityPlayer getPlayer() {
        return player;
    }

    public void setPlayer(@Nullable EntityPlayer player) {
        this.player = player;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public AxisAlignedBB getRenderBoundingBox() {
        return new AxisAlignedBB(this.pos.add(-2, -2, -2),
                this.pos.add(2, 2, 2));
    }

    @Override
    public Container getContainer(EntityPlayer player) {
        return new ContainerGate(player, this);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public GuiScreen getGui(EntityPlayer player, EnumHand hand) {
        return new GuiGate(player, this);
    }
}
