package me.emafire003.dev.structureplacerapi;

import net.minecraft.world.level.block.Block;
import net.minecraft.tags.TagKey;

@SuppressWarnings("unused")
public interface ICustomStructureTemplate {
    /** This is used to tell the StructureTemplate that we are using the StructurePlacerApi system */
    void structurePlacerAPI$setCustom(boolean custom);
    boolean structurePlacerAPI$isCustom();
    /** Weather or not this structure should replace the bedrock */
    void structurePlacerAPI$setReplaceBedrock(boolean replace);
    boolean structurePlacerAPI$getReplaceBedrock();
    /** Weather or not this structure should replace the barrier block */
    void structurePlacerAPI$setReplaceBarrier(boolean replace);
    boolean structurePlacerAPI$getReplaceBarrier();
    /** Weather or not this structure should only replace blocks with a certain tag, like Air blocks*/
    void structurePlacerAPI$setOnlyReplaceTagBlocks(boolean replace, TagKey<Block> tag);
    boolean structurePlacerAPI$getOnlyReplaceTagBlocks();
    /** Weather or not this structure should replace the blocks with the specified tag */
    void structurePlacerAPI$setPreventReplacementOfTagBlocks(boolean replace, TagKey<Block> tag);
    boolean structurePlacerAPI$getPreventReplacementOfTagBlocks();
    /** Sets the tag for the blocks needed for {@link #structurePlacerAPI$setPreventReplacementOfTagBlocks(boolean, TagKey)} or {@link #structurePlacerAPI$setOnlyReplaceTagBlocks(boolean, TagKey)}*/
    void structurePlacerAPI$setTaggedBlocks(TagKey<Block> taggedBlocks);
    TagKey<Block> structurePlacerAPI$getTaggedBlocks();

    void structurePlacerAPI$setActOnBlockStructurePlacing(boolean b);
    boolean structurePlacerAPI$getActOnBlockStructurePlacing();
    void structurePlacerAPI$setActOnBlockReplacedByStructure(boolean b);
    boolean structurePlacerAPI$getActOnBlockReplacedByStructure();

    void structurePlacerAPI$setOnBlockPlacingInStructure(ActionOnBlockFind action);
    ActionOnBlockFind structurePlacerAPI$getOnBlockPlacingInStructure();

    void structurePlacerAPI$setOnBlockReplacedByStructure(ActionOnBlockFind action);
    ActionOnBlockFind structurePlacerAPI$getOnBlockReplacedByStructure();

    void structurePlacerAPI$setBlockPlacedCheck(TagKey<Block> blocks);
    TagKey<Block> structurePlacerAPI$getBlockPlacedCheck();
    void structurePlacerAPI$setBlockReplacedCheck(TagKey<Block> blocks);
    TagKey<Block> structurePlacerAPI$getBlockReplacedCheck();



}
