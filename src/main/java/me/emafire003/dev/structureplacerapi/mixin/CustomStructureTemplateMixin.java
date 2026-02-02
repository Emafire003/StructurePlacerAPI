package me.emafire003.dev.structureplacerapi.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import me.emafire003.dev.structureplacerapi.ICustomStructureTemplate;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.structure.StructureTemplate;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ServerWorldAccess;
import org.spongepowered.asm.mixin.Debug;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

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


    /*@Inject(method = "place", at = @At(value = "INVOKE", target = "Lcom/mojang/datafixers/util/Pair;getFirst()Ljava/lang/Object;", ordinal = 1))
    public void injectPlace(ServerWorldAccess world, BlockPos pos, BlockPos pivot, StructurePlacementData placementData, Random random, int flags, CallbackInfoReturnable<Boolean> cir, @Local(ordinal = 0) Pair pair2){
        System.out.println("ok the current pos is " + pair2.getFirst());
    }*/

    /*@Inject(method = "place", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/ServerWorldAccess;setBlockState(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;I)Z", ordinal = 1, shift = At.Shift.BEFORE))
    public void injectPlace1(ServerWorldAccess world, BlockPos pos, BlockPos pivot, StructurePlacementData placementData, Random random, int flags, CallbackInfoReturnable<Boolean> cir, @Local StructureTemplate.StructureBlockInfo info, @Local FluidState fluidState, @Local BlockState state){
        if(world.getBlockState(info.pos()).isOf(Blocks.BEDROCK)){
            info = new StructureTemplate.StructureBlockInfo(info.pos(), world.getBlockState(info.pos()), null);
            if (info.nbt() != null) {
                BlockEntity blockEntity = world.getBlockEntity(info.pos());
                Clearable.clear(blockEntity);
                world.setBlockState(info.pos(), Blocks.BARRIER.getDefaultState(), 20);
            }
        }

    }*/
    /*

    @Definition(id = "process", method = "Lnet/minecraft/structure/StructureTemplate;process(Lnet/minecraft/world/ServerWorldAccess;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/structure/StructurePlacementData;Ljava/util/List;)Ljava/util/List;")
    @Expression("process(?, ?, ?, ?, ?)")
    @ModifyExpressionValue(method = "place", at = @At("MIXINEXTRAS:EXPRESSION"))
    public void modifyPlace(){

    }*/
/*
    @WrapOperation(
            method = "process",
            at = @At(value = "NEW", target = "Lnet/minecraft/structure/StructureTemplate$StructureBlockInfo")
            //at = @At(value = "INVOKE", target = ";<init>(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;Lnet/minecraft/nbt/NbtCompound;)V")
    )
    private static StructureTemplate.StructureBlockInfo injectProcess(){
        System.out.println("MIXIN IS INNNNNNNNNNNNNNNN");
        BlockState defaultState = world.getBlockState(originalBlockInfo.pos());
        if(defaultState.isOf(Blocks.BEDROCK)){
            return new StructureTemplate.StructureBlockInfo(originalBlockInfo.pos(), defaultState, null);
        }
        return originalBlockInfo;
    }
    */

    @WrapOperation(method = "process", at = @At(value = "NEW", target = "Lnet/minecraft/structure/StructureTemplate$StructureBlockInfo;"))
    private static StructureTemplate.StructureBlockInfo injectThing(BlockPos pos, BlockState state, NbtCompound nbt, Operation<StructureTemplate.StructureBlockInfo> original, @Local(argsOnly = true) ServerWorldAccess world){
        //TODO actually implement the logic
        BlockState defaultState = world.getBlockState(pos);
        if(defaultState.isOf(Blocks.BEDROCK)){
            return new StructureTemplate.StructureBlockInfo(pos, defaultState, null);
        }
        return new StructureTemplate.StructureBlockInfo(pos, state, nbt);
    }
}
