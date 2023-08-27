package net.minecraft.network.play.server;

import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.util.BlockPos;

import java.io.IOException;

public class S05PacketSpawnPosition implements Packet<NetHandlerPlayClient> {
    private BlockPos spawnBlockPos;

    public S05PacketSpawnPosition() {
    }

    public S05PacketSpawnPosition(BlockPos spawnBlockPosIn) {
        this.spawnBlockPos = spawnBlockPosIn;
    }

    /**
     * Reads the raw packet data from the data stream.
     */
    public void readPacketData(PacketBuffer buf) throws IOException {
        this.spawnBlockPos = buf.readBlockPos();
    }

    /**
     * Writes the raw packet data to the data stream.
     */
    public void writePacketData(PacketBuffer buf) throws IOException {
        buf.writeBlockPos(this.spawnBlockPos);
    }

    /**
     * Passes this Packet on to the NetHandler for processing.
     */
    public void processPacket(NetHandlerPlayClient handler) {
        handler.handleSpawnPosition(this);
    }

    public BlockPos getSpawnPos() {
        return this.spawnBlockPos;
    }
}
