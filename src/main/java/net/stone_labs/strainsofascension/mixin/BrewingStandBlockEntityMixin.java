package net.stone_labs.strainsofascension.mixin;

import net.minecraft.block.entity.BrewingStandBlockEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtElement;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.explosion.Explosion;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BrewingStandBlockEntity.class)
abstract class BrewingStandBlockEntityMixin
{
    @Inject(at = @At("HEAD"), method = "craft()V")
    private static void init(World world, BlockPos pos, DefaultedList<ItemStack> slots, CallbackInfo info)
    {
        float power = 0;

        for (ItemStack stack : slots)
        {
            if (!stack.hasTag())
                continue;

            //noinspection ConstantConditions
            if (!stack.getTag().contains("strainArtifact", NbtElement.STRING_TYPE))
                continue;

            power += stack.getTag().getInt("strainArtifactPower");
        }

        if (power > 0)
        {
            world.breakBlock(pos, true);
            world.createExplosion(null, pos.getX(), pos.getY(), pos.getZ(), power, Explosion.DestructionType.DESTROY);
        }
    }
}
