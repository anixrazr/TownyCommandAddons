package net.earthmc.townycommandaddons.listener;

import com.palmergames.bukkit.towny.TownyMessaging;
import com.palmergames.bukkit.towny.event.NationInviteTownEvent;
import com.palmergames.bukkit.towny.event.NationPreAddTownEvent;
import com.palmergames.bukkit.towny.event.TownPreAddResidentEvent;
import com.palmergames.bukkit.towny.event.town.TownPreInvitePlayerEvent;
import com.palmergames.bukkit.towny.object.Nation;
import com.palmergames.bukkit.towny.object.Resident;
import com.palmergames.bukkit.towny.object.Town;
import com.palmergames.bukkit.towny.object.inviteobjects.TownJoinNationInvite;
import net.earthmc.townycommandaddons.manager.NationMetadataManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.List;

public class TownyJoinListener implements Listener {

    @EventHandler
    public void onTownPreInvitePlayer(TownPreInvitePlayerEvent event) {
        Nation nation = event.getTown().getNationOrNull();
        if (nation == null) return;

        NationMetadataManager nmm = new NationMetadataManager();
        List<Resident> nationOutlaws = nmm.getNationOutlaws(nation);

        if (nationOutlaws.contains(event.getInvitedResident())) {
            event.setCancelled(true);
            TownyMessaging.sendErrorMsg(event.getInvite().getDirectSender(), "You cannot invite this player as they are outlawed in your nation");
        }
    }

    @EventHandler
    public void onTownPreAddResident(TownPreAddResidentEvent event) {
        Town town = event.getTown();
        Nation nation = town.getNationOrNull();
        if (nation == null) return;

        NationMetadataManager nmm = new NationMetadataManager();
        List<Resident> nationOutlaws = nmm.getNationOutlaws(nation);

        if (nationOutlaws.contains(event.getResident())) {
            event.setCancelled(true);
            TownyMessaging.sendErrorMsg(event.getResident(), "You cannot join " + town.getName() + " as you are outlawed in the nation it is a member of");
        }
    }

    @EventHandler
    public void onNationInviteTown(NationInviteTownEvent event) {
        TownJoinNationInvite invite = event.getInvite();

        NationMetadataManager nmm = new NationMetadataManager();
        List<Resident> nationOutlaws = nmm.getNationOutlaws(invite.getSender());

        for (Resident resident : invite.getReceiver().getResidents()) {
            if (nationOutlaws.contains(resident)) {
                invite.decline(true);

                TownyMessaging.sendErrorMsg(invite.getDirectSender(), "Failed to invite " + invite.getReceiver().getName() + " as your nation has outlawed residents in their town");
                break;
            }
        }
    }

    @EventHandler
    public void onNationPreAddTown(NationPreAddTownEvent event) {
        NationMetadataManager nmm = new NationMetadataManager();
        List<Resident> nationOutlaws = nmm.getNationOutlaws(event.getNation());

        for (Resident resident : event.getTown().getResidents()) {
            if (nationOutlaws.contains(resident)) {
                event.setCancelled(true);

                String townName = event.getTownName();
                String nationName = event.getNation().getName();
                event.setCancelMessage(townName + " can not join " + nationName + " as " + nationName + " has outlawed a resident in " + townName);
                break;
            }
        }
    }
}
