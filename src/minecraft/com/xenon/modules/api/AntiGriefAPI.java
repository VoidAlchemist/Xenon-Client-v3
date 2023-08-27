package com.xenon.modules.api;

import com.xenon.XenonClient;
import com.xenon.util.FileManager;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Set;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class AntiGriefAPI {

    private static final Path storageFile = FileManager.root_dir.resolve("griefers.txt");

    private static Set<UUID> griefers;


    @SuppressWarnings("resource")   // seems java 8 compiler is blind about the set collector
    public static void init() {


        try {
            if (Files.exists(storageFile))
                griefers = Files.lines(storageFile)
                        .map(UUID::fromString)
                        .collect(Collectors.toSet());
            else Files.createFile(storageFile);

        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Cannot launch Client without configuration files");
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            throw new RuntimeException("Malformed UUID in griefers.txt");
        }
    }

    /**
     * Saves the griefer list in the file ./XenonClient/griefers.txt
     * Called upon griefing command execution.
     */
    public static void saveTofile() {
        try {
            Files.write(
                    storageFile,
                    griefers.stream()
                            .map(UUID::toString)
                            .collect(Collectors.joining("\n"))
                            .getBytes(StandardCharsets.UTF_8)
            );
        } catch (IOException e) {
            e.printStackTrace();
            XenonClient.instance.senderr("Failed to write griefer list back to file");
        }
    }


    public static boolean addGriefer(UUID grieferUUID) {
        return griefers.add(grieferUUID);
    }


    public static boolean removeGriefer(UUID grieferUUID) {
        return griefers.remove(grieferUUID);
    }

    public static void forEachGriefer(Consumer<UUID> foreach) {
        griefers.forEach(foreach);
    }





    public static void onPlayerSpawn() {

    }


}
