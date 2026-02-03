package me.emafire003.dev.structureplacerapi;

import net.minecraft.structure.StructureTemplate;
import net.minecraft.world.ServerWorldAccess;

public interface ActionOnBlockFind {
    void action(StructureTemplate.StructureBlockInfo info, ServerWorldAccess world);
}
