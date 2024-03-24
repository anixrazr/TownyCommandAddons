package net.earthmc.townycommandaddons.manager;

import com.palmergames.bukkit.towny.TownyAPI;
import com.palmergames.bukkit.towny.object.Nation;
import com.palmergames.bukkit.towny.object.Resident;
import com.palmergames.bukkit.towny.object.metadata.StringDataField;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class NationMetadataManager {
    private final String nationOutlawsKey = "townycommandaddons_nation_outlaws";

    public void setNationOutlaws(Nation nation, List<Resident> residentsToOutlaw) {
        if (!nation.hasMeta(nationOutlawsKey))
            nation.addMetaData(new StringDataField(nationOutlawsKey, null));

        StringDataField sdf = (StringDataField) nation.getMetadata(nationOutlawsKey);
        if (sdf == null) return;

        String delimitedNames = residentsToOutlaw.stream()
                .map(resident -> resident.getUUID().toString())
                .collect(Collectors.joining(","));

        sdf.setValue(delimitedNames);
    }

    public List<Resident> getNationOutlaws(Nation nation) {
        if (nation == null) return null;

        List<Resident> outlawedResidents = new ArrayList<>();
        StringDataField sdf = (StringDataField) nation.getMetadata(nationOutlawsKey);
        if (sdf == null || sdf.getValue() == null || sdf.getValue().isEmpty()) return outlawedResidents;

        for (String string : sdf.getValue().split(",")) {
            Resident resident = TownyAPI.getInstance().getResident(UUID.fromString(string));
            if (resident == null) continue;

            outlawedResidents.add(resident);
        }

        return outlawedResidents;
    }
}
