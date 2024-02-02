package me.emafire003.dev.structureplacerapi;

import com.google.common.collect.Lists;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.structure.StructurePlacementData;
import net.minecraft.structure.StructureTemplate;
import net.minecraft.structure.StructureTemplateManager;
import net.minecraft.structure.processor.BlockRotStructureProcessor;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3i;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.EmptyBlockView;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

public class StructurePlacerAPI {

    private ServerWorld world;
    private Identifier templateName;
    private BlockPos blockPos;
    private BlockMirror mirror;
    private BlockRotation rotation;
    private boolean ignoreEntities;
    private float integrity;
    private BlockPos offset;

    /**
     * With this you can create placer object which will spawn
     * a new structure from an nbt file located in /data/modid/structures
     *
     * @param world The ServerWorld in which to place the structure
     * @param templateName The identifier of the structure to place, like <code>new Identifier(MOD_ID, structure_name)</code>
     * @param blockPos The position of the structure
     * @param mirror Use this to mirror the structure using <code>BlockMirror.#</code>
     * @param rotation Use this to rotate the structure using <code>BlockRotation.#</code>
     * @param ignoreEntities Set to true to block the spawning of entities saved in the structure file
     * @param integrity Set this to a value between 0f and 1f to remove some blocks from the placed structure. (All blocks = 1f)
     * @param offset Use this to offset the placing of the structure.
     * */
    public StructurePlacerAPI(ServerWorld world, Identifier templateName, BlockPos blockPos, BlockMirror mirror, BlockRotation rotation, boolean ignoreEntities, float integrity, BlockPos offset){
        this.world = world;
        this.templateName = templateName;
        this.blockPos = blockPos;
        this.mirror = mirror;
        this.rotation = rotation;
        this.ignoreEntities = ignoreEntities;
        this.integrity = integrity;
        this.offset = offset;
    }

    /**
     * With this you can create placer object which will spawn
     * a new structure from an nbt file located in /data/modid/structures
     *
     * @param world The ServerWorld in which to place the structure
     * @param templateName The identifier of the structure to place, like <code>new Identifier(MOD_ID, structure_name)</code>
     * @param blockPos The position of the structure
     * */
    public StructurePlacerAPI(ServerWorld world, Identifier templateName, BlockPos blockPos) {
        this.world = world;
        this.templateName = templateName;
        this.blockPos = blockPos;
        this.mirror = BlockMirror.NONE;
        this.rotation = BlockRotation.NONE;
        this.ignoreEntities = true;
        this.integrity = 1.0f;
        this.offset = new BlockPos(0, 0, 0);
    }

    /**
     * With this you can create placer object which will spawn
     * a new structure from an nbt file located in /data/modid/structures
     *
     * @param world The ServerWorld in which to place the structure
     * @param templateName The identifier of the structure to place, like <code>new Identifier(MOD_ID, structure_name)</code>
     * @param blockPos The position of the structure
     * @param offset Use this to offset the placing of the structure.
     * */
    public StructurePlacerAPI(ServerWorld world, Identifier templateName, BlockPos blockPos, BlockPos offset){
        this.world = world;
        this.templateName = templateName;
        this.blockPos = blockPos;
        this.mirror = BlockMirror.NONE;
        this.rotation = BlockRotation.NONE;
        this.ignoreEntities = true;
        this.integrity = 1.0f;
        this.offset = offset;
    }

    /**
     * With this you can create placer object which will spawn
     * a new structure from an nbt file located in /data/modid/structures
     *
     * @param world The ServerWorld in which to place the structure
     * @param templateName The identifier of the structure to place, like <code>new Identifier(MOD_ID, structure_name)</code>
     * @param blockPos The position of the structure
     * @param mirror Use this to mirror the structure using <code>BlockMirror.#</code>
    */
    public StructurePlacerAPI(ServerWorld world, Identifier templateName, BlockPos blockPos, BlockMirror mirror){
        this.world = world;
        this.templateName = templateName;
        this.blockPos = blockPos;
        this.mirror = mirror;
        this.rotation = BlockRotation.NONE;
        this.ignoreEntities = true;
        this.integrity = 1.0f;
        this.offset = new BlockPos(0, 0, 0);
    }

    /**
     * With this you can create placer object which will spawn
     * a new structure from an nbt file located in /data/modid/structures
     *
     * @param world The ServerWorld in which to place the structure
     * @param templateName The identifier of the structure to place, like <code>new Identifier(MOD_ID, structure_name)</code>
     * @param blockPos The position of the structure
     * @param rotation Use this to rotate the structure using <code>BlockRotation.#</code>
     * */
    public StructurePlacerAPI(ServerWorld world, Identifier templateName, BlockPos blockPos, BlockRotation rotation){
        this.world = world;
        this.templateName = templateName;
        this.blockPos = blockPos;
        this.mirror = BlockMirror.NONE;
        this.rotation = rotation;
        this.ignoreEntities = true;
        this.integrity = 1.0f;
        this.offset = new BlockPos(0, 0, 0);
    }

