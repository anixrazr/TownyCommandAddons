package net.earthmc.townycommandaddons.listener;

import com.palmergames.bukkit.towny.TownyAPI;
import com.palmergames.bukkit.towny.TownyMessaging;
import com.palmergames.bukkit.towny.event.NationSpawnEvent;
import com.palmergames.bukkit.towny.event.SpawnEvent;
import com.palmergames.bukkit.towny.event.TownSpawnEvent;
import com.palmergames.bukkit.towny.event.teleport.ResidentSpawnEvent;
import com.palmergames.bukkit.towny.object.Nation;
import com.palmergames.bukkit.towny.object.Resident;
import net.earthmc.townycommandaddons.manager.NationMetadataManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.List;

public class TownySpawnListener implements Listener {

    @EventHandler
    public void onNationSpawn(NationSpawnEvent event) {
        handleResidentTeleport(event, event.getPlayer(), event.getToNation());
    }

    @EventHandler
    public void onTownSpawn(TownSpawnEvent event) {
        handleResidentTeleport(event, event.getPlayer(), event.getToTown().getNationOrNull());
    }

    @EventHandler
    public void onResidentSpawn(ResidentSpawnEvent event) {
        handleResidentTeleport(event, event.getPlayer(), event.getToTown().getNationOrNull());
    }

    private void handleResidentTeleport(SpawnEvent event, Player player, Nation toNation) {
        if (toNation == null) return;

        if (toNation.hasResident(TownyAPI.getInstance().getResident(player))) return;

        NationMetadataManager nmm = new NationMetadataManager();
        List<Resident> nationOutlaws = nmm.getNationOutlaws(toNation);

        if (nationOutlaws.contains(TownyAPI.getInstance().getResident(player))) {
            event.setCancelled(true);
            TownyMessaging.sendErrorMsg(player, "You could not teleport to " + toNation.getName() + " as they have outlawed you");
        }
    }
}
