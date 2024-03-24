package net.earthmc.townycommandaddons;

import com.palmergames.bukkit.towny.TownyCommandAddonAPI;
import net.earthmc.townycommandaddons.command.NationOutlawCommand;
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
    }

    private void registerListeners() {
        PluginManager pm = getServer().getPluginManager();

        pm.registerEvents(new TownySpawnListener(), this);
    }
}
