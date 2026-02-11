package me.emafire003.dev.structureplacerapi;

import net.minecraft.ResourceLocationException;
import net.minecraft.Util;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.RandomizableContainerBlockEntity;
import net.minecraft.world.Container;
import net.minecraft.tags.TagKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;
import net.minecraft.world.level.levelgen.structure.templatesystem.BlockRotProcessor;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.core.Vec3i;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.tick.ServerTickEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@SuppressWarnings("unused")
@Mod("structureplacerapi")
public class StructurePlacerAPI {

    private WorldGenLevel world = null;
    private ResourceLocation templateName = null;
    private BlockPos blockPos = null;
    private Mirror mirror = null;
    private Rotation rotation = null;
    private boolean ignoreEntities = false;
    private float integrity = 1;
    private BlockPos offset = null;
    private Vec3i size = Vec3i.ZERO;

    protected boolean replaceBedrock = false;
    protected boolean replaceBarrier = false;
    protected boolean onlyReplaceTaggedBlocks = false;
    protected boolean preventReplacementOfTaggedBlocks = false;
    protected TagKey<Block> taggedBlocks = null;
    protected boolean actOnBlockStructurePlacing = false;
    protected boolean actOnBlockReplacedByStructure = false;
    /// aka check the block IN the structure
    protected ActionOnBlockFind onBlockPlacingInStructure;
    /// aka check the block that is getting replced BY the structure
    protected ActionOnBlockFind onBlockReplacedByStructure;
    protected TagKey<Block> blockPlacedCheck;
    protected TagKey<Block> blockReplacedCheck;

    public static final Logger LOGGER = LoggerFactory.getLogger("structureplacerapi");

