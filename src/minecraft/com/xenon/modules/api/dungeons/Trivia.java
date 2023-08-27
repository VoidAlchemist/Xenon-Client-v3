package com.xenon.modules.api.dungeons;

import com.xenon.XenonClient;
import net.minecraft.util.EnumChatFormatting;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Trivia {

    private static final Map<String, String[]> solutions;
    static {

        solutions = new HashMap<>();
        solutions.put("WhatisthestatusofTheWatcher?",new String[]{"Stalker"});
        solutions.put("WhatisthestatusofBonzo?", new String[]{"NewNecromancer"});
        solutions.put("WhatisthestatusofScarf?", new String[]{"ApprenticeNecromancer"});
        solutions.put("WhatisthestatusofTheProfessor?",new String[]{"Professor"});
        solutions.put("WhatisthestatusofThorn?", new String[]{"ShamanNecromancer"});
        solutions.put("WhatisthestatusofLivid?", new String[]{"MasterNecromancer"});
        solutions.put("WhatisthestatusofSadan?", new String[]{"NecromancerLord"});
        solutions.put("WhatisthestatusofMaxor?", new String[]{"YoungWither"});
        solutions.put("WhatisthestatusofGoldor?", new String[]{"WitherSoldier"});
        solutions.put("WhatisthestatusofStorm?", new String[]{"Elementalist"});
        solutions.put("WhatisthestatusofNecron?", new String[]{"WitherLord"});
        solutions.put("WhatisthestatusofMaxor,Storm,GoldorandNecron?",new String[]{"WitherLord"});
        solutions.put("WhichbrotherisontheSpider'sDen?",new String[]{"Rick"});
        solutions.put("WhatisthenameofRick'sbrother?",new String[]{"Pat"});
        solutions.put("WhatisthenameofthePainterintheHub?",new String[]{"Marco"});
        solutions.put("Whatisthenameofthepersonthatupgradespets?",new String[]{"Kat"});
        solutions.put("WhatisthenameoftheladyoftheNether?",new String[]{"Elle"});
        solutions.put("WhichvillagerintheVillagegivesyouaRogueSword?",new String[]{"Jamie"});
        solutions.put("Howmanyuniqueminionsarethere?",new String[]{"58Minions"});
        solutions.put("WhichoftheseenemiesdoesnotspawnintheSpider'sDen?",
                new String[]{"ZombieSpider","CaveSpider","WitherSkeleton","DashingSpooder",
                        "Broodfather","NightSpider"});
        solutions.put("Whichofthesemonstersonlyspawnsatnight?",
                new String[]{"ZombieVillager","Ghast"});
        solutions.put("WhichoftheseisnotadragoninTheEnd?",
                new String[]{"ZoomerDragon","WeakDragon","StonkDragon","HolyDragon","BoomerDragon",
                        "BoogerDragon","OlderDragon","ElderDragon","StableDragon","ProfessorDragon"});

    }
    private static String[] answers;



    public static void trysolve(String chatMessage) {
        if (chatMessage.contains("WhatSkyBlockyearisit?")) {

            double diff = Math.floor(System.currentTimeMillis() / 1000.0 - 1560276000);

            int year = (int) (diff / 446400.0) + 1;
            answers = new String[]{"Year" + year};
        } else {
            solutions.entrySet()
                    .stream()
                    .filter(entry -> chatMessage.contains(entry.getKey()))
                    .findFirst()
                    .ifPresent(entry -> answers = entry.getValue());

            if (answers != null) {
                char option = chatMessage.contains("ⓐ") ? 'a' : (
                        chatMessage.contains("ⓑ") ? 'b' : (
                                chatMessage.contains("ⓒ") ? 'c' : 0
                        )
                );
                if (option != 0 && Arrays.stream(answers).anyMatch(chatMessage::contains))
                    XenonClient.instance.sendmsg(EnumChatFormatting.DARK_GREEN +
                            "[XenonClient] Solution is option > " + option);

            }
        }
    }
}
