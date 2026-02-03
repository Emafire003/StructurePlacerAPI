package me.emafire003.dev.structureplacerapi.mixin;

import com.llamalad7.mixinextras.expression.Definition;
import com.llamalad7.mixinextras.expression.Expression;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import me.emafire003.dev.structureplacerapi.ICustomStructureTemplate;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
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
    public boolean onlyReplaceAir = false;

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
    public void structurePlacerAPI$setOnlyReplaceAir(boolean replaceAir) {
        onlyReplaceAir = replaceAir;
    }

    @Override
    public boolean structurePlacerAPI$getOnlyReplaceAir() {
        return onlyReplaceAir;
    }


    @Definition(id = "process", method = "Lnet/minecraft/structure/StructureTemplate;process(Lnet/minecraft/world/ServerWorldAccess;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/structure/StructurePlacementData;Ljava/util/List;)Ljava/util/List;")
    @Expression("process(?,?,?,?,?)")
    @ModifyExpressionValue(method = "place", at = @At("MIXINEXTRAS:EXPRESSION"))
    public List<StructureTemplate.StructureBlockInfo> modifyPlace(List<StructureTemplate.StructureBlockInfo> original, @Local(argsOnly = true) ServerWorldAccess world){
        if(isCustomStructureTemplate){
            List<StructureTemplate.StructureBlockInfo> filteredInfos = new ArrayList<>();
            original.forEach( structureBlockInfo -> {
                BlockState defaultState = world.getBlockState(structureBlockInfo.pos());
                if(defaultState.isOf(Blocks.BEDROCK)){
                    filteredInfos.add(new StructureTemplate.StructureBlockInfo(structureBlockInfo.pos(), defaultState, null));
                }else{
                    filteredInfos.add(structureBlockInfo);
                }
            });
            return filteredInfos;
        }
        return original;
    }
}