    /**
     * With this you can create placer object which will spawn
     * a new structure from an nbt file located in /data/modid/structures
     *
     * @param world The StructureWorldAccess in which to place the structure
     * @param templateName The identifier of the structure to place, like <code>new Identifier(MOD_ID, structure_name)</code>
     * @param blockPos The position of the structure
     * @param mirror Use this to mirror the structure using <code>BlockMirror.#</code>
     * @param rotation Use this to rotate the structure using <code>BlockRotation.#</code>
     * @param ignoreEntities Set to true to block the spawning of entities saved in the structure file
     * @param integrity Set this to a value between 0f and 1f to remove some blocks from the placed structure. (All blocks = 1f)
     * @param offset Use this to offset the placing of the structure.
     * */
    public StructurePlacerAPI(WorldGenLevel world, ResourceLocation templateName, BlockPos blockPos, Mirror mirror, Rotation rotation, boolean ignoreEntities, float integrity, BlockPos offset){
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
     * @param world The StructureWorldAccess in which to place the structure
     * @param templateName The identifier of the structure to place, like <code>new Identifier(MOD_ID, structure_name)</code>
     * @param blockPos The position of the structure
     * */
    public StructurePlacerAPI(WorldGenLevel world, ResourceLocation templateName, BlockPos blockPos) {
        this.world = world;
        this.templateName = templateName;
        this.blockPos = blockPos;
        this.mirror = Mirror.NONE;
        this.rotation = Rotation.NONE;
        this.ignoreEntities = true;
        this.integrity = 1.0f;
        this.offset = new BlockPos(0, 0, 0);
    }

    /**
     * With this you can create placer object which will spawn
     * a new structure from an nbt file located in /data/modid/structures
     *
     * @param world The StructureWorldAccess in which to place the structure
     * @param templateName The identifier of the structure to place, like <code>new Identifier(MOD_ID, structure_name)</code>
     * @param blockPos The position of the structure
     * @param offset Use this to offset the placing of the structure.
     * */
    public StructurePlacerAPI(WorldGenLevel world, ResourceLocation templateName, BlockPos blockPos, BlockPos offset){
        this.world = world;
        this.templateName = templateName;
        this.blockPos = blockPos;
        this.mirror = Mirror.NONE;
        this.rotation = Rotation.NONE;
        this.ignoreEntities = true;
        this.integrity = 1.0f;
        this.offset = offset;
    }

    /**
     * With this you can create placer object which will spawn
     * a new structure from an nbt file located in /data/modid/structures
     *
     * @param world The StructureWorldAccess in which to place the structure
     * @param templateName The identifier of the structure to place, like <code>new Identifier(MOD_ID, structure_name)</code>
     * @param blockPos The position of the structure
     * @param mirror Use this to mirror the structure using <code>BlockMirror.#</code>
     */
    public StructurePlacerAPI(WorldGenLevel world, ResourceLocation templateName, BlockPos blockPos, Mirror mirror){
        this.world = world;
        this.templateName = templateName;
        this.blockPos = blockPos;
        this.mirror = mirror;
        this.rotation = Rotation.NONE;
        this.ignoreEntities = true;
        this.integrity = 1.0f;
        this.offset = new BlockPos(0, 0, 0);
    }

    /**
     * With this you can create placer object which will spawn
     * a new structure from an nbt file located in /data/modid/structures
     *
     * @param world The StructureWorldAccess in which to place the structure
     * @param templateName The identifier of the structure to place, like <code>new Identifier(MOD_ID, structure_name)</code>
     * @param blockPos The position of the structure
     * @param rotation Use this to rotate the structure using <code>BlockRotation.#</code>
     * */
    public StructurePlacerAPI(WorldGenLevel world, ResourceLocation templateName, BlockPos blockPos, Rotation rotation){
        this.world = world;
        this.templateName = templateName;
        this.blockPos = blockPos;
        this.mirror = Mirror.NONE;
        this.rotation = rotation;
        this.ignoreEntities = true;
        this.integrity = 1.0f;
        this.offset = new BlockPos(0, 0, 0);
    }

    /**
     * With this you can create placer object which will spawn
     * a new structure from an nbt file located in /data/modid/structures
     *
     * @param world The StructureWorldAccess in which to place the structure
     * @param templateName The identifier of the structure to place, like <code>new Identifier(MOD_ID, structure_name)</code>
     * @param blockPos The position of the structure
     * @param mirror Use this to mirror the structure using <code>BlockMirror.#</code>
     * @param rotation Use this to rotate the structure using <code>BlockRotation.#</code>
     * */
    public StructurePlacerAPI(WorldGenLevel world, ResourceLocation templateName, BlockPos blockPos, Mirror mirror, Rotation rotation){
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
     * @param world The StructureWorldAccess in which to place the structure
     * @param templateName The identifier of the structure to place, like <code>new Identifier(MOD_ID, structure_name)</code>
     * @param blockPos The position of the structure
     * @param integrity Set this to a value between 0f and 1f to remove some blocks from the placed structure. (All blocks = 1f)
     * */
    public StructurePlacerAPI(WorldGenLevel world, ResourceLocation templateName, BlockPos blockPos, float integrity){
        this.world = world;
        this.templateName = templateName;
        this.blockPos = blockPos;
        this.mirror = Mirror.NONE;
        this.rotation = Rotation.NONE;
        this.ignoreEntities = true;
        this.integrity = integrity;
        this.offset = new BlockPos(0, 0, 0);
    }

    public StructurePlacerAPI(IEventBus modBus) {
        NeoForge.EVENT_BUS.register(StructurePlacerAPI.class);
    }

    /** Returns an instance of the loaded structure.
     * From this, you can get its size and other useful stuff
     */
    public Optional<StructureTemplate> getTemplate(){
        StructureTemplateManager structureTemplateManager = Objects.requireNonNull(world.getServer()).getStructureManager();
        return structureTemplateManager.get(this.templateName);
    }

    /** Returns the copy of a structure that you are going to be loading later,
     * in order to get its size and other info
     */
    public static Optional<StructureTemplate> getTemplatePreview(ServerLevel world, ResourceLocation templateName){
        StructureTemplateManager structureTemplateManager = world.getServer().getStructureManager();
        return structureTemplateManager.get(templateName);
    }

    /** Returns the copy of a structure that you are going to be loading later,
     * in order to get its size and other info
     */
    public static Optional<StructureTemplate> getTemplatePreview(WorldGenLevel world, ResourceLocation templateName){
        StructureTemplateManager structureTemplateManager = Objects.requireNonNull(world.getServer()).getStructureManager();
        return structureTemplateManager.get(templateName);
    }

    /**Use this method to load the structure into the world and
     * place it. You can check to see if the placing was successful.
     * <p>
     * This method DOES NOT support regenerating the old terrain,
     * use {@link #loadAndRestoreStructure(int)} or {@link #loadAndRestoreStructureAnimated(int, int, boolean)} instead
     * if you want to do it. This method however consumes less resources.
     */
    public boolean loadStructure() {
        return loadAndRestoreStructureAnimated(-1, -1, false);
    }

    /**Use this method to load the structure into the world and
     * spawn it. You can check to see if the placing was successful.
     * <p>
     * It will also restore the blocks it replaced after <i>restore_ticks</i>
     * Calling this function from the client only, will not regenerate the old terrain.
     * And will probably cause other issues.
     *<p>
     * <b>Notice:</b> Using this could lead to performance issues, especially with very large structures!
     *
     * @param restore_ticks Number of ticks (1 second = 20 ticks) after which the world would be restored.
     *                      Setting this to -1 will prevent the structure from being restored so use {@link #loadStructure()} instead
     */
    public boolean loadAndRestoreStructure(int restore_ticks) {
        return loadAndRestoreStructureAnimated(restore_ticks, -1, false);
    }

    /**Use this method to load the structure into the world and
     * spawn it. You can check to see if the placing was successful.
     *<p>
     * It will also restore the blocks it replaced after <i>restore_ticks</i> with an animation
     *<p>
     * Calling this function from the client only will not regenerate the old terrain.
     * And will probably cause other issues.
     *<p>
     * <b>Notice:</b> Using this could lead to performance issues, especially with very large structures!
     *
     * @param restore_ticks Number of ticks (1 second = 20 ticks) after which the blocks will begin to be restored
     *                      Setting this to -1 will prevent the structure from being restored so use {@link #loadStructure()} instead
     * @param blocks_per_tick How many blocks to restore per tick, the more, the faster the animation will go.
     *                        Setting thi to -1 will cancel the animation so use {@link #loadAndRestoreStructure(int)} instead
     * @param random Weather or not the blocks will be removed at random. If false, they will be removed from one corner to the other in sequence
     */
    public boolean loadAndRestoreStructureAnimated(int restore_ticks, int blocks_per_tick, boolean random) {
        if (this.templateName != null && world.getServer() != null) {
            StructureTemplateManager structureTemplateManager = world.getServer().getStructureManager();
            Optional<StructureTemplate> optional;
            try {
                optional = structureTemplateManager.get(this.templateName);
            } catch (ResourceLocationException var6) {
                return false;
            }
            optional.ifPresent(structureTemplate -> this.size = structureTemplate.getSize());

            if(!this.world.isClientSide() && restore_ticks != -1){
                if(blocks_per_tick != -1){
                    scheduleReplacementAnimated(restore_ticks, blocks_per_tick, random, saveFromWorld(this.world, this.blockPos.offset(this.offset), size));
                }else{
                    scheduleReplacement(restore_ticks, saveFromWorld(this.world, this.blockPos.offset(this.offset), size));
                }
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
            StructurePlaceSettings structurePlacementData = (new StructurePlaceSettings()).setMirror(this.mirror).setRotation(this.rotation).setIgnoreEntities(this.ignoreEntities);
            if (this.integrity < 1.0F) {
                structurePlacementData.clearProcessors().addProcessor(new BlockRotProcessor(Mth.clamp(this.integrity, 0.0F, 1.0F))).setRandom(createRandom(this.world.getSeed()));
            }
            BlockPos pos = blockPos.offset(this.offset);
            ((ICustomStructureTemplate) template).structurePlacerAPI$setCustom(true);
            ((ICustomStructureTemplate) template).structurePlacerAPI$setReplaceBedrock(replaceBedrock);
            ((ICustomStructureTemplate) template).structurePlacerAPI$setReplaceBarrier(replaceBarrier);
            ((ICustomStructureTemplate) template).structurePlacerAPI$setOnlyReplaceTagBlocks(onlyReplaceTaggedBlocks, taggedBlocks);
            ((ICustomStructureTemplate) template).structurePlacerAPI$setPreventReplacementOfTagBlocks(preventReplacementOfTaggedBlocks, taggedBlocks);

            // TODO test these out
            ((ICustomStructureTemplate) template).structurePlacerAPI$setActOnBlockStructurePlacing(actOnBlockStructurePlacing);
            ((ICustomStructureTemplate) template).structurePlacerAPI$setActOnBlockReplacedByStructure(actOnBlockReplacedByStructure);
            ((ICustomStructureTemplate) template).structurePlacerAPI$setOnBlockPlacingInStructure(onBlockPlacingInStructure);
            ((ICustomStructureTemplate) template).structurePlacerAPI$setOnBlockReplacedByStructure(onBlockReplacedByStructure);
            ((ICustomStructureTemplate) template).structurePlacerAPI$setBlockPlacedCheck(blockPlacedCheck);
            ((ICustomStructureTemplate) template).structurePlacerAPI$setBlockReplacedCheck(blockReplacedCheck);

            template.placeInWorld(world, pos, pos, structurePlacementData, createRandom(this.world.getSeed()), 2);
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
        if (this.templateName != null && world.getServer() != null) {
            StructureTemplateManager structureTemplateManager = world.getServer().getStructureManager();
            structureTemplateManager.remove(this.templateName);
        }
    }

    /**This method creates a random seed for the integrity run-down effect.
     * No need to use it on your own, included during placement*/
    public static RandomSource createRandom(long seed) {
        return seed == 0L ? RandomSource.create(Util.getMillis()) : RandomSource.create(seed);
    }

    /**Schedules the replacement of the older terrain/world and
     * after <i>ticks</i> it executes the replacement.
     *<p>
     * TO CALL SERVER SIDE ONLY*/
    private void scheduleReplacement(int ticks, List<StructureTemplate.StructureBlockInfo> blockInfoList){
        runLater(ticks, (server) -> {
            for(StructureTemplate.StructureBlockInfo info : blockInfoList){
                world.setBlock(info.pos(), info.state(), Block.UPDATE_ALL);

                if (info.nbt() != null) {
                    //The blockentities check needs to be done on the main thread
                    server.execute( () -> {
                        BlockEntity blockEntity = world.getBlockEntity(info.pos());
                        if (blockEntity != null) {
                            if (blockEntity instanceof RandomizableContainerBlockEntity) {
                                info.nbt().putLong("LootTableSeed", Objects.requireNonNull(blockEntity.getLevel()).getRandom().nextLong());
                            }

                            blockEntity.loadWithComponents(info.nbt(), world.registryAccess());
                        }
                    });
                }
            }
        });

    }

    /**Schedules the replacement of the older terrain/world and
     * after <i>ticks</i> it executes the replacement.
     * It also does this with a basic animation
     *<p>
     * TO CALL SERVER SIDE ONLY
     *
     * @param startDelayTicks Number of ticks after which the blocks will begin to be restored
     * @param blocks_per_tick How many blocks to restore per tick, the more, the faster the animation will go
     * @param rand Weather or not the blocks will be removed at random. If false, they will be removed from one corner to the other in sequence
     * */
    private void scheduleReplacementAnimated(int startDelayTicks, int blocks_per_tick, boolean rand, List<StructureTemplate.StructureBlockInfo> blockInfoList){
        List<StructureTemplate.StructureBlockInfo> infoList = new ArrayList<>();

        runEveryTick((server, ticks) -> {

            //Waits until we have reached the start delay. Aka when the current ticks are fewer than the delay return but continue to loop
            if(ticks < startDelayTicks){
                return true;
            }
            //If the length of the list is the same as the blocks placed it means it already placed the last one
            if(ticks == startDelayTicks){
                infoList.addAll(blockInfoList);
            }
            if(infoList.isEmpty()){
                return false;
            }

            for(int j = 0; j<blocks_per_tick; j++){
                if(infoList.isEmpty()){
                    return false;
                }
                StructureTemplate.StructureBlockInfo info;
                if(rand){
                    int i;
                    if(infoList.size()-1 == 0){
                        i = 0;
                    } else{
                        i = this.world.getRandom().nextInt(infoList.size()-1);
                    }
                    info = infoList.get(i);
                    infoList.remove(i);
                }else{
                    info = infoList.get(ticks - startDelayTicks);
                }

                world.setBlock(info.pos(), info.state(), Block.UPDATE_ALL);

                if (info.nbt() != null) {
                    //The block entities check needs to be done on the main thread
                    server.execute( () -> {
                        BlockEntity blockEntity = world.getBlockEntity(info.pos());
                        if (blockEntity != null) {
                            if (blockEntity instanceof RandomizableContainerBlockEntity) {
                                info.nbt().putLong("LootTableSeed", Objects.requireNonNull(blockEntity.getLevel()).getRandom().nextLong());
                            }
                            blockEntity.loadWithComponents(info.nbt(), world.registryAccess());
                        }
                    });
                }
            }
            return true;
        });
        infoList.clear();
    }

    /**Saves the block infos to <i>blockInfoLists</i>
     *<p>
     * Will also debug log the time it took to save the structure.
     */
    private List<StructureTemplate.StructureBlockInfo> saveFromWorld(WorldGenLevel world, BlockPos start, Vec3i dimensions) {
        List<StructureTemplate.StructureBlockInfo> blockInfoList = new ArrayList<>();
        Instant start_time = Instant.now();
        LOGGER.debug("Saving terrain to later restore it...");
        BlockPos blockPos = start.offset(dimensions).offset(-1, -1, -1);
        BlockPos min_pos = new BlockPos(Math.min(start.getX(), blockPos.getX()), Math.min(start.getY(), blockPos.getY()), Math.min(start.getZ(), blockPos.getZ()));
        BlockPos max_pos = new BlockPos(Math.max(start.getX(), blockPos.getX()), Math.max(start.getY(), blockPos.getY()), Math.max(start.getZ(), blockPos.getZ()));

        //Iterates through all the block positions and adds them to list
        BlockPos.betweenClosed(min_pos, max_pos).iterator().forEachRemaining(pos -> {
                    //Copying the pos, so it doesn't mutate and make a mess
                    BlockPos save_pos;
                    save_pos = new BlockPos(pos.getX(), pos.getY(), pos.getZ());

                    BlockEntity blockEntity = world.getBlockEntity(save_pos);
                    StructureTemplate.StructureBlockInfo structureBlockInfo;

                    //This means the block has an inventory, that it has already dropped, so don't copy its nbt

                    if (blockEntity != null) {
                        boolean has_inventory = Container.class.isAssignableFrom(blockEntity.getClass());
                        if(has_inventory){
                            structureBlockInfo = new StructureTemplate.StructureBlockInfo(save_pos, world.getBlockState(save_pos), null);
                        }else{
                            structureBlockInfo = new StructureTemplate.StructureBlockInfo(save_pos, world.getBlockState(save_pos), blockEntity.saveWithId(world.registryAccess()));
                        }
                    } else {
                        structureBlockInfo = new StructureTemplate.StructureBlockInfo(save_pos, world.getBlockState(save_pos), null);
                    }
                    blockInfoList.add(structureBlockInfo);
                }
        );

        Instant end_time = Instant.now();
        Duration timeElapsed = Duration.between(start_time, end_time);

        LOGGER.debug("Terrain saved! It took: " + timeElapsed.toMillis() + "ms");
        return blockInfoList;
    }


    public boolean isReplaceBedrock() {
        return replaceBedrock;
    }

    /** Sets weather or not this structure should replace the bedrock */
    public void setReplaceBedrock(boolean replaceBedrock) {
        this.replaceBedrock = replaceBedrock;
    }

    public boolean isReplaceBarrier() {
        return replaceBarrier;
    }

    /** Weather or not this structure should replace the barrier block */
    public void setReplaceBarrier(boolean replaceBarrier) {
        this.replaceBarrier = replaceBarrier;
    }

    public boolean isOnlyReplaceTaggedBlocks() {
        return onlyReplaceTaggedBlocks;
    }

    /** Sets weather or not this structure should only replace blocks with the provided tag, for example only replace air blocks */
    public void setOnlyReplaceTaggedBlocks(boolean onlyReplaceTaggedBlocks, TagKey<Block> tag) {
        this.onlyReplaceTaggedBlocks = onlyReplaceTaggedBlocks;
        this.setTaggedBlocks(tag);
    }

    public boolean isPreventReplacementOfTaggedBlocks() {
        return preventReplacementOfTaggedBlocks;
    }

    /** Allows to specify blocks which won't be replaced if they have the provided tag*/
    public void setPreventReplacementOfTaggedBlocks(boolean preventReplacementOfTaggedBlocks, TagKey<Block> tag) {
        this.preventReplacementOfTaggedBlocks = preventReplacementOfTaggedBlocks;
        this.setTaggedBlocks(tag);
    }

    public TagKey<Block> getTaggedBlocks() {
        return taggedBlocks;
    }
    public void setTaggedBlocks(TagKey<Block> taggedBlocks) {
        this.taggedBlocks = taggedBlocks;
    }

    /** Performs an action while placing a block with a tag, from the saved structure file
     * <p>
     * For example, a structure that has a fence inside of it will have an action that spawns a rabbit on top of it*/
    public void actionOnBlocksPlacedByStructure(ActionOnBlockFind action, TagKey<Block> targets){
        blockPlacedCheck = targets;
        actOnBlockStructurePlacing = true;
        onBlockPlacingInStructure = action;
    }

    /** Performs an action while a block with a tag in the world gets replaced by one from the structure file
     * <p>
     * For example, in the world there is snow block that will be replaced by the structure,
     * will have an action that spawn a snowgolem on top of it*/
    public void actionOnBlocksReplacedByStructure(ActionOnBlockFind action, TagKey<Block> targets){
        blockReplacedCheck = targets;
        actOnBlockReplacedByStructure = true;
        onBlockReplacedByStructure = action;
    }

    /**Used to schedule the replacements*/
    public static final class ServerTaskScheduler {

        private static final List<ScheduledTask> TASKS = new ArrayList<>();

        public static void schedule(ScheduledTask task) {
            TASKS.add(task);
        }

        public static void tick(MinecraftServer server) {
            TASKS.removeIf(task -> !task.tick(server));
        }
    }

    public interface ScheduledTask {
        boolean tick(MinecraftServer server);
    }

    // Subscribe
    @SubscribeEvent
    public static void onServerTick(ServerTickEvent.Post event) {
        ServerTaskScheduler.tick(event.getServer());
    }

    //Called from any function at any time
    public static void runLater(int delayTicks, ServerRunnable action) {
        ServerTaskScheduler.schedule(new ScheduledTask() {
            int ticks = delayTicks;

            @Override
            public boolean tick(MinecraftServer server) {
                if (--ticks <= 0) {
                    action.run(server);
                    return false; // remove task
                }
                return true;
            }
        });
    }

    //Called from any function at any time

    /**Runs an action every tick and provides the tick count with an optional start delay.
     * If the action lambda returns a falls the task is stopepd
     */
    public static void runEveryTick(ServerTickRunnable action) {
        ServerTaskScheduler.schedule(new ScheduledTask() {
            int ticks = 0;
            List<StructureTemplate.StructureBlockInfo> infoList = new ArrayList<>();

            @Override
            public boolean tick(MinecraftServer server) {
                boolean keepgoing = action.run(server, ticks);
                ticks++;
                //if the task is taking more than an hour just kill it
                if (ticks >= 20*3600) {
                    LOGGER.warn("A task has taken more than an hour to complete, it has now been terminated");
                    return false; // remove task
                }
                return keepgoing;
            }
        });
    }
}
