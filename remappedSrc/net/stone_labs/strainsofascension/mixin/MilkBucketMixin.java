package net.stone_labs.strainsofascension.mixin;

import net.minecraft.entity.LivingEntity;
import net.minecraft.item.MilkBucketItem;
import net.minecraft.server.network.ServerPlayerEntity;
import net.stone_labs.strainsofascension.effects.strains.NightVisionStrain;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(MilkBucketItem.class)
abstract class MilkBucketMixin
{

    @Redirect(method = "finishUsing", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;clearStatusEffects()Z"))
    private boolean injectedClearStatusEffects(LivingEntity player)
    {
        if (player.isPlayer())
            return NightVisionStrain.ClearAllExceptNV((ServerPlayerEntity) player);
        else
            return player.clearStatusEffects();
    }
}
