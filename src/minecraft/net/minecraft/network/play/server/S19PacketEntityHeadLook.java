package net.minecraft.network.play.server;

import net.minecraft.entity.Entity;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.world.World;

import java.io.IOException;

public class S19PacketEntityHeadLook implements Packet<NetHandlerPlayClient> {
    private int entityId;
    private byte yaw;

    public S19PacketEntityHeadLook() {
    }

    public S19PacketEntityHeadLook(Entity entityIn, byte p_i45214_2_) {
        this.entityId = entityIn.getEntityId();
        this.yaw = p_i45214_2_;
    }

    /**
     * Reads the raw packet data from the data stream.
     */
    public void readPacketData(PacketBuffer buf) throws IOException {
        this.entityId = buf.readVarIntFromBuffer();
        this.yaw = buf.readByte();
    }

    /**
     * Writes the raw packet data to the data stream.
     */
    public void writePacketData(PacketBuffer buf) throws IOException {
        buf.writeVarIntToBuffer(this.entityId);
        buf.writeByte(this.yaw);
    }

    /**
     * Passes this Packet on to the NetHandler for processing.
     */
    public void processPacket(NetHandlerPlayClient handler) {
        handler.handleEntityHeadLook(this);
    }

    public Entity getEntity(World worldIn) {
        return worldIn.getEntityByID(this.entityId);
    }

    public byte getYaw() {
        return this.yaw;
    }
}