package net.stone_labs.strainsofascension.effects;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.LiteralText;
import net.stone_labs.strainsofascension.artifacts.ArtifactState;
import net.stone_labs.strainsofascension.artifacts.Artifacts;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class DepthMendingArtifact extends Strain
{
    public DepthMendingArtifact()
    {
        super(20);
    }

    Random random = new Random();

    @Override
    public void effect(ServerPlayerEntity player, byte layer, ArtifactState artifactState)
    {
        int power = artifactState.GetPower(Artifacts.DEPTH_MENDING);
        if (layer >= 4)
        {
            if (random.nextFloat() < 0.05 * artifactState.GetPower(Artifacts.DEPTH_MENDING))
                return;

            List<ItemStack> stacks = new ArrayList<>();

            for (ItemStack stack : player.getInventory().armor)
                if (stack.isDamageable())
                    stacks.add(stack);

            if (power == Artifacts.DEPTH_MENDING.MAX_VALUE)
            {
                for (ItemStack stack : player.getInventory().offHand)
                    if (stack.isDamageable())
                        stacks.add(stack);

                if (player.getInventory().getMainHandStack().isDamageable())
                    stacks.add(player.getInventory().getMainHandStack());
            }

            if (stacks.size() == 0)
                return;

            ItemStack repairTarget = stacks.get(random.nextInt(stacks.size()));
            repairTarget.setDamage(repairTarget.getDamage() - 1);
        }
    }
}
