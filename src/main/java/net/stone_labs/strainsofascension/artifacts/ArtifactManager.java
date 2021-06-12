package net.stone_labs.strainsofascension.artifacts;

import com.google.gson.Gson;
import net.fabricmc.fabric.api.loot.v1.FabricLootPoolBuilder;
import net.fabricmc.fabric.api.loot.v1.FabricLootSupplierBuilder;
import net.fabricmc.fabric.api.loot.v1.event.LootTableLoadingCallback;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootGsons;
import net.minecraft.loot.LootManager;
import net.minecraft.loot.LootTables;
import net.minecraft.loot.condition.RandomChanceLootCondition;
import net.minecraft.loot.entry.LootPoolEntry;
import net.minecraft.loot.provider.number.*;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;
import net.stone_labs.strainsofascension.utils.ResourceLoader;
import net.stone_labs.strainsofascension.utils.StackPreventer;

import java.util.HashMap;
import java.util.Map;

public class ArtifactManager
{
    private static final Gson LOOT_GSON = LootGsons.getTableGsonBuilder().create();

    private static final Map<Identifier, Float> ALLOWED_LOOTTABLES_BASIC;
    private static final Map<Identifier, Float> ALLOWED_LOOTTABLES_FULL;
    private static final String LOOTABLE_SHIELD;

    private static final String LOOTABLE_CLOCK;
    private static final String LOOTABLE_CLOCK1;
    private static final String LOOTABLE_CLOCK2;
    private static final String LOOTABLE_CLOCK3;
    private static final String LOOTABLE_CLOCK4;
    private static final String LOOTABLE_CLOCK5;
    private static final String LOOTABLE_SPYGLASS;
    private static final String LOOTABLE_SPYGLASS1;
    private static final String LOOTABLE_SPYGLASS2;
    private static final String LOOTABLE_SPYGLASS3;
    private static final String LOOTABLE_POISON_TALISMAN;
    private static final String LOOTABLE_POISON_TALISMAN1;
    private static final String LOOTABLE_POISON_TALISMAN2;
    private static final String LOOTABLE_POISON_TALISMAN3;
    private static final String LOOTABLE_POISON_TALISMAN4;
    private static final String LOOTABLE_POISON_TALISMAN5;
    private static final String LOOTABLE_WITHER_TALISMAN;
    private static final String LOOTABLE_WITHER_TALISMAN1;
    private static final String LOOTABLE_WITHER_TALISMAN2;
    private static final String LOOTABLE_WITHER_TALISMAN3;
    private static final String LOOTABLE_WITHER_TALISMAN4;
    private static final String LOOTABLE_WITHER_TALISMAN5;
    private static final String LOOTABLE_CRIMSON_FUNGUS;

    public static void Init()
    {
        LootTableLoadingCallback.EVENT.register(ArtifactManager::ModifyLootTable);
    }

