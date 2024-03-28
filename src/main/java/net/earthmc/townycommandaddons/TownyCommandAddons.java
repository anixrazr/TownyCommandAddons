package net.earthmc.townycommandaddons;

import com.palmergames.bukkit.towny.TownyCommandAddonAPI;
import net.earthmc.townycommandaddons.command.NationOutlawCommand;
import net.earthmc.townycommandaddons.command.NationOutlawlistCommand;
import net.earthmc.townycommandaddons.listener.TownyJoinListener;
import net.earthmc.townycommandaddons.listener.TownySpawnListener;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class TownyCommandAddons extends JavaPlugin {

    @Override
    public void onEnable() {
        registerCommands();
        registerListeners();
    }

    private void registerCommands() {
        TownyCommandAddonAPI.addSubCommand(TownyCommandAddonAPI.CommandType.NATION, "outlaw", new NationOutlawCommand());
        TownyCommandAddonAPI.addSubCommand(TownyCommandAddonAPI.CommandType.NATION, "outlawlist", new NationOutlawlistCommand());
    }

    private void registerListeners() {
        PluginManager pm = getServer().getPluginManager();

        pm.registerEvents(new TownyJoinListener(), this);
        pm.registerEvents(new TownySpawnListener(), this);
    }
}
