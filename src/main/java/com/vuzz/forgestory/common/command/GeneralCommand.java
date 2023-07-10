package com.vuzz.forgestory.common.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import com.vuzz.forgestory.ForgeStory;
import com.vuzz.forgestory.api.plotter.story.Root;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.contents.TranslatableContents;

public class GeneralCommand {
    @SuppressWarnings("all")
    private static final SimpleCommandExceptionType UNKNOWN = new SimpleCommandExceptionType(MutableComponent.create(new TranslatableContents("command." + ForgeStory.MOD_ID + ".unknown")));
    private static final SimpleCommandExceptionType STORY_NOT_FOUND = new SimpleCommandExceptionType(MutableComponent.create(new TranslatableContents("command." + ForgeStory.MOD_ID + ".st_not_found")));

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("fs").requires(s -> s.hasPermission(1)
        )
        .then(Commands.literal("set_story")
            .then(Commands.argument("story", StringArgumentType.string())
            .executes(GeneralCommand::setStory))
        ).then(Commands.literal("refresh")
            .executes(GeneralCommand::refresh)
        ));
    }

    private static int setStory(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        CommandSourceStack source = context.getSource();
        String story = StringArgumentType.getString(context, "story");
        if(Root.storiesList.containsKey(story)) {
            Root.setActiveStory(story);
            Root.reloadStories();
            source.sendSuccess(MutableComponent.create(new TranslatableContents("command."+ForgeStory.MOD_ID+".success")), false);
            return 0;
        } else {
            throw STORY_NOT_FOUND.create();
        }
    }

    private static int refresh(CommandContext<CommandSourceStack> context) {
        CommandSourceStack source = context.getSource();
        Root.reloadStories();
        source.sendSuccess(MutableComponent.create(new TranslatableContents("command."+ForgeStory.MOD_ID+".st_refresh")), false);
        return 0;
    }
}
