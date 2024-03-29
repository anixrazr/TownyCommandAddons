package net.earthmc.townycommandaddons.command;

import com.palmergames.bukkit.towny.TownyAPI;
import com.palmergames.bukkit.towny.TownyFormatter;
import com.palmergames.bukkit.towny.TownyMessaging;
import com.palmergames.bukkit.towny.object.Nation;
import com.palmergames.bukkit.towny.object.Resident;
import com.palmergames.bukkit.towny.object.Translatable;
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

public class NationOutlawlistCommand implements TabExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        Nation nation;
        if (args.length == 0) {
            if (!(sender instanceof Player player)) {
                TownyMessaging.sendErrorMsg(sender, "Only players can use this command");
                return true;
            }

            Resident resident = TownyAPI.getInstance().getResident(player);
            if (resident == null) {
                TownyMessaging.sendErrorMsg(player, "You cannot use this command as your account has no associated resident data");
                return true;
            }

            nation = resident.getNationOrNull();
            if (nation == null) {
                TownyMessaging.sendErrorMsg(player, "You are not a member of a nation");
                return true;
            }
        } else {
            nation = TownyAPI.getInstance().getNation(args[0]);
            if (nation == null) {
                TownyMessaging.sendErrorMsg(sender, "Specified nation does not exist");
                return true;
            }
        }

        NationMetadataManager nmm = new NationMetadataManager();
        List<Resident> outlawedResidents = nmm.getNationOutlaws(nation);

        TownyMessaging.sendMessage(sender, TownyFormatter.getFormattedTownyObjects(Translatable.of("outlaws").forLocale(sender), new ArrayList<>(outlawedResidents)));

        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        List<String> nationNames = new ArrayList<>();
        for (Nation nation : TownyAPI.getInstance().getNations()) {
            nationNames.add(nation.getName());
        }

        if (args.length == 1) {
            if (args[0].isEmpty()) return nationNames;

            return nationNames.stream()
                    .collect(Collectors.toList());
        }

        return null;
    }
}
