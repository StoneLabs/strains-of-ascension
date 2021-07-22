package net.stone_labs.strainsofascension.mixin;

import net.minecraft.block.BellBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.explosion.Explosion;
import net.stone_labs.strainsofascension.entities.VexBossEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Random;

@Mixin(BellBlock.class)
abstract class BellBlockMixin
{
    private Random random = new Random();

    @Inject(at = @At("RETURN"), method = "onUse")
    private void init(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit, CallbackInfoReturnable<ActionResult> info)
    {
        if (info.getReturnValue() == ActionResult.PASS)
            return;

        if (world.getRegistryKey() != World.OVERWORLD)
            return;

        for (int x = -2; x <= 2; x++)
            for (int y = -1; y <= 1; y++)
                for (int z = -2; z <= 2; z++)
                {
                    BlockPos spawner = pos.add(x, y, z);
                    if (world.getBlockState(spawner).isOf(Blocks.SPAWNER))
                    {
                        if (random.nextFloat() < 0.1f)
                        {
                            new VexBossEntity((ServerWorld) world, spawner);
                            world.createExplosion(null, spawner.getX() + 0.5, spawner.getY() + 0.5, spawner.getZ() + 0.5, 1, Explosion.DestructionType.BREAK);
                            world.breakBlock(spawner, false);

                            world.createExplosion(null, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, 1, Explosion.DestructionType.BREAK);
                            world.breakBlock(pos, false);
                        }

                        return;
                    }
                }
    }
}
