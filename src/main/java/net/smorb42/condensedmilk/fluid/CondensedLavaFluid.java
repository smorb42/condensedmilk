package net.smorb42.condensedmilk.fluid;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraftforge.fluids.FluidAttributes;
import net.minecraftforge.fluids.ForgeFlowingFluid;
import net.smorb42.condensedmilk.CondensedMilk;
import net.smorb42.condensedmilk.block.ModBlocks;
import net.smorb42.condensedmilk.item.ModItems;

import javax.annotation.Nullable;
import java.awt.*;
import java.util.Optional;
import java.util.Random;

public abstract class CondensedLavaFluid extends ForgeFlowingFluid {

    protected CondensedLavaFluid() {
        super(new ForgeFlowingFluid.Properties(
                ModFluids.CONDENSED_LAVA,
                ModFluids.CONDENSED_LAVA_FLOW,
                FluidAttributes.builder(
                                new ResourceLocation(CondensedMilk.MOD_ID, "fluid/condensed_lava_still"),
                                new ResourceLocation(CondensedMilk.MOD_ID, "fluid/condensed_lava_flow")
                        )
                        .translationKey("fluid." + CondensedMilk.MOD_ID + ".condensed_lava").sound(SoundEvents.BUCKET_FILL_LAVA,SoundEvents.BUCKET_EMPTY_LAVA)
                        .color(new Color(255, 255, 255, 255).getRGB()).temperature(600).luminosity(15)
                        .viscosity(3000))
                .block(ModBlocks.CONDENSED_LAVA)
                .bucket(ModItems.CONDENSED_LAVA_BUCKET));
    }


    //@Override
    //public Optional<SoundEvent> getPickupSound() {
    //    return Optional.of(SoundEvents.BUCKET_FILL_LAVA);
    //}

    @Override
    public void animateTick(Level pLevel, BlockPos pPos, FluidState pState, Random pRandom) {
        BlockPos blockpos = pPos.above();
        if (pLevel.getBlockState(blockpos).isAir() && !pLevel.getBlockState(blockpos).isSolidRender(pLevel, blockpos)) {
            if (pRandom.nextInt(100) == 0) {
                double d0 = (double)pPos.getX() + pRandom.nextDouble();
                double d1 = (double)pPos.getY() + 1.0D;
                double d2 = (double)pPos.getZ() + pRandom.nextDouble();
                pLevel.addParticle(ParticleTypes.LAVA, d0, d1, d2, 0.0D, 0.0D, 0.0D);
                pLevel.playLocalSound(d0, d1, d2, SoundEvents.LAVA_POP, SoundSource.BLOCKS, 0.2F + pRandom.nextFloat() * 0.2F, 0.9F + pRandom.nextFloat() * 0.15F, false);
            }

            if (pRandom.nextInt(200) == 0) {
                pLevel.playLocalSound((double)pPos.getX(), (double)pPos.getY(), (double)pPos.getZ(), SoundEvents.LAVA_AMBIENT, SoundSource.BLOCKS, 0.2F + pRandom.nextFloat() * 0.2F, 0.9F + pRandom.nextFloat() * 0.15F, false);
            }
        }

    }

