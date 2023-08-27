package net.minecraft.network.play.server;

import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.client.network.NetHandlerPlayClient;

import java.io.IOException;

public class S00PacketKeepAlive implements Packet<NetHandlerPlayClient> {
    private int id;

    public S00PacketKeepAlive() {
    }

    public S00PacketKeepAlive(int idIn) {
        this.id = idIn;
    }

    /**
     * Passes this Packet on to the NetHandler for processing.
     */
    public void processPacket(NetHandlerPlayClient handler) {
        handler.handleKeepAlive(this);
    }

    /**
     * Reads the raw packet data from the data stream.
     */
    public void readPacketData(PacketBuffer buf) throws IOException {
        this.id = buf.readVarIntFromBuffer();
    }

    /**
     * Writes the raw packet data to the data stream.
     */
    public void writePacketData(PacketBuffer buf) throws IOException {
        buf.writeVarIntToBuffer(this.id);
    }

    public int func_149134_c() {
        return this.id;
    }
}
