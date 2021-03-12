package net.stone_labs.strainsofascension.mixin;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.item.MilkBucketItem;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MilkBucketItem.class)
abstract class MilkBucketMixin
{
    @Redirect(method = "finishUsing", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;clearStatusEffects()Z"))
    private boolean injectedClearStatusEffects(LivingEntity player)
    {
        @Nullable StatusEffectInstance nvEffect = player.getStatusEffect(StatusEffects.NIGHT_VISION);
        boolean ret = player.clearStatusEffects();

        if (nvEffect != null)
           player.addStatusEffect(nvEffect);

        return ret;
    }
}
