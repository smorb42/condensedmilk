package net.smorb42.condensedmilk.block;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.smorb42.condensedmilk.CondensedMilk;
import net.smorb42.condensedmilk.fluid.ModFluids;
import net.smorb42.condensedmilk.item.ModItems;

import java.util.function.Supplier;
import java.util.function.ToIntFunction;


public class ModBlocks {
    public static final DeferredRegister<Block> BLOCKS =
            DeferredRegister.create(ForgeRegistries.BLOCKS, CondensedMilk.MOD_ID);

  public static ToIntFunction<BlockState> lightLevel = BlockState -> 15;
    public static final RegistryObject<LiquidBlock> CONDENSED_LAVA = BLOCKS.register("condensed_lava",
            () -> new CondensedLavaBlock(ModFluids.CONDENSED_LAVA, BlockBehaviour.Properties.of(Material.LAVA).lightLevel(lightLevel)
                    .noCollission().strength(100.0F).noDrops()));


    private static <T extends Block> RegistryObject<T> registerBlock(String name, Supplier<T> block, CreativeModeTab tab) {
        RegistryObject<T> toReturn = BLOCKS.register(name, block);
        registerBlockItem(name, toReturn, tab);
        return toReturn;
    }

    private static <T extends Block> RegistryObject<Item> registerBlockItem(String name, RegistryObject<T> block,
                                                                            CreativeModeTab tab) {
        return ModItems.ITEMS.register(name, () -> new BlockItem(block.get(),
                new Item.Properties().tab(tab)));
    }

    public static void register(IEventBus eventBus) {
        BLOCKS.register(eventBus);
    }

}
