package com.vuzz.forgestory.api.plotter.story;

import com.vuzz.forgestory.annotations.MethodJSON;
import com.vuzz.forgestory.api.plotter.story.instances.SceneInstance;
import com.vuzz.forgestory.api.plotter.util.FileManager;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.contents.LiteralContents;

import java.lang.reflect.Method;
import java.util.HashMap;


public class PlotterJSON {

    public static Method getActionById(String id) {
        Method[] methods = PlotterJSON.class.getMethods();
        for(Method m : methods) {
            boolean hasAnn = m.isAnnotationPresent(MethodJSON.class);
            if(!hasAnn) continue;
            String actionId = m.getAnnotation(MethodJSON.class).id();
            if(id.equals(actionId)) return m;
        }
        return null;
    }

    @MethodJSON(id = "talk")
    @SuppressWarnings("all")
    public static void talk(SceneInstance scene, HashMap<String,Object> data) {
        String author = (String) FileManager.getSafely(data,"author","NPC");
        String text = (String) FileManager.getSafely(data,"text","Hello World!");
        String color = (String) FileManager.getSafely(data, "color", "f");
        String message = "\u00A7"+color+"["+author+"]\u00A7r "+text;
        LiteralContents playerMessage = new LiteralContents(message);
        scene.getPlayer().sendSystemMessage(MutableComponent.create(playerMessage));
    }

    @MethodJSON(id = "placeholder")
    @SuppressWarnings("all")
    public static void doNothing(SceneInstance scene, HashMap<String,Object> data) {

    }

}
