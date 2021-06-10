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
import net.minecraft.util.Identifier;
import net.stone_labs.strainsofascension.utils.ResourceLoader;
import net.stone_labs.strainsofascension.utils.StackPreventer;

public class ArtifactManager
{
    private static final Gson LOOT_GSON = LootGsons.getTableGsonBuilder().create();

    private static final Identifier LOOTTABLE_ID_SPAWNER;
    private static final String LOOTABLE_COMPASS;

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
                    .withFunction(new StackPreventer())
                    /*.withEntry(LOOT_GSON.fromJson(LOOTABLE_COMPASS, LootPoolEntry.class))
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
                    .withEntry(LOOT_GSON.fromJson(LOOTABLE_POISON_TALISMAN5, LootPoolEntry.class))*/
                    .withEntry(LOOT_GSON.fromJson(LOOTABLE_WITHER_TALISMAN, LootPoolEntry.class))
                    .withEntry(LOOT_GSON.fromJson(LOOTABLE_WITHER_TALISMAN1, LootPoolEntry.class))
                    .withEntry(LOOT_GSON.fromJson(LOOTABLE_WITHER_TALISMAN2, LootPoolEntry.class))
                    .withEntry(LOOT_GSON.fromJson(LOOTABLE_WITHER_TALISMAN3, LootPoolEntry.class))
                    .withEntry(LOOT_GSON.fromJson(LOOTABLE_WITHER_TALISMAN4, LootPoolEntry.class))
                    .withEntry(LOOT_GSON.fromJson(LOOTABLE_WITHER_TALISMAN5, LootPoolEntry.class));

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
        LOOTTABLE_ID_SPAWNER = new Identifier("minecraft", "blocks/spawner");

        LOOTABLE_COMPASS = ResourceLoader.LoadResource("data/shield.json");
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
    }
}
