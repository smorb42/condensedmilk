package net.smorb42.condensedmilk.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.EmptyFluid;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.Optional;
import java.util.function.Supplier;

public class CondensedLavaBlock extends LiquidBlock {
    public CondensedLavaBlock(Supplier<? extends FlowingFluid> supplier, Properties properties) {
        super(supplier, properties);
    }

    @Override
    public VoxelShape getCollisionShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        return pContext.isAbove(STABLE_SHAPE, pPos, true) && pState.getValue(LEVEL) == 0 && pContext.canStandOnFluid(pLevel.getFluidState(pPos.above()), pState.getFluidState()) ? STABLE_SHAPE : Shapes.empty();
    }

    @Override
    public boolean isPathfindable(BlockState p_54704_, BlockGetter p_54705_, BlockPos p_54706_, PathComputationType p_54707_) {
        return !getFluid().is(FluidTags.LAVA);
    }

    @Override
    public boolean skipRendering(BlockState pState, BlockState pAdjacentBlockState, Direction pSide) {
        return pAdjacentBlockState.getFluidState().getType().isSame(getFluid());
    }

    @Override
    public void onPlace(BlockState pState, Level pLevel, BlockPos pPos, BlockState pOldState, boolean pIsMoving) {
        if (this.reactWithNeighbors(pLevel, pPos, pState)) {
            pLevel.scheduleTick(pPos, pState.getFluidState().getType(), getFluid().getTickDelay(pLevel));
        }

    }

    @Override
    public BlockState updateShape(BlockState pState, Direction pFacing, BlockState pFacingState, LevelAccessor pLevel, BlockPos pCurrentPos, BlockPos pFacingPos) {
        if (pState.getFluidState().isSource() || pFacingState.getFluidState().isSource()) {
            pLevel.scheduleTick(pCurrentPos, pState.getFluidState().getType(), getFluid().getTickDelay(pLevel));
        }

        return super.updateShape(pState, pFacing, pFacingState, pLevel, pCurrentPos, pFacingPos);
    }

    @Override
    public void neighborChanged(BlockState pState, Level pLevel, BlockPos pPos, Block pBlock, BlockPos pFromPos, boolean pIsMoving) {
        if (this.reactWithNeighbors(pLevel, pPos, pState)) {
            pLevel.scheduleTick(pPos, pState.getFluidState().getType(), getFluid().getTickDelay(pLevel));
        }

    }


    private boolean reactWithNeighbors(Level world, BlockPos pos, BlockState state) {
        for (Direction dir : Direction.values()) {
            FluidState otherState = world.getFluidState(pos.offset(new Vec3i(dir.getStepX(),dir.getStepY(),dir.getStepZ())));
            Fluid otherFluid = otherState.getType();
            if (otherFluid instanceof FlowingFluid) {
                otherFluid = ((FlowingFluid) otherFluid).getSource();
            }
            if (otherFluid instanceof EmptyFluid || otherFluid.equals(this.getFluid())) {
                continue;
            }

            BlockState generate;
            boolean isHot = otherFluid.getAttributes().getTemperature(world, pos.offset(new Vec3i(dir.getStepX(),dir.getStepY(),dir.getStepZ()))) > 600;
            if (isHot) {
                //if (CraftingConfig.CONFIG.liquidStarlightInteractionSand.get()) {
                    generate = Blocks.NETHERRACK.defaultBlockState();
                    //if (CraftingConfig.CONFIG.liquidStarlightInteractionAquamarine.get() && world.rand.nextInt(800) == 0) {
                    //    generate = BlocksAS.AQUAMARINE_SAND_ORE.getDefaultState();
                   // }
               // } else {
                //    generate = Blocks.COBBLESTONE.getDefaultState();
               // }
            } else {
                //if (CraftingConfig.CONFIG.liquidStarlightInteractionIce.get()) {
                    generate = Blocks.COBBLED_DEEPSLATE.defaultBlockState();
                //} else {
                //    generate = Blocks.COBBLESTONE.getDefaultState();
                //}
            }
            fizz(world, pos.offset(new Vec3i(dir.getStepX(),dir.getStepY(),dir.getStepZ())));
            world.setBlock(pos.offset(new Vec3i(dir.getStepX(),dir.getStepY(),dir.getStepZ())), generate,2);
        }
        return true;
    }


    private void fizz(LevelAccessor pLevel, BlockPos pPos) {
        pLevel.levelEvent(1501, pPos, 0);
    }


    @Override
    public ItemStack pickupBlock(LevelAccessor pLevel, BlockPos pPos, BlockState pState) {
        if (pState.getValue(LEVEL) == 0) {
            pLevel.setBlock(pPos, Blocks.AIR.defaultBlockState(), 11);
            return new ItemStack(getFluid().getBucket());
        } else {
            return ItemStack.EMPTY;
        }
    }

    @Override
    public Optional<SoundEvent> getPickupSound() {
        return getFluid().getPickupSound();
    }
}
