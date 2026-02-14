package me.emafire003.dev.structureplacerapi;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.NeoForge;

@Mod("structureplacerapi")
public class StructurePlacerAPIMod {
    public StructurePlacerAPIMod(IEventBus modBus) {
        NeoForge.EVENT_BUS.register(StructurePlacerAPI.class);
    }
}
