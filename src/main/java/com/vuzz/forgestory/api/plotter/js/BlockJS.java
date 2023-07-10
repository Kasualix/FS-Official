package com.vuzz.forgestory.api.plotter.js;

import com.vuzz.forgestory.annotations.Documentate;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Objects;

public class BlockJS implements JSResource {

    public BlockState state;
    public Block block;
    public WorldJS world;
    public Level nativeWorld;
    public BlockPos blockPos;

    public BlockJS(BlockState state, WorldJS world, BlockPos pos) {
        this.state = state;
        this.block = state.getBlock();
        this.world = world;
        this.nativeWorld = (Level) world.getNative();
        this.blockPos = pos;
    }

    @Documentate(desc = "Gets Block id (minecraft:stone)")
    public String getId() { return Objects.requireNonNull(ForgeRegistries.BLOCKS.getKey(block)).toString(); }

    @Documentate(desc = "Returns block's BlockPos")
    public BlockPos getPosition() {return blockPos;}
    
    @Documentate(desc = "Replaces block with another and returns the new one.")
    public BlockJS replaceWith(String blockId) {
        world.setBlock(blockId,blockPos);
        return world.getBlock(blockPos);
    }

    @Documentate(desc = "Destroys the block")
    public void destroy() { world.setBlock("minecraft:air",blockPos); }


    @Override
    public Object getNative() {return (Object) state;}

    @Override
    public String getResourceId() {return "block";}

    @Override
    public boolean isClient() {return false;}
    
}
