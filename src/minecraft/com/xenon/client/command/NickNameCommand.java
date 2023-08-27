package com.xenon.client.command;

import com.xenon.XenonClient;

import java.util.Arrays;

/**
 * NickName command logic's placeholder class.
 *
 * @author Zenon
 * @see CommandDispatcher
 */
public class NickNameCommand extends CommandDispatcher.Cmd {

    private static final String error = "Invalid Syntax. Use: !nickname:nm set:show:toggle <new nickname>";

    NickNameCommand() {
        super("nickname");
    }


    boolean trydispatch(String[] args) {
        if ( !args[0].equals("nickname") && !args[0].equals("nm") )
            return false;

        if (args.length < 2) {
            XenonClient.instance.senderr(error);
            return true;
        }

        String op = args[1];
        switch (op) {
            case "set":
                if (args.length < 3) {
                    XenonClient.instance.senderr(error);
                    break;
                }

                String thirdArg = String.join(" ", Arrays.copyOfRange(args, 2, args.length));

                String newnickname = XenonClient.instance.settings.customName =
                        thirdArg.replaceAll("&", "§");
                XenonClient.instance.sendmsg("§aSet §r" + newnickname + " §aas a nickname.");
                break;
            case "show":
                String nickname = XenonClient.instance.settings.customName;
                nickname = nickname.replaceAll("§", "&");
                XenonClient.instance.sendmsg(nickname + " ('&' is used in place of the paragraph symbol)");
                break;
            case "toggle":
                boolean newState = XenonClient.instance.settings.customnameEnabled =
                        !XenonClient.instance.settings.customnameEnabled;
                XenonClient.instance.sendmsg("§aNickname is now " + (newState ? "enabled" : "disabled") + ".");
                break;
            default:
                XenonClient.instance.senderr(error);
                break;
        }
        return true;
    }
}
