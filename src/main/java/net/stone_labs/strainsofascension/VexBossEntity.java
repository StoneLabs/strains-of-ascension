package net.stone_labs.strainsofascension;

import net.minecraft.block.BlockState;
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
import net.minecraft.world.explosion.Explosion;

import java.util.Random;

public class VexBossEntity extends VexEntity
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

    public VexBossEntity(ServerWorld serverWorld, BlockPos summonPos)
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

        serverWorld.spawnEntity(this);
    }

    @Override
    public void tick()
    {
        super.tick();
        bossBar.setPercent(this.getHealth() / this.getMaxHealth());

        // Summon phase
        if (tickCounter == 0)
        {
            CreatePlatform();
            this.setInvulnerable(true);
            this.setAiDisabled(true);
        }

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

        DestroyCheatBlocks();
        if (!spawnCompleted)
        {
            heal(1);
            if (this.getHealth() == this.getMaxHealth())
            {
                this.setAiDisabled(false);
                this.setInvulnerable(false);
                spawnCompleted = true;
            }

            return;
        }

        if (bossBar.getPlayers().size() == 0)
            kill();

        if (this.world.getPlayers().stream().noneMatch(player -> player.distanceTo(this) < 50))
            kill();

        if (this.getHealth() <= 0)
            return;

        if (this.getHealth() < this.getMaxHealth() * 0.75 && this.getHealth() > this.getMaxHealth() * 0.33)
        {
            if (tickCounter % 100 == 0)
                CreatePlatform();
            if (tickCounter % 100 == 30)
                MarkRandomPlatformPart();
            if (tickCounter % 100 == 70)
                DestroyRedPlatform();
        }

        if (this.getHealth() < this.getMaxHealth() * 0.33)
            RandomizePlatform();
    }

    private void MarkRandomPlatformPart()
    {
        int lowerXLim = -SUMMON_DISTANCE;
        int lowerZLim = -SUMMON_DISTANCE;
        int upperXLim = SUMMON_DISTANCE;
        int upperZLim = SUMMON_DISTANCE;
        int lowerDLim = 0;
        int upperDLim = SUMMON_DISTANCE + 1;

        switch (random.nextInt(10))
        {
            case 0 -> lowerXLim = 0;
            case 1 -> lowerZLim = 0;
            case 2 -> upperXLim = 0;
            case 3 -> upperZLim = 0;
            case 4 -> {
                lowerXLim = 0;
                lowerZLim = 0;
            }
            case 5 -> {
                lowerXLim = 0;
                upperZLim = 0;
            }
            case 6 -> {
                upperXLim = 0;
                lowerZLim = 0;
            }
            case 7 -> {
                upperXLim = 0;
                upperZLim = 0;
            }
            case 8 -> upperDLim = SUMMON_DISTANCE / 2;
            case 9 -> lowerDLim = SUMMON_DISTANCE / 2;
        }

        for (int x = lowerXLim; x <= upperXLim; x++)
            for (int z = lowerZLim; z <= upperZLim; z++)
            {
                BlockPos pos = new BlockPos(summonPos.getX() + x, PLATFORM_HEIGHT, summonPos.getZ() + z);
                double distance = pos.getSquaredDistance(summonPos.withY(PLATFORM_HEIGHT));

                if (lowerDLim * lowerDLim < distance && upperDLim * upperDLim > distance)
                    if (this.serverWorld.getBlockState(pos).isOf(Blocks.BLACK_STAINED_GLASS))
                        this.serverWorld.setBlockState(pos, Blocks.RED_STAINED_GLASS.getDefaultState());
            }
    }

    private void DestroyCheatBlocks()
    {
        int XZDistance = SUMMON_DISTANCE + 3;
        int YDistance = 5;
        for (int x = -XZDistance; x <= XZDistance; x++)
            for (int y = -1; y <= YDistance; y++)
                for (int z = -XZDistance; z <= XZDistance; z++)
                {
                    BlockPos pos = new BlockPos(summonPos.getX() + x, PLATFORM_HEIGHT + y, summonPos.getZ() + z);
                    BlockState state = this.serverWorld.getBlockState(pos);
                    if (!state.isAir() &&
                        !state.isOf(Blocks.BLACK_STAINED_GLASS) &&
                        !state.isOf(Blocks.RED_STAINED_GLASS))
                    {
                        this.serverWorld.createExplosion(this, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, 1, Explosion.DestructionType.BREAK);
                        this.serverWorld.removeBlock(pos, false);
                    }
                }
    }

    private void DestroyRedPlatform()
    {
        for (int x = -SUMMON_DISTANCE; x <= SUMMON_DISTANCE; x++)
            for (int z = -SUMMON_DISTANCE; z <= SUMMON_DISTANCE; z++)
            {
                BlockPos pos = new BlockPos(summonPos.getX() + x, PLATFORM_HEIGHT, summonPos.getZ() + z);
                if (pos.isWithinDistance(summonPos.withY(PLATFORM_HEIGHT), SUMMON_DISTANCE))
                    if (this.serverWorld.getBlockState(pos).isOf(Blocks.RED_STAINED_GLASS))
                        this.serverWorld.removeBlock(pos, false);
            }
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

    private void CreatePlatform()
    {
        for (int x = -SUMMON_DISTANCE; x <= SUMMON_DISTANCE; x++)
            for (int z = -SUMMON_DISTANCE; z <= SUMMON_DISTANCE; z++)
            {
                BlockPos pos = new BlockPos(summonPos.getX() + x, PLATFORM_HEIGHT, summonPos.getZ() + z);
                if (pos.isWithinDistance(summonPos.withY(PLATFORM_HEIGHT), SUMMON_DISTANCE))
                    this.serverWorld.setBlockState(pos, Blocks.BLACK_STAINED_GLASS.getDefaultState());
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