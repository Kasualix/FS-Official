package com.vuzz.forgestory.api.plotter.js;

import com.vuzz.forgestory.annotations.Documentate;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.contents.LiteralContents;
import net.minecraft.world.entity.player.Player;


public class PlayerJS implements JSResource {

    private Player player;

    public PlayerJS(Player player) {
        this.player = player;
    }

    @Documentate(desc = "Returns player name.")
    public String getPlayerName() { return player.getDisplayName().getString(); }

    @Documentate(desc = "Sends message to the player.")
    public PlayerJS sendMessage(String text) { 
        player.sendSystemMessage(MutableComponent.create(new LiteralContents(text)));
        return this;
    }

    @Documentate(desc = "Sets rotation of player head on X&Y / Pitch&Yaw.")
    public void setHeadRotation(float pitch, float yaw) {
        player.setXRot(pitch);
        player.yHeadRot = yaw;
    }

    @Documentate(desc = "Gets Player Position.")
    public double[] getPosition() { return new double[] { getX(),getY(),getZ() };}

    @Documentate(desc = "Gets Player X Position.")
    public double getX() { return player.getX(); }

    @Documentate(desc = "Gets Player Y Position.")
    public double getY() { return player.getY(); }

    @Documentate(desc = "Gets Player Z Position.")
    public double getZ() { return player.getZ(); }

    @Documentate(desc = "Sets Player Position.")
    public void setPosition(double[] pos)  { player.teleportToWithTicket(pos[0],pos[1],pos[2]); };

    @Documentate(desc = "Sets Player Position.")
    public void setPosition(double x, double y, double z)  { setPosition( new double[] {x,y,z} ); };

    @Documentate(desc = "Sets Player X Position.")
    public void setX(double x) { setPosition(x,getY(),getZ()); };

    @Documentate(desc = "Sets Player Y Position.")
    public void setY(double y) { setPosition(getX(),y,getZ()); };

    @Documentate(desc = "Sets Player Z Position.")
    public void setZ(double z) { setPosition(getX(),getY(),z); };

    @Override public Object getNative() { return player; }
    @Override public String getResourceId() { return "player"; }
    @Override public boolean isClient() { return player.level.isClientSide; }
    
}