    @Override
    public void randomTick(Level pLevel, BlockPos pPos, FluidState pState, Random pRandom) {
        if (pLevel.getGameRules().getBoolean(GameRules.RULE_DOFIRETICK)) {
            int i = pRandom.nextInt(3);
            if (i > 0) {
                BlockPos blockpos = pPos;

                for(int j = 0; j < i; ++j) {
                    blockpos = blockpos.offset(pRandom.nextInt(3) - 1, 1, pRandom.nextInt(3) - 1);
                    if (!pLevel.isLoaded(blockpos)) {
                        return;
                    }

                    BlockState blockstate = pLevel.getBlockState(blockpos);
                    if (blockstate.isAir()) {
                        if (this.hasFlammableNeighbours(pLevel, blockpos)) {
                            pLevel.setBlockAndUpdate(blockpos, net.minecraftforge.event.ForgeEventFactory.fireFluidPlaceBlockEvent(pLevel, blockpos, pPos, Blocks.FIRE.defaultBlockState()));
                            return;
                        }
                    } else if (blockstate.getMaterial().blocksMotion()) {
                        return;
                    }
                }
            } else {
                for(int k = 0; k < 3; ++k) {
                    BlockPos blockpos1 = pPos.offset(pRandom.nextInt(3) - 1, 0, pRandom.nextInt(3) - 1);
                    if (!pLevel.isLoaded(blockpos1)) {
                        return;
                    }

                    if (pLevel.isEmptyBlock(blockpos1.above()) && this.isFlammable(pLevel, blockpos1, Direction.UP)) {
                        pLevel.setBlockAndUpdate(blockpos1.above(), net.minecraftforge.event.ForgeEventFactory.fireFluidPlaceBlockEvent(pLevel, blockpos1.above(), pPos, Blocks.FIRE.defaultBlockState()));
                    }
                }
            }

        }
    }

    private boolean hasFlammableNeighbours(LevelReader pLevel, BlockPos pPos) {
        for(Direction direction : Direction.values()) {
            if (this.isFlammable(pLevel, pPos.relative(direction), direction.getOpposite())) {
                return true;
            }
        }

        return false;
    }

    private boolean isFlammable(LevelReader level, BlockPos pos, Direction face) {
        return pos.getY() >= level.getMinBuildHeight() && pos.getY() < level.getMaxBuildHeight() && !level.hasChunkAt(pos) ? false : level.getBlockState(pos).isFlammable(level, pos, face);
    }

    @Override
    public int getTickDelay(LevelReader pLevel) {
        return pLevel.dimensionType().ultraWarm() ? 10 : 30;
    }

    @Override
    public int getSpreadDelay(Level pLevel, BlockPos pPos, FluidState p_76205_, FluidState p_76206_) {
        int i = this.getTickDelay(pLevel);
        if (!p_76205_.isEmpty() && !p_76206_.isEmpty() && !p_76205_.getValue(FALLING) && !p_76206_.getValue(FALLING) && p_76206_.getHeight(pLevel, pPos) > p_76205_.getHeight(pLevel, pPos) && pLevel.getRandom().nextInt(4) != 0) {
            i *= 4;
        }

        return i;
    }

    @Override
    protected boolean canConvertToSource() {
        return false;
    }

    @Override
    public int getDropOff(LevelReader pLevel) {
        return pLevel.dimensionType().ultraWarm() ? 1 : 2;
    }

    @Override
    public int getSlopeFindDistance(LevelReader pLevel) {
        return pLevel.dimensionType().ultraWarm() ? 4 : 2;
    }

    @Override
    @Nullable
    public ParticleOptions getDripParticle() {
        return ParticleTypes.DRIPPING_LAVA;
    }

    @Override
    protected void beforeDestroyingBlock(LevelAccessor pLevel, BlockPos pPos, BlockState pState) {
        fizz(pLevel, pPos);
    }

    private void fizz(LevelAccessor pLevel, BlockPos pPos) {
        pLevel.levelEvent(1501, pPos, 0);
    }
    public static class Flowing extends CondensedLavaFluid {
        public Flowing() {
            registerDefaultState(getStateDefinition().any().setValue(LEVEL, 7));
        }

        @Override
        protected void createFluidStateDefinition(StateDefinition.Builder<Fluid, FluidState> p_76476_) {
            super.createFluidStateDefinition(p_76476_);
            p_76476_.add(LEVEL);
        }

        @Override
        public int getAmount(FluidState p_76480_) {
            return p_76480_.getValue(LEVEL);
        }

        @Override
        public boolean isSource(FluidState p_76478_) {
            return false;
        }
    }

    public static class Source extends CondensedLavaFluid {

        @Override
        public int getAmount(FluidState p_76485_) {
            return 8;
        }

        @Override
        public boolean isSource(FluidState p_76483_) {
            return true;
        }
    }
}