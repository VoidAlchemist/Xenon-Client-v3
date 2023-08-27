package net.minecraft.network.play.server;

import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.client.network.NetHandlerPlayClient;

import java.io.IOException;

public class S2EPacketCloseWindow implements Packet<NetHandlerPlayClient> {
    private int windowId;

    public S2EPacketCloseWindow() {
    }

    public S2EPacketCloseWindow(int windowIdIn) {
        this.windowId = windowIdIn;
    }

    /**
     * Passes this Packet on to the NetHandler for processing.
     */
    public void processPacket(NetHandlerPlayClient handler) {
        handler.handleCloseWindow(this);
    }

    /**
     * Reads the raw packet data from the data stream.
     */
    public void readPacketData(PacketBuffer buf) throws IOException {
        this.windowId = buf.readUnsignedByte();
    }

    /**
     * Writes the raw packet data to the data stream.
     */
    public void writePacketData(PacketBuffer buf) throws IOException {
        buf.writeByte(this.windowId);
    }
}
