package net.minecraft.network.play.server;

import com.google.common.collect.Lists;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.util.BlockPos;
import net.minecraft.util.Vec3;

import java.io.IOException;
import java.util.List;

public class S27PacketExplosion implements Packet<NetHandlerPlayClient> {
    private double posX;
    private double posY;
    private double posZ;
    private float strength;
    private List<BlockPos> affectedBlockPositions;
    private float knockbackX;
    private float knockbackY;
    private float knockbackZ;

    public S27PacketExplosion() {
    }

    public S27PacketExplosion(double p_i45193_1_, double y, double z, float strengthIn, List<BlockPos> affectedBlocksIn, Vec3 p_i45193_9_) {
        this.posX = p_i45193_1_;
        this.posY = y;
        this.posZ = z;
        this.strength = strengthIn;
        this.affectedBlockPositions = Lists.newArrayList(affectedBlocksIn);

        if (p_i45193_9_ != null) {
            this.knockbackX = (float) p_i45193_9_.xCoord;
            this.knockbackY = (float) p_i45193_9_.yCoord;
            this.knockbackZ = (float) p_i45193_9_.zCoord;
        }
    }

    /**
     * Reads the raw packet data from the data stream.
     */
    public void readPacketData(PacketBuffer buf) throws IOException {
        this.posX = buf.readFloat();
        this.posY = buf.readFloat();
        this.posZ = buf.readFloat();
        this.strength = buf.readFloat();
        int i = buf.readInt();
        this.affectedBlockPositions = Lists.<BlockPos>newArrayListWithCapacity(i);
        int j = (int) this.posX;
        int k = (int) this.posY;
        int l = (int) this.posZ;

        for (int i1 = 0; i1 < i; ++i1) {
            int j1 = buf.readByte() + j;
            int k1 = buf.readByte() + k;
            int l1 = buf.readByte() + l;
            this.affectedBlockPositions.add(new BlockPos(j1, k1, l1));
        }

        this.knockbackX = buf.readFloat();
        this.knockbackY = buf.readFloat();
        this.knockbackZ = buf.readFloat();
    }

    /**
     * Writes the raw packet data to the data stream.
     */
    public void writePacketData(PacketBuffer buf) throws IOException {
        buf.writeFloat((float) this.posX);
        buf.writeFloat((float) this.posY);
        buf.writeFloat((float) this.posZ);
        buf.writeFloat(this.strength);
        buf.writeInt(this.affectedBlockPositions.size());
        int i = (int) this.posX;
        int j = (int) this.posY;
        int k = (int) this.posZ;

        for (BlockPos blockpos : this.affectedBlockPositions) {
            int l = blockpos.getX() - i;
            int i1 = blockpos.getY() - j;
            int j1 = blockpos.getZ() - k;
            buf.writeByte(l);
            buf.writeByte(i1);
            buf.writeByte(j1);
        }

        buf.writeFloat(this.knockbackX);
        buf.writeFloat(this.knockbackY);
        buf.writeFloat(this.knockbackZ);
    }

    /**
     * Passes this Packet on to the NetHandler for processing.
     */
    public void processPacket(NetHandlerPlayClient handler) {
        handler.handleExplosion(this);
    }

    public float knockbackX() {
        return this.knockbackX;
    }

    public float knockbackY() {
        return this.knockbackY;
    }

    public float knockbackZ() {
        return this.knockbackZ;
    }

    public double getX() {
        return this.posX;
    }

    public double getY() {
        return this.posY;
    }

    public double getZ() {
        return this.posZ;
    }

    public float getStrength() {
        return this.strength;
    }

    public List<BlockPos> getAffectedBlockPositions() {
        return this.affectedBlockPositions;
    }
}
