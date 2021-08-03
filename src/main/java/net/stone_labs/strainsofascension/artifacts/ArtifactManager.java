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
import net.minecraft.nbt.NbtCompound;
import net.minecraft.resource.ResourceManager;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.stone_labs.strainsofascension.utils.ResourceLoader;

import java.util.*;

public class ArtifactManager
{
    private static class ArtifactManagerLootType
    {
        private final Map<Identifier, Float> lootTableProbabilities;
        private final List<String> lootablesJSONs;

        public boolean IncludeInGiveCommand;

        public ArtifactManagerLootType(boolean includeInGiveCommand)
        {
            this.lootTableProbabilities = new HashMap<>();
            this.lootablesJSONs = new ArrayList<>();
            IncludeInGiveCommand = includeInGiveCommand;
        }

        public void RegisterLootTable(Identifier identifier, Float propability)
        {
            lootTableProbabilities.put(identifier, propability);
        }

        public void RegisterLootable(String path)
        {
            lootablesJSONs.add(ResourceLoader.LoadResource(path));
        }

        public boolean ContainsLootTable(Identifier identifier)
        {
            return lootTableProbabilities.containsKey(identifier);
        }

        public float GetLootTableProbability(Identifier identifier)
        {
            return lootTableProbabilities.get(identifier);
        }

        public List<String> GetLootableJsons()
        {
            return lootablesJSONs;
        }

        private LootPool LootPool = null;
        private void CreateLootPool()
        {
            FabricLootPoolBuilder poolBuilder = FabricLootPoolBuilder.builder()
                    .rolls(ConstantLootNumberProvider.create(1))
                    .withFunction(new StackPreventerLootFunction())
                    .withFunction(new ArtifactVersionLootFunction());

            for (String json : this.GetLootableJsons())
                poolBuilder.withEntry(LOOT_GSON.fromJson(json, LootPoolEntry.class));

            this.LootPool = poolBuilder.build();
        }
        public void GenerateLoot(ServerWorld world, Vec3d origin, int number, java.util.function.Consumer<ItemStack> consumer)
        {
            if (LootPool == null)
                CreateLootPool();

            for (int i = 0; i < number; i++)
                this.LootPool.addGeneratedLoot(consumer,
                        new LootContext.Builder(world)
                                .parameter(LootContextParameters.ORIGIN, origin)
                                .luck(3)
                                .random(new Random())
                                .build(LootContextTypes.COMMAND));
        }
    }

    private static final Gson LOOT_GSON = LootGsons.getTableGsonBuilder().create();
    private static final List<ArtifactManagerLootType> LOOT_TYPES;
    private static final ArtifactManagerLootType LOOT_TYPE_FULL;

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
                        .withCondition(RandomChanceLootCondition.builder(lootType.GetLootTableProbability(id)).build())
                        .withFunction(new StackPreventerLootFunction())
                        .withFunction(new ArtifactVersionLootFunction());

                for (String json : lootType.GetLootableJsons())
                    poolBuilder.withEntry(LOOT_GSON.fromJson(json, LootPoolEntry.class));

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

    @SuppressWarnings("ConstantConditions")
    public static boolean isArtifact(ItemStack stack)
    {
        if (!stack.hasNbt())
            return false;

        NbtCompound tag = stack.getNbt();

        Artifact artifact = Artifact.ByID(tag.getInt("artifact"));
        return artifact != null;
    }

    public static void DropFullLootItems(ServerWorld world, Vec3d origin, int number, java.util.function.Consumer<ItemStack> consumer)
    {
        List<ItemStack> drops = new ArrayList<>();
        while (drops.size() < number)
            LOOT_TYPE_FULL.GenerateLoot(world, origin, number, drops::add);

        for (ItemStack stack : drops)
            consumer.accept(stack);
    }

