package net.minecraft.network.play.server;

import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.client.network.NetHandlerPlayClient;

import java.io.IOException;

public class S46PacketSetCompressionLevel implements Packet<NetHandlerPlayClient> {
    private int threshold;

    /**
     * Reads the raw packet data from the data stream.
     */
    public void readPacketData(PacketBuffer buf) throws IOException {
        this.threshold = buf.readVarIntFromBuffer();
    }

    /**
     * Writes the raw packet data to the data stream.
     */
    public void writePacketData(PacketBuffer buf) throws IOException {
        buf.writeVarIntToBuffer(this.threshold);
    }

    /**
     * Passes this Packet on to the NetHandler for processing.
     */
    public void processPacket(NetHandlerPlayClient handler) {
        handler.handleSetCompressionLevel(this);
    }

    public int getThreshold() {
        return this.threshold;
    }
}
