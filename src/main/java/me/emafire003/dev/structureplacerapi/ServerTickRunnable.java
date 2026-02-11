package me.emafire003.dev.structureplacerapi;

import net.minecraft.server.MinecraftServer;

public interface ServerTickRunnable {
    boolean run(MinecraftServer server, int ticks);
}