    public static void ModifyLootTable(ResourceManager resourceManager, LootManager lootManager, Identifier id, FabricLootSupplierBuilder supplier, LootTableLoadingCallback.LootTableSetter setter)
    {
        if (ALLOWED_LOOTTABLES_BASIC.containsKey(id))
        {
            FabricLootPoolBuilder poolBuilder = FabricLootPoolBuilder.builder()
                    .rolls(ConstantLootNumberProvider.create(1))
                    .withCondition(RandomChanceLootCondition.builder(ALLOWED_LOOTTABLES_BASIC.get(id)).build())
                    .withFunction(new StackPreventer())
                    .withEntry(LOOT_GSON.fromJson(LOOTABLE_CLOCK, LootPoolEntry.class))
                    .withEntry(LOOT_GSON.fromJson(LOOTABLE_SPYGLASS, LootPoolEntry.class))
                    .withEntry(LOOT_GSON.fromJson(LOOTABLE_POISON_TALISMAN, LootPoolEntry.class))
                    .withEntry(LOOT_GSON.fromJson(LOOTABLE_WITHER_TALISMAN, LootPoolEntry.class))
                    .withEntry(LOOT_GSON.fromJson(LOOTABLE_CRIMSON_FUNGUS, LootPoolEntry.class));

            supplier.withPool(poolBuilder.build());
        }
        if (ALLOWED_LOOTTABLES_FULL.containsKey(id))
        {
            FabricLootPoolBuilder poolBuilder = FabricLootPoolBuilder.builder()
                    .rolls(ConstantLootNumberProvider.create(1))
                    .withCondition(RandomChanceLootCondition.builder(ALLOWED_LOOTTABLES_FULL.get(id)).build())
                    .withFunction(new StackPreventer())
                    .withEntry(LOOT_GSON.fromJson(LOOTABLE_SHIELD, LootPoolEntry.class))
                    .withEntry(LOOT_GSON.fromJson(LOOTABLE_CLOCK, LootPoolEntry.class))
                    .withEntry(LOOT_GSON.fromJson(LOOTABLE_CLOCK1, LootPoolEntry.class))
                    .withEntry(LOOT_GSON.fromJson(LOOTABLE_CLOCK2, LootPoolEntry.class))
                    .withEntry(LOOT_GSON.fromJson(LOOTABLE_CLOCK3, LootPoolEntry.class))
                    .withEntry(LOOT_GSON.fromJson(LOOTABLE_CLOCK4, LootPoolEntry.class))
                    .withEntry(LOOT_GSON.fromJson(LOOTABLE_CLOCK5, LootPoolEntry.class))
                    .withEntry(LOOT_GSON.fromJson(LOOTABLE_SPYGLASS, LootPoolEntry.class))
                    .withEntry(LOOT_GSON.fromJson(LOOTABLE_SPYGLASS1, LootPoolEntry.class))
                    .withEntry(LOOT_GSON.fromJson(LOOTABLE_SPYGLASS2, LootPoolEntry.class))
                    .withEntry(LOOT_GSON.fromJson(LOOTABLE_SPYGLASS3, LootPoolEntry.class))
                    .withEntry(LOOT_GSON.fromJson(LOOTABLE_POISON_TALISMAN, LootPoolEntry.class))
                    .withEntry(LOOT_GSON.fromJson(LOOTABLE_POISON_TALISMAN1, LootPoolEntry.class))
                    .withEntry(LOOT_GSON.fromJson(LOOTABLE_POISON_TALISMAN2, LootPoolEntry.class))
                    .withEntry(LOOT_GSON.fromJson(LOOTABLE_POISON_TALISMAN3, LootPoolEntry.class))
                    .withEntry(LOOT_GSON.fromJson(LOOTABLE_POISON_TALISMAN4, LootPoolEntry.class))
                    .withEntry(LOOT_GSON.fromJson(LOOTABLE_POISON_TALISMAN5, LootPoolEntry.class))
                    .withEntry(LOOT_GSON.fromJson(LOOTABLE_WITHER_TALISMAN, LootPoolEntry.class))
                    .withEntry(LOOT_GSON.fromJson(LOOTABLE_WITHER_TALISMAN1, LootPoolEntry.class))
                    .withEntry(LOOT_GSON.fromJson(LOOTABLE_WITHER_TALISMAN2, LootPoolEntry.class))
                    .withEntry(LOOT_GSON.fromJson(LOOTABLE_WITHER_TALISMAN3, LootPoolEntry.class))
                    .withEntry(LOOT_GSON.fromJson(LOOTABLE_WITHER_TALISMAN4, LootPoolEntry.class))
                    .withEntry(LOOT_GSON.fromJson(LOOTABLE_WITHER_TALISMAN5, LootPoolEntry.class))
                    .withEntry(LOOT_GSON.fromJson(LOOTABLE_CRIMSON_FUNGUS, LootPoolEntry.class));

            supplier.withPool(poolBuilder.build());
        }
    }

    public static ArtifactState GetPlayerArtifactState(PlayerInventory inventory)
    {
        ArtifactState artifactState = new ArtifactState();

        for (ItemStack stack : inventory.armor)
            artifactState.consider(stack, true);

        for (ItemStack stack : inventory.offHand)
            artifactState.consider(stack, true);

        for (ItemStack stack : inventory.main)
                artifactState.consider(stack,inventory.getMainHandStack() == stack);

        return artifactState;
    }

