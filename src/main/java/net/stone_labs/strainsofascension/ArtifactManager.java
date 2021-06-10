package net.stone_labs.strainsofascension;

import com.google.gson.Gson;
import net.fabricmc.fabric.api.loot.v1.FabricLootPoolBuilder;
import net.fabricmc.fabric.api.loot.v1.FabricLootSupplierBuilder;
import net.fabricmc.fabric.api.loot.v1.event.LootTableLoadingCallback;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootGsons;
import net.minecraft.loot.LootManager;
import net.minecraft.loot.condition.RandomChanceLootCondition;
import net.minecraft.loot.entry.LootPoolEntry;
import net.minecraft.loot.provider.number.*;
import net.minecraft.resource.ResourceManager;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.stone_labs.strainsofascension.utils.ResourceLoader;

import java.util.HashMap;
import java.util.Map;

public class ArtifactManager
{
    private static final Gson LOOT_GSON = LootGsons.getTableGsonBuilder().create();

    private static final Identifier LOOTTABLE_ID_SPAWNER;
    private static final String LOOTABLE_COMPASS;
    private static final String LOOTABLE_CLOCK;

    public static void Init()
    {
        LootTableLoadingCallback.EVENT.register(ArtifactManager::ModifyLootTable);
    }

    public static void ModifyLootTable(ResourceManager resourceManager, LootManager lootManager, Identifier id, FabricLootSupplierBuilder supplier, LootTableLoadingCallback.LootTableSetter setter)
    {
        if (id.equals(LOOTTABLE_ID_SPAWNER))
        {
            FabricLootPoolBuilder poolBuilder = FabricLootPoolBuilder.builder()
                    .rolls(ConstantLootNumberProvider.create(1))
                    .withCondition(RandomChanceLootCondition.builder(1f).build())
                    .withEntry(LOOT_GSON.fromJson(LOOTABLE_COMPASS, LootPoolEntry.class))
                    .withEntry(LOOT_GSON.fromJson(LOOTABLE_CLOCK, LootPoolEntry.class));

            supplier.withPool(poolBuilder.build());
        }
    }

    public static ArtifactState GetPlayerArtifactState(PlayerInventory inventory)
    {
        ArtifactState artifactState = new ArtifactState();

        for (ItemStack stack : inventory.armor)
            artifactState.consider(stack);

        for (ItemStack stack : inventory.offHand)
            artifactState.consider(stack);

        for (ItemStack stack : inventory.main)
            artifactState.consider(stack);

        artifactState.consider(inventory.getMainHandStack());



        return artifactState;
    }

    static
    {
        LOOTTABLE_ID_SPAWNER = new Identifier("minecraft", "blocks/spawner");

        LOOTABLE_COMPASS = ResourceLoader.LoadResource("data/compass.json");
        LOOTABLE_CLOCK = ResourceLoader.LoadResource("data/clock.json");
    }
}