    /**
     * With this you can create placer object which will spawn
     * a new structure from an nbt file located in /data/modid/structures
     *
     * @param world The ServerWorld in which to place the structure
     * @param templateName The identifier of the structure to place, like <code>new Identifier(MOD_ID, structure_name)</code>
     * @param blockPos The position of the structure
     * @param mirror Use this to mirror the structure using <code>BlockMirror.#</code>
     * @param rotation Use this to rotate the structure using <code>BlockRotation.#</code>
     * */
    public StructurePlacerAPI(ServerWorld world, Identifier templateName, BlockPos blockPos, BlockMirror mirror, BlockRotation rotation){
        this.world = world;
        this.templateName = templateName;
        this.blockPos = blockPos;
        this.mirror = mirror;
        this.rotation = rotation;
        this.ignoreEntities = true;
        this.integrity = 1.0f;
        this.offset = new BlockPos(0, 0, 0);
    }

    /**
     * With this you can create placer object which will spawn
     * a new structure from an nbt file located in /data/modid/structures
     *
     * @param world The ServerWorld in which to place the structure
     * @param templateName The identifier of the structure to place, like <code>new Identifier(MOD_ID, structure_name)</code>
     * @param blockPos The position of the structure
    * @param integrity Set this to a value between 0f and 1f to remove some blocks from the placed structure. (All blocks = 1f)
     * */
    public StructurePlacerAPI(ServerWorld world, Identifier templateName, BlockPos blockPos, float integrity){
        this.world = world;
        this.templateName = templateName;
        this.blockPos = blockPos;
        this.mirror = BlockMirror.NONE;
        this.rotation = BlockRotation.NONE;
        this.ignoreEntities = true;
        this.integrity = integrity;
        this.offset = new BlockPos(0, 0, 0);
    }

    /**Use this method to load the structure into the world and
     * spawn it. You can check to see if the placing was succesful.
     */
    public boolean loadStructure() {
        if (this.templateName != null) {
            StructureTemplateManager structureTemplateManager = world.getStructureTemplateManager();
            Optional<StructureTemplate> optional;
            try {
                optional = structureTemplateManager.getTemplate(this.templateName);
            } catch (InvalidIdentifierException var6) {
                return false;
            }
            //It may be needed for the restoration
            optional.ifPresent(structureTemplate -> this.size = structureTemplate.getSize());

            return optional.isPresent() && this.place(optional.get());
        } else {
            return false;
        }
    }