    static
    {
        ALLOWED_LOOTTABLES_FULL = new HashMap<>();
        ALLOWED_LOOTTABLES_FULL.put(new Identifier("minecraft", "blocks/spawner"), 0.20f);
        ALLOWED_LOOTTABLES_FULL.put(LootTables.SIMPLE_DUNGEON_CHEST, 0.15f);
        ALLOWED_LOOTTABLES_FULL.put(LootTables.ABANDONED_MINESHAFT_CHEST, 0.05f);
        ALLOWED_LOOTTABLES_FULL.put(LootTables.VILLAGE_CARTOGRAPHER_CHEST, 0.02f);

        ALLOWED_LOOTTABLES_FULL.put(LootTables.BASTION_TREASURE_CHEST, 0.25f);
        ALLOWED_LOOTTABLES_FULL.put(LootTables.BASTION_OTHER_CHEST, 0.1f);
        ALLOWED_LOOTTABLES_FULL.put(LootTables.BASTION_BRIDGE_CHEST, 0.1f);
        ALLOWED_LOOTTABLES_FULL.put(LootTables.BASTION_HOGLIN_STABLE_CHEST, 0.1f);

        ALLOWED_LOOTTABLES_BASIC = new HashMap<>();
        ALLOWED_LOOTTABLES_BASIC.put(LootTables.WOODLAND_MANSION_CHEST, 0.05f);
        ALLOWED_LOOTTABLES_BASIC.put(LootTables.PILLAGER_OUTPOST_CHEST, 0.05f);
        ALLOWED_LOOTTABLES_BASIC.put(LootTables.SHIPWRECK_MAP_CHEST, 0.05f);
        ALLOWED_LOOTTABLES_BASIC.put(LootTables.SHIPWRECK_SUPPLY_CHEST, 0.05f);
        ALLOWED_LOOTTABLES_BASIC.put(LootTables.SHIPWRECK_TREASURE_CHEST, 0.05f);
        ALLOWED_LOOTTABLES_BASIC.put(LootTables.UNDERWATER_RUIN_SMALL_CHEST, 0.05f);
        ALLOWED_LOOTTABLES_BASIC.put(LootTables.UNDERWATER_RUIN_BIG_CHEST, 0.05f);

        ALLOWED_LOOTTABLES_BASIC.put(LootTables.IGLOO_CHEST_CHEST, 0.05f);
        ALLOWED_LOOTTABLES_BASIC.put(LootTables.JUNGLE_TEMPLE_DISPENSER_CHEST, 0.05f);
        ALLOWED_LOOTTABLES_BASIC.put(LootTables.JUNGLE_TEMPLE_CHEST, 0.05f);
        ALLOWED_LOOTTABLES_BASIC.put(LootTables.DESERT_PYRAMID_CHEST, 0.05f);

        ALLOWED_LOOTTABLES_BASIC.put(LootTables.STRONGHOLD_CORRIDOR_CHEST, 0.05f);
        ALLOWED_LOOTTABLES_BASIC.put(LootTables.STRONGHOLD_CROSSING_CHEST, 0.05f);
        ALLOWED_LOOTTABLES_BASIC.put(LootTables.STRONGHOLD_LIBRARY_CHEST, 0.05f);

        ALLOWED_LOOTTABLES_BASIC.put(LootTables.FISHING_TREASURE_GAMEPLAY, 0.02f);

        LOOTABLE_SHIELD = ResourceLoader.LoadResource("data/shield.json");
        LOOTABLE_CLOCK = ResourceLoader.LoadResource("data/clock/clock.json");
        LOOTABLE_CLOCK1 = ResourceLoader.LoadResource("data/clock/clock1.json");
        LOOTABLE_CLOCK2 = ResourceLoader.LoadResource("data/clock/clock2.json");
        LOOTABLE_CLOCK3 = ResourceLoader.LoadResource("data/clock/clock3.json");
        LOOTABLE_CLOCK4 = ResourceLoader.LoadResource("data/clock/clock4.json");
        LOOTABLE_CLOCK5 = ResourceLoader.LoadResource("data/clock/clock5.json");
        LOOTABLE_SPYGLASS = ResourceLoader.LoadResource("data/spyglass/spyglass.json");
        LOOTABLE_SPYGLASS1 = ResourceLoader.LoadResource("data/spyglass/spyglass1.json");
        LOOTABLE_SPYGLASS2 = ResourceLoader.LoadResource("data/spyglass/spyglass2.json");
        LOOTABLE_SPYGLASS3 = ResourceLoader.LoadResource("data/spyglass/spyglass3.json");
        LOOTABLE_POISON_TALISMAN = ResourceLoader.LoadResource("data/poison_talisman/poison_talisman.json");
        LOOTABLE_POISON_TALISMAN1 = ResourceLoader.LoadResource("data/poison_talisman/poison_talisman1.json");
        LOOTABLE_POISON_TALISMAN2 = ResourceLoader.LoadResource("data/poison_talisman/poison_talisman2.json");
        LOOTABLE_POISON_TALISMAN3 = ResourceLoader.LoadResource("data/poison_talisman/poison_talisman3.json");
        LOOTABLE_POISON_TALISMAN4 = ResourceLoader.LoadResource("data/poison_talisman/poison_talisman4.json");
        LOOTABLE_POISON_TALISMAN5 = ResourceLoader.LoadResource("data/poison_talisman/poison_talisman5.json");
        LOOTABLE_WITHER_TALISMAN = ResourceLoader.LoadResource("data/wither_talisman/wither_talisman.json");
        LOOTABLE_WITHER_TALISMAN1 = ResourceLoader.LoadResource("data/wither_talisman/wither_talisman1.json");
        LOOTABLE_WITHER_TALISMAN2 = ResourceLoader.LoadResource("data/wither_talisman/wither_talisman2.json");
        LOOTABLE_WITHER_TALISMAN3 = ResourceLoader.LoadResource("data/wither_talisman/wither_talisman3.json");
        LOOTABLE_WITHER_TALISMAN4 = ResourceLoader.LoadResource("data/wither_talisman/wither_talisman4.json");
        LOOTABLE_WITHER_TALISMAN5 = ResourceLoader.LoadResource("data/wither_talisman/wither_talisman5.json");
        LOOTABLE_CRIMSON_FUNGUS = ResourceLoader.LoadResource("data/crimson_fungus.json");
    }
}
