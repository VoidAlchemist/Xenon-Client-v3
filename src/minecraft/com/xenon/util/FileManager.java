package com.xenon.util;

import com.google.gson.Gson;
import com.xenon.XenonClient;
import com.xenon.modules.ModSettings;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

/**
 * Now use java.nio & Gson instead of java.io.
 *
 * @author Zenon
 * @since 2.0
 */
public class FileManager {

    public static final Path root_dir = Paths.get("XenonClient");
    public static final Path main_conf = root_dir.resolve("main_conf.txt");
    public static final Path logger = Paths.get("logs/xenon.log");
    private static final Gson gson = new Gson();

    /**
     * Must run this method before calling anything else in this class.
     *
     * @see com.xenon.XenonClient#preInit()
     * @since 1.0
     */
    public static void init() {

        try {
            if (!Files.exists(root_dir))
                Files.createDirectory(root_dir);

            if (!Files.exists(main_conf))
                Files.createFile(main_conf);

            if (!Files.exists(logger))
                Files.createFile(logger);

        } catch (IOException e) {
            e.printStackTrace();
            throw new IllegalStateException("Cannot launch Client without configuration files.");
        }
        XenonClient.instance.printer.info("conf files initialized.");
    }


    public static void writeToJson(Path path, Object o) {
        try {
            Files.write(path, gson.toJson(o).getBytes(StandardCharsets.UTF_8));    //UTF-8 is REALLY important here.
            //was getting errors because of UTF-8 reads on ANSI written files.
        } catch (IOException e) {
            e.printStackTrace();
            throw new IllegalStateException("Couldn't write into configuration files.");
        }
    }

    public static <T> T readFromJson(Path path, Class<T> clazz) {
        try {

            StringBuilder builder = new StringBuilder();
            for (String line : Files.readAllLines(path)) {
                builder.append(line);
            }
            if (builder.length() < 1) return null;

            return gson.fromJson(builder.toString(), clazz);
        } catch (IOException e) {
            XenonClient.instance.printer.error("Couldn't read Xenon modules configuration files.");
            return null;
        }
    }

    public static void log(String msg) {
        try {
            Files.write(logger, msg.getBytes(), StandardOpenOption.APPEND);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static ModSettings instanciateModSettings() {
        ModSettings result = readFromJson(main_conf, ModSettings.class);
        return result == null ? new ModSettings() : result;
    }
}
