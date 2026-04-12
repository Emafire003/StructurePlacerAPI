package me.emafire003.dev.structureplacerapi.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import me.emafire003.dev.structureplacerapi.ActionOnBlockFind;
import me.emafire003.dev.structureplacerapi.ICustomStructureTemplate;
import me.emafire003.dev.structureplacerapi.StructurePlacerAPI;
import net.minecraft.tags.TagKey;
import net.minecraft.util.ProblemReporter;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraft.world.level.storage.TagValueOutput;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

import java.util.ArrayList;
import java.util.List;

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

    @ModifyExpressionValue(method = "placeInWorld", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureTemplate;processBlockInfos(Lnet/minecraft/world/level/ServerLevelAccessor;Lnet/minecraft/core/BlockPos;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructurePlaceSettings;Ljava/util/List;)Ljava/util/List;"))
    public List<StructureTemplate.StructureBlockInfo> modifyPlace(List<StructureTemplate.StructureBlockInfo> original, @Local(argsOnly = true, name = "level") ServerLevelAccessor world){
        if(isCustomStructureTemplate){

            List<StructureTemplate.StructureBlockInfo> filteredInfos = new ArrayList<>();
            original.forEach( structureBlockInfo -> {
                BlockState defaultState = world.getBlockState(structureBlockInfo.pos());

                boolean addedWithAction = false;
                /// Block action checks things
                // Checks if there is a potential action to be executed when a block from the saved structure is about to get placed
                if(actOnBlockStructurePlacing && structureBlockInfo.state().is(blockPlacedCheck)){
                    if(onBlockPlacingInStructure == null){
                        StructurePlacerAPI.LOGGER.error("The action to perform on block-placing is null!");
                    }else{
                        StructureTemplate.StructureBlockInfo structureBlockInfoNew = onBlockPlacingInStructure.action(structureBlockInfo, world);
                        filteredInfos.add(structureBlockInfoNew); //If this block is replacing something that it shouldn't,
                        // it should still get overridden by the new info generated below, since they have same position
                        addedWithAction = true;
                    }
                }
                // Checks if there is a potential action to be executed when the block from the world is about to be replaced by the one from the structure
                if(actOnBlockReplacedByStructure && defaultState.is(blockReplacedCheck)){
                    if(onBlockReplacedByStructure == null){
                        StructurePlacerAPI.LOGGER.error("The action to perform on block-replacing is null!");
                    }else{
                        StructureTemplate.StructureBlockInfo structureBlockInfoNew = onBlockReplacedByStructure.action(structureBlockInfo, world);
                        filteredInfos.add(structureBlockInfoNew);
                        addedWithAction = true;
                    }
                }

                /// Prevent block replacements
                //If we only have to replace tagged blocks, if the block found isn't tagged we should keep it along with its nbt data
                if(onlyReplaceTaggedBlocks && taggedBlocks != null && !defaultState.is(taggedBlocks)){
                    BlockEntity blockEntity = world.getBlockEntity(structureBlockInfo.pos());
                    StructureTemplate.StructureBlockInfo info;
                    if (blockEntity != null) {
                        TagValueOutput nbtWriteView = TagValueOutput.createWithContext(new ProblemReporter.ScopedCollector(StructurePlacerAPI.LOGGER), world.registryAccess());
                        blockEntity.saveWithId(nbtWriteView);
                        info = new StructureTemplate.StructureBlockInfo(structureBlockInfo.pos(), defaultState, nbtWriteView.buildResult());
                    } else {
                        info = new StructureTemplate.StructureBlockInfo(structureBlockInfo.pos(), defaultState, null);
                    }
                    filteredInfos.add(info);
                }
                //if the block found has a tag we should keep it along with its nbt
                else if(preventReplacementOfTagBlocks && taggedBlocks != null && defaultState.is(taggedBlocks)){
                    BlockEntity blockEntity = world.getBlockEntity(structureBlockInfo.pos());
                    StructureTemplate.StructureBlockInfo info;
                    if (blockEntity != null) {
                        TagValueOutput nbtWriteView = TagValueOutput.createWithContext(new ProblemReporter.ScopedCollector(StructurePlacerAPI.LOGGER), world.registryAccess());
                        blockEntity.saveWithId(nbtWriteView);
                        info = new StructureTemplate.StructureBlockInfo(structureBlockInfo.pos(), defaultState, nbtWriteView.buildResult());
                    } else {
                        info = new StructureTemplate.StructureBlockInfo(structureBlockInfo.pos(), defaultState, null);
                    }
                    filteredInfos.add(info);
                }
                //If replace bedrock (or barriers) is false, it means that if we find bedrock it should remain there
                else if((!replaceBedrock && defaultState.is(Blocks.BEDROCK)) || (!replaceBarrier && defaultState.is(Blocks.BARRIER))){
                    filteredInfos.add(new StructureTemplate.StructureBlockInfo(structureBlockInfo.pos(), defaultState, null));
                }
                else if(!addedWithAction){
                    filteredInfos.add(structureBlockInfo);
                }

            });
            return filteredInfos;
        }
        return original;
    }
}
