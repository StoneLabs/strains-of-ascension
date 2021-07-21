package net.stone_labs.strainsofascension;

import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.boss.BossBar;
import net.minecraft.entity.boss.ServerBossBar;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.VexEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.TeleportCommand;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.LiteralText;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Random;

public class VexBoss extends VexEntity
{
    final int SUMMON_DISTANCE = 10;
    final int PLATFORM_HEIGHT = 300;
    final int TELEPORT_HEIGHT = 306;
    final int SPAWN_HEIGHT = 306;

    boolean fightInProgress = false;
    long tickCounter = 0;

    boolean spawnCompleted = false;
    ServerWorld serverWorld;

    BlockPos summonPos;
    ServerBossBar bossBar;

    Random random = new Random();

    public VexBoss(ServerWorld serverWorld, BlockPos summonPos)
    {
        super(EntityType.VEX, serverWorld);

        this.serverWorld = serverWorld;
        this.summonPos = summonPos;

        this.bossBar = new ServerBossBar(new LiteralText("ANCIENT VEX"), BossBar.Color.PINK, BossBar.Style.NOTCHED_20);

        BlockPos spawnPosition = this.summonPos.withY(SPAWN_HEIGHT);
        this.refreshPositionAndAngles(spawnPosition, 0.0F, 0.0F);
        this.initialize(serverWorld, serverWorld.getLocalDifficulty(spawnPosition), SpawnReason.MOB_SUMMONED, null, null);
        this.setBounds(spawnPosition);
        this.setLifeTicks(Integer.MAX_VALUE);
        this.getAttributeInstance(EntityAttributes.GENERIC_MAX_HEALTH).setBaseValue(200);
        this.getAttributeInstance(EntityAttributes.GENERIC_ATTACK_DAMAGE).setBaseValue(15);
        this.setInvisible(false);

        serverWorld.spawnEntity(this);
    }

    @Override
    public void tick()
    {
        serverWorld.getServer().sendSystemMessage(new LiteralText(this.getPos().toString()), null);
        bossBar.setPercent(this.getHealth() / this.getMaxHealth());

        // Summon phase
        if (tickCounter == 0)
            CreatePlatform(this.serverWorld);

        if (tickCounter == 5)
            for (ServerPlayerEntity player : this.serverWorld.getPlayers())
                player.sendMessage(new LiteralText("The ground begins to rumble..."), false);

        if (tickCounter > 5 && tickCounter < 75)
            SpawnSummoningParticles();

        if (tickCounter < 75 && tickCounter % 10 == 0)
            for (ServerPlayerEntity player : this.serverWorld.getPlayers())
                player.playSound(SoundEvents.ENTITY_VEX_AMBIENT, SoundCategory.HOSTILE, 100.0f, 0.1f);

        if (tickCounter == 80)
        {
            TeleportPlayer();
            fightInProgress = true;
        }

        tickCounter++;

        // Fighting Stage
        if (!fightInProgress)
            return;

        if (!spawnCompleted)
        {
            heal(1);
            if (this.getHealth() == this.getMaxHealth())
                spawnCompleted = true;

            return;
        }
        super.tick();

        if (this.getHealth() < this.getMaxHealth() / 2)
            RandomizePlatform();
    }

    private void SpawnSummoningParticles()
    {
        for (int i = 0; i < 200; i++)
        {
            double distance = Math.cbrt(random.nextDouble()) * SUMMON_DISTANCE;
            double theta = random.nextDouble() * 2 * Math.PI;
            double phi = Math.acos(2.0 * random.nextDouble() - 1.0);

            double x = distance * Math.cos(theta) * Math.sin(phi);
            double z = distance * Math.sin(theta) * Math.sin(phi);
            double y = distance * Math.cos(phi);

            this.serverWorld.spawnParticles(ParticleTypes.GLOW, summonPos.getX() + x, summonPos.getY() + y, summonPos.getZ() + z, 1, 0, 0, 0, 1);
        }
    }

    private void RandomizePlatform()
    {
        for (int x = -SUMMON_DISTANCE; x <= SUMMON_DISTANCE; x++)
            for (int z = -SUMMON_DISTANCE; z <= SUMMON_DISTANCE; z++)
            {
                BlockPos pos = new BlockPos(summonPos.getX() + x, PLATFORM_HEIGHT, summonPos.getZ() + z);
                if (pos.isWithinDistance(summonPos.withY(PLATFORM_HEIGHT), SUMMON_DISTANCE))
                {
                    if (random.nextFloat() > 0.1f)
                        continue;

                    if (this.serverWorld.isAir(pos))
                    {
                        if (random.nextFloat() < (1.0f / 10.0f))
                            this.serverWorld.setBlockState(pos, Blocks.BLACK_STAINED_GLASS.getDefaultState());
                    }
                    else if (this.serverWorld.getBlockState(pos).isOf(Blocks.BLACK_STAINED_GLASS))
                    {
                        if (random.nextFloat() < (1.0f / 100.0f))
                            this.serverWorld.setBlockState(pos, Blocks.RED_STAINED_GLASS.getDefaultState());
                    }
                    else if (this.serverWorld.getBlockState(pos).isOf(Blocks.RED_STAINED_GLASS))
                    {
                        if (random.nextFloat() < (1.0f / 4.0f))
                            this.serverWorld.removeBlock(pos, false);
                    }
                }
            }
    }

    private void CreatePlatform(World world)
    {
        for (int x = -SUMMON_DISTANCE; x <= SUMMON_DISTANCE; x++)
            for (int z = -SUMMON_DISTANCE; z <= SUMMON_DISTANCE; z++)
            {
                BlockPos pos = new BlockPos(summonPos.getX() + x, PLATFORM_HEIGHT, summonPos.getZ() + z);
                if (pos.isWithinDistance(summonPos.withY(PLATFORM_HEIGHT), SUMMON_DISTANCE))
                    world.setBlockState(pos, Blocks.BLACK_STAINED_GLASS.getDefaultState());
            }
    }

    private void TeleportPlayer()
    {
        for (ServerPlayerEntity player : serverWorld.getPlayers())
            if (summonPos.isWithinDistance(player.getBlockPos(), SUMMON_DISTANCE))
            {
                serverWorld.spawnParticles(ParticleTypes.POOF, player.getPos().x, player.getPos().y, player.getPos().z, 50, 0, 0, 0, 1);
                player.teleport(player.getPos().x, TELEPORT_HEIGHT, player.getPos().z);

                player.playSound(SoundEvents.ENTITY_ENDERMAN_TELEPORT, SoundCategory.HOSTILE, 100.0f, 1f);
                bossBar.addPlayer(player);
            }
    }

    @Override
    public void onDeath(DamageSource source)
    {
        super.onDeath(source);
        bossBar.clearPlayers();

        if (source.getAttacker() == null)
            return;

        if (source.getAttacker().isPlayer())
            ((ServerPlayerEntity)source.getAttacker()).sendMessage(new LiteralText("test"), false);
    }
}