    /**This method is used by the <code>loadStructure()</code> method,
     * which already checks if the structure exists or not, so use that instead*/
    private boolean place(StructureTemplate template) {
        try {
            StructurePlacementData structurePlacementData = (new StructurePlacementData()).setMirror(this.mirror).setRotation(this.rotation).setIgnoreEntities(this.ignoreEntities);
            if (this.integrity < 1.0F) {
                structurePlacementData.clearProcessors().addProcessor(new BlockRotStructureProcessor(MathHelper.clamp(this.integrity, 0.0F, 1.0F))).setRandom(createRandom(this.world.getSeed()));
            }
            BlockPos blockPos2 = blockPos.add(this.offset);
            template.place(world, blockPos2, blockPos2, structurePlacementData, createRandom(this.world.getSeed()), 2);
            unloadStructure();
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    /**This method unloads the structure after it has been placed.
     * No need to use it on your own, included during placement*/
    private void unloadStructure() {
        if (this.templateName != null) {
            StructureTemplateManager structureTemplateManager = world.getStructureTemplateManager();
            structureTemplateManager.unloadTemplate(this.templateName);
        }
    }

    /**This method creates a random seed for the integrity run-down effect.
     * No need to use it on your own, included during placement*/
    public static Random createRandom(long seed) {
        return seed == 0L ? Random.create(Util.getMeasuringTimeMs()) : Random.create(seed);
    }




    /**Use this method to load the structure into the world and
     * spawn it. You can check to see if the placing was succesful.
     *
     * It will also restore the blocks it replaced after <i>restore_ticks</i>
     * Calling this function from the client only will not regenerate the old terrain.
     * And will probably cause other issues.
     *
     * @param restore_ticks Number of ticks (1 second = 20 ticks) after which the world would be restored.
     */
    public boolean loadAndRestoreStructure(int restore_ticks) {
        if(!this.world.isClient()){
            saveFromWorld(this.world, this.blockPos, size, null);
            scheduleReplacement(restore_ticks);
        }
        return loadStructure();
    }

    private int tickCounter = 0;

    /**Schedules the replacement of the older terrain/world and
     * after <i>ticks</i> it executes the replacement.
     *
     * TO CALL SERVER SIDE ONLY*/
    private void scheduleReplacement(int ticks){
       ServerTickEvents.END_SERVER_TICK.register((server -> {
           if(tickCounter == -1){
               return;
           }

           if(tickCounter == ticks){
               for(StructureTemplate.StructureBlockInfo info : blockInfoLists){
                   world.setBlockState(info.pos(), info.state());
                   BlockEntity blockEntity = world.getBlockEntity(info.pos());
                   if (blockEntity != null) {
                       blockEntity.readNbt(info.nbt());
                   }
               }
               tickCounter = -1;
           }
           tickCounter++;
       }));
    }


    private Vec3i size = Vec3i.ZERO;
    private final List<StructureTemplate.StructureBlockInfo> blockInfoLists = Lists.newArrayList();

    /**Saves the block infos to <i>blockInfoLists</i>
     *
     * It's the same stuff used inside the StructureBlockEntity*/
    private void saveFromWorld(World world, BlockPos start, Vec3i dimensions, @Nullable Block ignoredBlock) {
        if (dimensions.getX() >= 1 && dimensions.getY() >= 1 && dimensions.getZ() >= 1) {
            BlockPos blockPos = start.add(dimensions).add(-1, -1, -1);
            List<StructureTemplate.StructureBlockInfo> list = Lists.newArrayList();
            List<StructureTemplate.StructureBlockInfo> list2 = Lists.newArrayList();
            List<StructureTemplate.StructureBlockInfo> list3 = Lists.newArrayList();
            BlockPos blockPos2 = new BlockPos(Math.min(start.getX(), blockPos.getX()), Math.min(start.getY(), blockPos.getY()), Math.min(start.getZ(), blockPos.getZ()));
            BlockPos blockPos3 = new BlockPos(Math.max(start.getX(), blockPos.getX()), Math.max(start.getY(), blockPos.getY()), Math.max(start.getZ(), blockPos.getZ()));
            Iterator<BlockPos> var12 = BlockPos.iterate(blockPos2, blockPos3).iterator();

            while(true) {
                BlockPos blockPos4;
                BlockPos blockPos5;
                BlockState blockState;
                do {
                    if (!var12.hasNext()) {
                        List<StructureTemplate.StructureBlockInfo> list4 = combineSorted(list, list2, list3);
                        this.blockInfoLists.clear();
                        this.blockInfoLists.addAll(list4);
                        return;
                    }
                    blockPos4 = var12.next();
                    blockPos5 = blockPos4.subtract(blockPos2);
                    blockState = world.getBlockState(blockPos4);
                } while(ignoredBlock != null && blockState.isOf(ignoredBlock));

                BlockEntity blockEntity = world.getBlockEntity(blockPos4);
                StructureTemplate.StructureBlockInfo structureBlockInfo;
                if (blockEntity != null) {
                    structureBlockInfo = new StructureTemplate.StructureBlockInfo(blockPos5, blockState, blockEntity.createNbtWithId());
                } else {
                    structureBlockInfo = new StructureTemplate.StructureBlockInfo(blockPos5, blockState, null);
                }

                categorize(structureBlockInfo, list, list2, list3);
            }
        }
    }

    /**
     * Categorizes {@code blockInfo} based on its properties, modifying
     * the passed lists in-place.
     *
     * <p>If the block has an NBT associated with it, then it will be
     * put in {@code blocksWithNbt}. If the block does not have an NBT
     * associated with it, but is always a full cube, then it will be
     * put in {@code fullBlocks}. Otherwise, it will be put in
     * {@code otherBlocks}.
     *
     * @apiNote After all blocks are categorized, {@link #combineSorted}
     * should be called with the same parameters to get the final list.
     */
    private static void categorize(StructureTemplate.StructureBlockInfo blockInfo, List<StructureTemplate.StructureBlockInfo> fullBlocks, List<StructureTemplate.StructureBlockInfo> blocksWithNbt, List<StructureTemplate.StructureBlockInfo> otherBlocks) {
        if (blockInfo.nbt() != null) {
            blocksWithNbt.add(blockInfo);
        } else if (!blockInfo.state().getBlock().hasDynamicBounds() && blockInfo.state().isFullCube(EmptyBlockView.INSTANCE, BlockPos.ORIGIN)) {
            fullBlocks.add(blockInfo);
        } else {
            otherBlocks.add(blockInfo);
        }
    }

    /**Returns:
     the list that sorts and combines the passed block lists
     API Note:
     The parameters passed should be the same one that was passed to previous calls to categorize. The returned value is meant to be passed to StructureTemplate.PalettedBlockInfoList.
     Implementation Note:
     Each list passed will be sorted in-place using the items' Y, X, and Z coordinates. The returned list contains all items of fullBlocks, otherBlocks, and blocksWithNbt in this order.*/
    private static List<StructureTemplate.StructureBlockInfo> combineSorted(List<StructureTemplate.StructureBlockInfo> fullBlocks, List<StructureTemplate.StructureBlockInfo> blocksWithNbt, List<StructureTemplate.StructureBlockInfo> otherBlocks) {
        Comparator<StructureTemplate.StructureBlockInfo> comparator = Comparator.comparingInt((
                StructureTemplate.StructureBlockInfo blockInfo) -> blockInfo.pos().getY()).thenComparingInt((blockInfo) -> blockInfo.pos().getX()).thenComparingInt((blockInfo) -> blockInfo.pos().getZ());
        fullBlocks.sort(comparator);
        otherBlocks.sort(comparator);
        blocksWithNbt.sort(comparator);
        List<StructureTemplate.StructureBlockInfo> list = Lists.newArrayList();
        list.addAll(fullBlocks);
        list.addAll(otherBlocks);
        list.addAll(blocksWithNbt);
        return list;
    }
}
