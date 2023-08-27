package com.xenon.client.command;

import com.mojang.util.UUIDTypeAdapter;
import com.xenon.XenonClient;
import com.xenon.modules.api.AntiGriefAPI;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;


public class AntiGrieferCommand extends CommandDispatcher.Cmd {

    private static final String error =
            "Invalid Syntax. Use: !griefers:grf add:add_offline:remove:remove_offline:list <in-game name>";

    AntiGrieferCommand() {
        super("griefers");
    }

    @Override
    boolean trydispatch(String[] args) {
        if (!args[0].equals("griefers") && !args[0].equals("grf"))
            return false;

        if (args.length < 2) {
            XenonClient.instance.senderr(error);
            return true;
        }

        // for "add*" and "remove*" cases, note that `args[2]` will never OutOfBoundException since
        // the lambda will only be called by #tryUsernameToUUIDAndThen if `args.length = 3`.

        switch (args[1]) {
            case "add":
                tryUsernameToPlayerAndThen(
                        args,
                        "Invalid Syntax. Use: !griefers:grf add <in-game name>",
                        player -> XenonClient.instance.sendmsg(
                                AntiGriefAPI.addGriefer(player.getUniqueID()) ?
                                        "Added '" + args[2] + "' to the griefer list (UUID: "+player.getUniqueID()+')' :
                                        '\'' + args[2] + "' is already present in the griefer list"
                        ));
                break;
            case "remove":
                tryUsernameToPlayerAndThen(
                        args,
                        "Invalid Syntax. Use: !griefers:grf remove <in-game name>",
                        player -> XenonClient.instance.sendmsg(
                                AntiGriefAPI.removeGriefer(player.getUniqueID()) ?
                                        "Removed '"+args[2]+"' from the griefer list (UUID: "+player.getUniqueID()+')' :
                                        '\'' + args[2] + "' is not present in the griefer list"
                        ));
                break;
            case "add_offline":
                tryUsernameToUUIDAndThen(
                        args,
                        "Invalid Syntax. Use: !griefers:grf add_offline <in-game name>",
                        uuid -> XenonClient.instance.sendmsg(
                                AntiGriefAPI.addGriefer(uuid) ?
                                        "Added '" + args[2] + "' to the griefer list (UUID: " + uuid + ')' :
                                        '\'' + args[2] + "' is already present in the griefer list"
                        ));
                break;
            case "remove_offline":
                tryUsernameToUUIDAndThen(
                        args,
                        "Invalid Syntax. Use: !griefers:grf remove_offline <in-game name>",
                        uuid -> XenonClient.instance.sendmsg(
                                AntiGriefAPI.removeGriefer(uuid) ?
                                        "Removed '" + args[2] + "' from the griefer list (UUID: " + uuid + ')' :
                                        '\'' + args[2] + "' is not present in the griefer list"
                        ));
                break;
            case "list":
                listGriefers(args);
                break;
            default:
                XenonClient.instance.senderr(error);
                break;
        }

        return true;
    }


    /**
     * Tries to retrieve a UUID from a username by looping through the world's loaded players,
     * then executes <code>andThen</code> on it, then finishes by calling {@link AntiGriefAPI#saveTofile()}.
     * @param args the command's arguments. Expecting exactly 3 arguments: command's name, command's keyword and
     *            an in-game name.
     * @param errorMessage the error message to display when the argument count isn't 3
     * @param andThen the function to call after having retrieved a UUID from the third element of <code>args</code>
     * @see #tryUsernameToUUIDAndThen(String[], String, Consumer)
     */
    private static void tryUsernameToPlayerAndThen(String[] args, String errorMessage, Consumer<EntityPlayer> andThen) {
        if (args.length != 3) {
            XenonClient.instance.senderr(errorMessage);
            return;
        }

        double bestDistance = Double.MAX_VALUE;
        EntityPlayer bestMatch = null;

        for (EntityPlayer player : Minecraft.getMinecraft().theWorld.playerEntities)
            if (player.getUniqueID() != null) {
                String name = player.getName();
                if (name.equals(args[2])) {
                    andThen.accept(player);
                    AntiGriefAPI.saveTofile();
                    return;
                }
                double dist = StringUtils.getJaroWinklerDistance(args[2], name);
                if (dist < bestDistance) {
                    bestDistance = dist;
                    bestMatch = player;
                }
            }

        String msg = "Couldn't find a player named '" + args[2] + '\'';

        if (bestMatch != null)
            msg += ". Best match was '" + bestMatch.getName() + '\'';

        msg += ". Consider using '!griefers add/remove_offline <in-game name>' to add a player" +
                " that's not loaded in your world (still requires internet connection).";

        XenonClient.instance.senderr(msg);
    }


