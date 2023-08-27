package com.xenon.modules.api.dungeons;

import com.xenon.XenonClient;
import com.xenon.modules.api.GlowingAPI;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;

import java.util.Arrays;

public class Riddle {

    private static boolean solved;
    private static final String[] solutions = {
            "Therewardisnotinmychest!",
            "Atleastoneofthemislying,andtherewardisnotin",
            "Mychestdoesn'thavethereward.Wearealltellingthetruth",
            "MychesthastherewardandI'mtellingthetruth",
            "Therewardisn'tinanyofourchests",
            "Bothofthemaretellingthetruth."
    };

    static void reset() {
        solved = false;
    }

    public static void trysolve(String chatMessage) {
        if (!solved && Arrays.stream(solutions).anyMatch(chatMessage::contains)) {
            String npcName = chatMessage.substring(
                    chatMessage.indexOf(']') + 1,
                    chatMessage.indexOf(':')
            );
            XenonClient.instance.sendmsg("[XenonClient] solved riddle for npc " + npcName);

            for (Entity entity : Minecraft.getMinecraft().theWorld.getLoadedEntityList()) {
                if (entity != null && entity.hasCustomName() && entity.getCustomNameTag().contains(npcName)) {

                    GlowingAPI.colorForEntity.put(
                            Minecraft.getMinecraft().theWorld.getClosestPlayerToEntity(entity, 0.05),
                            GlowingAPI.green
                    );
                    solved = true;
                    break;
                }
            }

        }
    }
}
