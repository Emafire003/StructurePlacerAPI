package me.emafire003.dev.structureplacerapi;

import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraft.world.level.ServerLevelAccessor;

public interface ActionOnBlockFind {
    StructureTemplate.StructureBlockInfo action(StructureTemplate.StructureBlockInfo info, ServerLevelAccessor world);
}