    /**
     * Tries to retrieve a UUID from a username by calling MOJANG's API, then executes <code>andThen</code> on it,
     * then finishes by calling {@link AntiGriefAPI#saveTofile()}.
     * The process might take a bit of time depending on the user's internet connection speed.
     * A timeout of 2 seconds is set for every request to complete within, so this command will never completely
     * lock the game. However, the user must not call it too often due to MOJANG API request limit per minute.
     * When playing on a normal server (i.e. not cracked), it is advised to use
     * {@link #tryUsernameToPlayerAndThen(String[], String, Consumer)} whenever possible since it doesn't do any API call.
     * @param args the command's arguments. Expecting exactly 3 arguments: command's name, command's keyword and
     *            an in-game name.
     * @param errorMessage the error message to display when the argument count isn't 3
     * @param andThen the function to call after having retrieved a UUID from the third element of <code>args</code>
     * @see #tryUsernameToPlayerAndThen(String[], String, Consumer)
     */
    private static void tryUsernameToUUIDAndThen(String[] args, String errorMessage, Consumer<UUID> andThen) {
        if (args.length != 3) {
            XenonClient.instance.senderr(errorMessage);
            return;
        }

        try {
            URL url = new URL("https://api.mojang.com/users/profiles/minecraft/" + args[2]);

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(2000);
            connection.setReadTimeout(2000);

            if (connection.getResponseCode() == 200) {
                InputStreamReader isr = new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8);

                StringBuilder builder = new StringBuilder();
                boolean success = false;
                boolean found_id_key = false;
                boolean in_quote = false;
                int c1;

                while ((c1 = isr.read()) != -1) {
                    char c = (char)c1;
                    if (c == '"') {

                        if (in_quote) {
                            if (found_id_key) {
                                success = true;
                                break;
                            } else if (builder.toString().equals("id"))
                                found_id_key = true;
                        }

                        in_quote = !in_quote;
                        builder.setLength(0);
                    }
                    else if (in_quote)
                        builder.append(c);
                }

                if (success) {

                    // group uuid characters and add dash between groups
                    UUID uuid = UUIDTypeAdapter.fromString(builder.toString());
                    andThen.accept(uuid);
                    AntiGriefAPI.saveTofile();

                } else XenonClient.instance.senderr("Failed to retrieve UUID for username " + args[2]);


            } else XenonClient.instance.senderr("Failed to retrieve UUID for username " + args[2]);

        } catch (IOException | IllegalArgumentException e) {
            e.printStackTrace();
            XenonClient.instance.senderr("Failed to retrieve UUID for username " + args[2]);
        }
    }

    /**
     * For all griefer's UUID in {@link AntiGriefAPI#griefers}, requests the corresponding username to
     * MOJANG API and lists them in the chat.
     * The process might take a bit of time depending on the user's internet connection speed.
     * A timeout of 2 seconds is set for every request to complete within, so this command will never completely
     * lock the game. However, the user must not call it too often due to MOJANG API request limit per minute.
     * @param args the command arguments.
     */
    private static void listGriefers(String[] args) {

        Pattern pattern1 = null;

        if (args.length > 2) {
            if ( args.length < 4 || !args[2].equals("|") || !args[3].equals("grep") ) {
                XenonClient.instance.senderr("Invalid Syntax. Use: !griefers:grf list | grep <pattern>");
                return;
            }

            if (args.length > 4) {
                String fifthArg = String.join(" ", Arrays.copyOfRange(args, 4, args.length));
                try {
                    pattern1 = Pattern.compile(fifthArg, Pattern.CASE_INSENSITIVE);
                } catch (PatternSyntaxException e) {
                    e.printStackTrace();
                    XenonClient.instance.senderr("Invalid Pattern. Use: !griefers:grf list | grep <pattern>" +
                            " where <pattern> is a java-compliant pattern");
                    return;
                }
            }
        }

        final Pattern pattern = pattern1;
        final List<String> grieferNames = new ArrayList<>();

        AntiGriefAPI.forEachGriefer(uuid -> {
            try {
                URL url = new URL("https://sessionserver.mojang.com/session/minecraft/profile/" + uuid);

                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.setConnectTimeout(2000);
                connection.setReadTimeout(2000);

                if (connection.getResponseCode() == 200) {
                    InputStreamReader isr = new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8);

                    StringBuilder builder = new StringBuilder();
                    boolean success = false;
                    boolean found_name_key = false;
                    boolean in_quote = false;
                    int c1;

                    while ((c1 = isr.read()) != -1) {
                        char c = (char)c1;
                        if (c == '"') {

                            if (in_quote) {
                                if (found_name_key) {
                                    success = true;
                                    break;
                                } else if (builder.toString().equals("name"))
                                    found_name_key = true;
                            }

                            in_quote = !in_quote;
                            builder.setLength(0);
                        }
                        else if (in_quote)
                            builder.append(c);
                    }

                    if (success) {

                        String name = builder.toString();

                        if (pattern == null || pattern.matcher(name).find())
                            grieferNames.add(name);

                    } else XenonClient.instance.senderr("Failed to retrieve username for UUID " + uuid);


                } else XenonClient.instance.senderr("Failed to retrieve username for UUID " + uuid);

            } catch (IOException e) {
                e.printStackTrace();
                XenonClient.instance.senderr("Failed to retrieve username for UUID " + uuid);
            }
        });

        XenonClient.instance.sendmsg("--Griefers--");
        for (String name : grieferNames)
            XenonClient.instance.sendmsg(name);
    }
}
