package me.emafire003.dev.structureplacerapi;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;

@Mod("structureplacerapi")
public class StructurePlacerAPIMod {
    public StructurePlacerAPIMod(IEventBus modBus) {
        MinecraftForge.EVENT_BUS.register(StructurePlacerAPI.class);
    }
}
