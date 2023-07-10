package com.vuzz.forgestory.common.entity;

import com.vuzz.forgestory.api.plotter.js.event.EventManager;
import com.vuzz.forgestory.api.plotter.js.event.TickEvent;
import com.vuzz.forgestory.api.plotter.js.event.npc.InteractionEvent;
import com.vuzz.forgestory.common.items.ItemsFS;
import com.vuzz.forgestory.common.networking.NBTBank;
import net.minecraft.commands.arguments.EntityAnchorArgument;
import net.minecraft.core.NonNullList;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.IAnimationTickable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.geckolib3.util.GeckoLibUtil;

public class NPCEntity extends Mob implements IAnimatable,IAnimationTickable {

    private AnimationFactory anFactory = GeckoLibUtil.createFactory(this);

    private final NonNullList<ItemStack> armorInv = NonNullList.withSize(4, ItemStack.EMPTY);
    private final NonNullList<ItemStack> inventory = NonNullList.withSize(2, ItemStack.EMPTY);

    public final NBTBank nbtBank = new NBTBank();

    private int ticks = 0;
    public final EventManager evManager = new EventManager();

    public Entity focusedEntity;

    public NPCEntity(EntityType<? extends Mob> type, Level world) { super(type,world); }

    public static AttributeSupplier.Builder genAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH,20.0D)
                .add(Attributes.ARMOR,4D)
                .add(Attributes.ARMOR_TOUGHNESS,4D)
                .add(Attributes.MOVEMENT_SPEED,0.3);
    }

    @Override
    public boolean hurt(DamageSource dmg, float count) {
        return !getPersistentData().getBoolean("immortal");
    }

    @Override
    public void tick() {
        super.tick();
        if(level.isClientSide) return;
        evManager.runEvent("tick", new TickEvent(ticks));
        GroundPathNavigation nav = (GroundPathNavigation) getNavigation();
            nav.setCanFloat(true);
            nav.setCanOpenDoors(true);
        if(ticks % 10 == 0) {
            setTexturePath(getPersistentData().getString("texturePath"));
            setModelPath(getPersistentData().getString("modelPath"));
            setAnimationPath(getPersistentData().getString("animPath"));
            flushOnClient();
        }
        double goToX = getPersistentData().getDouble("gotox");
        double goToY = getPersistentData().getDouble("gotoy");
        double goToZ = getPersistentData().getDouble("gotoz");
        double speed = getPersistentData().getDouble("gotos");
        nav.moveTo(goToX,goToY,goToZ,speed);
        if(focusedEntity != null) {
            lookAt(EntityAnchorArgument.Anchor.EYES,new Vec3(focusedEntity.getX(),focusedEntity.getEyeY(),focusedEntity.getZ()));
        }
        setNoGravity(getPersistentData().getBoolean("no_gravity"));
        ticks++;
    }

    public void allowCollision(boolean coll) { getPersistentData().putBoolean("can_collision",coll); }
    public void setGravity(boolean grav) { getPersistentData().putBoolean("no_gravity",!grav); }

    @Override
    public boolean canCollideWith(Entity entity) {
        boolean can = getPersistentData().getBoolean("can_collision");
        if(can)
            return super.canCollideWith(entity);
        else return false;
    }

    public void setGoTo(double x, double y, double z, double s) {
        getPersistentData().putDouble("gotox",x);
        getPersistentData().putDouble("gotoy",y);
        getPersistentData().putDouble("gotoz",z);
        getPersistentData().putDouble("gotos",s);
    }

    @Override
    public void registerControllers(AnimationData data) {
        data.addAnimationController(new AnimationController<>(this,"controller",15,this::predicateDef));
        data.addAnimationController(new AnimationController<>(this,"c_playonce",15,this::playOnceC));
        data.addAnimationController(new AnimationController<>(this,"c_loop",15,this::loopC));
    }

    @Override
    public @NotNull InteractionResult interactAt(Player player, @NotNull Vec3 vec, @NotNull InteractionHand hand) {
        if(player.level.isClientSide) return super.interactAt(player,vec,hand);
        if(player.getItemInHand(hand).getItem() == ItemsFS.NPC_DELETER.get())
            remove(RemovalReason.KILLED);
        evManager.runEvent("interaction", new InteractionEvent(player, vec,hand,this));
        return super.interactAt(player,vec,hand);
    }

    private <E extends IAnimatable> PlayState predicateDef(AnimationEvent<E> event) {
        event.getController().transitionLengthTicks = 15;
        String idleAnim = getPersistentData().getString("idleAnim");
        String walkAnim = getPersistentData().getString("walkAnim");
        if (event.isMoving()) {
            AnimationBuilder def = new AnimationBuilder()
                .loop(walkAnim.equals("") ? "animation.npc.walk" : walkAnim);
            event.getController().setAnimation(def);
            return PlayState.CONTINUE;
        }
        AnimationBuilder def = new AnimationBuilder()
            .loop(idleAnim.equals("") ? "animation.npc.idle" : idleAnim);
        event.getController().setAnimation(def);
        return PlayState.CONTINUE;
    }

    private <E extends IAnimatable> PlayState playOnceC(AnimationEvent<E> event) {
        event.getController().transitionLengthTicks = 15;
        String anim = getPersistentData().getString("a_playonce");
        if(anim.equals(""))
            return PlayState.STOP;
        AnimationBuilder def = new AnimationBuilder().playOnce(anim);             
        event.getController().setAnimation(def);
        return PlayState.CONTINUE;
    }

    private <E extends IAnimatable> PlayState loopC(AnimationEvent<E> event) {
        event.getController().transitionLengthTicks = 15;
        String anim = getPersistentData().getString("a_loop");
        if(anim.equals(""))
            return PlayState.STOP;
        AnimationBuilder def = new AnimationBuilder().loop(anim);             
        event.getController().setAnimation(def);
        return PlayState.CONTINUE;
    }

    public void setTexturePath(String texture) { 
        getPersistentData().putString("texturePath",texture);
        nbtBank.postOnClient("texturePath", texture, NBTBank.Type.STRING); 
    }
    public void setModelPath(String model) { 
        getPersistentData().putString("modelPath",model);
        nbtBank.postOnClient("modelPath", model, NBTBank.Type.STRING); 
    }
    public void setAnimationPath(String model) { 
        getPersistentData().putString("animPath",model);
        nbtBank.postOnClient("animPath", model, NBTBank.Type.STRING); 
    }
    public void setIdleAnim(String anim) { nbtBank.postOnClient("idleAnim", anim, NBTBank.Type.STRING); }
    public void setWalkAnim(String anim) { nbtBank.postOnClient("walkAnim", anim, NBTBank.Type.STRING); }
    public void flushOnClient() { nbtBank.flush(this); }

    @Override
    public void setItemSlot(@NotNull EquipmentSlot arg0, @NotNull ItemStack arg1) {
        if(arg0 == EquipmentSlot.MAINHAND) inventory.set(0, arg1);
        if(arg0 == EquipmentSlot.OFFHAND) inventory.set(1, arg1);
    }

    @Override
    public @NotNull ItemStack getItemBySlot(@NotNull EquipmentSlot arg0) {
        if(arg0 == EquipmentSlot.MAINHAND) return inventory.get(0);
        if(arg0 == EquipmentSlot.OFFHAND) return inventory.get(1);
        return ItemStack.EMPTY;
    }

    @Override public boolean isLeashed() { return false;}
    @Override public boolean removeWhenFarAway(double p_213397_1_) {return false;}
    @Override public boolean isPersistenceRequired() {return true;}
    @Override public AnimationFactory getFactory() { return this.anFactory; }
    @Override protected @NotNull GroundPathNavigation createNavigation(@NotNull Level world) { return new GroundPathNavigation(this,world); }
    @Override public @NotNull Iterable<ItemStack> getArmorSlots() { return armorInv; }
    @Override public @NotNull HumanoidArm getMainArm() { return HumanoidArm.RIGHT; }

    @Override
    public int tickTimer() {
        return tickCount;
    }

}
