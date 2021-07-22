package net.stone_labs.strainsofascension;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.java.games.input.Component;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.text.KeybindText;
import net.minecraft.text.LiteralText;
import net.minecraft.text.TranslatableText;
import org.lwjgl.glfw.GLFW;

public class ClientWarning implements ClientModInitializer
{
    public boolean hide = false;
    @Override
    public void onInitializeClient()
    {
        HudRenderCallback.EVENT.register((stack, delta) -> {
            MinecraftClient.getInstance().textRenderer.draw(stack, new LiteralText("§4Warning: Strains of Ascension is a dedicated server only mod."), 2, 2, 1);
            MinecraftClient.getInstance().textRenderer.drawWithShadow(stack, new LiteralText("§fIt will not work when installed on client minecraft."), 2, 12, 1);
            MinecraftClient.getInstance().textRenderer.drawWithShadow(stack, new LiteralText("§fThis might change in the future. For updates and questions"), 2, 27, 1);
            MinecraftClient.getInstance().textRenderer.drawWithShadow(stack, new LiteralText("§frefer to https://github.com/StoneLabs/strains-of-ascension."), 2, 37, 1);
            MinecraftClient.getInstance().textRenderer.drawWithShadow(stack, new LiteralText("§fPress ").append(new KeybindText("key.stainsofascension.hide_client_warning")).append(new LiteralText(" §fto hide this warning.")), 2, 50, 1);

        });
        var keyBinding = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.stainsofascension.hide_client_warning",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_F1,
                "key.stainsofascension.misc"
        ));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (keyBinding.wasPressed()) {
                hide = !hide;
            }
        });
    }
}
