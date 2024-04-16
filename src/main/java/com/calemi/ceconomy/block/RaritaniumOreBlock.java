package com.calemi.ceconomy.block;

import com.calemi.ccore.api.location.Location;
import com.calemi.ccore.api.sound.SoundHelper;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.ExperienceDroppingBlock;
import net.minecraft.block.MapColor;
import net.minecraft.block.enums.Instrument;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.intprovider.UniformIntProvider;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;

public class RaritaniumOreBlock extends ExperienceDroppingBlock {

    public RaritaniumOreBlock(float hardness, BlockSoundGroup soundGroup, MapColor mapColor) {
        super(FabricBlockSettings.create().mapColor(mapColor).instrument(Instrument.BASEDRUM).requiresTool().strength(hardness, 3.0F).sounds(soundGroup), UniformIntProvider.create(0, 3));
    }

    @Override
    public void onBroken(WorldAccess world, BlockPos pos, BlockState state) {
        SoundHelper.playBlockBreak(new Location((World)world, pos), Blocks.AMETHYST_BLOCK.getDefaultState());
        super.onBroken(world, pos, state);
    }
}
