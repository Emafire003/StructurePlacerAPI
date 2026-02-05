package me.emafire003.dev.structureplacerapi.mixin;

import com.llamalad7.mixinextras.expression.Definition;
import com.llamalad7.mixinextras.expression.Expression;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import me.emafire003.dev.structureplacerapi.ActionOnBlockFind;
import me.emafire003.dev.structureplacerapi.ICustomStructureTemplate;
import me.emafire003.dev.structureplacerapi.StructurePlacerAPI;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.structure.StructureTemplate;
import net.minecraft.world.ServerWorldAccess;
import org.spongepowered.asm.mixin.Debug;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

import java.util.ArrayList;
import java.util.List;

@Debug(export = true)
@Mixin(StructureTemplate.class)
public abstract class CustomStructureTemplateMixin implements ICustomStructureTemplate {

    @Unique
    public boolean isCustomStructureTemplate = false;
    @Unique
    public boolean replaceBedrock = false;
    @Unique
    public boolean replaceBarrier = false;
    @Unique
    public boolean preventReplacementOfTagBlocks = false;
    @Unique
    public boolean onlyReplaceTaggedBlocks = false;
    @Unique
    public TagKey<Block> taggedBlocks = null;

    @Unique
    public boolean actOnBlockStructurePlacing = false;
    @Unique
    public boolean actOnBlockReplacedByStructure = false;
    @Unique /// aka check the block IN the structure
    public ActionOnBlockFind onBlockPlacingInStructure;
    @Unique /// aka check the block that is getting replced BY the structure
    public ActionOnBlockFind onBlockReplacedByStructure;
    @Unique
    public TagKey<Block> blockPlacedCheck;
    @Unique
    public TagKey<Block> blockReplacedCheck;

    @Override
    public void structurePlacerAPI$setCustom(boolean custom) {
        isCustomStructureTemplate = custom;
    }

    @Override
    public boolean structurePlacerAPI$isCustom() {
        return isCustomStructureTemplate;
    }

    @Override
    public void structurePlacerAPI$setReplaceBedrock(boolean replace) {
        replaceBedrock = replace;
    }

    @Override
    public boolean structurePlacerAPI$getReplaceBedrock() {
        return replaceBedrock;
    }

    @Override
    public void structurePlacerAPI$setReplaceBarrier(boolean replace) {
        replaceBarrier = replace;
    }

    @Override
    public boolean structurePlacerAPI$getReplaceBarrier() {
        return replaceBarrier;
    }

    @Override
    public void structurePlacerAPI$setOnlyReplaceTagBlocks(boolean replace, TagKey<Block> tag){
        onlyReplaceTaggedBlocks = replace;
        structurePlacerAPI$setTaggedBlocks(tag);
    }

    @Override
    public boolean structurePlacerAPI$getOnlyReplaceTagBlocks(){
        return onlyReplaceTaggedBlocks;
    }

    @Override
    public void structurePlacerAPI$setPreventReplacementOfTagBlocks(boolean replace, TagKey<Block> tag){
        preventReplacementOfTagBlocks = replace;
        structurePlacerAPI$setTaggedBlocks(tag);
    }

    @Override
    public boolean structurePlacerAPI$getPreventReplacementOfTagBlocks(){
        return preventReplacementOfTagBlocks;
    }

    @Override
    public void structurePlacerAPI$setTaggedBlocks(TagKey<Block> tag){
        taggedBlocks = tag;
    }

    @Override
    public TagKey<Block> structurePlacerAPI$getTaggedBlocks(){
        return taggedBlocks;
    }

    @Override
    public void structurePlacerAPI$setActOnBlockStructurePlacing(boolean b){
        actOnBlockStructurePlacing = b;
    }
    @Override
    public boolean structurePlacerAPI$getActOnBlockStructurePlacing(){
        return actOnBlockStructurePlacing;
    }
    @Override
    public void structurePlacerAPI$setActOnBlockReplacedByStructure(boolean b){
        actOnBlockReplacedByStructure = b;
    }
    @Override
    public boolean structurePlacerAPI$getActOnBlockReplacedByStructure(){
        return actOnBlockReplacedByStructure;
    }

    @Override
    public void structurePlacerAPI$setOnBlockPlacingInStructure(ActionOnBlockFind action){
        onBlockPlacingInStructure = action;
    }
    @Override
    public ActionOnBlockFind structurePlacerAPI$getOnBlockPlacingInStructure(){
        return onBlockPlacingInStructure;
    }

