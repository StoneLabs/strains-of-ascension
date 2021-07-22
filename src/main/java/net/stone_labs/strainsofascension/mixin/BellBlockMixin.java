package net.stone_labs.strainsofascension.mixin;

import net.minecraft.block.BellBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.FlintAndSteelItem;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.item.MilkBucketItem;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.LiteralText;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.stone_labs.strainsofascension.effects.BlindnessStrain;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BellBlock.class)
abstract class BellBlockMixin
{
    @Inject(at = @At("RETURN"), method = "onUse")
    private void init(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit, CallbackInfoReturnable<ActionResult> info)
    {
        if (info.getReturnValue() == ActionResult.PASS)
            return;

        for (int x = -2; x <= 2; x++)
            for (int y = -1; y <= 1; y++)
                for (int z = -2; z <= 2; z++)
                    if (world.getBlockState(pos.add(x, y, z)).isOf(Blocks.SPAWNER))
                    {
                        player.sendMessage(new LiteralText("Test"), false);
                        return;
                    }
    }
}
