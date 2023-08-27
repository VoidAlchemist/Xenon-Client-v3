package com.xenon.client.command;

import com.xenon.XenonClient;
import com.xenon.util.readability.Hook;

import java.util.Arrays;
import java.util.stream.Collectors;

public class CommandDispatcher {

    private static final Cmd[] commands;

    static {
        commands = new Cmd[2];

        commands[0] = new NickNameCommand();
        commands[1] = new AntiGrieferCommand();
    }

    /**
     * Try to dispatch a xenon command, i.e. a chat message starting with "!x".
     * @param message the potential client-side command. Must never be null.
     */
    @Hook("net.minecraft.client.gui.GuiChat#keyTyped --> line 111")
    public static void dispatch(String message) {

        if (message.length() != 1) {

            String[] args = message.split("\\s+");
            args[0] = args[0].substring(1); // eat '!' at the beginning.

            for (Cmd f : commands)
                if (f.trydispatch(args))
                    return;
        }

        XenonClient.instance.senderr("Unknown command. Available: " +
                Arrays.stream(commands).map(c -> c.fullName).collect(Collectors.joining(", ")));
    }

    /**
     * Simple Client-side command interface for dispatching.
     *
     * @see CommandDispatcher#commands
     * @see com.xenon.client.command com.xenon.client.command package for its implementations
     */
    static abstract class Cmd {

        final String fullName;

        protected Cmd(String fullName) {
            this.fullName = fullName;
        }

        /**
         * Tries to execute the command. Return true upon successful command's name match, false otherwise.
         * @param args the command arguments, starting with the command name. Always contains at least one element.
         * @return whether the command's name (i.e. args[0]) matched.
         *          Command's parsing errors on other arguments have to be handled internally.
         */
        abstract boolean trydispatch(String[] args);
    }
}
