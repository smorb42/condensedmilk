package net.smorb42.condensedmilk.item;


import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.smorb42.condensedmilk.CondensedMilk;
import net.smorb42.condensedmilk.fluid.ModFluids;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, CondensedMilk.MOD_ID);


    public static final RegistryObject<Item> CONDENSED_LAVA_BUCKET = ITEMS.register("condensed_lava_bucket",
            () -> new CondensedLavaBucket(ModFluids.CONDENSED_LAVA,
                    new Item.Properties().tab(CreativeModeTab.TAB_MISC).rarity(Rarity.UNCOMMON).stacksTo(1)));
    public static void register(IEventBus eventBus){
        ITEMS.register(eventBus);
    }
}
