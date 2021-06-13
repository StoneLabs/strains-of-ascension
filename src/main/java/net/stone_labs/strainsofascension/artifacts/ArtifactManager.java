package net.stone_labs.strainsofascension.artifacts;

import com.google.gson.Gson;
import net.fabricmc.fabric.api.loot.v1.FabricLootPoolBuilder;
import net.fabricmc.fabric.api.loot.v1.FabricLootSupplierBuilder;
import net.fabricmc.fabric.api.loot.v1.event.LootTableLoadingCallback;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.*;
import net.minecraft.loot.condition.RandomChanceLootCondition;
import net.minecraft.loot.context.LootContext;
import net.minecraft.loot.context.LootContextParameters;
import net.minecraft.loot.context.LootContextTypes;
import net.minecraft.loot.entry.LootPoolEntry;
import net.minecraft.loot.provider.number.*;
import net.minecraft.resource.ResourceManager;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.stone_labs.strainsofascension.utils.ResourceLoader;
import net.stone_labs.strainsofascension.utils.StackPreventer;

import java.util.*;

public class ArtifactManager
{
    private static class ArtifactManagerLootType
    {
        private final Map<Identifier, Float> lootTableProbabilities;
        private final List<String> lootablesJsons;

        public LootPool LootPool;
        public boolean IncludeInGiveCommand;

        public ArtifactManagerLootType(boolean includeInGiveCommand)
        {
            this.lootTableProbabilities = new HashMap<>();
            this.lootablesJsons = new ArrayList<>();
            IncludeInGiveCommand = includeInGiveCommand;
        }

        public void RegisterLoottable(Identifier identifier, Float propability)
        {
            lootTableProbabilities.put(identifier, propability);
        }

        public void RegisterCopyLoottable(ArtifactManagerLootType other)
        {
            lootTableProbabilities.putAll(other.lootTableProbabilities);
        }

        public void RegisterLootable(String path)
        {
            lootablesJsons.add(ResourceLoader.LoadResource(path));
        }

        public boolean ContainsLootTable(Identifier identifier)
        {
            return lootTableProbabilities.containsKey(identifier);
        }

        public float GetLootTablePropability(Identifier identifier)
        {
            return lootTableProbabilities.get(identifier);
        }

        public List<String> GetLootableJsons()
        {
            return lootablesJsons;
        }
    }

    private static final Gson LOOT_GSON = LootGsons.getTableGsonBuilder().create();
    private static final List<ArtifactManagerLootType> LOOT_TYPES;

    public static void Init()
    {
        LootTableLoadingCallback.EVENT.register(ArtifactManager::ModifyLootTable);
    }

