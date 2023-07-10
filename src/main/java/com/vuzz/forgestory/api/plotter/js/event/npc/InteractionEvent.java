package com.vuzz.forgestory.api.plotter.js.event.npc;

import com.vuzz.forgestory.annotations.Documentate;
import com.vuzz.forgestory.api.plotter.js.NpcJS;
import com.vuzz.forgestory.api.plotter.js.PlayerJS;
import com.vuzz.forgestory.api.plotter.js.event.EventJS;
import com.vuzz.forgestory.common.entity.NPCEntity;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;


public class InteractionEvent implements EventJS {

    @Documentate(desc = "Player that clicked")
    public PlayerJS player;
    @Documentate(desc = "Position")
    public double[] pos;
    @Documentate(desc = "Player hand")
    public InteractionHand hand;
    @Documentate(desc = "Npc that was clicked on")
    public NpcJS npc;

    public InteractionEvent(Player player, Vec3 vec, InteractionHand hand, NPCEntity npc) {
        this.player = new PlayerJS(player);
        this.pos = new double[] {vec.x,vec.y,vec.z};
        this.hand = hand;
        this.npc = new NpcJS(npc);
    }

    @Override
    public String getName() {return "interaction";}


}
