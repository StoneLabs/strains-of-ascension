package net.stone_labs.strainsofascension.utils;

import net.minecraft.item.ItemStack;
import net.minecraft.loot.context.LootContext;
import net.minecraft.loot.function.LootFunction;
import net.minecraft.loot.function.LootFunctionType;
import net.minecraft.loot.function.LootFunctionTypes;

import java.util.Random;

public class StackPreventer implements LootFunction
{
    Random random = new Random();

    @Override
    public LootFunctionType getType()
    {
        return LootFunctionTypes.SET_NBT;
    }

    @Override
    public ItemStack apply(ItemStack stack, LootContext lootContext)
    {
        stack.getOrCreateNbt().putInt("stackPreventer", random.nextInt());
        return stack;
    }
}
