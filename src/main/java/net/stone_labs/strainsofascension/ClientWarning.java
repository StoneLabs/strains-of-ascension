package net.stone_labs.strainsofascension;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.java.games.input.Component;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.resource.language.TranslationStorage;
import net.minecraft.client.util.InputUtil;
import net.minecraft.text.KeybindText;
import net.minecraft.text.LiteralText;
import net.minecraft.text.TranslatableText;
import org.lwjgl.glfw.GLFW;

public class ClientWarning implements ClientModInitializer
{
    public static boolean hide = false;
    @Override
    public void onInitializeClient()
    {
        // Yes, the translation key is hacky as fuck but whatever, this is only a client warning.
        // If you're seeing this message you are not using the mod correctly so it should be fine.
        // I can't be bothered to add a lang file for a screen you only see if you install the mod incorrectly.

        HudRenderCallback.EVENT.register((stack, delta) -> {
            if (hide)
                return;

            MinecraftClient.getInstance().textRenderer.draw(stack, new LiteralText("§4Warning: Strains of Ascension is a dedicated server only mod."), 2, 2, 1);
            MinecraftClient.getInstance().textRenderer.drawWithShadow(stack, new LiteralText("§fIt will not work when installed on client minecraft."), 2, 12, 1);
            MinecraftClient.getInstance().textRenderer.drawWithShadow(stack, new LiteralText("§fThis might change in the future. For updates and questions"), 2, 27, 1);
            MinecraftClient.getInstance().textRenderer.drawWithShadow(stack, new LiteralText("§frefer to https://github.com/StoneLabs/strains-of-ascension."), 2, 37, 1);
            MinecraftClient.getInstance().textRenderer.drawWithShadow(stack, new LiteralText("§fPress ").append(new KeybindText("Hide client warning")).append(new LiteralText(" §fto hide this warning.")), 2, 50, 1);

        });
        var keyBinding = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "Hide client warning",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_K,
                "Strains of Ascension"
        ));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (keyBinding.wasPressed()) {
                hide = !hide;
            }
        });
    }
}
