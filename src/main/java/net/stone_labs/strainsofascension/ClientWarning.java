package net.stone_labs.strainsofascension;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.LiteralText;

public class ClientWarning implements ClientModInitializer
{
    @Override
    public void onInitializeClient()
    {
        HudRenderCallback.EVENT.register((stack, delta) -> {
            MinecraftClient.getInstance().textRenderer.draw(stack, new LiteralText("§4Warning: Strains of Ascension is a dedicated server only mod."), 2, 2, 1);
            MinecraftClient.getInstance().textRenderer.drawWithShadow(stack, new LiteralText("§fIt will not work when installed on client minecraft."), 2, 12, 1);
            MinecraftClient.getInstance().textRenderer.drawWithShadow(stack, new LiteralText("§fThis might change in the future. For updates and questions"), 2, 27, 1);
            MinecraftClient.getInstance().textRenderer.drawWithShadow(stack, new LiteralText("§frefer to https://github.com/StoneLabs/strains-of-ascension."), 2, 37, 1);

        });
    }
}
