package net.stone_labs.strainsofascension.artifacts;

import net.minecraft.item.ItemStack;
import net.minecraft.loot.context.LootContext;
import net.minecraft.loot.function.LootFunction;
import net.minecraft.loot.function.LootFunctionType;
import net.minecraft.loot.function.LootFunctionTypes;

import java.util.Random;

public class ArtifactVersionLootFunction implements LootFunction
{
    public static int ARTIFACT_VERSION = 1;

    @Override
    public LootFunctionType getType()
    {
        return LootFunctionTypes.SET_NBT;
    }

    @Override
    public ItemStack apply(ItemStack stack, LootContext lootContext)
    {
        stack.getOrCreateNbt().putInt("artifactVersion", ARTIFACT_VERSION);
        return stack;
    }
}