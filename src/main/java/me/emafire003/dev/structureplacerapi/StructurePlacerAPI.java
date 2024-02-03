package me.emafire003.dev.structureplacerapi;

import com.google.common.collect.Lists;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.structure.StructurePlacementData;
import net.minecraft.structure.StructureTemplate;
import net.minecraft.structure.StructureTemplateManager;
import net.minecraft.structure.processor.BlockRotStructureProcessor;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3i;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.EmptyBlockView;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalTime;
import java.util.*;

public class StructurePlacerAPI {

    private ServerWorld world;
    private Identifier templateName;
    private BlockPos blockPos;
    private BlockMirror mirror;
    private BlockRotation rotation;
    private boolean ignoreEntities;
    private float integrity;
    private BlockPos offset;

    private Logger LOGGER = LoggerFactory.getLogger("structureplacerapi");

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

            if(!this.world.isClient()){
                saveFromWorld(this.world, this.blockPos.add(this.offset), size);
                scheduleReplacement(restore_ticks);
            }
            return optional.isPresent() && this.place(optional.get());
        } else {
            return false;
        }
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
                for(StructureTemplate.StructureBlockInfo info : blockInfoList){
                    world.setBlockState(info.pos(), info.state());
                    BlockEntity blockEntity = world.getBlockEntity(info.pos());
                    if (blockEntity != null) {
                        blockEntity.readNbt(info.nbt());
                    }
                }
                tickCounter = -1;

            }else{
                tickCounter++;
            }
        }));
    }



    private Vec3i size = Vec3i.ZERO;
    private final List<StructureTemplate.StructureBlockInfo> blockInfoList = Lists.newArrayList();

    /**Saves the block infos to <i>blockInfoLists</i>
     */
    //TODO might seriously impact performance
    //FIXED (yay) but there is a dupe glitch.
    private void saveFromWorld(World world, BlockPos start, Vec3i dimensions) {
        /*if (dimensions.getX() >= 1 && dimensions.getY() >= 1 && dimensions.getZ() >= 1) {

        }*/
        Instant start_time = Instant.now();
        LOGGER.debug("Saving terrain to later restore it...");
        BlockPos blockPos = start.add(dimensions).add(-1, -1, -1);
        BlockPos min_pos = new BlockPos(Math.min(start.getX(), blockPos.getX()), Math.min(start.getY(), blockPos.getY()), Math.min(start.getZ(), blockPos.getZ()));
        BlockPos max_pos = new BlockPos(Math.max(start.getX(), blockPos.getX()), Math.max(start.getY(), blockPos.getY()), Math.max(start.getZ(), blockPos.getZ()));

        //Iterates through all the block positions and adds them to list
        BlockPos.iterate(min_pos, max_pos).iterator().forEachRemaining(pos -> {
            //Copying the pos so it doesn't mutate and make a mess
            BlockPos save_pos;
            save_pos = new BlockPos(pos.getX(), pos.getY(), pos.getZ());

            BlockEntity blockEntity = world.getBlockEntity(save_pos);
            StructureTemplate.StructureBlockInfo structureBlockInfo;

            if (blockEntity != null) {
                structureBlockInfo = new StructureTemplate.StructureBlockInfo(save_pos, world.getBlockState(save_pos), blockEntity.createNbtWithId());
            } else {
                structureBlockInfo = new StructureTemplate.StructureBlockInfo(save_pos, world.getBlockState(save_pos), null);
            }
            blockInfoList.add(structureBlockInfo);
        }
        );

        Instant end_time = Instant.now();
        Duration timeElapsed = Duration.between(start_time, end_time);

        LOGGER.debug("Terrain saved! It took: " + timeElapsed.toMillis() + "ms");
    }
}
