package me.emafire003.dev.structureplacerapi.mixin;

import com.llamalad7.mixinextras.expression.Definition;
import com.llamalad7.mixinextras.expression.Expression;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import me.emafire003.dev.structureplacerapi.ActionOnBlockFind;
import me.emafire003.dev.structureplacerapi.ICustomStructureTemplate;
import me.emafire003.dev.structureplacerapi.StructurePlacerAPI;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
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
    public boolean structurePlacerAPI$isCustomStructureTemplate = false;
    @Unique
    public boolean structurePlacerAPI$replaceBedrock = false;
    @Unique
    public boolean structurePlacerAPI$replaceBarrier = false;
    @Unique
    public boolean structurePlacerAPI$preventReplacementOfTagBlocks = false;
    @Unique
    public boolean structurePlacerAPI$onlyReplaceTaggedBlocks = false;
    @Unique
    public TagKey<Block> structurePlacerAPI$taggedBlocks = null;

    @Unique
    public boolean structurePlacerAPI$actOnBlockStructurePlacing = false;
    @Unique
    public boolean structurePlacerAPI$actOnBlockReplacedByStructure = false;
    @Unique /// aka check the block IN the structure
    public ActionOnBlockFind structurePlacerAPI$onBlockPlacingInStructure;
    @Unique /// aka check the block that is getting replced BY the structure
    public ActionOnBlockFind structurePlacerAPI$onBlockReplacedByStructure;
    @Unique
    public TagKey<Block> structurePlacerAPI$blockPlacedCheck;
    @Unique
    public TagKey<Block> structurePlacerAPI$blockReplacedCheck;

    @Override
    public void structurePlacerAPI$setCustom(boolean custom) {
        structurePlacerAPI$isCustomStructureTemplate = custom;
    }

    @Override
    public boolean structurePlacerAPI$isCustom() {
        return structurePlacerAPI$isCustomStructureTemplate;
    }

    @Override
    public void structurePlacerAPI$setReplaceBedrock(boolean replace) {
        structurePlacerAPI$replaceBedrock = replace;
    }

    @Override
    public boolean structurePlacerAPI$getReplaceBedrock() {
        return structurePlacerAPI$replaceBedrock;
    }

    @Override
    public void structurePlacerAPI$setReplaceBarrier(boolean replace) {
        structurePlacerAPI$replaceBarrier = replace;
    }

    @Override
    public boolean structurePlacerAPI$getReplaceBarrier() {
        return structurePlacerAPI$replaceBarrier;
    }

    @Override
    public void structurePlacerAPI$setOnlyReplaceTagBlocks(boolean replace, TagKey<Block> tag){
        structurePlacerAPI$onlyReplaceTaggedBlocks = replace;
        structurePlacerAPI$setTaggedBlocks(tag);
    }

    @Override
    public boolean structurePlacerAPI$getOnlyReplaceTagBlocks(){
        return structurePlacerAPI$onlyReplaceTaggedBlocks;
    }

    @Override
    public void structurePlacerAPI$setPreventReplacementOfTagBlocks(boolean replace, TagKey<Block> tag){
        structurePlacerAPI$preventReplacementOfTagBlocks = replace;
        structurePlacerAPI$setTaggedBlocks(tag);
    }

    @Override
    public boolean structurePlacerAPI$getPreventReplacementOfTagBlocks(){
        return structurePlacerAPI$preventReplacementOfTagBlocks;
    }

    @Override
    public void structurePlacerAPI$setTaggedBlocks(TagKey<Block> tag){
        structurePlacerAPI$taggedBlocks = tag;
    }

    @Override
    public TagKey<Block> structurePlacerAPI$getTaggedBlocks(){
        return structurePlacerAPI$taggedBlocks;
    }

    @Override
    public void structurePlacerAPI$setActOnBlockStructurePlacing(boolean b){
        structurePlacerAPI$actOnBlockStructurePlacing = b;
    }
    @Override
    public boolean structurePlacerAPI$getActOnBlockStructurePlacing(){
        return structurePlacerAPI$actOnBlockStructurePlacing;
    }
    @Override
    public void structurePlacerAPI$setActOnBlockReplacedByStructure(boolean b){
        structurePlacerAPI$actOnBlockReplacedByStructure = b;
    }
    @Override
    public boolean structurePlacerAPI$getActOnBlockReplacedByStructure(){
        return structurePlacerAPI$actOnBlockReplacedByStructure;
    }

    @Override
    public void structurePlacerAPI$setOnBlockPlacingInStructure(ActionOnBlockFind action){
        structurePlacerAPI$onBlockPlacingInStructure = action;
    }
    @Override
    public ActionOnBlockFind structurePlacerAPI$getOnBlockPlacingInStructure(){
        return structurePlacerAPI$onBlockPlacingInStructure;
    }

    @Override
    public void structurePlacerAPI$setOnBlockReplacedByStructure(ActionOnBlockFind action){
        structurePlacerAPI$onBlockReplacedByStructure = action;
    }
    @Override
    public ActionOnBlockFind structurePlacerAPI$getOnBlockReplacedByStructure(){
        return structurePlacerAPI$onBlockReplacedByStructure;
    }

    @Override
    public void structurePlacerAPI$setBlockPlacedCheck(TagKey<Block> blocks){
        structurePlacerAPI$blockPlacedCheck = blocks;
    }
    @Override
    public TagKey<Block> structurePlacerAPI$getBlockPlacedCheck(){
        return structurePlacerAPI$blockPlacedCheck;
    }
    @Override
    public void structurePlacerAPI$setBlockReplacedCheck(TagKey<Block> blocks){
        structurePlacerAPI$blockReplacedCheck = blocks;
    }
    @Override
    public TagKey<Block> structurePlacerAPI$getBlockReplacedCheck(){
        return structurePlacerAPI$blockReplacedCheck;
    }

    @Definition(id = "processBlockInfos", method = "Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureTemplate;processBlockInfos(Lnet/minecraft/world/level/ServerLevelAccessor;Lnet/minecraft/core/BlockPos;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructurePlaceSettings;Ljava/util/List;Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureTemplate;)Ljava/util/List;")
    @Expression("processBlockInfos(?,?,?,?,?,?)")
    @ModifyExpressionValue(method = "placeInWorld", at = @At("MIXINEXTRAS:EXPRESSION"))
    public List<StructureTemplate.StructureBlockInfo> modifyPlace(List<StructureTemplate.StructureBlockInfo> original, @Local(argsOnly = true) ServerLevelAccessor world){
        if(structurePlacerAPI$isCustomStructureTemplate){

            List<StructureTemplate.StructureBlockInfo> filteredInfos = new ArrayList<>();
            original.forEach( structureBlockInfo -> {
                BlockState defaultState = world.getBlockState(structureBlockInfo.pos());

                /// Block action checks things
                // Checks if there is a potential action to be executed when a block from the saved structure is about to get placed
                if(structurePlacerAPI$actOnBlockStructurePlacing && structureBlockInfo.state().is(structurePlacerAPI$blockPlacedCheck)){
                    if(structurePlacerAPI$onBlockPlacingInStructure == null){
                        StructurePlacerAPI.LOGGER.error("The action to perform on block-placing is null!");
                    }else{
                        structurePlacerAPI$onBlockPlacingInStructure.action(structureBlockInfo, world);
                    }
                }
                // Checks if there is a potential action to be executed when the block from the world is about to be replaced by the one from the structure
                if(structurePlacerAPI$actOnBlockReplacedByStructure && defaultState.is(structurePlacerAPI$blockReplacedCheck)){
                    if(structurePlacerAPI$onBlockReplacedByStructure == null){
                        StructurePlacerAPI.LOGGER.error("The action to perform on block-placing is null!");
                    }else{
                        structurePlacerAPI$onBlockReplacedByStructure.action(structureBlockInfo, world);
                    }
                }

                /// Prevent block replacements
                //If we only have to replace tagged blocks, if the block found isn't tagged we should keep it along with its nbt data
                if(structurePlacerAPI$onlyReplaceTaggedBlocks && structurePlacerAPI$taggedBlocks != null && !defaultState.is(structurePlacerAPI$taggedBlocks)){
                    BlockEntity blockEntity = world.getBlockEntity(structureBlockInfo.pos());
                    StructureTemplate.StructureBlockInfo info;
                    if (blockEntity != null) {
                        info = new StructureTemplate.StructureBlockInfo(structureBlockInfo.pos(), defaultState, blockEntity.saveWithId(world.registryAccess()));
                    } else {
                        info = new StructureTemplate.StructureBlockInfo(structureBlockInfo.pos(), defaultState, null);
                    }
                    filteredInfos.add(info);
                }
                //if the block found has a tag we should keep it along with its nbt
                else if(structurePlacerAPI$preventReplacementOfTagBlocks && structurePlacerAPI$taggedBlocks != null && defaultState.is(structurePlacerAPI$taggedBlocks)){
                    BlockEntity blockEntity = world.getBlockEntity(structureBlockInfo.pos());
                    StructureTemplate.StructureBlockInfo info;
                    if (blockEntity != null) {
                        info = new StructureTemplate.StructureBlockInfo(structureBlockInfo.pos(), defaultState, blockEntity.saveWithId(world.registryAccess()));
                    } else {
                        info = new StructureTemplate.StructureBlockInfo(structureBlockInfo.pos(), defaultState, null);
                    }
                    filteredInfos.add(info);
                }
                //If replace bedrock (or barriers) is false, it means that if we find bedrock it should remain there
                else if((!structurePlacerAPI$replaceBedrock && defaultState.is(Blocks.BEDROCK)) || (!structurePlacerAPI$replaceBarrier && defaultState.is(Blocks.BARRIER))){
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