    public static void DropPlayerFullPool(ServerPlayerEntity player)
    {
        List<ItemStack> drops = new ArrayList<>();

        java.util.function.Consumer<ItemStack> applier = (itemStack) ->
        {
            if (drops.stream().noneMatch(stack -> stack.getName().getString().equals(itemStack.getName().getString())))
                drops.add(itemStack);
        };

        // Yes this is a terrible solution. But i dont see a simpler way sadly...
        // Also the chance of a item with loot change 0.05% is missing is e-11 so it should be fine
        for (ArtifactManagerLootType lootType : LOOT_TYPES)
            lootType.GenerateLoot(player.getServerWorld(), player.getPos(), 50000, applier);

        drops.sort(Comparator.comparing(o -> o.getName().getString()));

        BlockPos position = player.getBlockPos().add(0, -1, 0);
        for (int barrel_num = 0; barrel_num * 27 <= drops.size(); barrel_num++)
        {
            position = position.add(0, 1, 0);
            List<ItemStack> barrel_content = drops.subList(barrel_num * 27, Math.min((barrel_num + 1) * 27, drops.size()));

            player.world.setBlockState(position, Blocks.BARREL.getDefaultState());
            var barrel = player.world.getBlockEntity(position, BlockEntityType.BARREL);

            //noinspection SimplifyOptionalCallChains
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
        FullLoot.RegisterLootTable(new Identifier("minecraft", "blocks/spawner"), 0.40f);
        FullLoot.RegisterLootTable(LootTables.SIMPLE_DUNGEON_CHEST, 0.15f);
        FullLoot.RegisterLootTable(LootTables.ABANDONED_MINESHAFT_CHEST, 0.10f);
        FullLoot.RegisterLootTable(LootTables.VILLAGE_CARTOGRAPHER_CHEST, 0.02f);

        FullLoot.RegisterLootTable(LootTables.BASTION_TREASURE_CHEST, 0.25f);
        FullLoot.RegisterLootTable(LootTables.BASTION_OTHER_CHEST, 0.1f);
        FullLoot.RegisterLootTable(LootTables.BASTION_BRIDGE_CHEST, 0.1f);
        FullLoot.RegisterLootTable(LootTables.BASTION_HOGLIN_STABLE_CHEST, 0.1f);

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
        BasicLoot.RegisterLootTable(LootTables.WOODLAND_MANSION_CHEST, 0.05f);
        BasicLoot.RegisterLootTable(LootTables.PILLAGER_OUTPOST_CHEST, 0.05f);
        BasicLoot.RegisterLootTable(LootTables.SHIPWRECK_MAP_CHEST, 0.05f);
        BasicLoot.RegisterLootTable(LootTables.SHIPWRECK_SUPPLY_CHEST, 0.05f);
        BasicLoot.RegisterLootTable(LootTables.SHIPWRECK_TREASURE_CHEST, 0.05f);
        BasicLoot.RegisterLootTable(LootTables.UNDERWATER_RUIN_SMALL_CHEST, 0.05f);
        BasicLoot.RegisterLootTable(LootTables.UNDERWATER_RUIN_BIG_CHEST, 0.05f);

        BasicLoot.RegisterLootTable(LootTables.IGLOO_CHEST_CHEST, 0.05f);
        BasicLoot.RegisterLootTable(LootTables.JUNGLE_TEMPLE_DISPENSER_CHEST, 0.05f);
        BasicLoot.RegisterLootTable(LootTables.JUNGLE_TEMPLE_CHEST, 0.05f);
        BasicLoot.RegisterLootTable(LootTables.DESERT_PYRAMID_CHEST, 0.05f);

        BasicLoot.RegisterLootTable(LootTables.STRONGHOLD_CORRIDOR_CHEST, 0.05f);
        BasicLoot.RegisterLootTable(LootTables.STRONGHOLD_CROSSING_CHEST, 0.05f);
        BasicLoot.RegisterLootTable(LootTables.STRONGHOLD_LIBRARY_CHEST, 0.05f);

        BasicLoot.RegisterLootTable(LootTables.FISHING_TREASURE_GAMEPLAY, 0.02f);

        BasicLoot.RegisterLootable("data/clock/clock.json");
        BasicLoot.RegisterLootable("data/spyglass/spyglass.json");
        BasicLoot.RegisterLootable("data/poison_talisman/poison_talisman.json");
        BasicLoot.RegisterLootable("data/wither_talisman/wither_talisman.json");
        BasicLoot.RegisterLootable("data/crimson_fungus.json");
        BasicLoot.RegisterLootable("data/rabbit_foot.json");

        ArtifactManagerLootType LoreLoot = new ArtifactManagerLootType(true);
        LoreLoot.RegisterLootTable(LootTables.SIMPLE_DUNGEON_CHEST, 0.40f);
        LoreLoot.RegisterLootTable(LootTables.ABANDONED_MINESHAFT_CHEST, 0.15f);
        LoreLoot.RegisterLootTable(LootTables.VILLAGE_CARTOGRAPHER_CHEST, 0.10f);

        LoreLoot.RegisterLootTable(LootTables.WOODLAND_MANSION_CHEST, 0.10f);
        LoreLoot.RegisterLootTable(LootTables.PILLAGER_OUTPOST_CHEST, 0.15f);
        LoreLoot.RegisterLootTable(LootTables.SHIPWRECK_MAP_CHEST, 0.10f);
        LoreLoot.RegisterLootTable(LootTables.SHIPWRECK_SUPPLY_CHEST, 0.10f);
        LoreLoot.RegisterLootTable(LootTables.SHIPWRECK_TREASURE_CHEST, 0.10f);
        LoreLoot.RegisterLootTable(LootTables.UNDERWATER_RUIN_SMALL_CHEST, 0.10f);
        LoreLoot.RegisterLootTable(LootTables.UNDERWATER_RUIN_BIG_CHEST, 0.10f);

        LoreLoot.RegisterLootTable(LootTables.IGLOO_CHEST_CHEST, 0.15f);
        LoreLoot.RegisterLootTable(LootTables.JUNGLE_TEMPLE_DISPENSER_CHEST, 0.15f);
        LoreLoot.RegisterLootTable(LootTables.JUNGLE_TEMPLE_CHEST, 0.15f);
        LoreLoot.RegisterLootTable(LootTables.DESERT_PYRAMID_CHEST, 0.15f);

        LoreLoot.RegisterLootTable(LootTables.STRONGHOLD_CORRIDOR_CHEST, 0.15f);
        LoreLoot.RegisterLootTable(LootTables.STRONGHOLD_CROSSING_CHEST, 0.15f);
        LoreLoot.RegisterLootTable(LootTables.STRONGHOLD_LIBRARY_CHEST, 0.15f);

        LoreLoot.RegisterLootTable(LootTables.FISHING_TREASURE_GAMEPLAY, 0.02f);

        LoreLoot.RegisterLootable("data/lore/guidebook_introduction.json");
        LoreLoot.RegisterLootable("data/lore/guidebook_spawner.json");
        LoreLoot.RegisterLootable("data/lore/guidebook_portal.json");
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
        LoreLoot.RegisterLootable("data/lore/guidebook_artifact_depth_mending.json");

        LOOT_TYPES = new ArrayList<>()
        {{
            add(BasicLoot);
            add(FullLoot);
            add(LoreLoot);
        }};
        LOOT_TYPE_FULL = FullLoot;
    }
}
