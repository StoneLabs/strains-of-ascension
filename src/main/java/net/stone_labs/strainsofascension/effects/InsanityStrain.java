package net.stone_labs.strainsofascension.effects;

import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.text.LiteralText;
import net.stone_labs.strainsofascension.artifacts.ArtifactState;

import java.util.Random;

import static net.stone_labs.strainsofascension.StrainData.horrorMessages;
import static net.stone_labs.strainsofascension.StrainData.horrorSounds;

public class InsanityStrain extends Strain
{
    public InsanityStrain()
    {
        super(1);
    }

    Random random = new Random();

    @Override
    public void effect(ServerPlayerEntity player, byte layer, ArtifactState artifactState)
    {
        if (layer == 9)
        {
            if (random.nextFloat() < 0.0032f)
                playRandomHorrorMessage(player, random);
            if (random.nextFloat() < 0.0064f)
                playRandomHorrorAudio(player, random);
        }
        else
        {
            if (player.getPos().y < -32 && random.nextFloat() < ((-0.0001f) * player.getPos().y - 0.0032f))
                playRandomHorrorMessage(player, random);
            if (player.getPos().y < -32 && random.nextFloat() < ((-0.0002f) * player.getPos().y - 0.0064f))
                playRandomHorrorAudio(player, random);
        }
    }

    public static void playRandomHorrorMessage(ServerPlayerEntity player, Random random)
    {
        player.sendMessage(horrorMessages.get(random.nextInt(horrorMessages.size())), false);
    }

    public static void playRandomHorrorAudio(ServerPlayerEntity player, Random random)
    {
        player.playSound(horrorSounds.get(random.nextInt(horrorSounds.size())), SoundCategory.AMBIENT, random.nextFloat(), 0);
    }
}