package net.earthmc.townycommandaddons.command;

import com.palmergames.bukkit.towny.TownyAPI;
import com.palmergames.bukkit.towny.TownyMessaging;
import com.palmergames.bukkit.towny.object.Nation;
import com.palmergames.bukkit.towny.object.Resident;
import net.earthmc.townycommandaddons.manager.NationMetadataManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class NationOutlawCommand implements TabExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if ((!(sender instanceof Player player))) {
            TownyMessaging.sendErrorMsg("Only players can use this command");
            return true;
        }

        if (!player.hasPermission("townycommandaddons.command.nation.outlaw")) {
            TownyMessaging.sendErrorMsg(player, "You do not have permission to perform this command");
            return true;
        }

        Resident senderResident = TownyAPI.getInstance().getResident(player);
        if (senderResident == null) {
            TownyMessaging.sendErrorMsg(player, "You cannot use this command as your account has no associated resident data");
            return true;
        }

        Nation senderNation = senderResident.getNationOrNull();
        if (senderNation == null) {
            TownyMessaging.sendErrorMsg(player, "This command can only be used by players in a nation");
            return true;
        }

        if (args.length == 0) {
            TownyMessaging.sendErrorMsg(player, "No command method provided");
            return true;
        }

        if (args.length < 2) {
            TownyMessaging.sendErrorMsg(player, "No resident name provided");
            return true;
        }

        Resident resident = TownyAPI.getInstance().getResident(args[1]);
        if (resident == null) {
            TownyMessaging.sendErrorMsg(player, args[1] + " is not a real resident");
            return true;
        }

        if (senderNation.hasResident(resident)) {
            TownyMessaging.sendErrorMsg(player, resident.getName() + " is a resident of your nation and cannot be outlawed");
            return true;
        }

        if (resident.isNPC()) {
            TownyMessaging.sendErrorMsg(player, resident.getName() + " is an NPC and cannot be outlawed");
            return true;
        }

        NationMetadataManager nmm = new NationMetadataManager();
        switch (args[0]) {
            case "add": {
                List<Resident> outlawList = nmm.getNationOutlaws(senderNation);
                if (outlawList.contains(resident)) {
                    TownyMessaging.sendErrorMsg(player, resident.getName() + " is already outlawed in your nation");
                    return true;
                }

                outlawList.add(resident);

                nmm.setNationOutlaws(senderNation, outlawList);
                TownyMessaging.sendMsg(player, "Successfully added " + resident.getName() + " as an outlaw");
                break;
            }
            case "remove": {
                List<Resident> outlawList = nmm.getNationOutlaws(senderNation);
                if (!outlawList.contains(resident)) {
                    TownyMessaging.sendErrorMsg(player, resident.getName() + " is not outlawed in your nation");
                    return true;
                }

                outlawList.removeAll(List.of(resident));

                nmm.setNationOutlaws(senderNation, outlawList);
                TownyMessaging.sendMsg(player, "Successfully removed " + resident.getName() + " as an outlaw");
            }
        }

        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        List<String> availableArguments = List.of("add", "remove");

        if (args.length == 1) {
            if (args[0].isEmpty()) return availableArguments;

            return availableArguments.stream()
                    .filter(arg -> arg.startsWith(args[0].toLowerCase()))
                    .collect(Collectors.toList());
        }

        if (args.length >= 2) {
            return TownyAPI.getInstance().getResidents().stream()
                    .map(Resident::getName)
                    .collect(Collectors.toList());
        }

        return null;
    }
}
