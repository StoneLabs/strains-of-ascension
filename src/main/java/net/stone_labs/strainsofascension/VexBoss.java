package net.stone_labs.strainsofascension;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.FrostedIceBlock;
import net.minecraft.data.client.model.BlockStateSupplier;
import net.minecraft.entity.EntityData;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.mob.EvokerEntity;
import net.minecraft.entity.mob.VexEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.LiteralText;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.stone_labs.strainsofascension.artifacts.ArtifactManager;
import net.stone_labs.strainsofascension.artifacts.ArtifactState;

import java.util.Random;

public class VexBoss implements ServerTickEvents.EndTick
{
    public final int SUMMON_DISTANCE = 10;

    public boolean fightInProgress = false;
    public long tickCounter = 0;

    BlockPos summonPos;

    Random random = new Random();

    @Override
    public void onEndTick(MinecraftServer server)
    {
        final int PLATFORM_HEIGHT = 300;
        final int TELEPORT_HEIGHT = 306;
        final int SPAWN_HEIGHT = 306;

        if (!fightInProgress)
            return;

        if (tickCounter == 0)
        {
            for (int x = -SUMMON_DISTANCE; x <= SUMMON_DISTANCE; x++)
                for (int z = -SUMMON_DISTANCE; z <= SUMMON_DISTANCE; z++)
                {
                    BlockPos pos = new BlockPos(summonPos.getX() + x, PLATFORM_HEIGHT, summonPos.getZ() + z);
                    if (pos.isWithinDistance(summonPos.withY(PLATFORM_HEIGHT), SUMMON_DISTANCE))
                        server.getOverworld().setBlockState(pos, Blocks.BLACK_STAINED_GLASS.getDefaultState());
                }
        }

        if (tickCounter > 200)
        {
            for (int x = -SUMMON_DISTANCE; x <= SUMMON_DISTANCE; x++)
                for (int z = -SUMMON_DISTANCE; z <= SUMMON_DISTANCE; z++)
                {
                    BlockPos pos = new BlockPos(summonPos.getX() + x, PLATFORM_HEIGHT, summonPos.getZ() + z);
                    if (pos.isWithinDistance(summonPos.withY(PLATFORM_HEIGHT), SUMMON_DISTANCE))
                    {
                        if (random.nextFloat() > 0.1f)
                            continue;

                        if (server.getOverworld().isAir(pos))
                        {
                            if (random.nextFloat() < (1.0f / 10.0f))
                                server.getOverworld().setBlockState(pos, Blocks.BLACK_STAINED_GLASS.getDefaultState());
                        }
                        else if (server.getOverworld().getBlockState(pos).isOf(Blocks.BLACK_STAINED_GLASS))
                        {
                            if (random.nextFloat() < (1.0f / 100.0f))
                                server.getOverworld().setBlockState(pos, Blocks.RED_STAINED_GLASS.getDefaultState());
                        }
                        else if (server.getOverworld().getBlockState(pos).isOf(Blocks.RED_STAINED_GLASS))
                        {
                            if (random.nextFloat() < (1.0f / 4.0f))
                                server.getOverworld().removeBlock(pos, false);
                        }
                    }
                }
        }

        if (tickCounter < 75 && tickCounter % 10 == 0)
            for (ServerPlayerEntity player : server.getPlayerManager().getPlayerList())
                player.playSound(SoundEvents.ENTITY_VEX_AMBIENT, SoundCategory.HOSTILE, 100.0f, 0.1f);

        if (tickCounter == 5)
            for (ServerPlayerEntity player : server.getPlayerManager().getPlayerList())
                player.sendMessage(new LiteralText("The ground begins to rumble..."), false);

        if (tickCounter > 5 && tickCounter < 75)
            for (int i = 0; i < 200; i++)
            {
                double distance = Math.cbrt(random.nextDouble()) * SUMMON_DISTANCE;
                double theta = random.nextDouble() * 2 * Math.PI;
                double phi = Math.acos(2.0 * random.nextDouble() - 1.0);

                double x = distance * Math.cos(theta) * Math.sin(phi);
                double z = distance * Math.sin(theta) * Math.sin(phi);
                double y = distance * Math.cos(phi);

                server.getOverworld().spawnParticles(ParticleTypes.GLOW, summonPos.getX() + x, summonPos.getY() + y, summonPos.getZ() + z, 1, 0, 0, 0, 1);
            }

        if (tickCounter == 80)
        {
            for (ServerPlayerEntity player : server.getPlayerManager().getPlayerList())
                if (summonPos.isWithinDistance(player.getBlockPos(), SUMMON_DISTANCE))
                {
                    server.getOverworld().spawnParticles(ParticleTypes.POOF, player.getPos().x, player.getPos().y, player.getPos().z, 50, 0, 0, 0, 1);
                    player.teleport(player.getPos().x, TELEPORT_HEIGHT, player.getPos().z);

                    player.playSound(SoundEvents.ENTITY_ENDERMAN_TELEPORT, SoundCategory.HOSTILE, 100.0f, 1f);
                }
        }

        if (tickCounter == 150)
        {
            VexEntity vexEntity = (VexEntity)EntityType.VEX.create(server.getOverworld());
            vexEntity.refreshPositionAndAngles(summonPos.withY(SPAWN_HEIGHT), 0.0F, 0.0F);
            vexEntity.initialize(server.getOverworld(), server.getOverworld().getLocalDifficulty(summonPos.withY(SPAWN_HEIGHT)), SpawnReason.MOB_SUMMONED, null, null);
            vexEntity.setBounds(summonPos.withY(SPAWN_HEIGHT));
            vexEntity.setLifeTicks(Integer.MAX_VALUE);

            vexEntity.getAttributeInstance(EntityAttributes.GENERIC_MAX_HEALTH).setBaseValue(200);
            vexEntity.getAttributeInstance(EntityAttributes.GENERIC_ATTACK_DAMAGE).setBaseValue(15);
            vexEntity.heal(2000);

            server.getOverworld().spawnEntityAndPassengers(vexEntity);
        }

        if (tickCounter > 1000)
        {
            for (ServerPlayerEntity player : server.getPlayerManager().getPlayerList())
                player.sendMessage(new LiteralText("Summon completed."), false);

            StopFight();
        }

        tickCounter++;
    }

    private void StopFight()
    {
        fightInProgress = false;
    }

    public void Summon(BlockPos position)
    {
        if (fightInProgress)
            return;

        summonPos = position;
        tickCounter = 0;
        fightInProgress = true;
    }
}