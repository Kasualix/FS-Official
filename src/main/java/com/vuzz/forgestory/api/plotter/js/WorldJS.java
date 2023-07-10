package com.vuzz.forgestory.api.plotter.js;

import com.vuzz.forgestory.annotations.Documentate;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;


public class WorldJS implements JSResource {

    private Level world;

    public WorldJS(Level world) {
        this.world = world;
    }

    public WorldJS(WorldJS world) {
        this.world = (Level) world.getNative();
    }

    @Documentate(desc = "Places a block in the world")
    public void setBlock(BlockState block, BlockPos pos) { world.setBlock(pos, block, Block.UPDATE_ALL); }

    @Documentate(desc = "Places a block in the world")
    public void setBlock(Block block, BlockPos pos) { setBlock(ApiJS.createBlockState(block),pos); }

    @Documentate(desc = "Places a block in the world")
    public void setBlock(String blockId, BlockPos pos) { setBlock(ApiJS.createBlockState(blockId),pos); }

    @Documentate(desc = "Gets a block in the world")
    public BlockJS getBlock(BlockPos pos) { return new BlockJS(world.getBlockState(pos),this,pos); }

    @Override public Object getNative() { return world; }
    @Override public String getResourceId() { return "world"; }
    @Override public boolean isClient() { return world.isClientSide; }

}