    public static void ModifyLootTable(ResourceManager resourceManager, LootManager lootManager, Identifier id, FabricLootSupplierBuilder supplier, LootTableLoadingCallback.LootTableSetter setter)
    {
        for (ArtifactManagerLootType lootType : LOOT_TYPES)
            if (lootType.ContainsLootTable(id))
            {
                FabricLootPoolBuilder poolBuilder = FabricLootPoolBuilder.builder()
                        .rolls(ConstantLootNumberProvider.create(1))
                        .withCondition(RandomChanceLootCondition.builder(lootType.GetLootTablePropability(id)).build())
                        .withFunction(new StackPreventer());

                for (String json : lootType.GetLootableJsons())
                    poolBuilder.withEntry(LOOT_GSON.fromJson(json, LootPoolEntry.class));

                lootType.LootPool = poolBuilder.build();
                supplier.withPool(lootType.LootPool);
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
            artifactState.consider(stack, inventory.getMainHandStack() == stack);

        return artifactState;
    }

    public static void DropPlayerFullPool(ServerPlayerEntity player)
    {
        List<ItemStack> drops = new ArrayList<>();

        java.util.function.Consumer<ItemStack> applier = (itemStack) ->
        {
            if (drops.stream().noneMatch(stack -> stack.getName().getString().equals(itemStack.getName().getString())))
                drops.add(itemStack);
        };

        // Yes this is a bad solution.
        // But the chance of a item with loot change 0.05% is missing is e-11 so it should be fine
        for (int i = 0; i < 50000; i++)
        {
            for (ArtifactManagerLootType lootType : LOOT_TYPES)
                lootType.LootPool.addGeneratedLoot(applier,
                        new LootContext.Builder(player.getServerWorld())
                                .parameter(LootContextParameters.ORIGIN, player.getPos())
                                .random(new Random())
                                .luck(3)
                                .build(LootContextTypes.COMMAND));
        }
        drops.sort(Comparator.comparing(o -> o.getName().getString()));

        BlockPos position = player.getBlockPos().add(0, -1, 0);
        for (int barrel_num = 0; barrel_num * 27 <= drops.size(); barrel_num++)
        {
            position = position.add(0, 1, 0);
            List<ItemStack> barrel_content = drops.subList(barrel_num * 27, Math.min((barrel_num + 1) * 27, drops.size()));

            player.world.setBlockState(position, Blocks.BARREL.getDefaultState());
            var barrel = player.world.getBlockEntity(position, BlockEntityType.BARREL);
            if (!barrel.isPresent())
                return;

            for (int i = 0; i < barrel_content.size(); i++)
                barrel.get().setStack(i, barrel_content.get(i));
        }
        player.setPos(position.getX() + 0.5, position.getY() + 1, position.getZ() + 0.5);
    }

    static
    {
        ArtifactManagerLootType FullLoot = new ArtifactManagerLootType(true);
        FullLoot.RegisterLoottable(new Identifier("minecraft", "blocks/spawner"), 0.40f);
        FullLoot.RegisterLoottable(LootTables.SIMPLE_DUNGEON_CHEST, 0.15f);
        FullLoot.RegisterLoottable(LootTables.ABANDONED_MINESHAFT_CHEST, 0.10f);
        FullLoot.RegisterLoottable(LootTables.VILLAGE_CARTOGRAPHER_CHEST, 0.02f);

        FullLoot.RegisterLoottable(LootTables.BASTION_TREASURE_CHEST, 0.25f);
        FullLoot.RegisterLoottable(LootTables.BASTION_OTHER_CHEST, 0.1f);
        FullLoot.RegisterLoottable(LootTables.BASTION_BRIDGE_CHEST, 0.1f);
        FullLoot.RegisterLoottable(LootTables.BASTION_HOGLIN_STABLE_CHEST, 0.1f);

        FullLoot.RegisterLootable("data/shield.json");
        FullLoot.RegisterLootable("data/clock/clock.json");
        FullLoot.RegisterLootable("data/clock/clock1.json");
        FullLoot.RegisterLootable("data/clock/clock2.json");
        FullLoot.RegisterLootable("data/clock/clock3.json");
        FullLoot.RegisterLootable("data/clock/clock4.json");
        FullLoot.RegisterLootable("data/clock/clock5.json");
        FullLoot.RegisterLootable("data/spyglass/spyglass.json");
        FullLoot.RegisterLootable("data/spyglass/spyglass1.json");
        FullLoot.RegisterLootable("data/spyglass/spyglass2.json");
        FullLoot.RegisterLootable("data/spyglass/spyglass3.json");
        FullLoot.RegisterLootable("data/poison_talisman/poison_talisman.json");
        FullLoot.RegisterLootable("data/poison_talisman/poison_talisman1.json");
        FullLoot.RegisterLootable("data/poison_talisman/poison_talisman2.json");
        FullLoot.RegisterLootable("data/poison_talisman/poison_talisman3.json");
        FullLoot.RegisterLootable("data/poison_talisman/poison_talisman4.json");
        FullLoot.RegisterLootable("data/poison_talisman/poison_talisman5.json");
        FullLoot.RegisterLootable("data/wither_talisman/wither_talisman.json");
        FullLoot.RegisterLootable("data/wither_talisman/wither_talisman1.json");
        FullLoot.RegisterLootable("data/wither_talisman/wither_talisman2.json");
        FullLoot.RegisterLootable("data/wither_talisman/wither_talisman3.json");
        FullLoot.RegisterLootable("data/wither_talisman/wither_talisman4.json");
        FullLoot.RegisterLootable("data/wither_talisman/wither_talisman5.json");
        FullLoot.RegisterLootable("data/crimson_fungus.json");
        FullLoot.RegisterLootable("data/rabbit_foot.json");
        FullLoot.RegisterLootable("data/depth_mending_book/depth_mending_book.json");
        FullLoot.RegisterLootable("data/depth_mending_book/depth_mending_book1.json");
        FullLoot.RegisterLootable("data/depth_mending_book/depth_mending_book2.json");
        FullLoot.RegisterLootable("data/depth_mending_book/depth_mending_book3.json");

        ArtifactManagerLootType BasicLoot = new ArtifactManagerLootType(false);
        BasicLoot.RegisterLoottable(LootTables.WOODLAND_MANSION_CHEST, 0.05f);
        BasicLoot.RegisterLoottable(LootTables.PILLAGER_OUTPOST_CHEST, 0.05f);
        BasicLoot.RegisterLoottable(LootTables.SHIPWRECK_MAP_CHEST, 0.05f);
        BasicLoot.RegisterLoottable(LootTables.SHIPWRECK_SUPPLY_CHEST, 0.05f);
        BasicLoot.RegisterLoottable(LootTables.SHIPWRECK_TREASURE_CHEST, 0.05f);
        BasicLoot.RegisterLoottable(LootTables.UNDERWATER_RUIN_SMALL_CHEST, 0.05f);
        BasicLoot.RegisterLoottable(LootTables.UNDERWATER_RUIN_BIG_CHEST, 0.05f);

        BasicLoot.RegisterLoottable(LootTables.IGLOO_CHEST_CHEST, 0.05f);
        BasicLoot.RegisterLoottable(LootTables.JUNGLE_TEMPLE_DISPENSER_CHEST, 0.05f);
        BasicLoot.RegisterLoottable(LootTables.JUNGLE_TEMPLE_CHEST, 0.05f);
        BasicLoot.RegisterLoottable(LootTables.DESERT_PYRAMID_CHEST, 0.05f);

        BasicLoot.RegisterLoottable(LootTables.STRONGHOLD_CORRIDOR_CHEST, 0.05f);
        BasicLoot.RegisterLoottable(LootTables.STRONGHOLD_CROSSING_CHEST, 0.05f);
        BasicLoot.RegisterLoottable(LootTables.STRONGHOLD_LIBRARY_CHEST, 0.05f);

        BasicLoot.RegisterLoottable(LootTables.FISHING_TREASURE_GAMEPLAY, 0.02f);

        BasicLoot.RegisterLootable("data/clock/clock.json");
        BasicLoot.RegisterLootable("data/spyglass/spyglass.json");
        BasicLoot.RegisterLootable("data/poison_talisman/poison_talisman.json");
        BasicLoot.RegisterLootable("data/wither_talisman/wither_talisman.json");
        BasicLoot.RegisterLootable("data/crimson_fungus.json");
        BasicLoot.RegisterLootable("data/rabbit_foot.json");

        ArtifactManagerLootType LoreLoot = new ArtifactManagerLootType(true);
        LoreLoot.RegisterLoottable(LootTables.SIMPLE_DUNGEON_CHEST, 0.40f);
        LoreLoot.RegisterLoottable(LootTables.ABANDONED_MINESHAFT_CHEST, 0.15f);
        LoreLoot.RegisterLoottable(LootTables.VILLAGE_CARTOGRAPHER_CHEST, 0.10f);

        LoreLoot.RegisterLoottable(LootTables.WOODLAND_MANSION_CHEST, 0.10f);
        LoreLoot.RegisterLoottable(LootTables.PILLAGER_OUTPOST_CHEST, 0.15f);
        LoreLoot.RegisterLoottable(LootTables.SHIPWRECK_MAP_CHEST, 0.10f);
        LoreLoot.RegisterLoottable(LootTables.SHIPWRECK_SUPPLY_CHEST, 0.10f);
        LoreLoot.RegisterLoottable(LootTables.SHIPWRECK_TREASURE_CHEST, 0.10f);
        LoreLoot.RegisterLoottable(LootTables.UNDERWATER_RUIN_SMALL_CHEST, 0.10f);
        LoreLoot.RegisterLoottable(LootTables.UNDERWATER_RUIN_BIG_CHEST, 0.10f);

        LoreLoot.RegisterLoottable(LootTables.IGLOO_CHEST_CHEST, 0.15f);
        LoreLoot.RegisterLoottable(LootTables.JUNGLE_TEMPLE_DISPENSER_CHEST, 0.15f);
        LoreLoot.RegisterLoottable(LootTables.JUNGLE_TEMPLE_CHEST, 0.15f);
        LoreLoot.RegisterLoottable(LootTables.DESERT_PYRAMID_CHEST, 0.15f);

        LoreLoot.RegisterLoottable(LootTables.STRONGHOLD_CORRIDOR_CHEST, 0.15f);
        LoreLoot.RegisterLoottable(LootTables.STRONGHOLD_CROSSING_CHEST, 0.15f);
        LoreLoot.RegisterLoottable(LootTables.STRONGHOLD_LIBRARY_CHEST, 0.15f);

        LoreLoot.RegisterLoottable(LootTables.FISHING_TREASURE_GAMEPLAY, 0.02f);

        LoreLoot.RegisterLootable("data/lore/guidebook_introduction.json");
        LoreLoot.RegisterLootable("data/lore/guidebook_layers.json");
        LoreLoot.RegisterLootable("data/lore/guidebook_layer1.json");
        LoreLoot.RegisterLootable("data/lore/guidebook_layer2.json");
        LoreLoot.RegisterLootable("data/lore/guidebook_layer3.json");
        LoreLoot.RegisterLootable("data/lore/guidebook_layer4.json");
        LoreLoot.RegisterLootable("data/lore/guidebook_layer5.json");
        LoreLoot.RegisterLootable("data/lore/guidebook_layer6.json");
        LoreLoot.RegisterLootable("data/lore/guidebook_layer7.json");
        LoreLoot.RegisterLootable("data/lore/guidebook_layer8.json");
        LoreLoot.RegisterLootable("data/lore/guidebook_layer9.json");
        LoreLoot.RegisterLootable("data/lore/guidebook_artifacts.json");
        LoreLoot.RegisterLootable("data/lore/guidebook_artifact_clock.json");
        LoreLoot.RegisterLootable("data/lore/guidebook_artifact_crimson_fungus.json");
        LoreLoot.RegisterLootable("data/lore/guidebook_artifact_poison_talisman.json");
        LoreLoot.RegisterLootable("data/lore/guidebook_artifact_rabbit_foot.json");
        LoreLoot.RegisterLootable("data/lore/guidebook_artifact_shield.json");
        LoreLoot.RegisterLootable("data/lore/guidebook_artifact_spyglass.json");
        LoreLoot.RegisterLootable("data/lore/guidebook_artifact_wither_talisman.json");

        LOOT_TYPES = new ArrayList<>()
        {{
            add(BasicLoot);
            add(FullLoot);
            add(LoreLoot);
        }};
    }
}