    @Override
    public void structurePlacerAPI$setOnBlockReplacedByStructure(ActionOnBlockFind action){
        onBlockReplacedByStructure = action;
    }
    @Override
    public ActionOnBlockFind structurePlacerAPI$getOnBlockReplacedByStructure(){
        return onBlockReplacedByStructure;
    }

    @Override
    public void structurePlacerAPI$setBlockPlacedCheck(TagKey<Block> blocks){
        blockPlacedCheck = blocks;
    }
    @Override
    public TagKey<Block> structurePlacerAPI$getBlockPlacedCheck(){
        return blockPlacedCheck;
    }
    @Override
    public void structurePlacerAPI$setBlockReplacedCheck(TagKey<Block> blocks){
        blockReplacedCheck = blocks;
    }
    @Override
    public TagKey<Block> structurePlacerAPI$getBlockReplacedCheck(){
        return blockReplacedCheck;
    }


    @Definition(id = "process", method = "Lnet/minecraft/structure/StructureTemplate;process(Lnet/minecraft/world/ServerWorldAccess;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/structure/StructurePlacementData;Ljava/util/List;)Ljava/util/List;")
    @Expression("process(?,?,?,?,?)")
    @ModifyExpressionValue(method = "place", at = @At("MIXINEXTRAS:EXPRESSION"))
    public List<StructureTemplate.StructureBlockInfo> modifyPlace(List<StructureTemplate.StructureBlockInfo> original, @Local(argsOnly = true) ServerWorldAccess world){
        if(isCustomStructureTemplate){

            List<StructureTemplate.StructureBlockInfo> filteredInfos = new ArrayList<>();
            original.forEach( structureBlockInfo -> {
                BlockState defaultState = world.getBlockState(structureBlockInfo.pos());

                /// Block action checks things
                // Checks if there is a potential action to be executed when a block from the saved structure is about to get placed
                if(actOnBlockStructurePlacing && structureBlockInfo.state().isIn(blockPlacedCheck)){
                    if(onBlockPlacingInStructure == null){
                        StructurePlacerAPI.LOGGER.error("The action to perform on block-placing is null!");
                    }else{
                        onBlockPlacingInStructure.action(structureBlockInfo, world);
                    }
                }
                // Checks if there is a potential action to be executed when the block from the world is about to be replaced by the one from the structure
                if(actOnBlockReplacedByStructure && defaultState.isIn(blockReplacedCheck)){
                    if(onBlockReplacedByStructure == null){
                        StructurePlacerAPI.LOGGER.error("The action to perform on block-placing is null!");
                    }else{
                        onBlockReplacedByStructure.action(structureBlockInfo, world);
                    }
                }

                /// Prevent block replacements
                //If we only have to replace tagged blocks, if the block found isn't tagged we should keep it along with its nbt data
                if(onlyReplaceTaggedBlocks && taggedBlocks != null && !defaultState.isIn(taggedBlocks)){
                    BlockEntity blockEntity = world.getBlockEntity(structureBlockInfo.pos());
                    StructureTemplate.StructureBlockInfo info;
                    if (blockEntity != null) {
                        info = new StructureTemplate.StructureBlockInfo(structureBlockInfo.pos(), defaultState, blockEntity.createNbtWithId());
                    } else {
                        info = new StructureTemplate.StructureBlockInfo(structureBlockInfo.pos(), defaultState, null);
                    }
                    filteredInfos.add(info);
                }
                //if the block found has a tag we should keep it along with its nbt
                else if(preventReplacementOfTagBlocks && taggedBlocks != null && defaultState.isIn(taggedBlocks)){
                    BlockEntity blockEntity = world.getBlockEntity(structureBlockInfo.pos());
                    StructureTemplate.StructureBlockInfo info;
                    if (blockEntity != null) {
                        //blockEntity.read(NbtReadView.create(logging.makeChild(blockEntity.getReporterContext()), world.getRegistryManager(), structureBlockInfo.nbt));
                        info = new StructureTemplate.StructureBlockInfo(structureBlockInfo.pos(), defaultState, blockEntity.createNbtWithId());
                    } else {
                        info = new StructureTemplate.StructureBlockInfo(structureBlockInfo.pos(), defaultState, null);
                    }
                    filteredInfos.add(info);
                }
                //If replace bedrock (or barriers) is false, it means that if we find bedrock it should remain there
                else if((!replaceBedrock && defaultState.isOf(Blocks.BEDROCK)) || (!replaceBarrier && defaultState.isOf(Blocks.BARRIER))){
                    filteredInfos.add(new StructureTemplate.StructureBlockInfo(structureBlockInfo.pos(), defaultState, null));
                }
                else{
                    filteredInfos.add(structureBlockInfo);
                }

            });
            return filteredInfos;
        }
        return original;
    }
}
