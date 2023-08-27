package net.minecraft.command;

import com.xenon.client.patches.Enchants;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.BlockPos;

import java.util.List;

public class CommandEnchant extends CommandBase {
    /**
     * Gets the name of the command
     */
    public String getCommandName() {
        return "enchant";
    }

    /**
     * Return the required permission level for this command.
     */
    public int getRequiredPermissionLevel() {
        return 2;
    }

    /**
     * Gets the usage string for the command.
     */
    public String getCommandUsage(ICommandSender sender) {
        return "commands.enchant.usage";
    }

    /**
     * Callback when the command is invoked
     */
    public void processCommand(ICommandSender sender, String[] args) throws CommandException {
        if (args.length < 2) {
            throw new WrongUsageException("commands.enchant.usage");
        } else {
            EntityPlayer entityplayer = getPlayer(sender, args[0]);
            sender.setCommandStat(CommandResultStats.Type.AFFECTED_ITEMS, 0);
            int i;

            try {
                i = parseInt(args[1], 0);
            } catch (NumberInvalidException numberinvalidexception) {
                Enchantment enchantment = Enchants.getEnchantmentByLocation(args[1]);

                if (enchantment == null) {
                    throw numberinvalidexception;
                }

                i = enchantment.effectId;
            }

            int j = 1;
            ItemStack itemstack = entityplayer.getCurrentEquippedItem();

            if (itemstack == null) {
                throw new CommandException("commands.enchant.noItem");
            } else {
                Enchantment enchantment1 = Enchants.getEnchantmentById(i);

                if (enchantment1 == null) {
                    throw new NumberInvalidException("commands.enchant.notFound", i);
                } else if (!enchantment1.canApply(itemstack)) {
                    throw new CommandException("commands.enchant.cantEnchant");
                } else {
                    if (args.length >= 3) {
                        j = parseInt(args[2], enchantment1.getMinLevel(), enchantment1.getMaxLevel());
                    }

                    if (itemstack.hasTagCompound()) {
                        NBTTagList nbttaglist = itemstack.getEnchantmentTagList();

                        if (nbttaglist != null) {
                            for (int k = 0; k < nbttaglist.tagCount(); ++k) {
                                int l = nbttaglist.getCompoundTagAt(k).getShort("id");

                                if (Enchants.getEnchantmentById(l) != null) {
                                    Enchantment enchantment2 = Enchants.getEnchantmentById(l);

                                    if (!enchantment2.canApplyTogether(enchantment1)) {
                                        throw new CommandException("commands.enchant.cantCombine", new Object[]{enchantment1.getTranslatedName(j), enchantment2.getTranslatedName(nbttaglist.getCompoundTagAt(k).getShort("lvl"))});
                                    }
                                }
                            }
                        }
                    }

                    itemstack.addEnchantment(enchantment1, j);
                    notifyOperators(sender, this, "commands.enchant.success");
                    sender.setCommandStat(CommandResultStats.Type.AFFECTED_ITEMS, 1);
                }
            }
        }
    }

    public List<String> addTabCompletionOptions(ICommandSender sender, String[] args, BlockPos pos) {
        return args.length == 1 ?
                getListOfStringsMatchingLastWord(args, this.getListOfPlayers()) :
                (args.length == 2 ?
                        getListOfStringsMatchingLastWord(args, Enchants.enchantmentResourceLocations()) : null);
    }

    protected String[] getListOfPlayers() {
        return MinecraftServer.getServer().getAllUsernames();
    }

    /**
     * Return whether the specified command parameter index is a username parameter.
     */
    public boolean isUsernameIndex(String[] args, int index) {
        return index == 0;
    }
}